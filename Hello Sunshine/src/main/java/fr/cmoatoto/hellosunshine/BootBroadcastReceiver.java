package fr.cmoatoto.hellosunshine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SharedPrefUtils.isStartOnBoot(context)) {
            Intent startServiceIntent = new Intent(context, SunshineService.class);
            context.startService(startServiceIntent);
        }
    }
}
