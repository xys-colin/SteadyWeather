package cn.qmulin.steadyweather.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class LaunchActivity extends AppCompatActivity {
    private static final String TAG = LaunchActivity.class.getSimpleName();

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    Intent intent=new Intent(LaunchActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                });
    }
}
