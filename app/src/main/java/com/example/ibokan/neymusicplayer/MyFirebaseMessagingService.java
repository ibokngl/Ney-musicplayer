package com.example.ibokan.neymusicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by ibokan on 28.12.2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            // Data mesaj� i�eriyor mu
            //Uygulama arkaplanda veya �n planda olmas� farketmez. Her zaman �a��r�lacakt�r.
            //Gelen i�erik json format�ndad�r.
            Log.d(TAG, "Mesaj data i�eri�i: " + remoteMessage.getData());

            //Json format�ndaki datay� parse edip kullanabiliriz.
            // Biz direk datay� Push Notification olarak bast�r�yoruz

            sendNotification("ibokngl",""+remoteMessage.getData());
    }
}

    private void sendNotification(String messageTitle,String messageBody) {
        Intent intent = new Intent(this, Pesrevler.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        long[] pattern = {500,500,500,500};//Titre�im ayar�
        Toast.makeText(getApplicationContext(), messageBody, Toast.LENGTH_LONG).show();
        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        Notification notification = notificationBuilder.setSmallIcon(R.drawable.notify).setTicker(messageTitle).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(messageTitle)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentIntent(pendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(messageBody).build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0,notification);

    }
    }

