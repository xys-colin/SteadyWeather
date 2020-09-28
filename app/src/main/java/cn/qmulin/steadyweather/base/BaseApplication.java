package cn.qmulin.steadyweather.base;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;


public class BaseApplication extends Application {
    private static Context context;
    private static String sCacheDir;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
        /*
         * 如果存在SD卡则将缓存写入SD卡,否则写入手机内存
         */
        if (getApplicationContext().getExternalCacheDir() != null && ExistSDCard()) {
            sCacheDir = getApplicationContext().getExternalCacheDir().toString();
        } else {
            sCacheDir = getApplicationContext().getCacheDir().toString();
        }
    }

    private boolean ExistSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public static Context getAppContext() {
        return context;
    }

    public static String getAppCacheDir() {
        return sCacheDir;
    }
}
