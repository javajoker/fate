<?php
namespace DchLib;

class 脏腑 {
	/**
	 * 天干
	 */
	public static $胆, $肝, $小肠, $心, $胃, $脾, $大肠, $肺, $膀胱, $肾;

	/**
	 * 地支
	 */
	public static $心包, $三焦;

	public static function __init() {
		self::$胆 = new 脏腑('胆');
		self::$肝 = new 脏腑('肝');
		self::$小肠 = new 脏腑('小肠');
		self::$心 = new 脏腑('心');
		self::$胃 = new 脏腑('胃');
		self::$脾 = new 脏腑('脾');
		self::$大肠 = new 脏腑('大肠');
		self::$肺 = new 脏腑('肺');
		self::$膀胱 = new 脏腑('膀胱');
		self::$肾 = new 脏腑('肾');
		
		self::$心包 = new 脏腑('心包');
		self::$三焦 = new 脏腑('三焦');
	}
	
	private $m_key;
	
	private function __construct($key) {
		$this->m_key = $key;
	}
	
	public function toString() {
		return $this->m_key;
	}
}

脏腑::__init();
