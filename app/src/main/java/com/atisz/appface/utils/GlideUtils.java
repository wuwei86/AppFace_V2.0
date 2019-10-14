package com.atisz.appface.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by LaoWu on 2018/6/19 0019.
 * glide加载图片的工具类
 */

public class GlideUtils {
    public static void loadImage(Activity activity, int url, ImageView imageView) {
        Glide.with(activity).load(url).into(imageView);
    }

    public static void loadImage(Fragment fragment, int url, ImageView imageView) {
        Glide.with(fragment).load(url).into(imageView);
    }

    public static void loadImage(Context context, int url, ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }

    public static void loadImage(Activity activity, String url, ImageView imageView) {
        Glide.with(activity).load(url).into(imageView);
    }

    public static void loadImage(Fragment fragment, String url, ImageView imageView) {
        Glide.with(fragment).load(url).into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }
}
