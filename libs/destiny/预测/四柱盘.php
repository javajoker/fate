<?php

define('月提权重', 基础权重 * 3); // 得令故

class 四柱盘 extends 盘 {

	public function __construct($solarDateTime, $乾造, $location) {
		parent::__construct($solarDateTime, $乾造, $location);

		$柱 = array( 
				四柱::年柱($solarDateTime, $location),
				四柱::月柱($solarDateTime, $location), 
				四柱::日柱($solarDateTime, $location),
				四柱::时柱($solarDateTime, $location)
		);
		
		global $__VERBOSE;
		if ($__VERBOSE) {
			printf('== 四柱 ==' . "\n");
			printf('	年 : %s; 月 : %s; 日 : %s; 时 : %s' . "\n", 
					$柱[年序]->toString(), $柱[月序]->toString(), $柱[日序]->toString(), $柱[时序]->toString());
		}

		$this->initialize($柱);
	}

	#@Override
	protected function 配权() {
		$this->柱数[月序]->支数()->setWeight(月提权重);
	}

	#@Override
	protected function 柱递进序() {
		return array(
				array( 年序, 月序 ), 
				array( 年序, 日序 ), 
				array( 年序, 时序 ), 
				array( 月序, 日序 ), 
				array( 月序, 时序 ), 
				array( 日序, 时序 ), 
			);
	}

	#@Override
	protected function getDistance($x, $y) {
		return abs($x - $y);
	}

	public function 五行局面() {
		$eleWeights = array();
		for ($i = 0; $i < count($this->柱数); ++$i) {
			$eleWeights[$this->柱数[$i]->干数()->术数()->五行()->ordinal()] += $this->柱数[$i]->干数()->getWeight();
			foreach ($this->柱数[$i]->支数()->藏干数() as $gan) {
				$eleWeights[$gan->术数()->五行()->ordinal()] += $gan->getWeight();
			}
		}

		return $eleWeights;
	}

	public function 心性() {
		$weights = array();

		for ($i = 0; $i < count($this->柱数); ++$i) {
			foreach ($this->柱数[$i]->支数()->藏干数() as $gan) {
				$weights[$gan->神()->ordinal()] += $gan->getWeight();
			}
			if ($i == 日序)
				continue;
			$weights[$this->柱数[$i]->干数()->神()->ordinal()] += $this->柱数[$i]->干数()->getWeight();
		}

		$maxWeight = 0;
		$idx = 0;
		for ($i = 0; $i < count($weights); ++$i) {
			if ($weights[$i] > $maxWeight) {
				$maxWeight = $weights[$i];
				$idx = $i;
			}
		}

		$ss = 十神::values();
		return $ss[$idx];
	}
}
