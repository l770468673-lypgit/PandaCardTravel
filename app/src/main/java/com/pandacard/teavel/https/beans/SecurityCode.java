package com.pandacard.teavel.https.beans;

public class SecurityCode {


    /**
     * msg :
     * code : 1
     * extra : {"vcode":"350763"}
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
         * vcode : 350763
         */

        private String vcode;

        public String getVcode() {
            return vcode;
        }

        public void setVcode(String vcode) {
            this.vcode = vcode;
        }
    }
}
