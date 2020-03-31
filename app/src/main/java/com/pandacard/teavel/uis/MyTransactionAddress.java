package com.pandacard.teavel.uis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.adapters.AddressAdapter;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.small_routine_bean.MyAddressByUserId;
import com.pandacard.teavel.utils.HttpRetrifitUtils;
import com.pandacard.teavel.utils.ShareUtil;
import com.pandacard.teavel.utils.StatusBarUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTransactionAddress extends AppCompatActivity implements View.OnClickListener {

    private TextView mAddmytransaddress;
    private ImageView mLly_attbarimageview;
    private RecyclerView mAddressrecycle;
    private AddressAdapter mAddressAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_transaction_address);
        StatusBarUtil.setDrawable(this, R.drawable.mine_title_jianbian);

        initView();


    }

    private void initDate() {
        Call<MyAddressByUserId> myAddressByUserId = HttpManager.getInstance().getHttpClient3().
                getMyAddressByUserId(ShareUtil.getString(HttpRetrifitUtils.WECHAT_USERID));
        myAddressByUserId.enqueue(new Callback<MyAddressByUserId>() {
            @Override
            public void onResponse(Call<MyAddressByUserId> call, Response<MyAddressByUserId> response) {

                if (response.body() != null) {
                    List<MyAddressByUserId.AddressListBean> addressList = response.body().getAddressList();
                    mAddressAdapter.setAddressList(addressList);
                    mAddressAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MyAddressByUserId> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDate();
    }

    private void initView() {
        mAddmytransaddress = findViewById(R.id.addmytransaddress);
        mLly_attbarimageview = findViewById(R.id.lly_attbarimageview);
        mAddressrecycle = findViewById(R.id.addressrecycle);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mAddressrecycle.setLayoutManager(layoutManager);

        mAddressAdapter = new AddressAdapter(this);
        mAddressrecycle.setAdapter(mAddressAdapter);

        mAddmytransaddress.setOnClickListener(this);
        mLly_attbarimageview.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addmytransaddress:

                Intent intent = new Intent(this, AddNewAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.lly_attbarimageview:
                finish();
                break;

        }
    }
}
