package cn.qmulin.steadyweather.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.qmulin.steadyweather.entity.BasicEntity;
import cn.qmulin.steadyweather.ui.MainActivity;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context mContext;
    private List<BasicEntity> mBasics;

    public SearchAdapter(Context context, List<BasicEntity> mBasics) {
        this.mContext = context;
        this.mBasics = mBasics;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mContext != null) {
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        holder.textView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            BasicEntity basicEntity = mBasics.get(position);
            String location = basicEntity.location;
            String parentCity = basicEntity.parentCity;
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.putExtra("location", new String[]{location, parentCity});
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        BasicEntity basicEntity = mBasics.get(i);
        String location = basicEntity.location;
        String parentCity = basicEntity.parentCity;
        String adminArea = basicEntity.adminArea;
        if (location.equals(parentCity)) {
            viewHolder.textView.setText(String.format("%s - %s", location, parentCity));
        } else {
            viewHolder.textView.setText(String.format("%s - %s - %s", location, parentCity, adminArea));
        }
    }

    @Override
    public int getItemCount() {
        return mBasics == null ? 0 : mBasics.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
