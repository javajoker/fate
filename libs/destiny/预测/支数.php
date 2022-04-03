<?php

class 支数 extends 数 {
	public static $藏干权重 = array();
	public static function __init() {
		self::$藏干权重 = array(
			array( 1.0 ),
			array( 0.7, 0.3 ),
			array( 0.65, 0.25, 0.1 )
		);
	// array( 0.66, 0.24, 0.1 );
	}

	private $地支, $藏干数;

	public function __construct($地支, $weight = 基础权重) {
		parent::__construct($地支->术数());

		$this->地支 = $地支;
		$this->setWeight($weight);
	}

	public function 地支() {
		return $this->地支;
	}

	public function 藏干数() {
		return $this->藏干数;
	}

	#@Override
	function setWeight($weight) {
		$size = count( $this->地支->支藏() );
		$this->藏干数 = array();

		$idx = 0;
		foreach( $this->地支->支藏() as $干 ) {
			$this->藏干数[] = new 干数($干, ($weight * self::$藏干权重[$size - 1][$idx]));
			++ $idx;
		}

		// 支藏亦论生克
		for ($i = count($this->藏干数) - 1; $i > 0; --$i) {
			for ($j = $i - 1; $j >= 0; --$j)
				干数::生克($this->藏干数[$i], $this->藏干数[$j]);
		}

		$sum = 0;
		for ($i = 0; $i < count($this->藏干数); ++$i) {
			$sum += $this->藏干数[$i]->getWeight();
		}
		for ($i = 0; $i < count($this->藏干数); ++$i) {
			$this->藏干数[$i]->setWeight($this->藏干数[$i]->getWeight() * $weight / $sum);
		}
	}

	#@Override
	public function getWeight() {
		$weight = 0;
		foreach($this->藏干数 as $gan) {
			$weight += $gan->getWeight();
		}
		return $weight;
	}

	#@Override
	public function __DEBUG() {
		$s = sprintf('%s(%s)	藏干', $this->地支, $this->地支->术数()->五行());
		foreach($this->藏干数 as $x)
			$s .= sprintf('	: %s(%d)', $x->术数()->五行(), $x->getWeight());
		return $s;
	}
}

支数::__init();