package com.xlu.uis.maps.listener;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.Query;
import com.xlu.uis.Map2Activity;

import java.util.List;


public class MyOnPoiSearchListener implements OnPoiSearchListener {
	private AMap amap;
	private Map2Activity activity;
	private Marker mLastMarker;
	private Query query;
	private PoiResult poiResult;
	private List<PoiItem> poiItems;//pOi
	public myPoiOverlay poiOverlay;// poi

	
	public MyOnPoiSearchListener() {
	}
	

	public MyOnPoiSearchListener(Query query, Marker mlastMarker,
                                 Map2Activity activity, AMap amap) {
		super();
		this.amap = amap;
		this.activity = activity;
		this.mLastMarker = mlastMarker;
		this.query = query;
	}


	@Override
	public void onPoiItemSearched(PoiItem arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPoiSearched(PoiResult result, int code) {

		if(code==1000){
			if(result!=null&&result.getQuery()!=null){//返回结果为空
				if(result.getQuery().equals(query)){//
					poiItems=result.getPois();//
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();
					if(poiItems!=null&&poiItems.size()!=0){
						if(mLastMarker!=null){
							activity.resetlastmarker();
						}
						if(poiOverlay!=null){
							poiOverlay.removeFromMap();
						}
						poiOverlay=new myPoiOverlay(amap,poiItems, activity);
						poiOverlay.addToMap();
						poiOverlay.zoomToSpan();
					}else if(suggestionCities!=null&&suggestionCities.size()!=0){
						showSuggestCity(suggestionCities);
					}else{
//						App.show("没有搜索到相关内容");
					}


				}
			}else{
//				App.show("没有搜索到相关内容");
			}

		}else{
//			App.showSingleton("访问网络失败");
		}
	}
	

	/**
	 * poi没有搜索到数据，返回一些推荐城市的信息
	 */
	private void showSuggestCity(List<SuggestionCity> cities) {
		String infomation = "推荐城市\n";
		for (int i = 0; i < cities.size(); i++) {
			infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
					+ cities.get(i).getCityCode() + "城市编码:"
					+ cities.get(i).getAdCode() + "\n";
		}
//		App.show(infomation);

	}

}
