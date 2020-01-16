package com.pandacard.teavel.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.pandacard.teavel.R;
import com.pandacard.teavel.uis.NFCActivity;
import com.pandacard.teavel.utils.LUtils;

import java.util.List;
import java.util.Map;

public class GridViewAdapter extends BaseAdapter {
    private static String TAG = "GridViewAdapter";


    private NFCActivity mActivity;
    private int location=100;

    private List<Map<String, String>> mData_list;

    public GridViewAdapter(NFCActivity nfcActivity) {
        this.mActivity = nfcActivity;
    }

    public void setSeclection(int position) {
        location = position;
    }

    public void setData_list(List<Map<String, String>> data_list) {
        if (data_list.size() > 0) {
            this.mData_list = data_list;
            this.notifyDataSetChanged();

        }
    }

    @Override
    public int getCount() {

        return mData_list != null ? mData_list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mData_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_monry, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.realmoney.setText(mData_list.get(position).get("truemoney") + " 元");
//        holder.salemoney.setText("售价 ：" + mData_list.get(position).get("salemoney") + " 元");
//        if (mData_list.size() != position + 1) {
//            holder.realmoney.setText(mData_list.get(position).get("truemoney") + " 元");
//            holder.salemoney.setText("售价 ：" + mData_list.get(position).get("salemoney") + " 元");
//            LUtils.d(TAG, "mData_list.size()==" + mData_list.size());
//            LUtils.d(TAG, "position==" + position);
//        } else {
//            holder.realmoney.setText(mData_list.get(position).get("truemoney"));
//            holder.realmoney.setTextSize(16);
//            holder.salemoney.setText("");
//            //              holder.salemoney.setVisibility(View.GONE);
//            LUtils.d(TAG, " else mData_list.size()==" + mData_list.size());
//            LUtils.d(TAG, "else  position==" + position);
//
//
//
//        }

        if (position == location) {
            holder.gradviewid.setBackground(mActivity.getDrawable(R.drawable.nfcact_grid_itemclock_boxframe));
        } else {
            holder.gradviewid.setBackground(mActivity.getDrawable(R.drawable.nfcact_grid_item_boxframe));
        }


        return convertView;
    }

    class ViewHolder {
        TextView realmoney;
        TextView salemoney;
        LinearLayout gradviewid;

        public ViewHolder(View convertView) {
            gradviewid = (LinearLayout) convertView.findViewById(R.id.gradviewid);
            realmoney = (TextView) convertView.findViewById(R.id.resalmoney);
//            salemoney = (TextView) convertView.findViewById(R.id.salemoney);

        }
    }


}
