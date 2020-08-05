package com.example.boostitvendor.PushNotification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.boostitvendor.MainActivity;
import com.example.boostitvendor.StaffSection.OrdersPage;
import com.example.boostitvendor.R;

public class Notificationhelper {
    public static void sendNotifications(Context context,String title,String body) {

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent =PendingIntent.getActivity(
                context,
                100,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, OrdersPage.CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(context);
        mNotificationMgr.notify(1, mBuilder.build());
    }
}

