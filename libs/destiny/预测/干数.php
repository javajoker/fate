<?php

define('生主泄气', 1.0 / 4);
define('生客得气', 2.0 / 4);
define('克主耗气', 2.0 / 4);
define('克客失气', 3.0 / 4);

define('异性衰减', 3.0 / 4);

class 干数 extends 数 {
	/**************************** 生克 ************************************************/

	/**
	 * 同性之生，其生力大于异性；生者减气，受生者得益。
	 * 
	 * 同性之克，其克力大于异性；两者均受损伤、被克者损伤大；
	 */
	public static function 生克($i, $j, $柱距 = 0) {
		$weights = self::生克2(array( $i->getWeight(), $j->getWeight() ),
				array( $i->术数(), $j->术数() ), $柱距);
		$i->setWeight($weights[0]);
		$j->setWeight($weights[1]);
	}

	public static function 生克2($weights, $eles, $柱距 = 0) {
		if ($eles[0] === $eles[1])
			return $weights;

		$a = $weights[0];
		$b = $weights[1];
		$x = $eles[0];
		$y = $eles[1];

		if ($x->五行()->生() === $y->五行() || $y->五行()->生() === $x->五行()) {
			// 反生为克，是指五行相生双方，主生者旺，被生者衰的现象。
			// 泄多为克，是指五行相生双方，被生者旺，主生者弱的现象。
			$c = $d = $a > $b ? $b : $a;
			$c *= 生主泄气;
			$d *= 生客得气;
		} else if ($x->五行()->克() === $y->五行() || $y->五行()->克() === $x->五行()) {
			// 五行反克是指相克双方为主克者弱，被克者强的一种特殊现象。
			$c = $d = $a > $b ? $b : $a;
			$c *= 克主耗气;
			$d *= 克客失气;
		} else
			return $weights;

		if ($x->阴阳() !== $y->阴阳()) {
			$c *= 异性衰减;
			$d *= 异性衰减;
		}

		--$柱距;
		$衰减 = 1 / pow(2, ($柱距 <= 0) ? 0 : $柱距);
		$c *= $衰减;
		$d *= $衰减;

		if ($x->五行()->生() === $y->五行()) {
			$a -= $c;
			$b += $d;
		} else if ($y->五行()->生() === $x->五行()) {
			$b -= $c;
			$a += $d;
		} else if ($x->五行()->克() === $y->五行()) {
			$a -= $c;
			$b -= $d;
		} else if ($y->五行()->克() === $x->五行()) {
			$b -= $c;
			$a -= $d;
		}

		if ($a < 0)
			$a = 0;
		if ($b < 0)
			$b = 0;

		return array( $a, $b );
	}

	private $天干, $化数, $神;
	private $m_weight;

	public function __construct($天干, $weight = 基础权重) {
		parent::__construct($天干->术数());

		$this->天干 = $天干;
		$this->化数 = null;
		$this->神 = 十神::$NA;
		$this->m_weight = $weight;
	}

	public function 天干() {
		return $this->天干;
	}

	public function 化($ele) {
		if ($ele === $this->术数->五行())
			return;
		$this->化数 = new 术数($ele, $this->术数->阴阳());
	}

	public function 神($神 = null) {
		// get
		if($神 === null) return ($this->神 == null ? 十神::$NA : $this->神);
		// set
		$this->神 = $神;
	}

	#@Override
	public function 术数() {
		return ($this->化数 == null) ? $this->术数 : $this->化数;
	}

	#@Override
	public function getWeight() {
		return $this->m_weight;
	}

	#@Override
	function setWeight($weight) {
		$this->m_weight = $weight;
	}

	#@Override
	public function __DEBUG() {
		return sprintf('%s(%s)	: %s(%$d)', $this->天干->toString(), 
			$this->天干->术数()->五行()->toString(), $this->术数->五行()->toString(),
			$this->getWeight());
	}
}
