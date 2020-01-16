package com.pandacard.teavel.https.beans;

public class addorderBean {


    /**
     * msg : 添加成功
     * code : 1
     * extra : {}
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
    }
}


