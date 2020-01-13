package com.pandacard.teavel.https;

import com.pandacard.teavel.https.beans.ResourcesBean;
import com.pandacard.teavel.https.beans.SecurityCode;
import com.pandacard.teavel.https.beans.bean_person;
import com.pandacard.teavel.https.beans.bindSuccessBean;
import com.pandacard.teavel.https.beans.pandaInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Define all rest API with server here Use open source Retrofit for http access
 * http://square.github.io/retrofit/
 */
public interface ClientRestAPI {


    // 7获取短信吗
    @FormUrlEncoded
    @POST("panda/getSMSCode")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<SecurityCode> toSMSCode(@Field("mobile") String mobile);


    //注册1
    @FormUrlEncoded
    @POST("panda/register")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<SecurityCode> toRegister(@Field("mobile") String mobile,
                                  @Field("password") String password);

    //panda/wx_register2
    @FormUrlEncoded
    @POST("panda/wxRegister")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<SecurityCode> toWXRegister(@Field("wxid") String wxid, @Field("mobile") String mobile);

    //    3.手机号登录
    @FormUrlEncoded
    @POST("panda/login")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<SecurityCode> toPhoneLogin(@Field("mobile") String mobile,
                                    @Field("password") String password
    );

    //   4.WX登录
    @FormUrlEncoded
    @POST("panda/wxLogin")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<SecurityCode> toWXLogin(@Field("wxid") String wxid);


    //   5.拉取资源信息接口
    @FormUrlEncoded
    @POST("panda/info")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<pandaInfo> toGetInfo(@Field("") String s);


    //eid绑定
    @FormUrlEncoded
    @POST("panda/bindEid")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<bindSuccessBean> toeIdBind(@Field("mobile") String mobile,
                                    @Field("eid") String eid);

    @FormUrlEncoded
    @POST("guard/entranceGuard")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<bean_person> QueryIdCard(@Field("actionId") String actionId,
                                  @Field("reqId") String reqId);

    //9.删除熊猫卡接口
    @FormUrlEncoded
    @POST("panda/deleteCard")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<bean_person> deletePCard(@Field("cardCode") String cardCode);


    //8.增加熊猫卡接口
    @FormUrlEncoded
    @POST("panda/addCard")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<bean_person> addCard(@Field("cardCode") String cardCode,
                              @Field("authCode") String authCode);

    //10.根据微信id获取手机号接口
    @FormUrlEncoded
    @POST("panda/getMobiles")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<bean_person> getMobiles(@Field("wxid") String wxid);


    //   11.根据手机号获取所有对应熊猫卡接口
    @FormUrlEncoded
    @POST("panda/getCards")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<bean_person> getCards(@Field("mobile") String mobile);

    //12.根据微信id获取手机号接口
    @FormUrlEncoded
    @POST("panda/bindMobile")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<bindSuccessBean> bindMobile(@Field("cardCode") String cardCode,
                                 @Field("mobile") String mobile
                                 );


}
