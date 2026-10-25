package uk.org.fancy.AndroidTvHomeKit.Philips;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import uk.org.fancy.AndroidTvHomeKit.HomeKitService;
import uk.org.fancy.AndroidTvHomeKit.TelevisionInterface;
import uk.org.fancy.AndroidTvHomeKit.Philips.xtv.XTvHttp;
import io.github.hapjava.Service;

public class Television implements TelevisionInterface,
        TelevisionInterface.TelevisionSpeakerInterface,
        TelevisionInterface.RemoteInterface,
        TelevisionInterface.AdditionalServices {
    private static final String TAG = "HomeKit:Television";
    public static final String PREFS_NAME = "homekit_prefs";
    public static final String PREF_XTV_USER = "xtv_username";
    public static final String PREF_XTV_PASS = "xtv_password";
    private static final String DEFAULT_USER = "";
    private static final String DEFAULT_PASS = "";

    public final HomeKitService service;
    public final XTvHttp xtvhttp;
    private final PowerState powerStateManager;
    private final InputSourceManager inputSourceManager;
    private final TelevisionSpeaker speaker;
    private String name = "Philips Android TV";
    private final String manufacturer = "Philips";
    private String model = Build.MODEL != null ? Build.MODEL : "Philips TV";
    private String serialNumber = "YK1A2234440381";
    private String firmwareRevision = getSystemProperty("ro.tpvision.product.swversion", Build.DISPLAY);

    public Television(HomeKitService _service) {
        service = _service;
        SharedPreferences prefs = service.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String user = prefs.getString(PREF_XTV_USER, DEFAULT_USER);
        String pass = prefs.getString(PREF_XTV_PASS, DEFAULT_PASS);
        xtvhttp = new XTvHttp(user, pass);
        powerStateManager = new PowerState(this);
        inputSourceManager = new InputSourceManager(this);
        speaker = new TelevisionSpeaker(this);

        loadSystemDetails();
    }

    public void saveCredentials(String username, String password) {
        xtvhttp.setUser(username, password);
        SharedPreferences prefs = service.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit()
            .putString(PREF_XTV_USER, username)
            .putString(PREF_XTV_PASS, password)
            .apply();
        loadSystemDetails();
    }

    public void loadSystemDetails() {
        try {
            XTvHttp.System system = xtvhttp.getSystem().get(5, TimeUnit.SECONDS);

            if (system.name != null && !system.name.isEmpty()) name = system.name;
            if (system.model != null && !system.model.isEmpty()) model = system.model;
            if (system.serialNumber != null && !system.serialNumber.isEmpty()) serialNumber = system.serialNumber;
            if (system.softwareVersion != null && !system.softwareVersion.isEmpty()) {
                firmwareRevision = system.softwareVersion;
            }
        } catch (Exception err) {
            Log.w(TAG, "Using fallback system details (XTv /system error: " + err.toString() + ")");
        }
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public static String getSystemProperty(String property) {
        return getSystemProperty(property, null);
    }

    public static String getSystemProperty(String property, String _default) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            String val = (String) get.invoke(c, property, _default);
            return (val != null && !val.isEmpty()) ? val : _default;
        } catch (Exception e) {
            return _default;
        }
    }

    public String getFirmwareRevision() {
        return firmwareRevision != null ? firmwareRevision : "1.0.0";
    }

    public List<Service> getAdditionalServices() {
        List<Service> services = new LinkedList<Service>();

        Service ambilight = new uk.org.fancy.AndroidTvHomeKit.Philips.Ambilight.Service(this);
        services.add(ambilight);

        return Collections.unmodifiableList(services);
    }

    public PowerState getPowerStateManager() {
        return powerStateManager;
    }

    public InputSourceManager getInputSourceManager() {
        return inputSourceManager;
    }

    public TelevisionSpeaker getSpeaker() {
        return speaker;
    }

    public CompletableFuture<Object> keypress(String key) {
        return xtvhttp.keypress(key);
    }

    public void sendRemoteKey(RemoteKey hapkey) {
        if (hapkey == null) return;
        Log.i(TAG, "Send remote key " + hapkey.toString());

        String key = getXTvRemoteKey(hapkey);
        if (key == null) {
            Log.e(TAG, "Invalid remote key " + hapkey.toString());
            return;
        }

        keypress(key);
    }

    private String getXTvRemoteKey(RemoteKey key) {
        switch (key) {
            default:
                return null;
            case REWIND:
                return XTvHttp.RemoteKey.REWIND;
            case FAST_FORWARD:
                return XTvHttp.RemoteKey.FAST_FORWARD;
            case ARROW_UP:
                return XTvHttp.RemoteKey.CURSOR_UP;
            case ARROW_DOWN:
                return XTvHttp.RemoteKey.CURSOR_DOWN;
            case ARROW_LEFT:
                return XTvHttp.RemoteKey.CURSOR_LEFT;
            case ARROW_RIGHT:
                return XTvHttp.RemoteKey.CURSOR_RIGHT;
            case SELECT:
                return XTvHttp.RemoteKey.CONFIRM;
            case BACK:
                return XTvHttp.RemoteKey.BACK;
            case EXIT:
                return XTvHttp.RemoteKey.EXIT;
            case PLAY_PAUSE:
                return XTvHttp.RemoteKey.PLAY_PAUSE;
            case INFORMATION:
                return XTvHttp.RemoteKey.INFO;
        }
    }
}
