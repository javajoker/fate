package com.infoecos.cn.destiny.lib;

import java.util.Date;

import com.infoecos.cn.destiny.lib.时.节令;
import com.infoecos.cn.destiny.lib.时.节气;


public class 命运 {
	public static boolean 顺行(Date birthday, boolean genderIsMan,
			TimezoneLocation location) throws Exception {
		int 年干 = 四柱.年柱(birthday, location).天干().术数().getIndex();
		return genderIsMan ? (年干 % 2 == 1) : (年干 % 2 == 0);
	}

	/**
	 * 由出生日的下一日起，数至下月立节的日、时为止； 三日折一年，一日折四月，一时折十天。
	 * 
	 * 简单算法：
	 * 
	 * 根据年干分阴阳；
	 * 
	 * 阳男阴女，由出生之当天数起至下一个节，3日为一岁。
	 * 
	 * 阴男阳女，由出生之当天倒数至上一个节，3日为一岁。
	 * 
	 * @param birthday
	 * @param genderIsMan
	 * @return 起运日期（约）
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static Date 起运交脱(Date birthday, boolean genderIsMan,
			TimezoneLocation location) throws Exception {
		boolean isForward = 顺行(birthday, genderIsMan, location);
		节气 term = 节令.节气(birthday.getYear() + 1900, birthday.getMonth() + 1)[0];

		Date tmp = new Date(birthday.getTime()), before = term
				.getSolarTermDate(), after;
		if (before.getTime() > birthday.getTime()) {
			after = before;
			tmp.setMonth(tmp.getMonth() - 1);
			term = 节令.节气(tmp.getYear() + 1900, tmp.getMonth() + 1)[0];
			before = term.getSolarTermDate();
		} else {
			tmp.setMonth(birthday.getMonth() + 1);
			term = 节令.节气(tmp.getYear() + 1900, tmp.getMonth() + 1)[0];
			after = term.getSolarTermDate();
		}
		long ret = 0;
		if (isForward) {
			ret = (after.getTime() - birthday.getTime());
		} else {
			ret = (birthday.getTime() - before.getTime());
		}
		tmp = new Date(birthday.getTime() + ret * 365 / 3);
		return tmp;
	}

	public static 干支[] 大运(Date birthday, boolean genderIsMan, TimezoneLocation location)
			throws Exception {
		boolean isForward = 顺行(birthday, genderIsMan, location);
		int 月柱值 = 四柱.月柱(birthday, location).术数().getIndex();
		干支[] ret = new 干支[8];
		for (int i = 0; i < 8; ++i) {
			if (isForward)
				++月柱值;
			else
				--月柱值;
			ret[i] = 干支.lookup(月柱值);
		}
		return ret;
	}

	@SuppressWarnings("deprecation")
	public static 干支 大运(Date birthday, boolean genderIsMan, TimezoneLocation location,
			Date now) throws Exception {
		int days = (int) ((now.getTime() - 起运交脱(birthday, genderIsMan, location)
				.getTime()) / 1000 / 60 / 60 / 24);
		if (days < 0)
			throw new Exception("未起运。");
		Date diff = new Date(0, 0, days);
		boolean isForward = 顺行(birthday, genderIsMan, location);
		int 月柱值 = 四柱.月柱(birthday, location).术数().getIndex();

		int ret = (isForward ? (月柱值 + diff.getYear() / 10 + 1) : (月柱值
				- diff.getYear() / 10 - 1)) % 60;

		return 干支.lookup(ret < 0 ? (ret + 60) : ret);
	}

	@SuppressWarnings("deprecation")
	public static int 大运经年(Date birthday, boolean genderIsMan,
			TimezoneLocation location, Date now) throws Exception {
		int days = (int) ((now.getTime() - 起运交脱(birthday, genderIsMan, location)
				.getTime()) / 1000 / 60 / 60 / 24);
		if (days < 0)
			throw new Exception("未起运。");
		Date diff = new Date(0, 0, days);

		return diff.getYear() % 10;
	}

	@SuppressWarnings("deprecation")
	public static 干支 小运(Date birthday, boolean genderIsMan, TimezoneLocation location,
			Date now) throws Exception {
		// 按照虚岁计算，应该是阴历生日，此处大约以阳历代替
		int days = (int) ((now.getTime() - birthday.getTime()) / 1000 / 60 / 60 / 24);
		if (days < 0)
			throw new Exception("未出生");
		Date diff = new Date(0, 0, days);

		boolean isForward = 顺行(birthday, genderIsMan, location);
		int 时柱值 = 四柱.时柱(birthday, location).术数().getIndex();

		int ret = (isForward ? (时柱值 + diff.getYear() + 1) : (时柱值
				- diff.getYear() - 1)) % 60;
		return 干支.lookup(ret < 0 ? (ret + 60) : ret);
	}
}
