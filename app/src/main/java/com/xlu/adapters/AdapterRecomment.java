package com.xlu.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pandacard.teavel.R;
import com.pandacard.teavel.apps.MyApplication;
import com.xlu.po.ProductSpecal;
import com.xlu.uis.ActivityXianluDetail;
import com.xlu.utils.Constance;

import java.util.List;


public class AdapterRecomment extends BaseAdapter {
	Context context;
	List<ProductSpecal> datas;
	
	public AdapterRecomment(Context context, List<ProductSpecal> datas) {
		super();
		this.context = context;
		this.datas = datas;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos=position;
		ViewHolder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.gv_recommend_zone_item,null);
			holder=new ViewHolder();
			holder.iv=(ImageView) convertView.findViewById(R.id.iv_recomment_zone);
			holder.tv=(TextView) convertView.findViewById(R.id.tv_recomment_text);
			holder.tvMemo= (TextView) convertView.findViewById(R.id.tv_recomment_memo);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		ImageLoader imageLoader= ImageLoader.getInstance();
		if(!imageLoader.isInited())
		imageLoader.init(MyApplication.config);
		imageLoader.displayImage(Constance.HTTP_URL+datas.get(position).getPic(),holder.iv);
		holder.tv.setText(datas.get(position).getName());
		holder.tvMemo.setText(datas.get(position).getDesc_());
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it=new Intent(context, ActivityXianluDetail.class);
				it.putExtra("zone",datas.get(pos));
				context.startActivity(it);
				
			}
		});
		return convertView;
	}
	class ViewHolder{
		TextView tv;
		ImageView iv;
		TextView tvMemo;
	}

}
