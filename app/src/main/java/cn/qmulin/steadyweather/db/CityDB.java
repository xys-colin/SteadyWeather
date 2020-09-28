package cn.qmulin.steadyweather.db;

import org.litepal.LitePal;

import java.util.List;

import cn.qmulin.steadyweather.entity.Weather;
import cn.qmulin.steadyweather.util.SharedPreferenceUtil;

public class CityDB {
    public static void crudCity(Weather weather) {
        if (weather==null){
            return;
        }
        String location = SharedPreferenceUtil.getInstance().getCityName();
        if (location.equals(weather.basic.location+","+weather.basic.parentCity)) {
            updateLocCity(weather);
        } else {
            if (isExist(weather)) {
                updateCity(weather);
            } else {
                insertCity(weather);
            }
        }
    }

    public static void insertCity(Weather weather) {
        City city = new City();
        saveCity(city, weather);
    }

    public static void updateLocCity(Weather weather) {
        City city;
        city = LitePal.findFirst(City.class);
        if (city != null) {
            if (!city.getUpdate().equals(weather.update.updateTime)) {
                saveCity(city, weather);
            }
        } else {
            city = new City();
            saveCity(city, weather);
        }

    }

    public static void updateCity(Weather weather) {
        List<City> cities = LitePal
                .where("location=? and parentCity = ?", weather.basic.location, weather.basic.parentCity)
                .find(City.class);
        City city = cities.get(0);
        if (city != null && !city.getUpdate().equals(weather.update.updateTime)) {
            saveCity(city, weather);
        }
    }

    public static List<City> loadCities() {
        return LitePal.findAll(City.class);
    }

    public static boolean isExist(Weather weather) {
        List<City> cities = LitePal
                .where("location=? and parentCity = ?", weather.basic.location, weather.basic.parentCity)
                .find(City.class);
        return cities.size() > 0;
    }

    public static void deleteCity(String location, String parentCity) {
        LitePal.deleteAll(City.class, "location=? and parentCity=?", location, parentCity);
    }

    private static void saveCity(City city, Weather weather) {
        city.setLocation(weather.basic.location);
        city.setParentCity(weather.basic.parentCity);
        city.setAdminArea(weather.basic.adminArea);
        city.setMax(weather.forecast.get(0).max);
        city.setMin(weather.forecast.get(0).min);
        city.setCondCode(weather.now.condCode);
        city.setCondTxt(weather.now.condTxt);
        city.setTemperature(weather.now.temperature);
        city.setUpdate(weather.update.updateTime);
        city.save();
    }
}
