package com.xlu.uis;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pandacard.teavel.R;
import com.pandacard.teavel.apps.MyApplication;
import com.xlu.adapters.AdapterTicket;
import com.xlu.bases.BaseActivity2;
import com.xlu.po.MerInfo;
import com.xlu.po.Merchant1;
import com.xlu.po.Production;
import com.xlu.utils.Constance;
import com.xlu.utils.JsonUtil;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.com.wideroad.BaseHttp;
import cn.com.wideroad.http.AjaxCallBack;
import cn.com.wideroad.http.AjaxParams;
import cn.com.wideroad.utils.StringUtil;

public class ActivityTicketShow extends BaseActivity2 implements OnClickListener {

    ImageView iv;

    TextView tv_name;
    TextView tvTitle;
    ImageView ivLeft;

    WebView wv;
    RelativeLayout rlAdd;
    ListView lv;

    Merchant1 mt;
    AdapterTicket adapter;
    List<Merchant1> list;
    List<Production> ticketPro;
    ImageView tv_tool_left;


    @Override
    protected int initView() {
        return R.layout.activity_ticket;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        tvTitle.setCompoundDrawablePadding(0);
        tvTitle.setCompoundDrawables(null, null, null, null);
        tvTitle.setText("门票");
        tvTitle.setVisibility(View.VISIBLE);
        ivLeft.setVisibility(View.VISIBLE);

        init();
    }

    private void initViews() {
        iv = findViewById(R.id.iv);
        tv_name = findViewById(R.id.tv_name);
        tvTitle = findViewById(R.id.tv_all_title_name);
        ivLeft = findViewById(R.id.tv_tool_left);
        wv = findViewById(R.id.wv_info);
        rlAdd = findViewById(R.id.rl_add);
        lv = findViewById(R.id.lv);
        tv_tool_left = findViewById(R.id.tv_tool_left);
        tv_tool_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {

        mt = (Merchant1) getIntent().getSerializableExtra("zone");
//		wv.loadDataWithBaseURL(Constance.HTTP_URL, mt.getTicket_desc(),
//				"text/html", "UTF-8", null);
        int id = getIntent().getIntExtra("id", 0);
        if (mt != null) {
            id = mt.getId();
        }
        Log.v("tag", "id=" + id);

        BaseHttp http = new BaseHttp();
        AjaxParams params = new AjaxParams();
        params.put("id", id + "");
        http.post(Constance.HTTP_REQUEST_URL_BIZ + "merchant", params,
                new AjaxCallBack<Object>() {

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                    }

                    @Override
                    public void onSuccess(Object t) {
                        //��ȡProsInfo
                        Log.v("123","json--->"+StringUtil.removeNull(t));
                        try {
                            Type types1 = new TypeToken<List<Production>>() {
                            }.getType();
                            org.json.JSONArray array=new org.json.JSONArray("["+StringUtil.removeNull(t).toString()+"]");
                            ticketPro= (List<Production>) JsonUtil.fromJsonToObject(
                                    array.toString(), types1);
                            if(ticketPro==null||ticketPro.size()==0)
                                return;

                            tv_name.setText(mt.getName());
                            rlAdd.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    if(ticketPro.get(0).getMerchant().getAddress()!=null){
                                        bycar(ticketPro.get(0).getMerchant());
                                    }

                                }
                            });

                            ImageLoader.getInstance().displayImage(Constance.HTTP_URL+ticketPro.get(0).getMerchant().getPic(),iv,MyApplication.normalOption);
                            tv_name.setText(ticketPro.get(0).getMerchant().getName());
                            String html="<html><head><style>img{max-width:100%;height:auto !important;width:auto !important;};</style></head><body style='margin:5; padding:5;'>"+ticketPro.get(0).getMerchant().getDesc_()+"</body></html>";
                            wv.loadDataWithBaseURL(Constance.HTTP_REQUEST_URL,html, "text/html", "utf-8", null);
                            adapter = new AdapterTicket(ticketPro, context,mt);
                            lv.setAdapter(adapter);
                            setListViewHeightBasedOnChildren(lv);
                        } catch (Exception e) {
                        }
                    }

                });
    }

    private void bycar(MerInfo mer) {
//		Intent intent = new Intent(context, ActivitySceneRoute.class);
//		if(mer!=null){
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
//			intent.putExtra("y", jindu);
//		}else if(mer!=null){
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
//			intent.putExtra("y", jindu);
//		}
//
//
//		context.startActivity(intent);

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 20;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


}
