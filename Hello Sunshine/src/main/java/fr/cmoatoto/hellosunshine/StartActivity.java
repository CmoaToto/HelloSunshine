package fr.cmoatoto.hellosunshine;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import java.io.IOException;


public class StartActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = SunshineService.class.getName();

    private Button mStartStopButton;

    private Button mPlaySoundButton;

    private ImageButton mVolumeLessButton;

    private ImageButton mVolumeMoreButton;

    private CheckBox mStartOnBootCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "start in DEBUG mode");
        }
        setContentView(R.layout.activity_start);

        mStartStopButton = (Button) findViewById(R.id.activity_start_startstop_button);
        mPlaySoundButton = (Button) findViewById(R.id.activity_start_playsound_button);
        mVolumeLessButton = (ImageButton) findViewById(R.id.activity_start_volumeless_button);
        mVolumeMoreButton = (ImageButton) findViewById(R.id.activity_start_volumemore_button);
        mStartOnBootCheckbox = (CheckBox) findViewById(R.id.activity_start_startonboot_checkbox);

        if (isMyServiceRunning()) {
            mStartStopButton.setText(R.string.activity_start_stop);
        }

        mStartOnBootCheckbox.setChecked(SharedPrefUtils.isStartOnBoot(this));

        mStartStopButton.setOnClickListener(this);
        mPlaySoundButton.setOnClickListener(this);
        mVolumeLessButton.setOnClickListener(this);
        mVolumeMoreButton.setOnClickListener(this);

        mStartOnBootCheckbox.setOnCheckedChangeListener(this);
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SunshineService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_start_startstop_button:
                if (isMyServiceRunning()) {
                    stopService(new Intent(this, SunshineService.class));
                    mStartStopButton.setText(R.string.activity_start_start);
                } else {
                    startService(new Intent(this, SunshineService.class));
                    mStartStopButton.setText(R.string.activity_start_stop);
                }
                break;
            case R.id.activity_start_playsound_button:
                SoundUtils.playSound(this, null);
                break;
            case R.id.activity_start_volumeless_button:
                SoundUtils.volumeLess(this);
                break;
            case R.id.activity_start_volumemore_button:
                SoundUtils.volumeMore(this);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.activity_start_startonboot_checkbox:
                SharedPrefUtils.setStartOnBoot(this, isChecked);
        }
    }
}
