<?php

class 地支相害 {
	public static $子未害, $丑午害, $寅巳害, $卯辰害, $申亥害, $酉戌害;
	
	public static function __init() {
		self::$子未害 = new 地支相害('不能利润骨肉');
		self::$丑午害 = new 地支相害('逢旺易怒，缺乏忍耐力，坐十二宫弱地，恐有残伤');
		self::$寅巳害 = new 地支相害('重金者，疾病缠身');
		self::$卯辰害 = new 地支相害('逢旺易怒，缺乏忍耐力，坐十二宫弱地，恐有残伤');
		self::$申亥害 = new 地支相害();
		self::$酉戌害 = new 地支相害('重聋哑或头面多恶疮');
	}

	/*
	 * 月支害：孤独薄命。女命更甚；日时害：老年残疾。
	 */
	private $m_message;

	private function __construct($message = '') {
		$this->m_message = $message;
	}
	
	public function getMessage() {
		return message;
	}

	public static function 害(地支 $x, 地支 $y) {
		if ((地支::$子 === $x && 地支::$未 === $y)
				|| (地支::$子 === $y && 地支::$未 === $x))
			return self::$子未害;
		if ((地支::$丑 === $x && 地支::$午 === $y)
				|| (地支::$丑 === $y && 地支::$午 === $x))
			return self::$丑午害;
		if ((地支::$寅 === $x && 地支::$巳 === $y)
				|| (地支::$寅 === $y && 地支::$巳 === $x))
			return self::$寅巳害;
		if ((地支::$卯 === $x && 地支::$辰 === $y)
				|| (地支::$卯 === $y && 地支::$辰 === $x))
			return self::$卯辰害;
		if ((地支::$申 === $x && 地支::$亥 === $y)
				|| (地支::$申 === $y && 地支::$亥 === $x))
			return self::$申亥害;
		if ((地支::$酉 === $x && 地支::$戌 === $y)
				|| (地支::$酉 === $y && 地支::$戌 === $x))
			return self::$酉戌害;

		return null;
	}
}
地支相害::__init();