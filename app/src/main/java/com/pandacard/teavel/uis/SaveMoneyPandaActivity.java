package com.pandacard.teavel.uis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.bases.BasePandaActivity;
import com.pandacard.teavel.https.HttpManager;
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

public class SaveMoneyPandaActivity extends BasePandaActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private String TAG = "SaveMoneyActivity";
    private ImageView mSave_imageview_back;
    private RadioButton mSave_radio_btn_recharge;
    private RadioButton mSave_radio_btn_more;
    private TextView mChongzhinfc_textView;
    private List<String> mStrings;


    private List<String> mcards = new ArrayList<>();
    //    private ListView mListcard;
    //    private CardAdapter mAdapter;
    private Spinner mSavemmoney_spinn;

    private ArrayAdapter mAdapter;
//    private ListView mListView_popwindows;
//    private PopupWindow popupWindow;
    private int from = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_money);
        StatusBarUtil.setDrawable(this, R.drawable.mine_title_jianbian);
        //        liaddate();
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSpinnerdate();

        //
        //        if (mcards.size()>0) {
        //            mSavemmoney_spinn.setText(mcards.get(0));
        //        }
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
                        mAdapter = new ArrayAdapter<String>(SaveMoneyPandaActivity.this,
                                android.R.layout.simple_spinner_item, mcards);
                        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSavemmoney_spinn.setAdapter(mAdapter);
                    } else {
                        ToastUtils.showToast(SaveMoneyPandaActivity.this, body.getMsg());
                    }

                }
            }

            @Override
            public void onFailure(Call<cardsbean> call, Throwable t) {

            }
        });

    }

    //    private void liaddate() {
    //        mcards.clear();
    //        Call<cardsbean> cards = HttpManager.getInstance().getHttpClient().getCards(ShareUtil.getString(HttpRetrifitUtils.SERNAME_PHONE));
    //
    //        cards.enqueue(new Callback<cardsbean>() {
    //            @Override
    //            public void onResponse(Call<cardsbean> call, Response<cardsbean> response) {
    //                if (response.body() != null) {
    //                    cardsbean body = response.body();
    //                    if (body.getCode() == 1) {
    //                        String cards1 = body.getExtra().getCards();
    //                        mStrings = UserByteUtils.spliteStrWithBlank(cards1);
    //                        LUtils.d(TAG, "mStrings===" + mStrings);
    //                        for (int i = 0; i < mStrings.size(); i++) {
    //                            mcards.add(mStrings.get(i));
    //                        }
    //
    //                        mAdapter.setStringList(mcards);
    //                        LUtils.d(TAG, "mcards===" + mcards);
    //                        //                        mSavemmoney_spinn.setText(mcards.get(0));
    //                    } else {
    //                        ToastUtils.showToast(SaveMoneyActivity.this, body.getMsg());
    //                    }
    //
    //                }
    //            }
    //
    //            @Override
    //            public void onFailure(Call<cardsbean> call, Throwable t) {
    //
    //            }
    //        });
    //
    //    }

    private void initView() {

        mSave_imageview_back = findViewById(R.id.chongzhinfc_imageview_back);
        mChongzhinfc_textView = findViewById(R.id.chongzhinfc_textView);
        mSave_radio_btn_recharge = findViewById(R.id.save_radio_btn_recharge);
        mSave_radio_btn_more = findViewById(R.id.save_radio_btn_more);
        //        mListcard = findViewById(R.id.listcard);
        mSavemmoney_spinn = findViewById(R.id.savemmoney_spinn);


        mChongzhinfc_textView.setText(getResources().getText(R.string.nfcact_title_textname));
        mSave_imageview_back.setOnClickListener(this);
        mSave_radio_btn_recharge.setOnClickListener(this);
        mSave_radio_btn_more.setOnClickListener(this);

        //        mAdapter = new CardAdapter(this);
        //        mListcard.setAdapter(mAdapter);

//        mSavemmoney_spinn.setOnClickListener(this);

        //        mListcard.setOnItemClickListener(this);
        //        mAdapter = new CardAdapter(SaveMoneyActivity.this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.savemmoney_spinn:
                //                initPopupWindow();
                //                mListcard.setVisibility(View.VISIBLE);
                break;
            case R.id.chongzhinfc_imageview_back:
                finish();
                break;
            case R.id.save_radio_btn_recharge:

                Intent intent = new Intent(this, NFCPandaActivity.class);
                startActivity(intent);
                break;
            case R.id.save_radio_btn_more:

                Intent intent2 = new Intent(this, MoreDisposePandaActivity.class);
                startActivity(intent2);
                break;
        }
    }

    /**
     * 菜单弹出方向
     */
    public enum Location {
        BOTTOM;

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     */
    class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    //    protected void initPopupWindow() {
    //        View popupWindowView = getLayoutInflater().inflate(R.layout.list_pop, null);
    //        //内容，高度，宽度
    //        popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
    //        //  popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
    //        //菜单背景色
    //        //  ColorDrawable dw = new ColorDrawable(0xffffffff);
    //        popupWindow.setBackgroundDrawable(new BitmapDrawable());
    //
    //        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
    //        int ori = mConfiguration.orientation; //获取屏幕方向
    //        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
    //            //横屏
    //            if (Location.BOTTOM.ordinal() == from) {
    //                popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_main, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    //            }
    //        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
    //            //竖屏
    //            if (Location.BOTTOM.ordinal() == from) {
    //                popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_main, null), Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
    //            }
    //        }
    //
    //
    //        //设置背景半透明
    //        backgroundAlpha(0.5f);
    //        //关闭事件
    //        popupWindow.setOnDismissListener(new popupDismissListener());
    //
    //        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
    //
    //            @Override
    //            public boolean onTouch(View v, MotionEvent event) {
    //                /*if( popupWindow!=null && popupWindow.isShowing()){
    //                    popupWindow.dismiss();
    //                    popupWindow=null;
    //                }*/
    //                // 这里如果返回true的话，touch事件将被拦截
    //                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
    //                return false;
    //            }
    //        });
    //
    //
    //        liaddate();
    //
    //        mListView_popwindows = (ListView) popupWindowView.findViewById(R.id.list_commit);
    //        mListView_popwindows.setAdapter(mAdapter);
    //        mListView_popwindows.setOnItemClickListener(this);
    //    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String s = mcards.get(position);
//        mSavemmoney_spinn.setText(s);
//        popupWindow.dismiss();


    }
}
