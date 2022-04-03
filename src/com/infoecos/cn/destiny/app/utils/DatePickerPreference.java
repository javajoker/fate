package com.infoecos.cn.destiny.app.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

public class DatePickerPreference extends DialogPreference {
	private DatePicker dp;
	/**
	 * The validation expression for this preference
	 */
	private static final String VALIDATION_EXPRESSION = "[0-9]{1,2}-[0-9]{1,2}-[0-9]{2,4}";

	/**
	 * The default value for this preference
	 */
	private String defaultValue;

	/**
	 * @param context
	 * @param attrs
	 */
	public DatePickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public DatePickerPreference(Context context, AttributeSet attrs,
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

		dp = new DatePicker(getContext());

		int y = getYear(), m = getMonth() - 1, d = getDayOfMonth();
		if (y > 0 && m > 0 && d > 0) {
			dp.updateDate(y, m, d);
		}

		return dp;
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			dp.clearFocus();

			String result = String.format("%02d-%02d-%04d", dp.getMonth() + 1,
					dp.getDayOfMonth(), dp.getYear());

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

		return Integer.valueOf(time.split("-")[2]);
	}

	private int getMonth() {
		String time = getPersistedString(this.defaultValue);
		if (time == null || !time.matches(VALIDATION_EXPRESSION)) {
			return -1;
		}

		return Integer.valueOf(time.split("-")[0]);
	}

	private int getDayOfMonth() {
		String time = getPersistedString(this.defaultValue);
		if (time == null || !time.matches(VALIDATION_EXPRESSION)) {
			return -1;
		}

		return Integer.valueOf(time.split("-")[1]);
	}

	public String getText() {
		return String.format("%02d-%02d-%04d", getMonth(), getDayOfMonth(),
				getYear());
	}
}