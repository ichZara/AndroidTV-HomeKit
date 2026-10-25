package uk.org.fancy.AndroidTvHomeKit;

import java.io.IOException;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdManager.RegistrationListener;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import io.github.hapjava.HomekitAdvertiser;

public class Advertiser implements HomekitAdvertiser {
    private static final String TAG = "HomeKit:Advertiser";

    private static final String SERVICE_TYPE = "_hap._tcp";

    private boolean discoverable = true;
    private boolean isAdvertising = false;

    private final NsdManager nsdManager;
    public int category = 31;
    private String label;
    private String mac;
    private int port;
    private int configurationIndex;
    private String registeredServiceName = null;

    private final RegistrationListener registrationListener = new RegistrationListener() {
        @Override
        public void onServiceRegistered(NsdServiceInfo nsdServiceInfo) {
            registeredServiceName = nsdServiceInfo.getServiceName();
            Log.i(TAG, "Service registered with name " + registeredServiceName);
        }

        @Override
        public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            Log.e(TAG, "Service registration failed with code " + Integer.toString(errorCode));
        }

        @Override
        public void onServiceUnregistered(NsdServiceInfo arg0) {
            Log.i(TAG, "Service unregistered");
            registeredServiceName = null;

            if (isAdvertising) {
                try {
                    registerService();
                } catch (IOException e) {
                    Log.e(TAG, "IOException registering service", e);
                }
            }
        }

        @Override
        public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            Log.e(TAG, "Service unregistration failed with code " + Integer.toString(errorCode));
        }
    };

    public Advertiser(NsdManager _nsdManager, int _category) {
        nsdManager = _nsdManager;
        category = _category;
    }

    public Advertiser(NsdManager nsdManager) {
        this(nsdManager, 31);
    }

    public boolean isDiscoverable() {
        return discoverable;
    }

    public String getRegisteredServiceName() {
        return registeredServiceName;
    }

    public synchronized void advertise(String label, String mac, int port, int configurationIndex) throws Exception {
        if (isAdvertising) {
            throw new IllegalStateException("Homekit advertiser is already running");
        }

        this.label = label;
        this.mac = mac;
        this.port = port;
        this.configurationIndex = configurationIndex;

        Log.i(TAG, "Advertising accessory " + label + " (" + mac + ") on port " + port);

        registerService();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Log.i(TAG, "Stopping advertising in response to shutdown.");
            stop();
        }));

        isAdvertising = true;
    }

    public synchronized void stop() {
        isAdvertising = false;
        try {
            nsdManager.unregisterService(registrationListener);
        } catch (Exception ignored) {
        }
    }

    public synchronized void setDiscoverable(boolean discoverable) throws IOException {
        if (this.discoverable == discoverable) return;

        this.discoverable = discoverable;

        if (isAdvertising) {
            Log.i(TAG, "Re-creating service due to change in discoverability to " + discoverable);
            try {
                nsdManager.unregisterService(registrationListener);
            } catch (Exception ignored) {
            }
        }
    }

    public synchronized void updateMacAndDiscoverable(String newMac, boolean discoverable) {
        this.mac = newMac;
        this.discoverable = discoverable;
        if (isAdvertising) {
            Log.i(TAG, "Re-creating service with new MAC " + newMac + " (discoverable=" + discoverable + ")");
            try {
                nsdManager.unregisterService(registrationListener);
            } catch (Exception ignored) {
            }
        }
    }

    public synchronized void setConfigurationIndex(int revision) throws IOException {
        if (this.configurationIndex == revision) return;

        this.configurationIndex = revision;

        if (isAdvertising) {
            Log.i(TAG, "Re-creating service due to change in configuration index to " + revision);
            try {
                nsdManager.unregisterService(registrationListener);
            } catch (Exception ignored) {
            }
        }
    }

    private void registerService() throws IOException {
        Log.i(TAG, "Registering " + SERVICE_TYPE + " on port " + port);

        NsdServiceInfo serviceInfo = new NsdServiceInfo();

        String cleanName = (label == null || label.trim().isEmpty()) ? "Android TV" : label.replace("/", " ").trim();
        String upperMac = mac != null ? mac.toUpperCase(java.util.Locale.US) : "";
        String suffix = upperMac.length() >= 5 ? upperMac.substring(upperMac.length() - 5).replace(":", "") : "TV";
        serviceInfo.setServiceName(cleanName + " " + suffix);
        serviceInfo.setServiceType(SERVICE_TYPE);
        serviceInfo.setPort(port);
        serviceInfo.setAttribute("sf", discoverable ? "1" : "0");
        serviceInfo.setAttribute("id", upperMac);
        serviceInfo.setAttribute("md", cleanName);
        serviceInfo.setAttribute("pv", "1.1");
        serviceInfo.setAttribute("c#", Integer.toString(configurationIndex));
        serviceInfo.setAttribute("s#", "1");
        serviceInfo.setAttribute("ff", "0");
        serviceInfo.setAttribute("ci", Integer.toString(category));
        serviceInfo.setAttribute("sh", HomeKitSetupPayload.generateSetupHash(HomeKitSetupPayload.DEFAULT_SETUP_ID, upperMac));

        nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener);
    }
}
