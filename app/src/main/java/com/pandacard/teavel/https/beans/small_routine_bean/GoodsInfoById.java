package com.pandacard.teavel.https.beans.small_routine_bean;

import java.io.Serializable;
import java.util.List;

public class GoodsInfoById implements Serializable {

    /**
     * msg : 查询成功
     * goodsInfo : {"goodsSellingPrice":0.01,"sellTotal":0,"taxName":"",
     * "goodsState":3,
     * "bannerList":["https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/02/1577947266766.jpg",
     * "https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/02/1577947270139.jpg"],
     * "goodsTotal":100,"merchantId":"ff8080816f403bb2016f640bd7c40001","liquidClassify":2,
     * "goodsUnit":"张","id":"ff8080816f403bb2016f640f2cf20002","goodsName":"熊猫文旅通卡",
     * "goodsDesc":"https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/02/1577931588321.jpg",
     * "remarkMt":"","defaultCode":"","fullState":0,"productId":"ff8080816f40542a016f7eee20030004",
     * "descImgList":["https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/02/1577931588321.jpg"],
     * "goodsCostPrice":0.01,"remarkXc":"","taxRate":"","isSetTop":1,"createTime":"2020-01-02 10:21:54",
     * "goodsBanner":"https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/02/1577947266766.jpg,
     * https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/02/1577947270139.jpg","isRefund":0,
     * "goodsIntroduce":"出行交通全国通用，让出行更深省心","goodsCode":"gc4040020420430602","goodsLogo"
     * :"https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/09/1578564348276.png"}
     * status : true
     */

    private String msg;
    private GoodsInfoBean goodsInfo;
    private boolean status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public GoodsInfoBean getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(GoodsInfoBean goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    @Override
    public String toString() {
        return "GoodsInfoById{" +
                "msg='" + msg + '\'' +
                ", goodsInfo=" + goodsInfo +
                ", status=" + status +
                '}';
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static class GoodsInfoBean  implements Serializable {
        /**
         * goodsSellingPrice : 0.01
         * sellTotal : 0
         * taxName :
         * goodsState : 3
         * bannerList : ["https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/02/1577947266766.jpg","https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/02/1577947270139.jpg"]
         * goodsTotal : 100
         * merchantId : ff8080816f403bb2016f640bd7c40001
         * liquidClassify : 2
         * goodsUnit : 张
         * id : ff8080816f403bb2016f640f2cf20002
         * goodsName : 熊猫文旅通卡
         * goodsDesc : https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/02/1577931588321.jpg
         * remarkMt :
         * defaultCode :
         * fullState : 0
         * productId : ff8080816f40542a016f7eee20030004
         * descImgList : ["https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/02/1577931588321.jpg"]
         * goodsCostPrice : 0.01
         * remarkXc :
         * taxRate :
         * isSetTop : 1
         * createTime : 2020-01-02 10:21:54
         * goodsBanner : https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/02/1577947266766.jpg,https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/02/1577947270139.jpg
         * isRefund : 0
         * goodsIntroduce : 出行交通全国通用，让出行更深省心
         * goodsCode : gc4040020420430602
         * goodsLogo : https://dfs.ileyouzunyi.com/down/toImg.jspx?url=/2020/01/09/1578564348276.png
         */

        private double goodsSellingPrice;
        private int sellTotal;
        private String taxName;
        private int goodsState;
        private int goodsTotal;
        private String merchantId;
        private int liquidClassify;
        private String goodsUnit;
        private String id;
        private String goodsName;
        private String goodsDesc;
        private String remarkMt;
        private String defaultCode;
        private int fullState;
        private String productId;
        private double goodsCostPrice;
        private String remarkXc;
        private String taxRate;
        private int isSetTop;
        private String createTime;
        private String goodsBanner;
        private int isRefund;
        private String goodsIntroduce;
        private String goodsCode;
        private String goodsLogo;
        private List<String> bannerList;
        private List<String> descImgList;


        @Override
        public String toString() {
            return "GoodsInfoBean{" +
                    "goodsSellingPrice=" + goodsSellingPrice +
                    ", sellTotal=" + sellTotal +
                    ", taxName='" + taxName + '\'' +
                    ", goodsState=" + goodsState +
                    ", goodsTotal=" + goodsTotal +
                    ", merchantId='" + merchantId + '\'' +
                    ", liquidClassify=" + liquidClassify +
                    ", goodsUnit='" + goodsUnit + '\'' +
                    ", id='" + id + '\'' +
                    ", goodsName='" + goodsName + '\'' +
                    ", goodsDesc='" + goodsDesc + '\'' +
                    ", remarkMt='" + remarkMt + '\'' +
                    ", defaultCode='" + defaultCode + '\'' +
                    ", fullState=" + fullState +
                    ", productId='" + productId + '\'' +
                    ", goodsCostPrice=" + goodsCostPrice +
                    ", remarkXc='" + remarkXc + '\'' +
                    ", taxRate='" + taxRate + '\'' +
                    ", isSetTop=" + isSetTop +
                    ", createTime='" + createTime + '\'' +
                    ", goodsBanner='" + goodsBanner + '\'' +
                    ", isRefund=" + isRefund +
                    ", goodsIntroduce='" + goodsIntroduce + '\'' +
                    ", goodsCode='" + goodsCode + '\'' +
                    ", goodsLogo='" + goodsLogo + '\'' +
                    ", bannerList=" + bannerList +
                    ", descImgList=" + descImgList +
                    '}';
        }

        public double getGoodsSellingPrice() {
            return goodsSellingPrice;
        }

        public void setGoodsSellingPrice(double goodsSellingPrice) {
            this.goodsSellingPrice = goodsSellingPrice;
        }

        public int getSellTotal() {
            return sellTotal;
        }

        public void setSellTotal(int sellTotal) {
            this.sellTotal = sellTotal;
        }

        public String getTaxName() {
            return taxName;
        }

        public void setTaxName(String taxName) {
            this.taxName = taxName;
        }

        public int getGoodsState() {
            return goodsState;
        }

        public void setGoodsState(int goodsState) {
            this.goodsState = goodsState;
        }

        public int getGoodsTotal() {
            return goodsTotal;
        }

        public void setGoodsTotal(int goodsTotal) {
            this.goodsTotal = goodsTotal;
        }

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public int getLiquidClassify() {
            return liquidClassify;
        }

        public void setLiquidClassify(int liquidClassify) {
            this.liquidClassify = liquidClassify;
        }

        public String getGoodsUnit() {
            return goodsUnit;
        }

        public void setGoodsUnit(String goodsUnit) {
            this.goodsUnit = goodsUnit;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getGoodsDesc() {
            return goodsDesc;
        }

        public void setGoodsDesc(String goodsDesc) {
            this.goodsDesc = goodsDesc;
        }

        public String getRemarkMt() {
            return remarkMt;
        }

        public void setRemarkMt(String remarkMt) {
            this.remarkMt = remarkMt;
        }

        public String getDefaultCode() {
            return defaultCode;
        }

        public void setDefaultCode(String defaultCode) {
            this.defaultCode = defaultCode;
        }

        public int getFullState() {
            return fullState;
        }

        public void setFullState(int fullState) {
            this.fullState = fullState;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public double getGoodsCostPrice() {
            return goodsCostPrice;
        }

        public void setGoodsCostPrice(double goodsCostPrice) {
            this.goodsCostPrice = goodsCostPrice;
        }

        public String getRemarkXc() {
            return remarkXc;
        }

        public void setRemarkXc(String remarkXc) {
            this.remarkXc = remarkXc;
        }

        public String getTaxRate() {
            return taxRate;
        }

        public void setTaxRate(String taxRate) {
            this.taxRate = taxRate;
        }

        public int getIsSetTop() {
            return isSetTop;
        }

        public void setIsSetTop(int isSetTop) {
            this.isSetTop = isSetTop;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getGoodsBanner() {
            return goodsBanner;
        }

        public void setGoodsBanner(String goodsBanner) {
            this.goodsBanner = goodsBanner;
        }

        public int getIsRefund() {
            return isRefund;
        }

        public void setIsRefund(int isRefund) {
            this.isRefund = isRefund;
        }

        public String getGoodsIntroduce() {
            return goodsIntroduce;
        }

        public void setGoodsIntroduce(String goodsIntroduce) {
            this.goodsIntroduce = goodsIntroduce;
        }

        public String getGoodsCode() {
            return goodsCode;
        }

        public void setGoodsCode(String goodsCode) {
            this.goodsCode = goodsCode;
        }

        public String getGoodsLogo() {
            return goodsLogo;
        }

        public void setGoodsLogo(String goodsLogo) {
            this.goodsLogo = goodsLogo;
        }

        public List<String> getBannerList() {
            return bannerList;
        }

        public void setBannerList(List<String> bannerList) {
            this.bannerList = bannerList;
        }

        public List<String> getDescImgList() {
            return descImgList;
        }

        public void setDescImgList(List<String> descImgList) {
            this.descImgList = descImgList;
        }
    }
}
