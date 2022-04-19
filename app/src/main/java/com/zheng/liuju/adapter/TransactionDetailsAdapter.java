package com.zheng.liuju.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.zheng.liuju.R;
import com.zheng.liuju.bean.TransactionBean;
import com.zheng.liuju.bean.TransactionNum;

import java.util.ArrayList;

public class TransactionDetailsAdapter extends BaseAdapter {
    public static final int TYPE_TITLE = 0;
    public static final int TYPE_COMPANY = 1;
    private  int types;
    private  Context  context;
    private   ArrayList<TransactionBean> list;
    public TransactionDetailsAdapter(Context context, ArrayList<TransactionBean> list,int types ) {

        this.context = context;
        this.list = list;
        this.types=types;

    }
    @Override
    public boolean isEnabled(int position) {
        // 该位置判断的一个状态，比如栗子中的“已完成/未完成”
        // 也可以根据位置进行拦截，第一条Item不可点击为：if (position == 0) {
        if (list.get(position).type) {
            return false;// 符合条件的Item不可点击
        } else {
            // 拦截事件交给上一级处理
            return super.isEnabled(position);
        }
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public int getItemViewType(int position) {
        if (list.get(position).type == true) {
            return TYPE_TITLE;
        } else {
            return TYPE_COMPANY;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {


        int type = getItemViewType(position);

        TitleViewHolder holder1 = null;
        ComViewHolder holder2 = null;
        System.out.println("getView " + position + " " + convertView
                + " type = " + type);
        if (convertView == null) {
            //选择某一个样式。。
            switch (type) {
                case TYPE_TITLE:
                    convertView = LayoutInflater.from(context).inflate(R.layout.transaction_title, null);

                    holder1 = new TitleViewHolder();
                    holder1.title = convertView.findViewById(R.id.title);

                    holder1.times = convertView.findViewById(R.id.times);
                    convertView.setTag(holder1);
                    break;
                case TYPE_COMPANY:
                    convertView = LayoutInflater.from(context).inflate(R.layout.transaction_item,
                            null);
                    holder2 = new ComViewHolder();
                    holder2.title = convertView.findViewById(R.id.title);
                    holder2.money = convertView.findViewById(R.id.money);
                    holder2.type = convertView.findViewById(R.id.type);
                    holder2.time = convertView.findViewById(R.id.time);
                    holder2.iv = convertView.findViewById(R.id.iv);
                    convertView.setTag(holder2);
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case TYPE_TITLE:
                    holder1 = (TitleViewHolder) convertView.getTag();
                    break;
                case TYPE_COMPANY:
                    holder2 = (ComViewHolder) convertView.getTag();
                    break;

                default:
                    break;
            }

        }
        if (list.get(position).type){
          //  holder1.times.setEnabled(false);



            holder1.title.setText(list.get(position).dataTitle);


        }else {
            TransactionNum transactionNum  = new TransactionNum();
            transactionNum.nums=list.get(position).num;
            holder2.title.setTag(transactionNum);
            holder2.money.setText(list.get(position).money);
            holder2.time.setText(list.get(position).time);

            holder2.type.setText(list.get(position).sourceType);
            if (types==0){
                holder2.iv.setImageResource(R.mipmap.deposits);
            }else {
                holder2.iv.setImageResource(R.mipmap.consumption);
            }

        }
        return convertView;

    }

    class TitleViewHolder {

        TextView title;
        RelativeLayout times;
    }

    class ComViewHolder {
        TextView type;
        ImageView iv;
        TextView money;
        TextView time;
        RelativeLayout title;
    }

}
