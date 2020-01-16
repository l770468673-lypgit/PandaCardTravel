package com.pandacard.teavel.https.beans;

import java.util.List;

public class CaerdOrderDetalBean {


    /**
     * msg : 获取成功
     * code : 1
     * extra : [{"amount":1,"orderTime":"20200115155532","orderId":"20011515552597044041","cardCode":"3104895199900000100","id":2,"status":0},{"amount":1,"orderTime":"20200115155801","orderId":"20011515575512151072","cardCode":"3104895199900000100","id":3,"status":0},{"amount":1,"orderTime":"20200115160044","orderId":"20011516003831480765","cardCode":"3104895199900000100","id":4,"status":0},{"amount":1,"orderTime":"20200115160205","orderId":"20011516015858339241","cardCode":"3104895199900000100","id":5,"status":0},{"amount":1,"orderTime":"20200115160517","orderId":"20011516051182085474","cardCode":"3104895199900000100","id":6,"status":0},{"amount":1,"orderTime":"20200115160857","orderId":"20011516085080347482","cardCode":"3104895199900000100","id":7,"status":0}]
     */

    private String msg;
    private int code;
    private List<ExtraBean> extra;

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

    public List<ExtraBean> getExtra() {
        return extra;
    }

    public void setExtra(List<ExtraBean> extra) {
        this.extra = extra;
    }

    public static class ExtraBean {
        /**
         * amount : 1
         * orderTime : 20200115155532
         * orderId : 20011515552597044041
         * cardCode : 3104895199900000100
         * id : 2
         * status : 0
         */

        private int amount;
        private String orderTime;
        private String orderId;
        private String cardCode;
        private int id;
        private int status;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getCardCode() {
            return cardCode;
        }

        public void setCardCode(String cardCode) {
            this.cardCode = cardCode;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
