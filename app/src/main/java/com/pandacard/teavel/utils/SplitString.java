package com.pandacard.teavel.utils;

public class SplitString {

    public static  String spStrig(String time) {
        //2020 01 16 10 27 18

        String year = time.substring(0, 4);
        String month = time.substring(4, 6);
        String day = time.substring(6, 8);
        String house = time.substring(8, 10);
        String mins = time.substring(10, 12);
        String second = time.substring(12, 14);

        return year + "-" + month + "-" + day + "   " + house + ":" + mins + ":" + second;


    }
}
