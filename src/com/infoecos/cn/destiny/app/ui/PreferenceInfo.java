package com.infoecos.cn.destiny.app.ui;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

import com.infoecos.cn.destiny.app.Destiny;
import com.infoecos.cn.destiny.birth.R;

public class PreferenceInfo extends PreferenceActivity implements
		OnPreferenceChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);

		findPreference("time_preference_born").setOnPreferenceChangeListener(
				this);
		findPreference("list_preference_gender").setOnPreferenceChangeListener(
				this);
		findPreference("edittext_preference_location")
				.setOnPreferenceChangeListener(this);
		findPreference("list_preference_timezone")
				.setOnPreferenceChangeListener(this);
		// findPreference("edittext_preference_name")
		// .setOnPreferenceChangeListener(this);
		// findPreference("checkbox_preference_fan")
		// .setOnPreferenceChangeListener(this);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		Destiny.preferenceChanged = true;
		return true;
	}
}
