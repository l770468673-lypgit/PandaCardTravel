package com.xlu.adapters;

import android.content.Context;
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
import com.xlu.po.ProInfo1;
import com.xlu.utils.Constance;

import java.util.List;


public class AdapterZhuSuDetail extends BaseAdapter {
	Context mcontext;
	List<ProInfo1> datas;

	public AdapterZhuSuDetail(Context mcontext) {
		super();
		this.mcontext = mcontext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	public AdapterZhuSuDetail(Context mcontext, List<ProInfo1> datas) {
		super();
		this.mcontext = mcontext;
		this.datas = datas;
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
		RoomViewHolder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(mcontext).inflate(R.layout.lv_item_zhusu_detail, null);
			holder=new RoomViewHolder();
			holder.ivRoomImg=(ImageView) convertView.findViewById(R.id.iv_room_img);
			holder.tvRoomDetail=(TextView) convertView.findViewById(R.id.tv_room_miaoshu);
			holder.tvRoomName=(TextView) convertView.findViewById(R.id.tv_room_name);
			holder.tvRoomOrder=(TextView) convertView.findViewById(R.id.tv_room_order);
			holder.tvRoomPrice=(TextView) convertView.findViewById(R.id.tv_room_price_num);
			holder.tvLubi= (TextView) convertView.findViewById(R.id.tv_my_lubi);
			convertView.setTag(holder);
		}else{
			holder=(RoomViewHolder) convertView.getTag();
		}
		ImageLoader imageLoader= ImageLoader.getInstance();
		if(!imageLoader.isInited())
		imageLoader.init(MyApplication.config);
		imageLoader.displayImage(Constance.HTTP_URL+datas.get(position).getPic(), holder.ivRoomImg,MyApplication.normalOption);
		String []temp=null;
		if(datas.get(position).getDesc_()!=null){
		holder.tvRoomDetail.setText(datas.get(position).getDesc_());	
		}
		holder.tvRoomName.setText(datas.get(position).getName());
		holder.tvRoomPrice.setText(datas.get(position).getPrice()+"");

			holder.tvLubi.setText("鹿币立减:"+(int)(datas.get(position).getCoupon_max()));

		holder.tvRoomOrder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mListener!=null)
					mListener.ButtonClick(v,position);
			}
		});

//		holder.tvBedArea.setText(datas.get(position).)
		return convertView;
	}
	class RoomViewHolder{
		ImageView ivRoomImg;
		TextView tvRoomName;
		TextView tvRoomPrice;
		TextView tvRoomDetail;
		TextView tvRoomOrder;
		TextView tvLubi;
	}
	ZhuSuButtonClicker mListener;

	public void setmListener(ZhuSuButtonClicker mListener) {
		this.mListener = mListener;
	}

	public interface ZhuSuButtonClicker{
		public void ButtonClick(View view, int position);
	}



}
