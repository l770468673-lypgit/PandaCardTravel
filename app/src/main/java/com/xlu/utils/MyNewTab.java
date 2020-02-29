package com.xlu.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.pandacard.teavel.R;

/**
 * Created by giant on 2017/9/8.
 */

public class MyNewTab extends LinearLayout{
    private Context context;
    private TextView tvText;
    private ImageView ivShow;
    private boolean isChecked=false;
    private String text;
    public MyNewTab(Context context) {
        this(context,null,0);
    }

    public MyNewTab(Context context, AttributeSet attrs) {
       this(context,attrs,0);

    }

    public MyNewTab(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.tab, defStyle, 0);
        View view= LayoutInflater.from(context).inflate(R.layout.mynewtabview_layout,this);
        tvText= (TextView) view.findViewById(R.id.tv_text);
        ivShow= (ImageView) view.findViewById(R.id.iv_show);
        ivShow.setImageResource(a.getResourceId(R.styleable.tab_mysrc,0));
        if(isChecked){
            tvText.setTextColor(Color.rgb(143,216,91));
        }else{
            tvText.setTextColor(Color.parseColor("#888888"));
        }
        a.recycle();
    }


    public void setChecked(boolean checked) {
        isChecked = checked;
        if(checked){
            tvText.setTextColor(Color.rgb(143,216,91));
        }else{
            tvText.setTextColor(Color.parseColor("#888888"));
        }
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setText(String text) {
        this.text = text;
        tvText.setText(text);
    }
}
