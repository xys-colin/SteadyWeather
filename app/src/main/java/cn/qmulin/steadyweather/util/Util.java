package cn.qmulin.steadyweather.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

public class Util {
    /**
     * 只关注是否联网
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    public static String safeText(String msg) {
        return TextUtils.isEmpty(msg) ? "" : msg;
    }

    /**
     * 匹配掉错误信息
     * @param city
     * @return
     */
    public static String replaceCity(String city){
        city = safeText(city).replaceAll("(?:省|市|区|县|自治区|特别行政区|地区)", "");
        return city;
    }
}
