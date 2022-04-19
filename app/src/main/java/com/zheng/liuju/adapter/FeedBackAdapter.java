package com.zheng.liuju.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.zheng.liuju.R;
import com.zheng.liuju.bean.FeedBackBean;

import java.util.ArrayList;

public class FeedBackAdapter extends BaseAdapter {
    private Context context;//声明适配器中引用的上下文
    private ArrayList<FeedBackBean> data;
    //通过构造方法初始化上下文
    public FeedBackAdapter(Context context, ArrayList<FeedBackBean> data) {
        this.context = context;
        this.data = data;

    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_feed_back,null);
            holder.ids = (TextView) convertView.findViewById(R.id.ids);
             holder.logo = (ImageView) convertView.findViewById(R.id.logo);
          holder.bg = (RelativeLayout) convertView.findViewById(R.id.bg);
            /*     holder.sampleNo=(TextView) convertView.findViewById(R.id.sampleNo);*/
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ids.setText( data.get(i).feedName);
        if (data.get(i).type){
            Resources resources = context.getResources();
            Drawable drawable = resources.getDrawable(R.drawable.language_item);
            holder.bg.setBackgroundDrawable(drawable);
            holder.ids.setTextColor(Color.parseColor("#FFFFFF"));
            holder.logo.setImageResource(R.mipmap.redview);
        }else {
            Resources resources = context.getResources();
            Drawable drawable = resources.getDrawable(R.drawable.feed_bg);
            holder.bg.setBackgroundDrawable(drawable);
            holder.ids.setTextColor(Color.parseColor("#9B9B9B"));
            holder.logo.setImageResource(R.mipmap.grayview);
            // holder.ids.setBackgroundColor(R.drawable.test_bg);
        }
      /*  holder.sampleName.setText(data.get(i).sampleName);//
        holder.information.setText(data.get(i).information);
        holder.sampleNo.setText(data.get(i).number);*/
        return convertView;

    }
    private  class  ViewHolder{
        public TextView ids;
        public ImageView   logo;
        public RelativeLayout bg;
        public  TextView  sampleNo;

    }
}
