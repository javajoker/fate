<?php

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
class 四柱 {
	private static function getLocalAbsoluteDate($solarDateTime, $location) {
		if($location == null) $location = new TimezoneLocation();
		return $solarDateTime;
	}

	private static function getStandardDate($solarDateTime, $location) {
		if($location == null) $location = new TimezoneLocation();
		return $solarDateTime;
	}

	/**
	 * 获取年柱。
	 * 
	 * @param $solarDateTime
	 * @return
	 */
	private static function _年柱($solarDateTime) {
		$g = ($solarDateTime->getYear() - 1900 + 36) % 60;
		if ((LunarUtils::dayDifference(
				$solarDateTime->getYear(),
				$solarDateTime->getMonth(), 
				$solarDateTime->getDate()) + $solarDateTime->getHours() / 24) < 
				节令::term($solarDateTime->getYear(), 3, true) - 1) {// 判断是否过立春
			$g -= 1;
		}
		return 干支::lookup($g + 1);
	}

	public static function 年柱($solarDateTime, $location = null) {
		$local = self::_年柱(self::getLocalAbsoluteDate($solarDateTime, $location));
		$standard = self::_年柱(self::getStandardDate($solarDateTime, $location));
		return 干支::lookup2($standard->天干(), $local->地支());
	}

	/**
	 * 获取月柱。
	 * 
	 * @param $solarDateTime
	 * @return
	 */
	private static function _月柱($solarDateTime) {
		$v = (($solarDateTime->getYear() - 1900) * 12 + $solarDateTime->getMonth() + 12) % 60;
		$qs = 节令::节气($solarDateTime);
		if ($solarDateTime->getDate() <= $qs[0]->getSolarTermDate()->getDate())
			$v -= 1;
		return 干支::lookup($v + 1);
	}

	public static function 月柱($solarDateTime, $location = null) {
		$local = self::_月柱(self::getLocalAbsoluteDate($solarDateTime, $location));
		$standard = self::_月柱(self::getStandardDate($solarDateTime, $location));
		return 干支::lookup2($standard->天干(), $local->地支());
	}

	// / <summary>
	// / 获取日柱。
	// / </summary>
	/**
	 * 获取日柱。
	 * 
	 * @param $solarDateTime
	 * @return
	 */
	private static function _日柱($solarDateTime) {
		$gzD = ($solarDateTime->getHours() < 23) ? 
				LunarUtils::equivalentStandardDay($solarDateTime->getYear(), $solarDateTime->getMonth(), $solarDateTime->getDate()) : 
				LunarUtils::equivalentStandardDay($solarDateTime->getYear(), $solarDateTime->getMonth(), $solarDateTime->getDate()) + 1;
		return 干支::lookup(round(LunarUtils::rem($gzD + 15, 60)));
	}

	public static function 日柱($solarDateTime, $location = null) {
		$local = self::_日柱(self::getLocalAbsoluteDate($solarDateTime, $location));
		$standard = self::_日柱(self::getStandardDate($solarDateTime, $location));
		return 干支::lookup2($standard->天干(), $local->地支());
	}

	/**
	 * 获取时柱。
	 * 
	 * @param $solarDateTime
	 * @return
	 */
	private static function _时柱($solarDateTime) {
		$v = 12 * self::_日柱($solarDateTime)->天干()->术数()->getIndex() + floor(($solarDateTime->getHours() + 1) / 2) - 11;
		if ($solarDateTime->getHours() == 23)
			$v -= 12;
		return 干支::lookup(round(LunarUtils::rem($v, 60)));
	}

	public static function 时柱($solarDateTime, $location = null) {
		$local = self::_时柱(self::getLocalAbsoluteDate($solarDateTime, $location));
		$standard = self::_时柱(self::getStandardDate($solarDateTime, $location));
		return 干支::lookup2($standard->天干(), $local->地支());
	}
}
