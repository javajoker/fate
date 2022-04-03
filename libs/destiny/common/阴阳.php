<?php

class 阴阳 {
	public static $阴, $阳;

	public static function __init() {
		self::$阴 = new 阴阳('阴');
		self::$阳 = new 阴阳('阳');
	}
	
	private $m_key;
	
	private function __construct($key) {
		$this->m_key = $key;
	}
	
	public function toString() {
		return $this->m_key;
	}
}

阴阳::__init();
