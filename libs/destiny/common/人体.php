<?php

class 人体 {
	/**
	 * 天干
	 */
	public static $头, $肩, $额, $齿舌, $鼻面, $筋, $胸, $胫, $足;

	/**
	 * 地支
	 */
	public static $耳, $胞肚, $手, $指, $肩胸, $面咽齿, $眼, $脊梁, $经络, $精血, $命门腿足 /* 头 */;

	public static function __init() {
		self::$头 = new 人体('头');
		self::$肩 = new 人体('肩');
		self::$额 = new 人体('额');
		self::$齿舌 = new 人体('齿舌');
		self::$鼻面 = new 人体('鼻面');
		self::$筋 = new 人体('筋');
		self::$胸 = new 人体('胸');
		self::$胫 = new 人体('胫');
		self::$足 = new 人体('足');
		
		self::$耳 = new 人体('耳');
		self::$胞肚 = new 人体('胞肚');
		self::$手 = new 人体('手');
		self::$指 = new 人体('指');
		self::$肩胸 = new 人体('肩胸');
		self::$面咽齿 = new 人体('面咽齿');
		self::$眼 = new 人体('眼');
		self::$脊梁 = new 人体('脊梁');
		self::$经络 = new 人体('经络');
		self::$精血 = new 人体('精血');
		self::$命门腿足 = new 人体('命门腿足');
	}
	
	private $m_key;
	
	private function __construct($key) {
		$this->m_key = $key;
	}
	
	public function toString() {
		return $this->m_key;
	}
}

人体::__init();
