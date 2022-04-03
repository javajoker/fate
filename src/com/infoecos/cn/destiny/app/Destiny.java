package com.infoecos.cn.destiny.app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;

import com.infoecos.cn.destiny.lib.TimezoneLocation;
import com.infoecos.cn.destiny.lib.预测.八字命评;

public class Destiny {
	public static boolean preferenceChanged = true;
	private static final Object lock = new Object();

	public static Object getLock() {
		return lock;
	}

	private static 八字命评 destiny = null;

	private static void refreshData(Context context) throws Exception {
		if (preferenceChanged) {
			SharedPreferences settings = PreferenceManager
					.getDefaultSharedPreferences(context);

			String name = settings.getString("edittext_preference_name", "");

			DateFormat dfm = new SimpleDateFormat("MM-dd-yyyy HH:mm");
			Date born = dfm.parse(settings
					.getString("time_preference_born", ""));
			boolean genderIsMan = Boolean.parseBoolean(settings.getString(
					"list_preference_gender", "true"));
			String timezone = settings.getString("list_preference_timezone",
					"CCT");

			String loc = settings.getString("edittext_preference_location", "");
			Geocoder geo = new Geocoder(context);
			TimezoneLocation location = new TimezoneLocation(timezone);
			try {
				List<Address> addresses = geo.getFromLocationName(loc, 1);
				if (addresses != null && !addresses.isEmpty()) {
					Address address = addresses.get(0);
					location = new TimezoneLocation(address.getLongitude(),
							address.getLatitude(), timezone);
				}
			} catch (Exception e) {
			}

			destiny = new 八字命评(born, genderIsMan, location);
			destiny.流年行运数();

			preferenceChanged = false;
		}
	}

	public static Map<String, String> 八字(Context context) throws Exception {
		refreshData(context);
		return destiny.八字();
	}

	public static Map<String, String> 人事(Context context) throws Exception {
		refreshData(context);
		return destiny.人事();
	}

	public static Map<String, String> 家人(Context context) throws Exception {
		refreshData(context);
		return destiny.家人();
	}

	public static Map<String, String> 时运(Context context) throws Exception {
		refreshData(context);
		return destiny.时运();
	}
}
