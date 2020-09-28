package cn.qmulin.steadyweather.adapter;

import android.content.Context;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qmulin.steadyweather.db.CityDB;
import cn.qmulin.steadyweather.ui.MainActivity;
import cn.qmulin.steadyweather.R;
import cn.qmulin.steadyweather.db.City;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    private Context mContext;
    private List<City> mCityList;

    public CityAdapter(List<City> cities, Context context) {
        this.mCityList = cities;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mContext != null) {
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.city_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            City city = mCityList.get(position);
            String location = city.getLocation();
            String parentCity = city.getParentCity();
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.putExtra("location", new String[]{location, parentCity});
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });
        holder.cardView.setOnLongClickListener(view1 -> {
            int position = holder.getAdapterPosition();
            Snackbar.make(view1,"删除城市",Snackbar.LENGTH_SHORT).setAction("确认",v -> {
                City city = mCityList.get(position);
                CityDB.deleteCity(city.getLocation(),city.getParentCity());
                mCityList.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }).show();
            return true;
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        City city = mCityList.get(i);
        viewHolder.locationText.setText(city.getLocation());
        viewHolder.parentCityText.setText(String.format("%s,%s", city.getParentCity(), city.getAdminArea()));
        viewHolder.maxTmpText.setText(city.getMax());
        viewHolder.minTmpText.setText(String.format("%s℃", city.getMin()));
        Glide.with(mContext).load("https://cdn.heweather.com/cond_icon/" + city.getCondCode() + ".png").into(viewHolder.condImg);
        viewHolder.condText.setText(city.getCondTxt());
        viewHolder.tmpText.setText(String.format("%s℃", city.getTemperature()));
    }

    @Override
    public int getItemCount() {
        return mCityList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        @BindView(R.id.location_text)
        TextView locationText;
        @BindView(R.id.parent_city_text)
        TextView parentCityText;
        @BindView(R.id.max_tmp_text)
        TextView maxTmpText;
        @BindView(R.id.min_tmp_text)
        TextView minTmpText;
        @BindView(R.id.tmp_text)
        TextView tmpText;
        @BindView(R.id.cond_text)
        TextView condText;
        @BindView(R.id.cond_img)
        ImageView condImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
