package com.pandacard.teavel.utils;

import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.ResourcesBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HttpRetrifitUtils {

    //7.请求短信验证码接口
    public void getSMSCodes(String phone) {
        Call<ResourcesBean> resourcesBeanCall = HttpManager.getInstance().getHttpClient().toSMSCode(phone);
        resourcesBeanCall.enqueue(new Callback<ResourcesBean>() {
            @Override
            public void onResponse(Call<ResourcesBean> call, Response<ResourcesBean> response) {
                if (response.body() != null) {

                }
            }

            @Override
            public void onFailure(Call<ResourcesBean> call, Throwable t) {

            }
        });
    }

    //1.手机号注册
    public void toRegist(String phone,String pass){
        HttpManager.getInstance().getHttpClient().toRegister(phone, pass);
    }

    // 2 向微信用户信息表插入数据
    public void toWXRegist(String WXid,String phone){
        HttpManager.getInstance().getHttpClient().toWXRegister(WXid, phone);
    }

    // 3 3.手机号登录
    public void toPhoneLogin(String WXid,String phone){
        HttpManager.getInstance().getHttpClient().toPhoneLogin(WXid, phone);
    }


    //4 微信登录
    public void toWXLogin(String WXid){
        HttpManager.getInstance().getHttpClient().toWXLogin(WXid);
    }
    //5 5.拉取资源信息接口
    public void togetInfo(){
        HttpManager.getInstance().getHttpClient().toGetInfo();
    }
    //eid 绑定
    public void toeIdBind(String phone,String eid){
        HttpManager.getInstance().getHttpClient().toeIdBind(phone,eid);
    }

}
