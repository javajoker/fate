package com.infoecos.cn.destiny.lib;

import java.util.HashMap;
import java.util.Map;

public class TimezoneLocation {
	private double latitude = 1;
	private double longitude = 0;
	private float hoursOffset = 0;

	public static String[][] _timezone = { { "NZDT", "13f" },
			{ "IDLE", "12f" }, { "NZST", "12f" }, { "NZT", "12f" },
			{ "AESST", "11f" }, { "CST(ACSST)", "10.5f" }, { "CADT", "10.5f" },
			{ "SADT", "10.5f" }, { "EST(EAST)", "10f" }, { "GST", "10f" },
			{ "LIGT", "10f" }, { "CAST", "9.5f" }, { "SAT(SAST)", "9.5f" },
			{ "WDT(AWSST)", "9f" }, { "JST", "9f" }, { "KST", "9f" },
			{ "MT", "8.5f" }, { "WST(AWST)", "8f" }, { "CCT", "8f" },
			{ "JT", "7.5f" }, { "IT", "3.5f" }, { "BT", "3f" },
			{ "EETDST", "3f" }, { "CETDST", "2f" }, { "EET", "2f" },
			{ "FWT", "2f" }, { "IST", "2f" }, { "MEST", "2f" },
			{ "METDST", "2f" }, { "SST", "2f" }, { "BST", "1f" },
			{ "CET", "1f" }, { "DNT", "1f" }, { "FST", "1f" }, { "MET", "1f" },
			{ "MEWT", "1f" }, { "MEZ", "1f" }, { "NOR", "1f" },
			{ "SET", "1f" }, { "SWT", "1f" }, { "WETDST", "1f" },
			{ "GMT", "0f" }, { "WET", "0f" }, { "WAT", "-1f" },
			{ "NDT", "-2.5f" }, { "ADT", "-3f" }, { "NFT", "-3.5f" },
			{ "NST", "-3.5f" }, { "AST", "-4f" }, { "EDT", "-4f" },
			{ "CDT", "-5f" }, { "EST", "-5f" }, { "CST", "-6f" },
			{ "MDT", "-6f" }, { "MST", "-7f" }, { "PDT", "-7f" },
			{ "PST", "-8f" }, { "YDT", "-8f" }, { "HDT", "-9f" },
			{ "YST", "-9f" }, { "AHST", "-10f" }, { "CAT", "-10f" },
			{ "NT", "-11f" }, { "IDLW", "-12f" }, { "CCDT", "9f" }, };
	private static Map<String, Float> _timezoneMap = null;
	static {
		_timezoneMap = new HashMap<String, Float>();
		for (String[] tz : _timezone) {
			try {
				_timezoneMap.put(tz[0], Float.parseFloat(tz[1]));
			} catch (Exception e) {
			}
		}
	}

	public TimezoneLocation(double longitude, double latitude, String timezone) {
		this(timezone);

		this.latitude = latitude;
		this.longitude = longitude;
	}

	public TimezoneLocation(String timezone) {
		try {
			hoursOffset = Float.parseFloat(timezone);
		} catch (Exception e) {
			hoursOffset = 8f;
		}
		longitude = hoursOffset * 15;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getGMTAbsoluteHoursOffset() {
		return longitude / 15;
	}

	public double getGMTHoursOffset() {
		return hoursOffset;
	}
}
