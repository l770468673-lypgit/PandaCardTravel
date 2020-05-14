package com.xlu.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pandacard.teavel.R;


public class CustumDialog extends Dialog {
	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
	}

	@Override
	public void setContentView(View view) {
		// TODO Auto-generated method stub
		super.setContentView(view);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		// TODO Auto-generated method stub
		super.setContentView(view, params);
	}

	private Button positiveButton, negativeButton;
	private TextView tvNum;
	public TextView tvJindu;
	public ProgressBar pbjindu;
	public CustumDialog(Context context) {
		super(context, R.style.Dialog);
		setCustomDialog();
	}
	
	public TextView getTvNum() {
		return tvNum;
	}

	public void setTvNum(TextView tvNum) {
		this.tvNum = tvNum;
	}
	public void setPositiveButtonClick(boolean enable){
		positiveButton.setClickable(enable);
	}
	public void setNegativeButtonClick(boolean enable){
		negativeButton.setClickable(enable);
	}
	public void setNegativeButtonText(String text){
		negativeButton.setText(text);
	}
	public void setPositiveButtonText(String text){
		positiveButton.setText(text);
	}

	public ProgressBar getPbjindu() {
		return pbjindu;
	}

	public void setPbjindu(int jindu) {
		pbjindu.setProgress(jindu);
	}
	public void setPbjinduVisible(boolean isVisible){
		if(isVisible){
			pbjindu.setVisibility(View.VISIBLE);
		}else{
			pbjindu.setVisibility(View.GONE);
		}
		
	}
	public void setTvJinduVisible(boolean isVisible){
		if(isVisible){
			tvJindu.setVisibility(View.VISIBLE);
		}else{
			tvJindu.setVisibility(View.GONE);
		}
	}

	public void setCustomDialog() {
		View mView = LayoutInflater.from(getContext()).inflate(R.layout.alter_dialog, null);
		tvNum = mView.findViewById(R.id.tvnum);
		tvJindu= mView.findViewById(R.id.tv_jindu);
		positiveButton = mView.findViewById(R.id.positiveButton);
		negativeButton = mView.findViewById(R.id.negativeButton);
		pbjindu= mView.findViewById(R.id.pb_progressbar);
		super.setContentView(mView);
	}
	/** 
     *
     * @param listener 
     */  
    public void setOnPositiveListener(View.OnClickListener listener){
    	positiveButton.setOnClickListener(listener);  
    }  
    /** 
     *
     * @param listener 
     */  
    public void setOnNegativeListener(View.OnClickListener listener){
    	negativeButton.setOnClickListener(listener);  
    }
    public void setTvJindu(){
    	
    }

	

}
