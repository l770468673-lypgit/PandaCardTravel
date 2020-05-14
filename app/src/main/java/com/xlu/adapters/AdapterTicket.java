package com.xlu.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.pandacard.teavel.R;
import com.xlu.po.Merchant1;
import com.xlu.po.ProInfo1;
import com.xlu.po.Production;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class AdapterTicket extends BaseAdapter {

//	List<Ticket> list;
	List<Production> list;
	Context context;
	private Merchant1 m;

	public AdapterTicket(List<Production> list, Context context, Merchant1 m) {
		super();
		this.list = list;
		this.context = context;
		this.m=m;
	}

	class ViewHolder {
		public TextView tv_name;
		public TextView tv_price;
		public Button btn_buy;
		public TextView tvLubi;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.get(0).getPros().size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(0).getPros().get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder vh = null;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_ticket,
					null);
			vh = new ViewHolder();
			vh.tv_name = view.findViewById(R.id.tv_name);
			vh.tv_price = view.findViewById(R.id.tv_price);
			vh.btn_buy = view.findViewById(R.id.btn);
			vh.tvLubi= view.findViewById(R.id.tv_ticket_lubi);
			vh.btn_buy.setClickable(false);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		final ProInfo1 p = list.get(0).getPros().get(position);
		vh.tv_name.setText(p.getName());
		NumberFormat format=new DecimalFormat("#");
		if(String.valueOf(p.getPrice()).endsWith("0")){
			vh.tv_price.setText("￥" + format.format(p.getPrice()));
		}else{
			vh.tv_price.setText("￥"+p.getPrice());
		}

			vh.tvLubi.setText("鹿币立减:"+ p.getCoupon_max());


		vh.btn_buy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				if (p.getActive() == 1) {
//					Intent intent = new Intent(context,
//							ActivityEditAll.class);
//					intent.putExtra("proInfo", p);
//					intent.putExtra("merInfo", list.get(0).getMerchant());
//					intent.putExtra("m",m);
//					intent.putExtra("type",Constance.ticket);
//					intent.putExtra("comment", "");
//					context.startActivity(intent);
//				} else {
//					App.show("无法预订");
//				}
			}
		});

		return view;
	}
}
