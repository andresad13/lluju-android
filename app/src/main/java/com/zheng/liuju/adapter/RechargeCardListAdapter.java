package com.zheng.liuju.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zheng.liuju.R;
import com.zheng.liuju.bean.RechargeCardListBean;

import java.util.ArrayList;

public class RechargeCardListAdapter extends BaseAdapter {
    private Context context;//声明适配器中引用的上下文
    private ArrayList<RechargeCardListBean> data;
    //通过构造方法初始化上下文
    public RechargeCardListAdapter(Context context, ArrayList<RechargeCardListBean> data) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.recharge_card_adapt,null);
            holder.visa = (ImageView) convertView.findViewById(R.id.visa);
            holder.cardNum = (TextView) convertView.findViewById(R.id.cardNum);
            holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.cardNum.setText(data.get(i).CardNumberLast4);
       // Log.e("thumbnail",data.get(i).thumbnail);
        if (data.get(i).thumbnail.contains("balance")){

            Glide.with(context).load( R.mipmap.balance).into(holder.visa);
        }else {
           Glide.with(context).load( data.get(i).thumbnail).into(holder.visa);

        }


        if (data.get(i).type){
           holder.cb.setChecked(true);
        }else {
            holder.cb.setChecked(false);
        }
        return convertView;

    }
    private  class  ViewHolder{
        public ImageView visa;
        public  TextView  cardNum;
        public CheckBox cb;

    }
}
