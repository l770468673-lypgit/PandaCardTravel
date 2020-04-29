package com.xlu.widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pandacard.teavel.R;


public class CustomDialog extends Dialog {

	public CustomDialog(Context context) {
		super(context);
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}
	public static class Builder{
		private String positiveButtonText;
		private String negativeButtonText;
		private String message;
		private Context context;
		private String title;
		private View contentView;
		private OnClickListener positiveButtonClickListener;
		private OnClickListener negativeButtonClickListener;
		public Builder(Context context) {
			this.context=context;
			
		}
		/**
		 * 设置需要提示信息
		 * @param message
		 * @return
		 */
		public Builder setMessage(int message){
			this.message=(String) context.getText(message);
			return this;
		}
		/**
		 * 设置对话框标题
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title){
			this.title=(String) context.getText(title);
			return this;
			
		}
		/**
		 * 设置按钮文字和监听
		 * @param positiveButtonText
		 * @param positiveButtonClickListener
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText, OnClickListener positiveButtonClickListener){
			this.positiveButtonText=(String) context.getText(positiveButtonText);
			this.positiveButtonClickListener=positiveButtonClickListener;
			return this;
		}
		public Builder setNegativeButton(int negativeButtonText, OnClickListener negativeButtonClickListener){
			this.negativeButtonText=(String) context.getText(negativeButtonText);
			this.negativeButtonClickListener=negativeButtonClickListener;
			return this;
		}
		public CustomDialog create(){
			 LayoutInflater inflater = (LayoutInflater) context
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            // instantiate the dialog with the custom Theme  
	            final CustomDialog dialog = new CustomDialog(context, R.style.Dialog);
	            View layout = inflater.inflate(R.layout.my_alter_dialog_layout, null);
	            dialog.addContentView(layout,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	         // set the dialog title  
	            ((TextView) layout.findViewById(R.id.title)).setText(title);
	            // set the confirm button  
	            if(positiveButtonText!=null){
	            	((Button)layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);
	            	if(positiveButtonClickListener!=null){
	            		((Button)layout.findViewById(R.id.positiveButton)).setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
								
							}
						});
	            	}
	            	
	            }else{
	            (
	            		(Button)layout.findViewById(R.id.positiveButton))	.setVisibility(View.GONE);
	            }
	            if(negativeButtonText!=null){
	            	((Button)layout.findViewById(R.id.negativeButton)).setText(negativeButtonText);
	            	if(negativeButtonClickListener!=null){
	            		((Button)layout.findViewById(R.id.negativeButton)).setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
							}
						});
	            	}
	            }else{
	            	((Button)layout.findViewById(R.id.negativeButton)).setVisibility(View.GONE);
	            }
	            // set the content message  
	            if (message != null) {  
	                ((TextView) layout.findViewById(R.id.message)).setText(message);
	            } else if (contentView != null) {  
	                // if no message set  
	                // add the contentView to the dialog body  
	                ((LinearLayout) layout.findViewById(R.id.content))
	                        .removeAllViews();  
	                ((LinearLayout) layout.findViewById(R.id.content)).addView(
	                        contentView, new LayoutParams(
	                                LayoutParams.FILL_PARENT,
	                                LayoutParams.FILL_PARENT));
	            }
	            dialog.setContentView(layout);
			return dialog;
			
		}
	}

}
