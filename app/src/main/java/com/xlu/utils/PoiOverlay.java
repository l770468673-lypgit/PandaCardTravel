package com.xlu.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

public class PoiOverlay {
	private List<PoiItem> mPois;
	private AMap mAMap;
	private ArrayList<Marker> mPoiMarkers=new ArrayList<Marker>();
	private Context context;
	private Type type= Type.normal;
	private MarkerOptions markerOptions;
	private BitmapDescriptor normalBitmapDecriptor;
	private CameraUpdateFactory cameraUpdateFactory;
	LatLngBounds.Builder b;
	public enum Type{
		pub,normal,route
    }

	public void setType(Type type) {
		this.type = type;
	}

	public ArrayList<Marker> getMarkerList(){
		return mPoiMarkers;
	}

	/**
	 *
	 * @param mPois
	 * @param amap
	 */
	public PoiOverlay(List<PoiItem> mPois, AMap amap, Context context) {
		super();
		this.mPois = mPois;
		this.mAMap = amap;
		this.context=context;
		normalBitmapDecriptor= BitmapDescriptorFactory.fromResource(R.drawable.map_js_marker70);
		markerOptions=new MarkerOptions();
		cameraUpdateFactory=new CameraUpdateFactory();
		b= LatLngBounds.builder();
	}

	private MarkerOptions getMarkerOptins(int index, Type type){
		if(type== Type.normal){
			return markerOptions.position(new LatLng(mPois.get(index).getLatLonPoint().getLatitude(), mPois.get(index).getLatLonPoint().getLongitude())).title(getTitle(index)).snippet(getSnippet(index)).icon(normalBitmapDecriptor);
		}else if(type== Type.route){
			return markerOptions.position(new LatLng(mPois.get(index).getLatLonPoint().getLatitude(), mPois.get(index).getLatLonPoint().getLongitude())).title(getTitle(index)).snippet(getSnippet(index)).icon(getBitmapDescriptor(index,type));
		}else if(type== Type.pub){
			return null;
		}
		return null;
	}

	/**
	 * 添加Marker到地图中去
	 */
	public void addToMap(){
		try{
			if(mPoiMarkers.size()!=0)
			mPoiMarkers.clear();
			for(int i=0;i<mPois.size();i++){
				Marker marker=mAMap.addMarker(getMarkerOptins(i, Type.normal));
				marker.setObject(i);
				mPoiMarkers.add(marker);
			}
		}catch(Throwable e){
			e.printStackTrace();
		}
		
	}
	public void addSpToMap(){
		try{
			if(mPoiMarkers.size()!=0)
			mPoiMarkers.clear();
			for(int i=0;i<mPois.size();i++){
				Marker marker=mAMap.addMarker(getMarkerOptins(i, Type.route));
				marker.setObject(i);
				mPoiMarkers.add(marker);
			}

		}catch (Throwable e){
			e.printStackTrace();
		}
	}
	/**
	 * 去掉PoiOverlay上所有的Marker
	 */
	public void removeFromMap(){
//		for(Marker marker:mPoiMarkers){
//			marker.remove();
//		}
		if (null != mPoiMarkers&&mPoiMarkers.size()!=0){
			for(Marker marker:mPoiMarkers){
				marker.remove();
			}
			mPoiMarkers.clear();
			mPoiMarkers = null;
		}
		if(mPois!=null&&mPois.size()!=0)
			mPois.clear();
		if(v!=null)
			v=null;


	}
	/**
	 * 隐藏marker点
	 */
	public void hideMarker(){
		for(Marker marker:mPoiMarkers){
			marker.setVisible(false);
		}
	}
	/**
	 * 显示marker点
	 */
	public void showMarker(){
		for(Marker marker:mPoiMarkers){
			marker.setVisible(true);
		}
	}
	public void zoomToSpan(){
		try{
			if(mPois!=null&&mPois.size()>0){
				if(mAMap==null){
					return;
				}
				if(mPois.size()==1){
					mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mPois.get(0).getLatLonPoint().getLatitude(),mPois.get(0).getLatLonPoint().getLongitude()), 18f));
				}else{
					LatLngBounds bounds=getLatLngBounds();
					mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
				}
			}
			
		}catch (Throwable e){
			e.printStackTrace();
		}
	}
	private LatLngBounds getLatLngBounds() {

		for(int i=0;i<mPois.size();i++){
			b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),mPois.get(i).getLatLonPoint().getLongitude()));
		}
		
		return b.build();
	}
	/**
	 * 
	 * @param index
	 * @return marker的标题
	 */
	private String getTitle(int index){
		return mPois.get(index).getTitle();
	}
	private String getSnippet(int index){
		return mPois.get(index).getSnippet();
	}
	View v;
	/**
	 * 
	 * @param index
	 * @return 对应Marker 的BitmapDescriptor
	 */
	protected BitmapDescriptor getBitmapDescriptor(int index, Type type){
		if(type== Type.route){
			if(v==null)
                v= LayoutInflater.from(context).inflate(
                    R.layout.route_jieshuo_marker_layout, null);
            TextView tv = v.findViewById(R.id.tv_marker_name);
			tv.setText((index + 1) + "");
			return BitmapDescriptorFactory.fromView(v);
		}
		return null;

	}
	/**
	 * 
	 * @param marker
	 * @return 该marker对应的poi在list的位置
	 */
	public int getPoiIndex(Marker marker){
		for(int i=0;i<mPoiMarkers.size();i++){
			if(mPoiMarkers.get(i).equals(marker)){
				return i;
			}
		}
		return -1;
	}
	/**
	 * 
	 * @param index
	 * @return 返回对应条目的poi 信息
	 */
	public PoiItem getPoiItem(int index){
		if(index<0||index>=mPois.size()){
			return null;
		}
		return mPois.get(index);
	}
	
	/**
     * @param index 该Marker在地图中的索引
     */
    public void showMarkerInfoWindow(int index) {
        Marker marker = mPoiMarkers.get(index);
        if (!marker.isInfoWindowShown()) {
            marker.showInfoWindow();
        }
    }

    /**
     * @param index 该Marker在地图中的索引
     */
//    int i = 0;
    public void hideMarkerInfoWindow(int index) {
//        Log.d("AMapGeoFence",mPoiMarks.size()+"");
        Marker marker = mPoiMarkers.get(index);
        if (marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
        }
    }
    public Bitmap convertViewToBitmap(View view){
    	view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    	view.layout(0,0,view.getMeasuredWidth(),view.getMeasuredHeight());
    	view.buildDrawingCache();
    	Bitmap bitmap=view.getDrawingCache();
    	return bitmap;
    }
    public void destory(){
		removeFromMap();
		if(normalBitmapDecriptor!=null){
			normalBitmapDecriptor.recycle();
		}
		if(mAMap!=null){
			mAMap.clear();
			mAMap=null;
		}
		if(markerOptions!=null){
			markerOptions=null;
		}

	}

}
