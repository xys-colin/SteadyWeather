package cn.qmulin.steadyweather.entity;

import com.google.gson.annotations.SerializedName;

public class BasicEntity {
    public String location;
    @SerializedName("parent_city")
    public String parentCity;
    @SerializedName("admin_area")
    public String adminArea;

}
