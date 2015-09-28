package fr.cmoatoto.hellosunshine;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class SunshineService extends Service implements SensorEventListener {

    private static final String TAG = SunshineService.class.getName();

    public static final String STOPSELF_KEY = TAG + ".stopself";

    private SensorManager mSensorManager;

    private boolean isPlaying = false;

    private long lastHello = System.currentTimeMillis();

    private State mState = State.AWAKE;

    private long mLastTimeOfSleep = System.currentTimeMillis();

    private float lastValue = 0;

    private static final int MIN_DELAY_BETWEEN_HELLO = 5000;

    private static final int VALUE_TO_WAKE_UP = 2;

    private static final int MIN_DELAY_BEFORE_SLEEP = 5000;

    private enum State {
        SLEEPING, AWAKE
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor lightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        mState = State.AWAKE;

        if (lightSensor != null) {
            Log.d(TAG, "Sensor.TYPE_LIGHT Available :)");
            mSensorManager.registerListener(
                    this,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);

            NotificationHelper.showCallNotification(this);
        } else {
            Log.d(TAG, "Sensor.TYPE_LIGHT NOT Available :(");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.hasExtra(STOPSELF_KEY)) {
                stopSelf();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mSensorManager.unregisterListener(this);
        NotificationHelper.hideCallNotification(this);
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float newValue = event.values[0];
            if (lastValue == newValue) {
                return;
            }
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "LIGHT: " + newValue);
            }
            if (isShining(newValue)) {
                helloSunshine();
            }
            if (newValue >= VALUE_TO_WAKE_UP) {
                mState = State.AWAKE;
            } else if (newValue == 0) {
                mState = State.SLEEPING;
                mLastTimeOfSleep = System.currentTimeMillis();
            }
            lastValue = newValue;
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "State : " + mState.name());
            }
        }
    }

    private boolean isShining(float newValue) {
        if (mState == State.AWAKE || newValue < VALUE_TO_WAKE_UP || mLastTimeOfSleep + MIN_DELAY_BEFORE_SLEEP > System.currentTimeMillis()) {
            return false;
        }
        return true;
    }

    private void helloSunshine() {
        if (isPlaying || lastHello + MIN_DELAY_BETWEEN_HELLO > System.currentTimeMillis()) {
            return;
        }
        Log.d(TAG, "Hello Sunshine !");

        isPlaying = true;
        SoundUtils.playSound(this, new SoundUtils.SoundListener() {
            @Override
            public void onSoundEnd() {
                lastHello = System.currentTimeMillis();
                isPlaying = false;
            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
