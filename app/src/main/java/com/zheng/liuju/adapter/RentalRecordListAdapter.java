package com.zheng.liuju.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zheng.liuju.R;
import com.zheng.liuju.bean.OrderListBean;

import java.util.ArrayList;

public class RentalRecordListAdapter extends BaseAdapter {
    private Context context;//声明适配器中引用的上下文
    private ArrayList<OrderListBean.DataBean> data;
    public RentalRecordListAdapter(Context context, ArrayList<OrderListBean.DataBean> data) {      //
        this.context = context;
        this.data = data;

    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder = null;
        if (convertView == null) {
           holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.rental_record_list_item, null);
            holder.orderStatus = (TextView) convertView.findViewById(R.id.orderStatus);
            holder.orderNunber = (TextView) convertView.findViewById(R.id.orderNunber);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.RentalBusiness = (TextView) convertView.findViewById(R.id.RentalBusiness);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
          holder.orderNunber.setText( data.get(position).getOrderNum());
        holder.time.setText(data.get(position).getStart());
        holder.RentalBusiness.setText(data.get(position).getShopName());

       if (data.get(position).getOrderState().contains("租借中")) {
           holder.orderStatus.setText(context.getResources().getString(R.string.rent));
           holder.orderStatus.setTextColor(Color.parseColor("#63b15e"));
        } else if (data.get(position).getOrderState().contains("已归还")) {
           holder.orderStatus.setText(context.getResources().getString(R.string.Returned));
           holder.orderStatus.setTextColor(Color.parseColor("#000000"));
        } else if (data.get(position).getOrderState().contains("已撤销")) {
           holder.orderStatus.setText(context.getResources().getString(R.string.revoked));
           holder.orderStatus.setTextColor(Color.parseColor("#000000"));
        } else if (data.get(position).getOrderState().contains("存疑")) {
           holder.orderStatus.setText(context.getResources().getString(R.string.doubt));
           holder.orderStatus.setTextColor(Color.parseColor("#f45344"));
        } else if (data.get(position).getOrderState().contains("购买单")) {
           holder.orderStatus.setText(context.getResources().getString(R.string.purchase));
           holder.orderStatus.setTextColor(Color.parseColor("#000000"));
        }else if (data.get(position).getOrderState().contains("购买订单")) {
           holder.orderStatus.setText(context.getResources().getString(R.string.purchase));
           holder.orderStatus.setTextColor(Color.parseColor("#000000"));
       }

       else if (data.get(position).getOrderState().contains("故障单")) {
           holder.orderStatus.setText(context.getResources().getString(R.string.Failure));
           holder.orderStatus.setTextColor(Color.parseColor("#f45344"));
        } else if (data.get(position).getOrderState().contains("超时单")) {  //timeout
           holder.orderStatus.setText(context.getResources().getString(R.string.timeout));
           holder.orderStatus.setTextColor(Color.parseColor("#f45344"));
        } else if (data.get(position).getOrderState().contains("已支付")) {  //payed
           holder.orderStatus.setText(context.getResources().getString(R.string.payed));
           holder.orderStatus.setTextColor(Color.parseColor("#63b15e"));
        } else {
           holder.orderStatus.setText(data.get(position).getOrderState()+"");
        }
        return convertView;
    }

    private  class  ViewHolder{
        public TextView orderStatus;
        public TextView orderNunber;
        public TextView time;
        public  TextView  RentalBusiness;

    }
    }
