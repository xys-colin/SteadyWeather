package cn.qmulin.steadyweather.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;

import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qmulin.steadyweather.R;
import cn.qmulin.steadyweather.adapter.WeatherAdapter;
import cn.qmulin.steadyweather.base.BaseApplication;
import cn.qmulin.steadyweather.component.RetrofitSingleton;
import cn.qmulin.steadyweather.db.CityDB;
import cn.qmulin.steadyweather.entity.Weather;

import cn.qmulin.steadyweather.util.DoubleClickExit;
import cn.qmulin.steadyweather.util.RxUtil;
import cn.qmulin.steadyweather.util.SharedPreferenceUtil;

import cn.qmulin.steadyweather.util.ToastUtil;
import cn.qmulin.steadyweather.util.Util;
import io.reactivex.Observable;


public class MainActivity extends RxAppCompatActivity {
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.city_title)
    TextView cityTitle;
    @BindView(R.id.bing_pic_img)
    ImageView bingPicImg;
    @BindView(R.id.iv_add)
    ImageView imgAdd;
    public AMapLocationClient mLocationClient;
    public AMapLocationClientOption mLocationOption=null;
    public String[] location;
    public static boolean isFirst = true;
    private List<Weather> weathers = new ArrayList<>();
    private Weather mWeather = new Weather();
    private WeatherAdapter adapter;
    private boolean isNetwork = Util.isNetworkConnected(BaseApplication.getAppContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        location = intent.getStringArrayExtra("location");
        initView();
        new RxPermissions(MainActivity.this)
                .request(Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.READ_PHONE_STATE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .doOnNext(granted -> {
                    if (isNetwork) {
                        if (granted) {
                            if (isFirst) {
                                initLocation();
                                isFirst = false;
                            } else {
                                load(location[0] + "," + location[1]);
                                //load("北京北京");
                            }
                        } else {
                            if (location != null) {
                                load(location[0] + "," + location[1]);
                            } else {
                                load("auto_ip");
                            }
                        }
                    } else {
                        errorLoad();
                    }
                }).subscribe();
    }

    /**
     * 初始化基础View
     */
    private void initView() {
        imgAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CityActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        if (swipeRefresh != null) {
            swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
            swipeRefresh.setOnRefreshListener(() -> {
                if (isNetwork) {
                    if (location != null) {
                        load(location[0] + "," + location[1]);
                    } else {
                        String location = SharedPreferenceUtil.getInstance().getCityName();
                        load(location);
                    }
                } else {
                    errorLoad();
                }
            });
        }
        weathers.add(mWeather);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WeatherAdapter(weathers);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setWifiScan(false);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(aMapLocation -> {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    aMapLocation.getLocationType();
                    String city = Util.replaceCity(aMapLocation.getCity());
                    String district = Util.replaceCity(aMapLocation.getDistrict());
                    SharedPreferenceUtil.getInstance().setCityName(district + "," + city);
                    load(district + "," + city);
                } else {
                    ToastUtil.showShort("定位失败，加载默认城市！");
                    String location = SharedPreferenceUtil.getInstance().getCityName();
                    load(location);
                }
            }
        });
        mLocationClient.startLocation();
    }

    private void load(String location) {
        if (weathers.size() > 0) {
            weathers.clear();
        }
        Observable.concat(loadImg(), loadWeather(location))
                .doOnSubscribe(along -> swipeRefresh.setRefreshing(true))
                .doOnError(throwable -> {
                    swipeRefresh.setRefreshing(false);
                    errorLoad();
                })
                .doOnNext(o -> {
                    if (o instanceof Weather) {
                        recyclerView.setVisibility(View.VISIBLE);
                        Weather weather = (Weather) o;
                        mWeather.status = weather.status;
                        mWeather.now = weather.now;
                        mWeather.basic = weather.basic;
                        mWeather.forecast = weather.forecast;
                        mWeather.update = weather.update;
                        mWeather.lifestyle = weather.lifestyle;
                        weathers.add(mWeather);
                        cityTitle.setText(weather.basic.location);
                        adapter.notifyDataSetChanged();
                        CityDB.crudCity(weather);
                    } else {
                        Glide.with(this).load(o.toString()).into(bingPicImg);
                    }
                })
                .doOnComplete(() -> {
                    swipeRefresh.setRefreshing(false);
                    ToastUtil.showShort("加载完毕，✺◟(∗❛ัᴗ❛ั∗)◞✺,");
                })
                .subscribe();
    }

    private Observable<Weather> loadWeather(String location) {
        return RetrofitSingleton.getInstance().fetchWeather(location).compose(RxUtil.activityLifecycle(this));
    }

    private Observable<String> loadImg() {
        return RetrofitSingleton.getInstance().fetchImg().compose(RxUtil.activityLifecycle(this));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void errorLoad() {
        recyclerView.setVisibility(View.GONE);
        Glide.with(this).load(R.drawable.error).into(bingPicImg);
        ToastUtil.showShort("网络断开了！");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }

    @Override
    public void onBackPressed() {
        if (!DoubleClickExit.check()) {
            ToastUtil.showShort(getString(R.string.double_exit));
        } else {
            finish();
        }
    }
}
