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
import com.pandacard.teavel.https.beans.small_routine_bean.queryAddressInfoById;
import com.pandacard.teavel.https.beans.small_routine_bean.updateAddress;
import com.pandacard.teavel.utils.HttpRetrifitUtils;
import com.pandacard.teavel.utils.LUtils;
import com.pandacard.teavel.utils.ShareUtil;
import com.pandacard.teavel.utils.StatusBarUtil;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "AddNewAddressActivity";
    private TextView mAddadress_saves;
    private ImageView mLly_attbarimageview;

    private EditText mAddress_username;
    private EditText mAddress_phonenum;
    private EditText mAddress_addressprovinces;
    private EditText mAddress_addresscity;
    private EditText mAdress_addresscounty;
    private EditText mAddress_addressdetal;
    private TextView mAddress_changedefault;

    private boolean isaddressdefault;
    private int addressTag;
    private queryAddressInfoById.AddressInfoBean mQueryAddressInfoById;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);
        StatusBarUtil.setDrawable(this, R.drawable.mine_title_jianbian);
        initView();
    }


    private void initView() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mQueryAddressInfoById = (queryAddressInfoById.AddressInfoBean) extras.getSerializable("queryAddressInfoById");
        }


        isaddressdefault = false;
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
//        mAddress_changedefault.setBackground(getResources().getDrawable(R.mipmap.address_default));
        mAddadress_saves.setOnClickListener(this);
        mAddress_changedefault.setOnClickListener(this);
        mLly_attbarimageview.setOnClickListener(this);
        if (mQueryAddressInfoById != null) {
            mAddress_username.setText(mQueryAddressInfoById.getReceiver());
            mAddress_phonenum.setText(mQueryAddressInfoById.getPhoneNumber());
            mAddress_addressprovinces.setText(mQueryAddressInfoById.getProvinces());
            mAddress_addresscity.setText(mQueryAddressInfoById.getRegion());
            mAdress_addresscounty.setText(mQueryAddressInfoById.getCountries());
            mAddress_addressdetal.setText(mQueryAddressInfoById.getDetailAddress());

        }
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
            case R.id.address_changedefault:
                if (!isaddressdefault) {
                    isaddressdefault = true;
//                    mAddress_changedefault.setText("开");
                    mAddress_changedefault.setSelected(true);
//                    mAddress_changedefault.setBackground(getResources().getDrawable(R.mipmap.address_select));
                } else {
//                    mAddress_changedefault.setText("关");
                    mAddress_changedefault.setSelected(false);
                    isaddressdefault = false;
//                    mAddress_changedefault.setBackground(getResources().getDrawable(R.mipmap.address_default));
                }
                break;

        }
    }

    private void CommitAddaress() {
        if (isaddressdefault) {
            addressTag = 1;
        } else {
            addressTag = 2;
        }
        LUtils.d(TAG, "addressTag==" + addressTag);
        if (mAddress_username.getText().toString().trim().length() > 0 &&
                mAddress_phonenum.getText().toString().trim().length() > 0 &&
                mAddress_addressprovinces.getText().toString().trim().length() > 0 &&
                mAddress_addresscity.getText().toString().trim().length() > 0 &&
                mAdress_addresscounty.getText().toString().trim().length() > 0 &&
                mAddress_addressdetal.getText().toString().trim().length() > 0
        ) {
            if (mQueryAddressInfoById != null) {
                Call<updateAddress> updateAddressCall = HttpManager.getInstance().getHttpClient3().
                        updateAddress(
                                mQueryAddressInfoById.getId(),
                                mAddress_addressprovinces.getText().toString(),
                                mAddress_addresscity.getText().toString(),
                                mAdress_addresscounty.getText().toString(),
                                mAddress_addressdetal.getText().toString(),
                                mAddress_username.getText().toString().trim(),
                                mAddress_phonenum.getText().toString().trim(),
                                addressTag);
                updateAddressCall.enqueue(new Callback<updateAddress>() {
                    @Override
                    public void onResponse(Call<updateAddress> call, Response<updateAddress> response) {
                        if (response.body() != null) {
                            boolean status = response.body().isStatus();
                            if (status) {
                                finish();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<updateAddress> call, Throwable t) {

                    }
                });

            } else {
                Call<insertAddressInfo> insertAddressInfoCall = HttpManager.getInstance().getHttpClient3().insertAddressInfo(
                        ShareUtil.getString(HttpRetrifitUtils.WECHAT_USERID),
                        mAddress_addressprovinces.getText().toString(),
                        mAddress_addresscity.getText().toString(),
                        mAdress_addresscounty.getText().toString(),
                        mAddress_addressdetal.getText().toString(),
                        mAddress_username.getText().toString().trim(),
                        mAddress_phonenum.getText().toString().trim(),
                        addressTag);
                insertAddressInfoCall.enqueue(new Callback<insertAddressInfo>() {
                    @Override
                    public void onResponse(Call<insertAddressInfo> call, Response<insertAddressInfo> response) {
                        if (response.body() != null) {
                            boolean status = response.body().isStatus();
                            if (status) {
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
}
