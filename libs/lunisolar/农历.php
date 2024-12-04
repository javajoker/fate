<?php
namespace DchLib;
class 农历 {
	private static $LeapMonth = 0;
	private static $Jan1Month = 1;
	private static $Jan1Date = 2;
	private static $nDaysPerMonth = 3;

	// Fields
	private static $DaysToMonth365 = array( 0, 0x1f, 0x3b, 90,
			120, 0x97, 0xb5, 0xd4, 0xf3, 0x111, 0x130, 0x14e );
	private static $DaysToMonth366 = array( 0, 0x1f, 60, 0x5b,
			0x79, 0x98, 0xb6, 0xd5, 0xf4, 0x112, 0x131, 0x14f );

	private static $yinfo = array( 
		array( 0, 2, 0x13, 0x4ae0 ), array( 0, 2, 8, 0xa570 ), array( 5, 1, 0x1d, 0x5268 ), array( 0, 2, 0x10, 0xd260 ), array( 0, 2, 4, 0xd950 ), 
		array( 4, 1, 0x19, 0x6aa8 ), array( 0, 2, 13, 0x56a0 ), array( 0, 2, 2, 0x9ad0 ), array( 2, 1, 0x16, 0x4ae8 ), array( 0, 2, 10, 0x4ae0 ), 
		array( 6, 1, 30, 0xa4d8 ), array( 0, 2, 0x12, 0xa4d0 ), array( 0, 2, 6, 0xd250 ), array( 5, 1, 0x1a, 0xd528 ), array( 0, 2, 14, 0xb540 ),
		array( 0, 2, 3, 0xd6a0 ), array( 2, 1, 0x17, 0x96d0 ), array( 0, 2, 11, 0x95b0 ), array( 7, 2, 1, 0x49b8 ), array( 0, 2, 20, 0x4970 ), 
		array( 0, 2, 8, 0xa4b0 ), array( 5, 1, 0x1c, 0xb258 ), array( 0, 2, 0x10, 0x6a50 ), array( 0, 2, 5, 0x6d40 ), array( 4, 1, 0x18, 0xada8 ), 
		array( 0, 2, 13, 0x2b60 ), array( 0, 2, 2, 0x9570 ), array( 2, 1, 0x17, 0x4978 ), array( 0, 2, 10, 0x4970 ), array( 6, 1, 30, 0x64b0 ),
		array( 0, 2, 0x11, 0xd4a0 ), array( 0, 2, 6, 0xea50 ), array( 5, 1, 0x1a, 0x6d48 ), array( 0, 2, 14, 0x5ad0 ), array( 0, 2, 4, 0x2b60 ),
		array( 3, 1, 0x18, 0x9370 ), array( 0, 2, 11, 0x92e0 ), array( 7, 1, 0x1f, 0xc968 ), array( 0, 2, 0x13, 0xc950 ), array( 0, 2, 8, 0xd4a0 ),
		array( 6, 1, 0x1b, 0xda50 ), array( 0, 2, 15, 0xb550 ), array( 0, 2, 5, 0x56a0 ), array( 4, 1, 0x19, 0xaad8 ), array( 0, 2, 13, 0x25d0 ),
		array( 0, 2, 2, 0x92d0 ), array( 2, 1, 0x16, 0xc958 ), array( 0, 2, 10, 0xa950 ), array( 7, 1, 0x1d, 0xb4a8 ), array( 0, 2, 0x11, 0x6ca0 ),
		array( 0, 2, 6, 0xb550 ), array( 5, 1, 0x1b, 0x55a8 ), array( 0, 2, 14, 0x4da0 ), array( 0, 2, 3, 0xa5b0 ), array( 3, 1, 0x18, 0x52b8 ),
		array( 0, 2, 12, 0x52b0 ), array( 8, 1, 0x1f, 0xa950 ), array( 0, 2, 0x12, 0xe950 ), array( 0, 2, 8, 0x6aa0 ), array( 6, 1, 0x1c, 0xad50 ),
		array( 0, 2, 15, 0xab50 ), array( 0, 2, 5, 0x4b60 ), array( 4, 1, 0x19, 0xa570 ), array( 0, 2, 13, 0xa570 ), array( 0, 2, 2, 0x5260 ),
		array( 3, 1, 0x15, 0xe930 ), array( 0, 2, 9, 0xd950 ), array( 7, 1, 30, 0x5aa8 ), array( 0, 2, 0x11, 0x56a0 ), array( 0, 2, 6, 0x96d0 ),
		array( 5, 1, 0x1b, 0x4ae8 ), array( 0, 2, 15, 0x4ad0 ), array( 0, 2, 3, 0xa4d0 ), array( 4, 1, 0x17, 0xd268 ), array( 0, 2, 11, 0xd250 ),
		array( 8, 1, 0x1f, 0xd528 ), array( 0, 2, 0x12, 0xb540 ), array( 0, 2, 7, 0xb6a0 ), array( 6, 1, 0x1c, 0x96d0 ), array( 0, 2, 0x10, 0x95b0 ),
		array( 0, 2, 5, 0x49b0 ), array( 4, 1, 0x19, 0xa4b8 ), array( 0, 2, 13, 0xa4b0 ), array( 10, 2, 2, 0xb258 ), array( 0, 2, 20, 0x6a50 ),
		array( 0, 2, 9, 0x6d40 ), array( 6, 1, 0x1d, 0xada0 ), array( 0, 2, 0x11, 0xab60 ), array( 0, 2, 6, 0x9570 ), array( 5, 1, 0x1b, 0x4978 ),
		array( 0, 2, 15, 0x4970 ), array( 0, 2, 4, 0x64b0 ), array( 3, 1, 0x17, 0x6a50 ), array( 0, 2, 10, 0xea50 ), array( 8, 1, 0x1f, 0x6b28 ),
		array( 0, 2, 0x13, 0x5ac0 ), array( 0, 2, 7, 0xab60 ), array( 5, 1, 0x1c, 0x9368 ), array( 0, 2, 0x10, 0x92e0 ), array( 0, 2, 5, 0xc960 ),
		array( 4, 1, 0x18, 0xd4a8 ), array( 0, 2, 12, 0xd4a0 ), array( 0, 2, 1, 0xda50 ), array( 2, 1, 0x16, 0x5aa8 ), array( 0, 2, 9, 0x56a0 ),
		array( 7, 1, 0x1d, 0xaad8 ), array( 0, 2, 0x12, 0x25d0 ), array( 0, 2, 7, 0x92d0 ), array( 5, 1, 0x1a, 0xc958 ), array( 0, 2, 14, 0xa950 ),
		array( 0, 2, 3, 0xb4a0 ), array( 4, 1, 0x17, 0xb550 ), array( 0, 2, 10, 0xad50 ), array( 9, 1, 0x1f, 0x55a8 ), array( 0, 2, 0x13, 0x4ba0 ),
		array( 0, 2, 8, 0xa5b0 ), array( 6, 1, 0x1c, 0x52b8 ), array( 0, 2, 0x10, 0x52b0 ), array( 0, 2, 5, 0xa930 ), array( 4, 1, 0x19, 0x74a8 ),
		array( 0, 2, 12, 0x6aa0 ), array( 0, 2, 1, 0xad50 ), array( 2, 1, 0x16, 0x4da8 ), array( 0, 2, 10, 0x4b60 ), array( 6, 1, 0x1d, 0xa570 ),
		array( 0, 2, 0x11, 0xa4e0 ), array( 0, 2, 6, 0xd260 ), array( 5, 1, 0x1a, 0xe930 ), array( 0, 2, 13, 0xd530 ), array( 0, 2, 3, 0x5aa0 ),
		array( 3, 1, 0x17, 0x6b50 ), array( 0, 2, 11, 0x96d0 ), array( 11, 1, 0x1f, 0x4ae8 ), array( 0, 2, 0x13, 0x4ad0 ), array( 0, 2, 8, 0xa4d0 ),
		array( 6, 1, 0x1c, 0xd258 ), array( 0, 2, 15, 0xd250 ), array( 0, 2, 4, 0xd520 ), array( 5, 1, 0x18, 0xdaa0 ), array( 0, 2, 12, 0xb5a0 ),
		array( 0, 2, 1, 0x56d0 ), array( 2, 1, 0x16, 0x4ad8 ), array( 0, 2, 10, 0x49b0 ), array( 7, 1, 30, 0xa4b8 ), array( 0, 2, 0x11, 0xa4b0 ),
		array( 0, 2, 6, 0xaa50 ), array( 5, 1, 0x1a, 0xb528 ), array( 0, 2, 14, 0x6d20 ), array( 0, 2, 2, 0xada0 ), array( 3, 1, 0x17, 0x55b0 ),
		array( 0, 2, 11, 0x9370 ), array( 8, 2, 1, 0x4978 ), array( 0, 2, 0x13, 0x4970 ), array( 0, 2, 8, 0x64b0 ), array( 6, 1, 0x1c, 0x6a50 ),
		array( 0, 2, 15, 0xea50 ), array( 0, 2, 4, 0x6b20 ), array( 4, 1, 0x18, 0xab60 ), array( 0, 2, 12, 0xaae0 ), array( 0, 2, 2, 0x92e0 ),
		array( 3, 1, 0x15, 0xc970 ), array( 0, 2, 9, 0xc960 ), array( 7, 1, 0x1d, 0xd4a8 ), array( 0, 2, 0x11, 0xd4a0 ), array( 0, 2, 5, 0xda50 ),
		array( 5, 1, 0x1a, 0x5aa8 ), array( 0, 2, 14, 0x56a0 ), array( 0, 2, 3, 0xa6d0 ), array( 4, 1, 0x17, 0x52e8 ), array( 0, 2, 11, 0x52d0 ),
		array( 8, 1, 0x1f, 0xa958 ), array( 0, 2, 0x13, 0xa950 ), array( 0, 2, 7, 0xb4a0 ), array( 6, 1, 0x1b, 0xb550 ), array( 0, 2, 15, 0xad50 ),
		array( 0, 2, 5, 0x55a0 ), array( 4, 1, 0x18, 0xa5d0 ), array( 0, 2, 12, 0xa5b0 ), array( 0, 2, 2, 0x52b0 ), array( 3, 1, 0x16, 0xa938 ),
		array( 0, 2, 9, 0x6930 ), array( 7, 1, 0x1d, 0x7298 ), array( 0, 2, 0x11, 0x6aa0 ), array( 0, 2, 6, 0xad50 ), array( 5, 1, 0x1a, 0x4da8 ),
		array( 0, 2, 14, 0x4b60 ), array( 0, 2, 3, 0xa570 ), array( 4, 1, 0x18, 0x5270 ), array( 0, 2, 10, 0xd260 ), array( 8, 1, 30, 0xe930 ),
		array( 0, 2, 0x12, 0xd520 ), array( 0, 2, 7, 0xdaa0 ), array( 6, 1, 0x1b, 0x6b50 ), array( 0, 2, 15, 0x56d0 ), array( 0, 2, 5, 0x4ae0 ),
		array( 4, 1, 0x19, 0xa4e8 ), array( 0, 2, 12, 0xa4d0 ), array( 0, 2, 1, 0xd150 ), array( 2, 1, 0x15, 0xd928 ), array( 0, 2, 9, 0xd520 ) 
	);
	
	private static function getYearInfo($LunarYear, $Index) {
		if (($LunarYear < 0x76d) || ($LunarYear > 0x834)) {
			printf ('年限: %d - %d', 0x76d, 0x834);
			exit(0);
		}
		return self::$yinfo[$LunarYear - 0x76d][$Index];
	}

	private static function getMaxCalendarYear() {
		return 0x834;
	}

	private static function getMinCalendarYear() {
		return 0x76d;
	}

	private static function gergIsleap($y) {
		if (($y % 4) == 0) {
			if (($y % 100) != 0) {
				return 1;
			}
			if (($y % 400) == 0) {
				return 1;
			}
		}
		return 0;
	}

	private static function gregorianToLunar($nSYear, $nSMonth, $nSDate) {
		$num = (self::gergIsleap($nSYear) == 1) ? self::$DaysToMonth366[$nSMonth - 1]
				: self::$DaysToMonth365[$nSMonth - 1];
		$num += $nSDate;
		$num2 = $num;
		$nLYear = $nSYear;
		if ($nLYear == (self::getMaxCalendarYear() + 1)) {
			-- $nLYear;
			$num2 += (self::gergIsleap($nLYear) == 1) ? 0x16e : 0x16d;
			$num7 = self::getYearInfo($nLYear, self::$Jan1Month);
			$num8 = self::getYearInfo($nLYear, self::$Jan1Date);
		} else {
			$num7 = self::getYearInfo($nLYear, self::$Jan1Month);
			$num8 = self::getYearInfo($nLYear, self::$Jan1Date);
			if (($nSMonth < $num7) || (($nSMonth == $num7) && ($nSDate < $num8))) {
				$nLYear--;
				$num2 += (self::gergIsleap($nLYear) == 1) ? 0x16e : 0x16d;
				$num7 = self::getYearInfo($nLYear, self::$Jan1Month);
				$num8 = self::getYearInfo($nLYear, self::$Jan1Date);
			}
		}
		$num2 -= self::$DaysToMonth365[$num7 - 1];
		$num2 -= $num8 - 1;
		$num5 = 0x8000;
		$yearInfo = self::getYearInfo($nLYear, self::$nDaysPerMonth);
		$num6 = (($yearInfo & $num5) != 0) ? 30 : 0x1d;
		$nLMonth = 1;
		while ($num2 > $num6) {
			$num2 -= $num6;
			$nLMonth++;
			$num5 = $num5 >> 1;
			$num6 = (($yearInfo & $num5) != 0) ? 30 : 0x1d;
		}
		$nLDate = $num2;

		return new LunarDate($nLYear, $nLMonth, $nLDate);
	}

	public static function timeToLunar($time) {
		$nSYear = $time->getYear();
		$nSMonth = $time->getMonth();
		$nSDate = $time->getDate();

		return self::gregorianToLunar($nSYear, $nSMonth, $nSDate);
	}

	private static function lunarToGregorian($nLYear, $nLMonth, $nLDate) {
		$numLunarDays;

		if ($nLDate < 1 || $nLDate > 30) {
			printf ('日限: %d - %d', 1, 30);
			exit(0);
		}

		$numLunarDays = $nLDate - 1;

		// Add previous months days to form the total num of days from the first
		// of the month.
		for ($i = 1; $i < $nLMonth; $i++) {
			$numLunarDays += self::internalGetDaysInMonth($nLYear, $i);
		}

		// Get Gregorian First of year
		$nJan1Month = self::getYearInfo($nLYear, self::$Jan1Month);
		$nJan1Date = self::getYearInfo($nLYear, self::$Jan1Date);

		// calc the solar day of year of 1 Lunar day
		$fLeap = self::gergIsleap($nLYear);
		$days = ($fLeap == 1) ? self::$DaysToMonth366 : self::$DaysToMonth365;

		$nSolarDay = $nJan1Date;

		if ($nJan1Month > 1)
			$nSolarDay += $days[$nJan1Month - 1];

		// Add the actual lunar day to get the solar day we want
		$nSolarDay = $nSolarDay + $numLunarDays;
		// - 1;
		if ($nSolarDay > ($fLeap + 365)) {
			$nSolarYear = $nLYear + 1;
			$nSolarDay -= ($fLeap + 365);
		} else {
			$nSolarYear = $nLYear;
		}

		for ($nSolarMonth = 1; $nSolarMonth < 12; $nSolarMonth++) {
			if ($days[$nSolarMonth] >= $nSolarDay)
				break;
		}

		$nSolarDay -= $days[$nSolarMonth - 1];
		return Date::get($nSolarYear, $nSolarMonth, $nSolarDay);
	}

	public static function lunarToTime($ld) {
		return self::lunarToGregorian($ld->getYear(), $ld->getMonth(), $ld->getDate());
	}

	protected static function getMonth($time) {
		$ld = self::timeToLunar($time);
		return $ld->getMonth();
	}

	protected static function getDayOfMonth($time) {
		$ld = self::timeToLunar($time);
		return $ld->getDate();
	}

	protected static function getLeapMonth($year) {
		$yearInfo = self::getYearInfo($year, self::$LeapMonth);
		if ($yearInfo > 0) {
			return ($yearInfo + 1);
		}
		return 0;
	}

	private static function internalGetDaysInMonth($year, $month) {
		$num2 = 0x8000;
		$num2 = $num2 >> ($month - 1);
		if ((self::getYearInfo($year, self::$nDaysPerMonth) & $num2) == 0) {
			return 0x1d;
		}
		return 30;
	}

	protected static function getDayOfYear($time) {
		$ld = self::timeToLunar($time);
		$day = $ld->getDate();
		for ($i = 1; $i < $ld->getMonth(); $i++) {
			$day += self::internalGetDaysInMonth($ld->getYear(), $i);
		}
		return $day;
	}
}
