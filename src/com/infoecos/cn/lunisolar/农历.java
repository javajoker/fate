package com.infoecos.cn.lunisolar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * from 1901-2-19 to 2101-1-18
 * 
 * @author dch
 * 
 */
public class 农历 {
	private static final int LeapMonth = 0;
	private static final int Jan1Month = 1;
	private static final int Jan1Date = 2;
	private static final int nDaysPerMonth = 3;

	// Fields
	private static final int[] DaysToMonth365 = new int[] { 0, 0x1f, 0x3b, 90,
			120, 0x97, 0xb5, 0xd4, 0xf3, 0x111, 0x130, 0x14e };
	private static final int[] DaysToMonth366 = new int[] { 0, 0x1f, 60, 0x5b,
			0x79, 0x98, 0xb6, 0xd5, 0xf4, 0x112, 0x131, 0x14f };

	private static final int[][] yinfo;

	// Methods
	static {
		yinfo = new int[][] { { 0, 2, 0x13, 0x4ae0 }, { 0, 2, 8, 0xa570 },
				{ 5, 1, 0x1d, 0x5268 }, { 0, 2, 0x10, 0xd260 },
				{ 0, 2, 4, 0xd950 }, { 4, 1, 0x19, 0x6aa8 },
				{ 0, 2, 13, 0x56a0 }, { 0, 2, 2, 0x9ad0 },
				{ 2, 1, 0x16, 0x4ae8 }, { 0, 2, 10, 0x4ae0 },
				{ 6, 1, 30, 0xa4d8 }, { 0, 2, 0x12, 0xa4d0 },
				{ 0, 2, 6, 0xd250 }, { 5, 1, 0x1a, 0xd528 },
				{ 0, 2, 14, 0xb540 }, { 0, 2, 3, 0xd6a0 },
				{ 2, 1, 0x17, 0x96d0 }, { 0, 2, 11, 0x95b0 },
				{ 7, 2, 1, 0x49b8 }, { 0, 2, 20, 0x4970 }, { 0, 2, 8, 0xa4b0 },
				{ 5, 1, 0x1c, 0xb258 }, { 0, 2, 0x10, 0x6a50 },
				{ 0, 2, 5, 0x6d40 }, { 4, 1, 0x18, 0xada8 },
				{ 0, 2, 13, 0x2b60 }, { 0, 2, 2, 0x9570 },
				{ 2, 1, 0x17, 0x4978 }, { 0, 2, 10, 0x4970 },
				{ 6, 1, 30, 0x64b0 }, { 0, 2, 0x11, 0xd4a0 },
				{ 0, 2, 6, 0xea50 }, { 5, 1, 0x1a, 0x6d48 },
				{ 0, 2, 14, 0x5ad0 }, { 0, 2, 4, 0x2b60 },
				{ 3, 1, 0x18, 0x9370 }, { 0, 2, 11, 0x92e0 },
				{ 7, 1, 0x1f, 0xc968 }, { 0, 2, 0x13, 0xc950 },
				{ 0, 2, 8, 0xd4a0 }, { 6, 1, 0x1b, 0xda50 },
				{ 0, 2, 15, 0xb550 }, { 0, 2, 5, 0x56a0 },
				{ 4, 1, 0x19, 0xaad8 }, { 0, 2, 13, 0x25d0 },
				{ 0, 2, 2, 0x92d0 }, { 2, 1, 0x16, 0xc958 },
				{ 0, 2, 10, 0xa950 }, { 7, 1, 0x1d, 0xb4a8 },
				{ 0, 2, 0x11, 0x6ca0 }, { 0, 2, 6, 0xb550 },
				{ 5, 1, 0x1b, 0x55a8 }, { 0, 2, 14, 0x4da0 },
				{ 0, 2, 3, 0xa5b0 }, { 3, 1, 0x18, 0x52b8 },
				{ 0, 2, 12, 0x52b0 }, { 8, 1, 0x1f, 0xa950 },
				{ 0, 2, 0x12, 0xe950 }, { 0, 2, 8, 0x6aa0 },
				{ 6, 1, 0x1c, 0xad50 }, { 0, 2, 15, 0xab50 },
				{ 0, 2, 5, 0x4b60 }, { 4, 1, 0x19, 0xa570 },
				{ 0, 2, 13, 0xa570 }, { 0, 2, 2, 0x5260 },
				{ 3, 1, 0x15, 0xe930 }, { 0, 2, 9, 0xd950 },
				{ 7, 1, 30, 0x5aa8 }, { 0, 2, 0x11, 0x56a0 },
				{ 0, 2, 6, 0x96d0 }, { 5, 1, 0x1b, 0x4ae8 },
				{ 0, 2, 15, 0x4ad0 }, { 0, 2, 3, 0xa4d0 },
				{ 4, 1, 0x17, 0xd268 }, { 0, 2, 11, 0xd250 },
				{ 8, 1, 0x1f, 0xd528 }, { 0, 2, 0x12, 0xb540 },
				{ 0, 2, 7, 0xb6a0 }, { 6, 1, 0x1c, 0x96d0 },
				{ 0, 2, 0x10, 0x95b0 }, { 0, 2, 5, 0x49b0 },
				{ 4, 1, 0x19, 0xa4b8 }, { 0, 2, 13, 0xa4b0 },
				{ 10, 2, 2, 0xb258 }, { 0, 2, 20, 0x6a50 },
				{ 0, 2, 9, 0x6d40 }, { 6, 1, 0x1d, 0xada0 },
				{ 0, 2, 0x11, 0xab60 }, { 0, 2, 6, 0x9570 },
				{ 5, 1, 0x1b, 0x4978 }, { 0, 2, 15, 0x4970 },
				{ 0, 2, 4, 0x64b0 }, { 3, 1, 0x17, 0x6a50 },
				{ 0, 2, 10, 0xea50 }, { 8, 1, 0x1f, 0x6b28 },
				{ 0, 2, 0x13, 0x5ac0 }, { 0, 2, 7, 0xab60 },
				{ 5, 1, 0x1c, 0x9368 }, { 0, 2, 0x10, 0x92e0 },
				{ 0, 2, 5, 0xc960 }, { 4, 1, 0x18, 0xd4a8 },
				{ 0, 2, 12, 0xd4a0 }, { 0, 2, 1, 0xda50 },
				{ 2, 1, 0x16, 0x5aa8 }, { 0, 2, 9, 0x56a0 },
				{ 7, 1, 0x1d, 0xaad8 }, { 0, 2, 0x12, 0x25d0 },
				{ 0, 2, 7, 0x92d0 }, { 5, 1, 0x1a, 0xc958 },
				{ 0, 2, 14, 0xa950 }, { 0, 2, 3, 0xb4a0 },
				{ 4, 1, 0x17, 0xb550 }, { 0, 2, 10, 0xad50 },
				{ 9, 1, 0x1f, 0x55a8 }, { 0, 2, 0x13, 0x4ba0 },
				{ 0, 2, 8, 0xa5b0 }, { 6, 1, 0x1c, 0x52b8 },
				{ 0, 2, 0x10, 0x52b0 }, { 0, 2, 5, 0xa930 },
				{ 4, 1, 0x19, 0x74a8 }, { 0, 2, 12, 0x6aa0 },
				{ 0, 2, 1, 0xad50 }, { 2, 1, 0x16, 0x4da8 },
				{ 0, 2, 10, 0x4b60 }, { 6, 1, 0x1d, 0xa570 },
				{ 0, 2, 0x11, 0xa4e0 }, { 0, 2, 6, 0xd260 },
				{ 5, 1, 0x1a, 0xe930 }, { 0, 2, 13, 0xd530 },
				{ 0, 2, 3, 0x5aa0 }, { 3, 1, 0x17, 0x6b50 },
				{ 0, 2, 11, 0x96d0 }, { 11, 1, 0x1f, 0x4ae8 },
				{ 0, 2, 0x13, 0x4ad0 }, { 0, 2, 8, 0xa4d0 },
				{ 6, 1, 0x1c, 0xd258 }, { 0, 2, 15, 0xd250 },
				{ 0, 2, 4, 0xd520 }, { 5, 1, 0x18, 0xdaa0 },
				{ 0, 2, 12, 0xb5a0 }, { 0, 2, 1, 0x56d0 },
				{ 2, 1, 0x16, 0x4ad8 }, { 0, 2, 10, 0x49b0 },
				{ 7, 1, 30, 0xa4b8 }, { 0, 2, 0x11, 0xa4b0 },
				{ 0, 2, 6, 0xaa50 }, { 5, 1, 0x1a, 0xb528 },
				{ 0, 2, 14, 0x6d20 }, { 0, 2, 2, 0xada0 },
				{ 3, 1, 0x17, 0x55b0 }, { 0, 2, 11, 0x9370 },
				{ 8, 2, 1, 0x4978 }, { 0, 2, 0x13, 0x4970 },
				{ 0, 2, 8, 0x64b0 }, { 6, 1, 0x1c, 0x6a50 },
				{ 0, 2, 15, 0xea50 }, { 0, 2, 4, 0x6b20 },
				{ 4, 1, 0x18, 0xab60 }, { 0, 2, 12, 0xaae0 },
				{ 0, 2, 2, 0x92e0 }, { 3, 1, 0x15, 0xc970 },
				{ 0, 2, 9, 0xc960 }, { 7, 1, 0x1d, 0xd4a8 },
				{ 0, 2, 0x11, 0xd4a0 }, { 0, 2, 5, 0xda50 },
				{ 5, 1, 0x1a, 0x5aa8 }, { 0, 2, 14, 0x56a0 },
				{ 0, 2, 3, 0xa6d0 }, { 4, 1, 0x17, 0x52e8 },
				{ 0, 2, 11, 0x52d0 }, { 8, 1, 0x1f, 0xa958 },
				{ 0, 2, 0x13, 0xa950 }, { 0, 2, 7, 0xb4a0 },
				{ 6, 1, 0x1b, 0xb550 }, { 0, 2, 15, 0xad50 },
				{ 0, 2, 5, 0x55a0 }, { 4, 1, 0x18, 0xa5d0 },
				{ 0, 2, 12, 0xa5b0 }, { 0, 2, 2, 0x52b0 },
				{ 3, 1, 0x16, 0xa938 }, { 0, 2, 9, 0x6930 },
				{ 7, 1, 0x1d, 0x7298 }, { 0, 2, 0x11, 0x6aa0 },
				{ 0, 2, 6, 0xad50 }, { 5, 1, 0x1a, 0x4da8 },
				{ 0, 2, 14, 0x4b60 }, { 0, 2, 3, 0xa570 },
				{ 4, 1, 0x18, 0x5270 }, { 0, 2, 10, 0xd260 },
				{ 8, 1, 30, 0xe930 }, { 0, 2, 0x12, 0xd520 },
				{ 0, 2, 7, 0xdaa0 }, { 6, 1, 0x1b, 0x6b50 },
				{ 0, 2, 15, 0x56d0 }, { 0, 2, 5, 0x4ae0 },
				{ 4, 1, 0x19, 0xa4e8 }, { 0, 2, 12, 0xa4d0 },
				{ 0, 2, 1, 0xd150 }, { 2, 1, 0x15, 0xd928 },
				{ 0, 2, 9, 0xd520 } };
	}

	private static int GetYearInfo(int LunarYear, int Index) throws Exception {
		if ((LunarYear < 0x76d) || (LunarYear > 0x834)) {
			throw new Exception(String.format(
					"year ArgumentOutOfRange_Range: %d - %d", 0x76d, 0x834));
		}
		return yinfo[LunarYear - 0x76d][Index];
	}

	public static int getMaxCalendarYear() {
		return 0x834;
	}

	public static int getMinCalendarYear() {
		return 0x76d;
	}

	private static int GergIsleap(int y) {
		if ((y % 4) == 0) {
			if ((y % 100) != 0) {
				return 1;
			}
			if ((y % 400) == 0) {
				return 1;
			}
		}
		return 0;
	}

	private static LunarDate GregorianToLunar(int nSYear, int nSMonth,
			int nSDate) throws Exception {
		int nLYear, nLMonth, nLDate;

		int num7;
		int num8;
		int num = (GergIsleap(nSYear) == 1) ? DaysToMonth366[nSMonth - 1]
				: DaysToMonth365[nSMonth - 1];
		num += nSDate;
		int num2 = num;
		nLYear = nSYear;
		if (nLYear == (getMaxCalendarYear() + 1)) {
			nLYear--;
			num2 += (GergIsleap(nLYear) == 1) ? 0x16e : 0x16d;
			num7 = GetYearInfo(nLYear, Jan1Month);
			num8 = GetYearInfo(nLYear, Jan1Date);
		} else {
			num7 = GetYearInfo(nLYear, Jan1Month);
			num8 = GetYearInfo(nLYear, Jan1Date);
			if ((nSMonth < num7) || ((nSMonth == num7) && (nSDate < num8))) {
				nLYear--;
				num2 += (GergIsleap(nLYear) == 1) ? 0x16e : 0x16d;
				num7 = GetYearInfo(nLYear, Jan1Month);
				num8 = GetYearInfo(nLYear, Jan1Date);
			}
		}
		num2 -= DaysToMonth365[num7 - 1];
		num2 -= num8 - 1;
		int num5 = 0x8000;
		int yearInfo = GetYearInfo(nLYear, nDaysPerMonth);
		int num6 = ((yearInfo & num5) != 0) ? 30 : 0x1d;
		nLMonth = 1;
		while (num2 > num6) {
			num2 -= num6;
			nLMonth++;
			num5 = num5 >> 1;
			num6 = ((yearInfo & num5) != 0) ? 30 : 0x1d;
		}
		nLDate = num2;

		return new LunarDate(nLYear, nLMonth, nLDate);
	}

	public static LunarDate TimeToLunar(Date time) throws Exception {
		int nSYear = 0;
		int nSMonth = 0;
		int nSDate = 0;
		Calendar defaultInstance = GregorianCalendar.getInstance();
		defaultInstance.setTime(time);
		nSYear = defaultInstance.get(Calendar.YEAR);
		nSMonth = defaultInstance.get(Calendar.MONTH) + 1;
		nSDate = defaultInstance.get(Calendar.DATE);

		return GregorianToLunar(nSYear, nSMonth, nSDate);
	}

	@SuppressWarnings("deprecation")
	private static Date LunarToGregorian(int nLYear, int nLMonth, int nLDate)
			throws Exception {
		int numLunarDays;

		if (nLDate < 1 || nLDate > 30)
			throw new Exception(String.format(
					"month ArgumentOutOfRange_Range: %d - %d", 1, 30));

		numLunarDays = nLDate - 1;

		// Add previous months days to form the total num of days from the first
		// of the month.
		for (int i = 1; i < nLMonth; i++) {
			numLunarDays += InternalGetDaysInMonth(nLYear, i);
		}

		// Get Gregorian First of year
		int nJan1Month = GetYearInfo(nLYear, Jan1Month);
		int nJan1Date = GetYearInfo(nLYear, Jan1Date);

		// calc the solar day of year of 1 Lunar day
		int fLeap = GergIsleap(nLYear);
		int[] days = (fLeap == 1) ? DaysToMonth366 : DaysToMonth365;

		int nSolarYear, nSolarMonth, nSolarDay;

		nSolarDay = nJan1Date;

		if (nJan1Month > 1)
			nSolarDay += days[nJan1Month - 1];

		// Add the actual lunar day to get the solar day we want
		nSolarDay = nSolarDay + numLunarDays;
		// - 1;
		if (nSolarDay > (fLeap + 365)) {
			nSolarYear = nLYear + 1;
			nSolarDay -= (fLeap + 365);
		} else {
			nSolarYear = nLYear;
		}

		for (nSolarMonth = 1; nSolarMonth < 12; nSolarMonth++) {
			if (days[nSolarMonth] >= nSolarDay)
				break;
		}

		nSolarDay -= days[nSolarMonth - 1];
		return new Date(nSolarYear - 1900, nSolarMonth - 1, nSolarDay);
	}

	public static Date LunarToTime(LunarDate ld) throws Exception {
		return LunarToGregorian(ld.getYear(), ld.getMonth(), ld.getDay());
	}

	protected static int GetMonth(Date time) throws Exception {
		LunarDate ld = TimeToLunar(time);
		return ld.month;
	}

	protected static int GetDayOfMonth(Date time) throws Exception {
		LunarDate ld = TimeToLunar(time);
		return ld.day;
	}

	protected static int GetLeapMonth(int year) throws Exception {
		int yearInfo = GetYearInfo(year, LeapMonth);
		if (yearInfo > 0) {
			return (yearInfo + 1);
		}
		return 0;
	}

	protected static int getDayOfYear(Date a1) {
		Calendar c = GregorianCalendar.getInstance();
		c.setTime(a1);
		return c.get(Calendar.DAY_OF_YEAR);
	}

	private static int InternalGetDaysInMonth(int year, int month)
			throws Exception {
		int num2 = 0x8000;
		num2 = num2 >> (month - 1);
		if ((GetYearInfo(year, nDaysPerMonth) & num2) == 0) {
			return 0x1d;
		}
		return 30;
	}

	protected static int GetDayOfYear(Date time) throws Exception {
		LunarDate ld = TimeToLunar(time);
		int day = ld.day;
		for (int i = 1; i < ld.month; i++) {
			day += InternalGetDaysInMonth(ld.year, i);
		}
		return day;
	}
}
