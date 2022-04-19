package com.zheng.liuju.activity;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gigamole.library.ShadowLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.gyf.immersionbar.ImmersionBar;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.zheng.liuju.R;
import com.zheng.liuju.adapter.MapAdapter;
import com.zheng.liuju.adapter.MapTitleAdapter;
import com.zheng.liuju.base.BaseActivity;
import com.zheng.liuju.bean.AddAmountBean;
import com.zheng.liuju.bean.CardList;
import com.zheng.liuju.bean.EventMeager;
import com.zheng.liuju.bean.Infos;
import com.zheng.liuju.bean.RulesBean;
import com.zheng.liuju.bean.ShopBean;
import com.zheng.liuju.languageUtils.LanguageType;
import com.zheng.liuju.languageUtils.MultiLanguageUtil;
import com.zheng.liuju.presenter.MainPresenter;
import com.zheng.liuju.util.SPUtils;
import com.zheng.liuju.utils.Utils;
import com.zheng.liuju.view.ConstomDialog;
import com.zheng.liuju.view.DialogUtils;
import com.zheng.liuju.view.IMainView;
import com.zheng.liuju.view.LoadingDialog;
import com.zheng.liuju.view.PromptDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class MainActivity extends BaseActivity implements IMainView, DrawerLayout.DrawerListener,
        OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraIdleListener,
        OnCompleteListener<Location>, DialogUtils.OnButtonClickListener {
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.avatar)
    SimpleDraweeView avatar;
    @BindView(R.id.nearby)
    LinearLayout nearby;
    @BindView(R.id.search)
    RelativeLayout search;
    @BindView(R.id.posit)
    ImageView posit;
    @BindView(R.id.scans)
    ImageView scans;
    @BindView(R.id.scan)
    ShadowLayout scan;
    @BindView(R.id.menu)
    ImageView menu;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.ll_content_main)
    LinearLayout mLlContentMain;
    @BindView(R.id.sdview)
    SimpleDraweeView sdview;
    @BindView(R.id.menuList)
    ListView menuList;
    @BindView(R.id.loginOut)
    LinearLayout loginOut;
    @BindView(R.id.rl_menu)
    RelativeLayout mRlMenu;
    @BindView(R.id.gv)
    GridView gv;

    @BindView(R.id.my)
    RelativeLayout my;
    @BindView(R.id.purse)
    RelativeLayout purse;
    @BindView(R.id.shop)
    ImageView shop;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.tel)
    TextView tel;
    @BindView(R.id.wallet)
    TextView wallet;
    private ImageView navigation;
    private MainPresenter presenter;
    private String[] names;
    private String[] titleNames;
    private MapAdapter mapAdapter;
    private int selectedLanguage = LanguageType.LANGUAGE_SPANISH;
    //R.mipmap.hicon2
    private int[] image = {R.mipmap.hicon2
            , R.mipmap.hicon3, R.mipmap.hicon4};
    private int[] images = {R.mipmap.hicon6, R.mipmap.hicon7, R.mipmap.hicon8
            , R.mipmap.hicon9};
    private ArrayList<AddAmountBean> mlist;
    private ArrayList<AddAmountBean> mTitle;
    private MapTitleAdapter mapTitleAdapter;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private Task<Location> lastLocation;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private FusedLocationProviderClient fusedLocationClient;

    private LocationRequest mLocationRequest;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 30000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private LocationCallback locationCallback;
    private LatLng sydney;
    private Marker marker;
    private PopupWindow popupWindow;
    private static final int REQUEST_CODE = 0;
    private  boolean   locator;
    private LatLng target;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                if (promptDialog != null) {
                    promptDialog.dismiss();
                    promptDialog = null;
                }
            } else if (msg.what == 2) {
                SPUtils.putString(MainActivity.this, "token", "");
                startActivity(new Intent(MainActivity.this, LoginActivity.class));

                EventMeager eventMeager = new EventMeager();
                eventMeager.setFinishs("finish");
                EventBus.getDefault().post(eventMeager);
            }else if (msg.what == 3) {
                settags(MainActivity.this, tags);
            }
        }
    };
    private PromptDialog promptDialog;
    private  boolean    age;
    private    ArrayList<ShopBean.DataBean> mapData= new ArrayList<>();
    private DialogUtils dialogUtils;
    private AlertDialog dialogs;
    private PopupWindow cardWindow;
    private String deviceCode;


    private   boolean  agelogin;
    private LoadingDialog loadingDialog;
    private Set<String> tags;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventMeager message) {

        if (message.getFinishs() != null) {
            Log.e("finish", "finish");
            MainActivity.this.finish();


        } else if (message.getUpUser()) {

                presenter.info("0", "0", getToken(MainActivity.this));


        }else if (message.isRechargeSuccessful()){
            presenter.info("0", "0", getToken(MainActivity.this));
        }else  if (message.isRentalSuccess()){
            presenter.info("0", "0", getToken(MainActivity.this));
        }
    }

    private void initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        presenter = new MainPresenter();
        presenter.attachView(this);
        mDrawer.addDrawerListener(this);
        names = new String[]{  getResources().getString(R.string.aboutUs),
                getResources().getString(R.string.userAgreements), getResources().getString(R.string.privacyAgreement)};
        titleNames = new String[]{getResources().getString(R.string.rentalRecords), getResources().getString(R.string.cooperation), getResources().getString(R.string.useTutorial),
                getResources().getString(R.string.helpCenter)};
        mlist = new ArrayList<>();
        mTitle = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            AddAmountBean addAmountBean = new AddAmountBean();
            addAmountBean.contents = names[i];
            addAmountBean.imgae = image[i];
            mlist.add(addAmountBean);
        }
        mapAdapter = new MapAdapter(MainActivity.this, mlist);
        menuList.setAdapter(mapAdapter);
        for (int i = 0; i < images.length; i++) {
            AddAmountBean addAmountBean = new AddAmountBean();
            addAmountBean.contents = titleNames[i];
            addAmountBean.imgae = images[i];
            mTitle.add(addAmountBean);
        }

        mapTitleAdapter = new MapTitleAdapter(MainActivity.this, mTitle);
        gv.setAdapter(mapTitleAdapter);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);
        locationCallback = new LocationCallback();

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                nextGV(position);
            }
        });
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                nextList(position);
            }
        });

        Log.e("token", getToken(MainActivity.this));
        presenter.info("0", "0", getToken(MainActivity.this));
        presenter.service( getToken(MainActivity.this),MainActivity.this);

     String   openId = SPUtils.getString(MainActivity.this, "openId", "");
        tags = new HashSet<>();
        tags.add(openId);
        settags(this, tags);

    }

    public void settags(Context context, Set<String> tags) {
        TagAliasCallback callback = new TagAliasCallback() {
            @Override
            public void gotResult(int responseCode, String alias, Set<String> tags) {
                int code = responseCode;
                switch (code) {
                    case 0:
//设置成功
                        Log.e("TAG", "设置成功");
                        break;
                    case 6002:
                        Log.e("TAG", "Failed to set alias and tags due to timeout. Try again after 60s.");
//"Failed to set alias and tags due to timeout. Try again after 60s.";
                        handler.sendEmptyMessageDelayed(3, 30000);
                        break;
                    default:
//"Failed with errorCode = " + code;
                }
            }
        };
        JPushInterface.setTags(context, tags, callback);
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    private void nextList(int position) {
        switch (position) {
          //  case 0:
             //   if (Utils.isNotFastClick()) {
               //     startActivity(new Intent(MainActivity.this, CreditCardListActivity.class));
              //  }
               // break;
          /*  case 0:
                if (Utils.isNotFastClick()) {
                    startActivity(new Intent(MainActivity.this, MyShareCodeActivity.class));
                }
                break;*/
        /*    case 0:
                if (Utils.isNotFastClick()) {
                    startActivity(new Intent(MainActivity.this, FeedBackActivity.class));
                }
                break;
            case 1:
                if (Utils.isNotFastClick()) {
                    startActivity(new Intent(MainActivity.this, LanguageActivity.class));
                }
                break;*/
            case 0:
                if (Utils.isNotFastClick()) {
                    startActivity(new Intent(MainActivity.this, ABoutActivity.class));
                }
                break;
            case 1:
                if (Utils.isNotFastClick()) {
                    Intent intent = new Intent(MainActivity.this, AgreementActivity.class);
                    intent.putExtra("title", getResources().getString(R.string.userAgreements));
                    startActivity(intent);
                }
                break;

            case 2:
                if (Utils.isNotFastClick()) {
                    Intent intents = new Intent(MainActivity.this, AgreementActivity.class);
                    intents.putExtra("title", getResources().getString(R.string.privacyAgreement));
                    startActivity(intents);
                }
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        presenter.onDestroy();
        presenter = null;
        handler.removeMessages(1);
        handler.removeMessages(2);

        if (promptDialog != null) {
            promptDialog.dismiss();
        }
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        System.gc();
    }


    @Override
    public void upInformation(Infos.DataBean list) {
        try {
            if (list != null) {
                if (list.getAvatarUrl() != null) {
                    if (!list.getAvatarUrl().equals("")) {
                        sdview.setImageURI(list.getAvatarUrl());
                        avatar.setImageURI(list.getAvatarUrl());
                    }

                }
                SPUtils.putString(MainActivity.this,"openId",list.getOpenid());
                if (list.getWxname() != null) {
                    if (!list.getWxname().equals("")) {
                        if (list.getWxname().length()>10){
                            userName.setText(list.getWxname().substring(0,10)+"");
                        }else {
                            userName.setText(list.getWxname());
                        }

                    }

                }
                if (list.getTel() != null) {
                    if (!list.getTel().equals("")) {
                        tel.setText(list.getTel());
                    }

                }

                if (list.getAccountMy()!= 0) {
                  //  if (!list.getWallet().equals("")) {
                        wallet.setText(Double.parseDouble(list.getAccountMy()+"")+"");
                 //   }else {
                    //    wallet.setText("0");
                //    }

                }else {
                    wallet.setText("0");
                }

                if (list.getDownloadAppUrl()!=null){
                    if (!list.getDownloadAppUrl().equals("")){
                        SPUtils.putString(MainActivity.this,"AppQrCode",list.getDownloadAppUrl());
                   }
                }
                if (list.getInvateCode()!=null){
                    if (!list.getInvateCode().equals("")){
                        SPUtils.putString(MainActivity.this,"invateCode",list.getInvateCode());
                    }
                }

            }
            if (list.getEmail()!=null){
                if (!list.getEmail().equals("")){
                    SPUtils.putString(MainActivity.this,"email",list.getEmail());
                }
            }
        } catch (Exception e) {
            showDialog(e.getMessage() + "", false);
        }
    }

   @Override
    public void cardList(List<CardList.DataBean> data) {

    }




    @Override
    public void showDialog(String context, boolean type) {
        shows(context, type);
    }

    @Override
    public void upMap(List<ShopBean.DataBean> data) {
        mapData.clear();
try {
    if (data!=null) {
        mapData.addAll(data);
        if (data.size() != 0) {
            for (int i = 0; i < data.size(); i++) {
                String latitude1 = data.get(i).getLat();
                String longitude1 = data.get(i).getLng();
                LatLng sydney = new LatLng(Double.parseDouble(latitude1), Double.parseDouble(longitude1));
                if (data.get(i).getPhangye().equals("1")){
                    if (data.get(i).getOnlines()>0){
                        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.one)).title(i + ""));
                    }else {
                        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.unone)).title(i + ""));
                    }

                }else if (data.get(i).getPhangye().equals("2")){
                    if (data.get(i).getOnlines()>0){
                        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.two)).title(i + ""));
                    }else {
                        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.untwo)).title(i + ""));
                    }
                }else if (data.get(i).getPhangye().equals("3")){
                    if (data.get(i).getOnlines()>0){
                        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.three)).title(i + ""));
                    }else {
                        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.unthree)).title(i + ""));
                    }
                }else if (data.get(i).getPhangye().equals("4")){
                    if (data.get(i).getOnlines()>0){
                        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.four)).title(i + ""));
                    }else {
                        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.unfour)).title(i + ""));
                    }
                }else if (data.get(i).getPhangye().equals("5")){
                    if (data.get(i).getOnlines()>0){
                        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.fives)).title(i + ""));
                    }else {
                        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.unfives)).title(i + ""));
                    }
                }else if (data.get(i).getPhangye().equals("6")){
                    if (data.get(i).getOnlines()>0){
                        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.six)).title(i + ""));
                    }else {
                        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.unsix)).title(i + ""));
                    }
                }else if (data.get(i).getPhangye().equals("7")){
                    if (data.get(i).getOnlines()>0){
                        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.seven)).title(i + ""));
                    }else {
                        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.unseven)).title(i + ""));
                    }
                }else if (data.get(i).getPhangye().equals("8")){
                    if (data.get(i).getOnlines()>0){
                        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.eight)).title(i + ""));
                    }else {
                        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.uneight)).title(i + ""));
                    }
                }else if (data.get(i).getPhangye().equals("9")){
                    if (data.get(i).getOnlines()>0){
                        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.nine)).title(i + ""));
                    }else {
                        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.unnine)).title(i + ""));
                    }
                }else if (data.get(i).getPhangye().equals("10")){
                    if (data.get(i).getOnlines()>0){
                        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ten)).title(i + ""));
                    }else {
                        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.unten)).title(i + ""));
                    }
                } else {
                    if (data.get(i).getOnlines()>0){
                        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.zero)).title(i + ""));
                    }else {
                        mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.unzero)).title(i + ""));
                    }
                }

                Log.e("data的长度遍历", i + "");
            }
        }
    }
}catch (Exception e){
    showDialog(e.getMessage()+"",false);
}

    }




    public void shows(String content, boolean type) {
        if (!isLiving(MainActivity.this)) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(MainActivity.this);
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

    @Override
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(MainActivity.this);
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

    @SuppressLint("MissingPermission")
    @Override
    public void permissionSuccess() {
        if (mMap != null) {

            mMap.setMyLocationEnabled(false);
            mMap.setIndoorEnabled(true);
            showCurrentPlace();


        }
    }

    @Override
    public void cameraPermissionSuccess() {

            Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE);

    }

    @Override
    public void scan(RulesBean bean) {
        if (bean.getZucount()>0){
         showDialog(getResources().getString(R.string.scanTip),false);
         return;
        }
                 Intent intent = new Intent(MainActivity.this,ChargingRulesActivity.class);
                 intent.putExtra("deviceCode",deviceCode);
                 intent.putExtra("bean",bean);
                 startActivity(intent);


    }

    @Override
    public void onErr() {
        if (!agelogin){
            handler.sendEmptyMessageDelayed(2, 3000);
            agelogin=true;
        }

    }

    private void showCurrentPlace() {
        mGeoDataClient = Places.getGeoDataClient(this, null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        @SuppressWarnings("MissingPermission") final Task<PlaceLikelihoodBufferResponse> placeResult =
                mPlaceDetectionClient.getCurrentPlace(null);

        placeResult.addOnCompleteListener
                (task -> location());

    }

    @SuppressLint("MissingPermission")
    private void location() {
        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(MainActivity.this);
        lastLocation = fusedLocationClient.getLastLocation();
        if (lastLocation != null) {


            mMap.setOnCameraIdleListener(MainActivity.this);
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
            mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            fusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
            lastLocation.addOnCompleteListener(MainActivity.this, this);
        }

    }

    @OnClick({R.id.avatar, R.id.nearby, R.id.search, R.id.posit, R.id.scan, R.id.my, R.id.menu, R.id.sdview, R.id.purse,R.id.loginOut})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.avatar:
                if (Utils.isNotFastClick()) {
                    sideSlip();
                }
                break;
            case R.id.nearby:
                if (Utils.isNotFastClick()) {
                    nextNearbyOutlets();
                }
                break;
            case R.id.search:
                if (Utils.isNotFastClick()) {
                    nextNearbyOutlets();
                }
                break;
            case R.id.posit:

                 location();
                break;
            case R.id.scan:
                presenter.cameraRequestPermission(MainActivity.this, selectedLanguage);
              //  presenter.getRules(getToken(MainActivity.this), "10012212");
                break;
            case R.id.my:
                break;
            case R.id.menu:
             //   showService();
                if (Utils.isNotFastClick()) {
                    startActivity(new Intent(MainActivity.this, FeedBackActivity.class));
                }
                break;
            case R.id.sdview:
                //   startActivity(MainActivity.this,PersonalInformationActivity.class);
                presenter.jump(MainActivity.this, PersonalInformationActivity.class);
                break;
            case R.id.purse:
                if (Utils.isNotFastClick()) {
                   startActivity(new Intent(MainActivity.this, MyPurseActivity.class));
                }
                break;
            case R.id.loginOut:
                LoginOuts();
                break;
        }
    }

    private void LoginOuts() {
        //实例化自定义对话框
        final ConstomDialog mdialog = new ConstomDialog(this, getResources().getString(R.string.loginOut), getResources().getString(R.string.determines));
        //对话框中退出按钮事件
        mdialog.setOnExitListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果对话框处于显示状态
                if (mdialog != null && mdialog.isShowing()) {
                    //关闭对话框
                    mdialog.dismiss();
                }

            }
        });
        //对话框中取消按钮事件
        mdialog.setOnCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mdialog.isShowing()) {
                    mdialog.dismiss();
                    SPUtils.putString(MainActivity.this, "token", "");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));


                    EventMeager eventMeager = new EventMeager();
                    eventMeager.setFinishs("finish");
                    EventBus.getDefault().post(eventMeager);


                    //  finish();

                }

            }
        });
        mdialog.show();
    }

    private void showService() {
        String phone = SPUtils.getString(MainActivity.this, "phone", "");
        String time = SPUtils.getString(MainActivity.this, "time", "");
        Log.e("showService", "showService");
        DialogUtils dialogUtils = new DialogUtils();
        dialogUtils.showCustomerService(MainActivity.this,
                getResources().getString(R.string.phone) + phone,
                getResources().getString(R.string.workingHours) + time);

    }

    private void nextNearbyOutlets() {
        presenter.jump(MainActivity.this, NearbyOutletsListActivity.class);
    }

    private void sideSlip() {
        presenter.info("0", "0", getToken(MainActivity.this));
        if (!mDrawer.isDrawerOpen(GravityCompat.START)) {
   //         presenter.info("0", "0", getToken(MainActivity.this));
            mDrawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        drawerLayout(drawerView, slideOffset);

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    private void drawerLayout(View drawerView, float slideOffset) {
        //设置主布局随菜单滑动而滑动
        int drawerViewWidth = drawerView.getWidth();
        mLlContentMain.setTranslationX(drawerViewWidth * slideOffset);

        //设置控件最先出现的位置
        double padingLeft = drawerViewWidth * (1 - 0.618) * (1 - slideOffset);
        mRlMenu.setPadding((int) padingLeft, 0, 0, 0);
     //   presenter.info("0", "0", getToken(MainActivity.this));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        presenter.requestPermission(MainActivity.this, selectedLanguage);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String title = marker.getTitle();
        marker.hideInfoWindow();


        if (!title.equals("-1")) {
            int index = Integer.parseInt(title);

            showNoneEffect(title);
            uiUpData(index);
        }
        return true;
    }

    private void uiUpData(int index) {
Log.e("我要的数据",mapData.size()+"");

        if (mapData.size()>index){
          mMap.clear();

        if (mapData.size() != 0) {
            for (int i = 0; i < mapData.size(); i++) {
                String latitude1 = mapData.get(i).getLat();
                String longitude1 = mapData.get(i).getLng();
                LatLng sydney = new LatLng(Double.parseDouble(latitude1), Double.parseDouble(longitude1));
                if (mapData.get(i).getPhangye().equals("1")) {
                    if (i!=index){
                        if (mapData.get(i).getOnlines() > 0) {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.one)).title(i + ""));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.unone)).title(i + ""));
                        }
                    }else {
                        if (mapData.get(i).getOnlines() > 0) {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.bone)).title(i + ""));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.bunone)).title(i + ""));
                        }
                    }


                } else if (mapData.get(i).getPhangye().equals("2")) {
                    if (i!=index){
                        if (mapData.get(i).getOnlines() > 0) {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.two)).title(i + ""));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.untwo)).title(i + ""));
                        }
                    }else {
                        if (mapData.get(i).getOnlines() > 0) {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.btwo)).title(i + ""));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.buntwo)).title(i + ""));
                        }
                    }

                } else if (mapData.get(i).getPhangye().equals("3")) {

                    if (i!=index){
                        if (mapData.get(i).getOnlines() > 0) {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.three)).title(i + ""));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.unthree)).title(i + ""));
                        }
                    }else {
                        if (mapData.get(i).getOnlines() > 0) {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.bthree)).title(i + ""));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.bunthree)).title(i + ""));
                        }
                    }

                } else if (mapData.get(i).getPhangye().equals("4")) {
                    if (i!=index){
                        if (mapData.get(i).getOnlines() > 0) {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.four)).title(i + ""));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.unfour)).title(i + ""));
                        }
                    }else {
                        if (mapData.get(i).getOnlines() > 0) {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.bfour)).title(i + ""));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.baunfour)).title(i + ""));
                        }
                    }

                } else if (mapData.get(i).getPhangye().equals("5")) {
                    if (i!=index){
                        if (mapData.get(i).getOnlines() > 0) {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.fives)).title(i + ""));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.unfives)).title(i + ""));
                        }
                    }else {
                        if (mapData.get(i).getOnlines() > 0) {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.bfives)).title(i + ""));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.bunfives)).title(i + ""));
                        }
                    }

                } else if (mapData.get(i).getPhangye().equals("6")) {
                    if (i!=index){
                        if (mapData.get(i).getOnlines() > 0) {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.six)).title(i + ""));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.unsix)).title(i + ""));
                        }
                    }else {
                        if (mapData.get(i).getOnlines() > 0) {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.bsix)).title(i + ""));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.bunsix)).title(i + ""));
                        }
                    }

                } else if (mapData.get(i).getPhangye().equals("7")) {
                    if (i!=index){
                        if (mapData.get(i).getOnlines() > 0) {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.seven)).title(i + ""));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.unseven)).title(i + ""));
                        }
                    }else {
                        if (mapData.get(i).getOnlines() > 0) {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.bseven)).title(i + ""));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.bunseven)).title(i + ""));
                        }
                    }

                } else if (mapData.get(i).getPhangye().equals("8")) {
                    if (i!=index){
                        if (mapData.get(i).getOnlines() > 0) {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.eight)).title(i + ""));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.uneight)).title(i + ""));
                        }
                    }else {
                        if (mapData.get(i).getOnlines() > 0) {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.beight)).title(i + ""));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.buneight)).title(i + ""));
                        }
                    }

                } else if (mapData.get(i).getPhangye().equals("9")) {
                    if (i!=index){
                        if (mapData.get(i).getOnlines() > 0) {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.nine)).title(i + ""));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.unnine)).title(i + ""));
                        }
                    }else {
                        if (mapData.get(i).getOnlines() > 0) {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.bnine)).title(i + ""));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.bunnine)).title(i + ""));
                        }
                    }

                } else if (mapData.get(i).getPhangye().equals("10")) {
                    if (i!=index){
                        if (mapData.get(i).getOnlines() > 0) {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ten)).title(i + ""));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.unten)).title(i + ""));
                        }
                    }else {
                        if (mapData.get(i).getOnlines() > 0) {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.bten)).title(i + ""));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.bunten)).title(i + ""));
                        }
                    }

                } else {
                    if (i!=index){
                        if (mapData.get(i).getOnlines() > 0) {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.zero)).title(i + ""));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.unzero)).title(i + ""));
                        }
                    }else {
                        if (mapData.get(i).getOnlines() > 0) {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.bzero)).title(i + ""));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.bunzero)).title(i + ""));
                        }
                    }

                }
            }
        }
    }
    }
  /*  private  void  showCardList(){

    }*/


    private void showNoneEffect(String title) {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vPopupWindow = inflater.inflate(R.layout.map_popupwindow, null, false);//引入弹窗布局
        //  LinearLayout  details  =(LinearLayout) vPopupWindow.findViewById(R.id.details);

        ImageView shopLogo = vPopupWindow.findViewById(R.id.shopLogo);

        TextView shopName = vPopupWindow.findViewById(R.id.shopName);
        TextView address = vPopupWindow.findViewById(R.id.address);
        TextView time = vPopupWindow.findViewById(R.id.time);
        TextView available  = vPopupWindow.findViewById(R.id.available);
        TextView returns = vPopupWindow.findViewById(R.id.returns);
        TextView distance = vPopupWindow.findViewById(R.id.distance);
        navigation  = vPopupWindow.findViewById(R.id.navigation);
        int i = Integer.parseInt(title);
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapData.size()>i){
                    navigations(getLatitude(MainActivity.this),getLongitude(MainActivity.this)
                            ,mapData.get(i).getLat(),mapData.get(i).getLng());
                 //   Log.e("")
                }

            }
        });




        if (i<mapData.size()){
            if (mapData.get(i).getOnlines()>0){
                navigation.setImageResource(R.mipmap.mapnavigation);
            }else {
                navigation.setImageResource(R.mipmap.nonavigation);
            }
            Glide.with(MainActivity.this).load(mapData.get(i).getLogo()).error(R.mipmap.shoplogo)
            .placeholder(R.mipmap.shoplogo).into(shopLogo);
            shopName.setText(mapData.get(i).getShopname());
            address.setText(mapData.get(i).getAddress());
          time.setText(mapData.get(i).getServiceTime()+"");
            available.setText(getResources().getString(R.string.Available)+": "+mapData.get(i).getBatteryCount());
            returns.setText(getResources().getString(R.string.Rrturn)+": "+mapData.get(i).getReturnCount());
            if ((int)mapData.get(i).getDistance()>=1000){
                int  dis =     (int)mapData.get(i).getDistance()/1000;
                distance.setText(getResources().getString(R.string.distances)+": "+dis+ "km");
            }else {
                distance.setText(getResources().getString(R.string.distances)+":"+(int)mapData.get(i).getDistance() + "m");
            }

        }

        popupWindow = new PopupWindow(vPopupWindow, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);

        //设置背景透明
        addBackground();
        //设置进出动画
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        //引入依附的布局  activity_main   activity_login
        View parentView = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_login, null);

        //相对于父控件的位置（例如正中央Gravity.CENTER，下方Gravity.BOTTOM等），可以设置偏移或无偏移
        popupWindow.showAtLocation(parentView, Gravity.BOTTOM, 20, 20);

    }

    private void navigations(String latitude,String longitude,String Latitude,String Longitude) {
        if (isAvilible(this, "com.google.android.apps.maps")) {

            Uri parse = Uri.parse("http://maps.google.com/maps?f=d&source=s_d" +
                    "&saddr=" + latitude + "," + longitude + "&daddr=" + Latitude+ "," + Longitude + "&hl=zh&t=m&dirflg=d");
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    parse);
            intent.setPackage("com.google.android.apps.maps");
            this.startActivity(intent);


        } else {
            shows(getResources().getString(R.string.noGoogleMap), false);


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

    private void addBackground() {
        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;//调节透明度
        getWindow().setAttributes(lp);
        //dismiss时恢复原样
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

    }

    @Override
    public void onCameraIdle() {
        mapData.clear();
        if (mMap != null) {
            target = mMap.getCameraPosition().target;
            mMap.clear();
        }
        if (target != null){
          //  getAdd(target.latitude,target.longitude);
          SPUtils.putString(MainActivity.this, "latitudeCenter", target.latitude+"");
         SPUtils.putString(MainActivity.this, "longitudeCenter", target.longitude+"");
if (!locator){

    SPUtils.putString(MainActivity.this, "Latitude", target.latitude + "");  //
    SPUtils.putString(MainActivity.this, "Longitude", target.longitude + "");//
    locator=true;
}
            presenter.initMap(target.latitude + "",target.longitude  + "",
                    getLatitude(MainActivity.this), getLongitude(MainActivity.this),getToken(MainActivity.this));
        }
    }

    @Override
    public void onComplete(@NonNull Task<Location> task) {
        // ...
        if (task.isSuccessful()) {
            if (task != null) {

                Location result = task.getResult();
                if (result != null) {
                  //  Log.e("result", result.getLatitude() + ",," + result.getLongitude());
                    sydney = new LatLng(result.getLatitude(), result.getLongitude());
                  //  sydney= new LatLng(22.60768,113.85526);
                    if (mMap != null) {
                        mMap.clear();
                        mMap.setMaxZoomPreference(25f);
                        mMap.setMinZoomPreference(6f);
                        //   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY, 15));
                        mMap.resetMinMaxZoomPreference();

                        //  marker = mMap.addMarker(new MarkerOptions().position(sydney).icon(BitmapDescriptorFactory.fromResource(R.mipmap.shape)).title("-1"));
                        shop.setVisibility(View.VISIBLE);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
                        mMap.setOnCameraIdleListener(MainActivity.this);


                    }

                } else {
                   showDialog(getResources().getString(R.string.positioningFailed), false);
                }


            } else {
                showDialog(getResources().getString(R.string.positioningFailed), false);
            }
        } else {
            showDialog(getResources().getString(R.string.positioningFailed), false);
        }
    }


    private void nextGV(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(MainActivity.this, RentalRecordActivity.class));
                break;
            case 1:
                startActivity(new Intent(MainActivity.this, CooperationActivity.class));
                break;
            case 2:
                startActivity(new Intent(MainActivity.this, InstructionsUseActivity.class));
                break;
            case 3:
              startActivity(new Intent(MainActivity.this,CommonProblemActivity.class));
                break;
        }
    }

    @Override
    public void onPositiveButtonClick(int type, String context, AlertDialog dialog) {
        dialogs = dialog;
        dialogs.dismiss();
        startActivity(new Intent(MainActivity.this,RechargeActivity.class));
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
        if (result!=null){
            String[] split = result.split("/");
            int code=split.length-1;
            for (int i = 0; i <split.length ; i++) {
                Log.e("deviceCode",split[i]);
            }
            deviceCode = split[code];
            presenter.getRules(getToken(MainActivity.this), deviceCode);


        }else {
           showDialog(getResources().getString(R.string.acquisitionFailed),false);
        }
    }

    private void getAdd(double lat,double  lon){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat,lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses!=null||addresses.size()>0) {
            String locality=addresses.get(0).getLocality();
            Log.e("locality",locality);
        }

    }
    public void propertyValuesHolderDown(final View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0.9f, 0.9f, 0.91f, 0.92f, 0.93f, 0.94f, 0.95f, 0.96f, 0.97f, 0.98f, 0.99f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.9f, 0.9f, 0.91f, 0.92f, 0.93f, 0.94f, 0.95f, 0.96f, 0.97f, 0.98f, 0.99f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 0.94f, 0.95f, 0.96f, 0.97f, 0.98f, 0.99f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(800).start();

    }


    public static ObjectAnimator tada(View view) {

        return tada(view, 1f);  }

    public static ObjectAnimator tada(View view, float shakeFactor) {



        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,

                Keyframe.ofFloat(0f, 0f),

                Keyframe.ofFloat(.1f, 0f),

                Keyframe.ofFloat(.2f, 0f),

                Keyframe.ofFloat(.3f, 0f),

                Keyframe.ofFloat(.4f, 0f),

                Keyframe.ofFloat(.5f, 0f),

                Keyframe.ofFloat(.6f, 0f),

                Keyframe.ofFloat(.7f, 0f),

                Keyframe.ofFloat(.8f, 0f),

                Keyframe.ofFloat(.9f, 0f),

                Keyframe.ofFloat(1f, 1f)

        );



        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,

                Keyframe.ofFloat(0f, 1f),

                Keyframe.ofFloat(.1f, .9f),

                Keyframe.ofFloat(.2f, .9f),

                Keyframe.ofFloat(.3f, 1.1f),

                Keyframe.ofFloat(.4f, 1.1f),

                Keyframe.ofFloat(.5f, 1.1f),

                Keyframe.ofFloat(.6f, 1.1f),

                Keyframe.ofFloat(.7f, 1.1f),

                Keyframe.ofFloat(.8f, 1.1f),

                Keyframe.ofFloat(.9f, 1.1f),

                Keyframe.ofFloat(1f, 1f)

        );



        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(View.ROTATION,

                Keyframe.ofFloat(0f, 0f),

                Keyframe.ofFloat(.1f, -3f * shakeFactor),

                Keyframe.ofFloat(.2f, -3f * shakeFactor),

                Keyframe.ofFloat(.3f, 3f * shakeFactor),

                Keyframe.ofFloat(.4f, -3f * shakeFactor),

                Keyframe.ofFloat(.5f, 3f * shakeFactor),

                Keyframe.ofFloat(.6f, -3f * shakeFactor),

                Keyframe.ofFloat(.7f, 3f * shakeFactor),

                Keyframe.ofFloat(.8f, -3f * shakeFactor),

                Keyframe.ofFloat(.9f, 3f * shakeFactor),

                Keyframe.ofFloat(1f, 0)

        );



        return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY, pvhRotate).

                setDuration(1000);  }
}
