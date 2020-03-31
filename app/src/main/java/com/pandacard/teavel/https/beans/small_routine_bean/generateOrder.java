package com.pandacard.teavel.https.beans.small_routine_bean;

public class generateOrder {

    /**
     * msg : 生成订单成功
     * status : true
     * data : {"commission":"1","orderCode":"9753031713213207","status":true}
     */

    private String msg;
    private boolean status;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * commission : 1
         * orderCode : 9753031713213207
         * status : true
         */

        private String commission;
        private String orderCode;
        private boolean status;

        public String getCommission() {
            return commission;
        }

        public void setCommission(String commission) {
            this.commission = commission;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }
}
