package com.pandacard.teavel.uis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.adapters.pandainfoAdapter;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.bean_addCards;
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

public class MinePandaCards extends AppCompatActivity implements pandainfoAdapter.OnItemLongClickedListener, View.OnClickListener {


    private ImageView mAttbarimageview;
    private TextView mTextv_nodate;

    private List<String> mStrings;
    private String TAG = "MinePandaCards";
    private RecyclerView mCardinfo_recycle;

    private List<String> mcards = new ArrayList<>();
    private pandainfoAdapter mPandainfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panda_cards_info);
        StatusBarUtil.setDrawable(this, R.drawable.mine_title_jianbian);
        initview();

    }


    @Override
    public void onResume() {
        super.onResume();
        lodadaCard();
    }


    private void lodadaCard() {
        mcards.clear();
        if (HttpRetrifitUtils.SERNAME_PHONE != null) {

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
                            mPandainfoAdapter.setStringList(mcards);
                            mPandainfoAdapter.notifyDataSetChanged();
                            mTextv_nodate.setVisibility(View.GONE);
                            mCardinfo_recycle.setVisibility(View.VISIBLE);
                        } else {
                            mTextv_nodate.setVisibility(View.VISIBLE);
                            mCardinfo_recycle.setVisibility(View.GONE);
                            mPandainfoAdapter.setStringList(mcards);
                            mPandainfoAdapter.notifyDataSetChanged();
                            ToastUtils.showToast(MinePandaCards.this, body.getMsg());
                        }

                    }
                }

                @Override
                public void onFailure(Call<cardsbean> call, Throwable t) {

                }
            });
        } else {
            ToastUtils.showToast(this, "登录后再试");
        }

    }

    private void initview() {
        mAttbarimageview = findViewById(R.id.mincardattbarimageview);
        mCardinfo_recycle = findViewById(R.id.pandcardinfo_recycle);
        mCardinfo_recycle.setLayoutManager(new LinearLayoutManager(this));

        mTextv_nodate = findViewById(R.id.textv_nodate);

        mCardinfo_recycle.setLayoutManager(new LinearLayoutManager(this));
        mPandainfoAdapter = new pandainfoAdapter(this);
        mCardinfo_recycle.setAdapter(mPandainfoAdapter);

        mPandainfoAdapter.setOnItemLongClickedListener(this);
        mAttbarimageview.setOnClickListener(this);

    }

    @Override
    public void onItemlongClickedListeners(int position) {
        String s = mcards.get(position);
        Call<bean_addCards> bean_personCall = HttpManager.getInstance().getHttpClient().deletePCard(s);
        bean_personCall.enqueue(new Callback<bean_addCards>() {
            @Override
            public void onResponse(Call<bean_addCards> call, Response<bean_addCards> response) {
                if (response.body() != null) {
                    if (response.body().getCode() == 1) {
                        ToastUtils.showToast(MinePandaCards.this, response.body().getMsg());
                        mPandainfoAdapter.notifyDataSetChanged();
                        lodadaCard();
                    }
                }
            }

            @Override
            public void onFailure(Call<bean_addCards> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mincardattbarimageview:

                finish();
                break;
        }
    }
}
