package com.xlu.uis.maps.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.location.DPoint;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.pandacard.teavel.R;
import com.xlu.po.Jieshuo;
import com.xlu.utils.PoiOverlay;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by ligen on 16/12/15.
 */
public class AMapGeoFence implements GeoFenceListener {
    private final List<DPoint> dPointList;
    private final PoiOverlay Overlay;
    private String TAG = "AMapGeoFence";
    private GeoFenceClient mClientAllAction;
    private Context mContext;
    private AMap mAMap;
    private ConcurrentMap mCustomEntitys;
    private Handler mHandler;
 // 记录已经添加成功的围栏
    private volatile ConcurrentMap<String, GeoFence> fenceMap                  = new ConcurrentHashMap<String, GeoFence>();
private List<Jieshuo> jieShuoList;
    // 地理围栏的广播action
    static final String GEOFENCE_BROADCAST_ACTION = "com.amap.geofence";


    public AMapGeoFence(Context context, AMap amap, Handler handler, List<DPoint> dPointList, PoiOverlay Overlay, List<Jieshuo> jieShuoList) {
        mContext = context;
        mHandler = handler;
        this.dPointList = dPointList;
        this.Overlay = Overlay;
        this.jieShuoList = jieShuoList;
        mCustomEntitys = new ConcurrentHashMap<String, Object>();
        mAMap = amap;
        IntentFilter fliter = new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION);
        fliter.addAction(GEOFENCE_BROADCAST_ACTION);
        mContext.registerReceiver(mGeoFenceReceiver, fliter);
        addFenceAll();
    }

    private void addFenceAll() {
        mClientAllAction = new GeoFenceClient(mContext);
        mClientAllAction.createPendingIntent(GEOFENCE_BROADCAST_ACTION);
        mClientAllAction.setGeoFenceListener(this);
        mClientAllAction.setActivateAction(GeoFenceClient.GEOFENCE_IN | GeoFenceClient.GEOFENCE_STAYED | GeoFenceClient.GEOFENCE_OUT);

        addCircleGeoFence(dPointList);
    }

    private void addCircleGeoFence(List<DPoint> dPointList) {
    	for (int i = 0; i < dPointList.size(); i++) {
    		DPoint dPoint = dPointList.get(i);
    		mClientAllAction.addGeoFence(dPoint, 5f, jieShuoList.get(i).getId()+"");
		}
    }

    @Override
    public void onGeoFenceCreateFinished(List<GeoFence> geoFenceList, int errorCode, String s) {
        if (errorCode == GeoFence.ADDGEOFENCE_SUCCESS) {
            for (GeoFence fence : geoFenceList) {
                Log.e(TAG, "fenid:" + fence.getFenceId() + " customID:" + s + " " + fenceMap.containsKey(fence.getFenceId()));
                fenceMap.putIfAbsent(fence.getFenceId(), fence);
            }
            Log.e(TAG, "回调添加成功个数:" + geoFenceList.size());
            Log.e(TAG, "回调添加围栏个数:" + fenceMap.size());
            Message message = mHandler.obtainMessage();
            message.obj = geoFenceList;
            message.what = 0;
            mHandler.sendMessage(message);
            Log.e(TAG, "添加围栏成功！！");
        } else {
            Log.e(TAG, "添加围栏失败！！！！ errorCode: " + errorCode);
            Message msg = Message.obtain();
            msg.arg1 = errorCode;
            msg.what = 1;
            mHandler.sendMessage(msg);
        }
    }

    private void drawCircle(GeoFence fence) {
        CircleOptions option = new CircleOptions();
        option.fillColor(mContext.getResources().getColor(R.color.circle_fill));
        option.strokeColor(mContext.getResources().getColor(R.color.circle_stroke));
        option.strokeWidth(4);
        option.radius(fence.getRadius());
        DPoint dPoint = fence.getCenter();
        option.center(new LatLng(dPoint.getLatitude(), dPoint.getLongitude()));
        Circle circle = mAMap.addCircle(option);
        mCustomEntitys.put(fence.getFenceId(), circle);
    }

    public void drawFenceToMap() {
        Iterator iter = fenceMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            GeoFence val = (GeoFence) entry.getValue();
            if (!mCustomEntitys.containsKey(key)) {
                Log.d("LG", "添加围栏:" + key);
                drawFence(val);
            }
        }
    }

    private void drawFence(GeoFence fence) {
        switch (fence.getType()) {
            case GeoFence.TYPE_ROUND:
                drawCircle(fence);
                break;
            default:
                break;
        }
    }

    public void removeAll() {
        try {
            mClientAllAction.removeGeoFence();
            mContext.unregisterReceiver(mGeoFenceReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	// 接收广播
            if (intent.getAction().equals(GEOFENCE_BROADCAST_ACTION)) {
                Bundle bundle = intent.getExtras();
                String fenceID = bundle
                        .getString(GeoFence.BUNDLE_KEY_FENCEID);
                int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
                int code = bundle.getInt(GeoFence.BUNDLE_KEY_LOCERRORCODE);
                String customId = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
                StringBuffer sb = new StringBuffer();
                switch (status) {
                    case GeoFence.STATUS_LOCFAIL:
//                        sb.append("定位失败");
                        Log.e(TAG, "定位失败" + code);
                        break;
                    case GeoFence.STATUS_IN:
//                        sb.append("进入围栏 ").append(fenceID).append("自定义ID:"+customId);
//                        Overlay.showMarkerInfoWindow(Integer.parseInt(customId));
                    	sb.append(customId);
                        Log.e(TAG, "进入围栏 " + fenceID+"业务ID"+customId);
                        break;
                    case GeoFence.STATUS_OUT:
//                        sb.append("离开围栏 ").append(fenceID).append("自定义ID:"+customId);
//                        Overlay.hideMarkerInfoWindow(Integer.parseInt(customId));
//                    	sb.append(customId);
                        Log.e(TAG, "离开围栏 " + fenceID+"业务ID"+customId);
                        break;
                    case GeoFence.STATUS_STAYED:
//                        sb.append("停留在围栏内 ").append(fenceID).append("自定义ID:"+customId);
                    	sb.append(customId);
                        break;
                    default:
                        break;
                }
                
                String str = sb.toString();
                if(!str.equals("")){
                	Message msg = Message.obtain();
                    msg.obj = str;
                    msg.what = 2;
                    mHandler.sendMessage(msg);
                }
                
            }
        }
    };
}
