package com.zheng.liuju.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zheng.liuju.R;
import com.zheng.liuju.activity.CreditCardListActivity;
import com.zheng.liuju.bean.CardList;


import java.util.List;

public class CreditCardListAdapter extends BaseAdapter {
    private Context context;//声明适配器中引用的上下文
    private List<CardList.DataBean.CardsBean> data;
    //通过构造方法初始化上下文
    public CreditCardListAdapter(Context context, List<CardList.DataBean.CardsBean> data) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.credit_card_adapt,null);
            holder.cardnumber = (TextView) convertView.findViewById(R.id.cardnumber);
            holder.cardName = (TextView) convertView.findViewById(R.id.cardName);
            holder.visacard = (ImageView) convertView.findViewById(R.id.visacard);
            holder.delete_button =(RelativeLayout) convertView.findViewById(R.id.delete_button);
            holder.expiration =convertView.findViewById(R.id.expiration);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.cardnumber.setText("**** **** **** "+data.get(i).getLastFourDigits());
        holder.expiration.setText(data.get(i).getExpirationMonth()+"/"+data.get(i).getExpirationYear());
      //  if (data.get(i).getBank().contains("Visa")){
            holder.cardName.setText(data.get(i).getIssuer().getName());
          //  holder.visacard.setImageResource(data.get(i).getPaymentMethod().getThumbnail());
        Glide.with(context).load(data.get(i).getPaymentMethod().getThumbnail()).into(holder.visacard);
      /*  }else if (data.get(i).getBank().contains("MasterCard")){
            holder.cardName.setText(context.getResources().getString(R.string.Maestro));
            holder.visacard.setImageResource(R.mipmap.mastercard);
        }else {
            holder.cardName.setText(context.getResources().getString(R.string.jcb));
            holder.visacard.setImageResource(R.mipmap.jcb);
        }*/
        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String id = data.get(i).getId();
                CreditCardListActivity    creditCardListActivity =    (CreditCardListActivity)context;
               creditCardListActivity.deleteCard(id+"");
            }
        });
        return convertView;

    }
    private  class  ViewHolder{
        public TextView cardnumber;
        public  TextView cardName;
        public  ImageView visacard;
        public RelativeLayout delete_button;
        public  TextView expiration;

    }
}
