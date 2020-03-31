package com.pandacard.teavel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.https.beans.small_routine_bean.MyOrderList;
import com.pandacard.teavel.utils.LUtils;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    private static String TAG = "MyOrderAdapter";
    private Context mContext;
    private List<MyOrderList.OrderListBean> mOrderList;

    public AdapterItenClick mAdapterItenClick;

    public void setAdapterItenClick(AdapterItenClick adapterItenClick) {
        mAdapterItenClick = adapterItenClick;
    }

    public interface AdapterItenClick {
        void setClickDetal(int position);

        void setClickLogistics(int position);

    }

    public MyOrderAdapter(FragmentActivity activity) {
        this.mContext = activity;
    }


    public void setOrderList(List<MyOrderList.OrderListBean> orderList) {
        this.mOrderList = orderList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_recycle_all, parent, false);
        LUtils.d(TAG, "onCreateViewHolder");
        return new MyOrderAdapter.ViewHolder(view);
    }

    //0待付款 1已付款 2待收货 3已完成 4已失效(生成订单超时未支付)5已评论 6支付失败7（申请退款）退款中8退款成功9退款失败
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        LUtils.d(TAG, "mOrderList.size==" + mOrderList.size());

        final MyOrderList.OrderListBean orderListBean = mOrderList.get(position);
        holder.allorder_ordename.setText(orderListBean.getProductName());
        holder.allorder_ordernum.setText("订单号：" + orderListBean.getLyzyOrderCode());


        if (orderListBean.getOrderState() == 0) {
            holder.allorder_orderstate.setText("待付款");
        } else if (orderListBean.getOrderState() == 1) {
            holder.allorder_orderstate.setText("已付款");
        } else if (orderListBean.getOrderState() == 2) {
            holder.allorder_orderstate.setText("待收货");
        } else if (orderListBean.getOrderState() == 3) {
            holder.allorder_orderstate.setText("已完成");
        } else if (orderListBean.getOrderState() == 4) {
            holder.allorder_orderstate.setText("已失效");
        } else if (orderListBean.getOrderState() == 5) {
            holder.allorder_orderstate.setText("已评论");
        } else if (orderListBean.getOrderState() == 6) {
            holder.allorder_orderstate.setText("支付失败");
        } else if (orderListBean.getOrderState() == 7) {
            holder.allorder_orderstate.setText("已付款");
        } else if (orderListBean.getOrderState() == 8) {
            holder.allorder_orderstate.setText("退款中");
        } else if (orderListBean.getOrderState() == 9) {
            holder.allorder_orderstate.setText("退款失败");
        }
//        holder.allorder_orderlookdetal.setText("查看详情");
        holder.allorder_orderlookdetal.setVisibility(View.GONE);
        holder.allorder_ordelogistics.setText("查看物流");

        holder.allorder_ordercountmoney.setText("合计  :" + orderListBean.getPayPrice() + "");
//        holder.allorder_ordercolortype.setText("==========");
//        holder.allorder_ordercount.setText("===00==");
//        holder.allorder_ordermoney.setText("===00==");


        holder.allorder_orderlookdetal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LUtils.d(TAG, "查看详情");
                String id = orderListBean.getId();
                LUtils.d(TAG, "mOrderList.id==" + id);
//                mAdapterItenClick.setClickDetal(position);

            }
        });


        holder.allorder_ordelogistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LUtils.d(TAG, "查看物流");
//                mAdapterItenClick.setClickLogistics(position);
            }
        });


    }


    @Override
    public int getItemCount() {
        return mOrderList != null ? mOrderList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView allorder_ordernum;
        private TextView allorder_orderstate;
        private TextView allorder_ordename;
        private TextView allorder_ordermoney;
        private TextView allorder_ordercolortype;
        private TextView allorder_ordercount;
        private TextView allorder_ordercountmoney;
        private TextView allorder_orderlookdetal;
        private TextView allorder_ordelogistics;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            allorder_ordernum = itemView.findViewById(R.id.allorder_ordernum);
            allorder_orderstate = itemView.findViewById(R.id.allorder_orderstate);
            allorder_ordename = itemView.findViewById(R.id.allorder_ordename);
            allorder_ordermoney = itemView.findViewById(R.id.allorder_ordermoney);
            allorder_ordercolortype = itemView.findViewById(R.id.allorder_ordercolortype);
            allorder_ordercount = itemView.findViewById(R.id.allorder_ordercount);
            allorder_ordercountmoney = itemView.findViewById(R.id.allorder_ordercountmoney);
            allorder_orderlookdetal = itemView.findViewById(R.id.allorder_orderlookdetal);
            allorder_ordelogistics = itemView.findViewById(R.id.allorder_ordelogistics);
        }
    }
}
