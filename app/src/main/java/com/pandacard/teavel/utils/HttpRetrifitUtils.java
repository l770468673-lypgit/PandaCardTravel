package com.pandacard.teavel.utils;

import android.app.Activity;
import android.content.Context;

import com.pandacard.teavel.R;
import com.pandacard.teavel.https.HttpCallback;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.SecurityCode;
import com.pandacard.teavel.https.beans.resetPass;
import com.pandacard.teavel.https.beans.small_routine_bean.GoodsInfoById;
import com.pandacard.teavel.https.beans.small_routine_bean.regAppUser;
import com.pandacard.teavel.uis.LoginActivity;
import com.pandacard.teavel.uis.RegistActivity;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HTTP;


public class HttpRetrifitUtils {


    private static String TAG = "HttpRetrifitUtils";
    public static final String SERNAME_PHONE = "username_phone";
    public static final String SERNAME_PASS = "username_pass";
    public static final String WXLOGIN_UNID = "wxlogin_unid";
    public static final String APPISlOGIN = "appislogin";
    public static final String CARDISBIND = "cardisbind";
    public static final String DEFAULTCARDISBIND = "defaultcardisbind";

    public static final String ACT_TITLENAME = "act_titlename";
    public static final String WECHAT_USERID = "wechat_userid";
    public static final String ORGMEMBERID = "orgmemberid";
    public static final String USERMEMBERID = "UserMemberId";

    public static final String ACT_BUNUM = "act_bunum";
    public static final String vxAPPID = "wx066b02355bf9f39b";


    public static final int STATE_ALL = -1;
    public static final int STATE_NOTPAY = 0;
    public static final int STATE_NOTSEND = 1;
    public static final int STATE_NOTRECEIV = 2;
    public static final int STATE_FINISH = 3;
    public static regAppUser mMBody;
    public static SecurityCode mBody;


    /**
     * 手机号码注册
     */
    public static void toPhoneRegist(final String ph, final String pass, final Activity conttext) {
        Call<SecurityCode> registBeanCall = HttpManager.getInstance()
                .getHttpClient().toPhoneRegister(ph, pass);

        registBeanCall.enqueue(new Callback<SecurityCode>() {
            @Override
            public void onResponse(Call<SecurityCode> call, Response<SecurityCode> response) {
                if (response.body() != null) {

                    SecurityCode body = response.body();
                    ToastUtils.showToast(conttext, body.getMsg());
                    if (response.body().getCode() == 1) {
                        HttpRetrifitUtils.toPhoneLogin(ph, pass, conttext);
                        ToastUtils.showToast(conttext, "toLogin");

                    } else {
                        ToastUtils.showToast(conttext, response.body().getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<SecurityCode> call, Throwable t) {

            }
        });
    }

    /**
     * 微信注册
     */
    public static void WX_Regist(final String wxid, String phNb, final Activity context) {

        Call<SecurityCode> resourcesBeanCall = HttpManager.getInstance().getHttpClient().toWXRegister(wxid, phNb);
        resourcesBeanCall.enqueue(new Callback<SecurityCode>() {
            @Override
            public void onResponse(Call<SecurityCode> call, Response<SecurityCode> response) {
                if (response.body() != null) {
                    if (response.body().getCode() == 1 || response.body().getCode() == 2) {
                        ToastUtils.showToast(context, response.body().getMsg());
                        SecurityCode securityCode = HttpRetrifitUtils.toWXlogin(wxid, context);
                        if (securityCode.getCode() == 0) {
                            context.finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SecurityCode> call, Throwable t) {

            }
        });
    }

    /**
     * c重置密码
     */
    public static void ResetPassword(final String phone, final String pass, final Activity context) {
        Call<resetPass> resetPassCall = HttpManager.getInstance()
                .getHttpClient().resetPassword(
                        phone,
                        pass);
        resetPassCall.enqueue(new Callback<resetPass>() {
            @Override
            public void onResponse(Call<resetPass> call, Response<resetPass> response) {
                resetPass body = response.body();
                if (body != null) {
                    int code = body.getCode();
                    if (code == 1) {
                        HttpRetrifitUtils.toPhoneLogin(phone, pass, context);
                        ToastUtils.showToast(context, "已登录");
                    }

                }
            }

            @Override
            public void onFailure(Call<resetPass> call, Throwable t) {

            }
        });
    }

    /**
     * app  手机登录
     */
    public static SecurityCode toPhoneLogin(final String phone, final String pass, final Activity context) {
        Call<SecurityCode> resourcesBeanCall = HttpManager.getInstance().getHttpClient().toPhoneLogin(phone, pass);
        resourcesBeanCall.enqueue(new Callback<SecurityCode>() {
            @Override
            public void onResponse(Call<SecurityCode> call, Response<SecurityCode> response) {
                if (response.body() != null) {
                    mBody = response.body();
                    if (response.body().getCode() == 1 || response.body().getCode() == 2) {
                        ShareUtil.putString(HttpRetrifitUtils.APPISlOGIN, "login");
                        ShareUtil.putString(HttpRetrifitUtils.SERNAME_PHONE, phone);
                        ShareUtil.putString(HttpRetrifitUtils.SERNAME_PASS, pass);
                        ToastUtils.showToast(context, response.body().getMsg());
                        context.finish();
                    } else {
                        ToastUtils.showToast(context, response.body().getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<SecurityCode> call, Throwable t) {

            }
        });
        return mBody;
    }

    /**
     * app 手机号码 登录
     */
    public static SecurityCode toLogin(final String phone, final String pass, final Activity context) {
        Call<SecurityCode> resourcesBeanCall = HttpManager.getInstance().getHttpClient().toPhoneLogin(phone, pass);
        resourcesBeanCall.enqueue(new Callback<SecurityCode>() {
            @Override
            public void onResponse(Call<SecurityCode> call, Response<SecurityCode> response) {
                if (response.body() != null) {
                    if (response.body().getCode() == 1) {
                        mBody = response.body();
                        ShareUtil.putString(HttpRetrifitUtils.SERNAME_PHONE, phone);
                        ShareUtil.putString(HttpRetrifitUtils.SERNAME_PASS, pass);
                        ShareUtil.putString(HttpRetrifitUtils.APPISlOGIN, "login");
                        ToastUtils.showToast(context, response.body().getMsg());
                        //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        //startActivity(intent);
                        context.finish();
                    } else {
                        ToastUtils.showToast(context, response.body().getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<SecurityCode> call, Throwable t) {
            }
        });

        return mBody;
    }



    /**
     * app  wechat 登录
     */
    public static SecurityCode toWXlogin(final String wxid, final Activity context) {

        Call<SecurityCode> resourcesBeanCall = HttpManager.getInstance().getHttpClient().toWXLogin(wxid);
        resourcesBeanCall.enqueue(new Callback<SecurityCode>() {
            @Override
            public void onResponse(Call<SecurityCode> call, Response<SecurityCode> response) {
                if (response.body() != null) {
                    mBody = response.body();
                    ToastUtils.showToast(context, response.body().getMsg());
                    ShareUtil.putString(HttpRetrifitUtils.WXLOGIN_UNID, wxid);
                    ShareUtil.putString(HttpRetrifitUtils.APPISlOGIN, "login");
                    //userMemberId
                    SecurityCode.ExtraBean extra = response.body().getExtra();

                    LUtils.d(TAG, "toWXlogin=wxid == =" + wxid);
                    context.finish();
                }
            }

            @Override
            public void onFailure(Call<SecurityCode> call, Throwable t) {

            }
        });

        return mBody;
    }

    /**
     * 小程序注册
     */
    public static regAppUser toWchatReg(String a, String b) {

        Call<regAppUser> resetPassCall = HttpManager.getInstance().getHttpClient3().registerAppUser(a, b);
        resetPassCall.enqueue(new Callback<regAppUser>() {
            @Override
            public void onResponse(Call<regAppUser> call, Response<regAppUser> response) {
                if (response.body() != null) {
                    regAppUser body = response.body();
                    ShareUtil.putString(HttpRetrifitUtils.WECHAT_USERID, body.getUserInfo().getId());
                    ShareUtil.putString(HttpRetrifitUtils.ORGMEMBERID, body.getUserInfo().getOrgMemberId());
                    ShareUtil.putString(HttpRetrifitUtils.USERMEMBERID, body.getUserInfo().getUserMemberId());

                }
            }

            @Override
            public void onFailure(Call<regAppUser> call, Throwable t) {
            }
        });
        return mMBody;
    }


}
