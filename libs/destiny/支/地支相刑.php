<?php
namespace DchLib;

class 地支相刑 {
	public static $无礼刑, $恃势刑, $无恩刑, $自刑;
	
	public static function __init() {
		self::$无礼刑 = new 地支相刑('缺乏独立自主，行事有始无终，固执已见，常陷困境，且容貌鄙劣，内心险毒。与死绝同柱者。思虑浅薄，重者致疾。生日有此刑，夫妻有疾；生时有此刑，子病弱。四柱有二组自刑者，其凶兆更甚；四柱命佳，反有贵之诱力。');
		self::$恃势刑 = new 地支相刑('恃自己之势，过于猛进，易遭挫折失败。与十二宫中长生、沐浴、冠带、临官、帝旺同柱：精神刚毅。与死、绝同柱：卑屈或多狡猾，常罹疾招灾。女命则孤独。');
		self::$无恩刑 = new 地支相刑('性情冷酷薄义，或遭人陷害及凶事发生。若再坐十二宫死绝者，更甚。女命遇此刑易损孕。');
		self::$自刑 = new 地支相刑();
	}

	private $m_message;

	private function __construct($message = '') {
		$this->m_message = $message;
	}
	
	public function getMessage() {
		return $this->m_message;
	}

	public static function 刑($x, $y) {
		if ((地支::$子 === $x && 地支::$卯 === $y)
				|| (地支::$子 === $y && 地支::$卯 === $x))
			return self::$无礼刑;

		if ((地支::$寅 === $x && 地支::$巳 === $y)
				|| (地支::$寅 === $y && 地支::$巳 === $x))
			return self::$恃势刑;
		if ((地支::$巳 === $x && 地支::$申 === $y)
				|| (地支::$巳 === $y && 地支::$申 === $x))
			return self::$恃势刑;
		if ((地支::$申 === $x && 地支::$寅 === $y)
				|| (地支::$申 === $y && 地支::$寅 === $x))
			return self::$恃势刑;

		if ((地支::$丑 === $x && 地支::$戌 === $y)
				|| (地支::$丑 === $y && 地支::$戌 === $x))
			return self::$无恩刑;
		if ((地支::$戌 === $x && 地支::$未 === $y)
				|| (地支::$戌 === $y && 地支::$未 === $x))
			return self::$无恩刑;
		if ((地支::$未 === $x && 地支::$丑 === $y)
				|| (地支::$未 === $y && 地支::$丑 === $x))
			return self::$无恩刑;

		if (地支::$辰 === $x && 地支::$辰 === $y)
			return self::$自刑;
		if (地支::$午 === $x && 地支::$午 === $y)
			return self::$自刑;
		if (地支::$酉 === $x && 地支::$酉 === $y)
			return self::$自刑;
		if (地支::$亥 === $x && 地支::$亥 === $y)
			return self::$自刑;

		return null;
	}
}
地支相刑::__init();