package com.infoecos.cn.destiny.lib;

import java.util.Date;

import com.infoecos.cn.destiny.lib.支.地支;
import com.infoecos.cn.destiny.lib.时.节令;
import com.infoecos.cn.lunisolar.LunarUtils;

/**
 * 年柱—是八字之根，代表家族祖先的根基。
 *
 * 月柱—是八字之苗，也是八字的提纲，代表生身父母的荣枯，家庭状况的兴衰，更是判定日主强弱的基础。
 *
 * 日柱—是八字的花，又称日主、日元或元神，代表自己和配偶，也是运算推理的出发点。
 *
 * 时柱—是八字的果，代表子女。
 *
 * @author dch
 *
 */
public class 四柱 {

	private static TimezoneLocation 西安骊山 = new TimezoneLocation(109.21189d,
			34.35605d, "CCT");

	private static Date getLocalAbsoluteDate(Date solarDateTime,
			TimezoneLocation location) {
		Date d = new Date(
				solarDateTime.getTime()
						+ (long) ((location.getGMTAbsoluteHoursOffset() - location
								.getGMTHoursOffset()) * 60 * 60 * 1000));
		return d;
	}

	private static Date getStandardDate(Date solarDateTime,
			TimezoneLocation location) {
		Date d = new Date(
				solarDateTime.getTime()
						+ (long) ((西安骊山.getGMTAbsoluteHoursOffset() - location
								.getGMTHoursOffset()) * 60 * 60 * 1000));
		return d;
	}

	/**
	 * 获取年柱。
	 *
	 * @param solarDateTime
	 * @return
	 * @throws Exception
	 */
	public static 干支 年柱(Date solarDateTime) throws Exception {
		int g = (solarDateTime.getYear() + 1900 - 1900 + 36) % 60;
		if ((LunarUtils.dayDifference(solarDateTime.getYear() + 1900,
				solarDateTime.getMonth() + 1, solarDateTime.getDate()) + solarDateTime
				.getHours() / 24) < 节令.term(solarDateTime.getYear() + 1900, 3,
				true) - 1) {// 判断是否过立春
			g -= 1;
		}
		return 干支.lookup(g + 1);
	}

	public static 干支 年柱(Date solarDateTime, TimezoneLocation location)
			throws Exception {
		return 年柱(getStandardDate(solarDateTime, location));
	}

	/**
	 * 获取月柱。
	 *
	 * @param solarDateTime
	 * @return
	 * @throws Exception
	 */
	public static 干支 月柱(Date solarDateTime) throws Exception {
		int v = ((solarDateTime.getYear() + 1900 - 1900) * 12
				+ solarDateTime.getMonth() + 1 + 12) % 60;
		if (solarDateTime.getDate() <= 节令.节气(solarDateTime)[0]
				.getSolarTermDate().getDate())
			v -= 1;
		return 干支.lookup(v + 1);
	}

	public static 干支 月柱(Date solarDateTime, TimezoneLocation location)
			throws Exception {
		干支 standard = 月柱(getStandardDate(solarDateTime, location));
		地支 z = standard.地支();
		if (location.getLatitude() < 0)
			z = 地支.values()[(z.ordinal() + 6) % 12];
		return 干支.lookup(standard.天干(), z);
	}

	// / <summary>
	// / 获取日柱。
	// / </summary>
	/**
	 * 获取日柱。
	 *
	 * @param solarDateTime
	 * @return
	 * @throws Exception
	 */
	private static 干支 日柱(Date solarDateTime) throws Exception {
		double gzD = (solarDateTime.getHours() < 23) ? LunarUtils
				.equivalentStandardDay(solarDateTime.getYear() + 1900,
						solarDateTime.getMonth() + 1, solarDateTime.getDate())
				: LunarUtils.equivalentStandardDay(
						solarDateTime.getYear() + 1900,
						solarDateTime.getMonth() + 1, solarDateTime.getDate()) + 1;
		return 干支.lookup((int) Math.round((double) LunarUtils.rem(
				(int) gzD + 15, 60)));
	}

	public static 干支 日柱(Date solarDateTime, TimezoneLocation location)
			throws Exception {
		return 日柱(getStandardDate(solarDateTime, location));
	}

	/**
	 * 获取时柱。
	 *
	 * @param solarDateTime
	 * @return
	 * @throws Exception
	 */
	private static 干支 时柱(Date solarDateTime) throws Exception {
		double v = 12 * 日柱(solarDateTime).天干().术数().getIndex()
				+ Math.floor((double) ((solarDateTime.getHours() + 1) / 2))
				- 11;
		if (solarDateTime.getHours() == 23)
			v -= 12;
		return 干支.lookup((int) Math.round(LunarUtils.rem(v, 60)));
	}

	public static 干支 时柱(Date solarDateTime, TimezoneLocation location)
			throws Exception {
		return 时柱(getLocalAbsoluteDate(solarDateTime, location));
	}
}
