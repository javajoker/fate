<?php
namespace DchLib;

class 五行生克关系 {
	/**
	 * 生我者
	 */
	public static $印绶 = null;
	/**
	 * 我生者
	 */
	public static $食神 = null;
	/**
	 * 克我者
	 */
	public static $官杀 = null;
	/**
	 * 我克者
	 */
	public static $妻财 = null;
	/**
	 * 比肩者
	 */
	public static $比肩 = null;
	
	public static function __init() {
		self::$印绶 = new 五行生克关系('印绶', '父母');
		self::$食神 = new 五行生克关系('食神', '子孙');
		self::$官杀 = new 五行生克关系('官杀', '官鬼');
		self::$妻财 = new 五行生克关系('妻财', '妻财');
		self::$比肩 = new 五行生克关系('比肩', '兄弟');
	}

	private $m_key;
	private $m_stands;

	private function __construct($key, $stands) {
		$this->m_key = $key;
		$this->m_stands = $stands;
	}
	
	public function toString() {
		return $this->m_key;
	}

	public function 别名() {
		return $this->m_stands;
	}
}

五行生克关系::__init();