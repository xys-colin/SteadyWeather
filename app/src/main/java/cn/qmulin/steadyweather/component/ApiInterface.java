package cn.qmulin.steadyweather.component;


import cn.qmulin.steadyweather.entity.CityAPI;
import cn.qmulin.steadyweather.entity.WeatherAPI;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    String HOST = "https://free-api.heweather.net/s6/";

    @GET("weather")
    Observable<WeatherAPI> mWeatherAPI(@Query("location") String location, @Query("key") String key);

    @GET("http://guolin.tech/api/bing_pic")
    Observable<ResponseBody> mBingImg();

    @GET("https://search.heweather.net/find")
    Observable<CityAPI> mSearchAPI(@Query("location") String location, @Query("key") String key,@Query("group")String group);
}
