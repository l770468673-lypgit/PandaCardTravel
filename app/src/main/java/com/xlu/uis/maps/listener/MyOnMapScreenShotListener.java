package com.xlu.uis.maps.listener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.AMap.OnMapScreenShotListener;
import com.xlu.po.Jieshuo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MyOnMapScreenShotListener implements OnMapScreenShotListener {

	private Context context;
	private Jieshuo jieshuo;
	private AppCompatActivity activity;

	public MyOnMapScreenShotListener(Context context, Jieshuo jieshuo, AppCompatActivity activity) {
		this.context = context;
		this.jieshuo = jieshuo;
		this.activity=activity;
	}

	@Override
	public void onMapScreenShot(Bitmap arg0) {

	}

	@Override
	public void onMapScreenShot(Bitmap bitmap, int arg1) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if (null == bitmap) {
			return;
		}
		try {
			String imagePath = Environment.getExternalStorageDirectory()
					+ "/MyLocation" + sdf.format(new Date()) + ".png";
			FileOutputStream fos = new FileOutputStream(imagePath);
			boolean b = bitmap.compress(CompressFormat.PNG, 100, fos);
			try {
				fos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			StringBuffer buffer = new StringBuffer();
			if (b) {
				buffer.append("截屏成功 ");
				goShare(imagePath);
			} else {
				buffer.append("截屏失败 ");
			}
			if (arg1 != 0)
				buffer.append("地图渲染完成，截屏无网格");
			else {
				buffer.append("地图未渲染完成，截屏有网格");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	private void goShare(String path){
//		Intent intent=new Intent(context,ActivityMapShot.class);
//		intent.putExtra("path",path);
//		context.startActivity(intent);
		
	}
}
