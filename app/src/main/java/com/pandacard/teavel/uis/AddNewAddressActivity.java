package com.pandacard.teavel.uis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.small_routine_bean.insertAddressInfo;
import com.pandacard.teavel.utils.HttpRetrifitUtils;
import com.pandacard.teavel.utils.ShareUtil;
import com.pandacard.teavel.utils.StatusBarUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mAddadress_saves;
    private ImageView mLly_attbarimageview;

    private EditText mAddress_username;
    private EditText mAddress_phonenum;
    private EditText mAddress_addressprovinces;
    private EditText mAddress_addresscity;
    private EditText mAdress_addresscounty;
    private EditText mAddress_addressdetal;
    private Button mAddress_changedefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);
        StatusBarUtil.setDrawable(this, R.drawable.mine_title_jianbian);
        initView();
    }


    private void initView() {
        mAddadress_saves = findViewById(R.id.adress_saves);
        mLly_attbarimageview = findViewById(R.id.lly_attbarimageview);
//--
        mAddress_username = findViewById(R.id.address_username);
        mAddress_phonenum = findViewById(R.id.address_phonenum);
        mAddress_addressprovinces = findViewById(R.id.address_addressprovinces);
        mAddress_addresscity = findViewById(R.id.address_addresscity);
        mAdress_addresscounty = findViewById(R.id.address_addresscounty);
        mAddress_addressdetal = findViewById(R.id.address_addressdetal);
        mAddress_changedefault = findViewById(R.id.address_changedefault);

        mAddadress_saves.setOnClickListener(this);
        mLly_attbarimageview.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.adress_saves:

                CommitAddaress();
                break;
            case R.id.lly_attbarimageview:
                finish();
                break;

        }
    }

    private void CommitAddaress() {
        if (mAddress_username.getText().toString().trim().length() > 0 &&
                mAddress_phonenum.getText().toString().trim().length() > 0 &&
                mAddress_addressprovinces.getText().toString().trim().length() > 0 &&
                mAddress_addresscity.getText().toString().trim().length() > 0 &&
                mAdress_addresscounty.getText().toString().trim().length() > 0 &&
                mAddress_addressdetal.getText().toString().trim().length() > 0
        ) {
            Call<insertAddressInfo> insertAddressInfoCall = HttpManager.getInstance().getHttpClient3().insertAddressInfo(
                    ShareUtil.getString(HttpRetrifitUtils.WECHAT_USERID),
                    mAddress_addressprovinces.getText().toString(),
                    mAddress_addresscity.getText().toString(),
                    mAdress_addresscounty.getText().toString(),
                    mAddress_addressdetal.getText().toString(),
                    mAddress_username.getText().toString().trim(),
                    mAddress_phonenum.getText().toString().trim(),
                    1);
            insertAddressInfoCall.enqueue(new Callback<insertAddressInfo>() {
                @Override
                public void onResponse(Call<insertAddressInfo> call, Response<insertAddressInfo> response) {

                    if (response.body() != null) {
                        boolean status = response.body().isStatus();
                        if (status){
                            finish();
                        }

                    }
                }

                @Override
                public void onFailure(Call<insertAddressInfo> call, Throwable t) {

                }
            });
        }

    }
}
