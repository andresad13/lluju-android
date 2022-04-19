package com.zheng.liuju.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zheng.liuju.R;
import com.zheng.liuju.bean.CardList;

import java.util.List;

public class CardListAdapter extends BaseAdapter {

    private Context context;//声明适配器中引用的上下文
    private List<CardList.DataBean> data;
    //通过构造方法初始化上下文
    public CardListAdapter(Context context, List<CardList.DataBean> data) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.card_list_adapt,null);
            holder.cardnumber = (TextView) convertView.findViewById(R.id.cardnumber);
            holder.expirationdate = (TextView) convertView.findViewById(R.id.expirationdate);
            holder.visacard = (ImageView) convertView.findViewById(R.id.visacard);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
      /*  holder.cardnumber.setText("**** **** **** "+data.get(i).getLast_4_digits());
    //    holder.expirationdate.setText(data.get(i).getCardExpMonth()+"/"+data.get(i).getCardExpYear());
        if (data.get(i).getBank().contains("Visa")){
            holder.visacard.setImageResource(R.mipmap.visacard);
        }else if (data.get(i).getBank().contains("maestro")){
            holder.visacard.setImageResource(R.mipmap.mastercard);
        }else {
            holder.visacard.setImageResource(R.mipmap.jcb);
        }*/
        return convertView;

    }
    private  class  ViewHolder{
        public TextView cardnumber;
        public  TextView  expirationdate;
        public  ImageView visacard;

    }
}
