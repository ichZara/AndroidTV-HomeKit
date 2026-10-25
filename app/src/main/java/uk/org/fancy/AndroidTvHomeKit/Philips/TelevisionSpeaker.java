package uk.org.fancy.AndroidTvHomeKit.Philips;

import java.util.concurrent.CompletableFuture;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import uk.org.fancy.AndroidTvHomeKit.TelevisionSpeakerInterface;
import uk.org.fancy.AndroidTvHomeKit.PollThread;
import uk.org.fancy.AndroidTvHomeKit.Philips.xtv.XTvHttp;
import io.github.hapjava.HomekitCharacteristicChangeCallback;

public class TelevisionSpeaker implements TelevisionSpeakerInterface,
        TelevisionSpeakerInterface.RelativeVolumeInterface,
        TelevisionSpeakerInterface.AbsoluteVolumeInterface {
    private static final String TAG = "HomeKit:TelevisionSpeaker";
    private final Television television;
    private int MINIMUM_VOLUME = 0;
    private int MAXIMUM_VOLUME = 60;

    private HomekitCharacteristicChangeCallback muteCallback = null;
    private boolean lastMuteState = false;
    private PollThread.PollInterface pollMute = new PollThread.PollInterface() {
        public void poll() throws Exception {
            boolean muteState = lastMuteState;
            boolean mute = getMute().get();

            if (mute != muteState) {
                Log.i(TAG, "Mute state changed; now " + mute);
                if (muteCallback != null) muteCallback.changed();
                lastMuteState = mute;
            }
        }
    };

    private HomekitCharacteristicChangeCallback volumeCallback = null;
    private int lastVolume = 0;
    private PollThread.PollInterface pollVolume = new PollThread.PollInterface() {
        public void poll() throws Exception {
            int oldVolume = lastVolume;
            int volume = getVolume().get();

            if (volume != oldVolume) {
                Log.i(TAG, "Volume changed; now " + volume);
                if (volumeCallback != null) volumeCallback.changed();
                lastVolume = volume;
            }
        }
    };

    public TelevisionSpeaker(Television _television) {
        television = _television;
    }

    public CompletableFuture<Boolean> getMute() {
        return television.xtvhttp.getMute().exceptionally(err -> {
            try {
                AudioManager am = (AudioManager) television.service.getSystemService(Context.AUDIO_SERVICE);
                if (am != null) return am.isStreamMute(AudioManager.STREAM_MUSIC);
            } catch (Exception ignored) {
            }
            return lastMuteState;
        }).thenApply(mute -> {
            lastMuteState = mute;
            return mute;
        });
    }

    public void setMute(boolean mute) {
        Log.i(TAG, "Set mute: " + (mute ? "true" : "false"));
        television.xtvhttp.setMute(mute);
    }

    public void onMuteSubscribe(HomekitCharacteristicChangeCallback callback) {
        muteCallback = callback;
        television.service.pollThread.add(pollMute);
    }

    public void onMuteUnsubscribe() {
        television.service.pollThread.remove(pollMute);
        muteCallback = null;
    }

    public VolumeControlType getVolumeControlType() {
        return VolumeControlType.ABSOLUTE;
    }

    public void incrementVolume() {
        television.keypress(XTvHttp.RemoteKey.VOLUME_UP);
    }

    public void decrementVolume() {
        television.keypress(XTvHttp.RemoteKey.VOLUME_DOWN);
    }

    public CompletableFuture<Integer> getVolume() {
        return television.xtvhttp.getAudioSettings().thenApply(audio -> {
            MINIMUM_VOLUME = audio.MINIMUM_VOLUME;
            MAXIMUM_VOLUME = audio.MAXIMUM_VOLUME > 0 ? audio.MAXIMUM_VOLUME : 60;

            int v = (int) (((((float) audio.volume - MINIMUM_VOLUME) / MAXIMUM_VOLUME) * 100) + MINIMUM_VOLUME);
            if (v < 0) v = 0;
            if (v > 100) v = 100;

            lastVolume = v;
            return v;
        }).exceptionally(err -> {
            try {
                AudioManager am = (AudioManager) television.service.getSystemService(Context.AUDIO_SERVICE);
                if (am != null) {
                    int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    int cur = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                    if (max > 0) return (cur * 100) / max;
                }
            } catch (Exception ignored) {
            }
            return lastVolume;
        });
    }

    public void setVolume(int volume) {
        int v = (int) (((((float) volume - MINIMUM_VOLUME) / 100) * MAXIMUM_VOLUME) + MINIMUM_VOLUME);
        Log.i(TAG, "Set volume: " + Integer.toString(volume) + "; " + Integer.toString(v));
        television.xtvhttp.setVolume(v);
    }

    public void onVolumeSubscribe(HomekitCharacteristicChangeCallback callback) {
        volumeCallback = callback;
        television.service.pollThread.add(pollVolume);
    }

    public void onVolumeUnsubscribe() {
        television.service.pollThread.remove(pollVolume);
        volumeCallback = null;
    }
}
