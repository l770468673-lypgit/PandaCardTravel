package com.pandacard.teavel.adapters.fragments;

import android.content.Intent;
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
import com.pandacard.teavel.adapters.OneSelfCardOrderAdapter;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.small_routine_bean.CardsByUserId;
import com.pandacard.teavel.https.beans.small_routine_bean.queryUserOrderCardById;
import com.pandacard.teavel.uis.CardOrderDetalActivity;
import com.pandacard.teavel.utils.HttpRetrifitUtils;
import com.pandacard.teavel.utils.ShareUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * to handle interaction events.
 * Use the {@link Frag_OneSelfCardOrde#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frag_OneSelfCardOrde extends Fragment implements OneSelfCardOrderAdapter.AdapterItenClick {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String TAG = "Frag_AllCardOrder";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OneSelfCardOrderAdapter mAdapter;
    private List<CardsByUserId.CardListBean> mCardList;

    public Frag_OneSelfCardOrde() {
        // Required empty public constructor
    }

    private RecyclerView mAll_orderrecycle;


    private ImageView iamge_loaddate_anim;
    private AnimationDrawable mAnimaition;
    private RelativeLayout informationnull;
    private SelfUiHandler mHandler;

    class SelfUiHandler extends Handler {
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Frag_OneSelfCardOrde.
     */
    // TODO: Rename and change types and number of parameters
    public static Frag_OneSelfCardOrde newInstance(String param1, String param2) {
        Frag_OneSelfCardOrde fragment = new Frag_OneSelfCardOrde();
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
        mHandler = new SelfUiHandler();
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
        informationnull = inflate.findViewById(R.id.informationnull);
        mAnimaition.start();
        iamge_loaddate_anim.setVisibility(View.VISIBLE);

        mAll_orderrecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new OneSelfCardOrderAdapter(getActivity());
        mAll_orderrecycle.setAdapter(mAdapter);
        mAdapter.setOrderItenClick(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        informationnull.setVisibility(View.GONE);
        initDate();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initDate() {
        iamge_loaddate_anim.setVisibility(View.VISIBLE);
        Call<CardsByUserId> cardsByUserId = HttpManager.getInstance().getHttpClient3().getCardsByUserId(
                ShareUtil.getString(HttpRetrifitUtils.WECHAT_USERID),
                HttpRetrifitUtils.STATE_FINISH);
        cardsByUserId.enqueue(new Callback<CardsByUserId>() {
            @Override
            public void onResponse(Call<CardsByUserId> call, Response<CardsByUserId> response) {
                if (response.body() != null) {

                    mCardList = response.body().getCardList();
                    if (mCardList != null) {
                        mAdapter.setCardList(mCardList);
                        mAdapter.notifyDataSetChanged();
                        mAnimaition.stop();

                        mHandler.sendEmptyMessageDelayed(10002, 200);
                    } else {
                        mHandler.sendEmptyMessageDelayed(10001, 200);

                    }

                }

            }

            @Override
            public void onFailure(Call<CardsByUserId> call, Throwable t) {

                mHandler.sendEmptyMessage(10001);
                //                mLly_addressdetal.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void setClickBuy(int position) {
        checkOrderDetal(mCardList.get(position).getId());

    }

    @Override
    public void setClickSend(int position) {

    }


    private void checkOrderDetal(String id) {
        Call<queryUserOrderCardById> queryUserOrderCardByIdCall =
                HttpManager.getInstance().getHttpClient3().queryUserOrderCardById(id);
        queryUserOrderCardByIdCall.enqueue(new Callback<queryUserOrderCardById>() {
            @Override
            public void onResponse(Call<queryUserOrderCardById> call, Response<queryUserOrderCardById> response) {
                if (response.body() != null) {
                    Intent intent = new Intent(getActivity(), CardOrderDetalActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("CardInfocade", response.body().getCardInfo().getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<queryUserOrderCardById> call, Throwable t) {

            }
        });
    }
}
