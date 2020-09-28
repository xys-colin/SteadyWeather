package cn.qmulin.steadyweather.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WeatherAPI {
    @SerializedName("HeWeather6")
    public List<Weather> mWeathers=new ArrayList<>();
}
