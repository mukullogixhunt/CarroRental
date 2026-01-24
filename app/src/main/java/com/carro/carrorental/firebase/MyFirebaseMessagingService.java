package com.carro.carrorental.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.carro.carrorental.R;
import com.carro.carrorental.ui.activity.SplashActivity;

import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_NAME = "FCM";
    private static final String CHANNEL_DESC = "Firebase Cloud Messaging";
    private int numMessages = 0;
    private int notificationId = new Random().nextInt();
    int count = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

            try {
                sendNotification(remoteMessage.getNotification().getTitle(),
                        remoteMessage.getNotification().getBody(), null);
                Log.d("TAG", "onMessageReceived: ");
            } catch (ExecutionException e) {
                Log.d("TAG", "ExecutionException: " + e.toString());
                e.printStackTrace();
            } catch (InterruptedException e) {
                Log.d("TAG", "InterruptedException: " + e.toString());
                e.printStackTrace();
            }
    }

    private void sendNotification(String title, String body, JsonObject jsonObject) throws ExecutionException, InterruptedException {

        Intent intent;
        intent = new Intent(this, SplashActivity.class);
        intent.putExtra("data", new Gson().toJson(jsonObject));
        intent.putExtra("notify", "1");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_MUTABLE);

        Bitmap largeImage = Glide.with(this).asBitmap().load(R.mipmap.ic_launcher).into(120, 120).get();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,
                getString(R.string.default_notification_channel_id))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent)
                .setColor(getResources().getColor(R.color.primary))
                .setLights(Color.RED, 1000, 300)
                .setDefaults(Notification.DEFAULT_ALL)
                .setLargeIcon(largeImage)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setNumber(numMessages)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.mipmap.ic_launcher);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    getString(R.string.default_notification_channel_id), CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription(CHANNEL_DESC);
            channel.setShowBadge(true);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
        assert notificationManager != null;
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

}
