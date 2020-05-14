package com.xlu.uis;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

import com.google.gson.reflect.TypeToken;
import com.pandacard.teavel.R;
import com.pandacard.teavel.apps.MyApplication;
import com.xlu.bases.BaseActivity;
import com.xlu.bases.model.MapLayer;
import com.xlu.bases.model.MapObject;
import com.xlu.po.Jieshuo;
import com.xlu.po.Member;
import com.xlu.po.MyEvent;
import com.xlu.po.XiangQu;
import com.xlu.po.Zone;
import com.xlu.sys.SystemManger;
import com.xlu.uis.maps.MapWidget;
import com.xlu.uis.maps.interfaces.MapEventsListener;
import com.xlu.uis.maps.interfaces.OnMapScrollListener;
import com.xlu.uis.maps.interfaces.OnMapTouchListener;
import com.xlu.uis.maps.interfaces.event.MapScrolledEvent;
import com.xlu.uis.maps.interfaces.event.MapTouchedEvent;
import com.xlu.uis.maps.interfaces.event.ObjectTouchEvent;
import com.xlu.utils.AppUtil;
import com.xlu.utils.Constance;
import com.xlu.utils.DBUtil;
import com.xlu.utils.FileUtil;
import com.xlu.utils.JieshuoPopup;
import com.xlu.utils.JsonUtil;
import com.xlu.utils.PivotFactory;
import com.xlu.utils.servers.DownloadService;
import com.xlu.utils.servers.PlayService;
import com.xlu.widgets.CustomDialog;
import com.xlu.widgets.CustumDialog;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.wideroad.BaseHttp;
import cn.com.wideroad.http.AjaxCallBack;
import cn.com.wideroad.http.AjaxParams;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class ActivityZone extends BaseActivity implements MapEventsListener,
		OnMapTouchListener, SensorEventListener {

	private MapWidget mapWidget;
	private DownloadService.DownloadBinder downloadBinder;
	private boolean isAutoNavi=true;

	@BindView(R.id.iv_zone_back)
    ImageView ivBack;

	@BindView(R.id.tv_zone_name)
    TextView tvName;

	@BindView(R.id.tv_download)
    TextView tvDownload;

	@BindView(R.id.ly_download)
    LinearLayout lyDownload;

	@BindView(R.id.tv_moni)
    TextView tvMoni;

	@BindView(R.id.tv_nav)
    TextView tvNav;

	@BindView(R.id.iv_auto_nav)
    ImageView ivAutoNav;

	@BindView(R.id.rl_zone_map)
    RelativeLayout rlZoneMap;

	private ProgressDialog pbarDialog;

	private MapLayer layer;
	private List<Jieshuo> jieShuoList;
	private int jieshuoIndex;
	private Zone zone;
	protected PlayService.PlayBinder playBinder;
	private Jieshuo jieshuo;
	private Jieshuo jieshuo1;
	private Jieshuo jieshuo3;
	private Jieshuo jieshuoLine;
	private Bundle savedInstanceState;
	private boolean isDownload;
	private JieshuoPopup jieshuoPopup;
	private boolean isResume = false;
	private AudioManager audioManager;
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private Handler myHandler;
	CustumDialog dailog;
	private Handler handlerMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

		initPhoneListener();

		Type type = new TypeToken<Zone>() {
		}.getType();
		zone = (Zone) JsonUtil.fromJsonToObject(
				getIntent().getStringExtra("zonestr"), type);
		if (zone.getColor() == null || zone.getColor().equals(""))
			rlZoneMap.setBackgroundColor(Color.parseColor("#9db557"));
		else {
			if (zone.getColor().startsWith("#"))
				rlZoneMap.setBackgroundColor(Color.parseColor(zone.getColor()));
			else
				rlZoneMap.setBackgroundColor(Color.parseColor("#"
						+ zone.getColor()));
		}
		this.savedInstanceState = savedInstanceState;
		Intent intent = new Intent("cn.com.wideroad.xiaolu.service.download");// �������ط���
		intent.setPackage(getPackageName());
		context.bindService(intent, conn, BIND_AUTO_CREATE);
		intent = new Intent("cn.com.wideroad.xiaolu.service.play");
		intent.setPackage(getPackageName());
		context.bindService(intent, conn1, BIND_AUTO_CREATE);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constance.FIND_BLUE_TOOTH);
		filter.addAction(Constance.JIESHUO_PLAY_END);
		filter.addAction(Constance.DOWNLOAD_MAP_DONE);
		filter.addAction(Constance.DOWNLOAD_MAP_FAIL);
		filter.addAction(Constance.TISHI_END);
		filter.addAction(Constance.LINE_TISHI_END);
		filter.addAction(Constance.GPSLOCATION);
		registerReceiver(receiver, filter);
		isDownload = getIntent().getBooleanExtra("isDownload", false);
		pbarDialog = new ProgressDialog(this);
		pbarDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pbarDialog.setMessage("加载地图中...");
		pbarDialog.setCancelable(false);
		// ��ȥ
		addXQ();
	}

	// --------------------------监听来电----------------------------------
	private void initPhoneListener() {
		// 获取电话管理类
		TelephonyManager tpm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		// 设置监听
		tpm.listen(new MyPhoneStateListener(),
				PhoneStateListener.LISTEN_CALL_STATE);
	}

	class MyPhoneStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE: //
				EventBus.getDefault().post(
						new MyEvent(TelephonyManager.CALL_STATE_IDLE));
				break;
			case TelephonyManager.CALL_STATE_RINGING: //响铃
				EventBus.getDefault().post(
						new MyEvent(TelephonyManager.CALL_STATE_RINGING));
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK: // 挂断电话
				EventBus.getDefault().post(
						new MyEvent(TelephonyManager.CALL_STATE_OFFHOOK));
				break;
			}
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("确认退出吗,退出后将退出自动导览?");
		builder.setTitle("提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	@Override
	@Subscribe
	public void onEventMainThread(MyEvent e) {
		// TODO Auto-generated method stub
		super.onEventMainThread(e);
		if (e.getId() == TelephonyManager.CALL_STATE_RINGING) {
			if (playBinder.isPlaying()) {
				playBinder.pause(1);
				jieshuoPopup.setPlay(false);
				isResume = true;
			}
		} else if (e.getId() == TelephonyManager.CALL_STATE_IDLE) {
			if (isResume) {
				isResume = false;
				playBinder.resume();
				jieshuoPopup.setPlay(true);
			}
		} else if (e.getId() == TelephonyManager.CALL_STATE_OFFHOOK) {
			if (playBinder.isPlaying()) {
				playBinder.pause(1);
				jieshuoPopup.setPlay(false);
				isResume = true;
			}
		} else if (e.getId() == Constance.MYEVETN_GET_FOCUSS) {
			if (isResume) {
				isResume = false;
				playBinder.resume();
				jieshuoPopup.setPlay(true);
			}
		} else if (e.getId() == Constance.MYEVETN_LOSS_FOCUSS) {
			if (playBinder.isPlaying()) {
				playBinder.pause(1);
				jieshuoPopup.setPlay(false);
				isResume = true;
			}
		} else if (e.getId() == Integer.MAX_VALUE - 1003) {
			if(!isAutoNavi)
				
				playBinder.play1(Constance.HTTP_URL+"/upload/"+zone.getId()+"/"+jieshuoLine.getYuyin()+".mp3");
		}

	}

	private void initViews() {
		// TODO Auto-generated method stub
		tvName.setText(zone.getName());
		if (DBUtil.zoneHaveDownload(zone.getId())) {
			tvDownload.setText("自动导览");
			lyDownload.setBackgroundResource(R.drawable.rectangle_auto_nav);
			ivAutoNav.setImageResource(R.drawable.iv_auto_nav1);
			tvDownload.setTextColor(0xee111111);
			if (!zone.getAutomatic().equals("1")) {
				tvDownload.setTextColor(0x99999999);
			}
		} else {
			if (downloadBinder.isDownloading(zone)) {
				handler.removeMessages(1);
				handler.sendEmptyMessage(1);
				tvDownload.setText("正在下载" + downloadBinder.getProgress(zone)
						+ "%");
			} else {
				tvDownload.setText("下载语音离线包");
			}
		}
	}

	private void addXQ() {

		XiangQu xq = (XiangQu) JsonUtil.fromJsonToObject(getIntent()
				.getStringExtra("zonestr"), new TypeToken<XiangQu>() {
		}.getType());
		DBUtil.addXiangQu(xq);

		EventBus.getDefault().post(new MyEvent(Constance.MYEVENT_XIANGQU_ADD));

		BaseHttp http = new BaseHttp();
		AjaxParams params = new AjaxParams();
		params.put("sid", MyApplication.deviceId);
		params.put("jid", zone.getId() + "");
		http.post(Constance.HTTP_REQUEST_URL + "xiangqu", params,
				new AjaxCallBack<Object>() {
					@Override
					public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
						super.onFailure(t, errorNo, strMsg);
					}

					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
					}
				});

	}

	private ServiceConnection conn1 = new ServiceConnection() {

		public void onServiceDisconnected(ComponentName name) {

		}

		public void onServiceConnected(ComponentName name, IBinder service) {
			playBinder = (PlayService.PlayBinder) service;
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (DBUtil.zoneHaveDownload(zone.getId())) {
				tvDownload.setText("自动导览");
				lyDownload.setBackgroundResource(R.drawable.rectangle_auto_nav);
				ivAutoNav.setImageResource(R.drawable.iv_auto_nav1);
				tvDownload.setTextColor(0xee111111);
				if (!zone.getAutomatic().equals("1")) {
					tvDownload.setTextColor(0x99999999);
				}
				return;
			} else {
				if (downloadBinder.isDownloading(zone)) {
					tvDownload.setText("正在下载"
							+ downloadBinder.getProgress(zone) + "%");
				} else {
					tvDownload.setText("下载语音离线包");
				}
			}
			handler.sendEmptyMessageDelayed(1, 1000);
		}

	};

	private ServiceConnection conn = new ServiceConnection() {

		public void onServiceDisconnected(ComponentName name) {

		}

		public void onServiceConnected(ComponentName name, IBinder service) {
			downloadBinder = (DownloadService.DownloadBinder) service;
			initViews();
			String url = zone.getMapsrc_android();
			if (url == null || "".equals(url)) {
				AppUtil.showToastMsg(context, "景点地图正在建设中，敬请期待");
				initMap1();
			} else {
				int pos = url.lastIndexOf("/");
				String fileName = url.substring(pos + 1).split("[.]")[0];
				if (FileUtil.checkIsExistInLocal1(
						FileUtil.getApkStorageFile(context), fileName)) {
					initMap();
					loadData(isDownload);
				} else {
					pbarDialog.show();
					downloadBinder.downloadMap(zone);
				}
			}
		}
	};

	private void loadData(boolean isDownload) {
		if (isDownload) {
			jieShuoList = DBUtil.getJieshuoList(zone.getId());
			if (jieShuoList.size() != 0)
				updataMap();
		} else {
			BaseHttp http = new BaseHttp();
			AjaxParams params = new AjaxParams();
			params.put("sid", MyApplication.deviceId);
			params.put("jid", zone.getId() + "");
			http.post(Constance.HTTP_REQUEST_URL + "getJieShuoDianByJid",
					params, new AjaxCallBack<Object>() {

						@Override
						public void onFailure(Throwable t, int errorNo,
                                              String strMsg) {
							// TODO Auto-generated method stub
							super.onFailure(t, errorNo, strMsg);
						}

						@Override
						public void onSuccess(Object t) {
							// TODO Auto-generated method stub
							super.onSuccess(t);
							try {

								// Type types = new TypeToken<List<Jieshuo>>() {
								// }.getType();
								// jieShuoList = (List<Jieshuo>)
								// JsonUtil.fromJsonToObject(t.toString(),
								// types);

								jieShuoList = JsonUtil.getList(t.toString(),
										Jieshuo.class);
								if (jieShuoList.size() != 0) {
									updataMap();
								}
							} catch (Exception e) {

							}
						}

					});

		}
	}

	public void onTouch(MapWidget v, MapTouchedEvent event) {
		// TODO Auto-generated method stub
		ArrayList<ObjectTouchEvent> touchedObjs = event.getTouchedObjectIds();
		if (touchedObjs.size() > 0) {
			isAutoNavi=false;
			ObjectTouchEvent objectTouchEvent = event.getTouchedObjectIds()
					.get(0);
			Jieshuo jieshuo = (Jieshuo) objectTouchEvent.getObjectId();
			jieshuoLine=jieshuo;
			handlerMap = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if (msg.what == 1) {

					}
				}

			};
			
			if (FileUtil.checkIsExistInLocal(
					FileUtil.getApkStorageFile(context), jieshuo.getYuyin())) {
				playJieShuo(jieshuo);
			} else {
				CustomDialog.Builder dialog=new CustomDialog.Builder(ActivityZone.this);
				dialog.setTitle(R.string.map_title)
				.setMessage(R.string.map_msg)
				.setPositiveButton(R.string.map_load, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						isAutoNavi = false;
						downloadOrNav();
						dialog.dismiss();
						
					}
				})
				.setNegativeButton(R.string.map_play,new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						isAutoNavi=false;
						playBinder.pause(1);
						playBinder.playTishi("msg.mp3", 0);
						jieshuoPopup.setJieshuo(jieshuoLine);
						int x = xToScreenCoords(Double.valueOf(jieshuoLine.getZuobiao_x())
								.intValue());
						int y = yToScreenCoords(Double.valueOf(jieshuoLine.getZuobiao_y())
								.intValue());
						jieshuoPopup.show(rlZoneMap, x, y);
						
//					playBindViewer.play1(Constance.HTTP_URL+"/upload/"+zone.getId()+"/"+jieshuo.getYuyin()+".mp3");
					
						dialog.dismiss();
						
					}
				})
				.create()
				.show();
//				dailog = new CustumDialog(context) {
//
//					@Override
//					public void setTvJindu() {
//						super.setTvJindu();
//						// if (downloadBindViewer.isDownloading(zone)) {
//						myHandler = new Handler() {
//							@Override
//							public void handleMessage(Message msg) {
//								super.handleMessage(msg);
//								if (DBUtil.zoneHaveDownload(zone.getId())) {
//									dailog.setTvJinduVisible(true);
//									dailog.tvJindu.setText("离线语音包下载完毕");
//									dailog.setPositiveButtonClick(false);
//									dailog.setNegativeButtonClick(true);
//									dailog.setPositiveButtonText("已下载");
//									dailog.setNegativeButtonText("关闭");
//									dailog.setPbjinduVisible(false);
//									if (tvDownload != null)
//										tvDownload.setText("自动导览");
//									if (lyDownload != null)
//										lyDownload
//												.setBackgroundResource(R.drawable.rectangle_auto_nav);
//									if (ivAutoNav != null)
//										ivAutoNav
//												.setImageResource(R.drawable.iv_auto_nav1);
//									if (tvDownload != null)
//										tvDownload.setTextColor(0xee111111);
//									if (!zone.getAutomatic().equals("1")) {
//										tvDownload.setTextColor(0x99999999);
//									}
//								} else {
//									dailog.setPbjinduVisible(true);
//									dailog.setTvJinduVisible(true);
//									dailog.setPositiveButtonClick(false);
//									dailog.setPositiveButtonText("下载...");
//									dailog.tvJindu.setText("正在下载"
//											+ downloadBindViewer.getProgress(zone)
//											+ "%");
//									dailog.pbjindu.setProgress(downloadBindViewer
//											.getProgress(zone));
//								}
//
//								myHandler.sendEmptyMessageDelayed(1, 1000);
//
//							}
//						};
//						// tvDownload.setText("��������"
//						// + downloadBindViewer.getProgress(zone) + "%");
//						// }
//					}
//
//				};
//				dailog.setNegativeButtonText("关闭");
//				dailog.setPositiveButtonText("下载");
//				dailog.setNegativeButtonClick(true);
//				dailog.setPositiveButtonClick(true);
//				dailog.show();
//				dailog.setOnNegativeListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						dailog.dismiss();
//					}
//				});
//				dailog.setOnPositiveListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						// downloadOrNav();
//						dailog.setPositiveButtonClick(false);
//						dailog.setTvJindu();
//						downloadBindViewer.downloadZone(jieShuoList, zone);
//						myHandler.removeMessages(1);
//						myHandler.sendEmptyMessage(1);
//
//					}
//				});
			}
		}

	}

	private int xToScreenCoords(int mapCoord) {
		return (int) (mapCoord * mapWidget.getScale() - mapWidget.getScrollX());
	}

	private int yToScreenCoords(int mapCoord) {
		return (int) (mapCoord * mapWidget.getScale() - mapWidget.getScrollY());
	}

	private void playJieShuo(Jieshuo jieshuo2) {
		// TODO Auto-generated method stub

		if (jieshuo2 != null) {
			if (!DBUtil.freeIsEnd(zone.getId(), zone.getName())
					|| DBUtil.zoneIsPay(zone.getId()) || zone.getPrice() == 0) {
				jieshuoPopup.setJieshuo(jieshuo2);
				int x = xToScreenCoords(Double.valueOf(jieshuo2.getZuobiao_x())
						.intValue());
				int y = yToScreenCoords(Double.valueOf(jieshuo2.getZuobiao_y())
						.intValue());
				jieshuoPopup.show(rlZoneMap, x, y);
				if (!jieshuo2.equals(jieshuo1)) {
					jieshuoPopup.setPlay(true);
					playBinder.pause(1);
					playBinder.playTishi("msg.mp3", 1);
					DBUtil.consumeFree(zone.getId());
					jieshuo2.setListen(true);
					jieshuo1 = jieshuo2;
					jieshuo3 = jieshuo2;
				} else {
					playBinder.resume();
				}
				updataMap();
			} else {
				if (!SystemManger.isInPayZoneActivity) {
					AppUtil.showToastMsg(context, "亲，试用结束了,请激活景点");
					goActivityPayZone();
				}
			}
		}
	}

	private void handleOnMapScroll(MapWidget v, MapScrolledEvent event) {
		// When user scrolls the map we receive scroll events
		// This is useful when need to move some object together with the map

		int dx = event.getDX(); // Number of pixels that user has scrolled
								// horizontally
		int dy = event.getDY(); // Number of pixels that user has scrolled
								// vertically

		if (jieshuoPopup.isVisible()) {
			jieshuoPopup.moveBy(dx, dy);
		}
	}

	private void playJieShuoAuto(Jieshuo jieshuo2) {
		// TODO Auto-generated method stub
		isAutoNavi=true;
		if (jieshuo2 != null) {
			if (!DBUtil.freeIsEnd(zone.getId(), zone.getName())
					|| DBUtil.zoneIsPay(zone.getId()) || zone.getPrice() == 0) {
				if (!SystemManger.yuying.equals(jieshuo2.getYuyin())) {
					
					
					jieshuoPopup.setJieshuo(jieshuo2);
					int x = xToScreenCoords(Double.valueOf(
							jieshuo2.getZuobiao_x()).intValue());
					int y = yToScreenCoords(Double.valueOf(
							jieshuo2.getZuobiao_y()).intValue());
					jieshuoPopup.show(rlZoneMap, x, y);
					SystemManger.yuying = jieshuo2.getYuyin();
					playBinder.pause(1);
					jieshuoPopup.setPlay(true);
					playBinder.playTishi("msg.mp3", 1);
					jieshuo2.setListen(true);
					jieshuo = jieshuo2;
					jieshuo3 = jieshuo2;
					jieshuo1 = null;
					updataMap();
					mapWidget.scrollMapTo(jieshuo3.getZuobiao_x(),
							jieshuo3.getZuobiao_y());
					DBUtil.consumeFree(zone.getId());
					helloserver(MyApplication.deviceId, zone.getId(), jieshuo2.getId());
				}
				
			} else {
				if (!SystemManger.isInPayZoneActivity) {
					AppUtil.showToastMsg(context, "亲，试用结束了,请激活景点");
					goActivityPayZone();
				}
			}
		}
	}

	private void helloserver(String deviceId, Integer id, Integer id2) {
		// TODO Auto-generated method stub
		BaseHttp http = new BaseHttp();
		AjaxParams params = new AjaxParams();
		params.put("tid", deviceId);
		params.put("jsid", id + "");
		params.put("jid", id2 + "");
		http.post(Constance.HTTP_REQUEST_URL + "helloserver", params,
				new AjaxCallBack<Object>() {

				});
	}

	private void updataMap() {
		layer.clearAll();
		mapWidget.clearMapPath();
		// ArrayList<Point> path = new ArrayList<Point>();
		for (int j = 0; j < jieShuoList.size(); j++) {
			Jieshuo jieshuo2 = jieShuoList.get(j);
			Drawable icon = getResources().getDrawable(
					R.drawable.iv_map_marker3);
			// path.add(new Point(jieshuo2.getZuobiao_x(),
			// jieshuo2.getZuobiao_y()));
			if (jieshuo2.isListen())
				icon = getResources().getDrawable(R.drawable.iv_map_market2);
			if (jieshuo3 != null && jieshuo3.equals(jieshuo2))
				icon = getResources().getDrawable(R.drawable.iv_map_market1);
			MapObject mapObject = new MapObject(jieshuo2, icon, new Point(
					jieshuo2.getZuobiao_x(), jieshuo2.getZuobiao_y()),
					PivotFactory.createPivotPoint(icon,
							PivotFactory.PivotPosition.PIVOT_CENTER), true, false);
			layer.addMapObject(mapObject);
		}
		if (jieshuo != null) {
			Drawable icon1 = getResources().getDrawable(
					R.drawable.iv_map_location);
			MapObject mapObject = new MapObject(jieshuo, icon1, new Point(
					jieshuo.getZuobiao_x(), jieshuo.getZuobiao_y()),
					PivotFactory.createPivotPoint(icon1,
							PivotFactory.PivotPosition.PIVOT_CENTER), true, false);
			layer.addMapObject(mapObject);
		}
		// mapWidget.addMapPath(new MapPath(path, DensityUtil.dip2px(context,
		// 3)));
	}

	@OnClick({ R.id.iv_zone_back, R.id.ly_download, R.id.tv_moni, R.id.tv_nav })
	public void onClick(View v) {
		// TODO Auto-generated method stub
		try {
			String str = tvMoni.getText().toString();
			switch (v.getId()) {
			case R.id.iv_zone_back:
				onBackPressed();
				break;

			case R.id.ly_download:
				downloadOrNav();
				break;

			case R.id.tv_moni:
				Moni();
				break;

			case R.id.tv_nav:
				goActivityDashang();
				break;

			}
		} catch (Exception e) {
			// TODO: handle exception
			BaseHttp http = new BaseHttp();
			Member member = DBUtil.getLoginMeber();
			AjaxParams params = new AjaxParams();
			params.put("sid", MyApplication.deviceId);
			if (member != null)
				params.put("tel", member.getTel());
			params.put("content", e.getMessage() + e.getCause());
			http.post(Constance.HTTP_REQUEST_URL + "dosaveYiChang", params,
					new AjaxCallBack<Object>() {

						@Override
						public void onFailure(Throwable t, int errorNo,
                                              String strMsg) {
							// TODO Auto-generated method stub
							super.onFailure(t, errorNo, strMsg);
						}

						@Override
						public void onSuccess(Object t) {
							// TODO Auto-generated method stub
							super.onSuccess(t);
						}

					});
		}
	}

	private void goActivityDashang() {
		// TODO Auto-generated method stub
//		Intent intent = new Intent(context, ActivityDaShang.class);
//		startActivity(intent);
	}

	private void goActivityJieshuoDetail() {
		// TODO Auto-generated method stub
//		Intent intent = new Intent(context, ActivityJieshuoDetail.class);
//		Type type = new TypeToken<Jieshuo>() {
//		}.getType();
//		String str = JsonUtil.toJsonString(jieshuo3, type);
//		intent.putExtra("jieshuostr", str);
//		startActivity(intent);
	}

	private void Moni() {
		// TODO Auto-generated method stub
		if (tvMoni.getText().equals("模拟")) {
			if (DBUtil.zoneHaveDownload(zone.getId())) {
				stopBluetooth();
				tvMoni.setText("关闭");
				tvDownload.setText("自动导览");
				jieshuoIndex = 0;
				playBinder.playTishi("moni.mp3", 2);
			} else {
//				MyApplication.showSingleton("未下载离线语音包");
				playBinder.playTishi("no_download.mp3", 0);
			}
		} else {
			SystemManger.yuying = "";
			tvMoni.setText("模拟");
			playBinder.pause(1);
			playBinder.pause(2);
			jieshuoPopup.hide();
			jieshuoIndex = 0;
		}
	}

	private void downloadOrNav() {
		String str = tvDownload.getText().toString();
		if (str.isEmpty()) {
			return;
		}
		if (str.equals("下载语音离线包")) {
			downloadBinder.downloadZone(jieShuoList, zone);
			handler.removeMessages(1);
			handler.sendEmptyMessage(1);
		} else if (str.equals("自动导览")) {
			if (!zone.getAutomatic().equals("1")) {
//				App.showSingleton("亲，该景点暂不提供自动导览服务");
				return;
			}
			if (DBUtil.zoneHaveDownload(zone.getId())) {
				jieshuoIndex = 0;
				SystemManger.yuying = "";
				tvMoni.setText("模拟");
				tvDownload.setText("停止导览");
				playBinder.pause(1);
				playBinder.pause(2);
				playBinder.playTishi("start_nav.mp3", 0);
				jieshuo1 = null;
				startBluetooth();
				jieshuoPopup.hide();
			}
		} else if (str.equals("停止导览")) {
			jieshuo1 = null;
			tvDownload.setText("自动导览");
			stopBluetooth();
		}
	}

	private Handler handler1 = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			playJieShuoAuto((Jieshuo) msg.obj);
		}

	};

	private void startBluetooth() {
		Intent intent;
		if (getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)
				&& android.os.Build.VERSION.SDK_INT >= 18)
			intent = new Intent("cn.com.wideroad.xiaolu.service.BluetoothImp4");
		else
			intent = new Intent("cn.com.wideroad.xiaolu.service.BluetoothImp2");
		if (zone.getType().equals("1"))
			intent = new Intent("cn.com.wideroad.xiaolu.service.gpsService");
		intent.setPackage(getPackageName());
		startService(intent);
	}

	protected void goActivityPayZone() {
		// TODO Auto-generated method stub
		BaseHttp http = new BaseHttp();
		AjaxParams params = new AjaxParams();
		params.put("sid", MyApplication.deviceId);
		params.put("id", zone.getId() + "");
		http.post(Constance.HTTP_REQUEST_URL + "getJingdianById", params,
				new AjaxCallBack<Object>() {

					@Override
					public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, errorNo, strMsg);
					}

					@Override
					public void onSuccess(Object t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						try {
							Type type = new TypeToken<Zone>() {
							}.getType();
							Zone zone = (Zone) JsonUtil.fromJsonToObject(
									t.toString(), type);
//							Intent intent = new Intent(context,
//									ActivityPayZone.class);
//							intent.putExtra("name", zone.getName());
//							intent.putExtra("memo", zone.getMemo());
//							intent.putExtra("money", zone.getPrice() + "");
//							intent.putExtra("id", zone.getId());
//							startActivity(intent);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}

				});
	}

	protected Jieshuo getJieshuo(String mac) {
		for (Jieshuo jieshuo : jieShuoList) {
			if (jieshuo.getMac().equals(mac))
				return jieshuo;
		}
		return null;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler.removeMessages(1);
		handler1.removeMessages(1);
		stopBluetooth();
		unregisterReceiver(receiver);
		unbindService(conn);
		unbindService(conn1);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mSensorManager.registerListener(this, mSensor,
				SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mSensorManager.unregisterListener(this);
		super.onPause();

	}

	private void stopBluetooth() {
		Intent intent;
		SystemManger.yuying = "";
		if (getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)
				&& android.os.Build.VERSION.SDK_INT >= 18)
			intent = new Intent("cn.com.wideroad.xiaolu.service.BluetoothImp4");
		else
			intent = new Intent("cn.com.wideroad.xiaolu.service.BluetoothImp2");
		if (zone.getType().equals("1"))
			intent = new Intent("cn.com.wideroad.xiaolu.service.gpsService");
		intent.setPackage(getPackageName());
		stopService(intent);
		if (jieshuoPopup != null)
			jieshuoPopup.hide();
		if (playBinder != null) {
			playBinder.pause(1);
			playBinder.pause(2);
		}
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context1, Intent intent) {
			// TODO Auto-generated method stub
			try {
				String action = intent.getAction();
				if (action.equals(Constance.FIND_BLUE_TOOTH)) {
					String address = intent.getStringExtra("address");
					Float dis = intent.getFloatExtra("dis", 0);
					if (dis > zone.getDistance()){
						return;
					}
					Message msg = Message.obtain();
					Jieshuo jieshuo = getJieshuo(address);
//					("Bluetooth4","onReceive 解说点名称："+jieshuo.getMac()+"-->"+jieshuo.getName());
					msg.obj = jieshuo;
					handler1.sendMessage(msg);
				} else if (action.equals(Constance.GPSLOCATION)) {
					double longitude = intent.getDoubleExtra("longitude", 0);
					double latitude = intent.getDoubleExtra("latitude", 0);
					Jieshuo jie = getJieshuoBylocation(longitude, latitude);
					Message msg = Message.obtain();
					msg.obj = jie;
					handler1.sendMessage(msg);
				} else if (action.equals(Constance.JIESHUO_PLAY_END)) {
					playBinder.pause(1);
					if (tvMoni.getText().toString().equals("关闭")) {
						if (jieshuoIndex + 1 < jieShuoList.size()) {
							jieshuoIndex++;
							playJieShuoAuto(jieShuoList.get(jieshuoIndex));
						} else {
							jieshuoIndex = 0;
							tvMoni.setText("模拟");
						}
					}
				}

				else if (action.equals(Constance.DOWNLOAD_MAP_DONE)) {
					initMap();
					pbarDialog.dismiss();
					loadData(isDownload);
				} else if (action.equals(Constance.TISHI_END)) {
					playBinder.play(jieshuo3);
				} else if (action.equals(Constance.LINE_TISHI_END)) {
					playJieShuoAuto(jieShuoList.get(jieshuoIndex));
				} else if (action.equals(Constance.DOWNLOAD_MAP_FAIL)) {
					pbarDialog.dismiss();
//					App.showSingleton("地图加载失败");
				}
			} catch (Exception e) {

			}
		}

	};

	protected Jieshuo getJieshuoBylocation(double longitude, double latitude) {
		// TODO Auto-generated method stub
		try {
			Jieshuo jieshuo1 = null;
			float dis1 = 0;
			for (Jieshuo jieshuo : jieShuoList) {
				float dis = AMapUtils.calculateLineDistance(new LatLng(
						latitude, longitude), new LatLng(jieshuo.getLatitude(),
						jieshuo.getLongitude()));
				if (dis1 == 0 || dis < dis1) {
					jieshuo1 = jieshuo;
					dis1 = dis;
				}
			}
			if (dis1 < zone.getDistance())
				return jieshuo1;
			else
				return null;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	private void initMap() {
		String url = zone.getMapsrc_android();
		int pos = url.lastIndexOf("/");
		String name = url.substring(pos + 1).split("[.]")[0];
		File file = FileUtil.getLocalFile1(FileUtil.getApkStorageFile(context),
				name);
		file = new File(file, "map");
		mapWidget = new MapWidget(savedInstanceState, this, file, 11);
		mapWidget.setOnMapTouchListener(this);
		mapWidget.addMapEventsListener(this);
		rlZoneMap.addView(mapWidget);
		jieshuoPopup = new JieshuoPopup(context, rlZoneMap);
		jieshuoPopup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goActivityJieshuoDetail();
			}
		});
		jieshuoPopup.setPlayOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (jieshuoPopup.isPlay()) {
					playBinder.pause(1);
					jieshuoPopup.setPlay(false);
				} else {
					playBinder.resume();
					jieshuoPopup.setPlay(true);
				}
			}
		});
		mapWidget.getConfig().setFlingEnabled(true);
		mapWidget.getConfig().setPinchZoomEnabled(true);

		mapWidget.setMaxZoomLevel(13);
		mapWidget.setMinZoomLevel(10);
		mapWidget.setUseSoftwareZoom(true);
		mapWidget.setZoomButtonsVisible(false);
		layer = mapWidget.createLayer(100);
		mapWidget.setOnMapScrolledListener(new OnMapScrollListener() {
			public void onScrolledEvent(MapWidget v, MapScrolledEvent event) {
				handleOnMapScroll(v, event);
			}
		});
	}

	private void initMap1() {
		mapWidget = new MapWidget(savedInstanceState, this, "map", // root name
																	// of the
																	// map under
																	// assets
																	// folder.
				10); // initial zoom level
		layer = mapWidget.createLayer(100);
		jieshuoPopup = new JieshuoPopup(context, rlZoneMap);
	}

	@Override
	public void onPostZoomIn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPostZoomOut() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPreZoomIn() {
		// TODO Auto-generated method stub
		if (jieshuoPopup != null) {
			jieshuoPopup.hide();
		}
	}

	@Override
	public void onPreZoomOut() {
		// TODO Auto-generated method stub
		if (jieshuoPopup != null) {
			jieshuoPopup.hide();
		}
	}

	@Override
	protected int initView() {
		// TODO Auto-generated method stub
		return R.layout.activity_zone;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		/*
		 * float range = event.values[0]; if (range ==
		 * mSensor.getMaximumRange()) {
		 * audioManager.setMode(AudioManager.MODE_NORMAL); } else {
		 * audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION); }
		 */

	}

}
