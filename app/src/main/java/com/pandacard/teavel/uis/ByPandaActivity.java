package com.pandacard.teavel.uis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.utils.CustomizeGoodsAddView;
import com.pandacard.teavel.utils.HttpRetrifitUtils;
import com.pandacard.teavel.utils.LUtils;
import com.pandacard.teavel.utils.StatusBarUtil;
import com.pandacard.teavel.utils.ToastUtils;

public class ByPandaActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "ByPandaActivity";
    public Button mBycard_button;
    public int from = 0;
    private CustomizeGoodsAddView mCustomizeGoodsAddView;

    private TextView mPop_numtext;
    private Button mPop_button;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_by_panda);
        StatusBarUtil.setDrawable(this, R.drawable.mine_title_jianbian);
        initView();


    }

    private void initView() {
        mBycard_button = findViewById(R.id.bycard_button);
        mBycard_button.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bycard_button:
                from = Location.BOTTOM.ordinal();
                break;
            case R.id.pop_button:
                int value = mCustomizeGoodsAddView.getValue();
                mCustomizeGoodsAddView.setValue(value);
                LUtils.d(TAG, "pop_subtract" + value);
                finish();
                Bundle bundle = new Bundle();
                Intent intent = new Intent(this, confirmActivity.class);
                bundle.putInt(HttpRetrifitUtils.ACT_BUNUM,value);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
        initPopupWindow();
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
        mCustomizeGoodsAddView.setMaxValue(99);
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


}
