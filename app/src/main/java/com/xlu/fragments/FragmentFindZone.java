package com.xlu.fragments;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.google.gson.reflect.TypeToken;
import com.pandacard.teavel.R;
import com.pandacard.teavel.apps.MyApplication;
import com.xlu.po.Merchant1;
import com.xlu.po.MyEvent;
import com.xlu.po.ProductSpecal;
import com.xlu.po.Zone;
import com.xlu.uis.ActivityDinnerDetail;
import com.xlu.uis.ActivitySelectCity;
import com.xlu.uis.ActivityZhuSuDetail;
import com.xlu.uis.ActivityZone2;
import com.xlu.utils.Constance;
import com.xlu.utils.DBUtil;
import com.xlu.utils.JsonUtil;
import com.xlu.utils.MyNewTab;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.com.wideroad.BaseHttp;
import cn.com.wideroad.http.AjaxCallBack;
import cn.com.wideroad.http.AjaxParams;
import cn.com.wideroad.utils.StringUtil;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;


public class FragmentFindZone extends Fragment implements OnClickListener, LocationSource, AMapLocationListener {
    OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;

    public static Fragment newInstance() {
        FragmentFindZone fragment = new FragmentFindZone();
        Bundle args = new Bundle();
//        args.putString("MBananerpic", mTrippic);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    View contentView;
    TextView tvFindSelectCity;
    private MapView mvDaolan;
    private MyNewTab tabZone, tabJiudian, tabMeishi;
    private Context context;
    private List<Zone> listZone = new ArrayList<Zone>();
    private List<Zone> downloadList1 = new ArrayList<Zone>();
    private final int REQUEST_CODE_FIND_ZONE = 78;
    private final int RESPONSE_CODE_FIND_ZONE = 10;
    String[] tags = {"景点", "酒店", "美食"};
    AMap aMap;
    List<Merchant1> datasMeishi;
    List<Merchant1> datasJiudian;
    int type = 0;
    Intent intent = null;
    String city;
    int flag = 0;

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_find_zone, null);
        tabJiudian = (MyNewTab) contentView.findViewById(R.id.mtb_jiudian);
        tabMeishi = (MyNewTab) contentView.findViewById(R.id.mtb_meishi);
        tabZone = (MyNewTab) contentView.findViewById(R.id.mtb_zone);
        tabZone.setChecked(true);
        tabZone.setText("景区");
        tabMeishi.setText("美食");
        tabJiudian.setText("酒店");
        tabJiudian.setOnClickListener(this);
        tabZone.setOnClickListener(this);
        tabMeishi.setOnClickListener(this);
        context = getActivity();
//        if(MyApplication.isNeedPer){
//            requestRunTimePermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION"});
//            requestRunTimePermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"});
//        }
        mvDaolan = (MapView) contentView.findViewById(R.id.mv_dao_lan);
        mvDaolan.onCreate(savedInstanceState);
        aMap = mvDaolan.getMap();
        initAmap();
        initLocation();
        loadZoneData();
        flag = 1;
        if (listZone.size() == 0) {
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(MyApplication.weidu, MyApplication.jingdu), 16));

        } else if (listZone != null) {
            Zone zone = listZone.get(0);
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Double.valueOf(zone.getLatitude()), Double
                            .valueOf(zone.getLongitude())), 16));

        }


        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (isClick == false) {
                    isClick = true;
                } else {
                    isClick = false;
                }
                if (isClick) {

                }
                if (type == 0) {
                    for (Zone z : listZone) {
                        if (marker.getTitle().equals(z.getName())) {
                            if (z.getNorthj() == null || z.getNorthj().trim().equals("")
                                    || z.getNorthw() == null
                                    || z.getNorthw().trim().equals("")
                                    || z.getSouthj() == null || z.getSouthw() == null
                                    || z.getSouthj().trim().equals("")
                                    || z.getSouthw().trim().equals("")) {
                                Type type = new TypeToken<Zone>() {
                                }.getType();
                                String str = JsonUtil.toJsonString(z, type);
                                intent = new Intent(context, ActivityZone2.class);
                                intent.putExtra("zonestr", str);
                                intent.putExtra("isDownload", false);
                                context.startActivity(intent);

                            } else {
                                Type type = new TypeToken<Zone>() {
                                }.getType();
                                String str = JsonUtil.toJsonString(z, type);
//                                Intent intent = new Intent(context, Map2Activity.class);
//                                intent.putExtra("zonestr", str);
//                                intent.putExtra("isDownload", false);
//                                context.startActivity(intent, ActivityOptions.makeScaleUpAnimation(contentView, MyApplication.getScreenWidth(), MyApplication.getScreenHeight(), MyApplication.getScreenWidth() / 2, MyApplication.getScreenHeight() / 2).toBundle());
                            }

                        }
                    }
                } else if (type == 1) {
                    for (Merchant1 m1 : datasMeishi) {
                        if (marker.getTitle().equals(m1.getName())) {
                            intent = new Intent(getActivity(), ActivityDinnerDetail.class);
                            intent.putExtra("dinner", m1);
                            startActivity(intent);
                        }
                    }

                } else if (type == 2) {
                    for (Merchant1 m1 : datasJiudian) {
                        if (marker.getTitle().equals(m1.getName())) {
                            intent = new Intent(getActivity(), ActivityZhuSuDetail.class);
                            intent.putExtra("zhusu", m1);
                            startActivity(intent);
                        }
                    }
                }

                return true;
            }
        });

        return contentView;

    }

    private void initAmap() {
        aMap.setLocationSource(this);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);// 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked));
        myLocationStyle.strokeColor(R.color.transparent);
        myLocationStyle.radiusFillColor(R.color.transparent);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

    }

    boolean isClick = false;

    @Override
    public void onPause() {
        super.onPause();
        mvDaolan.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mvDaolan.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mvDaolan.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        mvDaolan.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    private void addMeiShi(List<Merchant1> list) {
        flag = 1;
        aMap.clear();
        mlocationClient.startLocation();
        List<Marker> markers = new ArrayList<Marker>();
        for (int i = 0; i < list.size(); i++) {
            Merchant1 m = list.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.zone_list_marker_layout, null);
            TextView tv = (TextView) view.findViewById(R.id.marker_tv_zone);
            ImageView iv = (ImageView) view.findViewById(R.id.iv_marker_img);
            tv.setText(m.getName());
            Marker marker = aMap.addMarker(new MarkerOptions().title(m.getName()).position(new LatLng(Double.valueOf(m.getWeidu()), Double.valueOf(m.getJindu()))).icon(BitmapDescriptorFactory.fromView(view)));
            markers.add(marker);
        }
        if (datasMeishi.size() == 0) {
//            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                    new LatLng(MyApplication.weidu,MyApplication.jingdu), 12));

        } else if (datasMeishi != null) {
            Merchant1 m = list.get(0);
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Double.valueOf(m.getWeidu()), Double
                            .valueOf(m.getJindu())), 10));

        }
    }

    private void addJiuDian(List<Merchant1> list) {
        flag = 1;
        aMap.clear();
        mlocationClient.startLocation();
        List<Marker> markers = new ArrayList<Marker>();
        for (int i = 0; i < datasJiudian.size(); i++) {
            Merchant1 m = list.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.zone_list_marker_layout, null);
            TextView tv = (TextView) view.findViewById(R.id.marker_tv_zone);
            ImageView iv = (ImageView) view.findViewById(R.id.iv_marker_img);
            tv.setText(m.getName());
            Marker marker = aMap.addMarker(new MarkerOptions().title(m.getName()).position(new LatLng(Double.valueOf(m.getWeidu()), Double.valueOf(m.getJindu()))).icon(BitmapDescriptorFactory.fromView(view)));
            markers.add(marker);
        }
        if (datasJiudian.size() == 0) {
//            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                    new LatLng(MyApplication.weidu,MyApplication.jingdu), 12));

        } else if (datasJiudian != null) {
            Merchant1 m = list.get(0);
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Double.valueOf(m.getWeidu()), Double
                            .valueOf(m.getJindu())), 11));


        }
    }

    private void addZoneMarker(List<Zone> list) {
        flag = 1;
        aMap.clear();
        mlocationClient.startLocation();
        List<Marker> markers = new ArrayList<Marker>();
        for (int i = 0; i < list.size(); i++) {
            Zone zone = list.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.zone_list_marker_layout, null);
            TextView tv = (TextView) view.findViewById(R.id.marker_tv_zone);
            tv.setText(zone.getName());
            Marker marker = aMap.addMarker(new MarkerOptions().title(zone.getName()).position(new LatLng(Double.valueOf(zone.getLatitude()), Double.valueOf(zone.getLongitude()))).icon(BitmapDescriptorFactory.fromView(view)));
            markers.add(marker);
        }
        if (listZone.size() == 0) {
//			aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//					new LatLng(MyApplication.weidu,MyApplication.jingdu), 16));

        } else if (listZone != null) {
            Zone zone = listZone.get(0);
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Double.valueOf(zone.getLatitude()), Double
                            .valueOf(zone.getLongitude())), 10));

        }


    }

    private void loadLocalMeishi() {
        BaseHttp http = new BaseHttp();
        AjaxParams params = new AjaxParams();


        params.put("type", "餐饮");

        params.put("page", 1 + "");
        // params.put("status", status);


        if (DBUtil.getCity(MyApplication.city) != null) {
            params.put("city", DBUtil.getCity(MyApplication.city).getBianhao());
        } else {
            params.put("city", "");
        }
        params.put("weidu", MyApplication.weidu + "");
        params.put("jindu", MyApplication.jingdu + "");

        http.post(Constance.HTTP_REQUEST_URL_BIZ + "merchantlist", params,
                new AjaxCallBack<Object>() {

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
                        super.onFailure(t, errorNo, strMsg);


                    }

                    int i = 0;

                    @Override
                    public void onSuccess(Object t) {
                        // TODO Auto-generated method stub
                        super.onSuccess(t);
                        try {
                            Type types1 = new TypeToken<List<ProductSpecal>>() {

                            }.getType();
                            Type types2 = new TypeToken<List<Merchant1>>() {
                            }.getType();

                            datasMeishi = (List<Merchant1>) JsonUtil.fromJsonToObject(StringUtil.removeNull(t), types2);
                            addMeiShi(datasMeishi);

                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }

                });
    }

    private void loadLocalJiudian() {
        BaseHttp http = new BaseHttp();
        AjaxParams params = new AjaxParams();


        params.put("type", "酒店");

        params.put("page", 1 + "");
        // params.put("status", status);


        if (DBUtil.getCity(MyApplication.city) != null) {
            params.put("city", DBUtil.getCity(MyApplication.city).getBianhao());
        } else {
            params.put("city", "");
        }
        params.put("weidu", MyApplication.weidu + "");
        params.put("jindu", MyApplication.jingdu + "");

        http.post(Constance.HTTP_REQUEST_URL_BIZ + "merchantlist", params,
                new AjaxCallBack<Object>() {

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
                        super.onFailure(t, errorNo, strMsg);


                    }

                    int i = 0;

                    @Override
                    public void onSuccess(Object t) {
                        // TODO Auto-generated method stub
                        super.onSuccess(t);
                        try {
                            Type types1 = new TypeToken<List<ProductSpecal>>() {

                            }.getType();
                            Type types2 = new TypeToken<List<Merchant1>>() {
                            }.getType();

                            datasJiudian = (List<Merchant1>) JsonUtil.fromJsonToObject(StringUtil.removeNull(t), types2);
                            addJiuDian(datasJiudian);

                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }

                });
    }

    private void bycar(Merchant1 merchant) {
//        Intent intent = new Intent(context, ActivitySceneRoute.class);
//        if (merchant != null) {
//            intent.putExtra("name", merchant.getName());
//            String jindu = "118";
//            String weidu = "31";
//            if (merchant.getWeidu() != null) {
//                if (!(merchant.getWeidu().equals(""))) {
//                    jindu = merchant.getJindu();
//                }
//            }
//            if (merchant.getWeidu() != null) {
//                if (!(merchant.getWeidu().equals(""))) {
//                    weidu = merchant.getWeidu();
//                }
//            }
//            intent.putExtra("x", weidu);
//            intent.putExtra("y", jindu);
//
//        }
//        startActivity(intent);
    }

    private void loadZoneData() {
        BaseHttp http = new BaseHttp();
        AjaxParams params = new AjaxParams();
        params.put("sid", MyApplication.deviceId);
        params.put("city", MyApplication.city);
        http.post(Constance.HTTP_REQUEST_URL + "getJingdianByCity", params,
                new AjaxCallBack<Object>() {

                    public void onSuccess(Object t) {
                        try {
                            Log.v("123", "onsuccess--1");
                            if (null != t && !"".equals(t)) {
                                if (!listZone.isEmpty())
                                    listZone.clear();
                                Log.v("123", "onsuccess--2");
                                listZone.addAll((List<Zone>) JsonUtil.getList(t.toString(), Zone.class));
                                Log.v("123", "长度=" + listZone.size());
                                addZoneMarker(listZone);

                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        // TODO Auto-generated method stub
                        super.onFailure(t, errorNo, strMsg);
//                        MyApplication.showSingleton("网络有状况");

                    }
                });

    }


    @Subscribe
    public void onEventMainThread(MyEvent e) {
        if (e.getId() == 1) {
            loadZoneData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_name:
                Intent intent = new Intent(context, ActivitySelectCity.class);
                startActivityForResult(intent, REQUEST_CODE_FIND_ZONE);
                break;
            case R.id.mtb_jiudian:

                tabJiudian.setChecked(true);
                tabZone.setChecked(false);
                tabMeishi.setChecked(false);
                loadLocalJiudian();
                type = 2;

                break;
            case R.id.mtb_meishi:
                tabMeishi.setChecked(true);
                tabJiudian.setChecked(false);
                tabZone.setChecked(false);
                loadLocalMeishi();
                type = 1;
                break;
            case R.id.mtb_zone:
                tabMeishi.setChecked(false);
                tabJiudian.setChecked(false);
                tabZone.setChecked(true);
                loadZoneData();
                type = 0;
                break;

            default:
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FIND_ZONE
                && resultCode == RESPONSE_CODE_FIND_ZONE) {
            tvFindSelectCity.setText(data.getStringExtra("city"));
            MyApplication.city = data.getStringExtra("city");
            aMap.clear();
            mlocationClient.startLocation();
            loadZoneData();
        }
    }


    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            //初始化定位

        }
    }

    private void initLocation() {
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getActivity());
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mLocationOption.setOnceLocation(true);
            mLocationOption.setMockEnable(true);
            mLocationOption.setNeedAddress(false);
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    int permissionsRequestCode = 110;

    /*处理权限问题*/


    public void requestRunTimePermissions(String[] permissions) {
        if (permissions == null || permissions.length == 0)
            return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkPermissionGranted(permissions)) {
            //提示已拥有权限

        } else {
            //申请权限
            requestPermission(permissions, permissionsRequestCode);
        }
    }

    public void requestPermission(final String[] permissions, final int permissionRequestCode) {
        if (shouldShowRequestPermissionRationale(permissions)) {
            new AlertDialog.Builder(FragmentFindZone.this.getActivity()).setTitle(R.string.attention)
                    .setMessage(R.string.content_to_request_permission)
                    .setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(FragmentFindZone.this.getActivity(), permissions, permissionRequestCode);
                        }
                    }).show();
        } else {
            ActivityCompat.requestPermissions(FragmentFindZone.this.getActivity(), permissions, permissionRequestCode);
        }
    }

    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        boolean flag = false;
        for (String p : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(FragmentFindZone.this.getActivity(), p)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public boolean checkPermissionGranted(String[] permissions) {
        boolean result = true;
        for (String p : permissions) {
            if (ActivityCompat.checkSelfPermission(FragmentFindZone.this.getActivity(), p) != PackageManager.PERMISSION_GRANTED) {
                result = false;
                break;
            }
        }
        return result;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": "
                        + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
            dataHandler.sendEmptyMessageDelayed(1, 800);


        }

    }

    private Handler dataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (flag == 1) {
                if (listZone.size() == 0) {
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(MyApplication.weidu, MyApplication.jingdu), 16));

                } else if (listZone != null) {
                    Zone zone = listZone.get(0);
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(Double.valueOf(zone.getLatitude()), Double
                                    .valueOf(zone.getLongitude())), 10));

                }
            }
            flag = 0;
        }
    };


}
