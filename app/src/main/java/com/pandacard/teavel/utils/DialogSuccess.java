package com.pandacard.teavel.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class DialogSuccess extends Dialog {
    //    style引用style样式
    public DialogSuccess(Context context, View layout) {

        super(context);

        setContentView(layout);


        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(30, 0, 30, 0);
        getWindow().setAttributes(layoutParams);
    }
}
