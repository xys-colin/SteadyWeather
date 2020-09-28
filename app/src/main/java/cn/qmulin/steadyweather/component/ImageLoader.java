package cn.qmulin.steadyweather.component;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * 图片加载类
 */
public class ImageLoader {
    public static void load(Context context, String url, ImageView imageView){
        Glide.with(context).load(url).into(imageView);
    }
    public static void clear(Context context){
        Glide.get(context).clearMemory();
    }
}
