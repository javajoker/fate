<?php

/**
 * 获取指定数的纳音五行。
 * 
 * 六十甲子和五音十二律结合起来，其中一律含五音，总数共为六十的“纳音五行”。
 * 
 * 音声流转，金传火，火传木，木传水，水传土
 * 
 * 甲戌乙亥山头火 丙申丁酉山下火 戊午己未天上火 庚辰辛巳白腊金 壬寅癸卯金箔金 甲子乙丑海中金
 * 
 * 丙戌丁亥屋上土 戊申己酉大驿土 庚午辛未路旁土 壬辰癸巳长流水 甲寅乙卯大溪水 丙子丁丑涧下水
 * 
 * 戊戌己亥平地木 庚申辛酉石榴木 壬午癸未杨柳木 甲辰乙巳佛灯火 丙寅丁卯炉中火 戊子己丑霹雳火
 * 
 * 庚戌辛亥钗钏金 壬申癸酉剑锋金 甲午乙未沙中金 丙辰丁巳沙中土 戊寅己卯城头土 庚子辛丑壁上土
 * 
 * 壬戌癸亥大海水 甲申乙酉泉中水 丙午丁未天河水 戊辰己巳大林木 庚寅辛卯松柏木 壬子癸丑桑枝木
 * 
 * @author dch
 * 
 */
class 干支 {

	public static $纳音 = array(
			// 1
			'海中金', '炉中火', '大林木', '路旁土', '剑锋金', '山头火',
			// 2
			'涧下水', '城墙土', '白腊金', '杨柳木', '泉中水', '屋上土',
			// 3
			'霹雷火', '松柏木', '常流水', '沙中金', '山下火', '平地木',
			// 4
			'壁上土', '金箔金', '佛灯火', '天河水', '大驿土', '钗钏金',
			// 5
			'桑松木', '大溪水', '沙中土', '天上火', '石榴木', '大海水' 
	);

	private static $values = array();

	public static function __init() {
		for ($i = 0; $i < 60; ++$i) {
			$s = self::$纳音[$i >> 1];
			switch ( cutStr($s, 2, 1) ) {
			case '金':
				$ele = 五行::$金;
				break;
			case '水':
				$ele = 五行::$水;
				break;
			case '火':
				$ele = 五行::$火;
				break;
			case '土':
				$ele = 五行::$土;
				break;
			case '木':
				$ele = 五行::$木;
				break;
			}
			self::$values[($i + 1) % 60] = new 干支($i + 1, $ele, cutStr($s, 0, 2));
		}
	}

	/**
	 * 返回甲子数x对应的干支
	 * 
	 * @param $x
	 * @return
	 */
	public static function lookup($x) {
		$x = $x % 60;

		return self::$values[($x < 0 ? $x + 60 : $x)];
	}

	/**
	 * 返回对应的干支
	 * 
	 * @return
	 */
	public static function lookup2(天干 $i, 地支 $j) {
		for ($y = 0; $y < 6; ++$y) {
			$a = (12.0 * $y + $j->术数()->getIndex() - $i->术数()->getIndex()) / 10;
			if ($a == intval($a)) {
				$x = $a * 10 + $i->术数()->getIndex();
				$x = $x % 60;
				return self::$values[($x < 0 ? $x + 60 : $x)];
			}
		}
		return null;
	}

	private $m_shushu;

	private $m_modifier;
	private $干;
	private $支;

	private function __construct($value, $element, $形容) {
		$this->m_modifier = $形容;
		$this->干 = 天干::lookup($value);
		$this->支 = 地支::lookup($value);

		$value = $value % 60;
		$this->m_shushu = new 术数($element, null, $value);
	}

	public function 术数() {
		return $this->m_shushu;
	}

	public function 天干() {
		return $this->干;
	}

	public function 地支() {
		return $this->支;
	}

	public function 副支() {
		$x = $this->m_shushu->getIndex();
		$x = ($x % 2 == 1) ? $x + 1 : $x - 1;
		return 地支::lookup($x < 0 ? $x + 60 : $x);
	}

	public function 形容() {
		return $this->m_modifier;
	}

	public function 纳音() {
		return $this->m_modifier . $this->m_shushu->五行()->toString();
	}

	public function toString() {
		return $this->干->toString() . $this->支->toString();
	}
}

干支::__init();
