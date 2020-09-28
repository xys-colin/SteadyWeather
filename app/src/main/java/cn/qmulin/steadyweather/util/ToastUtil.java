package cn.qmulin.steadyweather.util;

import android.widget.Toast;

import cn.qmulin.steadyweather.base.BaseApplication;

public class ToastUtil {
    public static void showShort(String msg) {
        Toast.makeText(BaseApplication.getAppContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(String msg) {
        Toast.makeText(BaseApplication.getAppContext(), msg, Toast.LENGTH_LONG).show();
    }
}
