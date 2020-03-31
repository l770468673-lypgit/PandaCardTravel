package com.pandacard.teavel.https.beans;

public class AddressBean {

    public AddressBean(String provinces, String region, String countries, String detailAddress, String phoneNumber,
                       String receiver, String userId, String orderCode) {
        this.provinces = provinces;
        this.region = region;
        this.countries = countries;
        this.detailAddress = detailAddress;
        this.phoneNumber = phoneNumber;
        this.receiver = receiver;
        this.userId = userId;
        this.orderCode = orderCode;
    }

    private  String provinces;
    private  String region;
    private  String countries;
    private  String detailAddress;
    private  String phoneNumber;
    private  String receiver;
    private  String userId;
    private  String orderCode;

    public void setProvinces(String provinces) {
        this.provinces = provinces;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setCountries(String countries) {
        this.countries = countries;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    @Override
    public String toString() {
        return "{" +
                "provinces='" + provinces + '\'' +
                ", region='" + region + '\'' +
                ", countries='" + countries + '\'' +
                ", detailAddress='" + detailAddress + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", receiver='" + receiver + '\'' +
                ", userId='" + userId + '\'' +
                ", orderCode='" + orderCode + '\'' +
                '}';
    }
}

//{ provinces:"北京市",
// region:"北京市",
// countries:"海淀区",
// detailAddress:"123456",
// phoneNumber:"12345678909",
// receiver:"asd",
// userId:"123",
// orderCode:"9753031619011501"}
