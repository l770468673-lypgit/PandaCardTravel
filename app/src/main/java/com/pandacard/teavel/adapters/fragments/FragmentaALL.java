package com.pandacard.teavel.adapters.fragments;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.adapters.MyOrderAdapter;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.small_routine_bean.MyOrderList;
import com.pandacard.teavel.utils.HttpRetrifitUtils;
import com.pandacard.teavel.utils.LUtils;
import com.pandacard.teavel.utils.ShareUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FragmentaALL#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentaALL extends Fragment implements MyOrderAdapter.AdapterItenClick {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String TAG = "FragmentaALL";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<MyOrderList.OrderListBean> mOrderList;

    private RecyclerView mAll_orderrecycle;
    private MyOrderAdapter mAdapter;

    private ImageView iamge_loaddate_anim;
    private AnimationDrawable mAnimaition;

    public FragmentaALL() {
        // Required empty public constructor
    }

    private RelativeLayout informationnull;
    private AllUiHandler mHandler;

    class AllUiHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10001:
                    mAnimaition.stop();
                    iamge_loaddate_anim.setVisibility(View.GONE);
                    informationnull.setVisibility(View.VISIBLE);
                    break;
                case 10002:
                    mAnimaition.stop();
                    iamge_loaddate_anim.setVisibility(View.GONE);
                    informationnull.setVisibility(View.GONE);
                    break;

            }
        }
    }
    public static FragmentaALL newInstance(String param1, String param2) {
        FragmentaALL fragment = new FragmentaALL();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mHandler = new AllUiHandler();
        View inflate = inflater.inflate(R.layout.fragment_blank, container, false);
        initView(inflate);
        return inflate;
    }

    private void initView(View inflate) {
        mAll_orderrecycle = inflate.findViewById(R.id.all_orderrecycle);
        iamge_loaddate_anim = inflate.findViewById(R.id.spaiamge_loaddate_anim);
        iamge_loaddate_anim.setBackgroundResource(R.drawable.load_date_anim);
        informationnull = inflate.findViewById(R.id.informationnull);
        mAnimaition = (AnimationDrawable) iamge_loaddate_anim.getBackground();
        mAnimaition.setOneShot(false);

        mAnimaition.start();
        iamge_loaddate_anim.setVisibility(View.VISIBLE);

    }

    @Override
    public void onResume() {
        super.onResume();
        informationnull.setVisibility(View.GONE);
        loadDate();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAll_orderrecycle.setLayoutManager(layoutManager);
        mAdapter = new MyOrderAdapter(getActivity());
        mAll_orderrecycle.setAdapter(mAdapter);
        mAdapter.setAdapterItenClick(this);

    }

    private void loadDate() {

        Call<MyOrderList> myOrderList = HttpManager.getInstance().getHttpClient3().getMyOrderList(
                ShareUtil.getString(HttpRetrifitUtils.WECHAT_USERID), HttpRetrifitUtils.STATE_ALL);
        myOrderList.enqueue(new Callback<MyOrderList>() {
            @Override
            public void onResponse(Call<MyOrderList> call, Response<MyOrderList> response) {
                if (response.body() != null) {
                    MyOrderList body = response.body();
                    mOrderList = body.getOrderList();

                    if (mOrderList != null) {
                        mAdapter.setOrderList(mOrderList);
                        mAdapter.notifyDataSetChanged();
                        mAnimaition.stop();

                        mHandler.sendEmptyMessageDelayed(10002, 200);
                    } else {
                        mHandler.sendEmptyMessageDelayed(10001, 200);

                    }
                }
            }

            @Override
            public void onFailure(Call<MyOrderList> call, Throwable t) {
                LUtils.d(TAG, "" + t);
                mHandler.sendEmptyMessageDelayed(10001, 200);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void setClickDetal(int position) {
//        MyOrderList.OrderListBean orderListBean = mOrderList.get(position);
//       checkOrderDetal(orderListBean.getId());


    }

    @Override
    public void setClickLogistics(int position) {

    }



}
