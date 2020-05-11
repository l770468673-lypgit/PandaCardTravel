package com.xlu.uis.maps.listener;

import android.graphics.BitmapFactory;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.PoiItem;
import com.pandacard.teavel.R;
import com.xlu.uis.Map2Activity;

import java.util.ArrayList;
import java.util.List;



public class myPoiOverlay {
	private AMap mAmap;
	private List<PoiItem> mPois;
	private Map2Activity activity;
	private ArrayList<Marker> mPoiMarks=new ArrayList<Marker>();
	public myPoiOverlay(AMap mAmap, List<PoiItem> mPois, Map2Activity activity) {
		super();
		this.mAmap = mAmap;
		this.mPois = mPois;
		this.activity = activity;
	}


    /**
	 * 添加Marker到地图上
	 */
	public void addToMap(){
		for(int i=0;i<mPois.size();i++){
			Marker marker=mAmap.addMarker(getMarkerOptions(i));
			PoiItem item=mPois.get(i);
			marker.setObject(item);
			mPoiMarks.add(marker);
		}
	}
	/**
	 * 删除marker
	 */
	public void removeFromMap(){
		if (null != mPoiMarks){
			for(Marker marker:mPoiMarks){
				marker.remove();
			}
			mPoiMarks.clear();
			mPoiMarks = null;
		}
	}
	/**
	 * 缩放到
	 */
	public void zoomToSpan(){
		if(mPois!=null&&mPois.size()>0){
			if(mAmap==null)
				return;
			LatLngBounds bounds=getLatLngBounds();
			mAmap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
		}
		
	}
	private LatLngBounds getLatLngBounds() {
		LatLngBounds.Builder b= LatLngBounds.builder();
		for(int i=0;i<mPois.size();i++){
			b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),mPois.get(i).getLatLonPoint().getLongitude()));
		}
		return b.build();
	}
	/**
	 * 
	 * @param index
	 * @return 返回对应mPois的index项 MarkerOptions
	 */
	private MarkerOptions getMarkerOptions(int index){
		return new MarkerOptions().position(new LatLng(mPois.get(index).getLatLonPoint().getLatitude(),mPois.get(index).getLatLonPoint().getLongitude()))
				.title(getTitle(index)).icon(getBitmapDesriptor(index));
	}
	/**
	 * @param index
	 * @return 返回mPois的index项的对应BitmapDescriptor
	 */
	private BitmapDescriptor getBitmapDesriptor(int index) {
		if(index<10){
			BitmapDescriptor icon= BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(activity.getResources(),activity.markers[index]));
			return icon;
		}else{
		 BitmapDescriptor icon= BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.marker_other_highlight));
		 return icon;
		}
	}
	private String getTitle(int index) {
		// TODO Auto-generated method stub
		return mPois.get(index).getTitle();
	}
	/**
	 * 
	 * @param marker
	 * @return marker在mPoiMarks的序号
	 */
	public int getPoiIndex(Marker marker){
		for(int i=0;i<mPoiMarks.size();i++){
			if(mPoiMarks.get(i).equals(marker)){
				return i;
			}
		}
		return -1;
	}
	

}
