package com.iwlpl.connectme;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class MyMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String token) {
        //Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        Log.e("FCM token",token);
        sendRegistrationToServer(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String datetime = dateformat.format(c.getTime());

        DBHelper mydb = new DBHelper(this);

        mydb.insertNotification(Objects.requireNonNull(remoteMessage.getNotification()).getTitle(),datetime,remoteMessage.getNotification().getBody());
        showNotification(Objects.requireNonNull(remoteMessage.getNotification()).getTitle(),remoteMessage.getNotification().getBody());
    }
    public void showNotification(String title,String message)
    {
        // Date currentTime = Calendar.getInstance().getTime();


        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"MyNotification")
                .setContentTitle("Inland World Logistics")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentText(title+"\n"+message);
        NotificationManagerCompat manager=NotificationManagerCompat.from(this);
        manager.notify(999,builder.build());
    }
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }
}
