package com.pandacard.teavel.https.beans.small_routine_bean;

public class updateOrderToSuccess {

    /**
     * status : true
     * msg : 修改订单状态成功
     */

    private boolean status;
    private String msg;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
