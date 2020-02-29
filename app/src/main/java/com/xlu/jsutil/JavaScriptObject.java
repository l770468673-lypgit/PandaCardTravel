package com.xlu.jsutil;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class JavaScriptObject {

	Context mContxt;

	public JavaScriptObject(Context mContxt) {
		this.mContxt = mContxt;
	}

	@JavascriptInterface
	public void fun1FromAndroid(String name) {
		Toast.makeText(mContxt, name, Toast.LENGTH_LONG).show();
		
	}

	public void fun2(String name) {
		Toast.makeText(mContxt, "调用fun2:" + name, Toast.LENGTH_SHORT).show();
	}
}
