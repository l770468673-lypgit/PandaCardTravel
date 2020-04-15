package com.pandacard.teavel.https.beans.small_routine_bean;

import java.io.Serializable;

public class queryAddressInfoById implements Serializable{


    /**
     * addressInfo : {"countries":"8552","
     * createTime":"2020-04-01 16:30:45",
     * "detailAddress":"8852",
     * "id":"ff808081713466a5017134dd3b3a0006","
     * isDefault":2,"phoneNumber":"9963",
     * "provinces":"9963",
     * "receiver":"9963",
     * "region":"9852","userId":"ff8080816f89cb95016f8d37e55a0012"}
     * status : true
     * msg : 查询成功
     */

    private AddressInfoBean addressInfo;
    private boolean status;
    private String msg;

    public AddressInfoBean getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfoBean addressInfo) {
        this.addressInfo = addressInfo;
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

    public static class AddressInfoBean  implements Serializable {
        /**
         * countries : 8552
         * createTime : 2020-04-01 16:30:45
         * detailAddress : 8852
         * id : ff808081713466a5017134dd3b3a0006
         * isDefault : 2
         * phoneNumber : 9963
         * provinces : 9963
         * receiver : 9963
         * region : 9852
         * userId : ff8080816f89cb95016f8d37e55a0012
         */

        private String countries;
        private String createTime;
        private String detailAddress;
        private String id;
        private int isDefault;
        private String phoneNumber;
        private String provinces;
        private String receiver;
        private String region;
        private String userId;

        public String getCountries() {
            return countries;
        }

        public void setCountries(String countries) {
            this.countries = countries;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDetailAddress() {
            return detailAddress;
        }

        public void setDetailAddress(String detailAddress) {
            this.detailAddress = detailAddress;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(int isDefault) {
            this.isDefault = isDefault;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getProvinces() {
            return provinces;
        }

        public void setProvinces(String provinces) {
            this.provinces = provinces;
        }

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
