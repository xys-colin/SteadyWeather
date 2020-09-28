package cn.qmulin.steadyweather.entity;

import com.google.gson.annotations.SerializedName;

public class ForecastEntity {
    @SerializedName("cond_code_d")
    public String condCode;
    @SerializedName("cond_txt_d")
    public String condTxt;
    public String date;
    @SerializedName("tmp_max")
    public String max;
    @SerializedName("tmp_min")
    public String min;
    public String pop;
    @SerializedName("wind_sc")
    public String windSc;
    @SerializedName("wind_spd")
    public String windSpd;
    @SerializedName("wind_dir")
    public String windDir;
}
