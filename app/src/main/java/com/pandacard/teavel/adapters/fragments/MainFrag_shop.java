package com.pandacard.teavel.adapters.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pandacard.teavel.R;
import com.pandacard.teavel.utils.LUtils;

public class MainFrag_shop extends Fragment {
    private static String TAG = "MainFrag_shop";
    private WebView mFragment_shop_webview;

    public MainFrag_shop() {
    }

    // TODO: Rename and change types and number of parameters
    public static MainFrag_shop newInstance() {
        MainFrag_shop fragment = new MainFrag_shop();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initview(view);
        loadDate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_main_frag_shop, container, false);
        return inflate;
    }

    private void loadDate() {


        mFragment_shop_webview.loadUrl("https://www.baidu.com");
        mFragment_shop_webview.setWebViewClient(new WebViewClient());
        mFragment_shop_webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress != 100) {
                    //                    mProgressBar.setProgress(newProgress);
                } else {
                    //                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        mFragment_shop_webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                LUtils.i(TAG,"onReceivedError 证书问题");
            }
        });

    }


    private void initview(View inflate) {
        mFragment_shop_webview = inflate.findViewById(R.id.fragment_shop_webview);
        WebSettings setting = mFragment_shop_webview.getSettings();
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
