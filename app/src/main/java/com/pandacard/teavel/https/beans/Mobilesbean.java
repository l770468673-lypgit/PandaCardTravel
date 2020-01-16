package com.pandacard.teavel.https.beans;

public class Mobilesbean {


    /**
     * msg : 获取成功
     * code : 1
     * extra : {"mobiles":""}
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
         * mobiles :
         */

        private String mobiles;

        public String getMobiles() {
            return mobiles;
        }

        public void setMobiles(String mobiles) {
            this.mobiles = mobiles;
        }
    }
}
