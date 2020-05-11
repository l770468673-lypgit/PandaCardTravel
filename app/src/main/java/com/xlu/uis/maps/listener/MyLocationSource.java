package com.xlu.uis.maps.listener;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.xlu.uis.Map2Activity;
import com.xlu.utils.SensorEventHelper;


public class MyLocationSource implements LocationSource {

	public OnLocationChangedListener mListener;
	public AMapLocationClient mlocationclient;
	private Map2Activity activity;
	AMapLocationClientOption mLocationOption;
	SensorEventHelper mSensorHelper;
	private AMap amap;
	public MyAMapLocationListener myAMapLocationListener;
	
	
	
	public MyLocationSource(Map2Activity activity,
			SensorEventHelper mSensorHelper, AMap amap) {
		super();
		this.activity = activity;
		this.mSensorHelper = mSensorHelper;
		this.amap = amap;
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener=listener;
		if(mlocationclient==null){
			mlocationclient=new AMapLocationClient(activity);
			mLocationOption=new AMapLocationClientOption();
			mLocationOption = new AMapLocationClientOption();
			//设置定位监听
			myAMapLocationListener = new MyAMapLocationListener(activity,amap,mSensorHelper,this);
			mlocationclient.setLocationListener(myAMapLocationListener);
			//设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			//设置定位参数
			mlocationclient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//			mlocationClient.startLocation();
			System.out.println("激活定位");
		}
	}

	@Override
	public void deactivate() {
		if (mlocationclient != null) {
			mlocationclient.stopLocation();
		}
	}

}
