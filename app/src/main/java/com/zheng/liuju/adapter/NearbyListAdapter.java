package com.zheng.liuju.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.zheng.liuju.R;
import com.zheng.liuju.activity.NearbyOutletsListActivity;
import com.zheng.liuju.bean.ShopBean;

import java.util.ArrayList;
import java.util.List;

public class NearbyListAdapter extends BaseAdapter {
    private AppCompatActivity context;//声明适配器中引用的上下文
    private ArrayList<ShopBean.DataBean> data;
    //通过构造方法初始化上下文
    public NearbyListAdapter(AppCompatActivity context, ArrayList<ShopBean.DataBean> data) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.nearby_item,null);
            holder.shopLogo = (ImageView) convertView.findViewById(R.id.shopLogo);
            holder. shopName = (TextView) convertView.findViewById(R.id.shopName);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.available  =(TextView) convertView.findViewById(R.id.available);
            holder.returns = (TextView) convertView.findViewById(R.id.returns);
            holder.distance = (TextView) convertView.findViewById(R.id.distance);
            holder.navigation  =(TextView) convertView.findViewById(R.id.navigation);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NearbyOutletsListActivity activity  = (NearbyOutletsListActivity) context;
                    navigations(activity.getLatitude(context),activity.getLongitude(context)
                            ,data.get(i).getLat(),data.get(i).getLng());
                    //   Log.e("")


            }
        });


        Glide.with(context).load(data.get(i).getLogo()).error(R.mipmap.shoplogo)
        .placeholder(R.mipmap.shoplogo)  .into( holder.shopLogo);
        holder. shopName.setText(data.get(i).getShopname());
        holder.address.setText(data.get(i).getAddress());
        holder. time.setText(data.get(i).getServiceTime()+"");
        holder.available.setText(context.getResources().getString(R.string.Available)+": "+data.get(i).getBatteryCount());
        holder.returns.setText(context.getResources().getString(R.string.Rrturn)+": "+data.get(i).getReturnCount());
        if ((int)data.get(i).getDistance()>=1000){
            int  dis =     (int)data.get(i).getDistance()/1000;
            holder. distance.setText(dis+ "km");
        }else {
            holder. distance.setText((int)data.get(i).getDistance() + "m");
        }

        return convertView;

    }
    private  class  ViewHolder{
        public ImageView shopLogo;
        public TextView shopName;
        public TextView address;
        public  TextView  time;
        public TextView available;
        public  TextView  returns;
        public TextView  distance;
        public  TextView navigation;
        public  ImageView  addres;
        public  TextView   borrowtv;
        public  ImageView  borrows;
        public  ImageView  alsos;
        public  TextView   returntv;
        public  ImageView  isnavigation;
        public  TextView   navigationtv;
    }

    private void navigations(String latitude,String longitude,String Latitude,String Longitude) {
        if (isAvilible(context, "com.google.android.apps.maps")) {

            Uri parse = Uri.parse("http://maps.google.com/maps?f=d&source=s_d" +
                    "&saddr=" + latitude + "," + longitude + "&daddr=" + Latitude+ "," + Longitude + "&hl=zh&t=m&dirflg=d");
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    parse);
            intent.setPackage("com.google.android.apps.maps");
            context.startActivity(intent);


        } else {
         NearbyOutletsListActivity activity  = (NearbyOutletsListActivity) context;
            activity.shows(activity.getResources().getString(R.string.noGoogleMap), false);


        }
    }

    public static boolean isAvilible(Context context, String packageName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);

    }

}
