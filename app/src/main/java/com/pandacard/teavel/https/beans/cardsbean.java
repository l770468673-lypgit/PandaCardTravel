package com.pandacard.teavel.https.beans;

public class cardsbean {


    /**
     * msg : 获取成功
     * code : 1
     * extra : {"cards":"3104895199900000100"}
     */

    private String msg;
    private int code;
    private ExtraBean extra;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

    public static class ExtraBean {
        /**
         * cards : 3104895199900000100
         */

        private String cards;

        public String getCards() {
            return cards;
        }

        public void setCards(String cards) {
            this.cards = cards;
        }
    }
}
