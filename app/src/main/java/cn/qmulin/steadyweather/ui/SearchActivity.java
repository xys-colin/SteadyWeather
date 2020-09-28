package cn.qmulin.steadyweather.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qmulin.steadyweather.R;
import cn.qmulin.steadyweather.adapter.SearchAdapter;
import cn.qmulin.steadyweather.component.RetrofitSingleton;
import cn.qmulin.steadyweather.entity.BasicEntity;
import cn.qmulin.steadyweather.entity.CityEntity;
import cn.qmulin.steadyweather.util.RxUtil;

import cn.qmulin.steadyweather.util.ToastUtil;
import io.reactivex.Observable;

public class SearchActivity extends RxAppCompatActivity {
    @BindView(R.id.recycle_city)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.toolbar_search)
    Toolbar toolbar;
    private SearchAdapter adapter;
    private List<BasicEntity> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initView() {
        searchView.setQueryHint("搜索国内城市");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                dataList.clear();
                Observable.just(s)
                        .take(1, TimeUnit.SECONDS)
                        .doOnSubscribe(disposable -> showProgressBar())
                        .flatMap(s1 -> searchCity(s))
                        .doOnNext(cityEntity -> {
                            if (cityEntity.basic != null) {
                                recyclerView.setVisibility(View.VISIBLE);
                                dataList.addAll(cityEntity.basic);
                                adapter.notifyDataSetChanged();
                            }else {
                                recyclerView.setVisibility(View.GONE);
                                ToastUtil.showShort("无匹配城市");
                            }
                        })
                        .doOnComplete(() -> hideProgressBar())
                        .compose(RxUtil.activityLifecycle(SearchActivity.this))
                        .subscribe();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                dataList.clear();
                Observable.just(s)
                        .filter(s1 -> s.length() >= 2)
                        .debounce(1000,TimeUnit.MILLISECONDS)
                        .doOnSubscribe(disposable -> showProgressBar())
                        .flatMap(s1 -> searchCity(s))
                        .doOnNext(cityEntity -> {
                            if (cityEntity.basic != null) {
                                recyclerView.setVisibility(View.VISIBLE);
                                dataList.addAll(cityEntity.basic);
                                adapter.notifyDataSetChanged();
                            }else {
                                recyclerView.setVisibility(View.GONE);
                                ToastUtil.showShort("无匹配城市");
                            }
                        })
                        .doOnComplete(() -> hideProgressBar())
                        .compose(RxUtil.activityLifecycle(SearchActivity.this))
                        .subscribe();
                return false;
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SearchAdapter(SearchActivity.this, dataList);
        recyclerView.setAdapter(adapter);

    }

    private Observable<CityEntity> searchCity(String search) {
        return RetrofitSingleton.getInstance()
                .fetchCity(search);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
