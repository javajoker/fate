package com.infoecos.cn.destiny.app.utils;

import android.content.Context;
import android.content.Intent;

import com.infoecos.cn.destiny.app.ui.MainActivity;

public class SysUtil {
	public static final int EXIT_APPLICATION = 0x0001;

	private Context mContext;

	public SysUtil(Context context) {
		this.mContext = context;
	}

	// 完全退出应用
	public void exit() {

		// 1.5 - 2.1之前下面两行是ok的,2.2之后就不行了，所以不通用
		// ActivityManager am =
		// (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
		// am.restartPackage("com.tutor.exit");

		Intent mIntent = new Intent();
		mIntent.setClass(mContext, MainActivity.class);
		// 这里设置flag还是比较 重要的
		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// 发出退出程序指示
		mIntent.putExtra("flag", EXIT_APPLICATION);
		mContext.startActivity(mIntent);
	}
}