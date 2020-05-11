package com.xlu.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.pandacard.teavel.R;
import com.xlu.po.RouteInfos;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by giant on 2017/7/21.
 */

    public class SelectRouteMapWindow extends PopupWindow {
    private View conentView=null;
    private GridView gvCity;
    private OnSelectChangeListener listener;

    private List<RouteInfos> citys = new ArrayList<RouteInfos>();

    public SelectRouteMapWindow(Context context, List<RouteInfos> list,
                                OnSelectChangeListener listener1) {
        this.listener = listener1;
        this.citys = list != null ? list : citys;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.popwindow_select_city, null);
        gvCity = (GridView) conentView.findViewById(R.id.gv_city);
        gvCity.setAdapter(new CityAdapter(context));


        this.setContentView(conentView);
        this.setFocusable(true);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        conentView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                SelectRouteMapWindow.this.dismiss();
            }
        });
        gvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                listener.onSelectChange(citys.get(position));
                dismiss();
            }
        });
    }

    public OnSelectChangeListener getListener() {
        return listener;
    }

    public void setListener(OnSelectChangeListener listener) {
        this.listener = listener;
    }

    public interface OnSelectChangeListener {
        void onSelectChange(RouteInfos route);
    }

    public class CityAdapter extends BaseAdapter {

        private Context context;

        public CityAdapter(Context context) {
            // TODO Auto-generated constructor stub
            this.context = context;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return citys.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if(convertView==null){
                convertView= LayoutInflater.from(context).inflate(R.layout.popwindow_layer_button,null);
            }

            Button btnLayer = (Button) convertView.findViewById(R.id.btn_select);
//			if (!citys.get(position).getName().equals("全国"))
//				tvCity.setBackgroundResource(R.drawable.rectangle1_city);
            btnLayer.setTextSize(16);
            btnLayer.setGravity(Gravity.CENTER);
            btnLayer.setText(citys.get(position).getName());
            int width = DensityUtil.dip2px(context, 5);
            btnLayer.setPadding(0, width, 0, width);
            if (citys.get(position).getName().equals(""))
            {
                btnLayer.setVisibility(View.INVISIBLE);

            }
            btnLayer.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    listener.onSelectChange(citys.get(position));
                    dismiss();

                }
            });
            return convertView;
//			TextView tvCity = new TextView(context);
//			if (!citys.get(position).getName().equals("全国"))
//				tvCity.setBackgroundResource(R.drawable.rectangle_default_layer);
//			tvCity.setTextSize(15);
//			tvCity.setTextColor(0xff555555);
//			tvCity.setGravity(Gravity.CENTER);
//			tvCity.setText(citys.get(position).getName());
//			int width = DensityUtil.dip2px(context, 5);
//			tvCity.setPadding(0, width, 0, width);
//			if (citys.get(position).getName().equals(""))
//				tvCity.setVisibility(tvCity.INVISIBLE);
//			return tvCity;
        }

    }
}
