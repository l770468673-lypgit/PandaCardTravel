package com.pandacard.teavel.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.https.beans.small_routine_bean.MyAddressByUserId;
import com.pandacard.teavel.utils.LUtils;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private static String TAG = "AddressAdapter";
    private List<MyAddressByUserId.AddressListBean> addressList;
    private Activity mActivity;

    public interItemClickListener mItemLongClickListener;

    public interface interItemClickListener {
        void ItemLongClick(int po);

        void ItemClick(int po);

        void ItemClickback(int po);
    }

    public void setClickListener(interItemClickListener itemLongClickListener) {
        mItemLongClickListener = itemLongClickListener;
    }

    public void setAddressList(List<MyAddressByUserId.AddressListBean> addressList) {
        this.addressList = addressList;
    }

    public AddressAdapter(Activity activity) {
        mActivity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_detal
                        , parent, false);
        LUtils.d(TAG, "onCreateViewHolder");
        return new AddressAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        MyAddressByUserId.AddressListBean addressListBean = addressList.get(position);
        LUtils.d(TAG, "addressList.size===" + addressList.size());
        int isDefault = addressListBean.getIsDefault();
        if (isDefault == 1) {
            holder.textview_isdefault.setText("默认");
            holder.textview_isdefault.setTextColor(mActivity.getResources().getColor(R.color.red));
        } else {
            holder.textview_isdefault.setText("   ");
        }
        holder.addressnumber.setText(addressListBean.getPhoneNumber());
        holder.addressname.setText(addressListBean.getReceiver());
        holder.address_detal.setText(addressListBean.getProvinces() + "  " + addressListBean.getRegion() + "  " +
                addressListBean.getCountries() + "  " + addressListBean.getDetailAddress());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mItemLongClickListener.ItemLongClick(position);
                return true;
            }
        });
        holder.textview_editaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemLongClickListener.ItemClick(position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemLongClickListener.ItemClickback(position);
            }
        });
    }

    @Override
    public int getItemCount() {

        return addressList != null ? addressList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView addressname;
        private TextView addressnumber;
        private TextView address_detal;
        private TextView textview_isdefault;
        private TextView textview_editaddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textview_isdefault = itemView.findViewById(R.id.textview_isdefault);
            textview_editaddress = itemView.findViewById(R.id.textview_editaddress);
            addressname = itemView.findViewById(R.id.addressname);
            addressnumber = itemView.findViewById(R.id.addressnumber);
            address_detal = itemView.findViewById(R.id.address_detal);

        }
    }
}

