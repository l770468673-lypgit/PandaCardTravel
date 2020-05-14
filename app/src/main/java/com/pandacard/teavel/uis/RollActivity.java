package com.pandacard.teavel.uis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.pandacard.teavel.R;
import com.pandacard.teavel.utils.LUtils;
import com.pandacard.teavel.utils.StatusBarUtil;

public class RollActivity extends AppCompatActivity {

    private String TAG = "RollActivity";
    private TextView mChongzhinfc_textView;
    private WebView mFragment_travel_webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll);
        StatusBarUtil.setDrawable(this, R.drawable.mine_title_jianbian);
        initView();
        loadDate();
    }


    private void loadDate() {

        mFragment_travel_webview.loadUrl("https://mp.weixin.qq.com/s/gJAMG6qUF-p9Vc-WXV6XBA");
        //        mFragment_travel_webview.loadUrl(getArguments().getString("MTrippic"));
        mFragment_travel_webview.setWebViewClient(new WebViewClient());
        mFragment_travel_webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress != 100) {
                    //mProgressBar.setProgress(newProgress);
                } else {
                    //mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        mFragment_travel_webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                LUtils.i(TAG, "onReceivedError 证书问题");
            }
        });

    }


    private void initView() {
        mChongzhinfc_textView = findViewById(R.id.chongzhinfc_textView);
        mChongzhinfc_textView.setText(R.string.fragment_mine_minecardwallet);
        mFragment_travel_webview = findViewById(R.id.fragment_travel_webview);
        WebSettings setting = mFragment_travel_webview.getSettings();
        setting.setJavaScriptEnabled(true);//支持Js
        setting.setCacheMode(WebSettings.LOAD_DEFAULT);//缓存模式
        //是否支持画面缩放，默认不支持
        setting.setBuiltInZoomControls(true);
        setting.setSupportZoom(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        setting.setDomStorageEnabled(true);
        setting.setUserAgentString("true");
        //是否显示缩放图标，默认显示
        setting.setDisplayZoomControls(false);
        //设置网页内容自适应屏幕大小 LayoutAlgorithm. AUTOSIZING
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);//注意网上例程很多的是.SINGLE_COLUMN，但调试时发现移动版网站会错位，所以改成									       //SINGLE_COLUMN
        }
        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);

    }

}
