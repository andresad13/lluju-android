package com.zheng.liuju.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zheng.liuju.R;
import com.zheng.liuju.bean.PayNumBean;

import java.util.ArrayList;

public class RechargeAdapter extends BaseAdapter {
    private Context context;//声明适配器中引用的上下文
    private ArrayList<PayNumBean> data;
    //通过构造方法初始化上下文
    public RechargeAdapter(Context context, ArrayList<PayNumBean> data) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.recharge_item,null);
            holder.pay = (TextView) convertView.findViewById(R.id.pay);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.pay.setText(data.get(i).pay);
        if (data.get(i).type){
            holder.pay.setBackgroundResource(R.drawable.red_recharge_bg);
            holder.pay.setTextColor(Color.parseColor("#FFFFFF"));
        }else {
            holder.pay.setBackgroundResource(R.drawable.white_recharge_bg);
            holder.pay.setTextColor(Color.parseColor("#111111"));
        }
        return convertView;

    }
    private  class  ViewHolder{

        public TextView   pay;
    }
}
