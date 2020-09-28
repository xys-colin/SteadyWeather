package cn.qmulin.steadyweather.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {
    public BasicEntity basic;
    public String status;
    public NowEntity now;
    public UpdateEntity update;
    @SerializedName("daily_forecast")
    public List<ForecastEntity> forecast;
    @SerializedName("lifestyle")
    public List<LifestyleEntity> lifestyle;

}