package cn.qmulin.steadyweather.entity;

import com.google.gson.annotations.SerializedName;

public class NowEntity {
    @SerializedName("cond_code")
    public String condCode;
    @SerializedName("cond_txt")
    public String condTxt;
    @SerializedName("fl")
    public String feelLike;
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("wind_dir")
    public String windDir;
    @SerializedName("wind_spd")
    public String windSpd;
    public String hum;
}
