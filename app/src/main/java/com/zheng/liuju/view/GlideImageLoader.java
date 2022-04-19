package com.zheng.liuju.view;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zheng.liuju.R;
import com.zheng.liuju.banner.loader.ImageLoader;


public class GlideImageLoader  extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {


        //Glide 加载图片简单用法
        Glide.with(context).load(path).placeholder(R.mipmap.error).error(R.mipmap.error).into(imageView);




    }


}
