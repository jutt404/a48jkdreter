package com.arrtytrails.admin;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class ArrtyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "arrty_admin_orders";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        DeviceTokenUploader.upload(getApplicationContext(), token);
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);

        String title = "New Arrty Trails Order";
        String body = "Tap to open Orders History";
        String orderId = "";
        String targetUrl = AppConfig.ORDERS_HISTORY_URL;

        RemoteMessage.Notification notification = message.getNotification();
        if (notification != null) {
            if (notification.getTitle() != null && !notification.getTitle().isEmpty()) title = notification.getTitle();
            if (notification.getBody() != null && !notification.getBody().isEmpty()) body = notification.getBody();
        }

        Map<String, String> data = message.getData();
        if (data != null) {
            if (data.containsKey("title")) title = data.get("title");
            if (data.containsKey("body")) body = data.get("body");
            if (data.containsKey("order_id")) orderId = data.get("order_id");
            if (data.containsKey("url")) targetUrl = data.get("url");
        }

        showNotification(title, body, orderId, targetUrl);
    }

    private void showNotification(String title, String body, String orderId, String targetUrl) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "New Orders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Admin alerts when a new order is received");
            channel.enableLights(true);
            channel.setLightColor(Color.rgb(16, 185, 129));
            channel.enableVibration(true);
            manager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("open_orders", true);
        intent.putExtra("order_id", orderId == null ? "" : orderId);
        intent.putExtra("target_url", targetUrl == null || targetUrl.isEmpty() ? AppConfig.ORDERS_HISTORY_URL : targetUrl);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) flags |= PendingIntent.FLAG_IMMUTABLE;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1001, intent, flags);

        Notification.Builder builder = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? new Notification.Builder(this, CHANNEL_ID)
                : new Notification.Builder(this);

        builder.setSmallIcon(R.drawable.ic_stat_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new Notification.BigTextStyle().bigText(body))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL);

        int notificationId = orderId == null || orderId.isEmpty() ? (int) System.currentTimeMillis() : Math.abs(orderId.hashCode());
        manager.notify(notificationId, builder.build());
    }
}
