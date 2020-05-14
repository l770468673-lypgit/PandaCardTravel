package com.xlu.uis;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;



import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pandacard.teavel.R;
import com.pandacard.teavel.apps.MyApplication;
import com.xlu.adapters.JieShuoPageAdapter;
import com.xlu.adapters.YuyinAdapter;
import com.xlu.bases.Base2Activity;
import com.xlu.bases.BaseActivity;
import com.xlu.bases.model.MapLayer;
import com.xlu.bases.model.MapObject;
import com.xlu.po.Jieshuo;
import com.xlu.po.Member;
import com.xlu.po.Merchant1;
import com.xlu.po.MyEvent;
import com.xlu.po.XiangQu;
import com.xlu.po.Zone;
import com.xlu.sys.SystemManger;
import com.xlu.uis.maps.MapWidget;
import com.xlu.uis.maps.interfaces.MapEventsListener;
import com.xlu.uis.maps.interfaces.OnMapScrollListener;
import com.xlu.uis.maps.interfaces.OnMapTouchListener;
import com.xlu.uis.maps.interfaces.event.MapScrolledEvent;
import com.xlu.uis.maps.interfaces.event.MapTouchedEvent;
import com.xlu.uis.maps.interfaces.event.ObjectTouchEvent;
import com.xlu.utils.AppUtil;
import com.xlu.utils.Constance;
import com.xlu.utils.DBUtil;
import com.xlu.utils.DensityUtil;
import com.xlu.utils.FileUtil;
import com.xlu.utils.JieshuoPopup;
import com.xlu.utils.JsonUtil;
import com.xlu.utils.NetWorkHelper;
import com.xlu.utils.PivotFactory;
import com.xlu.utils.servers.DownloadService;
import com.xlu.utils.servers.PlayService;
import com.xlu.widgets.CustomDialog;
import com.xlu.widgets.CustumDialog;
import com.xlu.widgets.MySpinnerDialog;
import com.xlu.widgets.MyTabView;
import com.xlu.widgets.PopWindowJieShuoList;
import com.xlu.widgets.SnappingSwipingViewBuilder;
import com.xlu.widgets.SnappyLinearLayoutManager;
import com.xlu.widgets.SnappyRecyclerView;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.com.wideroad.BaseHttp;
import cn.com.wideroad.http.AjaxCallBack;
import cn.com.wideroad.http.AjaxParams;
import cn.com.wideroad.utils.StringUtil;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class ActivityZone2 extends Base2Activity implements MapEventsListener,
        OnMapTouchListener, SensorEventListener, Base2Activity.PermissionCall,
        PopWindowJieShuoList.OnMyItemOnClickListener, OnClickListener, YuyinAdapter.OnClickButtonListener {

    private MapWidget mapWidget;
    private DownloadService.DownloadBinder downloadBinder;
    private boolean isAutoNavi = true;

    //标题栏
    ImageView ivBack;
    MyTabView myOne;
    MyTabView myTwo;
    private Jieshuo netJieshuo = null;

    //门票展示

    LinearLayout ltTopTag;
    TextView tvZoneName;
    TextView tvMore;
    ImageView ivZoneTicketPic;
    TextView tvTicketName;
    TextView tvOldPrice;
    TextView tvNewPrice;
    Button btnOrder;
    ImageView ivClose;

    //下载工具栏
    LinearLayout lyDownload;
    TextView tvDownload;
    TextView tvZoneList;
    TextView tvSetting;

    //左边工具栏
    LinearLayout ltLeftMenu;
    TextView tvPublic;
    TextView tvHide;
    TextView tvShare;
    TextView tvRoute;


    RelativeLayout rlZoneMap;

    private MySpinnerDialog pbarDialog;

    private MapLayer layer;
    private List<Jieshuo> jieShuoList = new ArrayList<Jieshuo>();
    private int jieshuoIndex;
    private Zone zone;
    protected PlayService.PlayBinder playBinder;
    private Jieshuo jieshuo;
    private Jieshuo jieshuo1;
    private Jieshuo jieshuo3;
    private Jieshuo jieshuoLine;
    private Bundle savedInstanceState;
    private boolean isDownload;
    private JieshuoPopup jieshuoPopup;
    private boolean isResume = false;
    private AudioManager audioManager;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private Handler myHandler;
    CustumDialog dailog;
    private TelephonyManager tpm;
    private MyPhoneStateListener phoneListener;
    private YuyinAdapter mAdapter;
    private SnappyRecyclerView slv;
    private RelativeLayout rlParent;
    private Jieshuo showJieshuo;

    private void findView() {
        //标题栏
        ivBack = findViewById(R.id.iv_zone_back);
        myOne = findViewById(R.id.mtv_one);
        myTwo = findViewById(R.id.mtv_two);
        //门票展示
        ltTopTag = findViewById(R.id.lt_tag);
        tvZoneName = findViewById(R.id.tv_zone_name);
        tvMore = findViewById(R.id.tv_more);
        ivZoneTicketPic = findViewById(R.id.iv_zone_ticket_pic);
        tvTicketName = findViewById(R.id.tv_zone_ticket_name);
        tvOldPrice = findViewById(R.id.tv_price_old);
        tvNewPrice = findViewById(R.id.tv_price);
        btnOrder = findViewById(R.id.btn_order);
        ivClose = findViewById(R.id.iv_close);

        //下载工具栏
        lyDownload = findViewById(R.id.ly_download);
        tvDownload = findViewById(R.id.tv_auto_navi);
        tvZoneList = findViewById(R.id.tv_zone_list);
        tvSetting = findViewById(R.id.tv_setting);

        //左边工具栏

        ltLeftMenu = findViewById(R.id.lt_left_menu);
        tvPublic = findViewById(R.id.tv_public);
        tvHide = findViewById(R.id.tv_hide);
        tvShare = findViewById(R.id.tv_location_share);
        tvRoute = findViewById(R.id.tv_route);


        rlZoneMap = findViewById(R.id.rl_zone_map);

        rlParent = findViewById(R.id.rl_map);


        ivBack.setOnClickListener(this);
        myOne.setOnClickListener(this);
        myTwo.setOnClickListener(this);
        tvMore.setOnClickListener(this);
        btnOrder.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        tvZoneList.setOnClickListener(this);
        tvSetting.setOnClickListener(this);
        tvHide.setOnClickListener(this);
        tvPublic.setOnClickListener(this);
        tvDownload.setOnClickListener(this);
        tvRoute.setOnClickListener(this);
        tvShare.setOnClickListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        initPhoneListener();

        setmPermissionCall(this);

        Type type = new TypeToken<Zone>() {
        }.getType();
        zone = (Zone) JsonUtil.fromJsonToObject(
                getIntent().getStringExtra("zonestr"), type);
        if (zone.getColor() == null || zone.getColor().equals(""))
            rlZoneMap.setBackgroundColor(Color.parseColor("#9db557"));
        else {
            if (zone.getColor().startsWith("#"))
                rlZoneMap.setBackgroundColor(Color.parseColor(zone.getColor()));
            else
                rlZoneMap.setBackgroundColor(Color.parseColor("#"
                        + zone.getColor()));
        }
        tvZoneName.setText(zone.getName());
        myOne.setText("景区攻略");
        myTwo.setText("景区门票");
        loadTicket();
        this.savedInstanceState = savedInstanceState;
        Intent intent = new Intent("cn.com.wideroad.xiaolu.service.download");
        intent.setPackage(getPackageName());
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        intent = new Intent("cn.com.wideroad.xiaolu.service.play");
        intent.setPackage(getPackageName());
        bindService(intent, conn1, BIND_AUTO_CREATE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constance.FIND_BLUE_TOOTH);
        filter.addAction(Constance.JIESHUO_PLAY_END);
        filter.addAction(Constance.DOWNLOAD_MAP_DONE);
        filter.addAction(Constance.DOWNLOAD_MAP_FAIL);
        filter.addAction(Constance.TISHI_END);
        filter.addAction(Constance.LINE_TISHI_END);
        filter.addAction(Constance.GPSLOCATION);
        registerReceiver(receiver, filter);
        isDownload = getIntent().getBooleanExtra("isDownload", false);
        pbarDialog = new MySpinnerDialog(this);
        addXQ();
        initRecycleView();
    }

    private void initRecycleView() {
        mAdapter = new YuyinAdapter(jieShuoList,this);
        slv = new SnappingSwipingViewBuilder(this)
                .setHeadTailExtraMarginDp(0F)
                .setOrientation(LinearLayoutManager.HORIZONTAL)
                .setItemAnimator(new DefaultItemAnimator())
                .setAdapter(mAdapter)
                .setItemMarginDp(0f, 0F, 0f, 0F)
                .setSnapMethod(SnappyLinearLayoutManager.SnappyLinearSmoothScroller.SNAP_CENTER)
                .build();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtil.dip2px(this, 450), ViewGroup.LayoutParams.WRAP_CONTENT);
        slv.setLayoutParams(params);
        rlParent.addView(slv);

        mAdapter.setListener(this);
        slv.setOnItemSelectLitener(new SnappyRecyclerView.OnItemSelectLitener() {
            @Override
            public void onItemSelect(int position) {
                Jieshuo j = jieShuoList.get(position);
                mapWidget.scrollMapTo(j.getZuobiao_x(), j.getZuobiao_y());
                for (Jieshuo j2 : jieShuoList) {
                    if (j2.equals(j)) {
                        j2.setSelected(true);
                    } else {
                        j2.setSelected(false);
                    }
                }

                updataMap();
                mAdapter.notifyDataSetChanged();


            }
        });


    }

    List<Merchant1> tickets;
    boolean haveTicket = false;

    private void loadTicket() {
        BaseHttp http = new BaseHttp();
        AjaxParams params = new AjaxParams();
        if (Double.valueOf(zone.getLatitude()) > 0
                && Double.valueOf(zone.getLongitude()) > 0) {
            params.put("jindu", zone.getLongitude());
            params.put("weidu", zone.getLatitude());

        } else {
            params.put("jindu", MyApplication.jingdu + "");
            params.put("weidu", MyApplication.weidu + "");
        }

        http.post(Constance.HTTP_REQUEST_URL_BIZ + "getRelativeProsByGps",
                params, new AjaxCallBack<Object>() {

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
                        super.onFailure(t, errorNo, strMsg);
                    }

                    @Override
                    public void onSuccess(Object t) {
                        // TODO Auto-generated method stub
                        super.onSuccess(t);
                        try {
                            Type types = new TypeToken<List<Merchant1>>() {
                            }.getType();
                            tickets = (List<Merchant1>) JsonUtil.fromJsonToObject(StringUtil.removeNull(t), types);
                            if (tickets.size() == 0) {
                                haveTicket = false;
                            } else {
                                haveTicket = true;
                                tvTicketName.setText(tickets.get(0).getName());
                                tvZoneName.setText(zone.getName());
                                tvOldPrice.setText("原价: ¥" + tickets.get(0).getPrice_market());
                                tvNewPrice.setText("¥" + tickets.get(0).getPrice());
                                ImageLoader.getInstance().displayImage(Constance.HTTP_URL + tickets.get(0).getPic(), ivZoneTicketPic, MyApplication.normalOption);

                            }

                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }

                });

    }

    // --------------------------监听来电----------------------------------
    private void initPhoneListener() {
        // 获取电话管理类
        tpm = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        phoneListener = new MyPhoneStateListener();
        // 设置监听
        tpm.listen(phoneListener,
                PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void requestSucess() {

    }

    @Override
    public void refused() {

    }

    @Override
    public void onItemOnClick(Jieshuo jieshuo) {
        for (int i = 0; i < jieShuoList.size(); i++) {
            if (jieShuoList.get(i).getName().equals(jieshuo.getName())) {
//                showJieshuo.setSelected(false);
                slv.setVisibility(View.VISIBLE);
                jieShuoList.get(i).setSelected(true);
                showJieshuo = jieShuoList.get(i);
                popJieShuo.dismiss();
                slv.scrollToPosition(i);
                slv.smoothScrollToPosition(i);
                slv.setFlag(i);
            }
        }
    }

    @Override
    public void playOnClickListener(final Jieshuo jieshuo, final View ivPlay, final View ivStop) {

        for (Jieshuo j : jieShuoList) {
            if (j.getName().equals(jieshuo.getName())) {
                j.setListen(true);
            } else {
                j.setListen(false);
            }


        }
        updataMap();
        playingJieshuo = jieshuo;
        if (!DBUtil.freeIsEnd(zone.getId(), zone.getName())
                || DBUtil.zoneIsPay(zone.getId())
                || zone.getPrice() == 0 || (DBUtil.getLoginMeber() != null && DBUtil.getLoginMeber().getIsvip() == 1)) {

            if (DBUtil.zoneHaveDownload(zone.getId())) {

                playBinder.pause(1);

                Jieshuo jieshuo2 = jieshuo;

                SystemManger.yuying = jieshuo2.getYuyin();
                playBinder.pause(1);
                // jieshuoPopup.setPlay(true);
                playBinder.playTishi("msg.mp3", 1);
                DBUtil.consumeFree(zone.getId());
                jieshuo2.setListen(true);
                jieshuo3 = jieshuo2;
                jieshuo1 = null;


            } else {
                if (!isClicked) {
                    final Jieshuo jieshuo2 = jieshuo;
                    //                    jieshuo = jieshuo2;
                    jieshuo3 = jieshuo2;
                    jieshuo1 = null;
                    // 通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                    DBUtil.consumeFree(zone.getId());
                    AlertDialog.Builder dialog = new AlertDialog.Builder(
                            ActivityZone2.this);
                    dialog.setTitle(R.string.map_title)
                            .setMessage(R.string.map_msg)
                            .setPositiveButton(
                                    R.string.map_load,
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            isAutoNavi = false;
                                            downloadOrNav();
                                            dialog.dismiss();
                                            ivPlay.setVisibility(View.VISIBLE);
                                            ivStop.setVisibility(View.GONE);
                                            jieshuo2.setListen(false);
                                            jieshuo2.setSelected(true);
                                            updataMap();


                                        }
                                    })
                            .setNegativeButton(
                                    R.string.map_play,
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            isAutoNavi = false;
                                            playBinder.pause(1);
                                            if (NetWorkHelper
                                                    .isNetWorkAvailble(ActivityZone2.this)) {
                                                playBinder.playTishi(
                                                        "msg.mp3", 0);
                                                playingJieshuo = jieshuo;
                                                jieshuo3 = jieshuo;
                                                netJieshuo = jieshuo;
                                                //                                                playMarker=markerList.get(jieShuoList.indexOf(jieshuo));
                                                //                                                playingMarker=markerList.get(jieShuoList.indexOf(jieshuo));


                                            } else {
//                                                MyApplication.show("无网络无法使用");
                                                jieshuo.setSelected(true);
                                                jieshuo.setListen(false);
                                                updataMap();
                                            }


                                            dialog.dismiss();

                                        }
                                    }).create().show();
                } else {
                    isAutoNavi = false;
                    playBinder.pause(1);
                    jieshuo3 = jieshuo;
                    DBUtil.consumeFree(zone.getId());
                    if (NetWorkHelper
                            .isNetWorkAvailble(ActivityZone2.this)) {
                        playBinder.playTishi(
                                "msg.mp3", 0);
                        playingJieshuo = jieshuo;


                    } else {
//                        MyApplication.show("无网络无法使用");
                        ivPlay.setVisibility(View.VISIBLE);
                        ivStop.setVisibility(View.GONE);
                        jieshuo.setSelected(true);
                        jieshuo.setListen(false);
                        return;
                    }
                }
                isClicked = true;

            }

        } else {
            if (!SystemManger.isInPayZoneActivity) {
                AppUtil.showToastMsg(ActivityZone2.this, "亲，试用结束了,请激活景点");
                goActivityPayZone();
                playBinder.stop(1);
                jieshuo.setListen(false);
                jieshuo.setSelected(true);
                updataMap();
                return;
            }
        }

        playingJieshuo = jieshuo;
        ivPlay.setVisibility(View.GONE);
        ivStop.setVisibility(View.VISIBLE);
        updataMap();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void stopOnClickListener(Jieshuo jieshuo, View v, View v2) {
        playBinder.pause(1);
        for (Jieshuo j : jieShuoList) {
            if (j.getName().equals(jieshuo.getName())) {
                j.setListen(false);
            }
        }
        updataMap();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void hereOnClickListener(Jieshuo jieshuo, View v) {
//        MyApplication.showSingleton("该地图无此功能");
    }

    @Override
    public void tuWenOnClickListener(Jieshuo jieshuo, View v) {
//        Intent intent = new Intent(ActivityZone2.this,
//                ActivityJieshuoDetail.class);
//        Type type = new TypeToken<Jieshuo>() {
//        }.getType();
//        String str = JsonUtil.toJsonString(jieshuo, type);
//        intent.putExtra("jieshuostr", str);
//        ActivityZone2.this.startActivity(intent);
    }

    class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: //
                    EventBus.getDefault().post(
                            new MyEvent(TelephonyManager.CALL_STATE_IDLE));
                    break;
                case TelephonyManager.CALL_STATE_RINGING: //响铃
                    EventBus.getDefault().post(
                            new MyEvent(TelephonyManager.CALL_STATE_RINGING));
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK: // 挂断电话
                    EventBus.getDefault().post(
                            new MyEvent(TelephonyManager.CALL_STATE_OFFHOOK));
                    break;
            }
        }

    }

    int i;
    JieShuoPageAdapter jieshuoadapter;
    boolean isClicked = false;

    private void initYuyinView(final List<Jieshuo> jieshuos) {
        if (jieShuoList.size() != 0)
            jieShuoList.clear();
        jieShuoList.addAll(jieshuos);
        mAdapter.notifyDataSetChanged();

    }

    int checkedPosition = 0;


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (playBinder != null && playBinder.isPlaying()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("确认退出吗,退出后将退出自动导览?");
            builder.setTitle("提示");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityZone2.this.finish();
                }
            });

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } else {
            ActivityZone2.this.finish();
        }

    }

    Jieshuo pauseJieshuo;

    @Subscribe(threadMode = ThreadMode.MainThread)
    @Override
    public void onEventMainThread(MyEvent e) {
        // TODO Auto-generated method stub
        super.onEventMainThread(e);
        if (playBinder != null && e.getId() == TelephonyManager.CALL_STATE_RINGING) {//正在响铃
            if (playBinder.isPlaying()) {
                playBinder.pause(1);
                pauseJieshuo = null;
                for (Jieshuo j : jieShuoList) {
                    if (j.isListen()) {
                        j.setListen(false);
                        pauseJieshuo = j;
                    }
                }
                updataMap();
                mAdapter.notifyDataSetChanged();
                isResume = true;
            }
        } else if (e.getId() == TelephonyManager.CALL_STATE_IDLE) {//无活动
            if (playBinder != null && isResume) {
                isResume = false;
                playBinder.resume();
                for (Jieshuo j : jieShuoList) {
                    if (j.getName().equals(pauseJieshuo.getName())) {
                        j.setListen(true);
                    }
                }
                updataMap();
                //                mAdapter.notifyDataSetChanged();
                slv.setFlag(jieShuoList.indexOf(pauseJieshuo));

                //                jieshuoPopup.setPlay(true);
            }
        } else if (e.getId() == TelephonyManager.CALL_STATE_OFFHOOK) {//正在通话
            //            if (playBinder!=null&&playBinder.isPlaying()) {
            //                playBinder.pause(1);
            //                for(Jieshuo j:jieShuoList){
            //                    if(j.getName().equals(pauseJieshuo.getName())){
            //                        j.setListen(false);
            //                    }
            //                }
            //                updataMap();
            //                mAdapter.notifyDataSetChanged();
            //                isResume = true;
            //            }
        } else if (e.getId() == Constance.MYEVETN_GET_FOCUSS) { //获得播放焦点
            if (playBinder != null && isResume) {
                isResume = false;
                playBinder.resume();
                for (Jieshuo j : jieShuoList) {
                    if (j.getName().equals(pauseJieshuo.getName())) {
                        j.setListen(true);
                    }
                }
                updataMap();
                slv.setFlag(jieShuoList.indexOf(pauseJieshuo));
            }

        } else if (e.getId() == Constance.MYEVETN_LOSS_FOCUSS) {//失去播放焦点
            if (playBinder != null && playBinder.isPlaying()) {
                playBinder.pause(1);
                pauseJieshuo = null;
                for (Jieshuo j : jieShuoList) {
                    if (j.isListen()) {
                        j.setListen(false);
                        pauseJieshuo = j;
                    }
                }
                updataMap();
                mAdapter.notifyDataSetChanged();
                isResume = true;
            }
        }

    }

    private void initviews() {
        // TODO Auto-generated method stub

        if (DBUtil.zoneHaveDownload(zone.getId())) {
            tvDownload.setText("自动导览");
            //            lyDownload.setBackgroundResource(R.drawable.rectangle_auto_nav);
            //			ivAutoNav.setImageResource(R.drawable.iv_auto_nav1);
            tvDownload.setTextColor(0xee111111);
            if (!zone.getAutomatic().equals("1")) {
                tvDownload.setTextColor(0x99999999);
            }
        } else {
            if (downloadBinder.isDownloading(zone)) {
                handler.removeMessages(1);
                handler.sendEmptyMessage(1);
                tvDownload.setText("正在下载" + downloadBinder.getProgress(zone)
                        + "%");
            } else {
                tvDownload.setText("下载语音离线包");
            }
        }
    }

    private void addXQ() {

        XiangQu xq = (XiangQu) JsonUtil.fromJsonToObject(getIntent()
                .getStringExtra("zonestr"), new TypeToken<XiangQu>() {
        }.getType());
        DBUtil.addXiangQu(xq);

        EventBus.getDefault().post(new MyEvent(Constance.MYEVENT_XIANGQU_ADD));

        BaseHttp http = new BaseHttp();
        AjaxParams params = new AjaxParams();
        params.put("sid", MyApplication.deviceId);
        params.put("jid", zone.getId() + "");
        http.post(Constance.HTTP_REQUEST_URL + "xiangqu", params,
                new AjaxCallBack<Object>() {
                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                    }

                    @Override
                    public void onSuccess(Object t) {
                        super.onSuccess(t);
                    }
                });

    }

    private ServiceConnection conn1 = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {

        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            playBinder = (PlayService.PlayBinder) service;
        }
    };

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (DBUtil.zoneHaveDownload(zone.getId())) {
                tvDownload.setText("自动导览");
                //                lyDownload.setBackgroundResource(R.drawable.rectangle_auto_nav);
                //				ivAutoNav.setImageResource(R.drawable.iv_auto_nav1);
                tvDownload.setTextColor(0xee111111);
                if (!zone.getAutomatic().equals("1")) {
                    tvDownload.setTextColor(0x99999999);
                }
                return;
            } else {
                if (downloadBinder.isDownloading(zone)) {
                    tvDownload.setText("正在下载"
                            + downloadBinder.getProgress(zone) + "%");
                } else {
                    tvDownload.setText("下载语音离线包");
                }
            }
            handler.sendEmptyMessageDelayed(1, 1000);
        }

    };

    private ServiceConnection conn = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {

        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder) service;
            initviews();
            String url = zone.getMapsrc_android();
            if (url == null || "".equals(url)) {
                AppUtil.showToastMsg(ActivityZone2.this, "景点地图正在建设中，敬请期待");
                initMap1();
            } else {
                int pos = url.lastIndexOf("/");
                String fileName = url.substring(pos + 1).split("[.]")[0];
                if (FileUtil.checkIsExistInLocal1(
                        FileUtil.getApkStorageFile(ActivityZone2.this), fileName)) {
                    initMap();
                    loadData(isDownload);
                } else {
                    pbarDialog.show();
                    pbarDialog.setTvShowText("加载地图中...");
                    pbarDialog.getWindow().setLayout(DensityUtil.dip2px(ActivityZone2.this, 185), WindowManager.LayoutParams.WRAP_CONTENT);
                    pbarDialog.setCancelable(false);
                    downloadBinder.downloadMap(zone);
                }
            }
        }
    };
    List<Jieshuo> jieshuoTitle;

    private void loadData(boolean isDownload) {
        jieshuoTitle = new ArrayList<>();
        if (isDownload && DBUtil.getJieshuoList(zone.getId()).size() != 0) {
            List<Jieshuo> temp = DBUtil.getJieshuoList(zone.getId());
            jieshuoTitle.addAll(temp);
            if (temp.size() != 0) {
                jieShuoList.addAll(temp);
                updataMap();
                initviews();
                initYuyinView(temp);
                slv.setFlag(0);
            }
        } else {
            BaseHttp http = new BaseHttp();
            AjaxParams params = new AjaxParams();
            params.put("sid", MyApplication.deviceId);
            params.put("jid", zone.getId() + "");
            http.post(Constance.HTTP_REQUEST_URL + "getJieShuoDianByJid",
                    params, new AjaxCallBack<Object>() {

                        @Override
                        public void onFailure(Throwable t, int errorNo,
                                              String strMsg) {
                            // TODO Auto-generated method stub
                            super.onFailure(t, errorNo, strMsg);
                        }

                        @Override
                        public void onSuccess(Object t) {
                            // TODO Auto-generated method stub
                            super.onSuccess(t);
                            try {

                                // Type types = new TypeToken<List<Jieshuo>>() {
                                // }.getType();
                                // jieShuoList = (List<Jieshuo>)
                                // JsonUtil.fromJsonToObject(t.toString(),
                                // types);

                                List<Jieshuo> temp = JsonUtil.getList(t.toString(),
                                        Jieshuo.class);
                                if (temp.size() != 0) {
                                    jieshuoTitle.addAll(temp);
                                    jieShuoList.addAll(temp);
                                    updataMap();
                                    initYuyinView(temp);
                                    slv.setFlag(0);

                                }
                            } catch (Exception e) {

                            }
                        }

                    });

        }
    }

    Jieshuo jieshuoSelect = null;

    public void onTouch(MapWidget v, MapTouchedEvent event) {
        // TODO Auto-generated method stub`
        ArrayList<ObjectTouchEvent> touchedObjs = event.getTouchedObjectIds();
        if (touchedObjs.size() > 0) {
            slv.setVisibility(View.VISIBLE);
            ObjectTouchEvent objectTouchEvent = event.getTouchedObjectIds()
                    .get(0);
            Jieshuo jieshuo = (Jieshuo) objectTouchEvent.getObjectId();
            jieshuoLine = jieshuo;
            int pos = jieShuoList.indexOf(jieshuo);
            for (Jieshuo j : jieShuoList) {
                if (!(jieshuo.equals(j)))
                    j.setSelected(false);
                else {
                    j.setSelected(true);
                }
            }
            //            for(Jieshuo j:jieShuoList){
            //                if(j.equals(jieshuo)){
            //                    j.setSelected(true);
            //                }
            //            }
            updataMap();
            //            mAdapter.notifyDataSetChanged();
            slv.scrollToPosition(pos);
            slv.smoothScrollToPosition(pos);
            slv.setFlag(pos);

        } else {
            slv.setVisibility(View.GONE);
        }

    }

    private int xToScreenCoords(int mapCoord) {
        return (int) (mapCoord * mapWidget.getScale() - mapWidget.getScrollX());
    }

    private int yToScreenCoords(int mapCoord) {
        return (int) (mapCoord * mapWidget.getScale() - mapWidget.getScrollY());
    }


    private void handleOnMapScroll(MapWidget v, MapScrolledEvent event) {
        // When user scrolls the map we receive scroll events
        // This is useful when need to move some object together with the map

        int dx = event.getDX(); // Number of pixels that user has scrolled
        // horizontally
        int dy = event.getDY(); // Number of pixels that user has scrolled
        // vertically

    }

    private void playJieShuoAuto(Jieshuo jieshuo2) {
        if (jieshuo2 != null && SystemManger.yuying.equals(jieshuo2.getYuyin()))
            return;
        isAutoNavi = true;
        if (jieshuo2 != null) {
            if (!DBUtil.freeIsEnd(zone.getId(), zone.getName())
                    || DBUtil.zoneIsPay(zone.getId()) || zone.getPrice() == 0 || (DBUtil.getLoginMeber() != null && DBUtil.getLoginMeber().getIsvip() == 1)) {
                if (!SystemManger.yuying.equals(jieshuo2.getYuyin())) {
                    for (Jieshuo j : jieShuoList) {
                        if (j.getName().equals(jieshuo2.getName())) {
                            j.setListen(true);
                            j.setSelected(true);
                        } else {
                            j.setSelected(false);
                            j.setListen(false);
                        }
                    }
                    jieshuo3 = jieshuo2;
                    //                    mAdapter.notifyDataSetChanged();
                    slv.setFlag(jieShuoList.indexOf(jieshuo2));
                    slv.scrollToPosition(jieShuoList.indexOf(jieshuo2));
                    slv.smoothScrollToPosition(jieShuoList.indexOf(jieshuo2));
                    if (playBinder.isPlaying())
                        playBinder.stop(1);
                    playBinder.playTishi("msg.mp3", 1);
                    SystemManger.yuying = jieshuo2.getYuyin();
                    DBUtil.consumeFree(zone.getId());

                }
            } else {
                if (!SystemManger.isInPayZoneActivity) {
                    slv.scrollToPosition(jieShuoList.indexOf(jieshuo2));
                    slv.smoothScrollToPosition(jieShuoList.indexOf(jieshuo2));
                    AppUtil.showToastMsg(ActivityZone2.this, "亲，试用结束了,请激活景点");
                    goActivityPayZone();
                }
            }
        }
    }

    private void helloserver(String deviceId, Integer id, Integer id2) {
        // TODO Auto-generated method stub
        BaseHttp http = new BaseHttp();
        AjaxParams params = new AjaxParams();
        params.put("tid", deviceId);
        params.put("jsid", id + "");
        params.put("jid", id2 + "");
        http.post(Constance.HTTP_REQUEST_URL + "helloserver", params,
                new AjaxCallBack<Object>() {

                });
    }

    private Jieshuo playingJieshuo = null;


    private void updataMap() {
        if (layer != null)
            layer.clearAll();
        if (mapWidget != null)
            mapWidget.clearMapPath();
        Jieshuo tempShow = null;
        Jieshuo tempPlay = null;
        // ArrayList<Point> path = new ArrayList<Point>();
        for (int j = 0; j < jieShuoList.size(); j++) {
            Jieshuo jieshuo2 = jieShuoList.get(j);
            //iv_map_marker3;
            Drawable icon = getResources().getDrawable(
                    R.drawable.map_js_marker70);
            if (jieshuo2.isSelected()) {
                icon = getResources().getDrawable(R.drawable.iv_zone_go);
                tempShow = jieshuo2;
            }
            if (jieshuo2.isListen()) {
                icon = getResources().getDrawable(R.drawable.iv_map_market1);
                tempPlay = jieshuo2;
            }


            MapObject mapObject = new MapObject(jieshuo2, icon, new Point(
                    jieshuo2.getZuobiao_x(), jieshuo2.getZuobiao_y()),
                    PivotFactory.createPivotPoint(icon,
                            PivotFactory.PivotPosition.PIVOT_CENTER), true, false);
            if (!jieshuo2.isSelected() || !jieshuo2.isListen())
                layer.addMapObject(mapObject);

        }
        if (tempShow != null && tempShow.isSelected() && !tempShow.isListen()) {
            MapObject mapObject = new MapObject(tempShow, getResources().getDrawable(R.drawable.iv_zone_go), new Point(
                    tempShow.getZuobiao_x(), tempShow.getZuobiao_y()),
                    PivotFactory.createPivotPoint(getResources().getDrawable(R.drawable.iv_zone_go),
                            PivotFactory.PivotPosition.PIVOT_CENTER), true, false);
            layer.addMapObject(mapObject);
        }
        if (tempPlay != null && tempPlay.isListen()) {
            MapObject mapObject = new MapObject(tempPlay, getResources().getDrawable(R.drawable.iv_map_market1), new Point(
                    tempPlay.getZuobiao_x(), tempPlay.getZuobiao_y()),
                    PivotFactory.createPivotPoint(getResources().getDrawable(R.drawable.iv_map_market1),
                            PivotFactory.PivotPosition.PIVOT_CENTER), true, false);
            layer.addMapObject(mapObject);

        }


    }

    boolean isShowMyMenu = false;
    boolean isHide = false;
    PopWindowJieShuoList popJieShuo;

    public void onClick(View v) {
        // TODO Auto-generated method stub
        try {
            String str = tvDownload.getText().toString();
            switch (v.getId()) {
                case R.id.mtv_one:
                    if (myOne.isChecked()) {
                        myOne.setChecked(false);
                    } else {
                        myOne.setChecked(true);
                        if (myTwo.isChecked()) {
                            myTwo.setChecked(false);
                            ltTopTag.setVisibility(View.GONE);
                        }

                    }
                    if (myOne.isChecked()) {
//                        Intent intent = new Intent(ActivityZone2.this, ActivityZoneDetail.class);
//                        intent.putExtra("zonestr", JsonUtil.getJson(zone));
//                        intent.putExtra("isDownload", DBUtil.zoneHaveDownload(zone.getId()));
//                        startActivity(intent);

                    }
                    break;
                case R.id.mtv_two:
                    if (myTwo.isChecked()) {
                        myTwo.setChecked(false);
                    } else {
                        myTwo.setChecked(true);
                        if (myOne.isChecked()) {
                            myOne.setChecked(false);
                        }
                    }
                    if (myTwo.isChecked()) {
                        ltTopTag.setVisibility(View.VISIBLE);
                    } else {
                        ltTopTag.setVisibility(View.GONE);
                    }
                    break;
                case R.id.tv_more:
//                    Intent intent = new Intent(this, ActivityZoneTicket.class);
//                    intent.putExtra("model", zone);
//                    startActivity(intent);
                    break;
                case R.id.iv_zone_back:
                    onBackPressed();
                    break;
                case R.id.tv_auto_navi:
                    downloadOrNav();
                    break;
                case R.id.tv_hide:
                    if (isHide) {
                        isHide = false;
                        updataMap();
                    } else {
                        isHide = true;
                        layer.clearAll();
                        mapWidget.clearMapPath();
                        mapWidget.centerMap();
                    }
                    break;
                case R.id.tv_setting:
                    if (isShowMyMenu) {
                        isShowMyMenu = false;
                        AnimationSet set = new AnimationSet(false);
                        TranslateAnimation translate = new TranslateAnimation(0, 0, 0,
                                DensityUtil.dip2px(ActivityZone2.this, 30));
                        translate.setDuration(300);
                        translate.setFillAfter(false);
                        AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
                        alpha.setDuration(300);
                        alpha.setFillAfter(false);
                        set.addAnimation(alpha);
                        set.addAnimation(translate);
                        ltLeftMenu.setAnimation(set);
                        set.start();
                        ltLeftMenu.setVisibility(View.GONE);
                    } else {
                        isShowMyMenu = true;
                        AnimationSet set = new AnimationSet(false);
                        TranslateAnimation translate = new TranslateAnimation(0, 0,
                                DensityUtil.dip2px(ActivityZone2.this, 30), 0);
                        translate.setDuration(300);
                        translate.setFillAfter(false);
                        AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);
                        alpha.setDuration(300);
                        alpha.setFillAfter(false);
                        set.addAnimation(alpha);
                        set.addAnimation(translate);
                        ltLeftMenu.setAnimation(set);
                        set.start();
                        ltLeftMenu.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.btn_order:
                    Intent intent1 = new Intent(ActivityZone2.this, ActivityTicketShow.class);
                    intent1.putExtra("zone", tickets.get(0));
                    startActivity(intent1);
                    break;
                case R.id.tv_zone_list:
                    if (jieshuoTitle != null && jieshuoTitle.size() != 0) {
                        if (popJieShuo == null)
                            popJieShuo = new PopWindowJieShuoList(this, jieshuoTitle, zone);
                        popJieShuo.setmListener(this);
                        popJieShuo.showAtLocation(rlZoneMap, Gravity.BOTTOM, 0, DensityUtil.dip2px(this, 50));
                    }

                    break;
                case R.id.iv_close:
                    ltTopTag.setVisibility(View.GONE);
                    break;
                case R.id.tv_public:
//                    MyApplication.showSingleton("本景点暂无此服务");
                    break;
                case R.id.tv_route:
//                    MyApplication.showSingleton("本景点暂无此服务");
                    break;
                case R.id.tv_location_share:
//                    MyApplication.showSingleton("本景点暂无此服务");
                    break;
            }
        } catch (Exception e) {
            // TODO: handle exception
            BaseHttp http = new BaseHttp();
            Member member = DBUtil.getLoginMeber();
            AjaxParams params = new AjaxParams();
            params.put("sid", MyApplication.deviceId);
            if (member != null)
                params.put("tel", member.getTel());
            params.put("content", e.getMessage() + e.getCause());
            http.post(Constance.HTTP_REQUEST_URL + "dosaveYiChang", params,
                    new AjaxCallBack<Object>() {

                        @Override
                        public void onFailure(Throwable t, int errorNo,
                                              String strMsg) {
                            // TODO Auto-generated method stub
                            super.onFailure(t, errorNo, strMsg);
                        }

                        @Override
                        public void onSuccess(Object t) {
                            // TODO Auto-generated method stub
                            super.onSuccess(t);
                        }

                    });
        }
    }

    private void goActivityDashang() {
        // TODO Auto-generated method stub
//        Intent intent = new Intent(ActivityZone2.this, ActivityDaShang.class);
//        startActivity(intent);
    }

    private void goActivityJieshuoDetail() {
        // TODO Auto-generated method stub
//        Intent intent = new Intent(ActivityZone2.this, ActivityJieshuoDetail.class);
//        Type type = new TypeToken<Jieshuo>() {
//        }.getType();
//        String str = JsonUtil.toJsonString(jieshuo3, type);
//        intent.putExtra("jieshuostr", str);
//        startActivity(intent);
    }


    private void downloadOrNav() {
        String str = tvDownload.getText().toString();
        if (str.isEmpty()) {
            return;
        }
        if (str.equals("下载语音离线包")) {
            downloadBinder.downloadZone(jieShuoList, zone);
            handler.removeMessages(1);
            handler.sendEmptyMessage(1);
        } else if (str.equals("自动导览")) {
            if (!zone.getAutomatic().equals("1")) {
//                MyApplication.showSingleton("亲，该景点暂不提供自动导览服务");
                return;
            }
            if (DBUtil.zoneHaveDownload(zone.getId())) {
                jieshuoIndex = 0;
                SystemManger.yuying = "";
                tvDownload.setText("停止导览");
                playBinder.pause(1);
                playBinder.pause(2);
                playBinder.playTishi("start_nav.mp3", 0);
                jieshuo1 = null;
                startBluetooth();
            }

        } else if (str.equals("停止导览")) {
            jieshuo1 = null;
            tvDownload.setText("自动导览");
            stopBluetooth();
        }
    }

    private Handler handler1 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            playJieShuoAuto((Jieshuo) msg.obj);
        }

    };

    private void startBluetooth() {
        Intent intent;
        if (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)
                && Build.VERSION.SDK_INT >= 18)
            intent = new Intent("cn.com.wideroad.xiaolu.service.BluetoothImp4");
        else
            intent = new Intent("cn.com.wideroad.xiaolu.service.BluetoothImp2");
        if (zone.getType().equals("1"))
            intent = new Intent("cn.com.wideroad.xiaolu.service.gpsService");
        intent.setPackage(getPackageName());
        startService(intent);
    }

    protected void goActivityPayZone() {
        // TODO Auto-generated method stub
        BaseHttp http = new BaseHttp();
        AjaxParams params = new AjaxParams();
        Log.v("123", "device_id--->" + MyApplication.deviceId);
        Log.v("123", "zone id--->" + zone.getId());
        params.put("sid", MyApplication.deviceId);
        params.put("id", zone.getId() + "");
        http.post(Constance.HTTP_REQUEST_URL + "getJingdianById", params,
                new AjaxCallBack<Object>() {

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
                        super.onFailure(t, errorNo, strMsg);
                    }

                    @Override
                    public void onSuccess(Object t) {
                        // TODO Auto-generated method stub
                        super.onSuccess(t);
                        try {
                            Type type = new TypeToken<Zone>() {
                            }.getType();
                            Zone zone = (Zone) JsonUtil.fromJsonToObject(
                                    StringUtil.removeNull(t), type);
//                            Intent intent = new Intent(ActivityZone2.this,
//                                    ActivityPayZone.class);
//                            intent.putExtra("name", zone.getName());
//                            intent.putExtra("memo", zone.getMemo());
//                            intent.putExtra("money", zone.getPrice() + "");
//                            intent.putExtra("id", zone.getId());
//                            startActivityForResult(intent, 14);
                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }

                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 14 && resultCode == 1) {
            loadZoneData();
            DBUtil.setPamenet(zone.getId(), zone.getName());
        }
    }

    private void loadZoneData() {
        BaseHttp http = new BaseHttp();
        AjaxParams params = new AjaxParams();
        params.put("sid", MyApplication.deviceId);
        params.put("id", zone.getId() + "");
        http.post(Constance.HTTP_REQUEST_URL + "getJingdianById", params,
                new AjaxCallBack<Object>() {

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                    }

                    @Override
                    public void onSuccess(Object t) {
                        super.onSuccess(t);
                        try {
                            Type type = new TypeToken<Zone>() {
                            }.getType();
                            zone = (Zone) JsonUtil.fromJsonToObject(
                                    t.toString(), type);

                        } catch (Exception e) {

                        }
                    }

                });
    }

    protected Jieshuo getJieshuo(String mac) {
        for (Jieshuo jieshuo : jieShuoList) {
            if (jieshuo.getMac().equals(mac))
                return jieshuo;
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
        handler1.removeMessages(1);
        stopBluetooth();
        unregisterReceiver(receiver);
        unbindService(conn);
        unbindService(conn1);
        EventBus.getDefault().unregister(this);
        tpm.listen(phoneListener, 0);
        if (jieShuoList != null && jieShuoList.size() != 0)
            jieShuoList.clear();
        if (tickets != null && tickets.size() != 0)
            tickets.clear();

        if (savedInstanceState != null) {
            savedInstanceState.clear();
            savedInstanceState = null;
        }
        if (layer != null) {
            layer.clearAll();
            layer = null;
            System.gc();
        }
        if (mapWidget != null) {
            mapWidget.clearMapPath();
            mapWidget.removeAllLayers();
            mapWidget.removeAllMapEventsListeners();
            rlZoneMap.removeView(mapWidget);
            mapWidget = null;
            System.gc();
        }

        System.gc();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mSensorManager.unregisterListener(this);

    }

    private void stopBluetooth() {
        Intent intent;
        SystemManger.yuying = "";
        if (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)
                && Build.VERSION.SDK_INT >= 18)
            intent = new Intent("cn.com.wideroad.xiaolu.service.BluetoothImp4");
        else
            intent = new Intent("cn.com.wideroad.xiaolu.service.BluetoothImp2");
        if (zone.getType().equals("1"))
            intent = new Intent("cn.com.wideroad.xiaolu.service.gpsService");
        intent.setPackage(getPackageName());
        stopService(intent);
        if (jieshuoPopup != null)
            jieshuoPopup.hide();
        if (playBinder != null) {
            playBinder.pause(1);
            playBinder.pause(2);
        }
        for (Jieshuo j : jieShuoList) {
            j.setListen(false);
        }
        updataMap();
        mAdapter.notifyDataSetChanged();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context1, Intent intent) {
            // TODO Auto-generated method stub
            try {
                String action = intent.getAction();
                if (action.equals(Constance.FIND_BLUE_TOOTH)) {
                    String address = intent.getStringExtra("address");
                    Float dis = intent.getFloatExtra("dis", 0);
                    if (dis > zone.getDistance()) {
                        return;
                    }
                    Message msg = Message.obtain();
                    Jieshuo jieshuo = getJieshuo(address);
                    //					("Bluetooth4","onReceive 解说点名称："+jieshuo.getMac()+"-->"+jieshuo.getName());
                    msg.obj = jieshuo;
                    handler1.sendMessage(msg);
                } else if (action.equals(Constance.GPSLOCATION)) {
                    double longitude = intent.getDoubleExtra("longitude", 0);
                    double latitude = intent.getDoubleExtra("latitude", 0);
                    Jieshuo jie = getJieshuoBylocation(longitude, latitude);
                    Message msg = Message.obtain();
                    msg.obj = jie;
                    handler1.sendMessage(msg);
                } else if (action.equals(Constance.JIESHUO_PLAY_END)) {
                    playBinder.pause(1);
                    if (jieshuo3 != null)
                        jieshuo3.setListen(false);
                    int pos = jieShuoList.indexOf(jieshuo3);

                    mAdapter.notifyDataSetChanged();
                    updataMap();

                } else if (action.equals(Constance.DOWNLOAD_MAP_DONE)) {
                    initMap();
                    pbarDialog.dismiss();
                    loadData(isDownload);
                } else if (action.equals(Constance.TISHI_END)) {

                    playBinder.play(jieshuo3);
                    int pos = jieShuoList.indexOf(jieshuo3);
                    jieShuoList.get(pos).setListen(true);
                    //                    updataMap();
                    //                    mAdapter.notifyDataSetChanged();
                } else if (action.equals(Constance.DOWNLOAD_MAP_FAIL)) {
                    pbarDialog.dismiss();
//                    MyApplication.showSingleton("地图加载失败");
                } else if (action.equals(Constance.LINE_TISHI_END)) {
                    if (!isAutoNavi && playBinder != null)
                        playBinder.play1(Constance.HTTP_URL + "/upload/" + zone.getId() + "/" + netJieshuo.getYuyin() + ".mp3");
                }
            } catch (Exception e) {

            }
        }

    };

    protected Jieshuo getJieshuoBylocation(double longitude, double latitude) {
        // TODO Auto-generated method stub
        try {
            Jieshuo jieshuo1 = null;
            float dis1 = 0;
            for (Jieshuo jieshuo : jieShuoList) {
                float dis = AMapUtils.calculateLineDistance(new LatLng(
                        latitude, longitude), new LatLng(jieshuo.getLatitude(),
                        jieshuo.getLongitude()));
                if (dis1 == 0 || dis < dis1) {
                    jieshuo1 = jieshuo;
                    dis1 = dis;
                }
            }
            if (dis1 < zone.getDistance())
                return jieshuo1;
            else
                return null;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    private void initMap() {
        String url = zone.getMapsrc_android();
        int pos = url.lastIndexOf("/");
        String name = url.substring(pos + 1).split("[.]")[0];
        File file = FileUtil.getLocalFile1(FileUtil.getApkStorageFile(ActivityZone2.this),
                name);
        file = new File(file, "map");
        mapWidget = new MapWidget(savedInstanceState, this, file, 11);
        mapWidget.setOnMapTouchListener(this);
        mapWidget.addMapEventsListener(this);
        rlZoneMap.addView(mapWidget);
        mapWidget.getConfig().setFlingEnabled(true);
        mapWidget.getConfig().setPinchZoomEnabled(true);

        mapWidget.setMaxZoomLevel(13);
        mapWidget.setMinZoomLevel(10);
        mapWidget.setUseSoftwareZoom(true);
        mapWidget.setZoomButtonsVisible(false);
        layer = mapWidget.createLayer(100);
        mapWidget.setOnMapScrolledListener(new OnMapScrollListener() {
            public void onScrolledEvent(MapWidget v, MapScrolledEvent event) {
                handleOnMapScroll(v, event);
            }
        });
    }

    private void initMap1() {
        mapWidget = new MapWidget(savedInstanceState, this, "map", // root name
                // of the
                // map under
                // assets
                // folder.
                10); // initial zoom level
        layer = mapWidget.createLayer(100);
        jieshuoPopup = new JieshuoPopup(ActivityZone2.this, rlZoneMap);
    }

    @Override
    public void onPostZoomIn() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPostZoomOut() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPreZoomIn() {
        // TODO Auto-generated method stub
        if (jieshuoPopup != null) {
            jieshuoPopup.hide();
        }
    }

    @Override
    public void onPreZoomOut() {
        // TODO Auto-generated method stub
        if (jieshuoPopup != null) {
            jieshuoPopup.hide();
        }
    }

    @Override
    public int initView() {
        // TODO Auto-generated method stub
        return R.layout.activity_zone3;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        /*
         * float range = event.values[0]; if (range ==
         * mSensor.getMaximumRange()) {
         * audioManager.setMode(AudioManager.MODE_NORMAL); } else {
         * audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION); }
         */

    }

}