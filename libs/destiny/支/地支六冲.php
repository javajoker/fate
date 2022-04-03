<?php

class 地支六冲 {
	public static $子午冲, $丑未冲, $寅申冲, $卯酉冲, $辰戌冲, $巳亥冲;
	
	public static function __init() {
		self::$子午冲 = new 地支六冲('一身不安', '居住地变迁，职业不变');
		self::$丑未冲 = new 地支六冲('事多阻逆', '居住地不变，职业变动');
		self::$寅申冲 = new 地支六冲('多情且好管闲事', '居住地和职业均改变');
		self::$卯酉冲 = new 地支六冲('背约失信，忧郁多劳，色情纠纷', '居住地变迁，职业不变');
		self::$辰戌冲 = new 地支六冲('克亲伤子寿短', '居住地不变，职业变动');
		self::$巳亥冲 = new 地支六冲('多事、喜助人', '居住地和职业均改变');
	}

	/*
	 * 年与月支冲：离祖别乡；
	 * 
	 * 年与日支冲：与亲不和；
	 * 
	 * 年与时支冲：与子不和；
	 * 
	 * 年与日月时支冲：性暴躁或易患疾；
	 * 
	 * 日冲月支：犯父母兄弟；
	 * 
	 * 四柱逢冲：多不居父母家；
	 */

	private $m_message;
	private $m_realtimeMessage;

	private function __construct($message, $realtimeMessage) {
		$this->m_message = $message;
		$this->m_realtimeMessage = $realtimeMessage;
	}
	
	public function getMessage() {
		return $this->m_message;
	}

	public function getRealtimeMessage() {
		return $this->m_realtimeMessage;
	}

	public static function 冲($x, $y) {
		if ((地支::$子 === $x && 地支::$午 === $y)
				|| (地支::$子 === $y && 地支::$午 === $x))
			return self::$子午冲;
		if ((地支::$丑 === $x && 地支::$未 === $y)
				|| (地支::$丑 === $y && 地支::$未 === $x))
			return self::$丑未冲;
		if ((地支::$寅 === $x && 地支::$申 === $y)
				|| (地支::$寅 === $y && 地支::$申 === $x))
			return self::$寅申冲;
		if ((地支::$卯 === $x && 地支::$酉 === $y)
				|| (地支::$卯 === $y && 地支::$酉 === $x))
			return self::$卯酉冲;
		if ((地支::$辰 === $x && 地支::$戌 === $y)
				|| (地支::$辰 === $y && 地支::$戌 === $x))
			return self::$辰戌冲;
		if ((地支::$巳 === $x && 地支::$亥 === $y)
				|| (地支::$巳 === $y && 地支::$亥 === $x))
			return self::$巳亥冲;

		return null;
	}
}

地支六冲::__init();