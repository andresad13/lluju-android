package com.zheng.liuju.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zheng.liuju.R;


public class PromptDialog extends Dialog {

    private boolean types;

    public PromptDialog(Context context) {

        this(context, R.style.dialogs);//R.style.loading_dialog
        //  tvTip = (TextView)this.findViewById(R.id.tvTip);
    }

    private PromptDialog(Context context, int theme) {
        super(context, theme);

    }

    private String mTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_prompt);
        TextView tvTip = (TextView) this.findViewById(R.id.tvTip);
        tvTip.setText(mTip+"");
        ImageView gf = (ImageView)this.findViewById(R.id.gf);
        if (types){
            gf.setImageResource(R.mipmap.corrects);
        }else {
            gf.setImageResource(R.mipmap.errors);
        }

        LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.LinearLayout);
        linearLayout.getBackground().setAlpha(210);
    }

    public void setTvTip(String tipStr,boolean  type) {
        mTip = tipStr;
        types = type;
    }
}
