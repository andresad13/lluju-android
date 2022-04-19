package com.zheng.liuju.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.cardview.widget.CardView;

import com.zheng.liuju.R;

import com.zheng.liuju.adapter.RechargeCardListAdapter;
import com.zheng.liuju.bean.CardList;
import com.zheng.liuju.bean.RechargeCardListBean;



import java.util.ArrayList;
import java.util.List;

public class DialogUtils implements  View.OnClickListener{
    private static OnButtonClickListener onButtonClickListener;
    private AlertDialog customerServiceDialog;
    private AlertDialog avatarDialog;
    private AlertDialog nickNameDialog;
    private AlertDialog emailDialog;
    private TextView unbing;
    private ArrayList<RechargeCardListBean> cardListBeans;
    private RechargeCardListAdapter cardListAdapter;
    private ListView cardLists;
    private LinearLayout other;

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        DialogUtils.onButtonClickListener = onButtonClickListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dismiss:
                dissmissService();
                break;
            case R.id.takePicture:
                dissmissAvatar();
                onButtonClickListener.onPositiveButtonClick(0,"",null);
                break;
            case R.id.album:
                dissmissAvatar();
                onButtonClickListener.onPositiveButtonClick(1,"",null);
                break;
            case R.id.other:

                onButtonClickListener.onPositiveButtonClick(4,"",customerServiceDialog);
                break;
            case R.id.cv:
                onButtonClickListener.onPositiveButtonClick(6,"",customerServiceDialog);
                break;
        }
    }

    private void dissmissAvatar() {
        if (avatarDialog!=null){
            avatarDialog.dismiss();
        }
    }

    private void dissmissService() {
        if (customerServiceDialog!=null){
            customerServiceDialog.dismiss();
        }
        if (nickNameDialog!=null){
            nickNameDialog.dismiss();
        }
        if (emailDialog!=null){
            emailDialog.dismiss();
        }
    }

    /**
     * 按钮点击回调接口
     */
    public interface OnButtonClickListener {
        void onPositiveButtonClick(int type,String  context,AlertDialog dialog);

        // void onNegativeButtonClick(AlertDialog dialog);
    }
    //客服弹框
    public   void  showCustomerService(Context  context,String phone,String Working){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog);
        customerServiceDialog = builder.create();
        customerServiceDialog.setCancelable(true);
        customerServiceDialog.show();
        View view = LayoutInflater.from(context).inflate(R.layout.view_customer_service, null);
        TextView phones = view.findViewById(R.id.phone);
        TextView tel = view.findViewById(R.id.tel);
        TextView workingHours = view.findViewById(R.id.workingHours);
        RelativeLayout dismiss = view.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(this);
        phones.setText(phone);
        workingHours.setText(Working);
        customerServiceDialog.getWindow().setContentView(view);
        tel.setOnClickListener(v -> callPhone(context,phone));
    }
    /**
     * 拨打电话（直接拨打电话）
     * @param phoneNum 电话号码
     */
    @SuppressLint("MissingPermission")
    public void callPhone(Context  context, String phoneNum){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context. startActivity(intent);

    }


    //提现
    public   void  showWithdraw(Context  context,String tip){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog);
        customerServiceDialog = builder.create();
        customerServiceDialog.setCancelable(true);
        customerServiceDialog.show();
        View view = LayoutInflater.from(context).inflate(R.layout.view_delete_credits, null);

        RelativeLayout dismiss = view.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(this);
        unbing = view.findViewById(R.id.unbing);
        TextView phone = view.findViewById(R.id.phone);
        if (tip.equals("")){
            phone.setText(context.getResources().getString(R.string.deleteTips));
        }else {
            phone.setText(Html.fromHtml(tip));
        }
        // unbing.setOnClickListener(v -> onButtonClickListener.onPositiveButtonClick(1,"",customerServiceDialog));
        unbing.setOnClickListener(v -> {
            onButtonClickListener.onPositiveButtonClick(5,"",customerServiceDialog);

        });
        customerServiceDialog.getWindow().setContentView(view);

    }
    //头像弹框
    public   void   showAvatar(Context  context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog);
        avatarDialog = builder.create();
        avatarDialog.setCancelable(true);
        avatarDialog.show();
        View view = LayoutInflater.from(context).inflate(R.layout.view_avatar, null);
        TextView  takePicture  = view.findViewById(R.id.takePicture);
        TextView  album  = view.findViewById(R.id.album);
        takePicture.setOnClickListener(this);
        album.setOnClickListener(this);
        avatarDialog.getWindow().setContentView(view);
    }
      //编辑昵称弹框

      public   void   showNickName(Context  context,String text){
          AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog);
          nickNameDialog = builder.create();
          nickNameDialog.setCancelable(true);
          nickNameDialog.show();
//只用下面这一行弹出对话框时需要点击输入框才能弹出软键盘
          nickNameDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//加上下面这一行弹出对话框时软键盘随之弹出
          nickNameDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
          View view = LayoutInflater.from(context).inflate(R.layout.view_nick_name, null);
          EditText nick_name   = view.findViewById(R.id.edit_name);
          nick_name.setText(text);
          TextView num  = view.findViewById(R.id.num);
          RelativeLayout dismiss = view.findViewById(R.id.dismiss);
          dismiss.setOnClickListener(this);
          num.setText(text.length()+"/10");
          nick_name.addTextChangedListener(new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence s, int start, int count, int after) {

              }

              @Override
              public void onTextChanged(CharSequence s, int start, int before, int count) {
                  num.setText(s.length()+"/10");
              }

              @Override
              public void afterTextChanged(Editable s) {

              }
          });

          CardView  determine  = view.findViewById(R.id.determine);
          determine.setOnClickListener(v -> onButtonClickListener.onPositiveButtonClick(2,nick_name.getText().toString().trim(),nickNameDialog));
          showSoftInputFromWindow(nick_name);

          nickNameDialog.getWindow().setContentView(view);

      }

    public void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);

    }

    //编辑邮箱弹框
    public   void   showEmail(Context  context,String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog);
        emailDialog = builder.create();
        emailDialog.setCancelable(true);
        emailDialog.show();
//只用下面这一行弹出对话框时需要点击输入框才能弹出软键盘
        emailDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//加上下面这一行弹出对话框时软键盘随之弹出
        emailDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        View view = LayoutInflater.from(context).inflate(R.layout.view_email, null);
        EditText edit_name   = view.findViewById(R.id.edit_name);
        edit_name.setText(text);
        RelativeLayout dismiss = view.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(this);
        CardView  determine  = view.findViewById(R.id.determine);
        ImageView cut = view.findViewById(R.id.cut);
        cut.setOnClickListener(v -> edit_name.setText(""));
        determine.setOnClickListener(v -> onButtonClickListener.onPositiveButtonClick(3,edit_name.getText().toString().trim(),emailDialog));
        showSoftInputFromWindow(edit_name);
        emailDialog.getWindow().setContentView(view);
    }



    //输入安全码弹框
    //编辑邮箱弹框
    public   void   showSecurityCode(Context  context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog);
        emailDialog = builder.create();
        emailDialog.setCancelable(true);
        emailDialog.show();
//只用下面这一行弹出对话框时需要点击输入框才能弹出软键盘
        emailDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//加上下面这一行弹出对话框时软键盘随之弹出
        emailDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        View view = LayoutInflater.from(context).inflate(R.layout.view_security, null);
        EditText edit_name   = view.findViewById(R.id.edit_name);

        RelativeLayout dismiss = view.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(this);
        CardView  determine  = view.findViewById(R.id.determine);
        ImageView cut = view.findViewById(R.id.cut);
        cut.setOnClickListener(v -> edit_name.setText(""));
        determine.setOnClickListener(v -> onButtonClickListener.onPositiveButtonClick(3,edit_name.getText().toString().trim(),emailDialog));
        showSoftInputFromWindow(edit_name);
        emailDialog.getWindow().setContentView(view);
    }


    //遗失购买弹框
    public   void  showLostPurchase(Context  context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog);
        customerServiceDialog = builder.create();
        customerServiceDialog.setCancelable(true);
        customerServiceDialog.show();
        View view = LayoutInflater.from(context).inflate(R.layout.view_lost_purchase, null);
        CardView  cv  =view.findViewById(R.id.cv);
        cv.setOnClickListener(this);
        RelativeLayout dismiss = view.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(this);

        customerServiceDialog.getWindow().setContentView(view);

    }

    //删除信用卡弹框
    public   void  showDeleteCreditCard(Context  context,String tip){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog);
        customerServiceDialog = builder.create();
        customerServiceDialog.setCancelable(true);
        customerServiceDialog.show();
        View view = LayoutInflater.from(context).inflate(R.layout.view_delete_credit, null);

        RelativeLayout dismiss = view.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(this);
        unbing = view.findViewById(R.id.unbing);
        TextView phone = view.findViewById(R.id.phone);
        if (tip.equals("")){
            phone.setText(context.getResources().getString(R.string.deleteTips));
        }else {
            phone.setText(Html.fromHtml(tip));
        }
       // unbing.setOnClickListener(v -> onButtonClickListener.onPositiveButtonClick(1,"",customerServiceDialog));
        unbing.setOnClickListener(v -> {
            if (tip.contains(context.getResources().getString(R.string.depositsTip))){
                onButtonClickListener.onPositiveButtonClick(5,"",customerServiceDialog);
            }else if (tip.contains(context.getResources().getString(R.string.balanceTip))){
                onButtonClickListener.onPositiveButtonClick(1,"",customerServiceDialog);
            }else {
                onButtonClickListener.onPositiveButtonClick(11,"",customerServiceDialog);
            }
        });
        customerServiceDialog.getWindow().setContentView(view);

    }


    //删除信用卡弹框
    public   void  showDeleteCreditCards(Context  context,String tip){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog);
        customerServiceDialog = builder.create();
        customerServiceDialog.setCancelable(true);
        customerServiceDialog.show();
        View view = LayoutInflater.from(context).inflate(R.layout.view_delete_credit, null);

        RelativeLayout dismiss = view.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(this);
        unbing = view.findViewById(R.id.unbing);
        TextView phone = view.findViewById(R.id.phone);
      //  if (tip.equals("")){
            phone.setText(tip);
      /*  }else {
            phone.setText(Html.fromHtml(tip));
        }*/
        // unbing.setOnClickListener(v -> onButtonClickListener.onPositiveButtonClick(1,"",customerServiceDialog));
        unbing.setOnClickListener(v -> {
            if (tip.contains(context.getResources().getString(R.string.addCardTip))){
                onButtonClickListener.onPositiveButtonClick(7,"",customerServiceDialog);
            }else if (tip.contains(context.getResources().getString(R.string.balanceTip))){
                onButtonClickListener.onPositiveButtonClick(8,"",customerServiceDialog);
            }
        });
        customerServiceDialog.getWindow().setContentView(view);

    }
    //选择银行卡
    public   void  showSelectBankCard(Context  context, List<CardList.DataBean.CardsBean> cardList,String  balance){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialog);
        customerServiceDialog = builder.create();
        customerServiceDialog.setCancelable(true);
        customerServiceDialog.show();
        View view = LayoutInflater.from(context).inflate(R.layout.view_select_credit, null);
        cardLists = view.findViewById(R.id.payList);

        other = view.findViewById(R.id.other);
        other.setOnClickListener(this);
        if (cardList.size()>=3){
            other.setVisibility(View.GONE);
        }else {
            other.setVisibility(View.VISIBLE);
        }
        cardListBeans = new ArrayList<>();

        if (balance!=null){
            RechargeCardListBean rechargeCardListBean = new RechargeCardListBean();
            rechargeCardListBean.id ="";
            //   rechargeCardListBean.cardNumberBin = "****";
            rechargeCardListBean.cardBrand = "";
            rechargeCardListBean.type = false;
            rechargeCardListBean.CardNumberLast4 = context.getResources().getString(R.string.Purse)+"（$"+balance+")";
            rechargeCardListBean.thumbnail="balance";
            cardListBeans.add(rechargeCardListBean);
        }
if (cardList!=null){
    for (int i = 0; i < cardList.size(); i++) {
        RechargeCardListBean rechargeCardListBean = new RechargeCardListBean();
        rechargeCardListBean.id = cardList.get(i).getId()+"";
        //   rechargeCardListBean.cardNumberBin = "****";
        rechargeCardListBean.cardBrand = cardList.get(i).getIssuer().getName();
        rechargeCardListBean.type = false;
        rechargeCardListBean.CardNumberLast4 ="**** **** ****"+ cardList.get(i).getLastFourDigits();
        rechargeCardListBean.thumbnail=cardList.get(i).getPaymentMethod().getThumbnail();
        cardListBeans.add(rechargeCardListBean);
    }
}

        cardListAdapter = new RechargeCardListAdapter(context, cardListBeans);
        cardLists.setAdapter(cardListAdapter);
        cardLists.setOnItemClickListener((parent, view1, position, id) -> {
            for (RechargeCardListBean feedBackBean : cardListBeans) {
                feedBackBean.type = false;
            }
            cardListAdapter.notifyDataSetChanged();
            RechargeCardListBean feedBackBean = new RechargeCardListBean();
            feedBackBean.type = true;
            feedBackBean.CardNumberLast4 = cardListBeans.get(position).CardNumberLast4;
           // feedBackBean.cardNumberBin = cardListBeans.get(position).cardNumberBin;
            feedBackBean.cardBrand = cardListBeans.get(position).cardBrand;
            feedBackBean.id = cardListBeans.get(position).id;
           String cardId=cardListBeans.get(position).id;
            cardListBeans.set(position, feedBackBean);
            cardListAdapter.notifyDataSetChanged();

            onButtonClickListener.onPositiveButtonClick(10,cardId,customerServiceDialog);
        });
      /*  RelativeLayout dismiss = view.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(this);*/
        /*unbing = (TextView) view.findViewById(R.id.unbing);

        unbing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClickListener.onPositiveButtonClick(9,"",customerServiceDialog);
            }
        });*/
        customerServiceDialog.getWindow().setContentView(view);

    }
}
