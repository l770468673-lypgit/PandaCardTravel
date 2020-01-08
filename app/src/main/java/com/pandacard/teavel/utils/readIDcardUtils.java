package com.pandacard.teavel.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.guoguang.jni.JniCall;

public class readIDcardUtils {


    public static final String TAG ="readIDcardUtils" ;


    public static String printHexString(byte[] b) throws Exception {
        String bstr = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            bstr = bstr + hex.toUpperCase();
        }
        return bstr;
    }
    public  static Bitmap readByteMap(byte[] pucPHMsg) {
        Bitmap bitmap = null;
        try {
            byte[] bmp = new byte[14 + 40 + 308 * 126];
            int ret1 = JniCall.Huaxu_Wlt2Bmp(pucPHMsg, bmp, 0);
            bitmap = BitmapFactory.decodeByteArray(bmp, 0, bmp.length);
            LUtils.e(TAG, "ret==" + ret1);
            LUtils.e(TAG, "bitmap==" + bitmap.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static boolean isHexStr(String num) {
        if (num == null || "".equals(num)) {
            return false;
        }
        for (int i = 0; i < num.length(); i++) {
            char c = num.charAt(i);
            if ((c < '0' || c > '9') && (c < 'A' || c > 'F') && (c < 'a' || c > 'f')) {
                return false;
            }
        }
        return true;
    }


    public static byte[] hexStr2byte(String hexStr) {
        System.out.println("hexStr2byte");
        if (!isHexStr(hexStr) || hexStr.length() % 2 != 0) {
            System.out.println("不符合十六进制数据");
            return null;
        }
        hexStr = hexStr.toUpperCase();
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();

        byte[] bytes = new byte[hexStr.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            int m = str.indexOf(hexs[2 * i]) << 4;
            int n = str.indexOf(hexs[2 * i + 1]);
            if (m == -1 || n == -1) {//非16进制字符串
                return null;
            }
            bytes[i] = (byte) (m | n);
        }
        return bytes;
    }


}
