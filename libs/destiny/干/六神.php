<?php

class 六神 {
	public static $青龙, $朱雀, $勾陈, $螣蛇, $白虎, $玄武;
	
	public static function __init() {
		self::$青龙 = new 六神('青龙', '喜庆');
		self::$朱雀 = new 六神('朱雀', '口舌官非');
		self::$勾陈 = new 六神('勾陈', '牢狱');
		self::$螣蛇 = new 六神('螣蛇', '虚惊');
		self::$白虎 = new 六神('白虎', '血光丧服');
		self::$玄武 = new 六神('玄武', '匪盗暗昧');
	}

	private $m_key;
	private $主;

	private function __construct($key, $主) {
		$this->m_key = $key;
		$this->主 = $主;
	}

	public function toString() {
		return $this->m_key;
	}
	
	public function 主() {
		return $this->主;
	}
}

六神::__init();