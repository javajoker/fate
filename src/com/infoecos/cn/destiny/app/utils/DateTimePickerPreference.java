package com.infoecos.cn.destiny.app.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

public class DateTimePickerPreference extends DialogPreference {
	private DatePicker dp;
	private TimePicker tp;

	final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
	/**
	 * The validation expression for this preference
	 */
	private static final String VALIDATION_EXPRESSION = "[0-9]{1,2}-[0-9]{1,2}-[0-9]{2,4} [0-9]{1,2}:[0-9]{1,2}";

	/**
	 * The default value for this preference
	 */
	private String defaultValue;

	/**
	 * @param context
	 * @param attrs
	 */
	public DateTimePickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public DateTimePickerPreference(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	/**
	 * Initialize this preference
	 */
	private void initialize() {
		setPersistent(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.DialogPreference#onCreateDialogView()
	 */
	@Override
	protected View onCreateDialogView() {
		LinearLayout layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.VERTICAL);

		dp = new DatePicker(getContext());
		int y = getYear(), m = getMonth() - 1, d = getDayOfMonth();
		if (y > 0 && m > 0 && d > 0) {
			dp.updateDate(y, m, d);
		}

		tp = new TimePicker(getContext());
		int h = getHour(), M = getMinute();
		if (h >= 0 && M >= 0) {
			tp.setCurrentHour(h);
			tp.setCurrentMinute(M);
		}

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				WRAP_CONTENT, WRAP_CONTENT);
		layout.addView(dp, layoutParams);
		layout.addView(tp, layoutParams);

		return layout;
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			dp.clearFocus();
			tp.clearFocus();

			String result = String.format("%02d-%02d-%04d %02d:%02d",
					dp.getMonth() + 1, dp.getDayOfMonth(), dp.getYear(),
					tp.getCurrentHour(), tp.getCurrentMinute());

			persistString(result);
			callChangeListener(result);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.Preference#setDefaultValue(java.lang.Object)
	 */
	@Override
	public void setDefaultValue(Object defaultValue) {
		// BUG this method is never called if you use the 'android:defaultValue'
		// attribute in your XML preference file, not sure why it isn't

		super.setDefaultValue(defaultValue);

		if (!(defaultValue instanceof String)) {
			return;
		}

		if (!((String) defaultValue).matches(VALIDATION_EXPRESSION)) {
			return;
		}

		this.defaultValue = (String) defaultValue;
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		final String value = a.getString(index);

		if (value == null || !value.matches(VALIDATION_EXPRESSION)) {
			return null;
		}

		this.defaultValue = value;
		return value;
	}

	private int getYear() {
		String time = getPersistedString(this.defaultValue);
		if (time == null || !time.matches(VALIDATION_EXPRESSION)) {
			return -1;
		}
		time = time.split(" ")[0];
		return Integer.valueOf(time.split("-")[2]);
	}

	private int getMonth() {
		String time = getPersistedString(this.defaultValue);
		if (time == null || !time.matches(VALIDATION_EXPRESSION)) {
			return -1;
		}
		time = time.split(" ")[0];
		return Integer.valueOf(time.split("-")[0]);
	}

	private int getDayOfMonth() {
		String time = getPersistedString(this.defaultValue);
		if (time == null || !time.matches(VALIDATION_EXPRESSION)) {
			return -1;
		}
		time = time.split(" ")[0];
		return Integer.valueOf(time.split("-")[1]);
	}

	/**
	 * Get the hour value (in 24 hour time)
	 * 
	 * @return The hour value, will be 0 to 23 (inclusive)
	 */
	private int getHour() {
		String time = getPersistedString(this.defaultValue);
		if (time == null || !time.matches(VALIDATION_EXPRESSION)) {
			return -1;
		}
		time = time.split(" ")[1];
		return Integer.valueOf(time.split(":")[0]);
	}

	/**
	 * Get the minute value
	 * 
	 * @return the minute value, will be 0 to 59 (inclusive)
	 */
	private int getMinute() {
		String time = getPersistedString(this.defaultValue);
		if (time == null || !time.matches(VALIDATION_EXPRESSION)) {
			return -1;
		}
		time = time.split(" ")[1];
		return Integer.valueOf(time.split(":")[1]);
	}

	public String getText() {
		int tmpHour = getHour(), tmpMinute = getMinute();
		String time = "";
		if (DateFormat.is24HourFormat(getContext())) {
			time = String.format("%02d:%02d", tmpHour, tmpMinute);
		} else {
			time = String.format("%02d:%02d %s", tmpHour % 12, tmpMinute,
					(tmpHour > 12 ? "PM" : "AM"));
		}

		return String.format("%02d-%02d-%04d %s", getMonth(), getDayOfMonth(),
				getYear(), time);
	}
}