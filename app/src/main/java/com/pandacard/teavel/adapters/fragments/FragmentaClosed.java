package com.pandacard.teavel.adapters.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pandacard.teavel.R;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.small_routine_bean.MyOrderList;
import com.pandacard.teavel.utils.HttpRetrifitUtils;
import com.pandacard.teavel.utils.ShareUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FragmentaClosed#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentaClosed extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public FragmentaClosed() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentaClosed.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentaClosed newInstance(String param1, String param2) {
        FragmentaClosed fragment = new FragmentaClosed();
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
        return inflater.inflate(R.layout.fragmenta_closed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadDate();
    }
    private void loadDate() {

        Call<MyOrderList> myOrderList = HttpManager.getInstance().getHttpClient3().getMyOrderList(
                ShareUtil.getString(HttpRetrifitUtils.WECHAT_USERID), -1);
        myOrderList.enqueue(new Callback<MyOrderList>() {
            @Override
            public void onResponse(Call<MyOrderList> call, Response<MyOrderList> response) {
                if (response.body()!=null){
                    MyOrderList body = response.body();
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
