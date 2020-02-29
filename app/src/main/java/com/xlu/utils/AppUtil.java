package com.xlu.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.io.File;


public class AppUtil {

	public static void showToastMsg(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	/**
	 * 文件存在，并且是健康状况
	 *
	 * @return
	 */
	public static boolean fileExsitAndIsOk(Context context, File file) {
		if (!file.exists())
			return false;
		if (StorageUtil.externalMemoryAvailable()) {
			String s = getUninstalledAppPackageName(context, file.getAbsolutePath());
			return file.exists() && StringUtils.isNotBlank(s);
		} else {
			String s = getUninstalledAppPackageName(context, file.getAbsolutePath());
			return file.exists() && StringUtils.isNotBlank(s);
		}
	}

	/**
	 * 获取没有安装的app包名
	 *
	 * @param context
	 *            {@link Context}
	 * @param archiveFilePath
	 * @author Melvin
	 * @date 2013-4-23
	 * @return String
	 */
	public static String getUninstalledAppPackageName(Context context, String archiveFilePath) {
		if (context == null || StringUtils.isBlank(archiveFilePath)) {
			return null;
		}
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
		String packageName = "";
		if (info != null) {
			packageName = info.packageName;
		}
		return packageName;
	}

	public static int getAppVersionCode(Context context, String packageName) {
		int versionCode = 0;
		if (context == null) {
			return versionCode;
		}
		if (StringUtils.isBlank(packageName)) {
			return versionCode;
		}
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
			versionCode = packageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			return 0;
		}

		return versionCode;
	}

	public static String getAppNameByPackageName(Context context, String packageName) {
		if (context == null || StringUtils.isBlank(packageName))
			return null;
		PackageManager pm = context.getPackageManager();
		ApplicationInfo applicationInfo = getAppInfo(pm, packageName);
		if (applicationInfo != null) {
			CharSequence appName = pm.getApplicationLabel(applicationInfo);
			if (appName != null)
				return appName.toString();

		}
		return null;
	}

	public static String getAppVersionName(Context context, String packageName) {
		String versionName = "0.0.0";
		if (context == null) {
			return versionName;
		}
		PackageManager packageManager = context.getPackageManager();
		try {
			if (packageName == null) {
				packageName = context.getPackageName();
			}
			PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
			versionName = packageInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
		}

		return versionName;
	}

	public static ApplicationInfo getAppInfo(PackageManager pm, String packageName) {
		ApplicationInfo appInfo = null;
		if (pm == null || StringUtils.isBlank(packageName))
			return appInfo;
		try {
			int flag = PackageManager.GET_UNINSTALLED_PACKAGES;
			appInfo = pm.getApplicationInfo(packageName, flag);
		} catch (Exception e) {
		}
		return appInfo;
	}

	public static boolean isInstalledApk(Context context, String packageName) {
		if (context == null || StringUtils.isBlank(packageName)) {
			return false;
		}
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
			if (packageInfo != null) {
				return true;
			}
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
		return false;
	}


}
