package com.xlu.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pandacard.teavel.R;
import com.pandacard.teavel.apps.MyApplication;
import com.pandacard.teavel.utils.LUtils;
import com.xlu.po.Merchant1;
import com.xlu.uis.ActivityTicketShow;
import com.xlu.utils.Constance;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;


public class AdapterTicketList extends BaseAdapter {

    List<Merchant1> list;
    Context context;


    public AdapterTicketList(List<Merchant1> list, Context context) {
        super();
        this.list = list;
        this.context = context;
    }

    public void setList(List<Merchant1> list) {
        this.list = list;
    }

    class TextViewHolder {
        public ImageView iv;
        public TextView tv_name;
        //		public TextView tv_city;
        public TextView tv_memo;
        public TextView tv_price;
        public TextView tv_price_old;
        public Button btn_buy;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_ticket_list, null);
            holder = new TextViewHolder();
            holder.iv = convertView.findViewById(R.id.iv);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
//			holder.tv_city = (TextView) convertView.findViewById(R.id.tv_city);
            holder.tv_memo = convertView.findViewById(R.id.tv_memo);
            holder.tv_price = convertView.findViewById(R.id.tv_price);
            holder.tv_price_old = convertView.findViewById(R.id.tv_price_old);
            holder.btn_buy = convertView.findViewById(R.id.btn);
            convertView.setTag(holder);
        } else {
            holder = (TextViewHolder) convertView.getTag();
        }
        final Merchant1 p = list.get(position);
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited())
            imageLoader.init(MyApplication.config);
        imageLoader.displayImage(
                Constance.HTTP_URL + p.getPic(), holder.iv, MyApplication.normalOption);
        holder.tv_name.setText(p.getName());
//		holder.tv_city.setText(App.city);
        NumberFormat format = new DecimalFormat("#");
        holder.tv_price.setText("￥" + format.format(p.getPrice()) + "起");
        holder.tv_price_old.setText("原价:￥" + format.format(p.getPrice_market()));
        holder.tv_price_old.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.tv_memo.setText(p.getName());
        holder.tv_memo.setMaxLines(2);
//		holder.tv_price_old.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
        holder.tv_price_old.getPaint().setFakeBoldText(true);
        holder.btn_buy.setClickable(false);

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                 LUtils.v("tag", "AdapterTicketList------------------------------------...=");
				Intent intent = new Intent(context, ActivityTicketShow.class);
				intent.putExtra("zone", p);
				context.startActivity(intent);
            }
        });

        return convertView;
    }
}
