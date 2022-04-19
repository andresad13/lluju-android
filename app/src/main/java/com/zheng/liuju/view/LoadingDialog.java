package com.zheng.liuju.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.wang.avi.AVLoadingIndicatorView;
import com.zheng.liuju.R;


/**
 * Created by 李浩 on 2019/4/24.
 */

public class LoadingDialog extends Dialog {

    private AVLoadingIndicatorView load;

    public LoadingDialog(Context context) {

        this(context,R.style.dialogs);//R.style.loading_dialog
        //  tvTip = (TextView)this.findViewById(R.id.tvTip);
    }

    private LoadingDialog(Context context, int theme) {
        super(context, theme);

    }

    private String mTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        TextView tvTip = this.findViewById(R.id.tvTip);
        tvTip.setText("");

        LinearLayout linearLayout = this.findViewById(R.id.LinearLayout);
        load = this.findViewById(R.id.load);
        load.show();
        linearLayout.getBackground().setAlpha(210);
    }

    public void setTvTip(String tipStr) {
        mTip = tipStr;
    }

    @Override
    protected void onStop() {

        super.onStop();
    }
}
