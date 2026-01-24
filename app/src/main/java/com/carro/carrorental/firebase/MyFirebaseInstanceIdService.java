package com.carro.carrorental.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseInstanceIdService extends MyFirebaseMessagingService {
    private static final String TAG = "MyFirebaseInstanceIdService";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
       /* if (BuildConfig.DEBUG)*/
            Log.d("token", "From: " + remoteMessage.getFrom());
    }
}
