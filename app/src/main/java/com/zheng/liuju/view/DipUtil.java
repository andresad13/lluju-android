package com.zheng.liuju.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

class DipUtil {
    public static int getStatusHeight(Context context) {

        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public static int getScreenWidth(Context context) {
        Activity contexts=(Activity)context;
        DisplayMetrics metric = new DisplayMetrics();
        contexts.getWindowManager().getDefaultDisplay().getRealMetrics(metric);
        int width = metric.widthPixels; // 宽度（PX）
        int height = metric.heightPixels; // 高度（PX）
        float density = metric.density; // 密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;
        return width;
    }

    public static int getScreenHeight(Context context) {
        Activity contexts=(Activity)context;
        DisplayMetrics metric = new DisplayMetrics();
        contexts.getWindowManager().getDefaultDisplay().getRealMetrics(metric);
        int width = metric.widthPixels; // 宽度（PX）
        int height = metric.heightPixels; // 高度（PX）
        float density = metric.density; // 密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;
        return height;

    }
}
