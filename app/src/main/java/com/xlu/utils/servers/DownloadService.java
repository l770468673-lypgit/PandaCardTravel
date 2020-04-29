package com.xlu.utils.servers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.pandacard.teavel.R;
import com.xlu.po.Jieshuo;
import com.xlu.po.Zone;
import com.xlu.utils.AppUtil;
import com.xlu.utils.Constance;
import com.xlu.utils.DBUtil;
import com.xlu.utils.FileUtil;
import com.xlu.utils.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DownloadService extends Service {

	private static Map<Zone, Integer> mDownloadProgress = new HashMap<Zone, Integer>();
	private Context context;
	private static Map<String, Integer> mUpdateProgress = new HashMap<String, Integer>();
	public NotificationManager mNotificationManager;
	public Notification notification;
	public static final int UPDATEVERSION = 100;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = this;
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}
	
	private boolean containsZone(Zone zone2) {//�ж�ĳ��������Ϣ��mDownloadProgress������
		// TODO Auto-generated method stub
		for( Zone zone : mDownloadProgress.keySet()){
			if(zone2.getId() == zone.getId()){
				return true;
			}
		}
		return false;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return new DownloadBinder();
	}

	public class DownloadBinder extends Binder {

		public void updateVersion(String url) {
			// TODO Auto-generated method stub
			downloadApp(url);
		}

		public int getProgress(Zone zone) {
			// TODO Auto-generated method stub
			for( Zone zone2 : mDownloadProgress.keySet()){
				if(zone2.getId() == zone.getId()){
					return mDownloadProgress.get(zone2);
				}
			}
			return 0;
		}

		public boolean isDownloading(Zone zone) {//�Ƿ���������
			// TODO Auto-generated method stub
			return containsZone(zone);
		}

		private void downloadApp(final String url) {
			if (url != null) {
				int pos = url.lastIndexOf("/");
				String name = url.substring(pos + 1);
				File file = FileUtil.getExternalApkStorageFile(context);
				FileUtil.CreateDir(file);
				file = new File(file, name.trim());
				final String path = file.getAbsolutePath();

				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (!mUpdateProgress.containsKey(url)) {
					AppUtil.showToastMsg(context, "小鹿智游开始更新");
					mUpdateProgress.put(url, 0);
					notificationProgress(url);
				} else {
					AppUtil.showToastMsg(context, "小鹿智游正在更新");
				}
				Log.e("DownLoadActivity",
						"--->file.getAbsolutePath():" + file.getAbsolutePath());
				Log.e("DownLoadActivity", "--->file.exists:" + file.exists());

				HttpUtils http = new HttpUtils();
				HttpHandler handler = http.download(Constance.HTTP_URL + url,
						path, false, //
						true, //
						new RequestCallBack<File>() {

							@Override
							public void onStart() {
							}

							@Override
							public void onLoading(long total, long current,
									boolean isUploading) {
								int pec = (int) ((float) current / total * 100);
								mUpdateProgress.put(url, pec);
								notificationProgress(url);
							}

							public void onSuccess(
									ResponseInfo<File> responseInfo) {
								mNotificationManager.cancel(UPDATEVERSION);
								mUpdateProgress.remove(url);
								Intent intent = new Intent(Intent.ACTION_VIEW);
								if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N){
									intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
									intent.setDataAndType(
											Uri.parse("file://" + path),
											"application/vnd.android.package-archive");
								}else{
									intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									intent.setDataAndType(
											Uri.parse("file://" + path),
											"application/vnd.android.package-archive");
								}


								DownloadService.this.startActivity(intent);
							}

							public void onFailure(HttpException error,
                                                  String msg) {
								AppUtil.showToastMsg(context, "小鹿智游更新失败，请重新下载");
								mUpdateProgress.remove(url);
								mNotificationManager.cancel(UPDATEVERSION);
							}
						});
			}
		}

		public void downloadZone(final List<Jieshuo> jieshuos, final Zone zone) {
			if (!containsZone(zone)) {
				mDownloadProgress.put(zone, 0);
				AppUtil.showToastMsg(context, zone.getName() + "开始下载");
				String url = zone.getSrc();
				if (url != null) {
					int pos = url.lastIndexOf("/");
					String name = url.substring(pos + 1);
					File file = FileUtil
							.getApkStorageFile(getApplicationContext());
					FileUtil.CreateDir(file);

					file = new File(file, name.trim());
					final String path = file.getAbsolutePath();

					if (!file.exists()) {
						try {
							file.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					HttpUtils http = new HttpUtils();
					HttpHandler handler = http.download(Constance.HTTP_URL
							+ url, path, true, //
							true, //
							new RequestCallBack<File>() {

								@Override
								public void onStart() {
								}

								@Override
								public void onLoading(long total, long current,
										boolean isUploading) {
									int pec = (int) ((float) current / total * 100);
									mDownloadProgress.put(zone, pec);
								}

								public void onSuccess(
										ResponseInfo<File> responseInfo) {
									new Thread() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											super.run();
											ZipUtil.unZip(path);
											new File(path).delete();
											DBUtil.savaZone(context, jieshuos,
													zone);
											mDownloadProgress.remove(zone);
										}

									}.start();

								}

								public void onFailure(HttpException error,
                                                      String msg) {
									if(error.getExceptionCode() == 416){
										mDownloadProgress.put(zone, 100);
										new Thread() {

											@Override
											public void run() {
												// TODO Auto-generated method stub
												super.run();
												ZipUtil.unZip(path);
												new File(path).delete();
												DBUtil.savaZone(context, jieshuos,
														zone);
												mDownloadProgress.remove(zone);
											}

										}.start();
									}
									else {
										mDownloadProgress.remove(zone);
										AppUtil.showToastMsg(context, "亲，下载失败，请重试");
									}
								}
							});
				}
			}
		}


		public void downloadMap(final Zone zone) {
			String url = zone.getMapsrc_android();
			if (url != null) {
				int pos = url.lastIndexOf("/");
				String name = url.substring(pos + 1);
				File file = FileUtil.getApkStorageFile(getApplicationContext());
				FileUtil.CreateDir(file);

				file = new File(file, name.trim());
				final String path = file.getAbsolutePath();

				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				HttpUtils http = new HttpUtils();
				HttpHandler handler = http.download(Constance.HTTP_URL + url,
						path, true, //
						true, //
						new RequestCallBack<File>() {

							@Override
							public void onStart() {
							}

							@Override
							public void onLoading(long total, long current,
									boolean isUploading) {
								int pec = (int) ((float) current / total * 100);
								Log.e("", "pec" + pec);
							}

							public void onSuccess(
									ResponseInfo<File> responseInfo) {
								ZipUtil.unZip(path);
								new File(path).delete();
								Intent intent = new Intent(
										Constance.DOWNLOAD_MAP_DONE);
								context.sendBroadcast(intent);
							}

							public void onFailure(HttpException error,
                                                  String msg) {
								if(error.getExceptionCode() == 416){
									ZipUtil.unZip(path);
									new File(path).delete();
									Intent intent = new Intent(
											Constance.DOWNLOAD_MAP_DONE);
									context.sendBroadcast(intent);
								}
								else {
									Intent intent = new Intent(
											Constance.DOWNLOAD_MAP_FAIL);
									context.sendBroadcast(intent);
								}
							}
						});
			}
		}

		private void notificationProgress(String url) {
			if (notification == null) {
				notification = new Notification(R.drawable.ic_launcher, "小鹿智游",
						System.currentTimeMillis()); // ����֪ͨ��ʵ��
				RemoteViews contentView = new RemoteViews(
						DownloadService.this.getPackageName(),
						R.layout.notification_update);
				notification.contentView = contentView;
				notification.flags = Notification.FLAG_ONGOING_EVENT
						| Notification.FLAG_NO_CLEAR;
				Intent intent = new Intent(DownloadService.this,
						DownloadService.class);
				PendingIntent contentIntent = PendingIntent.getActivity(
						DownloadService.this, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				notification.contentIntent = contentIntent;
			}
			notification.contentView.setProgressBar(
					R.id.version_update_progressbar, 100,
					mUpdateProgress.get(url), false);
			notification.contentView.setTextViewText(R.id.tv_version_name,
					"小鹿智游正在更新" + mUpdateProgress.get(url) + "% ...");
			mNotificationManager.notify(UPDATEVERSION, notification);

		}
	}
}
