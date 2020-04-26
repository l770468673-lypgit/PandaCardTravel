package com.pandacard.teavel.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.https.beans.small_routine_bean.CardsByUserId;
import com.pandacard.teavel.utils.LUtils;

import java.util.List;

//extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder>
public class OneSelfCardOrderAdapter extends RecyclerView.Adapter<OneSelfCardOrderAdapter.ViewHolder> {

    private static String TAG = "AllCardOrderAdapter";
    List<CardsByUserId.CardListBean> cardList;
    private Context mContext;

    public void setCardList(List<CardsByUserId.CardListBean> cardList) {
        this.cardList = cardList;
    }

    public interface AdapterItenClick {
        void setClickBuy(int position);

        void setClickSend(int position);

    }

    public AdapterItenClick mAdapterItenClick;

    public void setOrderItenClick(AdapterItenClick adapterItenClick) {
        mAdapterItenClick = adapterItenClick;
    }

    public OneSelfCardOrderAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reclclownbyadapter_item, parent, false);
        LUtils.d(TAG, "onCreateViewHolder");
        return new OneSelfCardOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CardsByUserId.CardListBean cardListBean = cardList.get(position);

        holder.allorder_ordernum.setText("订单号：" + cardListBean.getOrderCode());

        if (cardListBean.getCardOrderStatus() == 1) {
            holder.allorder_orderstate.setText("可使用");
        } else if (cardListBean.getCardOrderStatus() == 2) {
            holder.allorder_orderstate.setText("已自购");
        } else if (cardListBean.getCardOrderStatus() == 3) {
            holder.allorder_orderstate.setText("已赠送");
        }
        holder.allorder_ordercolortype.setText(cardListBean.getBuyNum() + "件");
        holder.allorder_ordercountmoney.setText(cardListBean.getCreateTime());

        holder.allorder_orderlookdetal.setText("查看详情");
        holder.allorder_ordelogistics.setVisibility(View.GONE);
        holder.allorder_orderlookdetal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterItenClick.setClickBuy(position);
            }
        });
        holder.allorder_ordelogistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterItenClick.setClickSend(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cardList != null ? cardList.size() : 0;
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
