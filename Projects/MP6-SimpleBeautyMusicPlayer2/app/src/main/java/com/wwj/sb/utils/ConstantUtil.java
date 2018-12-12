package com.wwj.sb.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class ConstantUtil {
	

	public static int[] getScreen(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		return new int[] {(int) (outMetrics.density * outMetrics.widthPixels),
				(int)(outMetrics.density * outMetrics.heightPixels)
		};
	}
	

	public static Toast showMessage(Toast toast, Context context, String msg) {
		if(toast == null) {
			toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		} else {
			toast.setText(msg);
		}
		toast.show();
		return toast;
	}
}
