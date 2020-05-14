package com.xlu.uis;


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.amap.api.location.DPoint;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.GroundOverlay;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.route.RouteSearch;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pandacard.teavel.R;
import com.pandacard.teavel.apps.MyApplication;
import com.xlu.adapters.YuyinAdapter;
import com.xlu.bases.BaseActivity;
import com.xlu.po.Jieshuo;
import com.xlu.po.Merchant1;
import com.xlu.po.MyEvent;
import com.xlu.po.RouteInfos;
import com.xlu.po.RouteJieShuo;
import com.xlu.po.Zone;
import com.xlu.sys.SystemManger;
import com.xlu.uis.maps.listener.AMapGeoFence;
import com.xlu.uis.maps.listener.MyInfoWindowAdapter2;
import com.xlu.uis.maps.listener.MyLocationSource;
import com.xlu.uis.maps.listener.MyOnPoiSearchListener;
import com.xlu.uis.maps.listener.MyRouteSearchListener;
import com.xlu.utils.AppUtil;
import com.xlu.utils.Constance;
import com.xlu.utils.DBUtil;
import com.xlu.utils.DensityUtil;
import com.xlu.utils.DistanceUtil;
import com.xlu.utils.FileUtil;
import com.xlu.utils.JsonUtil;
import com.xlu.utils.NetWorkHelper;
import com.xlu.utils.PoiOverlay;
import com.xlu.utils.SelectRouteMapWindow;
import com.xlu.utils.SensorEventHelper;
import com.xlu.utils.servers.BluetoothServiceImp2;
import com.xlu.utils.servers.BluetoothServiceImp4;
import com.xlu.utils.servers.DownloadService;
import com.xlu.utils.servers.PlayService;
import com.xlu.widgets.MyTabView;
import com.xlu.widgets.PopWindowJieShuoList;
import com.xlu.widgets.SnappingSwipingViewBuilder;
import com.xlu.widgets.SnappyLinearLayoutManager;
import com.xlu.widgets.SnappyRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.wideroad.BaseHttp;
import cn.com.wideroad.http.AjaxCallBack;
import cn.com.wideroad.http.AjaxParams;
import cn.com.wideroad.utils.StringUtil;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


public class Map2Activity extends BaseActivity implements View.OnClickListener,
        PopWindowJieShuoList.OnMyItemOnClickListener, YuyinAdapter.OnClickButtonListener {
    private MapView mapview;

    private Zone zone;
    private AMap amap;
    PopWindowJieShuoList popJieShuo = null;
    private List<Jieshuo> titleJieShuos = new ArrayList<>();
    private

    //标题栏
            RelativeLayout rlBiaoTi;
    private ImageView ivBack;
    MyTabView myOne, myTwo;

    //上部显示条
    LinearLayout ltTopTag;
    TextView tvTicketName;
    TextView tvMore;
    ImageView ivZoneTicketPic;
    TextView tvOldPrice;
    TextView tvNewPrice;
    TextView tvZoneName;
    Button btnOrder;
    ImageView ivClose, ivZonePic;

    RelativeLayout rlParent;

    //底部工具条
    LinearLayout ltBottomTool;
    TextView tvZoneList, tvAutoNavi, tvSetting;

    //左边工具条
    LinearLayout ltLeftMenu;
    Button btnRoute, btnPublic, btnLocationShare, btnHide;

    //错误提示
    public TextView mLocationErrText;

    //提示等待
    private RelativeLayout rlProgress;

    SnappyRecyclerView slv;
    RelativeLayout rlChange;


    private HttpUtils mHttp = new HttpUtils();
    private boolean isShowMyMenu = false;
    private boolean isAutoNavi = true;
    private boolean isShow = true;
    private boolean isLocation = false;
    Context context;

    private TelephonyManager tpm;
    private Map2Activity.MyPhoneStateListener phoneListener;


    private MyLocationSource myLocationSource;
    private SensorEventHelper mSensorHelper;
    private Bitmap bitmap;
    private List<Jieshuo> jieShuoList;
    private PoiOverlay jdOverlay;
    private List<PoiItem> jdMarks = new ArrayList<PoiItem>();
    private Marker mlastMarker;
    //    private EditText mSearchText;
    private LatLonPoint lp;
    private RouteSearch routeSearch;
    private DownloadService.DownloadBinder downBinder;
    protected PlayService.PlayBinder playBinder;
    private Jieshuo jieshuo;
    private Jieshuo jieshuo1;
    private Jieshuo jieshuo3;
    private MyRouteSearchListener myRouteSearchListener;
    private int jieshuoIndex;
    private List<RouteInfos> routeList = new ArrayList<RouteInfos>();
    private Map<String, GroundOverlay> overlayMap = new HashMap<String, GroundOverlay>();
    private MyInfoWindowAdapter2 infoAdapter;
    private MyOnPoiSearchListener searchListener = new MyOnPoiSearchListener();
    private HashMap<String, File> routePathMap = new HashMap<String, File>();
    public int[] markers = {R.drawable.poi_marker_1, R.drawable.poi_marker_2,
            R.drawable.poi_marker_3, R.drawable.poi_marker_4,
            R.drawable.poi_marker_5, R.drawable.poi_marker_6,
            R.drawable.poi_marker_7, R.drawable.poi_marker_8,
            R.drawable.poi_marker_9, R.drawable.poi_marker_10};


    Button btnHf;
    List<Jieshuo> playingJieShuos = new ArrayList<Jieshuo>();//存放正在播放解说点
    private int i = 0;
    private int tag = 1;
    int playMethod = -1;//-1 为点击播放，0为自动播放方式

    private Handler handler1 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            playJieShuoAuto((Jieshuo) msg.obj);
        }

    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (DBUtil.zoneHaveDownload(zone.getId())) {
                tvAutoNavi.setText("自动导览");
                tvAutoNavi.setCompoundDrawables(null, null, null, null);
                if (!zone.getAutomatic().equals("1")) {
                    tvAutoNavi.setTextColor(0x99999999);
                }
                return;
            } else {
                if (downBinder.isDownloading(zone)) {
                    tvAutoNavi.setText("正在下载" + downBinder.getProgress(zone)
                            + "%");
                } else {
                    tvAutoNavi.setText("下载语音离线包");
                }
            }
            handler.sendEmptyMessageDelayed(1, 1000);
        }
    };
    private Handler handlerWL = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mAMapGeoFence.drawFenceToMap();
                    break;
                case 1:
                    int errorCode = msg.arg1;
                    /*Toast.makeText(getApplicationContext(),
                            "添加围栏失败 " + errorCode, Toast.LENGTH_SHORT).show();*/
                    break;
                case 2:
                    String statusStr = (String) msg.obj;
                    int jsId = Integer.parseInt(statusStr);
                    for (int i = 0; i < jieShuoList.size(); i++) {
                        if (jieShuoList.get(i).getId() == jsId) {
                            playJieShuoAuto(jieShuoList.get(i));
                            break;
                        }
                    }

                    /*Toast.makeText(getApplicationContext(), statusStr,
                            Toast.LENGTH_SHORT).show();*/
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initviews();
        mapview.onCreate(savedInstanceState);
        infoAdapter = new MyInfoWindowAdapter2(this);
        myOne.setText("景区攻略");
        myTwo.setText("景区门票");

        initPhoneListener();
        getZone();
        initServer();
        initSet();
        initBrocastReceiver();


    }

    // --------------------------监听来电----------------------------------
    private void initPhoneListener() {
        // 获取电话管理类

        tpm = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        phoneListener = new Map2Activity.MyPhoneStateListener();
        // 设置监听
        tpm.listen(phoneListener,
                PhoneStateListener.LISTEN_CALL_STATE);
    }

    List<Merchant1> tickets;

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
                                tvZoneName.setText(tickets.get(0).getName());
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

    private boolean haveTicket = false;


    private void initBrocastReceiver() {
        // 注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constance.FIND_BLUE_TOOTH);
        filter.addAction(Constance.JIESHUO_PLAY_END);
        filter.addAction(Constance.TISHI_END);
        filter.addAction(Constance.LINE_TISHI_END);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constance.FIND_BLUE_TOOTH)) {
                String address = intent.getStringExtra("address");
                if (jieShuoList != null) {
                    for (Jieshuo shuo : jieShuoList) {
                        Log.v("123", "shuo--mac:" + shuo.getName() + "--" + shuo.getMac());
                        Log.v("123", "address" + address);
                        if (shuo.getMac().equals(address)) {
                            Float dis = intent.getFloatExtra("dis", 0);
                            // TODO 通过距离判断
                            if (dis > zone.getDistance()) {
                                myLocationSource.myAMapLocationListener.isGaoDe = true;
                                return;
                            }
                            Message msg = Message.obtain();
                            Jieshuo jieshuo = getJieshuo(address);
                            msg.obj = jieshuo;
                            handler1.sendMessage(msg);
                            myLocationSource.myAMapLocationListener.isGaoDe = false;
                            return;
                        }
                    }
                }
            } else if (action.equals(Constance.TISHI_END)) {
                playBinder.play(jieshuo3);
            } else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_OFF) {
                    myLocationSource.myAMapLocationListener.isGaoDe = true;
                }
            } else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetworkInfo = connectivityManager
                        .getActiveNetworkInfo();
                if (activeNetworkInfo != null) {
                    int netType = activeNetworkInfo.getType();
                    if (netType == ConnectivityManager.TYPE_MOBILE
                            || netType == ConnectivityManager.TYPE_WIFI) {
                    }
                }
            } else if (action.equals(Constance.LINE_TISHI_END)) {
                if (!isAutoNavi)
                    playBinder.play1(Constance.HTTP_URL + "/upload/" + zone.getId()
                            + "/" + jieshuo3.getYuyin() + ".mp3");

            } else if (action.equals(Constance.JIESHUO_PLAY_END)) {

                for (Jieshuo j : jieShuoList) {
                    if (j.isListen()) {
                        j.setListen(false);
                        if (j.isSelected()) {
                            markerList.get(jieShuoList.indexOf(j)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.iv_zone_go));
                        } else {
                            if (tag == 1) {
                                markerList.get(jieShuoList.indexOf(j)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_js_marker70));
                            } else {
                                View view = LayoutInflater.from(Map2Activity.this)
                                        .inflate(R.layout.route_jieshuo_marker_layout,
                                                null);
                                TextView tv = view
                                        .findViewById(R.id.tv_marker_name);
                                tv.setText(jieShuoList.indexOf(j) + 1 + "");
                                markerList.get(jieShuoList.indexOf(j)).setIcon(BitmapDescriptorFactory.fromView(view));
                            }
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
                //                playingMarker=null;
                if (!playingJieShuos.isEmpty())
                    playingJieShuos.clear();
            }

        }
    };

    protected Jieshuo getJieshuo(String mac) {
        for (Jieshuo jieshuo : jieShuoList) {
            if (jieshuo.getMac().equals(mac))
                return jieshuo;
        }
        return null;
    }

    private void initServer() {
        Intent downIntent = new Intent(this, DownloadService.class);
        bindService(downIntent, downService, BIND_AUTO_CREATE);

        Intent playIntent = new Intent(this, PlayService.class);
        playIntent.setAction("cn.com.wideroad.xiaolu.service.play");
        bindService(playIntent, playService, BIND_AUTO_CREATE);
    }

    private void loadData(int id) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("xid", id + "");
        mHttp.send(HttpRequest.HttpMethod.POST, Constance.HTTP_URL + "tour/jdXianLuList", params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                //                MyApplication.show("获取数据失败");
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                Log.d("onSuccess", arg0.result + "loadData");
                JSONObject obj;
                try {
                    obj = new JSONObject(arg0.result);
                    JSONArray array = obj.getJSONArray("rows");
                    List<RouteInfos> netRouteList = JsonUtil.getList(
                            array.toString(), RouteInfos.class);
                    for (int i = 0; i < netRouteList.size(); i++) {
                        RouteInfos route = netRouteList.get(i);
                        route.setJid(zone.getId());
                        routeList.add(route);
                    }
                    DBUtil.saveRouteInfo(routeList);
                    downRouteImage();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private ServiceConnection downService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downBinder = (DownloadService.DownloadBinder) service;
            initDownViews();

            String url = zone.getMapsrc_ios();
            Log.d("netMap", url);
            if (url == null || "".equals(url)) {
                AppUtil.showToastMsg(Map2Activity.this, "景点地图正在建设中，敬请期待");
            } else {
                int pos = url.lastIndexOf("/");
                String fileName = url.substring(pos + 1).split("[.]")[0];
                File mapDir = new File(
                        FileUtil.getApkStorageFile(Map2Activity.this), fileName);
                final File mapPng = new File(mapDir, url);
                // TODO 判断地图目录是否存在
                if (!mapDir.exists()) {
                    mapDir.mkdirs();
                }
                if (mapPng.exists()) {
                    bitmap = getNewBitmap(mapPng, 1);
                    // 添加覆盖层
                    addOverlayToMap();
                    rlProgress.setVisibility(View.GONE);
                } else {
                    Log.d("nkw", "mapImage-->" + Constance.HTTP_URL + url);
                    mHttp.download(Constance.HTTP_URL + url,
                            mapPng.getAbsolutePath(), true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                            true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                            new RequestCallBack<File>() {

                                @Override
                                public void onSuccess(ResponseInfo<File> arg0) {
                                    bitmap = getNewBitmap(mapPng, 1);
                                    // 添加覆盖层
                                    addOverlayToMap();
                                    rlProgress.setVisibility(View.GONE);
                                }

                                @Override
                                public void onFailure(HttpException arg0,
                                                      String arg1) {
                                    //                                    MyApplication.showSingleton("下载失败");
                                    rlProgress.setVisibility(View.GONE);
                                }
                            });
                }
            }

        }
    };

    private Bitmap getNewBitmap(File file, int type) {
        // TODO 判断本地是否有地图图层,有就从本地拿,没有就改为从网络获取图片,保存本地后再拿.
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        if (type == 1)
            options.inSampleSize = 1; // width，hight设为原来的2分一,图像变为原来的四分之一;
        else
            options.inSampleSize = 3;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
        return bitmap;


    }

    private void initDownViews() {
        if (DBUtil.zoneHaveDownload(zone.getId())) {
            initMarker();
            tvAutoNavi.setText("自动导览");
            tvAutoNavi.setCompoundDrawables(null, null, null, null);
        } else {
            if (downBinder.isDownloading(zone)) {
                handler.removeMessages(1);
                handler.sendEmptyMessage(1);
                tvAutoNavi.setText("正在下载" + downBinder.getProgress(zone) + "%");
            } else {
                tvAutoNavi.setText("下载语音离线包");
            }
        }
    }

    private ServiceConnection playService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            playBinder = (PlayService.PlayBinder) service;
            infoAdapter.playBinder = playBinder;
            System.out.println("2");
        }
    };

    private void initSet() {

        if (amap == null) {
            amap = mapview.getMap();
            amap.showMapText(true);
            amap.showBuildings(true);
        }

        amap.getUiSettings().setZoomControlsEnabled(false);

        location();
        // 添加锚点
        setOnLoadJdDataListener(new OnLoadJDListener() {

            @Override
            public void onLoadJdData(String string) {
                List<Jieshuo> temp = JsonUtil.getList(string, Jieshuo.class);
                titleJieShuos.addAll(temp);
                if (temp.size() != 0) {
                    addJinDianMarker(temp);
                    // 路线规划
                    initRoute();
                    // 描点响应事件
                    initMarker();
                    initYuyinView(temp);
                }
            }
        });
        loadJinDianData();


    }

    boolean isClicked = false;


    YuyinAdapter mAdapter;

    private void initYuyinView(final List<Jieshuo> jieshuos) {

        if (jieShuoList.size() != 0)
            jieShuoList.clear();
        jieShuoList.addAll(jieshuos);
        if (jieShuoList.size() != 0) {
            jieShuoList.get(0).setSelected(true);
            mAdapter.notifyDataSetChanged();
            slv.setFlag(0);
        }

    }

    Jieshuo showJieshuo;


    //    JieShuoPageListener jieShuoPageListener;
    List<Marker> markerList;

    @Override
    public void onItemOnClick(Jieshuo jieshuo) {
        for (int i = 0; i < jieShuoList.size(); i++) {
            if (jieShuoList.get(i).getName().equals(jieshuo.getName())) {
                slv.setVisibility(View.VISIBLE);
                jieShuoList.get(i).setSelected(true);
                slv.scrollToPosition(i);
                slv.smoothScrollToPosition(i);
                slv.setFlag(i);
                popJieShuo.dismiss();
            }
        }

    }

    Jieshuo playingJieshuo = null;

    @Override
    public void playOnClickListener(final Jieshuo jieshuo, final View ivPlay, final View ivStop) {

        for (Jieshuo j : jieShuoList) {
            if (j.getName().equals(jieshuo.getName())) {
                j.setListen(true);
            } else {
                j.setListen(false);
            }


        }
        for (Jieshuo j : jieShuoList) {
            if (j.isListen()) {
                if (j.getName().equals(jieshuo.getName())) {
                    markerList.get(jieShuoList.indexOf(j)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.iv_map_market1));
                } else {
                    markerList.get(jieShuoList.indexOf(j)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.iv_zone_go));
                }
            } else {
                if (tag == 1) {
                    markerList.get(jieShuoList.indexOf(j)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_js_marker70));
                } else if (tag == 2) {
                    View view = LayoutInflater.from(Map2Activity.this)
                            .inflate(R.layout.route_jieshuo_marker_layout,
                                    null);
                    TextView tv = view
                            .findViewById(R.id.tv_marker_name);
                    tv.setText(jieShuoList.indexOf(j) + 1 + "");
                    markerList.get(jieShuoList.indexOf(j)).setIcon(BitmapDescriptorFactory.fromView(view));
                }

            }
        }
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
                //                jieshuo = jieshuo2;
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
                            Map2Activity.this);
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
                                            markerList.get(jieShuoList.indexOf(jieshuo2)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.iv_zone_go));


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
                                                    .isNetWorkAvailble(Map2Activity.this)) {
                                                playBinder.playTishi(
                                                        "msg.mp3", 0);
                                                playingJieshuo = jieshuo;
                                                jieshuo3 = jieshuo;
                                                //                                                playMarker=markerList.get(jieShuoList.indexOf(jieshuo));
                                                //                                                playingMarker=markerList.get(jieShuoList.indexOf(jieshuo));


                                            } else {
                                                //                                                MyApplication.show("无网络无法使用");
                                                markerList.get(jieShuoList.indexOf(jieshuo)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.iv_zone_go));
                                                playBinder.stop(1);
                                                return;
                                            }

                                            // playBinder.play1(Constance.HTTP_URL+"/upload/"+zone.getId()+"/"+jieshuo.getYuyin()+".mp3");

                                            dialog.dismiss();

                                        }
                                    }).create().show();
                } else {
                    isAutoNavi = false;
                    playBinder.pause(1);
                    jieshuo3 = jieshuo;
                    DBUtil.consumeFree(zone.getId());
                    if (NetWorkHelper
                            .isNetWorkAvailble(Map2Activity.this)) {
                        playBinder.playTishi(
                                "msg.mp3", 0);
                        playingJieshuo = jieshuo;


                    } else {
                        //                        MyApplication.show("无网络无法使用");
                        ivPlay.setVisibility(View.VISIBLE);
                        ivStop.setVisibility(View.GONE);
                        markerList.get(jieShuoList.indexOf(jieshuo)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.iv_zone_go));
                        return;
                    }
                }
                isClicked = true;

            }

        } else {
            if (!SystemManger.isInPayZoneActivity) {
                AppUtil.showToastMsg(Map2Activity.this, "亲，试用结束了,请激活景点");
                goActivityPayZone();
                jieshuo.setListen(false);
                jieshuo.setSelected(true);
                playBinder.stop(1);
                markerList.get(jieShuoList.indexOf(jieshuo)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.iv_zone_go));
                return;
            }
        }

        playingJieshuo = jieshuo;
        ivPlay.setVisibility(View.GONE);
        ivStop.setVisibility(View.VISIBLE);
        mAdapter.notifyDataSetChanged();


    }

    @Override
    public void stopOnClickListener(Jieshuo jieshuo, View ivPlay, View ivStop) {


        playBinder.pause(1);
        for (Marker marker : markerList) {
            if (marker.getTitle().equals(jieshuo.getName())) {

                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.iv_zone_go));
            }
        }
        for (Jieshuo j : jieShuoList) {
            if (j.equals(jieshuo)) {
                j.setListen(false);
                break;
            }
        }
        ivStop.setVisibility(View.GONE);
        ivPlay.setVisibility(View.VISIBLE);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void hereOnClickListener(Jieshuo jieshuo, View v) {
        if (myLocationSource.myAMapLocationListener.location != null) {
            if (myRouteSearchListener.mWalkRouteOverlay != null) {
                myRouteSearchListener.mWalkRouteOverlay.removeFromMap();
                myRouteSearchListener.mWalkRouteOverlay = null;
            }
            LatLng lat = new LatLng(jieshuo.getLatitude(), jieshuo.getLongitude());
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(
                    new RouteSearch.FromAndTo(
                            new LatLonPoint(
                                    myLocationSource.myAMapLocationListener.location.latitude,
                                    myLocationSource.myAMapLocationListener.location.longitude),
                            new LatLonPoint(lat.latitude, lat.longitude)),
                    RouteSearch.WalkDefault);
            routeSearch.calculateWalkRouteAsyn(query);
        }
    }

    @Override
    public void tuWenOnClickListener(Jieshuo jieshuo, View v) {
        //        Intent intent = new Intent(Map2Activity.this,
        //                ActivityJieshuoDetail.class);
        //        Type type = new TypeToken<Jieshuo>() {
        //        }.getType();
        //        String str = JsonUtil.toJsonString(jieshuo, type);
        //        intent.putExtra("jieshuostr", str);
        //        Map2Activity.this.startActivity(intent);
    }


    Marker playMarker = null;//正在展示的marker;

    private void addRouteMarker(List<Jieshuo> data) {
        if (jdOverlay == null) {
            //            MyApplication.showSingleton("无解说点数据");
            return;
        }

        jdOverlay.removeFromMap();
        jdMarks.clear();
        playBinder.pause(1);
        for (int i = 0; i < data.size(); i++) {
            Jieshuo jieshuo = data.get(i);
            PoiItem jdPoi = new PoiItem(jieshuo.getName(), new LatLonPoint(
                    Double.valueOf(df.format(jieshuo.getLatitude())),
                    Double.valueOf(df.format(jieshuo.getLongitude()))),
                    jieshuo.getName(), jieshuo.getMemo());
            jdMarks.add(jdPoi);
        }
        jdOverlay = new PoiOverlay(jdMarks, amap, Map2Activity.this);
        jdOverlay.addSpToMap();
        markerList = jdOverlay.getMarkerList();

    }


    private void initRoute() {
        routeSearch = new RouteSearch(this);
        myRouteSearchListener = new MyRouteSearchListener(this, amap);
        routeSearch.setRouteSearchListener(myRouteSearchListener);
    }

    private void initMarker() {

        amap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (jdOverlay.getMarkerList().contains(marker)) {
                    isAutoNavi = false;
                    playMethod = -1;
                    slv.setFlag(jdOverlay.getPoiIndex(marker));
                    slv.scrollToPosition(jdOverlay.getPoiIndex(marker));
                    slv.smoothScrollToPosition(jdOverlay.getPoiIndex(marker));
                    jieShuoList.get(jdOverlay.getPoiIndex(marker)).setSelected(true);
                    slv.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
    }

    private void initInfoAdapter() {
        if (infoAdapter.jdOverlay == null
                || infoAdapter.myLocationSource == null
                || infoAdapter.routeSearch == null
                || infoAdapter.jieShuoList == null
                || infoAdapter.myRouteSearchListener == null
                || infoAdapter.searchListener == null) {
            infoAdapter.jdOverlay = jdOverlay;
            infoAdapter.myLocationSource = myLocationSource;
            infoAdapter.routeSearch = routeSearch;
            infoAdapter.jieShuoList = jieShuoList;
            infoAdapter.myRouteSearchListener = myRouteSearchListener;
            infoAdapter.searchListener = searchListener;

            amap.setInfoWindowAdapter(infoAdapter);
        }

    }

    private void loadJinDianData() {
        if (jieShuoList.size() != 0)
            jieShuoList.clear();
        List<Jieshuo> jieshuos = null;
        Log.v("123", "name=" + zone.getName() + "---id=" + zone.getId());
        try {
            jieshuos = DBUtil.getJieshuoList(zone.getId());
        } catch (Exception e) {
            Log.v("123", "错误信息:" + e.getLocalizedMessage());
        }

        jieShuoList.addAll(jieshuos);
        //        jieShuoList = DBUtil.getJieshuoList(zone.getId());
        titleJieShuos.addAll(jieshuos);
        if (jieShuoList.size() != 0) {
            addJinDianMarker(jieshuos);
            // 路线规划
            initRoute();
            // 描点响应事件
            initMarker();
            initYuyinView(jieshuos);

        } else {
            RequestParams params = new RequestParams();
            params.addBodyParameter("sid", MyApplication.deviceId);
            params.addBodyParameter("jid", zone.getId() + "");
            mHttp.send(HttpRequest.HttpMethod.POST, Constance.HTTP_REQUEST_URL
                            + "getJieShuoDianByJid", params,
                    new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException e, String arg1) {
                            // ToastUtil.show(Map2Activity.this, "请求失败");
                            //                            MyApplication.showSingleton("请求失败");
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> info) {
                            Log.v("123", "请求内容：" + info.result);
                            if (listener != null) {
                                listener.onLoadJdData(info.result);
                            }
                        }
                    });
        }
    }

    private DecimalFormat df = new DecimalFormat("#.000000");

    private void addJinDianMarker(List<Jieshuo> jieshuos) {
        for (Jieshuo jieshuo : jieshuos) {
            Log.v("123", "解说点名称：" + jieshuo.getYuyin());
            PoiItem jdPoi = new PoiItem(jieshuo.getName(), new LatLonPoint(
                    Double.valueOf(df.format(jieshuo.getLatitude())),
                    Double.valueOf(df.format(jieshuo.getLongitude()))),
                    jieshuo.getName(), jieshuo.getMemo());
            jdMarks.add(jdPoi);
        }

        jdOverlay = new PoiOverlay(jdMarks, amap, Map2Activity.this);
        jdOverlay.addToMap();
        // jdOverlay.zoomToSpan();
        markerList = jdOverlay.getMarkerList();
    }

    private GroundOverlay mapOverlay;

    private void addOverlayToMap() {
        if (zone.getLatitude().equals("") || null == (zone.getLatitude())
                || zone.getLongitude().equals("")
                || null == zone.getLongitude())
            return;

        // TODO 东北坐标和西南坐标通过数据传来
        if (zone.getNorthw().equals(null) || zone.equals("")
                || zone.getNorthj().equals("") || zone.getNorthj().equals(null)
                || zone.getSouthj().equals("") || zone.getSouthj().equals(null)
                || zone.getSouthw().equals("") || zone.getSouthw().equals(null)) {
            return;
        }
        double dis = DistanceUtil.Distance(Double.valueOf(zone.getLongitude()), Double.valueOf(zone.getNorthw()), Double.valueOf(zone.getLongitude()), Double.valueOf(zone.getLatitude()));
        //      float myZoom=16;
        //        if(dis*1000<500){
        //            myZoom=16;
        //        }else if(dis*1000<600){
        //            myZoom=15;
        //        }else if(dis*1000<700){
        //            myZoom=14;
        //        }else if(dis*1000<800){
        //           myZoom=13.9f;
        //        }else if(dis*1000<900){
        //            myZoom=13.8f;
        //        }else  if(dis*1000<1000){
        //            myZoom=13.7f;
        //        }else if(dis*100<7000){
        //            myZoom=13.6f;
        //        }else if(dis*1000>7000){
        //            myZoom=16f;
        //        }
        Log.v("123", "dis=" + dis * 1000);
        //        LatLngBounds latBounds=new LatLngBounds(new LatLng(Double.valueOf(zone.getNorthw()),Double.valueOf(zone.getNorthj())),new LatLng(Double.valueOf(zone.getSouthw()),Double.valueOf(zone.getSouthj())));
        //        amap.moveCamera(CameraUpdateFactory.newLatLngBounds(latBounds,12));
        //        amap.moveCamera(CameraUpdateFactory.newLatLngZoom(
        //                new LatLng(Double.valueOf(zone.getLatitude()), Double
        //                        .valueOf(zone.getLongitude())),12));// 移动到目标图层
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(
                        new LatLng(Double.valueOf(zone.getNorthw()), Double
                                .valueOf(zone.getNorthj())))
                .include(
                        new LatLng(Double.valueOf(zone.getSouthw()), Double
                                .valueOf(zone.getSouthj()))).build();
        amap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
        mapOverlay = amap.addGroundOverlay(new GroundOverlayOptions()
                .anchor(0.5f, 0.5f).transparency(0.0f)
                //				 .zIndex(0f)//显示级别
                .image(BitmapDescriptorFactory.fromBitmap(bitmap))
                .positionFromBounds(bounds));
    }

    private void location() {
        // 定位
        mSensorHelper = new SensorEventHelper(this);
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }

        myLocationSource = new MyLocationSource(this, mSensorHelper, amap);
        amap.setLocationSource(myLocationSource);
        amap.getUiSettings().setMyLocationButtonEnabled(false);
        amap.setMyLocationEnabled(true);
        amap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

    }

    private void getZone() {
        Type type = new TypeToken<Zone>() {
        }.getType();
        zone = (Zone) JsonUtil.fromJsonToObject(
                getIntent().getStringExtra("zonestr"), type);
        List<RouteInfos> dbRouteList = DBUtil.getRouteInfo(zone);
        if (dbRouteList.size() == 0) {
            if (NetWorkHelper.isNetWorkAvailble(Map2Activity.this)) {
                loadData(zone.getId());
            }
        } else {
            routeList.addAll(dbRouteList);
            downRouteImage();
        }
        loadTicket();
    }

    private void downRouteImage() {
        Log.d("onSuccess", "server");
        if (routeList.size() != 0) {
            File routeDir = new File(
                    FileUtil.getApkStorageFile(Map2Activity.this),
                    "route");
            for (int i = 0; i < routeList.size(); i++) {
                final String routeName = routeList.get(i).getName();

                String src = routeList.get(i).getSrc();
                if (src == null || src.equals("")) {
                    AppUtil.showToastMsg(Map2Activity.this, "图片路径不能为空");
                    return;
                } else {
                    int pos = src.lastIndexOf("/");
                    String fileName = src.substring(pos + 1).split("[.]")[0];
                    final File routePng = new File(routeDir, fileName);
                    if (!routeDir.exists()) {
                        routeDir.mkdirs();
                    }
                    if (routePng.exists()) {
                        routePathMap.put(routeName, routePng);
                    } else {
                        Log.d("nkw", "routeImageURL---->" + Constance.HTTP_URL + src);
                        mHttp.download(Constance.HTTP_URL + src,
                                routePng.getAbsolutePath(), true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                                true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                                new RequestCallBack<File>() {

                                    @Override
                                    public void onSuccess(
                                            ResponseInfo<File> arg0) {
                                        Log.d("onSuccess", arg0.result.getAbsolutePath());
                                        routePathMap.put(routeName, arg0.result);
                                    }

                                    @Override
                                    public void onFailure(HttpException arg0,
                                                          String arg1) {
                                        //                                        MyApplication.showSingleton("推荐路线下载失败");
                                    }
                                });
                    }
                }
            }
        }
    }

    private RelativeLayout rlSearchBar;
    //    private LinearLayout llSearch;

    private void initviews() {
        mapview = findViewById(R.id.map);

        rlBiaoTi = findViewById(R.id.rl);
        ivBack = findViewById(R.id.iv_zone_back);
        myOne = findViewById(R.id.mtv_one);
        myTwo = findViewById(R.id.mtv_two);
        ltBottomTool = findViewById(R.id.ly_download);

        //右边按钮
        btnHf = findViewById(R.id.btn_huifu);

        rlParent = findViewById(R.id.rl_map);


        //上部显示条
        ltTopTag = findViewById(R.id.lt_tag);
        tvTicketName = findViewById(R.id.tv_zone_ticket_name);
        tvMore = findViewById(R.id.tv_more);
        //        tvZoneName= (TextView) findViewById(R.id.tv_zone_name);
        tvOldPrice = findViewById(R.id.tv_price_old);
        tvNewPrice = findViewById(R.id.tv_price);
        ivZonePic = findViewById(R.id.iv_zone_pic);
        ivClose = findViewById(R.id.iv_close);
        btnOrder = findViewById(R.id.btn_order);

        //底部工具条
        ltBottomTool = findViewById(R.id.ly_download);
        tvZoneList = findViewById(R.id.tv_zone_list);
        tvAutoNavi = findViewById(R.id.tv_auto_navi);
        tvSetting = findViewById(R.id.tv_setting);

        //左边工具条
        ltLeftMenu = findViewById(R.id.lt_left_menu);
        btnRoute = findViewById(R.id.btn_route);
        btnPublic = findViewById(R.id.btn_public);
        btnLocationShare = findViewById(R.id.btn_location_share);
        btnHide = findViewById(R.id.btn_hide);


        rlProgress = findViewById(R.id.rl_progress);

        mLocationErrText = findViewById(R.id.location_errInfo_text);


        //语音播放
        rlChange = findViewById(R.id.rl_change);


        //        mSearchText = (EditText) findViewById(R.id.input_edittext);

        mLocationErrText.setVisibility(View.GONE);
        tvZoneName = findViewById(R.id.tv_zone_name);
        jieShuoList = new ArrayList<Jieshuo>();
        myOne.setOnClickListener(this);
        myTwo.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvMore.setOnClickListener(this);
        ltTopTag.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        btnOrder.setOnClickListener(this);

        tvZoneList.setOnClickListener(this);
        tvAutoNavi.setOnClickListener(this);
        tvSetting.setOnClickListener(this);

        btnRoute.setOnClickListener(this);
        btnPublic.setOnClickListener(this);
        btnLocationShare.setOnClickListener(this);
        btnHide.setOnClickListener(this);

        btnHf.setOnClickListener(this);

        initRecycleView();

    }

    private void initRecycleView() {
        mAdapter = new YuyinAdapter(jieShuoList,this);
        slv = new SnappingSwipingViewBuilder(this)
                .setHeadTailExtraMarginDp(0F)
                .setOrientation(LinearLayoutManager.HORIZONTAL)
                .setItemAnimator(new DefaultItemAnimator())
                .setAdapter(mAdapter)
                .setItemMarginDp(0f, 0f, 0f, 0f)
                .setSnapMethod(SnappyLinearLayoutManager.SnappyLinearSmoothScroller.SNAP_CENTER)
                .build();
        slv.setVisibility(View.INVISIBLE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        slv.setLayoutParams(params);
        rlParent.addView(slv);

        mAdapter.setListener(this);
        slv.setOnItemSelectLitener(new MySnappyOnItemListener());
        //        slv.setOnItemSelectLitener(new SnappyRecyclerView.OnItemSelectLitener() {
        //            @Override
        //            public void onItemSelect(int position) {
        //                if(jieShuoList.size()==0)
        //                    return;
        //                Jieshuo j = jieShuoList.get(position);
        //                amap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(j.getLatitude(), j.getLongitude())));
        //
        //                if(showJieshuo!=null){
        //                    for(Jieshuo j2:jieShuoList){
        //                        if(j2.getName().equals(showJieshuo.getName())){
        //                            j2.setSelected(false);
        //                            if(tag==1){
        //                                markerList.get(jieShuoList.indexOf(j2)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_js_marker70));
        //                            }else{
        //                                View view = LayoutInflater.from(Map2Activity.this)
        //                                        .inflate(R.layout.route_jieshuo_marker_layout,
        //                                                null);
        //                                TextView tv = (TextView) view
        //                                        .findViewById(R.id.tv_marker_name);
        //                                tv.setText(jieShuoList.indexOf(j2) + 1 + "");
        //                                markerList.get(jieShuoList.indexOf(j2)).setIcon(BitmapDescriptorFactory.fromView(view));
        //                            }
        //
        //
        //                        }
        //                    }
        //                }
        //
        //                j.setSelected(true);
        //                showJieshuo=j;
        //                if(j.isSelected()){
        //                    markerList.get(position).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.iv_zone_go));
        //                    markerList.get(position).setToTop();
        //                }
        //                if(j.isListen()){
        //                    markerList.get(position).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.iv_map_market1));
        //                    markerList.get(position).setToTop();
        //                }
        //                for(Jieshuo je:jieShuoList){
        //                    if(!(je.equals(j))&&je.isListen()){
        //                        markerList.get(jieShuoList.indexOf(je)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.iv_map_market1));
        //                    }
        //                }
        //
        //                mAdapter.notifyDataSetChanged();
        //
        //
        //            }
        //        });


    }

    class MySnappyOnItemListener implements SnappyRecyclerView.OnItemSelectLitener {

        @Override
        public void onItemSelect(int position) {
            if (jieShuoList.size() == 0)
                return;
            Jieshuo j = jieShuoList.get(position);
            amap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(j.getLatitude(), j.getLongitude())));

            if (showJieshuo != null) {
                for (Jieshuo j2 : jieShuoList) {
                    if (j2.getName().equals(showJieshuo.getName())) {
                        j2.setSelected(false);
                        if (tag == 1) {
                            markerList.get(jieShuoList.indexOf(j2)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_js_marker70));
                        } else {
                            View view = LayoutInflater.from(Map2Activity.this)
                                    .inflate(R.layout.route_jieshuo_marker_layout,
                                            null);
                            TextView tv = view
                                    .findViewById(R.id.tv_marker_name);
                            tv.setText(jieShuoList.indexOf(j2) + 1 + "");
                            markerList.get(jieShuoList.indexOf(j2)).setIcon(BitmapDescriptorFactory.fromView(view));
                        }


                    }
                }
            }

            j.setSelected(true);
            showJieshuo = j;
            if (j.isSelected()) {
                markerList.get(position).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.iv_zone_go));
                markerList.get(position).setToTop();
            }
            if (j.isListen()) {
                markerList.get(position).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.iv_map_market1));
                markerList.get(position).setToTop();
            }
            for (Jieshuo je : jieShuoList) {
                if (!(je.equals(j)) && je.isListen()) {
                    markerList.get(jieShuoList.indexOf(je)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.iv_map_market1));
                }
            }

            mAdapter.notifyDataSetChanged();

        }
    }

    private boolean isShowSearch;
    private Intent intent;

    @Override
    public void onClick(View v) {
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
                    //                    intent = new Intent(Map2Activity.this, ActivityZoneDetail.class);
                    //                    intent.putExtra("zonestr", JsonUtil.getJson(zone));
                    //                    intent.putExtra("isDownload", DBUtil.zoneHaveDownload(zone.getId()));
                    //                    startActivity(intent);

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
                //                intent = new Intent(this, ActivityZoneTicket.class);
                //                intent.putExtra("model", zone);
                //                startActivity(intent);
                break;
            case R.id.iv_close:
                ltTopTag.setVisibility(View.GONE);
                break;
            case R.id.btn_order:
                Intent intent = new Intent(context, ActivityTicketShow.class);
                intent.putExtra("zone", tickets.get(0));
                context.startActivity(intent);
                break;
            case R.id.tv_zone_list:
                if (jieShuoList != null && jieShuoList.size() != 0) {
                    if (popJieShuo == null)
                        popJieShuo = new PopWindowJieShuoList(this, jieShuoList, zone);
                    popJieShuo.setmListener(this);
                    popJieShuo.showAtLocation(mapview, Gravity.BOTTOM, 0, DensityUtil.dip2px(this, 50));
                }
                break;
            case R.id.tv_auto_navi:
                // 下载语音
                isAutoNavi = true;
                downloadOrNav();
                break;
            case R.id.tv_setting:
                if (isShowMyMenu) {
                    isShowMyMenu = false;
                    AnimationSet set = new AnimationSet(false);
                    TranslateAnimation translate = new TranslateAnimation(0, 0, 0,
                            DensityUtil.dip2px(Map2Activity.this, 30));
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
                            DensityUtil.dip2px(Map2Activity.this, 30), 0);
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
            case R.id.iv_zone_back:
                onBackPressed();
                break;
            case R.id.btn_location_share:
                goActivityShot();
                break;
            case R.id.btn_route:
                // 推荐路线
                introduceRoute(v);
                break;
            case R.id.btn_public:
                //                MyApplication.showSingleton("本景点暂无此服务");
                break;
            case R.id.btn_hide:
                if (isShow) {
                    isShow = false;
                    //                    btnHide.setBackgroundResource(R.drawable.btn_hide);
                    jdOverlay.hideMarker();
                    if (playBinder.isPlaying()) {
                        playBinder.pause(1);
                    }
                } else {
                    isShow = true;
                    //                    btnHide.setBackgroundResource(R.drawable.btn_show);
                    jdOverlay.showMarker();
                }
                break;
            case R.id.btn_huifu:
                btnHf.setVisibility(View.GONE);
                if (playingJieShuos != null && playingJieShuos.size() != 0)
                    playingJieShuos.clear();

                //                if (playingMarker != null)
                //                    playingMarker = null;
                //                checkedPosition = 0;
                tag = 1;
                jdOverlay.removeFromMap();
                if (myLocationSource.myAMapLocationListener.location != null) {
                    if (myRouteSearchListener.mWalkRouteOverlay != null) {
                        myRouteSearchListener.mWalkRouteOverlay.removeFromMap();
                        myRouteSearchListener.mWalkRouteOverlay = null;
                    }
                }
                if (oldRouteOverlay != null)
                    oldRouteOverlay.remove();
                if (oldRouteBitmap != null) {
                    oldRouteBitmap.recycle();
                    oldRouteBitmap = null;
                    System.gc();
                }

                if (jieShuoList.size() != 0)
                    jieShuoList.clear();
                jieShuoList.addAll(titleJieShuos);
                addJinDianMarker(titleJieShuos);
                jieShuoList.get(0).setSelected(true);
                mAdapter.notifyDataSetChanged();
                slv.setFlag(0);
                //                vpChange.setVisibility(View.GONE);
                if (popJieShuo != null && popJieShuo.isShowing())
                    popJieShuo.dismiss();
                break;


            default:
                break;
        }

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


    private void goActivityShot() {
        if (jieShuoList.size() == 0) {
            if (NetWorkHelper.isNetWorkAvailble(Map2Activity.this)) {

            } else {
                //                MyApplication.showSingleton("无网络状况,不能使用功能");
            }
            return;
        }
//        amap.getMapScreenShot(
//                new MyOnMapScreenShotListener(this, jieShuoList.get(0), this));
    }

    private void hideRLSearchBar() {
        AnimationSet animationSet = new AnimationSet(false);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f,
                0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setDuration(1000);
        animationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rlSearchBar.setVisibility(View.GONE);
            }
        });
        rlSearchBar.startAnimation(animationSet);
    }

    private void showRLSearchBar() {
        rlSearchBar.setVisibility(View.VISIBLE);
        AnimationSet animationSet = new AnimationSet(false);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f,
                1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setInterpolator(new BounceInterpolator());// 设置动画插入器

        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setDuration(1000);
        rlSearchBar.startAnimation(animationSet);

    }

    private void addOverlay(View view) {
        // TODO 设置地图可见性
        if (view.isSelected()) {
            view.setSelected(false);
            /* mapOverlay.setVisible(false); */
        } else {
            view.setSelected(true);
            /* mapOverlay.setVisible(true); */
        }

    }

    SelectRouteMapWindow pop;

    private void introduceRoute(View v) {
        Log.d("introduceRoute", routeList.size() + "");
        if (routeList.size() == 0) {
            //            MyApplication.showSingleton("无推荐线路");
            return;
        }
        if (pop == null)
            pop = new SelectRouteMapWindow(context, routeList, listener1);
        pop.showAtLocation(
                LayoutInflater.from(Map2Activity.this).inflate(
                        R.layout.activity_map2, null), Gravity.BOTTOM, 0, 0);
    }

    private boolean isShowRoute = false;
    private boolean isShowRouteSearch = false;
    private Bitmap oldRouteBitmap;

    SelectRouteMapWindow.OnSelectChangeListener listener1 = new SelectRouteMapWindow.OnSelectChangeListener() {

        @Override
        public void onSelectChange(final RouteInfos route) {
            //			MyApplication.showSingleton(route.getName());
            if (oldRouteOverlay != null) {
                oldRouteOverlay.remove();
                oldRouteOverlay = null;
            }
            if (oldRouteBitmap != null) {
                oldRouteBitmap.recycle();
                oldRouteBitmap = null;
            }
            File routeFile = routePathMap.get(route.getName());
            Log.d("nkw", "routeImagePath---->" + routeFile.getAbsolutePath());
            //			Bitmap routeBitmap = BitmapFactory.decodeFile(routeFile.getAbsolutePath());
            Bitmap routeBitmap = getNewBitmap(routeFile, 2);
            oldRouteBitmap = routeBitmap;
            addRoutyeOverlayToMap(routeBitmap);
            routeBitmap.recycle();
            routeBitmap = null;
            initXianluJieshuo(route.getId());

            isShowRoute = true;
        }

    };

    private void goActivityDashang() {
        //        Intent intent = new Intent(context, ActivityDaShang.class);
        //        startActivity(intent);
    }

    private GroundOverlay oldRouteOverlay;

    protected void addRoutyeOverlayToMap(Bitmap bitmap) {


        if (zone.getLatitude().equals("") || null == (zone.getLatitude())
                || zone.getLongitude().equals("")
                || null == zone.getLongitude())
            return;
        // TODO 东北坐标和西南坐标通过数据传来
        if (zone.getNorthw().equals(null) || zone.equals("")
                || zone.getNorthj().equals("") || zone.getNorthj().equals(null)
                || zone.getSouthj().equals("") || zone.getSouthj().equals(null)
                || zone.getSouthw().equals("") || zone.getSouthw().equals(null)) {
            return;
        }
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(
                        new LatLng(Double.valueOf(zone.getNorthw()), Double
                                .valueOf(zone.getNorthj())))
                .include(
                        new LatLng(Double.valueOf(zone.getSouthw()), Double
                                .valueOf(zone.getSouthj()))).build();
        GroundOverlay routeOverlay = amap.addGroundOverlay(new GroundOverlayOptions()
                .anchor(0.5f, 0.5f).transparency(0.0f)
                //				 .zIndex(1f)
                .image(BitmapDescriptorFactory.fromBitmap(bitmap))
                .positionFromBounds(bounds));

        String str = (routeOverlay == null) + "";
        Log.d("addRoutyeOverlayToMap", "走着了" + str);
        oldRouteOverlay = routeOverlay;
    }

    private void downloadOrNav() {
        String str = tvAutoNavi.getText().toString();
        if (str.isEmpty()) {
            return;
        }
        if (str.equals("下载语音离线包")) {
            downBinder.downloadZone(jieShuoList, zone);
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
                // tvMoni.setText("模拟");
                tvAutoNavi.setText("停止导览");
                playBinder.pause(1);
                playBinder.pause(2);
                if (autoGuideListener != null) {
                    autoGuideListener.onAutoGuide();
                }
                playBinder.playTishi("start_nav.mp3", 0);
                jieshuo1 = null;
                if (zone.getType().equals("0")) {
                    //					initWeiLan();//测试用


                    startBluetooth();
                } else if (zone.getType().equals("1")) {
                    initWeiLan();
                } else {
                    initWeiLan();
                    startBluetooth();
                }
            }
            isAutoGuide = true;
        } else if (str.equals("停止导览")) {
            jieshuo1 = null;
            tvAutoNavi.setText("自动导览");

            if (zone.getType().equals("0")) {
				/*if (mAMapGeoFence!=null) {
					mAMapGeoFence.removeAll();//测试
					mAMapGeoFence =null;
				}*/
                playBinder.seekTo(0);
                playBinder.pause(1);
                Intent intent = new Intent(Constance.JIESHUO_PLAY_END);
                context.sendBroadcast(intent);
                SystemManger.yuying = "";


                stopBluetooth();
            } else if (zone.getType().equals("1")) {
                if (mAMapGeoFence != null) {
                    mAMapGeoFence.removeAll();
                    mAMapGeoFence = null;
                }
            } else {
                if (mAMapGeoFence != null) {
                    mAMapGeoFence.removeAll();
                    mAMapGeoFence = null;
                }
                stopBluetooth();
            }
            isAutoGuide = false;
        }
    }

    private AMapGeoFence mAMapGeoFence;

    private void initWeiLan() {
        List<DPoint> mDPoints = new ArrayList<DPoint>();
        for (Jieshuo jieshuo : jieShuoList) {
            DPoint dPoint = new DPoint(Double.valueOf(df.format(jieshuo.getLatitude())), Double.valueOf(df.format(jieshuo.getLongitude())));
            mDPoints.add(dPoint);
        }
        mAMapGeoFence = new AMapGeoFence(this, amap, handlerWL, mDPoints, jdOverlay, jieShuoList);

    }

    private void startBluetooth() {
        Intent intent;
        if (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)
                && Build.VERSION.SDK_INT >= 18) {
            intent = new Intent(this, BluetoothServiceImp4.class);
        } else {
            intent = new Intent(this, BluetoothServiceImp2.class);
        }
        intent.setPackage(getPackageName());
        startService(intent);
    }

    private boolean isAutoGuide = false;

    private void stopBluetooth() {
        Intent intent;
        if (isAutoGuide) {
            SystemManger.yuying = "";
            if (getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_BLUETOOTH_LE)
                    && Build.VERSION.SDK_INT >= 18) {
                intent = new Intent(this, BluetoothServiceImp4.class);
            } else {
                intent = new Intent(this, BluetoothServiceImp2.class);
            }
            intent.setPackage(getPackageName());
            stopService(intent);
            if (playBinder != null) {
                playBinder.pause(1);
                playBinder.pause(2);
            }
            if (autoGuideListener != null) {
                autoGuideListener.onAutoGuide();
            }
        }
    }


    // 将之前被点击的marker置为原来的状态
    public void resetlastmarker() {
        int index = searchListener.poiOverlay.getPoiIndex(mlastMarker);
        if (index < 10) {
            mlastMarker.setIcon(BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(getResources(),
                            markers[index])));
        } else {
            mlastMarker.setIcon(BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(getResources(),
                            R.drawable.marker_other_highlight)));
        }
        mlastMarker = null;

    }

    /**
     * 开始进行poi搜索
     */
    /**
     * 开始进行poi搜索
     *
     * @param
     * @param
     */
    //    protected void doSearchQuery() {
    ////        keyWord = mSearchText.getText().toString().trim();
    //        currentPage = 0;
    //        query = new PoiSearch.Query(keyWord, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
    //        query.setPageSize(20);// 设置每页最多返回多少条poiitem
    //        query.setPageNum(currentPage);// 设置查第一页
    //
    //        if (myLocationSource.myAMapLocationListener.location != null) {
    //            poiSearch = new PoiSearch(this, query);
    //            searchListener = new MyOnPoiSearchListener(query, mlastMarker,
    //                    this, amap);
    //            infoAdapter.searchListener = searchListener;
    //            poiSearch.setOnPoiSearchListener(searchListener);
    //            lp = new LatLonPoint(
    //                    myLocationSource.myAMapLocationListener.location.latitude,
    //                    myLocationSource.myAMapLocationListener.location.longitude);
    //            poiSearch.setBound(new PoiSearch.SearchBound(lp, 5000, true));//
    //            // 设置搜索区域为以lp点为圆心，其周围5000米范围
    //            poiSearch.searchPOIAsyn();// 异步搜索
    //        }
    //    }
    private void clearRoutePoi(View view) {
        // 重新显示兴趣点
        if (myRouteSearchListener.mWalkRouteOverlay != null
                && searchListener.poiOverlay != null) {
            myRouteSearchListener.mWalkRouteOverlay.removeFromMap();
            myRouteSearchListener.mWalkRouteOverlay = null;
            // 并还原点击marker样式
            if (mlastMarker != null) {
                resetlastmarker();
            }
            searchListener.poiOverlay.addToMap();
            return;
        }
        if (searchListener.poiOverlay != null) {
            // 并还原点击marker样式
            if (mlastMarker != null) {
                resetlastmarker();
            }
            searchListener.poiOverlay.removeFromMap();
            searchListener.poiOverlay = null;
            return;
        }
        if (myRouteSearchListener.mWalkRouteOverlay != null) {
            myRouteSearchListener.mWalkRouteOverlay.removeFromMap();
            myRouteSearchListener.mWalkRouteOverlay = null;
            return;
        }
        // ToastUtil.show(this, "请搜索,再规划路线");
        //		MyApplication.showSingleton("没有路线可以清除");
    }

    PopupWindow popupWindow;

    private void showPopupWindow(View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.view_pop_seach, null);

        TextView tvWC = contentView.findViewById(R.id.tv_wc);
        TextView tvExit = contentView.findViewById(R.id.tv_exit);
        tvWC.setOnClickListener(this);
        tvExit.setOnClickListener(this);
        popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        //        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置好参数之后再show
        popupWindow.showAsDropDown(view, view.getWidth(), -200);
    }

    private void goInmyself() {
        if (!isLocation) {
            amap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    myLocationSource.myAMapLocationListener.location, 18));
            isLocation = true;
        } else {
            isLocation = false;
            if (zone.getLatitude().equals("")
                    || null == (zone.getLatitude().trim())
                    || zone.getLongitude().trim().equals("")
                    || null == zone.getLongitude()) {

                amap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                        31.911302110, 117.131477110), 16));// 移动到目标图层
                return;
            } else {
                amap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(Double.valueOf(zone.getLatitude()), Double
                                .valueOf(zone.getLongitude())), 16));// 移动到目标图层
            }

        }

    }


    // 自动解说
    private void playJieShuoAuto(Jieshuo jieshuo2) {
        if (jieshuo2 != null && jieshuo2.getYuyin().equals(SystemManger.yuying))
            return;
        if (playBinder.isPlaying())
            playBinder.stop(1);
        isAutoNavi = true;
        playMethod = 0;
        //        vpChange.setVisibility(View.VISIBLE);
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
                    mAdapter.notifyDataSetChanged();
                    slv.setFlag(jieShuoList.indexOf(jieshuo2));
                    slv.scrollToPosition(jieShuoList.indexOf(jieshuo2));
                    slv.smoothScrollToPosition(jieShuoList.indexOf(jieshuo2));
                    jieshuo3 = jieshuo2;
                    playBinder.playTishi("msg.mp3", 1);
                    SystemManger.yuying = jieshuo2.getYuyin();
                    DBUtil.consumeFree(zone.getId());

                }
            } else {
                if (!SystemManger.isInPayZoneActivity) {
                    slv.scrollToPosition(jieShuoList.indexOf(jieshuo2));
                    slv.smoothScrollToPosition(jieShuoList.indexOf(jieshuo2));
                    AppUtil.showToastMsg(Map2Activity.this, "亲，试用结束了,请激活景点");
                    goActivityPayZone();
                }
            }
        }
    }

    protected void goActivityPayZone() {
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
                            Zone zone = (Zone) JsonUtil.fromJsonToObject(
                                    t.toString(), type);
                            //                            Intent intent = new Intent(context,
                            //                                    ActivityPayZone.class);
                            //                            intent.putExtra("name", zone.getName());
                            //                            intent.putExtra("memo", zone.getMemo());
                            //                            intent.putExtra("money", zone.getPrice() + "");
                            //                            intent.putExtra("id", zone.getId());
                            //                            startActivityForResult(intent, 14);
                        } catch (Exception e) {

                        }
                    }

                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapview.onResume();
        if (myLocationSource.mlocationclient != null) {
            myLocationSource.mlocationclient.startLocation();
        }
    }

    List<RouteJieShuo> list;

    private void initXianluJieshuo(int id) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("xid", id + "");
        mHttp.send(HttpRequest.HttpMethod.POST,
                Constance.HTTP_URL + "tour/jieshuodianlist", params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        //                        MyApplication.show("获取线路解说点失败");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        try {
                            Log.v("123", "routejieshuo:" + arg0.result);
                            JSONObject jsonobj = new JSONObject(arg0.result);
                            JSONArray arr = jsonobj.getJSONArray("jsddetail");
                            list = JsonUtil.getList(arr.toString(),
                                    RouteJieShuo.class);
                            Log.v("123", "routejieshuo size=" + list.size());
                            Log.v("123", "jieshuolist size=" + jieShuoList.size());
                            List<Jieshuo> temp = new ArrayList<Jieshuo>();
                            for (int i = 0; i < list.size(); i++) {
                                for (int j = 0; j < titleJieShuos.size(); j++) {
                                    RouteJieShuo info = list.get(i);
                                    Jieshuo js = titleJieShuos.get(j);
                                    if (info.getName().equals(js.getName()))
                                        temp.add(js);

                                }
                            }
                            for (int i = 0; i < temp.size(); i++) {
                                Log.v("123", "jieshuo name=" + temp.get(i).getName());
                            }
                            Log.v("123", "temp size:" + temp.size());
                            //                            for (int i = 0; i < markerList.size(); i++) {
                            //                                markerList.get(i).remove();
                            //                            }
                            //                            markerList.clear();
                            jieShuoList.clear();
                            jieShuoList.addAll(temp);
                            tag = 2;
                            //                            checkedPosition = 0;
                            btnHf.setVisibility(View.VISIBLE);
                            addRouteMarker(temp);
                            initYuyinView(temp);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


    @Override
    protected void onPause() {
        super.onPause();
        mapview.onPause();
        myLocationSource.deactivate();
        myLocationSource.myAMapLocationListener.mFirstFix = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapview.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        unbindService(downService);
        unbindService(playService);
        // 销毁定位监听
        myLocationSource.myAMapLocationListener = null;
        if (myLocationSource.mlocationclient != null) {
            myLocationSource.mlocationclient.stopLocation();
            myLocationSource.mlocationclient.onDestroy();
        }
        myLocationSource.mlocationclient = null;
        // 销毁方向监听
        if (mSensorHelper != null) {
            mSensorHelper.unRegisterSensorListener();
            mSensorHelper.setCurrentMarker(null);
            mSensorHelper = null;
        }
        myLocationSource.mListener = null;
        myLocationSource.myAMapLocationListener = null;
        myLocationSource = null;
        jdOverlay.destory();
        if (tpm != null)
            tpm.listen(phoneListener, 0);
        // 回收加载的图片
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
                // 回收图片所占的内存
                System.gc(); // 提醒系统及时回收
            }
        }
        //销毁推荐路线
        if (oldRouteOverlay != null) {
            oldRouteOverlay.remove();
            if (oldRouteBitmap != null && !oldRouteBitmap.isRecycled()) {
                oldRouteBitmap.recycle();
                oldRouteBitmap = null;
            }

            isShowRoute = false;
            System.gc(); // 提醒系统及时回收
        }
        if (mapOverlay != null) {
            mapOverlay.remove();
            mapOverlay = null;
        }
        if (overlayMap != null) {
            overlayMap.clear();
            overlayMap = null;
        }


        handler.removeMessages(1);
        handler1.removeMessages(1);

        if (zone.getType().equals("0")) {
			/*if (mAMapGeoFence!=null) {
				mAMapGeoFence.removeAll();//测试
			}*/


            stopBluetooth();
        } else if (zone.getType().equals("1")) {
            if (mAMapGeoFence != null) {
                mAMapGeoFence.removeAll();
            }
        } else {
            if (mAMapGeoFence != null) {
                mAMapGeoFence.removeAll();
            }
            stopBluetooth();
        }
        EventBus.getDefault().unregister(this);
        if (routeList != null && routeList.size() != 0)
            routeList.clear();
        if (jieShuoList != null && jieShuoList.size() != 0)
            jieShuoList.clear();
        if (jdMarks != null && jdMarks.size() != 0)
            jdMarks.clear();


        if (amap != null)
            amap.clear();
        mapview.removeAllViews();

        if (rlParent != null && slv != null) {
            mAdapter.setListener(null);
            slv.setOnItemSelectLitener(null);
            slv.removeAllViews();
            rlParent.removeView(slv);
        }
        super.onDestroy();
        mapview.onDestroy();
    }

    private boolean isResume;
    Jieshuo pauseJieshuo = null;

    @Override
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(MyEvent e) {
        // TODO 来电暂停
        super.onEventMainThread(e);

        if (e.getId() == TelephonyManager.CALL_STATE_RINGING) {
            if (playBinder.isPlaying()) {
                playBinder.pause(1);
                for (Jieshuo j : jieShuoList) {
                    if (j.isListen()) {
                        j.setListen(false);
                        pauseJieshuo = j;

                    }
                }
                if (tag == 1) {
                    if (pauseJieshuo.isSelected()) {
                        markerList.get(jieShuoList.indexOf(pauseJieshuo)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.iv_zone_go));
                    } else {
                        markerList.get(jieShuoList.indexOf(pauseJieshuo)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_js_marker70));
                    }

                } else {
                    if (pauseJieshuo.isSelected()) {
                        markerList.get(jieShuoList.indexOf(pauseJieshuo)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.iv_zone_go));
                    } else {
                        View view = LayoutInflater.from(Map2Activity.this)
                                .inflate(R.layout.route_jieshuo_marker_layout,
                                        null);
                        TextView tv = view
                                .findViewById(R.id.tv_marker_name);
                        tv.setText(jieShuoList.indexOf(pauseJieshuo) + 1 + "");
                        markerList.get(jieShuoList.indexOf(pauseJieshuo)).setIcon(BitmapDescriptorFactory.fromView(view));
                    }
                }

                mAdapter.notifyDataSetChanged();
                //                infoAdapter.ivPlay
                //                        .setImageResource(R.drawable.iv_jieshuo_windown_start);
                //                infoAdapter.tvPlay.setText("试听");
                isResume = true;
            }
        } else if (e.getId() == TelephonyManager.CALL_STATE_IDLE) {
            if (isResume) {
                isResume = false;
                playBinder.resume();
                for (Jieshuo j : jieShuoList) {
                    if (j.getName().equals(playingJieshuo.getName())) {
                        j.setListen(true);
                    }

                }
                markerList.get(jieShuoList.indexOf(pauseJieshuo)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.iv_map_market1));
                mAdapter.notifyDataSetChanged();
                slv.setFlag(jieShuoList.indexOf(pauseJieshuo));
                //                infoAdapter.ivPlay
                //                        .setImageResource(R.drawable.iv_jieshuo_windown_stop);
                //                infoAdapter.tvPlay.setText("暂停");
            }
        } else if (e.getId() == TelephonyManager.CALL_STATE_OFFHOOK) {

        } else if (e.getId() == Integer.MAX_VALUE - 1003) {
            if (!isAutoNavi)

                playBinder.play1(Constance.HTTP_URL + "/upload/" + zone.getId()
                        + "/" + jieshuo3.getYuyin() + ".mp3");
        }

    }

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
                    Map2Activity.this.finish();
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
            Map2Activity.this.finish();
        }

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


    @Override
    public int initView() {
        return R.layout.activity_map3;
    }

    private OnLoadJDListener listener;

    public void setOnLoadJdDataListener(OnLoadJDListener listener) {
        this.listener = listener;
    }

    public interface OnLoadJDListener {
        void onLoadJdData(String string);
    }

    private OnInitPlayBinderOverListener playBinderListener;

    public void setOnInitPlayBinderOverListener(
            OnInitPlayBinderOverListener playBinderListener) {
        this.playBinderListener = playBinderListener;
    }

    public interface OnInitPlayBinderOverListener {
        void onInitPlayBinderOver(PlayService.PlayBinder play);
    }

    private OnAutoGuideListener autoGuideListener;

    public void setOnAutoGuideListener(OnAutoGuideListener autoGuideListener) {
        this.autoGuideListener = autoGuideListener;
    }

    public interface OnAutoGuideListener {
        void onAutoGuide();
    }
}



