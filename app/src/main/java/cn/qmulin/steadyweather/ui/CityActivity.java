package cn.qmulin.steadyweather.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qmulin.steadyweather.R;
import cn.qmulin.steadyweather.adapter.CityAdapter;
import cn.qmulin.steadyweather.db.City;
import cn.qmulin.steadyweather.db.CityDB;
import cn.qmulin.steadyweather.util.ToastUtil;

public class CityActivity extends RxAppCompatActivity {
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        List<City> cityList = CityDB.loadCities();
        CityAdapter adapter = new CityAdapter(cityList, CityActivity.this);
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(CityActivity.this, SearchActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.city_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.edit:
                ToastUtil.showShort("功能还未开发，敬请期待！");
                break;
            default:
                break;
        }
        return true;
    }

}
