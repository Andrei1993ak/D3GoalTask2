package com.github.andrei1993ak.mentoring.task2.fcm;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(final String s) {
        super.onNewToken(s);

        Log.d("MyFirebaseMessaging", s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("MyFirebaseMessaging", "onMessageReceived: " + remoteMessage.getData());
    }
}
