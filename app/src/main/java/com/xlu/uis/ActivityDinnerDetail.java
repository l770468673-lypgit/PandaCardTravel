package com.xlu.uis;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pandacard.teavel.R;
import com.pandacard.teavel.apps.MyApplication;
import com.xlu.bases.BaseActivity;
import com.xlu.po.MerInfo;
import com.xlu.po.Merchant1;
import com.xlu.po.Production;
import com.xlu.utils.Constance;
import com.xlu.utils.DistanceUtil;
import com.xlu.utils.JsonUtil;
import com.xlu.widgets.ScrollDisabledListView;
import com.xlu.widgets.StarView;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

import cn.com.wideroad.BaseHttp;
import cn.com.wideroad.http.AjaxCallBack;
import cn.com.wideroad.http.AjaxParams;
import cn.com.wideroad.utils.StringUtil;

public class ActivityDinnerDetail extends BaseActivity implements OnClickListener {
	ImageView ivBack;
	TextView tvTitle;
	TextView tvYouHuiFu;
	ImageView ivByCar;
	ImageView ivMeiShi;
	TextView tvMeiShiTitle;
	TextView tvMeiShiDist;
	TextView tvPhoneNum;
	TextView tvMeiShiPrice;
	WebView wvCaiPin;
	List<Production>  list;
	Merchant1 merchant;
	StarView rbPinFenShow;
	TextView tvAddress;
	TextView tvPingLunNum;
	ScrollDisabledListView lvPingLun;


	MerInfo mer;
	int id;
	private ImageView mTv_tool_left;
	private TextView mTv_you_hui_fu;
	private ImageView mIv_car_nav;
	private TextView mTv_meishi_phone_num;


	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_tool_left:
			finish();
			break;
		case R.id.tv_you_hui_fu:
			goPayForDinner();
			break;
		case R.id.iv_car_nav:
//			Intent intent = new Intent(this, ActivitySceneRoute.class);
//			intent.putExtra("name", mer.getName());
//			String jindu="118";
//			String weidu="31";
//			if(mer.getWeidu()!=null){
//				if(!(mer.getWeidu().equals(""))){
//					jindu=mer.getJindu();
//				}
//			}
//			if(mer.getWeidu()!=null){
//				if(!(mer.getWeidu().equals(""))){
//					weidu=mer.getWeidu();
//				}
//			}
//			intent.putExtra("x",weidu);
//			intent.putExtra("y",jindu);
//			startActivity(intent);
			break;
		case R.id.tv_meishi_phone_num:
			dailPhone(tvPhoneNum.getText().toString());
			break;


		default:
			break;
		}
	}
	private void bycar(MerInfo mer){
//		Intent intent = new Intent(context, ActivitySceneRoute.class);
//		if(mer!=null) {
//			intent.putExtra("name", merchant.getName());
//			String jindu = "118";
//			String weidu = "31";
//			if (mer.getWeidu() != null) {
//				if (!(merchant.getWeidu().equals(""))) {
//					jindu = merchant.getJindu();
//				}
//			}
//			if (mer.getWeidu() != null) {
//				if (!(mer.getWeidu().equals(""))) {
//					weidu = mer.getWeidu();
//				}
//			}
//			intent.putExtra("x", weidu);
//			intent.putExtra("y", jindu);
//
//
//			context.startActivity(intent);
//		}

	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initVEWW();
		merchant=(Merchant1) getIntent().getSerializableExtra("dinner");
		id=getIntent().getIntExtra("id",0);
		ivBack.setVisibility(View.VISIBLE);
		tvTitle.setText(merchant.getName());
		tvTitle.setCompoundDrawablePadding(0);
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setCompoundDrawables(null,null,null,null);
		if(merchant!=null){
			id=merchant.getId();
		}
		init();

	}

	private void initVEWW() {
		ivBack=findViewById(R.id.tv_tool_left);
		tvTitle=findViewById(R.id.tv_all_title_name);
		tvYouHuiFu=findViewById(R.id.tv_you_hui_fu);
		ivByCar=findViewById(R.id.iv_car_nav);
		ivMeiShi=findViewById(R.id.iv_meishi);
		tvMeiShiTitle=findViewById(R.id.tv_meishi_title);
		tvMeiShiDist=findViewById(R.id.tv_meishi_distance);
		tvPhoneNum=findViewById(R.id.tv_meishi_phone_num);
		tvMeiShiPrice=findViewById(R.id.tv_meishi_price);
		wvCaiPin=findViewById(R.id.wv_caipin);
		rbPinFenShow=findViewById(R.id.rb_pingfen_show);
		tvAddress=findViewById(R.id.tv_jiudian_address);
		tvPingLunNum=findViewById(R.id.tv_pinglun_num);
		lvPingLun=findViewById(R.id.lv_pinglun);
		mTv_tool_left = findViewById(R.id.tv_tool_left);
		mTv_you_hui_fu = findViewById(R.id.tv_you_hui_fu);
		mIv_car_nav = findViewById(R.id.iv_car_nav);
		mTv_meishi_phone_num = findViewById(R.id.tv_meishi_phone_num);

		mTv_tool_left.setOnClickListener(this);


	}

	private void dailPhone(String phoneNum){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.CALL");
		intent.setData(Uri.parse("tel:"+phoneNum));//
		context.startActivity(intent);


	}

	private void goPayForDinner(){
//		Intent it=new Intent(this,ActivityDinnerOrder.class);
//		it.putExtra("merInfo",mer);
//		startActivity(it);
	}

	@Override
	protected int initView() {
		// TODO Auto-generated method stub
		return R.layout.activity_dinner_detail;
	}
	private  void init(){
		BaseHttp http = new BaseHttp();
		AjaxParams params = new AjaxParams();
		params.put("id",id+"");
		http.post(Constance.HTTP_REQUEST_URL_BIZ+ "merchant", params,
				new AjaxCallBack<Object>() {

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
					}

					@Override
					public void onSuccess(Object t) {
						try {
							Type types = new TypeToken<List<Production>>() {
							}.getType();
							JSONArray array=new JSONArray("["+ StringUtil.removeNull(t) +"]");

							list = (List<Production>) JsonUtil.fromJsonToObject(
									StringUtil.removeNull(array.toString()), types);
							if(list==null||list.size()==0)
								return;

							mer=list.get(0).getMerchant();
							ImageLoader.getInstance().displayImage(Constance.HTTP_URL+list.get(0).
									getMerchant().getPic(),ivMeiShi,MyApplication.normalOption);
							tvMeiShiTitle.setText(""+list.get(0).getMerchant().getName());
							tvPhoneNum.setText(list.get(0).getMerchant().getTel());
							tvMeiShiPrice.setText(merchant.getPrice()+"");
							rbPinFenShow.setStar(0);
							if(mer.getSatisfaction()!=null)
							rbPinFenShow.setStar((mer.getSatisfaction()));

							if(list.get(0).getMerchant().getAddress()!=null){
								tvAddress.setText(list.get(0).getMerchant().getAddress());
							}
							if(list.get(0).getMerchant().getJindu()!=null&&list.get(0).getMerchant().getWeidu()!=null){
								if(!(list.get(0).getMerchant().getJindu().equals(""))&&!(list.get(0).getMerchant().getWeidu().equals(""))){
									tvMeiShiDist.setText("Լ"+ DistanceUtil.Distance(MyApplication.jingdu,
											MyApplication.weidu,Double.valueOf(list.get(0).getMerchant().getJindu().trim()),Double.valueOf(list.get(0).getMerchant().getWeidu().trim())) + "km");
								}
							}
							tvAddress.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									bycar(list.get(0).getMerchant());

								}
							});

							tvPingLunNum.setText("评论("+list.get(0).getComments().size()+")");
							tvAddress.setText(list.get(0).getMerchant().getAddress());
//							AdapterProComment adapter=new AdapterProComment(ActivityDinnerDetail.this, list.get(0).getComments());
//							lvPingLun.setAdapter(adapter);
//							wvCaiPin.getSettings().setUseWideViewPort(true); 
//							wvCaiPin.getSettings().setLoadWithOverviewMode(true); 
//							wvCaiPin.getSettings().setDisplayZoomControls(false);
//							wvCaiPin.getSettings().setMinimumFontSize(20);
//							WebSettings settings = wvCaiPin.getSettings(); 
//					        settings.setBuiltInZoomControls(true); // ��ʾ�Ŵ���С controler 
//					        settings.setSupportZoom(true); // �������� 
//					        settings.setDefaultZoom(ZoomDensity.CLOSE);// Ĭ������ģʽ 
							String html="<html><head><style>img{max-width:100%;height:auto !important;width:auto !important;};</style></head><body style='margin:5; padding:5;'>"+list.get(0).getMerchant().getDesc_()+"</body></html>";
							wvCaiPin.loadDataWithBaseURL(Constance.HTTP_REQUEST_URL_BIZ,html,"text/html","utf-8",null);


						} catch (Exception e) {


						}
					}

				});

	}








}
