package com.xlu.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pandacard.teavel.R;


/**
 * Created by giant on 2017/8/15.
 */

public class MyTabView extends RelativeLayout {
    private Context context;
    private TextView tvText;
    private ImageView ivShow;
    private boolean isChecked=false;
    private String text;
    public MyTabView(Context context) {
        this(context,null);
    }

    public MyTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }
    private void init(){
       View view= LayoutInflater.from(context).inflate(R.layout.mytabview_layout,this);
        tvText= (TextView) view.findViewById(R.id.tv_text);
        ivShow= (ImageView) view.findViewById(R.id.iv_show);
        if(isChecked){
            ivShow.setVisibility(View.VISIBLE);
        }else{
            ivShow.setVisibility(View.INVISIBLE);
        }
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
        if(checked){
            ivShow.setVisibility(View.VISIBLE);
        }else{
            ivShow.setVisibility(View.INVISIBLE);
        }
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setText(String text) {
        this.text = text;
        tvText.setText(text);
    }
    public void setTextColor(String color){
        tvText.setTextColor(Color.parseColor(color));
    }


}
