package uk.org.fancy.AndroidTvHomeKit.Philips;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.LinkedList;
import java.util.List;
import android.media.tv.TvInputManager;
import android.media.tv.TvInputInfo;
import android.util.Log;
import uk.org.fancy.AndroidTvHomeKit.PollThread;
import uk.org.fancy.AndroidTvHomeKit.InputSourceManagerInterface;
import uk.org.fancy.AndroidTvHomeKit.InputSourceInterface;
import uk.org.fancy.AndroidTvHomeKit.Philips.xtv.XTvHttp;
import io.github.hapjava.HomekitCharacteristicChangeCallback;

public class InputSourceManager implements InputSourceManagerInterface, PollThread.PollInterface {
    private static final String TAG = "HomeKit:InputSourceManager";
    public final Television television;
    private final Collection<InputSourceInterface> inputSources = new LinkedList<InputSourceInterface>();
    private final HomeScreenInputSource homeScreenInputSource;
    private final Collection<XTvApplicationInputSource> xtvApplicationInputSources = new LinkedList<XTvApplicationInputSource>();
    private final Collection<TIFInputSource> tifInputSources = new LinkedList<TIFInputSource>();
    private HomekitCharacteristicChangeCallback callback = null;
    private InputSourceInterface lastInputSource = null;

    public InputSourceManager(Television _television) {
        television = _television;

        homeScreenInputSource = new HomeScreenInputSource(this);
        inputSources.add(homeScreenInputSource);

        TvInputManager tvInputManager = (TvInputManager) television.service.getSystemService("tv_input");
        if (tvInputManager != null) {
            List<TvInputInfo> tifInputs = tvInputManager.getTvInputList();

            for (TvInputInfo tvInputInfo: tifInputs) {
                String id = tvInputInfo.getId();
                if (tvInputInfo.isHidden(television.service)) {
                    continue;
                }
                if (tvInputInfo.getType() == TvInputInfo.TYPE_HDMI && tvInputInfo.getParentId() != null) {
                    continue;
                }
                if (tvInputInfo.getType() != TvInputInfo.TYPE_HDMI &&
                    !id.contains("TunerInputService/HW0")) {
                    continue;
                }

                Log.i(TAG, "Registering TIF input source " + id);

                TIFInputSource tifInputSource = new TIFInputSource(this, tvInputInfo);
                tifInputSources.add(tifInputSource);
                inputSources.add(tifInputSource);
            }
        }

        try {
            List<XTvHttp.Application> xtvApplicationInputs = television.xtvhttp.getApplications().get(5, TimeUnit.SECONDS);

            for (XTvHttp.Application application: xtvApplicationInputs) {
                String pkg = application.intent.activity.packageName;
                if (pkg == null) continue;
                if (pkg.startsWith("org.droidtv.") && !pkg.startsWith("org.droidtv.nettvapp")) continue;
                if (pkg.equals("com.android.tv.settings") ||
                    pkg.equals("com.android.vending") ||
                    pkg.equals("com.google.android.play.games") ||
                    pkg.equals("com.google.android.videos") ||
                    pkg.equals("io.cleanyourtv.ui") ||
                    pkg.equals("uk.org.fancy.AndroidTvHomeKit")) {
                    continue;
                }

                Log.i(TAG, "Registering XTv Application input source " + application.label + " (" + pkg + ")");

                XTvApplicationInputSource xtvApplicationInputSource = new XTvApplicationInputSource(this, application);
                xtvApplicationInputSources.add(xtvApplicationInputSource);
                inputSources.add(xtvApplicationInputSource);
            }
        } catch (Exception err) {
            Log.w(TAG, "Could not load XTv applications at startup: " + err.toString());
        }
    }

    public Collection<InputSourceInterface> getInputSources() {
        return inputSources;
    }

    private String getCurrentInputSourceId() {
        return television.getSystemProperty("persist.sys.inputid");
    }

    public CompletableFuture<InputSourceInterface> getActiveInput() {
        return television.xtvhttp.getCurrentActivity().thenApply(activity -> {
            if (activity == null || activity.packageName == null) {
                return homeScreenInputSource;
            }

            if (activity.packageName.contentEquals("com.google.android.tvlauncher") ||
                activity.packageName.contentEquals("io.cleanyourtv.ui")) {
                return homeScreenInputSource;
            }

            if (activity.packageName.contentEquals("com.droidtv.channels") ||
                activity.packageName.contentEquals("com.droidtv.settings") ||
                activity.packageName.contentEquals("com.android.tv.settings") ||
                activity.packageName.contentEquals("org.droidtv.philipstvdrawer") ||
                activity.packageName.contentEquals("android") ||
                activity.packageName.contentEquals("com.google.android.tv.remote.service") ||
                activity.packageName.contentEquals("com.google.android.apps.mediashell") ||
                activity.packageName.contentEquals("com.google.android.tvrecommendations")
            ) {
                return lastInputSource != null ? lastInputSource : homeScreenInputSource;
            }

            if (activity.packageName.contentEquals("org.droidtv.playtv") &&
                activity.className != null &&
                activity.className.contentEquals("org.droidtv.playtv.PlayTvActivity")
            ) {
                String inputid = getCurrentInputSourceId();

                for (TIFInputSource inputSource: tifInputSources) {
                    if (!inputSource.tvInputInfo.getId().equals(inputid)) continue;
                    return inputSource;
                }
            }

            List<XTvApplicationInputSource> matchingApplicationInputSources = new LinkedList<XTvApplicationInputSource>();
            for (XTvApplicationInputSource inputSource: xtvApplicationInputSources) {
                if (inputSource.application.intent.activity.packageName.contentEquals(activity.packageName)) {
                    if (activity.className != null &&
                        inputSource.application.intent.activity.className != null &&
                        inputSource.application.intent.activity.className.contentEquals(activity.className)) {
                        return inputSource;
                    }
                    matchingApplicationInputSources.add(inputSource);
                }
            }

            if (matchingApplicationInputSources.size() == 1) return matchingApplicationInputSources.get(0);

            return homeScreenInputSource;
        }).exceptionally(err -> {
            return lastInputSource != null ? lastInputSource : homeScreenInputSource;
        }).thenApply(inputSource -> {
            lastInputSource = inputSource;
            return inputSource;
        });
    }

    public void setActiveInput(InputSourceInterface inputSource) {
        inputSource.activate();
    }

    public void onSubscribe(HomekitCharacteristicChangeCallback _callback) {
        callback = _callback;
        television.service.pollThread.add(this);
    }

    public void onUnsubscribe() {
        television.service.pollThread.remove(this);
        callback = null;
    }

    public void poll() throws Exception {
        InputSourceInterface prev = this.lastInputSource;
        InputSourceInterface inputSource = getActiveInput().get();

        if (inputSource != prev) {
            Log.i(TAG, "Input source changed; now " + inputSource.getId());
            if (callback != null) callback.changed();
            this.lastInputSource = inputSource;
        }
    }
}
