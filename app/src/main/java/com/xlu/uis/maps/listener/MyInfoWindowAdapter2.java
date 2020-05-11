package com.xlu.uis.maps.listener;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.FromAndTo;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pandacard.teavel.R;
import com.pandacard.teavel.apps.MyApplication;
import com.xlu.po.Jieshuo;
import com.xlu.uis.Map2Activity;
import com.xlu.utils.Constance;
import com.xlu.utils.JsonUtil;
import com.xlu.utils.PoiOverlay;
import com.xlu.utils.servers.PlayService;

import java.lang.reflect.Type;
import java.util.List;


public class MyInfoWindowAdapter2 implements InfoWindowAdapter {
	public PoiOverlay jdOverlay;
	private Map2Activity activity;
	TextView tvJDname;
	TextView tvJDdetails;
	public ImageView ivPlay;
	public TextView tvPlay;
	ImageView ivJDimage;
	TextView tvGoHere;
	TextView tVPlay;
	LinearLayout llGoDeatails;
	public MyLocationSource myLocationSource;
	public RouteSearch routeSearch;
	public PlayService.PlayBinder playBinder;
	public List<Jieshuo> jieShuoList;
	public Jieshuo nowJieshuo;
	public MyRouteSearchListener myRouteSearchListener;
	public MyOnPoiSearchListener searchListener;
	private View customWindow;

	public MyInfoWindowAdapter2(Map2Activity activity) {
		this.activity = activity;
		customWindow = LayoutInflater.from(activity).inflate(
				R.layout.view_jd_details_dialog, null, false);
	}
	@Override
	public View getInfoContents(Marker marker) {
		if (!jdOverlay.getMarkerList().contains(marker)) {
			return null;
		}
		
		render(marker, customWindow);
		return customWindow;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		
		return null;
	}

	private void render(final Marker marker, View customWindow) {
		tvJDname = (TextView) customWindow.findViewById(R.id.tv_jd_name);
		tvJDdetails = (TextView) customWindow.findViewById(R.id.tv_jd_details);
		tvGoHere = (TextView) customWindow.findViewById(R.id.tv_go_here);
		tVPlay = (TextView) customWindow.findViewById(R.id.tv_play);
		ivPlay = (ImageView) customWindow.findViewById(R.id.iv_play);
		llGoDeatails = (LinearLayout) customWindow
				.findViewById(R.id.ll_go_details);
		ivJDimage = (ImageView) customWindow.findViewById(R.id.iv_jd_image);
		activity.setOnAutoGuideListener(new Map2Activity.OnAutoGuideListener() {

			@Override
			public void onAutoGuide() {
				marker.hideInfoWindow();
			}
		});
		for (Jieshuo jieShuo : jieShuoList) {
			if (jieShuo.getName().equals(marker.getTitle())) {
				nowJieshuo = jieShuo;
				break;
			}
		}

		tvJDname.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				App.show("关闭");
				marker.hideInfoWindow();
				playBinder.pause(1);
			}
		});

		llGoDeatails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO �򿪾�������
//				App.show("ȥ������");
//				Intent intent = new Intent(activity,
//						ActivityJieshuoDetail.class);
//				Type type = new TypeToken<Jieshuo>() {
//				}.getType();
//				String str = JsonUtil.toJsonString(nowJieshuo, type);
//				intent.putExtra("jieshuostr", str);
//				activity.startActivity(intent);
			}
		});
		tVPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String str = tVPlay.getText().toString();
				if (str.equals("暂停")) {
					tVPlay.setText("试听");
					ivPlay.setImageResource(R.drawable.iv_jieshuo_windown_start);
					// 暂停音乐
					playBinder.pause(1);
				} else {
					tVPlay.setText("暂停");
					ivPlay.setImageResource(R.drawable.iv_jieshuo_windown_stop);
					// ������������
					playBinder.resume();
				}

			}
		});

		tvJDname.setText(marker.getTitle());
		tvJDdetails.setText(marker.getSnippet());
		ImageLoader.getInstance().displayImage(
				Constance.HTTP_URL + nowJieshuo.getPic(), ivJDimage,
				MyApplication.normalOption);

		tvGoHere.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (myLocationSource.myAMapLocationListener.location != null) {
					if (myRouteSearchListener.mWalkRouteOverlay != null) {
						myRouteSearchListener.mWalkRouteOverlay.removeFromMap();
						myRouteSearchListener.mWalkRouteOverlay = null;
					}
					if (searchListener.poiOverlay!= null) {
						searchListener.poiOverlay.removeFromMap();
						searchListener.poiOverlay = null;
					}
					LatLng lat = marker.getOptions().getPosition();
					WalkRouteQuery query = new WalkRouteQuery(
							new FromAndTo(
									new LatLonPoint(
											myLocationSource.myAMapLocationListener.location.latitude,
											myLocationSource.myAMapLocationListener.location.longitude),
									new LatLonPoint(lat.latitude, lat.longitude)),
							RouteSearch.WalkDefault);
					routeSearch.calculateWalkRouteAsyn(query);
				}
			}

		});
	}


}
