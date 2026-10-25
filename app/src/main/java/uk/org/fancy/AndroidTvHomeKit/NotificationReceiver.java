package uk.org.fancy.AndroidTvHomeKit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    public static final String TAG = "HomeKit:HomeKitService";

    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Starting HomeKitService on boot");
        Intent serviceIntent = new Intent(context, HomeKitService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }
}
