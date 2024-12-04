<?php
namespace DchLib;

class 方位 {
	public static $东, $南, $西, $北, $中;
	public static function __init() {
		self::$东 = new 方位('东');
		self::$南 = new 方位('南');
		self::$西 = new 方位('西');
		self::$北 = new 方位('北');
		self::$中 = new 方位('中');
	}
	
	private $m_key;
	
	private function __construct($key) {
		$this->m_key = $key;
	}
	
	public function toString() {
		return $this->m_key;
	}
}

方位::__init();
