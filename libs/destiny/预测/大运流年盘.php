<?php

define('大运序', 时序 + 1);
define('流年序', 大运序 + 1);

define('太岁权重', 140.0);
define('大运流年干支权和', 220.0);
define('大运生发', 20.0);

class 大运流年盘 extends 盘 {

	private $大运经年;

	public function __construct($solarDateTime, $乾造, $location, $大运, $流年, $大运经年) {
		parent::__construct($solarDateTime, $乾造, $location);

		$this->大运经年 = $大运经年;

		$柱 = array( 
				四柱::年柱($solarDateTime, $location),
				四柱::月柱($solarDateTime, $location), 
				四柱::日柱($solarDateTime, $location),
				四柱::时柱($solarDateTime, $location), 
				$大运, 
				$流年
		);
		$this->initialize($柱);
	}

	#@Override
	protected function 配权() {
		$this->柱数[月序]->支数()->setWeight(月提权重);

		$this->柱数[大运序]->干数()->setWeight(大运流年干支权和 - ($this->大运经年 + 1) * 大运生发);
		$this->柱数[大运序]->支数()->setWeight(($this->大运经年 + 1) * 大运生发);

		$this->柱数[流年序]->干数()->setWeight(大运流年干支权和 - 太岁权重);
		$this->柱数[流年序]->支数()->setWeight(太岁权重); // 太岁
	}

	#@Override
	protected function 柱递进序() {
		return array(
				array( 大运序, 年序 ), 
				array( 大运序, 月序 ), 
				array( 大运序, 日序 ), 
				array( 大运序, 时序 ), 
				array( 流年序, 年序 ), 
				array( 流年序, 月序 ), 
				array( 流年序, 日序 ), 
				array( 流年序, 时序 ), 
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
		return ($x <= 时序 && $y <= 时序) ? abs($x - $y) : 1;
	}
}
