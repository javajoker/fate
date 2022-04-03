<?php

class LunarUtils {

	/**
	 * 判断y年m月(1,2,..,12,下同)d日是Gregorian历还是Julian历
	 * 
	 * （$opt=1,2,3分别表示标准日历,Gregorge历和Julian历）
	 * 
	 * @param $y
	 * @param $m
	 * @param $d
	 * @param $opt
	 * @return
	 */
	private static function isGregorian($y, $m, $d, $opt) {
		if ($opt == 1) {
			if ($y > 1582 || ($y == 1582 && $m > 10)
					|| ($y == 1582 && $m == 10 && $d > 14))
				return true; // Gregorian
			else if ($y == 1582 && $m == 10 && $d >= 5 && $d <= 14) {
				printf('Special days removed from Gregorian');
				exit(0);
			}
			else
				return false; // Julian
		}

		if ($opt == 2)
			return true; // Gregorian
		if ($opt == 3)
			return false; // Julian
		printf('Invalid $opt, should be 1, 2, 3');
		exit(0);
	}

	/**
	 * 返回等效标准天数
	 * 
	 * y年m月d日相应历种的1年1月1日的等效(即对Gregorian历与Julian历是统一的)天数
	 * 
	 * @param $y
	 * @param $m
	 * @param $d
	 * @return
	 */
	public static function equivalentStandardDay($y, $m, $d) {
		$v = ($y - 1) * 365 + floor((($y - 1) / 4))
				+ self::dayDifference($y, $m, $d) - 2; // Julian的等效标准天数
		if ($y > 1582)
			$v += -floor((($y - 1) / 100))
					+ floor((($y - 1) / 400)) + 2; // Gregorian的等效标准天数
		return $v;
	}

	/**
	 * 返回阳历y年m月d日的日差天数
	 * 
	 * （在y年年内所走过的天数，如2000年3月1日为61）
	 * 
	 * @param $y
	 * @param $m
	 * @param $d
	 * @return
	 */
	public static function dayDifference($y, $m, $d) {
		$isG = self::isGregorian($y, $m, $d, 1);
		$monL = array( 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 );
		if ($isG)
			if (($y % 100 != 0 && $y % 4 == 0) || ($y % 400 == 0))
				$monL[2] += 1;
			else if ($y % 4 == 0)
				$monL[2] += 1;
		$v = 0;
		for ($i = 0; $i <= $m - 1; $i++) {
			$v += $monL[$i];
		}
		$v += $d;
		if ($y == 1582) {
			if ($isG)
				$v -= 10;
		}
		return $v;
	}

	/**
	 * 返回阳历y年日差天数为x时所对应的月日数
	 * 
	 * 如y=2000，$x=274时，返回1001(表示10月1日，即返回100*$m+$d)）
	 * 
	 * @param $y
	 * @param $x
	 * @return
	 */
	public static function antiDayDifference($y, $x) {
		$m = 1;
		for ($j = 1; $j <= 12; $j++) {
			$mL = self::dayDifference($y, $j + 1, 1) - self::dayDifference($y, $j, 1);
			if ($x <= $mL || $j == 12) {
				$m = $j;
				break;
			} else
				$x -= $mL;
		}
		return 100 * $m + $x;
	}

	/**
	 * 返回x的小数尾数，若x为负值，则是1-小数尾数
	 * 
	 * @param $x
	 * @return
	 */
	public static function tail($x) {
		return $x - floor($x);
	}

	/**
	 * 角度函数。原始：ang
	 * 
	 * @param $x
	 * @param $t
	 * @param $c1
	 * @param $t0
	 * @param $t2
	 * @param $t3
	 * @return
	 */
	public static function angle($x, $t, $c1, $t0, $t2, $t3) {
		return self::tail($c1 * $x) * 2 * pi() + $t0 - $t2 * $t * $t - $t3 * $t * $t * $t;
	}

	/**
	 * 广义求余
	 * 
	 * @param $x
	 * @param $w
	 * @return
	 */
	public static function rem($x, $w) {
		return self::tail(($x / $w)) * $w;
	}
}
