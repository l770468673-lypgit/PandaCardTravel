package com.xlu.uis;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pandacard.teavel.R;
import com.pandacard.teavel.apps.MyApplication;
import com.xlu.bases.BaseActivity;
import com.xlu.jsutil.JavaScriptObject;
import com.xlu.po.Member;
import com.xlu.po.MyEvent;
import com.xlu.po.ProductSpecal;
import com.xlu.po.Production;
import com.xlu.po.Production1;
import com.xlu.utils.Constance;
import com.xlu.utils.DBUtil;
import com.xlu.utils.JsonUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.com.wideroad.BaseHttp;
import cn.com.wideroad.http.AjaxCallBack;
import cn.com.wideroad.http.AjaxParams;
import cn.com.wideroad.utils.StringUtil;
import cn.sharesdk.onekeyshare.OnekeyShare;
import de.greenrobot.event.EventBus;

public class ActivityXianluDetail extends BaseActivity {
    ImageView ivXLXQ;
    TextView tvTitle;
    TextView tvShare;
    TextView tvRouteCare;
    ImageView ivRouteTicket;
    ImageView ivXiangQing;
    ImageView ivRoadZhuSu;
    ImageView tv_tool_left;
    WebView wv;
    TextView tvDetailItem;
    ProductSpecal ps;
    Production xinLuInfos;
    TextView tvXianLuName;
    //	AMap aMap;
    List<Production1> datapro;
    //	PopWindowLogin pop;
    private List<ProductSpecal> proInfos = new ArrayList<ProductSpecal>();
    SharedPreferences sp;
    Bitmap bitmap;
    String tempFileName;
    boolean isSameJym = true;
    Handler handler;
    private final int permissionsRequestCode = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVIEWww();
        ps = (ProductSpecal) getIntent().getSerializableExtra("zone");
        sp = getSharedPreferences("mimi", Context.MODE_PRIVATE);
        init();
        tvTitle.setText("线路详情");
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setCompoundDrawablePadding(0);
        tvTitle.setCompoundDrawables(null, null, null, null);
        tvShare.setVisibility(View.VISIBLE);
        ivXLXQ.setVisibility(View.VISIBLE);
        if (DBUtil.getLoginMeber() != null && DBUtil.getLoginMeber().getJym() != null) {
            getPersonInfo();
            handler = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 1) {
                        if (isSameJym)
                            initStatus();
                        else {
//							PopWindowLogin pop=new PopWindowLogin(ActivityXianluDetail.this);
//							pop.showAsDropDown(ivXLXQ);
                        }
                    }
                }

            };

        }

    }

    private void initVIEWww() {
        ivXLXQ = findViewById(R.id.tv_tool_left);
        tvTitle = findViewById(R.id.tv_all_title_name);
        tvShare = findViewById(R.id.tv_all_right);
        tvRouteCare = findViewById(R.id.tv_xianlu_guanzhu);
        ivRouteTicket = findViewById(R.id.iv_xianlu_buy_ticket);
        ivXiangQing = findViewById(R.id.iv_xiangqing);
        ivRoadZhuSu = findViewById(R.id.iv_xianlu_zhusu);
        wv = findViewById(R.id.wv_ziyouxing);
        tv_tool_left = findViewById(R.id.tv_tool_left);
        tvDetailItem = findViewById(R.id.tv_route_memo);
        tvXianLuName = findViewById(R.id.tv_road_name);
        ImageView iv_xianlu_food = findViewById(R.id.iv_xianlu_food);
        ImageView iv_xianlu_zhusu = findViewById(R.id.iv_xianlu_zhusu);
        tv_tool_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }

    private void getPersonInfo() {
        BaseHttp http = new BaseHttp();
        AjaxParams params = new AjaxParams();
        params.put("jym", sp.getString("jym", ""));
        http.post(Constance.HTTP_REQUEST_URL_BIZ + "getMemberInfo", params,
                new AjaxCallBack<Object>() {

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                        if (errorNo == 500) {
                            isSameJym = false;
//							MyApplication.showSingleton("登录过期请重新登录");
                            DBUtil.quit(ActivityXianluDetail.this, DBUtil.getLoginMeber());
                            sp.edit().clear().commit();
//							ShareSDK.initSDK(ActivityXianluDetail.this);
//							Platform weixin= ShareSDK.getPlatform(Wechat.NAME);
//							weixin.removeAccount(true);
//							ShareSDK.stopSDK(ActivityXianluDetail.this);
                            EventBus.getDefault().post(
                                    new MyEvent(Integer.MAX_VALUE - 2));
                            Message msg = new Message();
                            msg.what = 1;
                            handler.sendMessage(msg);
                        } else {
//							App.showSingleton("获取数据失败");
                            Message msg = new Message();
                            msg.what = 1;
                            handler.sendMessage(msg);

                        }


                    }

                    @Override
                    public void onSuccess(Object t) {
                        super.onSuccess(t);
                        try {
                            if (StringUtil.removeNull(t).equals("0")) {
                                DBUtil.quit(ActivityXianluDetail.this, DBUtil.getLoginMeber());
                                sp.edit().clear().commit();
//								ShareSDK.initSDK(ActivityXianluDetail.this);
//								Platform weixin= ShareSDK.getPlatform(Wechat.NAME);
//								weixin.removeAccount(true);
//								ShareSDK.stopSDK(ActivityXianluDetail.this);
                                EventBus.getDefault().post(
                                        new MyEvent(Integer.MAX_VALUE - 2));
                                isSameJym = false;
                                Message msg = new Message();
                                msg.what = 1;
                                handler.sendMessage(msg);
                            } else {
                                Type type = new TypeToken<Member>() {
                                }.getType();
                                Member member = (Member) JsonUtil
                                        .fromJsonToObject(StringUtil.removeNull(t), type);
                                if (member != null) {
                                    DBUtil.updateMeber(member);

                                    if (!((sp.getString("jym", "123")).equals("123")) && !(sp.getString("jym", "123").equals(member.getJym()))) {
                                        DBUtil.quit(ActivityXianluDetail.this, member);
                                        sp.edit().clear().commit();
//										ShareSDK.initSDK(ActivityXianluDetail.this);
//										Platform weixin= ShareSDK.getPlatform(Wechat.NAME);
//										weixin.removeAccount(true);
//										ShareSDK.stopSDK(ActivityXianluDetail.this);
                                        EventBus.getDefault().post(
                                                new MyEvent(Integer.MAX_VALUE - 2));
                                        isSameJym = false;
                                    } else {
                                        isSameJym = true;
                                    }
                                    Message msg = new Message();
                                    msg.what = 1;
                                    handler.sendMessage(msg);
                                }
                            }


                        } catch (Exception e) {

                        }
                    }

                });
    }


    //    @OnClick({R.id.tv_tool_left, R.id.iv_xianlu_buy_ticket,
//            R.id.iv_xianlu_food, R.id.iv_xianlu_zhusu, R.id.tv_xianlu_guanzhu, R.id.tv_all_right})
    void OnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tool_left:
                finish();
                break;
            case R.id.iv_xianlu_buy_ticket:
                goActivityTicket();
                break;
            case R.id.iv_xianlu_food:
                goActivityMeiShi();
                break;
            case R.id.iv_xianlu_zhusu:
                goActivityZhuSu();
                break;
            case R.id.tv_xianlu_guanzhu:
//			if(DBUtil.getLoginMeber()==null||DBUtil.getLoginMeber().getJym()==null){
////				PopWindowLogin pop=new PopWindowLogin(ActivityXianluDetail.this);
////				pop.showAsDropDown(ivXLXQ);
//				return;
//			}
//			getPersonInfo();
//			handler=new Handler(){
//
//				@Override
//				public void handleMessage(Message msg) {
//					super.handleMessage(msg);
//					if(isSameJym)
//						guanZhuXianLu();
//					else{
////						PopWindowLogin pop=new PopWindowLogin(ActivityXianluDetail.this);
////						pop.showAsDropDown(ivXLXQ);
//					}
//				}
//
//			};
//			guanZhuXianLu();
                break;
            case R.id.tv_all_right:
//			if(MyApplication.isNeedPer){
//				requestRunTimePermissions(new String[]{Manifest.permission.READ_PHONE_STATE});
//				requestRunTimePermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
//			}
//			if(datapro.size()==0||datapro==null||datapro.get(0).getPro().getDesc_().equals(null)||datapro.get(0).getPro().getDesc_().equals("")||datapro.get(0).getPro().getPic()==null||datapro.get(0).getPro().getPic().equals("")){
////				MyApplication.showSingleton("暂无数据，不支持分享");
//				return;
//			}else{
//			Share();
//			}


                break;

            default:
                break;
        }
    }

    private void guanZhuXianLu() {
        BaseHttp http = new BaseHttp();
        AjaxParams params = new AjaxParams();
        params.put("proid", ps.getId() + "");
        params.put("count", 1 + "");
        params.put("jym", DBUtil.getLoginMeber().getJym());
        http.post(Constance.HTTP_REQUEST_URL_BIZ + "dosaveProcart", params, new AjaxCallBack<Object>() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
//				MyApplication.showSingleton("关注没有成功");
            }

            @Override
            public void onSuccess(Object t) {
                super.onSuccess(t);
                if (t.toString().equals("1")) {
//					App.showSingleton("关注成功");
                    tvRouteCare.setText("已关注");
                    tvRouteCare.setClickable(false);
                } else {
//					App.showSingleton("关注失败");
                }


            }

        });
    }


    private void Share() {
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();

        oks.setTitle(getString(R.string.app_name));
        oks.setTitleUrl(Constance.HTTP_URL);
        oks.setText(datapro.get(0).getPro().getDesc_());

        oks.setImageUrl(Constance.HTTP_URL + datapro.get(0).getPro().getPic());
        oks.setUrl(Constance.HTTP_URL);

        oks.setSite(getString(R.string.app_name));
        oks.setSiteUrl(Constance.HTTP_URL);
        oks.show(this);
    }

    private void goActivityMeiShi() {
//		Intent it=new Intent(ActivityXianluDetail.this,ActivityDinnerLive.class);
//		it.putExtra("type","餐饮");
//		startActivity(it);

    }


    @Override
    protected int initView() {
        // TODO Auto-generated method stub
        return R.layout.activity_xian_lu_detail;
    }

    private void init() {
        BaseHttp http = new BaseHttp();
        AjaxParams params = new AjaxParams();
        params.put("id", ps.getId() + "");
        http.post(Constance.HTTP_REQUEST_URL_BIZ + "merchantpro", params,
                new AjaxCallBack<Object>() {
                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        super.onFailure(t, errorNo, strMsg);

                    }

                    @Override
                    public void onSuccess(Object t) {
                        super.onSuccess(t);
                        try {
                            if (t == null) {
                                return;
                            }
                            String jsonString = "[" + t + "]";
                            datapro = JsonUtil.getList(jsonString, Production1.class);
                            if (datapro == null || datapro.size() == 0)
                                return;
                            String html = null;
//							wv.getSettings().setUseWideViewPort(true); 
//							wv.getSettings().setLoadWithOverviewMode(true); 
//							wv.getSettings().setDisplayZoomControls(false);
//							WebSettings settings = wv.getSettings(); 
//					        settings.setBuiltInZoomControls(true); // ��ʾ�Ŵ���С controler 
//					        settings.setSupportZoom(true); // �������� 
//					        settings.setDefaultZoom(ZoomDensity.CLOSE);// Ĭ������ģʽ 
                            wv.getSettings().setJavaScriptEnabled(true);
                            wv.addJavascriptInterface(new JavaScriptObject(ActivityXianluDetail.this), "myobj");
                            String html1 = "<html><head><style>img{max-width:100%;height:auto !important;width:auto !important;};</style></head><body style='margin:5; padding:5;'>" + datapro.get(0).getPro().getMemo() + "</body></html>";
                            wv.loadDataWithBaseURL(Constance.HTTP_REQUEST_URL_BIZ, html1, "text/html", "utf-8", null);
                            ImageLoader.getInstance().displayImage(
                                    Constance.HTTP_URL + datapro.get(0).getPro().getPic(), ivXiangQing, MyApplication.normalOption);
                            tvXianLuName.setText(datapro.get(0).getPro().getName());
                            tvDetailItem.setText(datapro.get(0).getPro().getDesc_());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void goActivityZhuSu() {
//		Intent it=new Intent(ActivityXianluDetail.this,ActivityDinnerLive.class);
//		it.putExtra("type","酒店");
//		startActivity(it);
    }

    private void goActivityTicket() {
        // TODO Auto-generated method stub
//		Intent intent = new Intent(ActivityXianluDetail.this, ActivityTicketList.class);
//		startActivity(intent);
    }

    private void initStatus() {
//		if(DBUtil.getLoginMeber()==null||DBUtil.getLoginMeber().getJym()==null){
//			PopWindowLogin pop=new PopWindowLogin(ActivityXianluDetail.this);
//			pop.showAsDropDown(ivXLXQ);
//		}
        BaseHttp http = new BaseHttp();
        AjaxParams params = new AjaxParams();
        params.put("jym", DBUtil.getLoginMeber().getJym());
        params.put("type", "线路攻略");
        http.post(Constance.HTTP_REQUEST_URL_BIZ + "getCartPros", params,
                new AjaxCallBack<Object>() {
                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        super.onFailure(t, errorNo, strMsg);

                    }

                    @Override
                    public void onSuccess(Object t) {
                        super.onSuccess(t);
                        try {
                            if (t == null) {
                                return;
                            }
                            proInfos = JsonUtil.getList(t.toString(), ProductSpecal.class);
                            if (proInfos.size() != 0) {
                                for (int i = 0; i < proInfos.size(); i++) {
                                    if (proInfos.get(i).getId() == ps.getId()) {
                                        tvRouteCare.setText("已关注");
                                        tvRouteCare.setClickable(false);
                                    }
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    /*处理权限问题*/

    /**
     * @param permissions 申请的权限
     */
    public void requestRunTimePermissions(String[] permissions) {
        if (permissions == null || permissions.length == 0)
            return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkPermissionGranted(permissions)) {
            //提示已拥有权限

        } else {
            //申请权限
            requestPermission(permissions, permissionsRequestCode);
        }
    }

    public void requestPermission(final String[] permissions, final int permissionRequestCode) {
        if (shouldShowRequestPermissionRationale(permissions)) {
            new AlertDialog.Builder(ActivityXianluDetail.this).setTitle(R.string.attention)
                    .setMessage(R.string.content_to_request_permission)
                    .setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(ActivityXianluDetail.this, permissions, permissionRequestCode);
                        }
                    }).show();
        } else {
            ActivityCompat.requestPermissions(this, permissions, permissionRequestCode);
        }
    }

    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        boolean flag = false;
        for (String p : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, p)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public boolean checkPermissionGranted(String[] permissions) {
        boolean result = true;
        for (String p : permissions) {
            if (ActivityCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                result = false;
                break;
            }
        }
        return result;
    }

    private boolean checkGranted(int[] grantResults) {
        boolean result = true;
        for (int grant : grantResults) {
            if (grant != PackageManager.PERMISSION_GRANTED) {
                result = false;
                break;
            }
        }
        return result;
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case permissionsRequestCode:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意，可以去放肆了。
                } else {
                    // 权限被用户拒绝了，洗洗睡吧。
//					MyApplication.showSingleton("读写sdcard权限等被您禁止无法分享");
                }
                break;
        }
    }


}
