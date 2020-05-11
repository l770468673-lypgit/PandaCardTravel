package com.xlu.utils.servers;

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


/**
 * Created by giant on 2017/7/21.
 */

public class BluetoothServiceImp2 extends Service {

    private Context context;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean isFirst = true;

    private Handler handle1 = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            Object[] obj = SystemManger.getNearestEqu();
            String address = (String) obj[0];
            Float dis = (Float) obj[1];
            if(!address.equals("") && address != null){
                Intent intent = new Intent(Constance.FIND_BLUE_TOOTH);
                intent.putExtra("address", address);
                intent.putExtra("dis", dis);
                sendBroadcast(intent);
            }
            sendEmptyMessageDelayed(1, 8000);
            start();
            unregisterReceiver(mReceiver);
            IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
            mFilter.addAction(BluetoothDevice.ACTION_FOUND);
            mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED );
            registerReceiver(mReceiver, mFilter);
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        context = this;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        mFilter.addAction(BluetoothDevice.ACTION_FOUND);
        mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED );
        registerReceiver(mReceiver, mFilter);
        if(!mBluetoothAdapter.isEnabled())
            mBluetoothAdapter.enable();
        else
            start();
        super.onCreate();
    }


    public void start() {
        mBluetoothAdapter.cancelDiscovery();
        boolean iscan = mBluetoothAdapter.startDiscovery();
        if(!iscan)
            start();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                if(isFirst){
                    handle1.sendEmptyMessageDelayed(1, 8000);
                    isFirst = false;
                }
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                if (name == null || name.contains(Constance.BLUETOTH_NAME)) {
                    float rssi = (float)intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                    SystemManger.setEqus(device.getAddress(), rssi);
                }
            }
            else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                start();
                unregisterReceiver(mReceiver);
                IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
                mFilter.addAction(BluetoothDevice.ACTION_FOUND);
                mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED );
                registerReceiver(mReceiver, mFilter);
            }
            else if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                if(state == BluetoothAdapter.STATE_ON){
                    start();
                    unregisterReceiver(mReceiver);
                    IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
                    mFilter.addAction(BluetoothDevice.ACTION_FOUND);
                    mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                    mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED );
                    registerReceiver(mReceiver, mFilter);
                }
                else {
                    mBluetoothAdapter.enable();
                }
            }
        }
    };

    private void stop() {
        unregisterReceiver(mReceiver);
        mBluetoothAdapter.cancelDiscovery();
    }

    public void onDestroy() {
        stop();
        handle1.removeMessages(1);
        super.onDestroy();
    }

}
