package cn.qmulin.steadyweather.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CityAPI {
    @SerializedName("HeWeather6")
    public List<CityEntity> mCities=new ArrayList<>();
}
