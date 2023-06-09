package co.fatweb.com.wedding.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    public static int TYPE_WIFI = 1;

    public static int getConnectivityStatus(Context context) {
        @SuppressLint("WrongConstant") NetworkInfo activeNetwork = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == 1) {
                return TYPE_WIFI;
            }
            if (activeNetwork.getType() == 0) {
                return TYPE_MOBILE;
            }
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        if (conn == TYPE_WIFI) {
            return "Wifi enabled";
        }
        if (conn == TYPE_MOBILE) {
            return "Mobile data enabled";
        }
        if (conn == TYPE_NOT_CONNECTED) {
            return "unavailable";
        }
        return null;
    }
}
