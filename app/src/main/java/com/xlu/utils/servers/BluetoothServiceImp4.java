package com.xlu.utils.servers;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.xlu.sys.SystemManger;
import com.xlu.utils.Constance;

import java.util.Map;




/**
 *
 * 
 * @author qinling
 */
@SuppressLint("NewApi")
public class BluetoothServiceImp4 extends Service {

	private Context context;
	private BluetoothAdapter mBluetoothAdapter;
	private boolean isFirst = true;

	private Handler handle1 = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Map<String, Float> map= SystemManger.getEqus();
			for(String mac_temp:map.keySet()){
			}
			Object[] obj = SystemManger.getNearestEqu();
			String address = (String) obj[0];
			Float dis = (Float) obj[1];
			if(!address.equals("") && address != null){
				Intent intent = new Intent(Constance.FIND_BLUE_TOOTH);
				intent.putExtra("address", address);
				intent.putExtra("dis", dis);
				sendBroadcast(intent);
			}
			sendEmptyMessageDelayed(1, 4000);
		}
	};

	private Handler handle2 = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			start();
			sendEmptyMessageDelayed(1, 6000);
		}
	};


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		stop();
		super.onDestroy();
	}

	@SuppressLint("NewApi")
	public void start() {
		if(leScanCallback != null&&mBluetoothAdapter!=null){

				mBluetoothAdapter.stopLeScan(leScanCallback);
				boolean isCan = mBluetoothAdapter.startLeScan(leScanCallback);

		}
	}

	@SuppressLint("NewApi")
	private BluetoothAdapter.LeScanCallback leScanCallback =
			new BluetoothAdapter.LeScanCallback() {

				public void onLeScan(BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
					// TODO Auto-generated method stub
					String name=device.getName();
					if (name!= null && name.contains(Constance.BLUETOTH_NAME)) {
						SystemManger.setEqus(device.getAddress(),-(float)(Math.abs((rssi))));
						if(isFirst){
							int equitSize=SystemManger.getEqusSize();
							handle1.sendEmptyMessageDelayed(1, 4000);
//						handle1.sendEmptyMessageDelayed(1, 8000);
							isFirst = false;
						}
					}
				}

			};

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		context = this;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(!mBluetoothAdapter.isEnabled()){
			mBluetoothAdapter.enable();
		}
		IntentFilter mFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED );
		registerReceiver(mReceiver, mFilter);
		handle2.sendEmptyMessage(1);
		super.onCreate();
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
				int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
				if(state == BluetoothAdapter.STATE_ON){
					start();
				}
				else {
					mBluetoothAdapter.enable();
					start();
				}
			}
		}

	};
	@SuppressLint("NewApi")
	public void stop() {
		unregisterReceiver(mReceiver);
		handle1.removeMessages(1);
		handle2.removeMessages(1);
		if(leScanCallback != null)
			mBluetoothAdapter.stopLeScan(leScanCallback);
	}

}