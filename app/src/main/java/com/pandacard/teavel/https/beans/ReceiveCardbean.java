package com.pandacard.teavel.https.beans;

public class ReceiveCardbean {
//    id: '123',
//    orderCode: '123456',
//    userId: userMid,
//    userName: '张三',
//    userLogo: 用户头像地址,
//    proName: 产品名称,
//    proLogo:产品logo地址,
//    overdueTime: 过期时间

    private String id;
    private String orderCode;
    private String userId;
    private String userName;
    private String userLogo;
    private String proName;
    private String proLogo;
    private String overdueTime;

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", orderCode='" + orderCode + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userLogo='" + userLogo + '\'' +
                ", proName='" + proName + '\'' +
                ", proLogo='" + proLogo + '\'' +
                ", overdueTime='" + overdueTime + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProLogo() {
        return proLogo;
    }

    public void setProLogo(String proLogo) {
        this.proLogo = proLogo;
    }

    public String getOverdueTime() {
        return overdueTime;
    }

    public void setOverdueTime(String overdueTime) {
        this.overdueTime = overdueTime;
    }

    public ReceiveCardbean(String id, String orderCode, String userId, String userName, String userLogo, String proName, String proLogo, String overdueTime) {
        this.id = id;
        this.orderCode = orderCode;
        this.userId = userId;
        this.userName = userName;
        this.userLogo = userLogo;
        this.proName = proName;
        this.proLogo = proLogo;
        this.overdueTime = overdueTime;
    }
}
