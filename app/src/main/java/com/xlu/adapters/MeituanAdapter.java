package com.xlu.adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pandacard.teavel.R;
import com.xlu.po.MeiTuanBean;
import com.xlu.utils.CommonAdapter;
import com.xlu.utils.ViewHolder;

import java.util.List;



/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */


public class MeituanAdapter extends CommonAdapter<MeiTuanBean> {
    public MeituanAdapter(Context context, int layoutId, List<MeiTuanBean> datas) {
        super(context, layoutId, datas);
    }



    @Override
    public void convert(ViewHolder holder, final MeiTuanBean cityBean) {
        holder.setText(R.id.tvCity, cityBean.getCity());
    }
}