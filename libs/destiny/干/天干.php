<?php
namespace DchLib;

class 天干 {
	public static $甲, $乙, $丙, $丁, $戊, $己, $庚, $辛, $壬, $癸;
	private static $values = array();

	/**
	 * 返回甲子数$x对应的天干
	 * 
	 * @param $x
	 * @return
	 */
	public static function lookup($x) {
		$x = $x % 10;
		return self::$values[($x < 0 ? $x + 10 : $x)];
	}

	public static function lookup2($ele, $yinYang) {
		foreach (self::$values as $干) {
			if ($ele === $干->术数()->五行() && $yinYang === $干->术数()->阴阳())
				return $干;
		}
		return null;
	}
	
	public static function __init() {
		self::$甲 = new 天干(1, '甲', 人体::$头, 脏腑::$胆, 六神::$青龙);
		self::$乙 = new 天干(2, '乙', 人体::$肩, 脏腑::$肝, 六神::$青龙);
		self::$丙 = new 天干(3, '丙', 人体::$额, 脏腑::$小肠, 六神::$朱雀);
		self::$丁 = new 天干(4, '丁', 人体::$齿舌, 脏腑::$心, 六神::$朱雀);
		self::$戊 = new 天干(5, '戊', 人体::$鼻面, 脏腑::$胃, 六神::$勾陈);
		self::$己 = new 天干(6, '己', 人体::$鼻面, 脏腑::$脾, 六神::$螣蛇);
		self::$庚 = new 天干(7, '庚', 人体::$筋, 脏腑::$大肠, 六神::$白虎);
		self::$辛 = new 天干(8, '辛', 人体::$胸, 脏腑::$肺, 六神::$白虎);
		self::$壬 = new 天干(9, '壬', 人体::$胫, 脏腑::$膀胱, 六神::$玄武);
		self::$癸 = new 天干(10, '癸', 人体::$足, 脏腑::$肾, 六神::$玄武);
		
		self::$values = array(
			self::$癸, self::$甲, self::$乙, self::$丙, self::$丁, self::$戊, self::$己, self::$庚, self::$辛, self::$壬
		);
	}

	private $m_shushu;
	private $m_key;
	private $m_position;
	private $m_body;
	private $m_organ;
	private $m_god;

	private function __construct($value, $key, $body, $organ, $god) {
		switch (($value - 1) >> 1) {
		case 0:
			$element = 五行::$木;
			$position = 方位::$东;
			break;
		case 1:
			$element = 五行::$火;
			$position = 方位::$南;
			break;
		case 2:
			$element = 五行::$土;
			$position = 方位::$中;
			break;
		case 3:
			$element = 五行::$金;
			$position = 方位::$西;
			break;
		case 4:
			$element = 五行::$水;
			$position = 方位::$北;
			break;
		}

		$this->m_key = $key;
		$this->m_position = $position;
		$this->m_body = $body;
		$this->m_organ = $organ;
		$this->m_god = $god;

		$value = $value % 10;
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
		return $this->m_organ;
	}

	public function 六神() {
		return $this->m_god;
	}

	public function toString() {
		return $this->m_key;
	}

	public function 五合($x) {
		if ((天干::$甲 === $x && 天干::$己 === $this)
				|| (天干::$甲 === $this && 天干::$己 === $x))
			return 五行::$土;
		if ((天干::$乙 === $x && 天干::$庚 === $this)
				|| (天干::$乙 === $this && 天干::$庚 === $x))
			return 五行::$金;
		if ((天干::$丙 === $x && 天干::$辛 === $this)
				|| (天干::$丙 === $this && 天干::$辛 === $x))
			return 五行::$水;
		if ((天干::$丁 === $x && 天干::$壬 === $this)
				|| (天干::$丁 === $this && 天干::$壬 === $x))
			return 五行::$木;
		if ((天干::$戊 === $x && 天干::$癸 === $this)
				|| (天干::$戊 === $this && 天干::$癸 === $x))
			return 五行::$火;

		return null;
	}
}

天干::__init();
