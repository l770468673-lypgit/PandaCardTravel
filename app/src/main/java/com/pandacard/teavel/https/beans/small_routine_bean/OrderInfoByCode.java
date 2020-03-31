package com.pandacard.teavel.https.beans.small_routine_bean;

public class OrderInfoByCode {

    /**
     * orderInfo : {"buyNum":1,"createTime":"2020-03-17 14:52:08","cusOrgMid":"ff8080816f4045af016f6409510a0000","cusOrgName":"熊猫文旅","cusUserMid":"ff8080816f4045af01703d2131c00034","cusUserName":"Keep","id":"ff808081700eb84b0170e7438a4a0054","isSettlement":0,"isStock":0,"lyzyOrderCode":"9753031714520613","lyzyUserId":"ff8080816f89cb95016f8d37e55a0012","memberInvalidTime":"","orderFrom":2,"orderState":4,"orderType":0,"orerCode":"or4040063429041729","payPrice":0.01,"payTime":"","payType":0,"platPrice":0,"proPolicyId":"","proSalePrice":0.01,"proUnitPrice":0.01,"productId":"ff8080816f40542a016f7eee20030004","productLogo":"https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/09/1578564348276.png","productName":"熊猫文旅通卡","remarkCode":"","remarkDescribe":"","shopCode":"","shopId":"","shopLogo":"","shopName":"","shopOwnerOrgMid":"","shopOwnerOrgName":"","shopOwnerUserMid":"","shopOwnerUserName":"","sourceCode":"","sourceProId":"ff8080816f403bb2016f640f2cf20002","supOrgMid":"ff8080816f4045af016f6409510a0000","supOrgName":"熊猫文旅","supPolicyId":"ff808081700b1f8801700b22d3d70802","supPrice":0.01,"supUserMid":"ff8080816f4045af016f640951140003","supUserName":"熊猫文旅","ticketId":"","ticketPrice":0}
     * status : true
     * msg : 获取订单详情成功
     */

    private OrderInfoBean orderInfo;
    private boolean status;
    private String msg;

    public OrderInfoBean getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfoBean orderInfo) {
        this.orderInfo = orderInfo;
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

    public static class OrderInfoBean {
        /**
         * buyNum : 1
         * createTime : 2020-03-17 14:52:08
         * cusOrgMid : ff8080816f4045af016f6409510a0000
         * cusOrgName : 熊猫文旅
         * cusUserMid : ff8080816f4045af01703d2131c00034
         * cusUserName : Keep
         * id : ff808081700eb84b0170e7438a4a0054
         * isSettlement : 0
         * isStock : 0
         * lyzyOrderCode : 9753031714520613
         * lyzyUserId : ff8080816f89cb95016f8d37e55a0012
         * memberInvalidTime :
         * orderFrom : 2
         * orderState : 4
         * orderType : 0
         * orerCode : or4040063429041729
         * payPrice : 0.01
         * payTime :
         * payType : 0
         * platPrice : 0
         * proPolicyId :
         * proSalePrice : 0.01
         * proUnitPrice : 0.01
         * productId : ff8080816f40542a016f7eee20030004
         * productLogo : https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/09/1578564348276.png
         * productName : 熊猫文旅通卡
         * remarkCode :
         * remarkDescribe :
         * shopCode :
         * shopId :
         * shopLogo :
         * shopName :
         * shopOwnerOrgMid :
         * shopOwnerOrgName :
         * shopOwnerUserMid :
         * shopOwnerUserName :
         * sourceCode :
         * sourceProId : ff8080816f403bb2016f640f2cf20002
         * supOrgMid : ff8080816f4045af016f6409510a0000
         * supOrgName : 熊猫文旅
         * supPolicyId : ff808081700b1f8801700b22d3d70802
         * supPrice : 0.01
         * supUserMid : ff8080816f4045af016f640951140003
         * supUserName : 熊猫文旅
         * ticketId :
         * ticketPrice : 0
         */

        private int buyNum;
        private String createTime;
        private String cusOrgMid;
        private String cusOrgName;
        private String cusUserMid;
        private String cusUserName;
        private String id;
        private int isSettlement;
        private int isStock;
        private String lyzyOrderCode;
        private String lyzyUserId;
        private String memberInvalidTime;
        private int orderFrom;
        private int orderState;
        private int orderType;
        private String orerCode;
        private double payPrice;
        private String payTime;
        private int payType;
        private double platPrice;
        private String proPolicyId;
        private double proSalePrice;
        private double proUnitPrice;
        private String productId;
        private String productLogo;
        private String productName;
        private String remarkCode;
        private String remarkDescribe;
        private String shopCode;
        private String shopId;
        private String shopLogo;
        private String shopName;
        private String shopOwnerOrgMid;
        private String shopOwnerOrgName;
        private String shopOwnerUserMid;
        private String shopOwnerUserName;
        private String sourceCode;
        private String sourceProId;
        private String supOrgMid;
        private String supOrgName;
        private String supPolicyId;
        private double supPrice;
        private String supUserMid;
        private String supUserName;
        private String ticketId;
        private int ticketPrice;

        public int getBuyNum() {
            return buyNum;
        }

        public void setBuyNum(int buyNum) {
            this.buyNum = buyNum;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCusOrgMid() {
            return cusOrgMid;
        }

        public void setCusOrgMid(String cusOrgMid) {
            this.cusOrgMid = cusOrgMid;
        }

        public String getCusOrgName() {
            return cusOrgName;
        }

        public void setCusOrgName(String cusOrgName) {
            this.cusOrgName = cusOrgName;
        }

        public String getCusUserMid() {
            return cusUserMid;
        }

        public void setCusUserMid(String cusUserMid) {
            this.cusUserMid = cusUserMid;
        }

        public String getCusUserName() {
            return cusUserName;
        }

        public void setCusUserName(String cusUserName) {
            this.cusUserName = cusUserName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getIsSettlement() {
            return isSettlement;
        }

        public void setIsSettlement(int isSettlement) {
            this.isSettlement = isSettlement;
        }

        public int getIsStock() {
            return isStock;
        }

        public void setIsStock(int isStock) {
            this.isStock = isStock;
        }

        public String getLyzyOrderCode() {
            return lyzyOrderCode;
        }

        public void setLyzyOrderCode(String lyzyOrderCode) {
            this.lyzyOrderCode = lyzyOrderCode;
        }

        public String getLyzyUserId() {
            return lyzyUserId;
        }

        public void setLyzyUserId(String lyzyUserId) {
            this.lyzyUserId = lyzyUserId;
        }

        public String getMemberInvalidTime() {
            return memberInvalidTime;
        }

        public void setMemberInvalidTime(String memberInvalidTime) {
            this.memberInvalidTime = memberInvalidTime;
        }

        public int getOrderFrom() {
            return orderFrom;
        }

        public void setOrderFrom(int orderFrom) {
            this.orderFrom = orderFrom;
        }

        public int getOrderState() {
            return orderState;
        }

        public void setOrderState(int orderState) {
            this.orderState = orderState;
        }

        public int getOrderType() {
            return orderType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }

        public String getOrerCode() {
            return orerCode;
        }

        public void setOrerCode(String orerCode) {
            this.orerCode = orerCode;
        }

        public double getPayPrice() {
            return payPrice;
        }

        public void setPayPrice(double payPrice) {
            this.payPrice = payPrice;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public int getPayType() {
            return payType;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

        public double getPlatPrice() {
            return platPrice;
        }

        public void setPlatPrice(int platPrice) {
            this.platPrice = platPrice;
        }

        public String getProPolicyId() {
            return proPolicyId;
        }

        public void setProPolicyId(String proPolicyId) {
            this.proPolicyId = proPolicyId;
        }

        public double getProSalePrice() {
            return proSalePrice;
        }

        public void setProSalePrice(double proSalePrice) {
            this.proSalePrice = proSalePrice;
        }

        public double getProUnitPrice() {
            return proUnitPrice;
        }

        public void setProUnitPrice(double proUnitPrice) {
            this.proUnitPrice = proUnitPrice;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductLogo() {
            return productLogo;
        }

        public void setProductLogo(String productLogo) {
            this.productLogo = productLogo;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getRemarkCode() {
            return remarkCode;
        }

        public void setRemarkCode(String remarkCode) {
            this.remarkCode = remarkCode;
        }

        public String getRemarkDescribe() {
            return remarkDescribe;
        }

        public void setRemarkDescribe(String remarkDescribe) {
            this.remarkDescribe = remarkDescribe;
        }

        public String getShopCode() {
            return shopCode;
        }

        public void setShopCode(String shopCode) {
            this.shopCode = shopCode;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getShopLogo() {
            return shopLogo;
        }

        public void setShopLogo(String shopLogo) {
            this.shopLogo = shopLogo;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopOwnerOrgMid() {
            return shopOwnerOrgMid;
        }

        public void setShopOwnerOrgMid(String shopOwnerOrgMid) {
            this.shopOwnerOrgMid = shopOwnerOrgMid;
        }

        public String getShopOwnerOrgName() {
            return shopOwnerOrgName;
        }

        public void setShopOwnerOrgName(String shopOwnerOrgName) {
            this.shopOwnerOrgName = shopOwnerOrgName;
        }

        public String getShopOwnerUserMid() {
            return shopOwnerUserMid;
        }

        public void setShopOwnerUserMid(String shopOwnerUserMid) {
            this.shopOwnerUserMid = shopOwnerUserMid;
        }

        public String getShopOwnerUserName() {
            return shopOwnerUserName;
        }

        public void setShopOwnerUserName(String shopOwnerUserName) {
            this.shopOwnerUserName = shopOwnerUserName;
        }

        public String getSourceCode() {
            return sourceCode;
        }

        public void setSourceCode(String sourceCode) {
            this.sourceCode = sourceCode;
        }

        public String getSourceProId() {
            return sourceProId;
        }

        public void setSourceProId(String sourceProId) {
            this.sourceProId = sourceProId;
        }

        public String getSupOrgMid() {
            return supOrgMid;
        }

        public void setSupOrgMid(String supOrgMid) {
            this.supOrgMid = supOrgMid;
        }

        public String getSupOrgName() {
            return supOrgName;
        }

        public void setSupOrgName(String supOrgName) {
            this.supOrgName = supOrgName;
        }

        public String getSupPolicyId() {
            return supPolicyId;
        }

        public void setSupPolicyId(String supPolicyId) {
            this.supPolicyId = supPolicyId;
        }

        public double getSupPrice() {
            return supPrice;
        }

        public void setSupPrice(double supPrice) {
            this.supPrice = supPrice;
        }

        public String getSupUserMid() {
            return supUserMid;
        }

        public void setSupUserMid(String supUserMid) {
            this.supUserMid = supUserMid;
        }

        public String getSupUserName() {
            return supUserName;
        }

        public void setSupUserName(String supUserName) {
            this.supUserName = supUserName;
        }

        public String getTicketId() {
            return ticketId;
        }

        public void setTicketId(String ticketId) {
            this.ticketId = ticketId;
        }

        public int getTicketPrice() {
            return ticketPrice;
        }

        public void setTicketPrice(int ticketPrice) {
            this.ticketPrice = ticketPrice;
        }
    }
}
