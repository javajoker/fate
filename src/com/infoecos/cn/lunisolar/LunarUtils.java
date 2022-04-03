package com.infoecos.cn.lunisolar;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class LunarUtils {
	@SuppressWarnings("deprecation")
	public static Date getDate(int year, int month, int day, int hours,
			int minutes, int seconds) {
		return getDate(new Date(year - 1900, month - 1, day, hours, minutes,
				seconds));
	}

	@SuppressWarnings("deprecation")
	public static Date getDate(Date date) {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		c.setTime(date);
		return new Date(c.get(Calendar.YEAR) - 1900, c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY),
				c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
	}

	/**
	 * 获取儒略日。
	 * 
	 * zone时区y年m月d日h时min分sec秒。
	 * 
	 * 距儒略历公元前4713年1月1日格林尼治时间正午12时的天数
	 * 
	 * @param solarDateTime
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static double getJulianDay(Date solarDateTime) throws Exception {
		boolean isG = isGregorian(solarDateTime.getYear() + 1900,
				solarDateTime.getMonth() + 1, solarDateTime.getDate(), 1);
		double jt = (solarDateTime.getHours() + (solarDateTime.getMinutes() + solarDateTime
				.getSeconds() / 60) / 60) / 24 - 0.5
		// - TimeZone.CurrentTimeZone.GetUtcOffset(solarDateTime).Hours
				- (solarDateTime.getTimezoneOffset() / 60) / 24;
		double jd = isG ? (equivalentStandardDay(
				solarDateTime.getYear() + 1900, solarDateTime.getMonth() + 1,
				solarDateTime.getDate()) + 1721425 + jt)
				: (equivalentStandardDay(solarDateTime.getYear() + 1900,
						solarDateTime.getMonth() + 1, solarDateTime.getDate()) + 1721425 + jt);// 儒略日
		return jd;
	}

	/**
	 * 获取约化儒略日。
	 * 
	 * 为了使用方便，国际上定义当前儒略日减去2400000.5日为约化儒略日（记为MJD）。
	 * 
	 * @param solarDateTime
	 * @return
	 * @throws Exception
	 */
	public static double getMiniJulianDay(Date solarDateTime) throws Exception {
		return getJulianDay(solarDateTime) - 2400000.5;
	}

	/**
	 * 判断y年m月(1,2,..,12,下同)d日是Gregorian历还是Julian历
	 * 
	 * （opt=1,2,3分别表示标准日历,Gregorge历和Julian历）
	 * 
	 * @param y
	 * @param m
	 * @param d
	 * @param opt
	 * @return
	 */
	public static boolean isGregorian(int y, int m, int d, int opt)
			throws Exception {
		if (opt == 1) {
			if (y > 1582 || (y == 1582 && m > 10)
					|| (y == 1582 && m == 10 && d > 14))
				return true; // Gregorian
			else if (y == 1582 && m == 10 && d >= 5 && d <= 14)
				throw new Exception("Special days removed from Gregorian");
			else
				return false; // Julian
		}

		if (opt == 2)
			return true; // Gregorian
		if (opt == 3)
			return false; // Julian
		throw new Exception("Invalid opt, should be 1, 2, 3");
	}

	/**
	 * 返回等效标准天数
	 * 
	 * y年m月d日相应历种的1年1月1日的等效(即对Gregorian历与Julian历是统一的)天数
	 * 
	 * @param y
	 * @param m
	 * @param d
	 * @return
	 * @throws Exception
	 */
	public static double equivalentStandardDay(int y, int m, int d)
			throws Exception {
		double v = (y - 1) * 365 + Math.floor((double) ((y - 1) / 4))
				+ dayDifference(y, m, d) - 2; // Julian的等效标准天数
		if (y > 1582)
			v += -Math.floor((double) ((y - 1) / 100))
					+ Math.floor((double) ((y - 1) / 400)) + 2; // Gregorian的等效标准天数
		return v;
	}

	/**
	 * 返回阳历y年m月d日的日差天数
	 * 
	 * （在y年年内所走过的天数，如2000年3月1日为61）
	 * 
	 * @param y
	 * @param m
	 * @param d
	 * @return
	 * @throws Exception
	 */
	public static int dayDifference(int y, int m, int d) throws Exception {
		boolean isG = isGregorian(y, m, d, 1);
		int[] monL = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (isG)
			if ((y % 100 != 0 && y % 4 == 0) || (y % 400 == 0))
				monL[2] += 1;
			else if (y % 4 == 0)
				monL[2] += 1;
		int v = 0;
		for (int i = 0; i <= m - 1; i++) {
			v += monL[i];
		}
		v += d;
		if (y == 1582) {
			if (isG)
				v -= 10;
		}
		return v;
	}

	/**
	 * 返回阳历y年日差天数为x时所对应的月日数
	 * 
	 * 如y=2000，x=274时，返回1001(表示10月1日，即返回100*m+d)）
	 * 
	 * @param y
	 * @param x
	 * @return
	 * @throws Exception
	 */
	public static double antiDayDifference(int y, double x) throws Exception {
		int m = 1;
		for (int j = 1; j <= 12; j++) {
			int mL = dayDifference(y, j + 1, 1) - dayDifference(y, j, 1);
			if (x <= mL || j == 12) {
				m = j;
				break;
			} else
				x -= mL;
		}
		return 100 * m + x;
	}

	/**
	 * 返回x的小数尾数，若x为负值，则是1-小数尾数
	 * 
	 * @param x
	 * @return
	 */
	public static double tail(double x) {
		return x - Math.floor(x);
	}

	/**
	 * 角度函数。原始：ang
	 * 
	 * @param x
	 * @param t
	 * @param c1
	 * @param t0
	 * @param t2
	 * @param t3
	 * @return
	 */
	public static double angle(double x, double t, double c1, double t0,
			double t2, double t3) {
		return tail(c1 * x) * 2 * Math.PI + t0 - t2 * t * t - t3 * t * t * t;
	}

	/**
	 * 广义求余
	 * 
	 * @param x
	 * @param w
	 * @return
	 */
	public static double rem(double x, double w) {
		return tail((x / w)) * w;
	}
}
