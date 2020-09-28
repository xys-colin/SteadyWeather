package cn.qmulin.steadyweather.util;

import android.content.Context;
import android.content.SharedPreferences;

import cn.qmulin.steadyweather.base.BaseApplication;

public class SharedPreferenceUtil {
    public static final String LOCATION = "location"; //选择城市
    public static final String HOUR = "current_hour"; //当前小时

    public static final String CHANGE_ICONS = "change_icons"; //切换图标
    public static final String CLEAR_CACHE = "clear_cache"; //清空缓存
    public static final String AUTO_UPDATE = "change_update_time"; //自动更新时长
    private SharedPreferences mPrefs;

    public static SharedPreferenceUtil getInstance() {
        return SPHolder.sInstance;
    }

    private static class SPHolder {
        private static final SharedPreferenceUtil sInstance = new SharedPreferenceUtil();
    }

    private SharedPreferenceUtil() {
        mPrefs = BaseApplication.getAppContext().getSharedPreferences("setting", Context.MODE_PRIVATE);
    }

    public SharedPreferenceUtil putInt(String key, int value) {
        mPrefs.edit().putInt(key, value).apply();
        return this;
    }

    public int getInt(String key, int defValue) {
        return mPrefs.getInt(key, defValue);
    }

    public SharedPreferenceUtil putString(String key, String value) {
        mPrefs.edit().putString(key, value).apply();
        return this;
    }

    public String getString(String key, String defValue) {
        return mPrefs.getString(key, defValue);
    }

    public SharedPreferenceUtil putBoolean(String key, boolean value) {
        mPrefs.edit().putBoolean(key, value).apply();
        return this;
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mPrefs.getBoolean(key, defValue);
    }

    // 图标种类相关
    public void setIconType(int type) {
        mPrefs.edit().putInt(CHANGE_ICONS, type).apply();
    }

    public int getIconType() {
        return mPrefs.getInt(CHANGE_ICONS, 0);
    }


    //当前城市
    public void setCityName(String name) {
        mPrefs.edit().putString(LOCATION, name).apply();
    }

    public String getCityName() {
        return mPrefs.getString(LOCATION, "北京");
    }
}
