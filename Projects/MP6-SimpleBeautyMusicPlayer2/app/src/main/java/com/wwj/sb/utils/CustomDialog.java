package com.wwj.sb.utils;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wwj.sb.activity.R;

public class CustomDialog extends Dialog {

	public CustomDialog(Context context) {
		super(context);
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {

		private Context context;
		private int mIcon = -1;
		private CharSequence mTitle;
		private CharSequence mMessage;
		private CharSequence mPositiveButtonText;
		private CharSequence mNegativeButtonText;
		private CharSequence mNeutralButtonText;
		private boolean mCancelable = true;

		private int mViewSpacingLeft;
		private int mViewSpacingTop;
		private int mViewSpacingRight;
		private int mViewSpacingBottom;
		private boolean mViewSpacingSpecified = false;

		private View mView;

		private OnClickListener mPositiveButtonClickListener,
				mNegativeButtonClickListener, mNeutralButtonClickListener;
		private OnCancelListener mCancelListener;
		private OnKeyListener mKeyListener;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setMessage(CharSequence message) {
			this.mMessage = message;
			return this;
		}

		public Builder setMessage(int message) {
			this.mMessage = context.getText(message);
			return this;
		}

		public Builder setTitle(int title) {
			this.mTitle = context.getText(title);
			return this;
		}

		public Builder setTitle(CharSequence title) {
			this.mTitle = title;
			return this;
		}

		public Builder setIcon(int icon) {
			this.mIcon = icon;
			return this;
		}

		public Builder setView(View view) {
			this.mView = view;
			mViewSpacingSpecified = false;
			return this;
		}

		public Builder setView(View view, int left, int top, int right,
				int bottom) {
			this.mView = view;
			this.mViewSpacingLeft = left;
			this.mViewSpacingTop = top;
			this.mViewSpacingRight = right;
			this.mViewSpacingBottom = bottom;
			mViewSpacingSpecified = true;
			return this;
		}

		public Builder setPositiveButton(int textId,
				final OnClickListener listener) {
			this.mPositiveButtonText = context.getText(textId);
			this.mPositiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String text,
				final OnClickListener listener) {
			this.mPositiveButtonText = text;
			this.mPositiveButtonClickListener = listener;
			return this;
		}

		public Builder setNeutralButton(int textId,
				final OnClickListener listener) {
			this.mNeutralButtonText = context.getText(textId);
			this.mNeutralButtonClickListener = listener;
			return this;
		}
		
		public Builder setNeutralButton(String text, final OnClickListener listener) {
			this.mNeutralButtonText = text;
			this.mNeutralButtonClickListener = listener;
			return this;
		}
		public Builder setNegativeButton(int textId,
				final OnClickListener listener) {
			this.mNegativeButtonText = context.getText(textId);
			this.mNegativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String text,
				final OnClickListener listener) {
			this.mNegativeButtonText = text;
			this.mNegativeButtonClickListener = listener;
			return this;
		}
		
		
		public Builder setCancelable(boolean cancelable) {
			this.mCancelable = cancelable;
			return this;
		}
		
		public Builder setOnCancelListener(OnCancelListener listener) {
			this.mCancelListener = listener;
			return this;
		}
		
		public Builder setOnKeyListener(OnKeyListener listener) {
			this.mKeyListener = listener;
			return this;
		}
		
		public CustomDialog create() {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final CustomDialog dialog = new CustomDialog(context, R.style.CustomDialog);
			dialog.setCancelable(mCancelable);

			if(mCancelListener != null) {
				dialog.setOnCancelListener(mCancelListener);
			}

			if(mKeyListener != null) {
				dialog.setOnKeyListener(mKeyListener);
			}

			View layout = inflater.inflate(R.layout.alert_dialog, (ViewGroup)(((Activity)context).findViewById(R.id.parentPanel)));
			layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			((TextView) layout.findViewById(R.id.alertTitle)).setText(mTitle);
			if(mIcon != -1) {
				((ImageView) layout.findViewById(R.id.icon))
				.setBackgroundResource(mIcon);
			}
			
			int count = 0;
			if(setButton(layout, mPositiveButtonText, R.id.button1, dialog, mPositiveButtonClickListener)) count++;
			if(setButton(layout, mNegativeButtonText, R.id.button2, dialog, mNegativeButtonClickListener)) count++;
			if(setButton(layout, mNeutralButtonText, R.id.button3, dialog, mNeutralButtonClickListener)) count++;
			
			if(count == 0) {
				layout.findViewById(R.id.buttonPanel).setVisibility(View.GONE);
			}
			if(count == 1) {
				layout.findViewById(R.id.leftSpacer)
					.setVisibility(View.INVISIBLE);
				layout.findViewById(R.id.rightSpacer).setVisibility(View.INVISIBLE);
			}
			if(!TextUtils.isEmpty(mMessage)) {
				((TextView)layout.findViewById(R.id.message))
					.setText(mMessage);
			} else {
				((LinearLayout) layout.findViewById(R.id.contentPanel))
					.setVisibility(View.GONE);
			}
			if(mView != null) {
				final FrameLayout customPanel = (FrameLayout) layout
						.findViewById(R.id.customPanel);
				if(mViewSpacingSpecified) {
					customPanel.setPadding(mViewSpacingLeft, mViewSpacingTop, mViewSpacingRight, mViewSpacingBottom);
				}
				customPanel.addView(mView);
			} else {
				((FrameLayout) layout.findViewById(R.id.customPanel))
							.setVisibility(View.GONE);
			}
			dialog.setContentView(layout);
			return dialog;
				
		}
		
		public CustomDialog show() {
			CustomDialog dialog = create();
			dialog.show();
			return dialog;
		}
		
		private boolean setButton(View layout, CharSequence mPositiveButtonText, int id, final Dialog dialog, final OnClickListener listener) {
			if(!TextUtils.isEmpty(mPositiveButtonText)) {
				final Button button = (Button) layout.findViewById(id);
				button.setText(mPositiveButtonText);
				if(listener != null) {
					button.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							listener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
						}
					});
				} else {
					button.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dialog.cancel();
							dialog.dismiss();
						}
					});
				}
				return true;
			} else {
				layout.findViewById(id).setVisibility(View.GONE);
				return false;
			}
		}
	}
}
