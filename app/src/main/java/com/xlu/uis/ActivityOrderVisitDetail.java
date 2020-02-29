package com.xlu.uis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pandacard.teavel.R;
import com.pandacard.teavel.apps.MyApplication;
import com.pandacard.teavel.utils.ToastUtils;
import com.xlu.bases.BaseActivity2;
import com.xlu.po.Member;
import com.xlu.po.ProductSpecal;
import com.xlu.po.Production;
import com.xlu.po.Production1;
import com.xlu.po.TuanInfo;
import com.xlu.popwindows.PopwindowQrCode;
import com.xlu.utils.Constance;
import com.xlu.utils.JsonUtil;
import com.xlu.widgets.HorizontalListView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.wideroad.BaseActivity;
import cn.com.wideroad.BaseHttp;
import cn.com.wideroad.http.AjaxCallBack;
import cn.com.wideroad.http.AjaxParams;
import cn.com.wideroad.utils.StringUtil;

public class ActivityOrderVisitDetail extends BaseActivity2  {
    ImageView iv_back;

    TextView tv_title;

    TextView tvCreateActive;
    ImageView tvTuanTour;
    WebView wView;
    ScrollView svTourDetail;
    RelativeLayout rlJoin;
    TextView tvName;
    TextView tvPrice;
    LinearLayout button;
    TextView tvLubi;


//
//	View header;
//	Footer footer;

    private TuanInfo tuanInfo;
    private int page = 1;
    int tid;
    ProductSpecal proTour;
    Production pro;
    List<Production1> listPro;

    SharedPreferences sp;

    private PopwindowQrCode popwindowQrCode;
    int id;
    private HorizontalListView hlv;

    private TextView tvAllUser;

    protected List<Member> members = new ArrayList<Member>();

    @Override
    protected int initView() {
        return R.layout.activity_zhuanche_detail;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVieww();
        sp = getSharedPreferences("mimi", Context.MODE_PRIVATE);
        id = getIntent().getIntExtra("id", 0);
        proTour = (ProductSpecal) getIntent().getSerializableExtra("tuanInfo");
        if (proTour != null) {
            id = proTour.getId();
        }

        tvLubi.setText("鹿币立减:");
//        tvLubi.setText("鹿币立减:" + (int) (proTour.getCoupon_max()));

//		AdapterTuanXingCheng adapter=new AdapterTuanXingCheng(this);
//		lvXCh.setAdapter(adapter);
        initData();
    }

    private void initVieww() {
        tvLubi = findViewById(R.id.tv_xianlu_lubi);
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        tvCreateActive = findViewById(R.id.tv_create_active);
        tvTuanTour = findViewById(R.id.iv_tuan_tour);
        wView = findViewById(R.id.wv_detail);
        svTourDetail = findViewById(R.id.sv_tour_detail);
        rlJoin = findViewById(R.id.rl_join_active);
        tvName = findViewById(R.id.tv_zhuanche_name);
        tvPrice = findViewById(R.id.tv_zhuanche_price);
        button = findViewById(R.id.ll_button_container);

        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

    }


//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.iv_back:
//                finish();
//                break;
//
//            case R.id.rl_join_active:
////			Intent it=new Intent(ActivityOrderVisitDetail.this,ActivityEditAll.class);
////			it.putExtra("proInfo",proTour);
////			it.putExtra("type", Constance.jiaotong);
////			startActivity(it);
//                break;
//
//            default:
//                break;
//        }
//    }


    private void initData() {
        BaseHttp http = new BaseHttp();
        AjaxParams params = new AjaxParams();
        params.put("id", id + "");
        http.post(Constance.HTTP_REQUEST_URL_BIZ + "merchantpro", params,
                new AjaxCallBack<Object>() {
                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                        try {
                            ToastUtils.showToast(ActivityOrderVisitDetail.this, "获取网络数据失败，请重试");
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onSuccess(Object t) {
                        super.onSuccess(t);
                        try {
                            String jsonString = "[" + StringUtil.removeNull(t) + "]";
                            listPro = JsonUtil.getList(jsonString, Production1.class);
                            if (listPro == null || listPro.size() == 0)
                                return;
                            ImageLoader.getInstance().displayImage(Constance.HTTP_URL + listPro.get(0).getPro().getPic(),
                                    tvTuanTour, MyApplication.normalOption);
                            tvName.setText(listPro.get(0).getPro().getName());
//						tvGanshou.setText(listPro.get(0).getPro().getDesc_());
                            tvPrice.setText("¥" + (int) (listPro.get(0).getPro().getPrice()));
                            if (listPro.get(0).getPro().getPrice() == 0) {
                                button.setVisibility(View.GONE);
                                tvPrice.setVisibility(View.GONE);
                            } else {
                                button.setVisibility(View.VISIBLE);
                            }
//						wView.getSettings().setUseWideViewPort(true); 
//						wView.getSettings().setLoadWithOverviewMode(true); 
//						wView.getSettings().setDisplayZoomControls(false);
//						WebSettings settings = wView.getSettings(); 
//				        settings.setBuiltInZoomControls(true); // 显示放大缩小 controler 
//				        settings.setSupportZoom(true); // 可以缩放 
//				        settings.setDefaultZoom(ZoomDensity.CLOSE);// 默认缩放模式 
                            String html = "<html><head><style>img{max-width:100%;height:auto !important;width:auto !important;};</style></head><body style='margin:5; padding:5;'>" + listPro.get(0).getPro().getMemo() + "</body></html>";
                            wView.loadDataWithBaseURL(Constance.HTTP_REQUEST_URL_BIZ, html, "text/html", "utf-8", null);
                        } catch (Exception e) {
                        }
                    }
                });
    }
//	private void confirmOrder(){
//		final ProgressDialog pbarDialog = new ProgressDialog(this);
//		pbarDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		pbarDialog.setCancelable(false);
//		pbarDialog.setMessage("提交订单...");
//		pbarDialog.show(); 
//		BaseHttp http = new BaseHttp();
//		AjaxParams params = new AjaxParams();
//		if(sp.getString("jym","123").equals("123")){
//			App.showSingleton("请登录！");
//			return;
//		}
//		params.put("jym",sp.getString("jym","123"));
//		params.put("tel","");
//		params.put("proid",proTour.getId()+"");
//		params.put("name",proTour.getName()+"");
//		params.put("sfz","");
//		params.put("money",proTour.getPrice()+"");
//		params.put("amount","1");
//		http.post(Constance.HTTP_REQUEST_URL1 + "saveorder", params,
//				new AjaxCallBack<Object>() {
//
//					@Override
//					public void onFailure(Throwable t, int errorNo,
//							String strMsg) {
//						super.onFailure(t, errorNo, strMsg);
//						try {
//							pbarDialog.cancel();
//						} catch (Exception e) {
//						}
//					}
//
//					@Override
//					public void onSuccess(Object t) {
//						try {
//							pbarDialog.cancel();
//							super.onSuccess(t);
//							("123",t.toString());
//							String temp = StringUtil.removeNull(t);
//							OrderParam pa=JsonUtil.getObject(temp,OrderParam.class);
//							if(pa.getFlag()!=true){
//								App.showSingleton("提交订单失败！");
//								return;
//							}
//							Intent intent = new Intent(ActivityZhuanCheDetail.this,
//									ActivityTicketPay.class);
//							intent.putExtra("order", temp);
//							("123",temp);
//							intent.putExtra("sum",proTour.getPrice());
//							intent.putExtra("bid", pa.getBid());
//							intent.putExtra("member", DBUtil.getLoginMeber());
//							intent.putExtra("merName",proTour.getName());
//							ActivityZhuanCheDetail.this.startActivity(intent);
//						} catch (Exception e) {
//						}
//					}
//
//				});
//	}


}
