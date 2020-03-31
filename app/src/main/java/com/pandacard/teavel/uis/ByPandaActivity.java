package com.pandacard.teavel.uis;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pandacard.teavel.R;
import com.pandacard.teavel.https.HttpManager;
import com.pandacard.teavel.https.beans.small_routine_bean.GoodsInfoById;
import com.pandacard.teavel.utils.CustomizeGoodsAddView;
import com.pandacard.teavel.utils.HttpRetrifitUtils;
import com.pandacard.teavel.utils.LUtils;
import com.pandacard.teavel.utils.MyDialog;
import com.pandacard.teavel.utils.StatusBarUtil;
import com.pandacard.teavel.utils.ToastUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ByPandaActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "ByPandaActivity";
    public Button mBycard_button;
    public int from = 0;
    private CustomizeGoodsAddView mCustomizeGoodsAddView;

    private TextView mPop_numtext;
    private Button mPop_button;
    private PopupWindow mPopupWindow;
    private TextView mReadandok;
    private LinearLayout mLly_readandok;
    private ImageView mScrlly_imgv, mScrlly_imgv2;

    private String mMSbanna;
    private String mMGoodsDesc;
    ;
    private TextView mCard_money;
    private AnimationDrawable mAnimaition;
    private ImageView iamge_loaddate_anim;

    private double mGoodsCostPrice;
    private String mProductId;
    private MyDialog mMdialogby;
    private ImageView mChaeck_imageview;

    private boolean ispremisscheck = false;
    private boolean iscardtypeclick = false;
    private TextView mTextv_cardtype;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_by_panda);
        StatusBarUtil.setDrawable(this, R.drawable.mine_title_jianbian);


        initView();


    }


    private void initView() {
        mBycard_button = findViewById(R.id.bycard_button);
        mCard_money = findViewById(R.id.card_money);
        iamge_loaddate_anim = findViewById(R.id.spaiamge_loaddate_anim);

        mLly_readandok = findViewById(R.id.lly_readandok);
        mScrlly_imgv = findViewById(R.id.scrlly_imgv);
        mScrlly_imgv2 = findViewById(R.id.scrlly_imgv2);
        mReadandok = findViewById(R.id.readandok);
        mChaeck_imageview = findViewById(R.id.chaeck_imageview);


        iamge_loaddate_anim.setBackgroundResource(R.drawable.load_date_anim);

        mAnimaition = (AnimationDrawable) iamge_loaddate_anim.getBackground();
        mAnimaition.setOneShot(false);

        mAnimaition.start();
        iamge_loaddate_anim.setVisibility(View.VISIBLE);
        mLly_readandok.setOnClickListener(this);
        mBycard_button.setOnClickListener(this);

//        mChaeck_imageview.setOnClickListener(this);
        mCard_money.setText("¥ " + mGoodsCostPrice + "");


    }


    class MYTask extends AsyncTask<String, Void, Bitmap> {

        /**
         * 表示任务执行之前的操作
         */
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        /**
         * 主要是完成耗时的操作
         */
        @Override
        protected Bitmap doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            // 使用网络连接类HttpClient类王城对网络数据的提取
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(arg0[0]);
            Bitmap bitmap = null;
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    byte[] data = EntityUtils.toByteArray(httpEntity);
                    bitmap = BitmapFactory
                            .decodeByteArray(data, 0, data.length);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            return bitmap;
        }

        /**
         * 主要是更新UI的操作
         */
        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            mScrlly_imgv2.setImageBitmap(result);
            mAnimaition.stop();
            iamge_loaddate_anim.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAnimaition != null) {
            iamge_loaddate_anim.setVisibility(View.GONE);
            mAnimaition.stop();
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {


            case R.id.lly_readandok:

                if (!ispremisscheck) {
                    ispremisscheck = true;
                    mReadandok.setTextColor(getResources().getColor(R.color.google_blue, null));
                    mChaeck_imageview.setBackground(getResources().getDrawable(R.mipmap.checkagreed, null));
                } else {
                    ispremisscheck = false;
                    mReadandok.setTextColor(getResources().getColor(R.color.black, null));
                    mChaeck_imageview.setBackground(getResources().getDrawable(R.mipmap.checknotagreed, null));
                }
                break;
            case R.id.bycard_button:
                CheckPremissconfirm();
                break;
            case R.id.chaeck_imageview:

                break;
            case R.id.pop_button:

                if (iscardtypeclick) {
                    if (mPopupWindow != null) {
                        mPopupWindow.dismiss();
                    }
                    int value = mCustomizeGoodsAddView.getValue();
                    mCustomizeGoodsAddView.setValue(value);
                    LUtils.d(TAG, "pop_subtract" + value);
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(this, confirmActivity.class);
                    bundle.putInt(HttpRetrifitUtils.ACT_BUNUM, value);
                    bundle.putString("mProductId", mProductId);
                    bundle.putDouble("mGoodsCostPrice", mGoodsCostPrice);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    ToastUtils.showToast(ByPandaActivity.this, "请选中购买的类别");
                }

                break;
        }

    }

    private void CheckPremissconfirm() {
        if (!ispremisscheck) {
            View inflate = getLayoutInflater().inflate(R.layout.premissconfirm, null);
            mMdialogby = new MyDialog(this, inflate);
            mMdialogby.setCancelable(true);
            mMdialogby.show();
            LinearLayout viewById = inflate.findViewById(R.id.premiss_llytv);
            viewById.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMdialogby.cancel();
                }
            });
        } else {
            from = Location.BOTTOM.ordinal();
            initPopupWindow();
        }

    }


    protected void initPopupWindow() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.activity_by_panda_pop, null);
        //内容，高度，宽度       
        mPopupWindow = new PopupWindow(popupWindowView, getWindowManager().getDefaultDisplay().getWidth(),
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        //菜单背景色
        View contentView = mPopupWindow.getContentView();

        mPop_button = contentView.findViewById(R.id.pop_button);
        mCustomizeGoodsAddView = contentView.findViewById(R.id.customizeGoodsAddView);
        mTextv_cardtype = contentView.findViewById(R.id.textv_cardtype);
        mCustomizeGoodsAddView.setMaxValue(99);
        mCustomizeGoodsAddView.setClickable(false);
        mTextv_cardtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!iscardtypeclick) {
                    iscardtypeclick = true;
                    mTextv_cardtype.setBackground(getResources().getDrawable(R.drawable.bycard_popback));
                    mTextv_cardtype.setTextColor(getResources().getColor(R.color.B23008));
                } else {
                    iscardtypeclick = false;
                    mTextv_cardtype.setBackground(getResources().getDrawable(R.drawable.bycard_popbackdefault));
                    mTextv_cardtype.setTextColor(getResources().getColor(R.color.a515C6F));
                }
            }
        });
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        mPopupWindow.setBackgroundDrawable(dw);
        mPopupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_main, null),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        //设置背景半透明   
        backgroundAlpha(0.5f);
        //关闭事件   
        mPopupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        mPop_button.setOnClickListener(this);
    }

    /**
     *  * 设置添加屏幕的背景透明度   *   * @param bgAlpha
     *  
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0   
        getWindow().setAttributes(lp);
    }

    /**
     *  * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *  
     */
    class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    /**
     *  * 菜单弹出方向
     *  
     */
    public enum Location {
        BOTTOM;
    }

    @Override
    public void onResume() {
        super.onResume();
        iscardtypeclick = false;
        getGoodsInfoById("ff8080816f403bb2016f640f2cf20002");


    }

    /**
     * 2.产品详情查询接口
     * goodsId=ff8080816f403bb2016f640f2cf20002
     */
    public void getGoodsInfoById(String goodsId) {

        Call<GoodsInfoById> goodsInfoById =
                HttpManager.getInstance().getHttpClient3().getGoodsInfoById(goodsId);
        goodsInfoById.enqueue(new Callback<GoodsInfoById>() {
            @Override
            public void onResponse(Call<GoodsInfoById> call, Response<GoodsInfoById> response) {
                if (response.body() != null) {
                    GoodsInfoById body = response.body();
                    mGoodsCostPrice = body.getGoodsInfo().getGoodsCostPrice();

                    String mSbanna = body.getGoodsInfo().getBannerList().get(0);
                    String mGoodsDesc = body.getGoodsInfo().getGoodsDesc();
                    mProductId = body.getGoodsInfo().getProductId();
                    loadPic(mGoodsDesc, mSbanna);
                }
            }

            @Override
            public void onFailure(Call<GoodsInfoById> call, Throwable t) {

            }
        });


    }

    private void loadPic(String goodsDesc, String sbanna) {
        Glide.with(ByPandaActivity.this).load(sbanna)
                .dontAnimate().skipMemoryCache(true).
                diskCacheStrategy(DiskCacheStrategy.ALL).
                into(mScrlly_imgv);

        new MYTask().execute(goodsDesc);
    }

}
