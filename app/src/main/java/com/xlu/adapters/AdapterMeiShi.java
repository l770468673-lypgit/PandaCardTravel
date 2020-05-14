package com.xlu.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pandacard.teavel.R;
import com.pandacard.teavel.apps.MyApplication;
import com.xlu.po.Merchant1;
import com.xlu.uis.ActivityDinnerDetail;
import com.xlu.utils.Constance;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;


public class AdapterMeiShi extends BaseAdapter {
	Context mContext;
	List<Merchant1> datas;

	public AdapterMeiShi(List<Merchant1> datas, Context context) {
		super();
		this.mContext = context;
		this.datas = datas;
	}

	public AdapterMeiShi(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		JiuDianViewHolder holder = null;
		if (convertView == null) {

			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.lv_item_meishi, null);
			holder = new JiuDianViewHolder();
			holder.ivJiuDian = convertView
					.findViewById(R.id.iv_jiudian);
			holder.rbJiuDianPk = convertView
					.findViewById(R.id.rb_jiudian_pk);
			holder.tvDistanceTextView = convertView
					.findViewById(R.id.tv_juli);
			holder.tvJiuDianName = convertView
					.findViewById(R.id.tv_jiudian_names);
			holder.tvPriceNum = convertView
					.findViewById(R.id.tv_price_real_num);
//			holder.tvTags=(TextView) convertView.findViewById(R.id.tv_tags);
			convertView.setTag(holder);

		} else {
			holder = (JiuDianViewHolder) convertView.getTag();
		}
		ImageLoader imageLoader= ImageLoader.getInstance();
		if(!imageLoader.isInited())
		imageLoader.init(MyApplication.config);
		imageLoader.displayImage(
				Constance.HTTP_URL + datas.get(position).getPic(),
				holder.ivJiuDian, MyApplication.normalOption);
		if(datas.get(position).getSatisfaction()!=null)
		holder.rbJiuDianPk.setRating(datas.get(position).getSatisfaction());
		if (datas.get(position).getWeidu() != null
				&& datas.get(position).getJindu() != null) {
			if (!(datas.get(position).getJindu().equals(""))
					&& !(datas.get(position).getWeidu().equals(""))) {
				double dis = datas.get(position).getJuli();
				holder.tvDistanceTextView.setText(parseTo(dis) + "km");
			}
		}
		if(parseTo(datas.get(position).getJuli())>10000){
			holder.tvDistanceTextView.setVisibility(View.INVISIBLE);
		}
		holder.tvJiuDianName.setText(datas.get(position).getName());
		NumberFormat format=new DecimalFormat("#");
		holder.tvPriceNum.setText(format.format(datas.get(position).getPrice()) + "");
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent it=new Intent(mContext, ActivityDinnerDetail.class);
				it.putExtra("dinner", datas.get(position));
				mContext.startActivity(it);
				
			}
		});

		return convertView;
	}

	class JiuDianViewHolder {
		ImageView ivJiuDian;
		TextView tvJiuDianName;
		TextView tvPriceNum;
		RatingBar rbJiuDianPk;
		TextView tvDistanceTextView;
		TextView tvTags;
	}
	private double parseTo(double sum) {
		double f = sum;
		BigDecimal b = new BigDecimal(f);
		double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1;

	}

}
