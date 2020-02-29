package com.xlu.sys;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by giant on 2017/7/21.
 */

public class SystemManger {
    public static Map<String,Float> equs = new HashMap<String,Float >();//附近的蓝牙设备MAP集合
    public static String yuying = "";
    public static boolean isInPayZoneActivity  = false;
    public static Integer freeTimes = 3;//免费试听的次数
    public static String timeId;

    public  static int getEqusSize(){
        return equs.size();
    }
    //giant add
    public static Map<String,Float> getEqus(){
        return equs;

    }

    public static void setEqus(String mac,Float rssi){
        if(equs.containsKey(mac)){
            equs.put(mac, (equs.get(mac) + rssi)/2);
        }
        else{
            equs.put(mac, rssi);
        }
    }

    /**
     * 找到最近的一个设备的MAC
     *
     * @return 最近的一个设备的MAC
     */
    public static Object[] getNearestEqu(){
        String mac = "";
        float rssi = 0f;
        for(String mac1:equs.keySet()){
            if(rssi == 0f || equs.get(mac1) > rssi){
                rssi = equs.get(mac1);
                mac = mac1;
            }
        }
        equs.clear();
        Object[] obj = {mac,getDistance(rssi)};
        return obj;
    }

    public static float getDistance(float rssi){
        float dis = (float) Math.pow(10,(Math.abs(rssi) - 59)/20);
        return dis;
    }
}
