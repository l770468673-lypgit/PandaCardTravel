/*************************************************************************
 * Copyright (c) 2015 Lemberg Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **************************************************************************/

package com.xlu.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pandacard.teavel.R;
import com.pandacard.teavel.apps.MyApplication;
import com.xlu.po.Jieshuo;


public class JieshuoPopup
        extends MapPopupBase {
    private View infoWindow;
    private Context context;
    private ImageView pic;
    private TextView tvName;
    private LinearLayout lyPlay;
    private ImageView ivPlay;
    private TextView tvPlay;
    private TextView tvMemo;

    public JieshuoPopup(Context context, ViewGroup parentView) {
        super(context, parentView);
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        infoWindow = inflater.inflate(
                R.layout.jieshuo_window, null);
        container.addView(infoWindow);
        pic = (ImageView) infoWindow.findViewById(R.id.iv_jieshuo_window_pic);
        tvName = (TextView) infoWindow.findViewById(R.id.tv_jieshuo_window_name);
        tvMemo = (TextView) infoWindow.findViewById(R.id.tv_jieshuo_window_memo);
        tvPlay = (TextView) infoWindow.findViewById(R.id.tv_jieshuo_windown_play);
        lyPlay = (LinearLayout) infoWindow.findViewById(R.id.ly_jieshuo_windown_play);
        ivPlay = (ImageView) infoWindow.findViewById(R.id.iv_jieshuo_windown_play);
        infoWindow.setFocusable(true);
        infoWindow.setClickable(true);
    }

    public void setJieshuo(Jieshuo jieshuo) {
        ImageLoader.getInstance().displayImage(
                Constance.HTTP_URL + jieshuo.getPic(), pic, MyApplication.normalOption);

        tvName.setText(jieshuo.getName());
        tvMemo.setText(jieshuo.getMemo());
    }

    public void moveBy(int dx, int dy) {
        if (lastX != -1 && lastY != -1) {
            int paddingBottom = 0;
            int paddingRight = 0;
            if (container.getPaddingTop() > (screenHeight - (infoWindow.getHeight() + 3))) {
                paddingBottom = (container.getPaddingBottom() - dy);
            }

            if (container.getPaddingLeft() > (screenWidth - (infoWindow.getWidth() + 3))) {
                paddingRight = container.getPaddingRight() - dx;
            }

            container.setPadding(container.getPaddingLeft() + dx,
                    container.getPaddingTop() + dy,
                    paddingRight, paddingBottom);
        }
    }

    public void setOnClickListener(OnClickListener listener) {
        if (infoWindow != null) {
            infoWindow.setOnClickListener(listener);
        }
    }

    public void setPlayOnClickListener(OnClickListener listener) {
        if (infoWindow != null) {
            lyPlay.setOnClickListener(listener);
        }
    }

    public boolean isPlay() {
        return tvPlay.getText().toString().equals("暂停");
    }

    public void setPlay(boolean b) {
        // TODO Auto-generated method stub
        if (b) {
            ivPlay.setImageResource(R.drawable.iv_jieshuo_windown_stop);
            tvPlay.setText("暂停");
        } else {
            ivPlay.setImageResource(R.drawable.iv_jieshuo_windown_start);
            tvPlay.setText("试听");
        }
    }
}
