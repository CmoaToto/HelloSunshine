package fr.cmoatoto.hellosunshine;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by CmoaToto on 07/05/14.
 */
public class SoundUtils {

    private static final String TAG = SoundUtils.class.getName();

    public static interface SoundListener {
        public void onSoundEnd();
    }

    public static void playSound(Context c, final SoundListener listener) {
        try {
            final AudioManager audioManager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
            final int lastVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int volume = SharedPrefUtils.getSoundVolume(c);
            if (volume == -1) {
                volume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2;
                SharedPrefUtils.setSoundVolume(c, volume);
            }
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);

            AssetFileDescriptor afd = c.getAssets().openFd("hello.ogg");
            final MediaPlayer player = new MediaPlayer();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    player.reset();
                    player.release();
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, lastVolume, 0);
                    if (listener != null) {
                        listener.onSoundEnd();
                    }
                }
            });
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Toast mVolumeToast;

    public static void volumeLess(final Context c) {
        AudioManager audioManager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        int volume = SharedPrefUtils.getSoundVolume(c);
        if (volume == -1) {
            volume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2;
        }
        volume = Math.max(0, volume - (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 10));
        SharedPrefUtils.setSoundVolume(c, volume);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Volume: " + volume);
        }

        if (mVolumeToast != null) {
            mVolumeToast.cancel();
        }

        mVolumeToast = Toast.makeText(c, c.getResources().getString(R.string.sound_showvalue, (int) volume), Toast.LENGTH_SHORT);
        mVolumeToast.show();
    }

    public static void volumeMore(final Context c) {
        AudioManager audioManager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        int volume = SharedPrefUtils.getSoundVolume(c);
        if (volume == -1) {
            volume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2;
        }
        volume = Math.min(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                volume + (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 10));
        SharedPrefUtils.setSoundVolume(c, volume);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Volume: " + volume);
        }

        if (mVolumeToast != null) {
            mVolumeToast.cancel();
        }

        mVolumeToast = Toast.makeText(c, c.getResources().getString(R.string.sound_showvalue, (int) volume), Toast.LENGTH_SHORT);
        mVolumeToast.show();
    }
}
