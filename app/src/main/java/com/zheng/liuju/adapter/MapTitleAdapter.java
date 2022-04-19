package com.zheng.liuju.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zheng.liuju.R;
import com.zheng.liuju.bean.AddAmountBean;

import java.util.ArrayList;

public class MapTitleAdapter extends BaseAdapter {
    private Context context;//声明适配器中引用的上下文
    private ArrayList<AddAmountBean> data;
    //通过构造方法初始化上下文
    public MapTitleAdapter(Context context, ArrayList<AddAmountBean> data) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_map_list,null);
            holder.icno = (ImageView) convertView.findViewById(R.id.icno);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.icno.setImageResource(data.get(i).imgae);
        holder.name.setText(data.get(i).contents);
        return convertView;

    }
    private  class  ViewHolder{
        public ImageView icno;
        public TextView   name;
    }
}
