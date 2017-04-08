package org.apache.cordova.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.text.TextUtils;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;


public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";

    /**
     * Called when a message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title;
        String text;
        String id;
        Map<String, String> data = remoteMessage.getData();
        RemoteMessage.Notification notification = remoteMessage.getNotification();

        if (notification != null) {
            title = notification.getTitle();
            text = notification.getBody();
            id = remoteMessage.getMessageId();
        } else {
            title = data.get("title");
            text = data.get("text");
            id = data.get("id");
        }

        if (TextUtils.isEmpty(id)) {
            Random rand = new Random();
            int n = rand.nextInt(50) + 1;
            id = Integer.toString(n);
        }

        Log.i(TAG, "From: " + remoteMessage.getFrom());
        Log.i(TAG, "Notification Message id: " + id);
        Log.i(TAG, "Notification Message Title: " + title);
        Log.i(TAG, "Notification Message Body/Text: " + text);

        if (!TextUtils.isEmpty(text) || !TextUtils.isEmpty(title) || (!remoteMessage.getData().isEmpty())) {
            boolean showNotification = (Firebase.inBackground() || !MessagingComponent.hasNotificationsCallback())
                    && (!TextUtils.isEmpty(text) || !TextUtils.isEmpty(title));
            sendNotification(id, title, text, data, showNotification);
        }
    }

    private void sendNotification(String id, String title, String messageBody, Map<String, String> data,
                                  boolean showNotification) {
        Bundle bundle = new Bundle();
        for (String key : data.keySet()) {
            bundle.putString(key, data.get(key));
        }
        if (showNotification) {
            Intent intent = new Intent(this, NotificationReceiver.class);
            intent.putExtras(bundle);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id.hashCode(), intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            int resID = getResources().getIdentifier("notification_icon", "drawable", getPackageName());
            if (resID != 0) {
                notificationBuilder.setSmallIcon(resID);
            } else {
                notificationBuilder.setSmallIcon(getApplicationInfo().icon);
            }

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(id.hashCode(), notificationBuilder.build());
        } else {
            bundle.putBoolean("tap", false);
            bundle.putString("title", title);
            bundle.putString("body", messageBody);
            MessagingComponent.sendNotification(bundle);
        }
    }


}
