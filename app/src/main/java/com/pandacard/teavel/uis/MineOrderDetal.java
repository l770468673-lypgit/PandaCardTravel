package com.pandacard.teavel.uis;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.adapters.reclcleAdapter;
import com.pandacard.teavel.bases.BasePandaActivity;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.CaerdOrderDetalBean;
import com.pandacard.teavel.https.beans.cardsbean;
import com.pandacard.teavel.utils.HttpRetrifitUtils;
import com.pandacard.teavel.utils.LUtils;
import com.pandacard.teavel.utils.ShareUtil;
import com.pandacard.teavel.utils.StatusBarUtil;
import com.pandacard.teavel.utils.ToastUtils;
import com.pandacard.teavel.utils.UserByteUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MineOrderDetal extends BasePandaActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, reclcleAdapter.OnItemLongClickedListener, reclcleAdapter.OnItemClickedListener {


    private ImageView mAttbarimageview;
    private Spinner mSpinner;
    private ArrayAdapter mAdapter;
    private List<String> mcards = new ArrayList<>();
    private List<String> mStrings;
    private String TAG = "MineOrderDetal";
    private RecyclerView mCardinfo_recycle;
    private reclcleAdapter mReclcleAdapter;
    private List<CaerdOrderDetalBean.ExtraBean> mExtra;
    private TextView mTextv_nodate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_cars_detal);

        StatusBarUtil.setDrawable(this, R.drawable.mine_title_jianbian);
        initview();
        loadSpinnerdate();
    }

    private void loadSpinnerdate() {
        mcards.clear();
        Call<cardsbean> cards = HttpManager.getInstance().getHttpClient().getCards(ShareUtil.getString(HttpRetrifitUtils.SERNAME_PHONE));

        cards.enqueue(new Callback<cardsbean>() {
            @Override
            public void onResponse(Call<cardsbean> call, Response<cardsbean> response) {
                if (response.body() != null) {
                    cardsbean body = response.body();
                    if (body.getCode() == 1) {
                        String cards1 = body.getExtra().getCards();
                        mStrings = UserByteUtils.spliteStrWithBlank(cards1);
                        LUtils.d(TAG, "mStrings===" + mStrings);
                        for (int i = 0; i < mStrings.size(); i++) {
                            mcards.add(mStrings.get(i));
                        }
                        LUtils.d(TAG, "mcards===" + mcards);
                        mAdapter = new ArrayAdapter<String>(MineOrderDetal.this,
                                android.R.layout.simple_spinner_item, mcards);
                        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinner.setAdapter(mAdapter);
                    } else {
                        ToastUtils.showToast(MineOrderDetal.this, body.getMsg());
                    }

                }
            }

            @Override
            public void onFailure(Call<cardsbean> call, Throwable t) {

            }
        });

    }

    private void initview() {
        mAttbarimageview = findViewById(R.id.mincardattbarimageview);
        mSpinner = findViewById(R.id.spinner);
        mCardinfo_recycle = findViewById(R.id.cardinfo_recycle);
        mTextv_nodate = findViewById(R.id.textv_nodate);


        mCardinfo_recycle.setLayoutManager(new LinearLayoutManager(this));

        mReclcleAdapter = new reclcleAdapter(this);
        mCardinfo_recycle.setAdapter(mReclcleAdapter);

        mReclcleAdapter.setOnItemClickedListener(this);
        mReclcleAdapter.setOnItemLongClickedListener(this);
        mAttbarimageview.setOnClickListener(this);
        mSpinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mincardattbarimageview:
                finish();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String s = mcards.get(position);
        //        ToastUtils.showToast(this, s);

        loadCardsInfo(s);


    }

    private void loadCardsInfo(String s) {
        mExtra = null;
        Call<CaerdOrderDetalBean> orders = HttpManager.getInstance().getHttpClient().getOrders(s);
        orders.enqueue(new Callback<CaerdOrderDetalBean>() {
            @Override
            public void onResponse(Call<CaerdOrderDetalBean> call, Response<CaerdOrderDetalBean> response) {
                if (response.body() != null) {
                    if (response.body().getCode() == 1) {
                        mExtra = response.body().getExtra();

                        //                        runOnUiThread(new Runnable() {
                        //                            @Override
                        //                            public void run() {
                        mReclcleAdapter.setStringList(mExtra);
                        mReclcleAdapter.notifyDataSetChanged();
                        mTextv_nodate.setVisibility(View.GONE);
                        mCardinfo_recycle.setVisibility(View.VISIBLE);
                        //                            }
                        //                        });
                    } else {
                        mReclcleAdapter.setStringList(mExtra);
                        mReclcleAdapter.notifyDataSetChanged();
                        mTextv_nodate.setVisibility(View.VISIBLE);
                        mCardinfo_recycle.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<CaerdOrderDetalBean> call, Throwable t) {
                mTextv_nodate.setVisibility(View.VISIBLE);
                mCardinfo_recycle.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemlongClickedListeners(int position) {

        LUtils.d(TAG, "onItemlongClickedListeners");
    }

    @Override
    public void onItemClickedListeners(int position) {
        LUtils.d(TAG, "onItemClickedListeners");
    }
}
