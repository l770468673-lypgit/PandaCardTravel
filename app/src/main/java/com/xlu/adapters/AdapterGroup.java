package com.xlu.adapters;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pandacard.teavel.R;
import com.pandacard.teavel.apps.MyApplication;
import com.pandacard.teavel.utils.LUtils;
import com.xlu.po.ProductSpecal;
import com.xlu.uis.ActivityOrderVisitDetail;
import com.xlu.utils.Constance;
import com.xlu.widgets.EllipsizeText;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


public class AdapterGroup extends BaseAdapter {
	private static  String TAG="AdapterGroup";

	private LayoutInflater inflater;
	private Context context;
	private List<ProductSpecal> listTuanInfos;

	public AdapterGroup(Context context, List<ProductSpecal> listTuanInfos) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.listTuanInfos = listTuanInfos;
	}

	public int getCount() {
		return listTuanInfos.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		final ProductSpecal tuanInfo = listTuanInfos.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_group, null);
			holder.ivGroupPic = (ImageView) convertView
					.findViewById(R.id.iv_group_pic);
			holder.tvName = (EllipsizeText) convertView
					.findViewById(R.id.tv_group_name);
			holder.tvPrice = (TextView) convertView
					.findViewById(R.id.tv_group_price);
			holder.tvGanshou = (EllipsizeText) convertView
					.findViewById(R.id.tv_xinalu_ganshou);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvName.setText(tuanInfo.getName());
		if (tuanInfo.getName().length() > 18)
		{
			holder.tvName.setMinLines(2);
			holder.tvName.setMaxEms(18);
		}
		
		else {
			holder.tvName.setMinLines(1);
			holder.tvName.setMaxEms(18);
		}
		ImageLoader imageLoader=ImageLoader.getInstance();
		if(!imageLoader.isInited())
		imageLoader.init(MyApplication.config);
		imageLoader.displayImage(
				Constance.HTTP_URL + tuanInfo.getPic(), holder.ivGroupPic,
				MyApplication.normalOption);

		LUtils.d(TAG,"getPic==="+Constance.HTTP_URL + tuanInfo.getPic());
//		 LayoutParams params = (LayoutParams) holder.ivGroupPic
//		 .getLayoutParams();
//		 params.width=Constance.width;
//		 params.height = (int) (Constance.width/1280f*700);
//		 holder.ivGroupPic.setLayoutParams(params);
		NumberFormat format=new DecimalFormat("#");
		if (tuanInfo.getPrice() != 0) {
			holder.tvPrice.setText("￥" + format.format(tuanInfo.getPrice()));
		} else {
			holder.tvPrice.setVisibility(View.GONE);
		}
		holder.tvGanshou.setText(tuanInfo.getDesc_());
		if (tuanInfo.getDesc_().length() < 16){
			holder.tvGanshou.setMaxLines(1);
			holder.tvGanshou.setMaxEms(16);
		}	else {
			holder.tvGanshou.setMaxLines(2);
			holder.tvGanshou.setMaxEms(16);
		}
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(context, ActivityOrderVisitDetail.class);
				intent.putExtra("tuanInfo",tuanInfo);
				context.startActivity(intent);
				intent=null;
			}
		});

		return convertView;
	}

	class ViewHolder {
		ImageView ivGroupPic;
		EllipsizeText tvName;
		TextView tvPrice;
		EllipsizeText tvGanshou;

	}

}
