package com.xlu.utils;

import android.content.Context;
import android.content.Intent;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.pandacard.teavel.apps.MyApplication;
import com.xlu.po.Address;
import com.xlu.po.BlueToothInfo;
import com.xlu.po.City;
import com.xlu.po.CookieInfo;
import com.xlu.po.Jieshuo;
import com.xlu.po.Member;
import com.xlu.po.Payment;
import com.xlu.po.RouteInfos;
import com.xlu.po.XiangQu;
import com.xlu.po.Zone;
import com.xlu.sys.SystemManger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class DBUtil {
	static LiteOrm liteOrm = MyApplication.getLiteOrm();

	public static boolean zoneHaveDownload(Context context) {
		if(liteOrm!=null){
			QueryBuilder qb = new QueryBuilder(Zone.class).whereEquals(
					"isDownload", true);
			List<Zone> list = liteOrm.<Zone> query(qb);
			if (list.size() == 0) {
				return false;
			}
			return true;
		}else{
			return false;
		}

	}


	public static List<Zone> getZoneHaveDownload(Context context) {
		if(liteOrm!=null){
			List<Zone> list = liteOrm.query(Zone.class);
			return list;
		}else{
			List<Zone> list=new ArrayList<Zone>();
			return list;
		}

	}
	public static void saveCookie(CookieInfo cookie){
		if(liteOrm!=null)
		liteOrm.save(cookie);
	}
	public static List<CookieInfo> getCookie(){
		if(liteOrm!=null){
			ArrayList<CookieInfo> list=liteOrm.query(CookieInfo.class);
			return list;
		}else{
			List<CookieInfo> list=new ArrayList<CookieInfo>();
			return list;
		}

	}


	public static boolean zoneHaveDownload( int zoneId) {
		if(liteOrm!=null){
			QueryBuilder qb = new QueryBuilder(Zone.class)
					.whereEquals("id", zoneId);
			List<Zone> list = liteOrm.<Zone> query(qb);
			if (list.size() == 0) {
				return false;
			}
			return true;
		}else{
			return false;
		}

	}

	public static Jieshuo getJieshuo(Context context, String address) {
		if(liteOrm!=null){
			QueryBuilder qb = new QueryBuilder(Jieshuo.class).whereEquals("mac",
					address);
			List<Jieshuo> list = liteOrm.<Jieshuo> query(qb);
			if (list.size() > 0) {
				Jieshuo jieshuo = list.get(0);
				return jieshuo;
			}
			return null;
		}else{
			return null;
		}

	}

	public static boolean zoneIsPay( int zoneId) {
		if(liteOrm!=null){
			QueryBuilder qb = new QueryBuilder(Payment.class).whereEquals("id",
					zoneId);
			List<Payment> list = liteOrm.<Payment> query(qb);
			if (list.size() != 0){
				Payment payment = list.get(0);
				return payment.getIsActive();
			}
			else {
				return false;
			}
		}else{
			return false;
		}


	}

	public static boolean freeIsEnd(int id,String name) {
		if(liteOrm!=null){

		}else{

		}
		QueryBuilder qb = new QueryBuilder(Payment.class)
				.whereEquals("id", id);
		List<Payment> list = liteOrm.<Payment> query(qb);
		if (list.size() == 0){
			Payment payment = new Payment(name,id, SystemManger.freeTimes,false);
			liteOrm.save(payment);
			return false;
		}
		else if(list.get(0).getTimes() > 0){
			return false;
		}
		else {
			return true;
		}
	}

	public static void consumeFree(int id) {
		QueryBuilder qb = new QueryBuilder(Payment.class).whereEquals("id",
				id);
		List<Payment> list = liteOrm.<Payment> query(qb);
		Payment payment = list.get(0);
		if(payment.getTimes() != 0){
			payment.setTimes(payment.getTimes() - 1);
			liteOrm.update(list);
		}
	}

	public static List<Jieshuo> getJieshuoList(int zoneId) {
		QueryBuilder qb = new QueryBuilder(Jieshuo.class)
				.whereEquals("jid", zoneId)/*.whereAppendAnd()
				.whereEquals("active", 1).whereNoEquals("quanju", 0)*/;
		return liteOrm.<Jieshuo> query(qb);
	}

	public static void setPamenet(Integer id,String name) {
		// TODO Auto-generated method stub
		QueryBuilder qb = new QueryBuilder(Payment.class).whereEquals("id",
				id);
		List<Payment> list = liteOrm.<Payment> query(qb);
		if(list.size() == 0)
			liteOrm.save(new Payment(name,id,SystemManger.freeTimes,true));
		else {
			Payment payment = list.get(0);
			payment.setIsActive(true);
			liteOrm.update(list);
		}

	}

	public static Member getLoginMeber() {
		// TODO Auto-generated method stub
		List<Member> listMembers = liteOrm.query(Member.class);
		if (listMembers.size() != 0)
			return listMembers.get(0);
		else {
			return null;
		}
	}
	public static void saveZoneInfo(List<Zone> zoneList){
			liteOrm.save(zoneList);
	}


	public static void savaZone(Context context, List<Jieshuo> jieshuos, Zone zone) {
		// TODO Auto-generated method stub
		liteOrm.save(jieshuos);
		QueryBuilder qb = new QueryBuilder(Zone.class).whereEquals("id",
				zone.getId());
		List<Zone> zoneList = liteOrm.<Zone> query(qb);
		if (zoneList.size() == 0) {
			liteOrm.save(zone);
			Intent intent = new Intent(Constance.DOWNLOAD_DONE);
			liteOrm.save(jieshuos);
			context.sendBroadcast(intent);
		}
	}


	public static void quit(Context context, Member member) {
		// TODO Auto-generated method stub
		liteOrm.delete(Member.class);
	}
	public static void saveBuyAdd(Address add){
		liteOrm.save(add);
	}
	public static void updateBuyAdd(Address add){
		liteOrm.delete(Address.class);
		liteOrm.save(add);
	}
	public static Address getBuyAdd(String jym){
		QueryBuilder qb = new QueryBuilder(Address.class).whereEquals("jym",
				jym);
		List<Address> list=liteOrm.query(qb);
		if(list!=null&&list.size()!=0){
			return list.get(0);
		}
		return null;
	}

	public static void updateMeber(Member member) {
		// TODO Auto-generated method stub
		liteOrm.delete(Member.class);
		liteOrm.save(member);
	}

	public static void addXiangQu(XiangQu xq) {
		liteOrm.save(xq);
	}

	public static void removeXiangQu(XiangQu xq) {
		liteOrm.delete(xq);
	}

	public static List<XiangQu> getXiangQu() {
		return liteOrm.query(XiangQu.class);
	}

	public static void deleteZone(Zone zone, Context context) {
		// TODO Auto-generated method stub
		int pos = zone.getMapsrc_android().lastIndexOf("/");
		String fileName = zone.getMapsrc_android().substring(pos + 1).split("[.]")[0];
		FileUtil.deleteFile(new File(FileUtil.getApkStorageFile(context), fileName));

		pos = zone.getSrc().lastIndexOf("/");
		fileName = zone.getSrc().substring(pos + 1).split("[.]")[0];
		FileUtil.deleteFile(new File(FileUtil.getApkStorageFile(context), fileName));

		WhereBuilder wb = new WhereBuilder(Zone.class).equals("id",
				zone.getId());
		liteOrm.delete(wb);
	}
	@SuppressWarnings("hiding")
	public static <City> void insertAll(List<City> list) {
		liteOrm.save(list);
	}
	public static City getCity(String name) {
		// TODO Auto-generated method stub
		QueryBuilder qb = new QueryBuilder(City.class).whereEquals("name",
				name);
		List<City> list=liteOrm.query(qb);
		if(list!=null&&list.size()!=0){
			return list.get(0);
		}
		return null;

	}

	public static List<City> getCityList(){
		if(liteOrm!=null){
			List<City> temp=liteOrm.query(City.class);
			return temp;
		}else{
			List<City> citys=new ArrayList<City>();
			return citys;
		}

	}

	public void saveBlueToothDevice(BlueToothInfo info){
		liteOrm.save(info);
	}
	public List<BlueToothInfo> getBlueToothDevice(){
		List<BlueToothInfo> temp=liteOrm.query(BlueToothInfo.class);
		return temp;

	}
	public static void saveRouteInfo(List<RouteInfos> routeInfos){
		liteOrm.save(routeInfos);
	}
	public static List<RouteInfos> getRouteInfo(Zone zone){
		QueryBuilder qb = new QueryBuilder(RouteInfos.class).whereEquals("jid",zone.getId());
		List<RouteInfos> zoneList=liteOrm.<RouteInfos>query(qb);
		return zoneList;
	}




}
