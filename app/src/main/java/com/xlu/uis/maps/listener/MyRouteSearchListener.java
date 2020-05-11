package com.xlu.uis.maps.listener;

import com.amap.api.maps.AMap;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.pandacard.teavel.R;
import com.xlu.uis.Map2Activity;


public class MyRouteSearchListener implements OnRouteSearchListener {
	private WalkRouteResult mWalkRouteResult;
	public WalkRouteOverlay mWalkRouteOverlay;
	private Map2Activity activity;
	private AMap amap;
	public MyRouteSearchListener(Map2Activity activity, AMap amap) {
		super();
		this.activity = activity;
		this.amap = amap;
	}

	@Override
	public void onBusRouteSearched(BusRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	

	@Override
	public void onDriveRouteSearched(DriveRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRideRouteSearched(RideRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {

		if(errorCode==1000){
			if(result!=null&&result.getPaths()!=null){
				if(result.getPaths().size()>0){
					mWalkRouteResult=result;
					final WalkPath walkPath=mWalkRouteResult.getPaths().get(0);
					mWalkRouteOverlay=new WalkRouteOverlay(activity, amap, walkPath,mWalkRouteResult.getStartPos(),mWalkRouteResult.getTargetPos());
					mWalkRouteOverlay.addToMap();
					mWalkRouteOverlay.setNodeIconVisibility(false);
					mWalkRouteOverlay.zoomToSpan();
					
				}else{
//					App.showSingleton(activity.getResources().getString(R.string.no_result));
				}
			}else{
//				App.showSingleton(activity.getResources().getString(R.string.no_result));
			}
		}else if(errorCode==3001){
//			App.showSingleton("附近无路");
		}else if(errorCode==3002){
//			App.showLong("路线计算失败");
		}else if(errorCode==3003){
//			App.show("步行距离太长");
		}else{
//			App.show("网络连接异常");
		}
	}

}
