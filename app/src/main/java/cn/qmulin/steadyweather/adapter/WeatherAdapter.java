package cn.qmulin.steadyweather.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import cn.qmulin.steadyweather.R;
import cn.qmulin.steadyweather.base.BaseViewHolder;
import cn.qmulin.steadyweather.component.ImageLoader;
import cn.qmulin.steadyweather.entity.Weather;
import cn.qmulin.steadyweather.util.TimeUitl;

public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private static final int TYPE_ONE = 0;
    private static final int TYPE_TWO = 1;
    private static final int TYPE_THREE = 2;
    private Weather mWeatherData;

    public WeatherAdapter(List<Weather> weathers) {
        if (weathers.size()>0){
            this.mWeatherData = weathers.get(0);
        }

    }

    @Override
    public int getItemViewType(int position) {

            if (position == WeatherAdapter.TYPE_ONE) {
                return WeatherAdapter.TYPE_ONE;
            }
            if (position == WeatherAdapter.TYPE_TWO) {
                return WeatherAdapter.TYPE_TWO;
            }
            if (position == WeatherAdapter.TYPE_THREE) {
                return WeatherAdapter.TYPE_THREE;
            }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        switch (i) {
            case TYPE_ONE:
                return new NowWeatherViewHolder(LayoutInflater.from(mContext).inflate(R.layout.now, viewGroup, false));
            case TYPE_TWO:
                return new ForecastViewHolder(LayoutInflater.from(mContext).inflate(R.layout.forecast, viewGroup, false));
            case TYPE_THREE:
                return new LifestyleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.lifestyle, viewGroup, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int itemType = getItemViewType(i);
        switch (itemType) {
            case TYPE_ONE:
                ((NowWeatherViewHolder) viewHolder).bind(mWeatherData);
                break;
            case TYPE_TWO:
                ((ForecastViewHolder) viewHolder).bind(mWeatherData);
                break;
            case TYPE_THREE:
                ((LifestyleViewHolder) viewHolder).bind(mWeatherData);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mWeatherData.status != null ? 3 : 0;
    }


    class NowWeatherViewHolder extends BaseViewHolder<Weather> {
        @BindView(R.id.degree_text)
        TextView degreeText;
        @BindView(R.id.cond_text)
        TextView condText;
        @BindView(R.id.update)
        TextView update;
        @BindView(R.id.fl_text)
        TextView flText;
        @BindView(R.id.wind_text)
        TextView windText;
        @BindView(R.id.hum_text)
        TextView humText;

        public NowWeatherViewHolder(View inflate) {
            super(inflate);
        }

        @Override
        public void bind(Weather weather) {
            try {
                degreeText.setText(String.format("%s℃", weather.now.temperature));
                condText.setText(weather.now.condTxt);
                update.setText(weather.update.updateTime.split(" ")[1]);
                flText.setText(String.format("%s℃", weather.now.feelLike));
                windText.setText(String.format("%s级", weather.now.windDir + weather.now.windSpd));
                humText.setText(weather.now.hum);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class ForecastViewHolder extends BaseViewHolder<Weather> {
        private LinearLayout forecastLinear;
        private TextView[] forecastDate = new TextView[mWeatherData.forecast.size()];
        private TextView[] forecastTemp = new TextView[mWeatherData.forecast.size()];
        private TextView[] forecastTxt = new TextView[mWeatherData.forecast.size()];
        private ImageView[] forecastIcon = new ImageView[mWeatherData.forecast.size()];

        ForecastViewHolder(View itemView) {
            super(itemView);
            forecastLinear = (LinearLayout) itemView.findViewById(R.id.forecast_linear);
            for (int i = 0; i < mWeatherData.forecast.size(); i++) {
                View view = View.inflate(mContext, R.layout.forecast_item, null);
                forecastDate[i] = (TextView) view.findViewById(R.id.forecast_date);
                forecastTemp[i] = (TextView) view.findViewById(R.id.forecast_temp);
                forecastTxt[i] = (TextView) view.findViewById(R.id.forecast_txt);
                forecastIcon[i] = (ImageView) view.findViewById(R.id.forecast_icon);
                forecastLinear.addView(view);
            }
        }

        @Override
        public void bind(Weather weather) {
            try {
                forecastDate[0].setText("今日");
                forecastDate[1].setText("明日");
                for (int i = 0; i < weather.forecast.size(); i++) {
                    if (i > 1) {
                        try {
                            forecastDate[i].setText(TimeUitl.dayForWeek(weather.forecast.get(i).date));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    ImageLoader.load(mContext, "https://cdn.heweather.com/cond_icon/" + weather.forecast.get(i).condCode + ".png", forecastIcon[i]);
                    forecastTemp[i].setText(
                            String.format("%s℃ ~ %s℃", weather.forecast.get(i).min, weather.forecast.get(i).max));
                    forecastTxt[i].setText(
                            String.format("%s。 %s %s %s km/h。 降水几率 %s%%。",
                                    weather.forecast.get(i).condTxt,
                                    weather.forecast.get(i).windSc,
                                    weather.forecast.get(i).windDir,
                                    weather.forecast.get(i).windSpd,
                                    weather.forecast.get(i).pop));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class LifestyleViewHolder extends BaseViewHolder<Weather> {
        @BindView(R.id.cloth_brief)
        TextView clothBrief;
        @BindView(R.id.cloth_txt)
        TextView clothText;
        @BindView(R.id.sport_brief)
        TextView sportBrief;
        @BindView(R.id.sport_txt)
        TextView sportText;
        @BindView(R.id.travel_brief)
        TextView travelBrief;
        @BindView(R.id.travel_txt)
        TextView travelText;
        @BindView(R.id.flu_brief)
        TextView fluBrief;
        @BindView(R.id.flu_txt)
        TextView fluText;

        public LifestyleViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void bind(Weather weather) {
            try {
                clothBrief.setText(String.format("穿衣指数---%s", weather.lifestyle.get(1).brf));
                clothText.setText(weather.lifestyle.get(1).txt);
                fluBrief.setText(String.format("感冒指数---%s", weather.lifestyle.get(2).brf));
                fluText.setText(weather.lifestyle.get(2).txt);
                sportBrief.setText(String.format("运动指数---%s", weather.lifestyle.get(3).brf));
                sportText.setText(weather.lifestyle.get(3).txt);
                travelBrief.setText(String.format("旅游指数---%s", weather.lifestyle.get(4).brf));
                travelText.setText(weather.lifestyle.get(4).txt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
