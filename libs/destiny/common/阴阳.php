<?php
namespace DchLib;

class 阴阳 {
	public static $阴, $阳;

	public static function __init() {
		self::$阴 = new 阴阳(0, '阴');
		self::$阳 = new 阴阳(1, '阳');
	}
	
	private $m_key;
	private $m_ordinal;
	
	private function __construct($ordinal, $key) {
		$this->m_ordinal = $ordinal;
		$this->m_key = $key;
	}
	
	public function ordinal() {
		return $this->m_ordinal;
	}

	public function toString() {
		return $this->m_key;
	}

	public function opposite() {
		if (self::$阴 == $this)
			return self::$阳;
		else
			return self::$阴;
	}
}

阴阳::__init();
