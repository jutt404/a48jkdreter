# Arrty Trails Admin Notifier App v0.01

Professional admin Android app for Arrty Trails with push notifications for new orders.

## What this app does

- Opens admin panel inside a secure WebView.
- Registers this phone for admin push notifications.
- Shows mobile notification when a new order is received, even if the app is closed.
- Notification tap opens the admin Orders History URL.
- Blocks unsafe cleartext traffic.
- Uses only required permissions: INTERNET, ACCESS_NETWORK_STATE, POST_NOTIFICATIONS.

## Important URLs to customize

Edit:

`app/src/main/java/com/arrtytrails/admin/AppConfig.java`

Set these if your admin path is different:

```java
ADMIN_HOME_URL = "https://arrtytrails.site/ad-admin/";
ORDERS_HISTORY_URL = "https://arrtytrails.site/ad-admin/#orders";
PUSH_REGISTER_URL = "https://arrtytrails.site/admin_push/register_device.php";
PUSH_DEVICE_SECRET = "CHANGE_THIS_RANDOM_SECRET_32_CHARS";
```

## Firebase setup required for real push notifications

Closed-app notifications require Firebase Cloud Messaging.

1. Go to Firebase Console.
2. Create/open project.
3. Add Android app package:

`com.arrtytrails.admin`

4. Download `google-services.json`.
5. Replace this file:

`app/google-services.json`

6. Go to Firebase Project Settings > Service Accounts.
7. Generate new private key.
8. Upload it on website as:

`admin_push/firebase-service-account.json`

9. Edit website file:

`admin_push/config.php`

Set:

```php
const PUSH_SECRET = 'same secret used in AppConfig.java';
const FCM_PROJECT_ID = 'your Firebase project id';
```

## Backend upload

Upload this folder to your website root:

`BACKEND_UPLOAD_TO_WEBSITE/admin_push/`

It should become:

`https://arrtytrails.site/admin_push/register_device.php`

Then open:

`https://arrtytrails.site/admin_push/setup_check.php`

## Trigger notification when new order is created

After your website saves a new order, add:

```php
require_once __DIR__ . '/admin_push/notify_helper.php';
arrty_notify_admin_new_order((string)$orderId, (string)$customerName, (string)$grandTotal);
```

For testing, after app opens once, call:

`https://arrtytrails.site/admin_push/send_new_order_notification.php?secret=YOUR_SECRET&order_id=AT-1001&customer=Test&total=Rs%201000`

## Build in Codemagic

Upload all root files to GitHub, then use workflow:

`Arrty Trails Admin Notifier APK`

Download artifact:

`app-debug.apk`

For public release, build signed release APK/AAB.
