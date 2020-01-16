package com.pandacard.teavel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.uis.MinePandaCards;
import com.pandacard.teavel.utils.LUtils;

import java.util.List;

public class pandainfoAdapter extends RecyclerView.Adapter<pandainfoAdapter.ViewHolder> {

    private Context mContext;

    private List<String> mStringList;
    private String TAG = "reclcleAdapter";

    private OnItemLongClickedListener onItemlongClickedListener;
    private OnItemClickedListener onItemClickedListener;


    public pandainfoAdapter(MinePandaCards mineCarsDetal) {
        this.mContext = mineCarsDetal;
    }

    public void setStringList(List<String> stringList) {
        this.mStringList = stringList;
        LUtils.d(TAG, "stringList.toString()==" + stringList.toString());
    }

    @NonNull
    @Override
    public pandainfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pandainfo_item, parent, false);
        LUtils.d(TAG, "onCreateViewHolder");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull pandainfoAdapter.ViewHolder holder, final int position) {
        String s = mStringList.get(position);
        LUtils.d(TAG, "ss====" + s);
        LUtils.d(TAG, "onBindViewHolder");
        holder.cardinfo_money.setText(s);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               onItemClickedListener.onItemClickedListeners(position);
//            }
//        });

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
        private TextView cardinfo_type;
        private TextView cardinfo_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardinfo_money = itemView.findViewById(R.id.cardinfo_money);
            cardinfo_type = itemView.findViewById(R.id.cardinfo_type);
            cardinfo_time = itemView.findViewById(R.id.cardinfo_time);
        }
    }
}
