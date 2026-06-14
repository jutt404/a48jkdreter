package com.arrtytrails.admin;

import android.content.Context;
import android.os.Build;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class DeviceTokenUploader {
    private DeviceTokenUploader() {}

    public static void upload(Context context, String token) {
        if (token == null || token.trim().isEmpty()) return;
        new Thread(() -> {
            try {
                String postData = "secret=" + enc(AppConfig.PUSH_DEVICE_SECRET) +
                        "&token=" + enc(token) +
                        "&app_version=" + enc(AppConfig.VERSION_NAME) +
                        "&device=" + enc(Build.MANUFACTURER + " " + Build.MODEL) +
                        "&platform=" + enc("android") +
                        "&package=" + enc(context.getPackageName());

                URL url = new URL(AppConfig.PUSH_REGISTER_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(12000);
                conn.setReadTimeout(12000);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                byte[] bytes = postData.getBytes(StandardCharsets.UTF_8);
                conn.setFixedLengthStreamingMode(bytes.length);
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(bytes);
                }
                conn.getResponseCode();
                conn.disconnect();
            } catch (Exception ignored) {
                // Silent by design: token upload retries when app opens or FCM refreshes token.
            }
        }).start();
    }

    private static String enc(String value) throws Exception {
        return URLEncoder.encode(value == null ? "" : value, "UTF-8");
    }
}
