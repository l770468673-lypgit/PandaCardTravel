package com.pandacard.teavel.https;

import com.pandacard.teavel.https.beans.ResourcesBean;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Define all rest API with server here Use open source Retrofit for http access
 * http://square.github.io/retrofit/
 */
public interface ClientRestAPI {




    //注册1
    @FormUrlEncoded
    @POST("panda/register")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<ResourcesBean> toRegister(@Field("mobile") String mobile,
                                   @Field("password") String password);

    //panda/wx_register2
    @FormUrlEncoded
    @POST("panda/wx_register")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<ResourcesBean> toWXRegister(@Field("wxid") String wxid, @Field("mobile") String mobile
    );

    //    3.手机号登录
    @FormUrlEncoded
    @POST("panda/login")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<ResourcesBean> toPhoneLogin(@Field("mobile") String mobile,
                                     @Field("password") String password
                                   );
    //   4.WX登录
    @FormUrlEncoded
    @POST("panda/wxlogin")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<ResourcesBean> toWXLogin(@Field("wxid") String wxid);


    //   5.拉取资源信息接口
    @FormUrlEncoded
    @POST("panda/info")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<ResourcesBean> toGetInfo();


    //eid绑定
    @FormUrlEncoded
    @POST("panda/bind")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<ResourcesBean> toeIdBind(@Field("mobile") String mobile,
                                  @Field("eid") String eid);


    // 获取短信吗
    @FormUrlEncoded
    @POST("panda/getSMSCode")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<ResourcesBean> toSMSCode(@Field("mobile") String mobile);


}
