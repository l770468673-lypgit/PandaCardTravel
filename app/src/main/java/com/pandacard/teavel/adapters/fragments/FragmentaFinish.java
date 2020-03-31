package com.pandacard.teavel.adapters.fragments;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.adapters.MyOrderAdapter;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.small_routine_bean.MyOrderList;
import com.pandacard.teavel.utils.HttpRetrifitUtils;
import com.pandacard.teavel.utils.ShareUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FragmentaFinish#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentaFinish extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageView iamge_loaddate_anim;
    private AnimationDrawable mAnimaition;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<MyOrderList.OrderListBean> mOrderList;
    private RecyclerView mAll_orderrecycle;
    private MyOrderAdapter mAdapter;

    public FragmentaFinish() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentaFinish newInstance(String param1, String param2) {
        FragmentaFinish fragment = new FragmentaFinish();
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
        View inflate = inflater.inflate(R.layout.fragment_blank, container, false);
        initView(inflate);
        return inflate;
    }

    private void initView(View inflate) {
        mAll_orderrecycle = inflate.findViewById(R.id.all_orderrecycle);
        iamge_loaddate_anim = inflate.findViewById(R.id.spaiamge_loaddate_anim);
        iamge_loaddate_anim.setBackgroundResource(R.drawable.load_date_anim);
        mAnimaition = (AnimationDrawable) iamge_loaddate_anim.getBackground();
        mAnimaition.setOneShot(false);

        mAnimaition.start();
        iamge_loaddate_anim.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAll_orderrecycle.setLayoutManager(layoutManager);
        mAdapter = new MyOrderAdapter(getActivity());
        mAll_orderrecycle.setAdapter(mAdapter);
        loadDate();
    }


    private void loadDate() {

        Call<MyOrderList> myOrderList = HttpManager.getInstance().getHttpClient3().getMyOrderList(
                ShareUtil.getString(HttpRetrifitUtils.WECHAT_USERID), HttpRetrifitUtils.STATE_FINISH);
        myOrderList.enqueue(new Callback<MyOrderList>() {
            @Override
            public void onResponse(Call<MyOrderList> call, Response<MyOrderList> response) {
                if (response.body() != null) {
                    MyOrderList body = response.body();
                    mOrderList = body.getOrderList();
                    mAdapter.setOrderList(mOrderList);
                    mAdapter.notifyDataSetChanged();              mAnimaition.stop();
                    iamge_loaddate_anim.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<MyOrderList> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
