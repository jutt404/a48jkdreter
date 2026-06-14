package com.arrtytrails.admin;

public final class AppConfig {
    private AppConfig() {}

    public static final String APP_NAME = "Arrty Trails Admin";
    public static final String ORGANIZATION = "Arrty Trails";
    public static final String APP_CREATOR = "Wzdev";
    public static final String VERSION_NAME = "0.01";

    // Change only these URLs if your real admin path is different.
    public static final String ADMIN_HOME_URL = "https://arrtytrails.site/ad-admin/";
    public static final String ORDERS_HISTORY_URL = "https://arrtytrails.site/ad-admin/#orders";

    // Upload BACKEND_UPLOAD_TO_WEBSITE/admin_push to your site, then keep this URL.
    public static final String PUSH_REGISTER_URL = "https://arrtytrails.site/admin_push/register_device.php";

    // Must match PUSH_SECRET in BACKEND_UPLOAD_TO_WEBSITE/admin_push/config.php.
    // This is only a device-registration secret, not your admin password.
    public static final String PUSH_DEVICE_SECRET = "CHANGE_THIS_RANDOM_SECRET_32_CHARS";

    public static final String[] ALLOWED_HOSTS = new String[] {
        "arrtytrails.site",
        "www.arrtytrails.site"
    };
}
