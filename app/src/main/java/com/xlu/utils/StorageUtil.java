package com.xlu.utils;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("DefaultLocale")
@SuppressWarnings("deprecation")
public class StorageUtil {
	private static final int ERROR = -1;
	public static final int MOVE_APP_BEGIN = 1;
	public static final int MOVE_APP_OK = 2;
	public static final int SYSTEM_MOVE_APP = 3;
	public static final int MOVE_APP_FAIL = -6;

	/**
	 * SDCARD是否存G
	 */
	public static boolean externalMemoryAvailable() {
		List<String> sdPaths = getSdPath();
		return null != sdPaths && sdPaths.size() > 0;
	}

	/**
	 *  判断是否有外置sdcard
	 */
	public static boolean isHasExternalSdcard(){
		boolean isHas=false;
		try {
			//tmpfs /storage/sdcard1/.android_secure tmpfs ro,relatime,size=0k,mode=000 0
			// tmpfs  可认为外置sdcard
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec("mount");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			String line;
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				line=line.toLowerCase();
				if(line.contains("tmpfs")&&line.contains("sdcard")){
					isHas=true;
					break;
				}else if(line.contains("extsdcard")||
						line.contains("ext_sd")&&line.contains("/dev/fuse")){
					isHas=true;
					break;
				}else if(getSdPath().size()>=2){
					isHas=true;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isHas;
	}
	
	/**
	 * 获取手机内部剩余存储空间
	 * 
	 * @return
	 */
	public static long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * 获取手机内部总的存储空间
	 * 
	 * @return
	 */
	public static long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	/**
	 * 获取SDCARD剩余存储空间
	 * 
	 * @return
	 */
	public static long getAvailableExternalMemorySize() {
		if (externalMemoryAvailable()) {
			ArrayList<String> pathList=getSdPath();
			long totalSize=0;
			for(String path:pathList){
				StatFs stat = new StatFs(path);
				totalSize+=(long)stat.getBlockSize()*(long)stat.getAvailableBlocks();
			}
			return totalSize;
		} else {
			return ERROR;
		}
	}

	/**
	 * 获取SDCARD总的存储空间
	 * 
	 * @return
	 */
	public static long getTotalExternalMemorySize() {
		if (externalMemoryAvailable()) {
			ArrayList<String> pathList=getSdPath();
			long totalSize=0;
			for(String path:pathList){
				StatFs stat = new StatFs(path);
				totalSize+=(long)stat.getBlockSize()*(long)stat.getBlockCount();
			}
			return totalSize;
		} else {
			return ERROR;
		}
	}

	
	private static ArrayList<String> getSdPath(){
			ArrayList<String> pathList=new ArrayList<String>();
			try {
				Runtime runtime = Runtime.getRuntime();
				Process proc = runtime.exec("mount");
				InputStream is = proc.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				String line;
				BufferedReader br = new BufferedReader(isr);
				while ((line = br.readLine()) != null) {
					if (line.contains("secure"))
						continue;
					if (line.contains("asec"))
						continue;
					if (line.contains("media"))
						continue;
	                if (line.contains("platform"))  
	                    continue; 
	                if (line.contains("system") || line.contains("cache")  
	                        || line.contains("sys") || line.contains("data")  
	                        || line.contains("tmpfs") || line.contains("shell")  
	                        || line.contains("root") || line.contains("acct")  
	                        || line.contains("proc") || line.contains("misc")  
	                        || line.contains("obb")) {  
	                    continue;  
	                }  
					if ((line.contains("fat") || line.contains("fuse"))&&
							line.toLowerCase().contains("sd")) {
						String[] columns = line.split(" ");
						if (!columns[0].contains("/vold")&&!columns[0].contains("/storage")
								&&!columns[0].contains("/dev"))
							continue;
						if (columns != null && columns.length > 1&&
								!pathList.contains(columns[1])) {
							pathList.add(columns[1]);
						}
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pathList;
	}
}
