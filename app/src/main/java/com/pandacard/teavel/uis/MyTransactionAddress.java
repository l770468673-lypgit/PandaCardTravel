package com.pandacard.teavel.uis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.adapters.AddressAdapter;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.small_routine_bean.MyAddressByUserId;
import com.pandacard.teavel.https.beans.small_routine_bean.deleteAddressInfoById;
import com.pandacard.teavel.https.beans.small_routine_bean.queryAddressInfoById;
import com.pandacard.teavel.utils.HttpRetrifitUtils;
import com.pandacard.teavel.utils.LUtils;
import com.pandacard.teavel.utils.MyDialog;
import com.pandacard.teavel.utils.ShareUtil;
import com.pandacard.teavel.utils.StatusBarUtil;
import com.pandacard.teavel.utils.ToastUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTransactionAddress extends AppCompatActivity implements View.OnClickListener, AddressAdapter.interItemClickListener {

    private String TAG = "MyTransactionAddress";
    private TextView mAddmytransaddress;
    private ImageView mLly_attbarimageview;
    private RecyclerView mAddressrecycle;
    private AddressAdapter mAddressAdapter;
    private List<MyAddressByUserId.AddressListBean> mAddressList;
    private MyDialog mMyDialog;
    private String mMyTransactionAddress;
    private int ADDRESSRESULT = 0x0888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_transaction_address);
        StatusBarUtil.setDrawable(this, R.drawable.mine_title_jianbian);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mMyTransactionAddress = extras.getString("MyTransactionAddress");
        }

        initView();


    }

    private void initDate() {
        Call<MyAddressByUserId> myAddressByUserId = HttpManager.getInstance().getHttpClient3().
                getMyAddressByUserId(ShareUtil.getString(HttpRetrifitUtils.WECHAT_USERID));
        myAddressByUserId.enqueue(new Callback<MyAddressByUserId>() {
            @Override
            public void onResponse(Call<MyAddressByUserId> call, Response<MyAddressByUserId> response) {

                if (response.body() != null) {
                    mAddressList = response.body().getAddressList();
                    mAddressAdapter.setAddressList(mAddressList);
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
        mAddressAdapter.setClickListener(this);

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


    //
    @Override
    public void ItemLongClick(final int po) {
        LUtils.d(TAG, "----ItemLongClick---");

        View inflate = getLayoutInflater().inflate(R.layout.addressdelect_dialog, null);
        mMyDialog = new MyDialog(this, inflate);
        TextView viewcancle = inflate.findViewById(R.id.delectdialog_cancle);
        TextView viewdelect = inflate.findViewById(R.id.delectdialog_delect);
        mMyDialog.show();
        viewcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyDialog.cancel();
            }
        });
        viewdelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAddressByUserId.AddressListBean addressListBean = mAddressList.get(po);
                String id = addressListBean.getId();
                Call<deleteAddressInfoById> deleteAddressInfoByIdCall =
                        HttpManager.getInstance().getHttpClient3().deleteAddressInfoById(id);

                deleteAddressInfoByIdCall.enqueue(new Callback<deleteAddressInfoById>() {
                    @Override
                    public void onResponse(Call<deleteAddressInfoById> call, Response<deleteAddressInfoById> response) {
                        if (response.body() != null) {
                            ToastUtils.showToast(MyTransactionAddress.this, response.body().getMsg());
                            mMyDialog.cancel();
                            initDate();
                        }
                    }

                    @Override
                    public void onFailure(Call<deleteAddressInfoById> call, Throwable t) {
                        mMyDialog.cancel();
                    }
                });
            }
        });

    }

    @Override
    public void ItemClick(int po) {
        LUtils.d(TAG, "----ItemClick---");
        MyAddressByUserId.AddressListBean addressListBean = mAddressList.get(po);
        String id = addressListBean.getId();
        Call<queryAddressInfoById> queryAddressInfoByIdCall = HttpManager.getInstance().getHttpClient3().
                queryAddressInfoById(id);
        queryAddressInfoByIdCall.enqueue(new Callback<queryAddressInfoById>() {
            @Override
            public void onResponse(Call<queryAddressInfoById> call, Response<queryAddressInfoById> response) {
                if (response.body() != null) {
                    Intent intent = new Intent(MyTransactionAddress.this, AddNewAddressActivity.class);
                    Bundle bundle = new Bundle();
                    queryAddressInfoById.AddressInfoBean addressInfo = response.body().getAddressInfo();
                    bundle.putSerializable("queryAddressInfoById", response.body().getAddressInfo());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<queryAddressInfoById> call, Throwable t) {

            }
        });
    }

    @Override
    public void ItemClickback(int po) {

        if (mMyTransactionAddress != null) {
            // 设置返回数据
            Bundle bundle = new Bundle();
            MyAddressByUserId.AddressListBean addressListBean = mAddressList.get(po);
            bundle.putSerializable("addressListBean", addressListBean);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            // 返回intent
            setResult(ADDRESSRESULT, intent);
            finish();

        }
    }

}
