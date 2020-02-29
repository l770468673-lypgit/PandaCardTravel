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
import com.xlu.uis.ActivityZhuSuDetail;
import com.xlu.utils.Constance;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;


public class AdapterZhuSu extends BaseAdapter {
	Context mContext;
	List<Merchant1> datas;

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

	public AdapterZhuSu(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ZhuSuViewHolder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(mContext).inflate(R.layout.lv_item_zhusu, null);
			holder=new ZhuSuViewHolder();
			holder.ivJiuDian=(ImageView) convertView.findViewById(R.id.iv_jiudian_img);
			holder.tvJiuDianJuLi=(TextView) convertView.findViewById(R.id.tv_jiudian_juli);
			holder.tvJiuDianName=(TextView) convertView.findViewById(R.id.tv_jiudian_name);
			holder.rbZhuSuPk=(RatingBar) convertView.findViewById(R.id.rb_zhusu_pk);
			holder.tvJiuDianPrice=(TextView) convertView.findViewById(R.id.tv_iiudian_price_num);
//			holder.tvZhuSuType=(TextView) convertView.findViewById(R.id.tv_zhusu_type);
			convertView.setTag(holder);
		}else{
			holder=(ZhuSuViewHolder) convertView.getTag();
		}
		ImageLoader imageLoader= ImageLoader.getInstance();
		if(!imageLoader.isInited())
		imageLoader.init(MyApplication.config);
		imageLoader.displayImage(Constance.HTTP_URL+datas.get(position).getPic(), holder.ivJiuDian,MyApplication.normalOption);
		holder.tvJiuDianName.setText(datas.get(position).getName());
		if(datas.get(position).getWeidu()!=null&&datas.get(position).getJindu()!=null){
			if(!(datas.get(position).getWeidu().equals(""))&&!(datas.get(position).getJindu().equals(""))){
				double dis=datas.get(position).getJuli();
				holder.tvJiuDianJuLi.setText(parseTo(dis)+"km");
			}
			
		}
//		if(parseTo(datas.get(position).getJuli())>10000){
//			holder.tvJiuDianJuLi.setVisibility(View.INVISIBLE);
//		}
		if(datas.get(position).getSatisfaction()!=null){
			float a=datas.get(position).getSatisfaction();
			int b=(int) a;
			holder.rbZhuSuPk.setNumStars(b);
		}
		NumberFormat format=new DecimalFormat("#");
		holder.tvJiuDianPrice.setText(format.format(datas.get(position).getPrice())+"");
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent it=new Intent(mContext, ActivityZhuSuDetail.class);
				it.putExtra("zhusu", datas.get(position));
				mContext.startActivity(it);
				
			}
		});
		return convertView;
	}
	public AdapterZhuSu(Context mContext, List<Merchant1> datas) {
		super();
		this.mContext = mContext;
		this.datas = datas;
	}
	class ZhuSuViewHolder{
		ImageView ivJiuDian;
		TextView tvJiuDianJuLi;
		TextView tvJiuDianName;
		RatingBar rbZhuSuPk;
		TextView tvJiuDianPrice;
		TextView tvZhuSuType;
	}
	private double parseTo(double sum) {
		double f = sum;
		BigDecimal b = new BigDecimal(f);
		double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1;

	}
}
