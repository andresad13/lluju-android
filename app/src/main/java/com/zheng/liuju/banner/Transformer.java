package com.zheng.liuju.banner;


import androidx.viewpager.widget.ViewPager;

import com.zheng.liuju.banner.transformer.AccordionTransformer;
import com.zheng.liuju.banner.transformer.BackgroundToForegroundTransformer;
import com.zheng.liuju.banner.transformer.CubeInTransformer;
import com.zheng.liuju.banner.transformer.CubeOutTransformer;
import com.zheng.liuju.banner.transformer.DefaultTransformer;
import com.zheng.liuju.banner.transformer.DepthPageTransformer;
import com.zheng.liuju.banner.transformer.FlipHorizontalTransformer;
import com.zheng.liuju.banner.transformer.FlipVerticalTransformer;
import com.zheng.liuju.banner.transformer.ForegroundToBackgroundTransformer;
import com.zheng.liuju.banner.transformer.RotateDownTransformer;
import com.zheng.liuju.banner.transformer.RotateUpTransformer;
import com.zheng.liuju.banner.transformer.ScaleInOutTransformer;
import com.zheng.liuju.banner.transformer.StackTransformer;
import com.zheng.liuju.banner.transformer.TabletTransformer;
import com.zheng.liuju.banner.transformer.ZoomInTransformer;
import com.zheng.liuju.banner.transformer.ZoomOutSlideTransformer;
import com.zheng.liuju.banner.transformer.ZoomOutTranformer;


public class Transformer {
    public static Class<? extends ViewPager.PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
