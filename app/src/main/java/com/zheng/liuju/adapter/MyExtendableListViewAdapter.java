package com.zheng.liuju.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.zheng.liuju.R;
import com.zheng.liuju.bean.CommonProblemBean;

import java.util.ArrayList;

public class MyExtendableListViewAdapter extends BaseExpandableListAdapter {
    private Context mcontext;
    public String[] groupString ;
    public String[][] childString ;
    private  ArrayList<CommonProblemBean> mlists;
    public MyExtendableListViewAdapter(ArrayList<CommonProblemBean> mlist, Context mcontexts){
       this. mlists = mlist;
       this.mcontext = mcontexts;
        groupString  = new String[mlists.size()];
        childString =new String[mlists.size()][1];
        for (int i = 0; i <mlist.size() ; i++) {

            groupString[i]  = mlist.get(i).groupString;
            childString[i][0] =mlist.get(i).childStrings;
        }
    }


    @Override
    // 获取分组的个数
    public int getGroupCount() {
        return groupString.length;
    }

    //获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return childString[groupPosition].length;
    }

    //        获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return groupString[groupPosition];
    }

    //获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childString[groupPosition][childPosition];
    }

    //获取指定分组的ID, 这个ID必须是唯一的
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //获取子选项的ID, 这个ID必须是唯一的
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们
    @Override
    public boolean hasStableIds() {
        return true;
    }
/**
 *
 * 获取显示指定组的视图对象
 *
 * @param groupPosition 组位置
 * @param isExpanded 该组是展开状态还是伸缩状态
 * @param convertView 重用已有的视图对象
 * @param parent 返回的视图对象始终依附于的视图组
  */
// 获取显示指定分组的视图
private    GroupViewHolder groupViewHolder;
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {



        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.partent_item,parent,false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = (TextView)convertView.findViewById(R.id.label_group_normal);
            groupViewHolder.item  = (RelativeLayout) convertView.findViewById(R.id.item);
            groupViewHolder.ic  =  (ImageView) convertView.findViewById(R.id.ic);
            groupViewHolder.circle  =  (TextView) convertView.findViewById(R.id.circle);
            convertView.setTag(groupViewHolder);
        }else {
            groupViewHolder = (GroupViewHolder)convertView.getTag();
        }
        groupViewHolder.tvTitle.setText(groupString[groupPosition]);
        groupViewHolder.circle.setText( groupPosition + 1+"");
     if (mlists.get(groupPosition).type){
         //groupViewHolder.item.setBackground(R.drawable.common);
     /*    Resources resources = mcontext.getResources();
         Drawable drawable = resources.getDrawable(R.dra);
         groupViewHolder.item.setBackgroundDrawable(drawable);*/
         groupViewHolder.ic.setImageResource(R.mipmap.tops);
     }else {

         groupViewHolder.ic.setImageResource(R.mipmap.bottoms);

      /*   Resources resources = mcontext.getResources();
         Drawable drawable = resources.getDrawable(R.drawable.common);
         groupViewHolder.item.setBackgroundDrawable(drawable);*/
     }


        return convertView;
    }


    /**
     *
     * 获取一个视图对象，显示指定组中的指定子元素数据。
     *
     * @param groupPosition 组位置
     * @param childPosition 子元素位置
     * @param isLastChild 子元素是否处于组中的最后一个
     * @param convertView 重用已有的视图(View)对象
     * @param parent 返回的视图(View)对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, View,
     *      ViewGroup)
     */

    //取得显示给定分组给定子位置的数据用的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item,parent,false);

            childViewHolder = new ChildViewHolder();

            childViewHolder.tvTitle = (TextView)convertView.findViewById(R.id.expand_child);
            convertView.setTag(childViewHolder);

        }else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.tvTitle.setText(childString[groupPosition][childPosition]);
        return convertView;
    }

    //指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        TextView tvTitle;
        RelativeLayout item;
        ImageView  ic;
        TextView circle;
    }

    static class ChildViewHolder {
        TextView tvTitle;

    }

    public   void  notfig( ArrayList<CommonProblemBean> mlistss){
        this.mlists = mlistss;
        mhandler.sendEmptyMessage(1);

    }

    private Handler   mhandler  =  new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            notifyDataSetChanged();
        }
    };
}