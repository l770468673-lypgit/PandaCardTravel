package com.pandacard.teavel.utils;

import java.util.ArrayList;
import java.util.List;

public class UserByteUtils {


    /**
     * 以空格为分界，分割字符串
     * @param msg 输入字符串
     * @return 分割后的字符串列表
     */
    static public List<String> spliteStrWithBlank(String msg) {
        List<String> mlist = new ArrayList<>();
        String[] s = msg.split(",");
        for (int i = 0; i < s.length; i++) {
            if (s[i].length() > 0) {
                mlist.add(s[i]);
            }
        }
        return mlist;
    }


    /**
     * 去除字符串中的空格
     * @param str 输入字符串
     * @return 输出字符串
     */
    static public String eliminateBlankInString(String str) {
        String[] msg = str.split(" ");
        StringBuilder sb = new StringBuilder();
        if (msg != null && msg.length > 0) {
            for (int i = 0; i < msg.length; i++) {
                if (i < msg.length - 1) {
                    sb.append(msg[i]);
                } else {
                    sb.append(msg[i]);
                }
            }
        }
        return sb.toString();
    }



}
