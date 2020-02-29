package com.xlu.uis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pandacard.teavel.R;
import com.pandacard.teavel.apps.MyApplication;
import com.xlu.adapters.AdapterZhuSuDetail;
import com.xlu.bases.BaseActivity2;
import com.xlu.po.MerInfo;
import com.xlu.po.Merchant1;
import com.xlu.po.Production;
import com.xlu.utils.Constance;
import com.xlu.utils.JsonUtil;
import com.xlu.widgets.ScrollDisabledListView;
import com.xlu.widgets.StarView;

import org.json.JSONArray;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.wideroad.BaseHttp;
import cn.com.wideroad.http.AjaxCallBack;
import cn.com.wideroad.http.AjaxParams;
import cn.com.wideroad.utils.StringUtil;

public class ActivityZhuSuDetail extends BaseActivity2 implements OnClickListener {
	ImageView ivBack;
	TextView tvTitle;
	ImageView ivZhuSuByCar;
	ScrollDisabledListView lv;
	ImageView ivZhuSu;
	StarView rbZhuSu;
	TextView tvZhuSuPinFenNum;
	TextView tvZhuSuAdd;
	TextView tvZhuSuPh;
	TextView tvZhuSuPingLunNum;
	WebView wvZhuSu;
	TextView tvZhuSuTitle,tv_zhusu_phone;
	ImageView iv_zhu_su_car_nav,tv_tool_left;



	Merchant1 merchant;
	List<Production> list;
	SharedPreferences sp;

	public void onClick(View v) {
		if (v.getId() == R.id.tv_tool_left) {
			finish();
		}
		if (v.getId() == R.id.iv_zhu_su_car_nav) {
//			Intent intent = new Intent(this, ActivitySceneRoute.class);
//			intent.putExtra("name", merchant.getName());
//			String jindu="118";
//			String weidu="31";
//			if(merchant.getWeidu()!=null){
//				if(!(merchant.getWeidu().equals(""))){
//					jindu=merchant.getJindu();
//				}
//			}
//			if(merchant.getWeidu()!=null){
//				if(!(merchant.getWeidu().equals(""))){
//					weidu=merchant.getWeidu();
//				}
//			}
//			intent.putExtra("x",weidu);
//			intent.putExtra("y", jindu);
//
//			startActivity(intent);
		}
		if(v.getId()==R.id.tv_zhusu_phone){
		dailPhone(tvZhuSuPh.getText().toString());
		}
	}
	private void dailPhone(String phoneNum){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.CALL");
		intent.setData(Uri.parse("tel:"+phoneNum));//
		context.startActivity(intent);


	}

	@Override
	protected int initView() {
		// TODO Auto-generated method stub
		return R.layout.activity_zhusu_detail;
	}
	int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initVieww();
		sp=getSharedPreferences("mimi",Context.MODE_PRIVATE);
		merchant=(Merchant1) getIntent().getSerializableExtra("zhusu");
		id=getIntent().getIntExtra("id", 0);
		if(merchant!=null){
			id=merchant.getId();
		}
		ivBack.setVisibility(View.VISIBLE);
		tvTitle.setText(merchant.getName());
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setCompoundDrawablePadding(0);
		tvTitle.setCompoundDrawables(null,null,null,null);
		init();
		
		
		
	}

    private void initVieww() {
        ivBack =  findViewById(R.id.tv_tool_left);
        tvTitle =  findViewById(R.id.tv_all_title_name);
        ivZhuSuByCar =  findViewById(R.id.iv_zhu_su_car_nav);
        lv =  findViewById(R.id.lv_zhuSu);
        ivZhuSu =  findViewById(R.id.iv_zhusu);
        rbZhuSu =  findViewById(R.id.rb_zhusu_pinfen);
        tvZhuSuPinFenNum =  findViewById(R.id.tv_pinfen_num);
        tvZhuSuAdd =  findViewById(R.id.tv_zhusu_address);
        tvZhuSuPh =  findViewById(R.id.tv_zhusu_phone);
        tvZhuSuPingLunNum =  findViewById(R.id.tv_zhusu_pinglun_num);
        wvZhuSu =  findViewById(R.id.wv_zhusu);
        tvZhuSuTitle =  findViewById(R.id.tv_zhusu_title);
		tv_tool_left =  findViewById(R.id.tv_tool_left);
		iv_zhu_su_car_nav =  findViewById(R.id.iv_zhu_su_car_nav);
		tv_zhusu_phone =  findViewById(R.id.tv_zhusu_phone);

		ivBack.setOnClickListener(this);
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
	
	
	private void init(){
		BaseHttp http = new BaseHttp();
		AjaxParams params = new AjaxParams();
		params.put("id",id+"");
		http.post(Constance.HTTP_REQUEST_URL_BIZ + "merchant", params,
				new AjaxCallBack<Object>() {

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
//						MyApplication.showSingleton("访问网络失败请重试");
					}

					@Override
					public void onSuccess(Object t) {
						try {
							Type types = new TypeToken<List<Production>>() {
							}.getType();
							JSONArray array=new JSONArray("["+ StringUtil.removeNull(t).toString()+"]");
							list = (List<Production>) JsonUtil.fromJsonToObject(
									StringUtil.removeNull(array.toString()), types);
							if(list==null||list.size()==0)
								return;
							tvZhuSuTitle.setText(list.get(0).getMerchant().getName());
							tvZhuSuAdd.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									if(list.get(0).getMerchant().getAddress()!=null){
										bycar(list.get(0).getMerchant());

									}
									
								}
							});
							ImageLoader.getInstance().displayImage(Constance.HTTP_URL+list.get(0).getMerchant().getPic(), ivZhuSu,MyApplication.normalOption);
							rbZhuSu.setStar(0);
							if(list.get(0).getMerchant().getSatisfaction()!=null){
								tvZhuSuPinFenNum.setText(list.get(0).getMerchant().getSatisfaction()+"");
								rbZhuSu.setStar(list.get(0).getMerchant().getSatisfaction());
							}
							tvZhuSuPingLunNum.setText(list.get(0).getComments().size()+"条评论");
							if(list.get(0).getMerchant().getAddress()!=null){
								tvZhuSuAdd.setText(list.get(0).getMerchant().getAddress());
							}
							tvZhuSuPingLunNum.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
//									Intent it=new Intent(ActivityZhuSuDetail.this,ActivityZhuSuPingLun.class);
//									it.putExtra("title","住宿评论");
//									it.putExtra("data",(Serializable)(list.get(0).getComments()));
//									startActivity(it);
								}
							});
							tvZhuSuAdd.setText(list.get(0).getMerchant().getAddress());
//							onMapLoaded(Float.valueOf(list.get(0).getMerchant().getWeidu()), Float.valueOf(list.get(0).getMerchant().getJindu()));
							tvZhuSuPh.setText(list.get(0).getMerchant().getTel());
//							wvZhuSu.getSettings().setUseWideViewPort(true); 
//							wvZhuSu.getSettings().setLoadWithOverviewMode(true); 
//							wvZhuSu.getSettings().setDisplayZoomControls(false);
//							wvZhuSu.getSettings().setMinimumFontSize(20);
//							WebSettings settings = wvZhuSu.getSettings(); 
//					        settings.setBuiltInZoomControls(true); //
//					        settings.setSupportZoom(true); //
//					        settings.setDefaultZoom(ZoomDensity.CLOSE);//
							String html="<html><head><style>img{max-width:100%;height:auto !important;width:auto !important;};</style></head><body style='margin:5; padding:5;'>"+list.get(0).getMerchant().getDesc_()+"</body></html>";
							wvZhuSu.loadDataWithBaseURL(Constance.HTTP_REQUEST_URL_BIZ,html,"text/html","utf-8",null);
							AdapterZhuSuDetail adapter=new AdapterZhuSuDetail(ActivityZhuSuDetail.this,list.get(0).getPros());
							lv.setAdapter(adapter);
							adapter.setmListener(new AdapterZhuSuDetail.ZhuSuButtonClicker() {
								@Override
								public void ButtonClick(View view, int position) {
									final int i=position;
									goZhuSuOrder(i);
								}
							});
							lv.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
//									Intent intent=new Intent(ActivityZhuSuDetail.this,ActivityRoomDetail.class);
//									intent.putExtra("desc_",list.get(0).getPros().get(position).getMemo());
//									intent.putExtra("name",list.get(0).getPros().get(position).getName());
//									ActivityZhuSuDetail.this.startActivity(intent);
									
								}
							});
			
							
						} catch (Exception e) {

						}
					}

				});
	}
	private void goZhuSuOrder(int position){
//		Intent it=new Intent(ActivityZhuSuDetail.this,ActivityEditAll.class);
//		it.putExtra("proInfo", list.get(0).getPros().get(position));
//		it.putExtra("merInfo",list.get(0).getMerchant());
//		it.putExtra("type", Constance.ZhuSu);
//		startActivity(it);
	}
//	@SuppressLint("NewApi")
//	private void confirmOrder(final int position){
//		if(DBUtil.getLoginMeber()==null||DBUtil.getLoginMeber().getJym()==null){
//			PopWindowLogin pop=new PopWindowLogin(ActivityZhuSuDetail.this);
//			pop.showAsDropDown(ivBack);
//		}
//		final ProgressDialog pbarDialog = new ProgressDialog(this);
//		pbarDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		pbarDialog.setCancelable(false);
//		pbarDialog.setMessage("�ύ����...");
//		pbarDialog.show(); 
//		BaseHttp http = new BaseHttp();
//		AjaxParams params = new AjaxParams();
//		
//		params.put("jym",sp.getString("jym","123"));
//		params.put("tel","");
//		params.put("proid",list.get(0).getPros().get(position).getId()+"");
//		params.put("name",list.get(0).getPros().get(position).getName()+"");
//		params.put("sfz","");
//		params.put("money",list.get(0).getPros().get(position).getPrice()+"");
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
//								App.showSingleton("�ύ����ʧ�ܣ�");
//								return;
//							}
//							Intent intent = new Intent(ActivityZhuSuDetail.this,
//									ActivityTicketPay.class);
//							intent.putExtra("order", temp);
//							("123",temp);
//							intent.putExtra("sum",list.get(0).getPros().get(position).getPrice());
//							intent.putExtra("bid", pa.getBid());
//							intent.putExtra("member", DBUtil.getLoginMeber());
//							intent.putExtra("merName",merchant.getName());
//							ActivityZhuSuDetail.this.startActivity(intent);
//						} catch (Exception e) {
//						}
//					}
//
//				});
//	}

}
