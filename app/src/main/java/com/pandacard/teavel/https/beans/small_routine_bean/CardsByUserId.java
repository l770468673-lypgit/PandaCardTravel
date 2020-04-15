package com.pandacard.teavel.https.beans.small_routine_bean;

import java.util.List;

public class CardsByUserId {


    /**
     * cardList : [{"buyNum":3,"cardOrderStatus":"1",
     * "giveStatus":""
     * ,"createTime":"2020-03-25 02:03:30",
     * "orderCode":"9753032514472502",
     * "remark":"",
     * "proLogo":"https://dfs.ileyouzunyi.com/down/toImg.jspx?
     * url=/2020/01/09/1578564348276.png",
     * "id":"ff80808171105262017110722d0c0009",
     * "proName":"熊猫文旅通卡",
     * "userId":"ff8080816f89cb95016f8d37e55a0012",
     * "useUserId":"",
     * "lastUpdateTime":"2020-03-25 02:03:30"},
     * {"buyNum":3,"cardOrderStatus":"1","giveStatus":"","createTime":"2020-03-25 02:03:14","orderCode":"9753032511092701","remark":"","proLogo":"https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/09/1578564348276.png","id":"ff8080817110526201711052cc960001","proName":"熊猫文旅通卡","userId":"ff8080816f89cb95016f8d37e55a0012","useUserId":"","lastUpdateTime":"2020-03-25 02:03:14"},{"buyNum":3,"cardOrderStatus":"1","giveStatus":"","createTime":"2020-03-25 02:03:43","orderCode":"9753032511092701","remark":"","proLogo":"https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/09/1578564348276.png","id":"ff808081711048500171104a14890001","proName":"熊猫文旅通卡","userId":"ff8080816f89cb95016f8d37e55a0012","useUserId":"","lastUpdateTime":"2020-03-25 02:03:43"},{"buyNum":1,"cardOrderStatus":"1","giveStatus":"","createTime":"2020-03-25 11:03:07","orderCode":"9753032511250403","remark":"","id":"ff808081710a7e8501710fb8e1aa0004","userId":"ff8080816f89cb95016f8d37e55a0012","useUserId":"","lastUpdateTime":"2020-03-25 11:03:07"},{"buyNum":1,"cardOrderStatus":"1","giveStatus":"","createTime":"2020-03-25 11:03:03","orderCode":"9753032511190202","remark":"","id":"ff808081710a7e8501710fb355d20002","userId":"ff8080816f89cb95016f8d37e55a0012","useUserId":"","lastUpdateTime":"2020-03-25 11:03:03"},{"buyNum":3,"cardOrderStatus":"1","giveStatus":"","createTime":"2020-03-25 11:03:30","orderCode":"9753032511092701","remark":"","id":"ff808081710a7e8501710faa95530000","userId":"ff8080816f89cb95016f8d37e55a0012","useUserId":"","lastUpdateTime":"2020-03-25 11:03:30"}]
     * msg : 查询成功
     * status : true
     */

    private String msg;
    private boolean status;
    private List<CardListBean> cardList;

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

    public List<CardListBean> getCardList() {
        return cardList;
    }

    public void setCardList(List<CardListBean> cardList) {
        this.cardList = cardList;
    }

    public static class CardListBean {


        @Override
        public String toString() {
            return "CardListBean{" +
                    "buyNum=" + buyNum +
                    ", cardOrderStatus=" + cardOrderStatus +
                    ", giveStatus='" + giveStatus + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", orderCode='" + orderCode + '\'' +
                    ", remark='" + remark + '\'' +
                    ", proLogo='" + proLogo + '\'' +
                    ", id='" + id + '\'' +
                    ", proName='" + proName + '\'' +
                    ", userId='" + userId + '\'' +
                    ", useUserId='" + useUserId + '\'' +
                    ", lastUpdateTime='" + lastUpdateTime + '\'' +
                    '}';
        }

        /**
         * buyNum : 3
         * cardOrderStatus : 1
         * giveStatus :
         * createTime : 2020-03-25 02:03:30
         * orderCode : 9753032514472502
         * remark :
         * proLogo : https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/09/1578564348276.png
         * id : ff80808171105262017110722d0c0009
         * proName : 熊猫文旅通卡
         * userId : ff8080816f89cb95016f8d37e55a0012
         * useUserId :
         * lastUpdateTime : 2020-03-25 02:03:30
         */

        private int buyNum;
        private int cardOrderStatus;
        private String giveStatus;
        private String createTime;
        private String orderCode;
        private String remark;
        private String proLogo;
        private String id;
        private String proName;
        private String userId;
        private String useUserId;
        private String lastUpdateTime;

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

        public String getGiveStatus() {
            return giveStatus;
        }

        public void setGiveStatus(String giveStatus) {
            this.giveStatus = giveStatus;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getProLogo() {
            return proLogo;
        }

        public void setProLogo(String proLogo) {
            this.proLogo = proLogo;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProName() {
            return proName;
        }

        public void setProName(String proName) {
            this.proName = proName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUseUserId() {
            return useUserId;
        }

        public void setUseUserId(String useUserId) {
            this.useUserId = useUserId;
        }

        public String getLastUpdateTime() {
            return lastUpdateTime;
        }

        public void setLastUpdateTime(String lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }
    }
}
