package com.pandacard.teavel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.https.beans.CaerdOrderDetalBean;
import com.pandacard.teavel.uis.MineOrderDetal;
import com.pandacard.teavel.utils.LUtils;
import com.pandacard.teavel.utils.SplitString;
import com.pandacard.teavel.utils.Yuan2fen;

import java.util.List;

public class reclcleAdapter extends RecyclerView.Adapter<reclcleAdapter.ViewHolder> {

    private Context mContext;

    private List<CaerdOrderDetalBean.ExtraBean> mStringList;
    private String TAG = "reclcleAdapter";

    private OnItemLongClickedListener onItemlongClickedListener;
    private OnItemClickedListener onItemClickedListener;


    public reclcleAdapter(MineOrderDetal mineCarsDetal) {
        this.mContext = mineCarsDetal;
    }

    public void setStringList(List<CaerdOrderDetalBean.ExtraBean> stringList) {
        this.mStringList = stringList;

    }

    @NonNull
    @Override
    public reclcleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reclcleadapter_item, parent, false);
        LUtils.d(TAG, "onCreateViewHolder");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull reclcleAdapter.ViewHolder holder, final int position) {
        CaerdOrderDetalBean.ExtraBean extraBean = mStringList.get(position);
        String cardCode = extraBean.getCardCode();
        String orderId = extraBean.getOrderId();
        String orderTime = extraBean.getOrderTime();
        String sorderTime = SplitString.spStrig(orderTime);
        int status = extraBean.getStatus();
        int amount = extraBean.getAmount();

        String s = Yuan2fen.changeF2Y(amount);

        holder.cardinfo_money.setText(s + "元");
        holder.cardinfo_orderId.setText(orderId);
        holder.cardinfo_time.setText(sorderTime);
        holder.cardinfo_cardnum.setText(cardCode);


        if (status == 0) {
            holder.cardinfo_quancun.setText(R.string.eidactivity_nquancun);
            holder.cardinfo_quancun.setTextColor(mContext.getResources().getColor(R.color.B23008));
        } else if (status == 1) {
            holder.cardinfo_quancun.setText(R.string.eidactivity_yquancun);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickedListener.onItemClickedListeners(position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                onItemlongClickedListener.onItemlongClickedListeners(position);
                return true;
            }
        });

    }

    public interface OnItemLongClickedListener {
        void onItemlongClickedListeners(int position);
    }

    public void setOnItemLongClickedListener(OnItemLongClickedListener onItemlongClickedListener) {
        this.onItemlongClickedListener = onItemlongClickedListener;
    }

    public interface OnItemClickedListener {
        void onItemClickedListeners(int position);
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    @Override
    public int getItemCount() {
        LUtils.d(TAG, "getItemCount");
        return mStringList != null ? mStringList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView cardinfo_money;
        private TextView cardinfo_orderId;
        private TextView cardinfo_time;
        private TextView cardinfo_cardnum;
        private TextView cardinfo_quancun;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardinfo_money = itemView.findViewById(R.id.cardinfo_money);
            cardinfo_orderId = itemView.findViewById(R.id.cardinfo_type);
            cardinfo_time = itemView.findViewById(R.id.cardinfo_time);
            cardinfo_cardnum = itemView.findViewById(R.id.cardinfo_cardnum);
            cardinfo_quancun = itemView.findViewById(R.id.cardinfo_quancun);
        }
    }
}
