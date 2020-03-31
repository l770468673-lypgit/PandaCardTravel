package com.pandacard.teavel.https.beans.small_routine_bean;

public class queryUserOrderCardById {


    /**
     * cardInfo : {"buyNum":3,"cardOrderStatus":3,"createTime":"2020-03-25 14:03:43","giveStatus":1,"id":"ff808081711048500171104a14890001","lastUpdateTime":"2020-03-26 14:15:32","orderCode":"9753032511092701","overdueTime":"2020-03-27 14:03:43","proLogo":"https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/09/1578564348276.png","proName":"熊猫文旅通卡","remark":"","useUserId":"","userId":"ff8080816f89cb95016f8d37e55a0012"}
     * status : true
     * msg : 查询成功
     */

    private CardInfoBean cardInfo;
    private boolean status;
    private String msg;

    public CardInfoBean getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(CardInfoBean cardInfo) {
        this.cardInfo = cardInfo;
    }

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

    public static class CardInfoBean {
        /**
         * buyNum : 3
         * cardOrderStatus : 3
         * createTime : 2020-03-25 14:03:43
         * giveStatus : 1
         * id : ff808081711048500171104a14890001
         * lastUpdateTime : 2020-03-26 14:15:32
         * orderCode : 9753032511092701
         * overdueTime : 2020-03-27 14:03:43
         * proLogo : https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/09/1578564348276.png
         * proName : 熊猫文旅通卡
         * remark :
         * useUserId :
         * userId : ff8080816f89cb95016f8d37e55a0012
         */

        private int buyNum;
        private int cardOrderStatus;
        private String createTime;
        private int giveStatus;
        private String id;
        private String lastUpdateTime;
        private String orderCode;
        private String overdueTime;
        private String proLogo;
        private String proName;
        private String remark;
        private String useUserId;
        private String userId;

        public int getBuyNum() {
            return buyNum;
        }

        public void setBuyNum(int buyNum) {
            this.buyNum = buyNum;
        }

        public int getCardOrderStatus() {
            return cardOrderStatus;
        }

        public void setCardOrderStatus(int cardOrderStatus) {
            this.cardOrderStatus = cardOrderStatus;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getGiveStatus() {
            return giveStatus;
        }

        public void setGiveStatus(int giveStatus) {
            this.giveStatus = giveStatus;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLastUpdateTime() {
            return lastUpdateTime;
        }

        public void setLastUpdateTime(String lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public String getOverdueTime() {
            return overdueTime;
        }

        public void setOverdueTime(String overdueTime) {
            this.overdueTime = overdueTime;
        }

        public String getProLogo() {
            return proLogo;
        }

        public void setProLogo(String proLogo) {
            this.proLogo = proLogo;
        }

        public String getProName() {
            return proName;
        }

        public void setProName(String proName) {
            this.proName = proName;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getUseUserId() {
            return useUserId;
        }

        public void setUseUserId(String useUserId) {
            this.useUserId = useUserId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
