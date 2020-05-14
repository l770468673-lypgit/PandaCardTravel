package com.xlu.widgets;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.pandacard.teavel.R;


/**
 * Created by giant on 2017/7/9.
 */

public class MySpinnerDialog extends ProgressDialog {
    TextView tv;
    public MySpinnerDialog(Context context) {
        super(context);
    }

    public MySpinnerDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getContext());
    }

    private void init(Context context) {
        //设置不可取消，点击其他区域不能消失
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.my_spinner_dialog);
        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.width= WindowManager.LayoutParams.WRAP_CONTENT;
        params.height= WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
        tv= findViewById(R.id.tv_load_dialog);
    }

    public void setTvShowText(String text) {
        this.tv.setText(text);
    }

    @Override
    public void show() {
        super.show();
    }

}
