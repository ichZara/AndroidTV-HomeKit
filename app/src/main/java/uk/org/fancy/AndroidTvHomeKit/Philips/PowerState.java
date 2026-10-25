package uk.org.fancy.AndroidTvHomeKit.Philips;

import java.util.concurrent.TimeUnit;
import android.content.Context;
import android.os.PowerManager;
import android.util.Log;
import uk.org.fancy.AndroidTvHomeKit.PowerStateInterface;
import uk.org.fancy.AndroidTvHomeKit.PollThread;
import uk.org.fancy.AndroidTvHomeKit.Philips.xtv.XTvHttp;
import io.github.hapjava.HomekitCharacteristicChangeCallback;

public class PowerState implements PowerStateInterface, PollThread.PollInterface {
    private static final String TAG = "HomeKit:PowerState";
    private final Television television;
    private HomekitCharacteristicChangeCallback callback = null;
    private boolean lastPowerState = false;

    public PowerState(Television _television) {
        television = _television;
    }

    public boolean getPowerState() {
        try {
            XTvHttp.PowerState state = television.xtvhttp.getPowerState().get(3, TimeUnit.SECONDS);
            return state == XTvHttp.PowerState.ON;
        } catch (Exception e) {
            try {
                PowerManager pm = (PowerManager) television.service.getSystemService(Context.POWER_SERVICE);
                if (pm != null) return pm.isInteractive();
            } catch (Exception ignored) {
            }
            Log.i(TAG, "Error getting power state: " + e.toString());
            return false;
        }
    }

    public void setPowerState(boolean on) {
        Log.i(TAG, "Set power state " + on);

        television.xtvhttp.setPowerState(on ? XTvHttp.PowerState.ON : XTvHttp.PowerState.STANDBY);
    }

    public void onSubscribe(HomekitCharacteristicChangeCallback _callback) {
        callback = _callback;
        lastPowerState = getPowerState();
        television.service.pollThread.add(this);
    }

    public void onUnsubscribe() {
        television.service.pollThread.remove(this);
        callback = null;
    }

    public void poll() {
        boolean powerState = getPowerState();

        if (powerState != lastPowerState) {
            Log.i(TAG, "Power state changed to " + powerState);
            if (callback != null) callback.changed();
            lastPowerState = powerState;
        }
    }
}
