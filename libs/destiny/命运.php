<?php

class 命运 {
	public static function 顺行($birthday, $genderIsMan, $location = null) {
		$年干 = 四柱::年柱($birthday, $location)->天干()->术数()->getIndex();
		return $genderIsMan ? ($年干 % 2 == 1) : ($年干 % 2 == 0);
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
	 * @param $birthday
	 * @param $genderIsMan
	 * @return 起运日期（约）
	 */
	public static function 起运交脱($birthday, $genderIsMan, $location = null) {
		$isForward = self::顺行($birthday, $genderIsMan, $location);
		$qs = 节令::节气($birthday);
		$term = $qs[0];

		$before = $term->getSolarTermDate();
		if ($before->getTime() > $birthday->getTime()) {
			$after = $before;
			$tmp = Date::get($birthday->getYear(), $birthday->getMonth() - 1, $birthday->getDate());
			$qs = 节令::节气($tmp);
			$term = $qs[0];
			$before = $term->getSolarTermDate();
		} else {
			$tmp = Date::get($birthday->getYear(), $birthday->getMonth() + 1, $birthday->getDate());
			$qs = 节令::节气($tmp);
			$term = $qs[0];
			$after = $term->getSolarTermDate();
		}
		$ret = 0;
		if ($isForward) {
			$ret = ($after->getTime() - $birthday->getTime());
		} else {
			$ret = ($birthday->getTime() - $before->getTime());
		}
		$tmp = new Date($birthday->getTime() + $ret * 365 / 3);
		return $tmp;
	}

	public static function 大运($birthday, $genderIsMan, $location = null) {
		$isForward = self::顺行($birthday, $genderIsMan, $location);
		$月柱值 = 四柱::月柱($birthday, $location)->术数()->getIndex();
		$ret = array();
		for ($i = 0; $i < 8; ++$i) {
			if ($isForward)
				++$月柱值;
			else
				--$月柱值;
			$ret[] = 干支::lookup($月柱值);
		}
		return $ret;
	}

	public static function 大运经年($birthday, $genderIsMan, $now, $location = null) {
		$days = (($now->getTime() - self::起运交脱($birthday, $genderIsMan, $location) ->getTime()) / 60 / 60 / 24);
		if ($days < 0) {
			printf('未起运。');
			exit(0);
		}
		$diff = $days / 365.25;

		return $diff % 10;
	}

	public static function 小运($birthday, $genderIsMan, $now, $location = null) {
		// 按照虚岁计算，应该是阴历生日，此处大约以阳历代替
		$days = (($now->getTime() - $birthday->getTime()) / 60 / 60 / 24);
		if ($days < 0) {
			printf('未出生。');
			exit(0);
		}
		$diff = floor($days / 365.25);

		$isForward = self::顺行($birthday, $genderIsMan, $location);
		$时柱值 = 四柱::时柱($birthday, $location)->术数()->getIndex();

		$ret = ($isForward ? ($时柱值 + $diff + 1) : ($时柱值 - $diff - 1)) % 60;
		return 干支::lookup($ret);
	}
}