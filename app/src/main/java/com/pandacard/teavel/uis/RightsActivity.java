package com.pandacard.teavel.uis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pandacard.teavel.R;
import com.pandacard.teavel.utils.LUtils;

public class RightsActivity extends AppCompatActivity {
    private static String TAG = "RightsActivity";
    private WebView mRight_webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rights);

        initView();
        loadDate();
    }

    private void loadDate() {
        mRight_webview.loadUrl("https://mp.weixin.qq.com/s/Kylt25d-5XUyz0LzRI2FqA");
        mRight_webview.setWebViewClient(new WebViewClient());
        mRight_webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress != 100) {
                    //                    mProgressBar.setProgress(newProgress);
                } else {
                    //                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        mRight_webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                LUtils.i(TAG, "onReceivedError 证书问题");
            }
        });
    }

    private void initView() {
        mRight_webview =findViewById(R.id.right_webview);

        WebSettings setting = mRight_webview.getSettings();
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
