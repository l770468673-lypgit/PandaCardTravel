package com.xlu.uis.maps.listener;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.pandacard.teavel.R;
import com.xlu.uis.Map2Activity;
import com.xlu.utils.SensorEventHelper;


public class MyAMapLocationListener implements AMapLocationListener {

	public boolean isGaoDe = true;
	private Map2Activity activity;
	public LatLng location = null;
	public boolean mFirstFix = false;
	private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
	private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
	private Circle mCircle;
	private AMap amap;
	private Marker mLocMarker;
	private SensorEventHelper mSensorHelper;
	public static final String LOCATION_MARKER_FLAG = "mylocation";
	private MyLocationSource locationSource;
	
	public MyAMapLocationListener(Map2Activity activity, AMap amap, SensorEventHelper mSensorHelper, MyLocationSource locationSource){
		this.activity = activity;
		this.amap = amap;
		this.mSensorHelper = mSensorHelper;
		this.locationSource = locationSource;
	}
	
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (isGaoDe) {
			if (locationSource.mListener != null && amapLocation != null) {
				if (amapLocation != null
						&& amapLocation.getErrorCode() == 0) {
					activity.mLocationErrText.setVisibility(View.GONE);
					location = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
					if (!mFirstFix) {
						mFirstFix = true;
						addCircle(location, amapLocation.getAccuracy());//��Ӷ�λ����Բ
						addMarker(location);//��Ӷ�λͼ��
						mSensorHelper.setCurrentMarker(mLocMarker);//��λͼ����ת
					} else {
						mCircle.setCenter(location);
						mCircle.setRadius(amapLocation.getAccuracy());
						mLocMarker.setPosition(location);
					}
					System.out.println("高德定位成功");
				} else {
					String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
					Log.e("AmapErr",errText);
					activity.mLocationErrText.setVisibility(View.VISIBLE);
					activity.mLocationErrText.setText("定位失败,请打开网络连接或GPS定位");
					
				}
			}
		}else{
			if (!mFirstFix) {
				mFirstFix = true;
				addCircle(location, amapLocation.getAccuracy());//��Ӷ�λ����Բ
				addMarker(location);//��Ӷ�λͼ��
				mSensorHelper.setCurrentMarker(mLocMarker);//��λͼ����ת
			} else {
				mCircle.setCenter(location);
				mCircle.setRadius(amapLocation.getAccuracy());
				mLocMarker.setPosition(location);
			}
			activity.mLocationErrText.setVisibility(View.GONE);
			System.out.println("蓝牙定位成功");
		}
		
	}
	
	private void addMarker(LatLng latlng) {
		if (mLocMarker != null) {
			return;
		}
		/*Bitmap bMap = BitmapFactory.decodeResource(activity.getResources(),
				R.drawable.zone_map_marker);
		BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);*/
		
		BitmapDescriptor des = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
		MarkerOptions options = new MarkerOptions();
		options.icon(des);
		options.anchor(0.5f, 0.5f);
		options.position(latlng);
		mLocMarker = amap.addMarker(options);
		mLocMarker.setTitle(LOCATION_MARKER_FLAG);
	}
	
	private void addCircle(LatLng latlng, double radius) {
		CircleOptions options = new CircleOptions();
		options.strokeWidth(1f);
		options.fillColor(FILL_COLOR);
		options.strokeColor(STROKE_COLOR);
		options.center(latlng);
		options.radius(radius);
		mCircle = amap.addCircle(options);
	}

}
