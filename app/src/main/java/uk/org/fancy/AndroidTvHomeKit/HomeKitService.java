package uk.org.fancy.AndroidTvHomeKit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import io.github.hapjava.HomekitServer;
import io.github.hapjava.HomekitStandaloneAccessoryServer;
import io.github.hapjava.impl.HomekitRegistryState;

public class HomeKitService extends Service {
    public static final String HOMEKIT_VERSION = "0.2.0";
    private static final String TAG = "HomeKit:HomeKitService";
    private static final String CHANNEL_ID = "homekit_service_channel";
    public static final int PORT = 9123;
    private static WakeLock wakeLock = null;
    private static WifiManager.MulticastLock multicastLock = null;
    private HomekitServer homekit;
    private AuthInfo authInfo;
    private Advertiser advertiser;
    private HomekitStandaloneAccessoryServer accessoryServer;
    private volatile boolean started = false;
    public static HomeKitService instance;
    public TelevisionInterface implementation;
    private Runnable stateListener;

    private TelevisionInterface getImplementation() {
        return new uk.org.fancy.AndroidTvHomeKit.Philips.Television(this);
    }

    public PollThread pollThread = new PollThread();

    private void startHomekitServerAsync() {
        new Thread(() -> {
            try {
                InetAddress address = InetAddress.getByAddress(new byte[]{0, 0, 0, 0});
                homekit = new HomekitServer(address, PORT);
                TelevisionAccessory accessory = new TelevisionAccessory(HomeKitService.this);
                advertiser = new Advertiser((NsdManager) getSystemService(Context.NSD_SERVICE), 31 /* Television */);
                if (authInfo.isPaired()) advertiser.setDiscoverable(false);
                accessoryServer = homekit.createStandaloneAccessory(authInfo, accessory, advertiser);

                accessoryServer.getRoot().getRegistry().setAllowUnauthenticatedRequests(true);
            } catch (Exception e) {
                Log.e(TAG, "Error creating accessory", e);
                return;
            }

            HomekitRegistryState oldRegistryState = null;

            try {
                Log.i(TAG, "Loading registry state");
                FileInputStream fileInputStream = getApplicationContext().openFileInput("registry-state.bin");
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                try {
                    Log.i(TAG, "Restoring registry state");
                    HomekitRegistryState registryState = (HomekitRegistryState) objectInputStream.readObject();
                    accessoryServer.getRoot().getRegistry().reset(registryState);
                    HomekitRegistryState newRegistryState = accessoryServer.getRoot().getRegistry().saveIds(registryState);
                    if (registryState != newRegistryState) oldRegistryState = registryState;
                } finally {
                    objectInputStream.close();
                }
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
                Log.i(TAG, "Error reading registry state");
            } catch (ClassNotFoundException e) {
                Log.i(TAG, "ClassNotFoundException reading registry state, creating new auth data");
            }

            try {
                Log.i(TAG, "Writing registry state");
                FileOutputStream fileOutputStream = getApplicationContext().openFileOutput("registry-state.bin", 0);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(accessoryServer.getRoot().getRegistry().saveIds(oldRegistryState));
                objectOutputStream.flush();
                objectOutputStream.close();
            } catch (IOException e) {
                Log.e(TAG, "Error writing registry state", e);
            }

            Log.i(TAG, "Starting HAP server");

            try {
                accessoryServer.start();
                started = true;
                Log.i(TAG, "Started HAP server on port " + PORT);
                notifyStateChanged();
            } catch (Exception e) {
                Log.e(TAG, "Failed to start HAP server", e);
            }
        }, "HomeKitServerThread").start();
    }

    public HomeKitService() throws IOException, UnknownHostException {}

    public AuthInfo getAuthInfo() {
        return authInfo;
    }

    public Advertiser getAdvertiser() {
        return advertiser;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStateListener(Runnable listener) {
        this.stateListener = listener;
    }

    private void notifyStateChanged() {
        if (stateListener != null) {
            stateListener.run();
        }
    }

    public void resetHomeKitPairings() {
        try {
            getApplicationContext().deleteFile("auth-state.bin");
            getApplicationContext().deleteFile("registry-state.bin");
            if (authInfo != null) {
                authInfo.resetPairings();
            }
            if (advertiser != null && authInfo != null) {
                advertiser.updateMacAndDiscoverable(authInfo.getMac(), true);
            }
            notifyStateChanged();
        } catch (Exception e) {
            Log.e(TAG, "Error resetting HomeKit pairings", e);
        }
    }

    private void startAsForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (nm != null) {
                NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Apple HomeKit Servisi",
                    NotificationManager.IMPORTANCE_LOW
                );
                nm.createNotificationChannel(channel);
            }
            Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Apple HomeKit TV Servisi")
                .setContentText("HomeKit sunucusu aktif (Port " + PORT + ")")
                .setSmallIcon(android.R.drawable.stat_sys_data_bluetooth)
                .build();
            startForeground(1, notification);
        }
    }

    public void onCreate() {
        instance = this;
        Log.i(TAG, "onCreate");

        startAsForeground();

        implementation = getImplementation();

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (pm != null && wakeLock == null) {
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
            wakeLock.acquire();
            Log.i(TAG, "Acquired wake lock");
        }

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wm != null && multicastLock == null) {
            multicastLock = wm.createMulticastLock("HomeKitMulticastLock");
            multicastLock.setReferenceCounted(false);
            multicastLock.acquire();
            Log.i(TAG, "Acquired multicast lock");
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        startAsForeground();

        if (started) {
            Log.i(TAG, "Not starting as the service has already been started");
            notifyStateChanged();
            return START_STICKY;
        }

        Log.i(TAG, "onStartCommand");

        try {
            try {
                FileInputStream fileInputStream = getApplicationContext().openFileInput("auth-state.bin");
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                try {
                    Log.i(TAG, "Using saved auth data");
                    AuthState authState = (AuthState) objectInputStream.readObject();
                    authInfo = new AuthInfo(authState);
                } finally {
                    objectInputStream.close();
                }
            } catch (Exception e) {
                Log.i(TAG, "Creating new auth data");
                authInfo = new AuthInfo();
            }

            authInfo.onChange(state -> {
                try {
                    Log.i(TAG, "State has changed! Writing");
                    FileOutputStream fileOutputStream = getApplicationContext().openFileOutput("auth-state.bin", 0);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(state);
                    objectOutputStream.flush();
                    objectOutputStream.close();
                    if (advertiser != null) {
                        advertiser.setDiscoverable(!authInfo.isPaired());
                    }
                    notifyStateChanged();
                } catch (IOException e) {
                    Log.e(TAG, "Error saving auth state", e);
                }
            });

            startHomekitServerAsync();
            if (!pollThread.isAlive()) {
                pollThread = new PollThread();
                pollThread.start();
            }
        } catch (InvalidAlgorithmParameterException e) {
            Log.e(TAG, "InvalidAlgorithmParameterException starting server", e);
        }

        return START_STICKY;
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onDestroy() {
        Log.i(TAG, "onDestroy");

        if (accessoryServer != null) {
            try {
                accessoryServer.stop();
            } catch (Exception ignored) {
            }
            accessoryServer = null;
            started = false;
        }

        if (advertiser != null) {
            advertiser.stop();
            advertiser = null;
        }

        pollThread.setShouldStop();

        if (multicastLock != null && multicastLock.isHeld()) {
            multicastLock.release();
            multicastLock = null;
        }

        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
            wakeLock = null;
        }
        instance = null;
    }
}
