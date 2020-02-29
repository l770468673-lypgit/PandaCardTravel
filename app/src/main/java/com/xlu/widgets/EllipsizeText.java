package com.xlu.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import java.util.ArrayList;
import java.util.List;

public class EllipsizeText extends AppCompatTextView {
	//要显示省略号
	private static final String ELLIPSIS = "...";  

	private static final String TAG = "EllipsizeText";

	private float lineSpacingMultiplier = 1.0f;   
	private float lineAdditionalVerticalPadding = 0.0f;  
	public interface EllipsizeListener {
		void ellipsizeStateChanged(boolean ellipsized);
		}

	private final List<EllipsizeListener> ellipsizeListeners = new ArrayList<EllipsizeListener>();


	private boolean isEllipsized;   
	private boolean isStale;   
	private boolean programmaticChange;   
	private String fullText;   
	private int maxLines = -1;      

	public EllipsizeText(Context context) {
	super(context);
	}

	public EllipsizeText(Context context, AttributeSet attrs) {
	super(context, attrs);
	}

	public EllipsizeText(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	// TODO Auto-generated constructor stub

	}


	//是否会在文字过多的时候显示省略符号
	public boolean isEllipsized() {   
	return isEllipsized;   
	}   


	//重写setMaxLines的方法，因为只有在代码中setMaxLine才有效
	@Override   
	public void setMaxLines(int maxLines) {  
	super.setMaxLines(maxLines);   
	this.maxLines = maxLines;   
	isStale = true;   
	}   


	public int getMaxLines() {   
	return maxLines;   
	}   


	@Override   
	public void setLineSpacing(float add, float mult) {   
	this.lineAdditionalVerticalPadding = add;   
	this.lineSpacingMultiplier = mult;   
	super.setLineSpacing(add, mult);   
	}   


	@Override   
	protected void onTextChanged(CharSequence text, int start, int before, int after) {   
	super.onTextChanged(text, start, before, after);   
	if (!programmaticChange) {   
	fullText = text.toString();
	isStale = true;   
	}   
	}   


	@Override   
	protected void onDraw(Canvas canvas) { 
	if (isStale) {   
	super.setEllipsize(null);   
	resetText();  
	}   
	super.onDraw(canvas);   
	}   
	private void resetText() {   
		int maxLines = getMaxLines();   
		String workingText = fullText;  
		boolean ellipsized = false;   
		if (maxLines != -1) {   
		Layout layout = createWorkingLayout(workingText);   
		if (layout.getLineCount() > maxLines) {   
			//获取一行显示字符个数，然后截取字符串数
		workingText = fullText.substring(0, layout.getLineEnd(maxLines - 1)).trim()+ ELLIPSIS;   
		Layout layout2=createWorkingLayout(workingText);
		while (layout2.getLineCount() > maxLines) {   
		int lastSpace = workingText.length()-1;  
		if (lastSpace == -1) {   
		break;   
		}   
		workingText = workingText.substring(0, lastSpace); 
		layout2=createWorkingLayout(workingText + ELLIPSIS);
		}   
		workingText = workingText + ELLIPSIS;   
		ellipsized = true;   
		}   
		}   
		if (!workingText.equals(getText())) {   
		programmaticChange = true;   
		try {   
		setText(workingText);   
		invalidate();
		} finally {   
		programmaticChange = false;   
		}   
		}   
		isStale = false;   
		if (ellipsized != isEllipsized) {   
		isEllipsized = ellipsized;   
		for (EllipsizeListener listener : ellipsizeListeners) {   
		listener.ellipsizeStateChanged(ellipsized);   
		}   
		}   
		} 
	
	private Layout createWorkingLayout(String workingText) {   
		//字符串资源，画笔，layout的宽度，Layout的样式，字体的大小，行间距
		return new StaticLayout(workingText, getPaint(), getWidth() - getPaddingLeft() - getPaddingRight(),   
		Alignment.ALIGN_NORMAL, lineSpacingMultiplier, lineAdditionalVerticalPadding, false);   
		}   


}
