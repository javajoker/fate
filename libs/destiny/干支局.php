<?php

class 干支局 {
	public static $天干五合;
	public static $三会局, $三合局, $生地半合, $墓地半合, $非旺半合, $地支六合;
	public static $冲, $刑, $害;
	
	public static function __init() {
		self::$天干五合 = new 干支局( true, true );
		
		self::$三会局 = new 干支局();
		self::$三合局 = new 干支局();
		self::$生地半合 = new 干支局();
		self::$墓地半合 = new 干支局();
		self::$非旺半合 = new 干支局();
		self::$地支六合 = new 干支局( false, true );
		
		self::$冲 = new 干支局();
		self::$刑 = new 干支局();
		self::$害 = new 干支局();
	}

	private $干局, $合化局;
	
	private function __construct($干局 = false, $合化局 = false) {
		$this->干局 = $干局;
		$this->合化局 = $合化局;
	}
	
	public function 干局() {
		return $this->干局;
	}

	public function 合化局() {
		return $this->合化局;
	}
}
干支局::__init();