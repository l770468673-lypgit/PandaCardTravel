package com.pandacard.teavel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.utils.LUtils;

import java.util.List;

public class CardAdapter extends BaseAdapter {
    private List<String> mStringList;

    private Context mContext;

    public CardAdapter(Context context) {
        this.mContext = context;
    }

    public void setStringList(List<String> stringList) {
        this.mStringList = stringList;
    }

    @Override
    public int getCount() {
        return mStringList != null ? mStringList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mStringList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_cards, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String s = mStringList.get(position);
        LUtils.d("SaveMoneyActivity", "s===========" + s);
        holder.resalcards.setText(s);
        return convertView;
    }

    class ViewHolder {
        TextView resalcards;

        public ViewHolder(View convertView) {
            resalcards = convertView.findViewById(R.id.resalcards);


        }
    }

}
