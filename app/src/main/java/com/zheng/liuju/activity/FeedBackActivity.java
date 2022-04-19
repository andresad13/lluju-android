package com.zheng.liuju.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gigamole.library.ShadowLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.zheng.liuju.R;
import com.zheng.liuju.adapter.FeedBackAdapter;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.bean.FeedBackBean;
import com.zheng.liuju.bean.FeedBackData;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.presenter.FeedBackPresenter;
import com.zheng.liuju.util.SPUtils;
import com.zheng.liuju.view.DragImageView;
import com.zheng.liuju.view.IFeedBackView;
import com.zheng.liuju.view.LoadingDialog;
import com.zheng.liuju.view.MyGridView;
import com.zheng.liuju.view.PromptDialog;
import com.zheng.liuju.view.TitleView;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoActivity;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedBackActivity extends TakePhotoActivity implements TakePhoto.TakeResultListener,
        InvokeListener, IFeedBackView, View.OnTouchListener {
    @BindView(R.id.title)
    TitleView title;
    @BindView(R.id.gv)
    MyGridView gv;
    @BindView(R.id.noScrollgridview)
    GridView gridview;
    @BindView(R.id.sv)
    ScrollView sv;
    @BindView(R.id.scan)
    ImageView scan;
    @BindView(R.id.home)
    DragImageView home;
    private ArrayList<String> bmp = new ArrayList<String>();
    @BindView(R.id.cv)
    ShadowLayout cv;
    @BindView(R.id.deviceID)
    EditText deviceID;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.content)
    EditText content;
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;
    private float dp;
    private GridAdapter adapter;
    private TakePhoto takePhoto;
    private PromptDialog promptDialog;
    private ArrayList<FeedBackBean> mlists = new ArrayList<>();
    private FeedBackPresenter presenter;
    private FeedBackAdapter feedBackAdapter;
    private String opinionName = "";
    private LoadingDialog loadingDialog;
    private static final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
        initView();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                if (promptDialog != null) {
                    promptDialog.dismiss();
                    promptDialog = null;
                }
            } else if (msg.what == 2) {
                SPUtils.putString(FeedBackActivity.this, "token", "");
                startActivity(new Intent(FeedBackActivity.this, LoginActivity.class));

                EventMeager eventMeager = new EventMeager();
                eventMeager.setFinishs("finish");
                EventBus.getDefault().post(eventMeager);
            }
        }
    };

    private void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        EventBus.getDefault().register(this);
        sv.setOnTouchListener(this);
        gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridviewInit();
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Acp.getInstance(FeedBackActivity.this).request(new AcpOptions.Builder().setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .build(1), new AcpListener() {
                    @Override
                    public void onGranted() {

                        //onClick(getTakePhoto());
                        getPic();

                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        // Toast.makeText(FeedBackActivity.this, "摄像头打开失败", Toast.LENGTH_SHORT).show();
                        shows(getResources().getString(R.string.noCamera), false);

                    }
                });

            }
        });

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (FeedBackBean feedBackBean : mlists) {
                    feedBackBean.type = false;
                }
                feedBackAdapter.notifyDataSetChanged();
                FeedBackBean feedBackBean = new FeedBackBean();
                feedBackBean.feedName = mlists.get(i).feedName;
                opinionName = mlists.get(i).ids;
                feedBackBean.ids = mlists.get(i).ids;
                feedBackBean.type = true;
                mlists.set(i, feedBackBean);
                feedBackAdapter.notifyDataSetChanged();
            }
        });
        presenter = new FeedBackPresenter();
        presenter.attachView(this);
        presenter.initData(getToken(FeedBackActivity.this));
        title.setOnHeaderClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public String getContent() {

        return content.getText().toString().trim();
    }

    public String getDeviceID() {

        return deviceID.getText().toString().trim();
    }

    public String getPhone() {

        return phone.getText().toString().trim();
    }

    /**
     * 获取经度
     *
     * @param context 上下文
     * @return 经度值
     */
    public String getLongitude(Context context) {
        String longitude = SPUtils.getString(context, "longitude", "");
        return longitude;
    }

    /**
     * 获取纬度
     *
     * @param context 上下文
     * @return 纬度值
     */
    public String getLatitude(Context context) {
        String latitude = SPUtils.getString(context, "latitude", "");
        return latitude;
    }

    public String getToken(Context context) {
        String token = SPUtils.getString(context, "token", "");
        return token;
    }

    public void shows(String content, boolean type) {
        if (!isLiving(FeedBackActivity.this)) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(FeedBackActivity.this);
        }
        promptDialog.setTvTip(content + "", type);
        promptDialog.show();
        handler.sendEmptyMessageDelayed(1, 2000);
    }

    private static boolean isLiving(Activity activity) {

        if (activity == null) {
            Log.d("wisely", "activity == null");
            return false;
        }

        if (activity.isFinishing()) {
            Log.d("wisely", "activity is finishing");
            return false;
        }
        return true;
    }

    private void getPic() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);

        //  takePhoto.onPickMultiple(1);
        getTakePhoto().onPickMultipleWithCrop(3 - bmp.size(), getCropOptions(), selectedLanguage);
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }


    private CropOptions getCropOptions() {

        int height = Integer.parseInt(800 + "");
        int width = Integer.parseInt(800 + "");
        boolean withWonCrop = false;

        CropOptions.Builder builder = new CropOptions.Builder();


        builder.setOutputX(width).setOutputY(height);

        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }

    private void gridviewInit() {
        dp = getResources().getDimension(R.dimen.dp);
        adapter = new GridAdapter(this);
        adapter.setSelectedPosition(0);
        int size = 0;
        if (bmp.size() < 3) {
            size = bmp.size() + 1;
        } else {
            size = bmp.size();
        }

        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        final int width = size * (int) (dp * 9.4f);
        params.width = width;
        gridview.setLayoutParams(params);
        gridview.setColumnWidth((int) (dp * 9.4f));
        gridview.setStretchMode(GridView.NO_STRETCH);
        gridview.setNumColumns(size);
        gridview.setAdapter(adapter);
    }

    @Override
    public void showDialog(String context, boolean type) {

        shows(context, type);
    }


    @Override
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(FeedBackActivity.this);
        }
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    @Override
    public void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onErr() {
        handler.sendEmptyMessageDelayed(2, 3000);
    }

    @Override
    public void upInformation(List<FeedBackData.DataBean> mlist) {
        if (mlist != null) {
            if (mlist.size() == 0) {
                shows(getResources().getString(R.string.acquisitionFailed), false);
            } else {
                for (int i = 0; i < mlist.size(); i++) {
                    FeedBackBean feedBackBean = new FeedBackBean();
                    feedBackBean.feedName = mlist.get(i).getName();
                    feedBackBean.ids = mlist.get(i).getId() + "";
                    feedBackBean.type = false;
                    mlists.add(feedBackBean);
                }
                feedBackAdapter = new FeedBackAdapter(FeedBackActivity.this, mlists);
                gv.setAdapter(feedBackAdapter);
            }
        } else {
            shows(getResources().getString(R.string.acquisitionFailed), false);
        }
       /* if (mlist == null | data.size() == 0) {
            Toast.makeText(this, getResources().getString(R.string.acquisitionFailed), Toast.LENGTH_SHORT).show();
            return;
        } else {
            for (int i = 0; i < data.size(); i++) {
                FeedBackBean feedBackBean = new FeedBackBean();
                feedBackBean.feedName = data.get(i).getName();
                feedBackBean.ids = data.get(i).getName() + "";
                feedBackBean.type = false;
                mlist.add(feedBackBean);
            }
            feedBackAdapter = new FeedBackAdapter(FeedBackActivity.this, mlist);
            gv.setAdapter(feedBackAdapter);
        }*/
    }

    @Override
    public void cameraPermissionSuccess() {
        Intent intent = new Intent(FeedBackActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @OnClick({R.id.cv,R.id.home})
    public void onViewClicked(View view) {
        Log.e("我进来了", "我进来了");

        switch (view.getId()){
            case R.id.cv:
                presenter.sendData(bmp, getToken(FeedBackActivity.this),
                        getLatitude(FeedBackActivity.this), getLongitude(FeedBackActivity.this)
                        , getDeviceID(), opinionName, getPhone(), getContent(), FeedBackActivity.this);
                break;

            case R.id.home:
                EventMeager eventMeager = new EventMeager();

                eventMeager.setHome(true);

                EventBus.getDefault().post(eventMeager);
                break;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        inputClose(sv, FeedBackActivity.this);
        return false;
    }

    public static void inputClose(View view, Context context) {
        if (view instanceof EditText) {
            view.clearFocus();
        }
        try {
            InputMethodManager im = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.scan)
    public void onClick() {
        presenter.cameraRequestPermission(FeedBackActivity.this, selectedLanguage);
    }

    public class GridAdapter extends BaseAdapter {
        private LayoutInflater listContainer;
        private int selectedPosition = -1;
        private boolean shape;

        public GridAdapter(Context context) {
            listContainer = LayoutInflater.from(context);
        }

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public class ViewHolder {
            public ImageView image;
            public Button bt;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        @Override
        public int getCount() {
            if (bmp.size() < 3) {
                return bmp.size() + 1;
            } else {
                return bmp.size();
            }
            // return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int sign = position;
            // 自定义视图
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                // 获取list_item布局文件的视图

                convertView = listContainer.inflate(
                        R.layout.item_published_grida, null);

                // 获取控件对象
                holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
                holder.bt = (Button) convertView.findViewById(R.id.item_grida_bt);
                // 设置控件集到convertView
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == bmp.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.mipmap.addpic));
                holder.bt.setVisibility(View.GONE);
                if (position == 3) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                Bitmap bitmap = BitmapFactory.decodeFile(bmp.get(position));
                holder.image.setImageBitmap(bitmap);
                holder.bt.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // PhotoActivity.bitmap.remove(sign);

                        bmp.remove(sign);
                        //drr.remove(sign);

                        gridviewInit();
                    }
                });
            }

            return convertView;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        presenter.detachView();
        presenter = null;
        handler.removeMessages(1);
        handler.removeMessages(2);
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if (promptDialog != null) {
            promptDialog.dismiss();
        }
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {
        if (message.getFinishs() != null) {
            finish();


        }else if (message.isHome()){

            finish();
        }
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);

        Log.e("我要的数据", result.getImages().get(0).getOriginalPath());


        for (int i = 0; i < result.getImages().size(); i++) {
            bmp.add(result.getImages().get(i).getOriginalPath());
        }
        gridviewInit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                String result = bundle.getString("result_string");
                Log.e("result", result + "");

                scanResult(result);

            }
        }
    }

    private void scanResult(String result) {
        if (result != null) {
            String[] split = result.split("/");
            int code = split.length - 1;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    deviceID.setText(split[code]);
                }
            });


        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showDialog(getResources().getString(R.string.acquisitionFailed), false);
                }
            });

        }
    }
}
