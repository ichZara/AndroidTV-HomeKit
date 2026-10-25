package uk.org.fancy.AndroidTvHomeKit.Philips;

import android.util.Log;
import uk.org.fancy.AndroidTvHomeKit.ApplicationInputSourceInterface;
import uk.org.fancy.AndroidTvHomeKit.Philips.xtv.XTvHttp;

public class XTvApplicationInputSource extends InputSource implements ApplicationInputSourceInterface {
    private static final String TAG = "HomeKit:XTvApplicationInputSource";
    private final InputSourceManager inputSourceManager;
    public final XTvHttp.Application application;

    public XTvApplicationInputSource(InputSourceManager _inputSourceManager, XTvHttp.Application _application) {
        super(_inputSourceManager);

        inputSourceManager = _inputSourceManager;
        application = _application;
    }

    public String getId() {
        return "XTvApplication:" + getApplicationId();
    }

    public String getApplicationId() {
        return application.id;
    }

    public String getName() {
        return application.label;
    }

    public InputSourceTypes getInputSourceType() {
        if ("org.droidtv.channels".equals(application.intent.activity.packageName)) {
            return InputSourceTypes.OTHER;
        } else if ("org.droidtv.contentexplorer".equals(application.intent.activity.packageName)) {
            return InputSourceTypes.USB;
        } else {
            return InputSourceTypes.APPLICATION;
        }
    }

    public boolean isConfigured() {
        return true;
    }

    public boolean getCurrentVisibilityState() {
        return true;
    }

    public String getConfiguredName() {
        return getName();
    }

    public void setConfiguredName(String name) {
        Log.i(TAG, "Set configured name " + getName() + " to " + name);
    }

    public void activate() {
        Log.i(TAG, "Activate XTvApplication " + getName());

        inputSourceManager.television.xtvhttp.launch(application.intent);
    }
}
