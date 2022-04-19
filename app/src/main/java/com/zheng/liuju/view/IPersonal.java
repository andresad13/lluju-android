package com.zheng.liuju.view;

import com.zheng.liuju.bean.Infos;

public interface IPersonal extends IBaseView  {
    void  permissionSuccess(int type);
    //弹框提示
    void showDialog(String  context,boolean type);
    //网络加载的dialog
    void showLoading();
    void dismissLoading();
   void  upData();
    void  onErr();
    void upInformation(Infos.DataBean list);
    void upAvatar(String url,String path);

}
