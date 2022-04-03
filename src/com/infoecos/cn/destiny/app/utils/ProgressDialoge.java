package com.infoecos.cn.destiny.app.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

public class ProgressDialoge {
	private static final int DIALOG_PROGRESS = 100;
	private static final int MAX_PROGRESS = 100;

	private ProgressDialog mProgressDialog;
	private int mProgress;
	private Handler mProgressHandler;

	private Activity activity;
	private String title, message;
	private boolean showing = false;

	public ProgressDialoge(Activity activity, String title, String message) {
		this.activity = activity;
		this.title = title;
		this.message = message;

		initialize();
	}

	protected void initialize() {
		mProgressHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (mProgress >= MAX_PROGRESS) {
					mProgressDialog.dismiss();
				} else {
					mProgressDialog.incrementProgressBy(1);
					mProgressHandler.sendEmptyMessageDelayed(0, 10);
				}
			}
		};
	}

	public void showDialog() {
		showing = true;
		activity.showDialog(DIALOG_PROGRESS);

		mProgress = 0;
		mProgressDialog.setProgress(0);
		mProgressHandler.sendEmptyMessage(0);
	}

	public void closeDialog() {
		if (!showing)
			return;

		mProgressDialog.setProgress(100);
		mProgress = 100;
		showing = false;
	}

	public Dialog onCreateDialog(int id) {
		if (id != DIALOG_PROGRESS)
			return null;

		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setTitle(title);
		mProgressDialog.setMessage(message);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setMax(MAX_PROGRESS);
		// mProgressDialog.setIcon(R.drawable.alert_dialog_icon);
		// mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// mProgressDialog.setButton(getText(R.string.alert_dialog_hide),
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int whichButton) {
		//
		// /* User clicked Yes so do some stuff */
		// }
		// });
		// mProgressDialog.setButton2(getText(R.string.alert_dialog_cancel),
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int whichButton) {
		//
		// /* User clicked No so do some stuff */
		// }
		// });

		return mProgressDialog;
	}

	public Activity getActivity() {
		return activity;
	}
}
