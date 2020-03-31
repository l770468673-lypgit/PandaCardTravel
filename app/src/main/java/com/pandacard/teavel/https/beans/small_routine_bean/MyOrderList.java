package com.pandacard.teavel.https.beans.small_routine_bean;

import java.util.List;

public class MyOrderList {


    /**
     * msg : 查询成功
     * orderList : [{"supPrice":1.6,"orderType":0,"buyNum":2,"payTime":"","shopName":"","supOrgName":"熊猫文旅","productName":"熊猫文旅通卡","orderState":4,"remarkCode":"","sourceCode":"","payType":0,"proSalePrice":1,"supOrgMid":"ff8080816f4045af016f6409510a0000","payPrice":1,"supUserName":"熊猫文旅","proPolicyId":""
     * ,"id":"ff808081700eb84b0170e6fd3dba004e","shopId":"","shopOwnerUserName":"","platPrice":0.4,"shopCode":"","cusUserName":"匿名u31712452401","isSettlement":0,"productId":"ff8080816f40542a016f7eee20030004","ticketPrice":0,"shopLogo":"","proUnitPrice":0.01,"productLogo":"https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/09/1578564348276.png","lyzyOrderCode":"9753031713351911","supPolicyId":"ff808081700b1f8801700b22d3d70802","sourceProId":"ff8080816f403bb2016f640f2cf20002","shopOwnerUserMid":"","memberInvalidTime":"","remarkDescribe":"","shopOwnerOrgMid":"","shopOwnerOrgName":"","supUserMid":"ff8080816f4045af016f640951140003","createTime":"2020-03-17 13:35:21","orerCode":"or4040063426704320","cusOrgMid":"ff8080816f4045af016f6409510a0000","cusOrgName":"熊猫文旅","lyzyUserId":"ff80808170e2ea440170e6cf85d80003","orderFrom":2,"cusUserMid":"ff8080816f4045af0170e6cf85bf0044","ticketId":"","isStock":0},{"supPrice":1.6,"orderType":0,"buyNum":2,"payTime":"","shopName":"","supOrgName":"熊猫文旅","productName":"熊猫文旅通卡","orderState":4,"remarkCode":"","sourceCode":"","payType":0,"proSalePrice":1,"supOrgMid":"ff8080816f4045af016f6409510a0000","payPrice":1,"supUserName":"熊猫文旅","proPolicyId":"","id":"ff808081700eb84b0170e6f0a123004d","shopId":"","shopOwnerUserName":"","platPrice":0.4,"shopCode":"","cusUserName":"匿名u31712452401","isSettlement":0,"productId":"ff8080816f40542a016f7eee20030004","ticketPrice":0,"shopLogo":"","proUnitPrice":0.01,"productLogo":"https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/09/1578564348276.png","lyzyOrderCode":"9753031713213207","supPolicyId":"ff808081700b1f8801700b22d3d70802","sourceProId":"ff8080816f403bb2016f640f2cf20002","shopOwnerUserMid":"","memberInvalidTime":"","remarkDescribe":"","shopOwnerOrgMid":"","shopOwnerOrgName":"","supUserMid":"ff8080816f4045af016f640951140003","createTime":"2020-03-17 13:21:34","orerCode":"or4040063426426917","cusOrgMid":"ff8080816f4045af016f6409510a0000","cusOrgName":"熊猫文旅","lyzyUserId":"ff80808170e2ea440170e6cf85d80003","orderFrom":2,"cusUserMid":"ff8080816f4045af0170e6cf85bf0044","ticketId":"","isStock":0}]
     * status : true
     */

    private String msg;
    private boolean status;
    private List<OrderListBean> orderList;

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

    public List<OrderListBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderListBean> orderList) {
        this.orderList = orderList;
    }

    public static class OrderListBean {
        /**
         * supPrice : 1.6
         * orderType : 0
         * buyNum : 2
         * payTime :
         * shopName :
         * supOrgName : 熊猫文旅
         * productName : 熊猫文旅通卡
         * orderState : 4
         * remarkCode :
         * sourceCode :
         * payType : 0
         * proSalePrice : 1
         * supOrgMid : ff8080816f4045af016f6409510a0000
         * payPrice : 1
         * supUserName : 熊猫文旅
         * proPolicyId :
         * id : ff808081700eb84b0170e6fd3dba004e
         * shopId :
         * shopOwnerUserName :
         * platPrice : 0.4
         * shopCode :
         * cusUserName : 匿名u31712452401
         * isSettlement : 0
         * productId : ff8080816f40542a016f7eee20030004
         * ticketPrice : 0
         * shopLogo :
         * proUnitPrice : 0.01
         * productLogo : https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/09/1578564348276.png
         * lyzyOrderCode : 9753031713351911
         * supPolicyId : ff808081700b1f8801700b22d3d70802
         * sourceProId : ff8080816f403bb2016f640f2cf20002
         * shopOwnerUserMid :
         * memberInvalidTime :
         * remarkDescribe :
         * shopOwnerOrgMid :
         * shopOwnerOrgName :
         * supUserMid : ff8080816f4045af016f640951140003
         * createTime : 2020-03-17 13:35:21
         * orerCode : or4040063426704320
         * cusOrgMid : ff8080816f4045af016f6409510a0000
         * cusOrgName : 熊猫文旅
         * lyzyUserId : ff80808170e2ea440170e6cf85d80003
         * orderFrom : 2
         * cusUserMid : ff8080816f4045af0170e6cf85bf0044
         * ticketId :
         * isStock : 0
         */

        private double supPrice;
        private int orderType;
        private int buyNum;
        private String payTime;
        private String shopName;
        private String supOrgName;
        private String productName;
        private int orderState;
        private String remarkCode;
        private String sourceCode;
        private int payType;
        private double proSalePrice;
        private String supOrgMid;
        private double payPrice;
        private String supUserName;
        private String proPolicyId;
        private String id;
        private String shopId;
        private String shopOwnerUserName;
        private double platPrice;
        private String shopCode;
        private String cusUserName;
        private int isSettlement;
        private String productId;
        private double ticketPrice;
        private String shopLogo;
        private double proUnitPrice;
        private String productLogo;
        private String lyzyOrderCode;
        private String supPolicyId;
        private String sourceProId;
        private String shopOwnerUserMid;
        private String memberInvalidTime;
        private String remarkDescribe;
        private String shopOwnerOrgMid;
        private String shopOwnerOrgName;
        private String supUserMid;
        private String createTime;
        private String orerCode;
        private String cusOrgMid;
        private String cusOrgName;
        private String lyzyUserId;
        private int orderFrom;
        private String cusUserMid;
        private String ticketId;
        private int isStock;

        public double getSupPrice() {
            return supPrice;
        }

        public void setSupPrice(double supPrice) {
            this.supPrice = supPrice;
        }

        public int getOrderType() {
            return orderType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
        }

        public int getBuyNum() {
            return buyNum;
        }

        public void setBuyNum(int buyNum) {
            this.buyNum = buyNum;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getSupOrgName() {
            return supOrgName;
        }

        public void setSupOrgName(String supOrgName) {
            this.supOrgName = supOrgName;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getOrderState() {
            return orderState;
        }

        public void setOrderState(int orderState) {
            this.orderState = orderState;
        }

        public String getRemarkCode() {
            return remarkCode;
        }

        public void setRemarkCode(String remarkCode) {
            this.remarkCode = remarkCode;
        }

        public String getSourceCode() {
            return sourceCode;
        }

        public void setSourceCode(String sourceCode) {
            this.sourceCode = sourceCode;
        }

        public int getPayType() {
            return payType;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

        public double getProSalePrice() {
            return proSalePrice;
        }

        public void setProSalePrice(int proSalePrice) {
            this.proSalePrice = proSalePrice;
        }

        public String getSupOrgMid() {
            return supOrgMid;
        }

        public void setSupOrgMid(String supOrgMid) {
            this.supOrgMid = supOrgMid;
        }

        public double getPayPrice() {
            return payPrice;
        }

        public void setPayPrice(int payPrice) {
            this.payPrice = payPrice;
        }

        public String getSupUserName() {
            return supUserName;
        }

        public void setSupUserName(String supUserName) {
            this.supUserName = supUserName;
        }

        public String getProPolicyId() {
            return proPolicyId;
        }

        public void setProPolicyId(String proPolicyId) {
            this.proPolicyId = proPolicyId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getShopOwnerUserName() {
            return shopOwnerUserName;
        }

        public void setShopOwnerUserName(String shopOwnerUserName) {
            this.shopOwnerUserName = shopOwnerUserName;
        }

        public double getPlatPrice() {
            return platPrice;
        }

        public void setPlatPrice(double platPrice) {
            this.platPrice = platPrice;
        }

        public String getShopCode() {
            return shopCode;
        }

        public void setShopCode(String shopCode) {
            this.shopCode = shopCode;
        }

        public String getCusUserName() {
            return cusUserName;
        }

        public void setCusUserName(String cusUserName) {
            this.cusUserName = cusUserName;
        }

        public int getIsSettlement() {
            return isSettlement;
        }

        public void setIsSettlement(int isSettlement) {
            this.isSettlement = isSettlement;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public double getTicketPrice() {
            return ticketPrice;
        }

        public void setTicketPrice(int ticketPrice) {
            this.ticketPrice = ticketPrice;
        }

        public String getShopLogo() {
            return shopLogo;
        }

        public void setShopLogo(String shopLogo) {
            this.shopLogo = shopLogo;
        }

        public double getProUnitPrice() {
            return proUnitPrice;
        }

        public void setProUnitPrice(double proUnitPrice) {
            this.proUnitPrice = proUnitPrice;
        }

        public String getProductLogo() {
            return productLogo;
        }

        public void setProductLogo(String productLogo) {
            this.productLogo = productLogo;
        }

        public String getLyzyOrderCode() {
            return lyzyOrderCode;
        }

        public void setLyzyOrderCode(String lyzyOrderCode) {
            this.lyzyOrderCode = lyzyOrderCode;
        }

        public String getSupPolicyId() {
            return supPolicyId;
        }

        public void setSupPolicyId(String supPolicyId) {
            this.supPolicyId = supPolicyId;
        }

        public String getSourceProId() {
            return sourceProId;
        }

        public void setSourceProId(String sourceProId) {
            this.sourceProId = sourceProId;
        }

        public String getShopOwnerUserMid() {
            return shopOwnerUserMid;
        }

        public void setShopOwnerUserMid(String shopOwnerUserMid) {
            this.shopOwnerUserMid = shopOwnerUserMid;
        }

        public String getMemberInvalidTime() {
            return memberInvalidTime;
        }

        public void setMemberInvalidTime(String memberInvalidTime) {
            this.memberInvalidTime = memberInvalidTime;
        }

        public String getRemarkDescribe() {
            return remarkDescribe;
        }

        public void setRemarkDescribe(String remarkDescribe) {
            this.remarkDescribe = remarkDescribe;
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

        public String getSupUserMid() {
            return supUserMid;
        }

        public void setSupUserMid(String supUserMid) {
            this.supUserMid = supUserMid;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getOrerCode() {
            return orerCode;
        }

        public void setOrerCode(String orerCode) {
            this.orerCode = orerCode;
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

        public String getLyzyUserId() {
            return lyzyUserId;
        }

        public void setLyzyUserId(String lyzyUserId) {
            this.lyzyUserId = lyzyUserId;
        }

        public int getOrderFrom() {
            return orderFrom;
        }

        public void setOrderFrom(int orderFrom) {
            this.orderFrom = orderFrom;
        }

        public String getCusUserMid() {
            return cusUserMid;
        }

        public void setCusUserMid(String cusUserMid) {
            this.cusUserMid = cusUserMid;
        }

        public String getTicketId() {
            return ticketId;
        }

        public void setTicketId(String ticketId) {
            this.ticketId = ticketId;
        }

        public int getIsStock() {
            return isStock;
        }

        public void setIsStock(int isStock) {
            this.isStock = isStock;
        }
    }
}
