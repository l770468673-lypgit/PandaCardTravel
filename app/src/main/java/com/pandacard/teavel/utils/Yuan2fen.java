package com.pandacard.teavel.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Yuan2fen {

    public static int changeY2F(double price) {
        DecimalFormat df = new DecimalFormat("#.00");
        price = Double.valueOf(df.format(price));
        int money = (int) (price * 100);
        return money;
    }

    /**
     * 分转元，转换为bigDecimal在toString
     * @return
     */
    public static String changeF2Y(int price) {
        return BigDecimal.valueOf(Long.valueOf(price)).divide(new BigDecimal(100)).toString();
    }


}
