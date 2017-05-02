package fr.cmoatoto.hellosunshine;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;


/**
 * Created by CmoaToto on 06/05/14.
 */
public class NotificationHelper {

    public static final int SUNSHINE_NOTIFICATION_KEY = 4242;

    public static void showCallNotification(Service s) {

        // Creates an explicit intent for StartActivity
        Intent launchAppIntent = new Intent(s, StartActivity.class);
        PendingIntent launchAppPendingIntent = PendingIntent.getActivity(s, 0, launchAppIntent, 0);

        // Creates an explicit intent to Stop SunshineService
        Intent stopIntent = new Intent(s, SunshineService.class);
        stopIntent.putExtra(SunshineService.STOPSELF_KEY, true);
        PendingIntent stopPendingIntent = PendingIntent.getService(s, 0, stopIntent, 0);

        Notification.Builder mBuilder =
                new Notification.Builder(s)
                        .setSmallIcon(R.drawable.ic_notif)
                        .setContentTitle(s.getString(R.string.notif_title))
                        .setContentText(s.getString(R.string.notif_detail))
                        .setContentIntent(launchAppPendingIntent)
                        .setAutoCancel(false)
                        .addAction(R.drawable.ic_content_remove, s.getString(R.string.notif_stop), stopPendingIntent)
                        .setOngoing(true);

        NotificationManager mNotificationManager =
                (NotificationManager) s.getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(SUNSHINE_NOTIFICATION_KEY, mBuilder.build());
    }

    public static void hideCallNotification(Service s) {
        NotificationManager mNotificationManager =
                (NotificationManager) s.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(SUNSHINE_NOTIFICATION_KEY);
    }
}
