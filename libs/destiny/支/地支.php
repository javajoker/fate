<?php
namespace DchLib;

class 地支 {
	public static $子, $丑, $寅, $卯, $辰, $巳, $午, $未, $申, $酉, $戌, $亥;
	private static $values = array();

	/**
	 * 返回甲子数x对应的地支
	 * 
	 * @param $x
	 * @return
	 */
	public static function lookup($x) {
		$x = $x % 12;
		return self::$values[($x < 0 ? $x + 12 : $x)];
	}

	public static function __init() {
		self::$子 = new 地支(1, '子', array( 天干::$癸 ), 人体::$耳, array( 脏腑::$膀胱, 脏腑::$三焦 ));
		self::$丑 = new 地支(2, '丑', array( 天干::$己, 天干::$癸, 天干::$辛 ), 人体::$胞肚, array( 脏腑::$脾 ));
		self::$寅 = new 地支(3, '寅', array( 天干::$甲, 天干::$丙, 天干::$戊 ), 人体::$手, array( 脏腑::$胆 ));
		self::$卯 = new 地支(4, '卯', array( 天干::$乙 ), 人体::$指, array( 脏腑::$肝 ));
		self::$辰 = new 地支(5, '辰', array( 天干::$戊, 天干::$乙, 天干::$癸 ), 人体::$肩胸, array( 脏腑::$胃 ));
		self::$巳 = new 地支(6, '巳', array( 天干::$丙, 天干::$戊, 天干::$庚 ), 人体::$面咽齿, array( 脏腑::$心 ));
		self::$午 = new 地支(7, '午', array( 天干::$丁, 天干::$己 ), 人体::$眼, array( 脏腑::$小肠 ));
		self::$未 = new 地支(8, '未', array( 天干::$己, 天干::$丁, 天干::$乙 ), 人体::$脊梁, array( 脏腑::$脾 ));
		self::$申 = new 地支(9, '申', array( 天干::$庚, 天干::$壬, 天干::$戊 ), 人体::$经络, array( 脏腑::$大肠 ));
		self::$酉 = new 地支(10, '酉', array( 天干::$辛 ), 人体::$精血, array( 脏腑::$肺 ));
		self::$戌 = new 地支(11, '戌', array( 天干::$戊, 天干::$辛, 天干::$丁 ), 人体::$命门腿足, array( 脏腑::$胃 ));
		self::$亥 = new 地支(12, '亥', array( 天干::$壬, 天干::$甲 ), 人体::$头, array( 脏腑::$肾, 脏腑::$心包 ));

		self::$values = array(
			self::$亥, self::$子, self::$丑, self::$寅, self::$卯, self::$辰, self::$巳, self::$午, self::$未, self::$申, self::$酉, self::$戌
		);
	}
	
	private $m_shushu;
	private $支藏;

	private $m_key;

	private $m_position;
	private $m_body;
	private $m_organs;

	private function __construct($value, $key, $支藏, $body, $organs) {
		$value = $value % 12;

		switch ($value / 3) {
		case 0:
			$element = 五行::$水;
			$position = 方位::$北;
			break;
		case 1:
			$element = 五行::$木;
			$position = 方位::$东;
			break;
		case 2:
			$element = 五行::$火;
			$position = 方位::$南;
			break;
		case 3:
			$element = 五行::$金;
			$position = 方位::$西;
			break;
		}
		if ($value % 3 == 2)
			$element = 五行::$土;

		$this->m_key = $key;
		$this->m_position = $position;
		$this->支藏 = $支藏;
		$this->m_body = $body;
		$this->m_organs = $organs;

		$this->m_shushu = new 术数($element, null, $value);
	}

	public function 术数() {
		return $this->m_shushu;
	}

	public function 方位() {
		return $this->m_position;
	}

	public function 人体() {
		return $this->m_body;
	}

	public function 脏腑() {
		return $this->m_organs;
	}

	public function 支藏() {
		return $this->支藏;
	}

	public function toString() {
		return $this->m_key;
	}

	public function 六合($x) {
		if ((self::$子 === $x && self::$丑 === $this) || (self::$子 === $this && self::$丑 === $x))
			return 五行::$土;
		if ((self::$寅 === $x && self::$亥 === $this) || (self::$寅 === $this && self::$亥 === $x))
			return 五行::$木;
		if ((self::$卯 === $x && self::$戌 === $this) || (self::$卯 === $this && self::$戌 === $x))
			return 五行::$火;
		if ((self::$辰 === $x && self::$酉 === $this) || (self::$辰 === $this && self::$酉 === $x))
			return 五行::$金;
		if ((self::$巳 === $x && self::$申 === $this) || (self::$巳 === $this && self::$申 === $x))
			return 五行::$水;
		if ((self::$午 === $x && self::$未 === $this) || (self::$午 === $this && self::$未 === $x))
			return 五行::$土;

		return null;
	}

	/**
	 * 会一方之气。
	 * 
	 * @param $x
	 * @param $y
	 * @param $z
	 * @return
	 */
	public static function 三会($x, $y, $z) {
		if (self::$亥 === $x || self::$亥 === $y || self::$亥 === $z) {
			if (self::$子 === $x || self::$子 === $y || self::$子 === $z) {
				if (self::$丑 === $x || self::$丑 === $y || self::$丑 === $z)
					return 五行::$水;
			}
		} else if (self::$寅 === $x || self::$寅 === $y || self::$寅 === $z) {
			if (self::$卯 === $x || self::$卯 === $y || self::$卯 === $z) {
				if (self::$辰 === $x || self::$辰 === $y || self::$辰 === $z)
					return 五行::$木;
			}
		} else if (self::$巳 === $x || self::$巳 === $y || self::$巳 === $z) {
			if (self::$午 === $x || self::$午 === $y || self::$午 === $z) {
				if (self::$未 === $x || self::$未 === $y || self::$未 === $z)
					return 五行::$火;
			}
		} else if (self::$申 === $x || self::$申 === $y || self::$申 === $z) {
			if (self::$酉 === $x || self::$酉 === $y || self::$酉 === $z) {
				if (self::$戌 === $x || self::$戌 === $y || self::$戌 === $z)
					return 五行::$金;
			}
		}

		return null;
	}

	/**
	 * 生、旺、墓成局。三合局力量次于三会局。
	 * 
	 * 例，水生于申，旺于子，墓于辰，故申子辰合水局
	 * 
	 * 丑辰未戌全，方是土局
	 * 
	 * @param $x
	 * @param $y
	 * @param $z
	 * @return
	 */
	public static function 三合($x, $y, $z) {
		if (self::$申 === $x || self::$申 === $y || self::$申 === $z) {
			if (self::$子 === $x || self::$子 === $y || self::$子 === $z) {
				if (self::$辰 === $x || self::$辰 === $y || self::$辰 === $z)
					return 五行::$水;
			}
		} else if (self::$巳 === $x || self::$巳 === $y || self::$巳 === $z) {
			if (self::$酉 === $x || self::$酉 === $y || self::$酉 === $z) {
				if (self::$丑 === $x || self::$丑 === $y || self::$丑 === $z)
					return 五行::$金;
			}
		} else if (self::$寅 === $x || self::$寅 === $y || self::$寅 === $z) {
			if (self::$午 === $x || self::$午 === $y || self::$午 === $z) {
				if (self::$戌 === $x || self::$戌 === $y || self::$戌 === $z)
					return 五行::$火;
			}
		} else if (self::$亥 === $x || self::$亥 === $y || self::$亥 === $z) {
			if (self::$卯 === $x || self::$卯 === $y || self::$卯 === $z) {
				if (self::$未 === $x || self::$未 === $y || self::$未 === $z)
					return 五行::$木;
			}
		}

		return null;
	}

	/**
	 * 三合半合局，力量次于三合局
	 * 
	 * @param $x
	 * @param $y
	 * @return
	 */
	public static function 生地半合($x, $y) {
		if ((self::$卯 === $x && self::$亥 === $y) || (self::$卯 === $y && self::$亥 === $x))
			return 五行::$木;
		if ((self::$巳 === $x && self::$酉 === $y) || (self::$巳 === $y && self::$酉 === $x))
			return 五行::$金;
		if ((self::$子 === $x && self::$申 === $y) || (self::$子 === $y && self::$申 === $x))
			return 五行::$水;
		if ((self::$午 === $x && self::$寅 === $y) || (self::$午 === $y && self::$寅 === $x))
			return 五行::$火;
		return null;
	}

	/**
	 * 三合半合局，力量次于生地半合
	 * 
	 * @param $x
	 * @param $y
	 * @return
	 */
	public static function 墓地半合($x, $y) {
		if ((self::$未 === $x && self::$卯 === $y) || (self::$未 === $y && self::$卯 === $x))
			return 五行::$木;
		if ((self::$丑 === $x && self::$酉 === $y) || (self::$丑 === $y && self::$酉 === $x))
			return 五行::$金;
		if ((self::$子 === $x && self::$辰 === $y) || (self::$子 === $y && self::$辰 === $x))
			return 五行::$水;
		if ((self::$午 === $x && self::$戌 === $y) || (self::$午 === $y && self::$戌 === $x))
			return 五行::$火;
		return null;
	}

	/**
	 * 三合半合局，力量次于生地半合
	 * 
	 * @param $x
	 * @param $y
	 * @return
	 */
	public static function 非旺半合($x, $y) {
		if ((self::$未 === $x && self::$亥 === $y) || (self::$未 === $y && self::$亥 === $x))
			return 五行::$木;
		if ((self::$丑 === $x && self::$巳 === $y) || (self::$丑 === $y && self::$巳 === $x))
			return 五行::$金;
		if ((self::$申 === $x && self::$辰 === $y) || (self::$申 === $y && self::$辰 === $x))
			return 五行::$水;
		if ((self::$寅 === $x && self::$戌 === $y) || (self::$寅 === $y && self::$戌 === $x))
			return 五行::$火;

		return null;
	}
}

地支::__init();