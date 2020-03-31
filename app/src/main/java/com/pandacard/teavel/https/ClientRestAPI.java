package com.pandacard.teavel.https;

import com.pandacard.teavel.https.beans.AppUpdate;
import com.pandacard.teavel.https.beans.CaerdOrderDetalBean;
import com.pandacard.teavel.https.beans.small_routine_bean.CardsByUserId;
import com.pandacard.teavel.https.beans.small_routine_bean.GoodsInfoById;
import com.pandacard.teavel.https.beans.Mobilesbean;
import com.pandacard.teavel.https.beans.small_routine_bean.MyAddressByUserId;
import com.pandacard.teavel.https.beans.small_routine_bean.MyOrderList;
import com.pandacard.teavel.https.beans.small_routine_bean.OrderInfoByCode;
import com.pandacard.teavel.https.beans.SecurityCode;
import com.pandacard.teavel.https.beans.addorderBean;
import com.pandacard.teavel.https.beans.bean_addCards;
import com.pandacard.teavel.https.beans.bean_person;
import com.pandacard.teavel.https.beans.bindSuccessBean;
import com.pandacard.teavel.https.beans.cardsbean;
import com.pandacard.teavel.https.beans.small_routine_bean.generateOrder;
import com.pandacard.teavel.https.beans.pandaInfo;
import com.pandacard.teavel.https.beans.small_routine_bean.insertAddressInfo;
import com.pandacard.teavel.https.beans.small_routine_bean.queryUserOrderCardById;
import com.pandacard.teavel.https.beans.small_routine_bean.regAppUser;
import com.pandacard.teavel.https.beans.resetPass;
import com.pandacard.teavel.https.beans.small_routine_bean.updateOrderCardStatusForGive;
import com.pandacard.teavel.https.beans.small_routine_bean.updateOrderCardStatusForRefuse;
import com.pandacard.teavel.https.beans.small_routine_bean.updateOrderToClose;
import com.pandacard.teavel.https.beans.small_routine_bean.updateOrderToSuccess;

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


    // 7获取短信吗
    @FormUrlEncoded
    @POST("panda/getSMSCode")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<SecurityCode> toSMSCode(@Field("mobile") String mobile);


    //注册1
    @FormUrlEncoded
    @POST("panda/register")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<SecurityCode> toPhoneRegister(@Field("mobile") String mobile,
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
    Call<bean_addCards> deletePCard(@Field("cardCode") String cardCode);


    //8.增加熊猫卡接口
    @FormUrlEncoded
    @POST("panda/addCard")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<bean_addCards> addCard(@Field("cardCode") String cardCode,
                                @Field("authCode") String authCode);

    //10.根据微信id获取手机号接口
    @FormUrlEncoded
    @POST("panda/getMobiles")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<Mobilesbean> getMobiles(@Field("wxid") String wxid);


    //   11.根据手机号获取所有对应熊猫卡接口
    @FormUrlEncoded
    @POST("panda/getCards")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<cardsbean> getCards(@Field("mobile") String mobile);

    //    //12.12.绑定手机号接口
//    @FormUrlEncoded
//    @POST("panda/bindMobile")
//    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
//   Call<bindSuccessBean> bindMobile(@Field("cardCode") String cardCode,
//                                     @Field("mobile") String mobile
//    );
    //12.12.绑定手机号接口
    @FormUrlEncoded
    @POST("panda/bindCard")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<bindSuccessBean> mbindCard(@Field("cardCode") String cardCode,
                                    @Field("mobile") String mobile,
                                    @Field("name") String name,
                                    @Field("eid") String eid
    );

    //13.充值后添加订单信息接口
    @FormUrlEncoded
    @POST("panda/addOrder")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<addorderBean> toaddOrder(@Field("orderId") String orderId,
                                  @Field("orderTime") String orderTime,
                                  @Field("amount") String amount,
                                  @Field("cardCode") String cardCode
    );

    //14.修改订单状态接口
    @FormUrlEncoded
    @POST("panda/editOrder")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<addorderBean> toeditOrder(@Field("orderId") String orderId,
                                   @Field("cardCode") String cardCode,
                                   @Field("status") String status);


    //15.获取某个熊猫卡充值所有订单接口

    //14.修改订单状态接口
    @FormUrlEncoded
    @POST("panda/getOrders")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<CaerdOrderDetalBean> getOrders(@Field("cardCode") String cardCode
    );


    //16.获取apk信息接口
    @FormUrlEncoded
    @POST("panda/getAPKInfo")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<AppUpdate> upDateApp(@Field("") String s);

    // 重置密码
    @FormUrlEncoded
    @POST("panda/resetPassword")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<resetPass> resetPassword(@Field("mobile") String mobile,
                                  @Field("password") String password
    );

    // 小程序 api
//    1.用户注册接口
//    接口地址： /foreign/registerAppUser.jspx
    @FormUrlEncoded
    @POST("foreign/registerAppUser.jspx")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<regAppUser> registerAppUser(@Field("phone") String phone,
                                     @Field("password") String password
    );

    ///foreign/getGoodsInfoById.jspx
//    2.产品详情查询接口
    @FormUrlEncoded
    @POST("foreign/getGoodsInfoById.jspx")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<GoodsInfoById> getGoodsInfoById(@Field("goodsId") String goodsId
    );

    ///foreign/getMyOrderList.jspx  3.订单列表查询接口
    @FormUrlEncoded
    @POST("foreign/getMyOrderList.jspx")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<MyOrderList> getMyOrderList(@Field("userId") String userId,
                                     @Field("state") int state
    );

    //4.订单详情查询接口
    //接口地址： /foreign/getOrderInfoByCode.jspx
    @FormUrlEncoded
    @POST("foreign/getOrderInfoByCode.jspx")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<OrderInfoByCode> getOrderInfoByCode(@Field("orderCode") String orderCode
    );

    //5.下单接口
    //接口地址： /foreign/generateOrder.jspx
    @FormUrlEncoded
    @POST("foreign/generateOrder.jspx")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<generateOrder> generateOrder(@Field("userId") String userId,
                                      @Field("productId") String productId,
                                      @Field("unitPrice") double unitPrice,
                                      @Field("orderPrice") double orderPrice,
                                      @Field("totalPrice") double totalPrice,
                                      @Field("couponPrice") double couponPrice,
                                      @Field("couponId") String couponId,
                                      @Field("buyNum") int buyNum,
                                      @Field("payType") int payType
    );

    // /foreign/updateOrderToSuccess.jspx  6.支付成功回调接口
    @FormUrlEncoded
    @POST("foreign/updateOrderToSuccess.jspx")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<updateOrderToSuccess> updateOrderToSuccess(@Field("orderCode") String orderCode
    );


    // TODO: 2020/3/13  777--
    @FormUrlEncoded
    @POST("foreign/insertAddressInfoForSelfPurchase.jspx")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<CardsByUserId> AddressInfoForSelfPurchase(
            @Field("info") String info

    );
//    Call<CardsByUserId> AddressInfoForSelfPurchase(
//            @Field("userId") String userId,
//            @Field("provinces") String provinces,
//            @Field("region") String region,
//            @Field("countries") String countries,
//            @Field("detailAddress") String detailAddress,
//            @Field("receiver") String receiver,
//            @Field("phoneNumber") String phoneNumber
//    );

    //8 /foreign/ getCardsByUserId.jspx
//    8.我的卡包接口
    @FormUrlEncoded
    @POST("foreign/getCardsByUserId.jspx")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<CardsByUserId> getCardsByUserId(
            @Field("userId") String userId,
            @Field("cardOrderStatus") int cardOrderStatus
    );


    /// 9.卡订单详情查询接口
    @FormUrlEncoded
    @POST("foreign/queryUserOrderCardById.jspx")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<queryUserOrderCardById> queryUserOrderCardById(
            @Field("id") String id

    );


    //10.我的收获地址查询接口
    //接口地址： /foreign/getMyAddressByUserId.jspx
    @FormUrlEncoded
    @POST("foreign/getMyAddressByUserId.jspx")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<MyAddressByUserId> getMyAddressByUserId(
            @Field("userId") String userId

    );

    //11.添加我的收获地址
    //接口地址：/foreign/insertAddressInfo.jspx
    @FormUrlEncoded
    @POST("foreign/insertAddressInfo.jspx")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<insertAddressInfo> insertAddressInfo(
            @Field("userId") String userId,
            @Field("provinces") String provinces,
            @Field("region") String region,
            @Field("countries") String countries,
            @Field("detailAddress") String detailAddress,
            @Field("receiver") String receiver,
            @Field("phoneNumber") String phoneNumber,
            @Field("isDefault") int isDefault

    );

//    12.卡订单赠送时自己输入收获地址接口
//    接口地址：/foreign/insertAddressInfoForGive.jspx
// TODO: 2020/3/13


    //13.用户领取赠送熊猫卡接口
    //接口地址：/foreign/insertAddressInfoForReceive.jspx

    //14.用户分享熊猫卡接口
    //接口地址：/foreign/updateOrderCardStatusForGive.jspx
    @FormUrlEncoded
    @POST("foreign/updateOrderCardStatusForGive.jspx")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<updateOrderCardStatusForGive> updateOrderCardStatusForGive(
            @Field("id") String id

    );


    //15.好友拒绝领取熊猫卡接口
    //接口地址：/foreign/updateOrderCardStatusForRefuse.jspx
    @FormUrlEncoded
    @POST("foreign/updateOrderCardStatusForRefuse.jspx")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Call<updateOrderCardStatusForRefuse> updateOrderCardStatusForRefuse(
            @Field("id") String id

    );
//16.取消订单的接口
    ///foreign/updateOrderToClose.jspx
@FormUrlEncoded
@POST("foreign/updateOrderToClose.jspx")
@Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
Call<updateOrderToClose> updateOrderToClose(
        @Field("orderCode") String orderCode

);
}