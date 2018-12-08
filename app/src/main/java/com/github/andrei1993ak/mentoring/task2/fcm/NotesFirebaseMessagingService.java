package com.github.andrei1993ak.mentoring.task2.fcm;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class NotesFirebaseMessagingService extends FirebaseMessagingService {

    public static final String PUSH_MESSAGE_BROADCAST = "push_message_broadcast";

    LocalBroadcastManager mLocalBroadcastManager;

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        final Map<String, String> data = remoteMessage.getData();

        if (data != null) {
            final String title = data.get(PushNotificationDataKeys.TITLE);
            final String description = data.get(PushNotificationDataKeys.DESCRIPTION);

            final Intent intent = new Intent(PUSH_MESSAGE_BROADCAST);
            intent.putExtra(PushNotificationDataKeys.TITLE, title);
            intent.putExtra(PushNotificationDataKeys.DESCRIPTION, description);

            getBroadcastManager().sendBroadcast(intent);
        }
    }

    private LocalBroadcastManager getBroadcastManager() {
        if (mLocalBroadcastManager == null) {
            mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        }

        return mLocalBroadcastManager;
    }
}
