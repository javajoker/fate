<?php
namespace DchLib;

/**
 * 天干 | 长生 沐浴 冠带 临官 帝旺 衰 病 死 墓 绝 胎 养
 * 
 * 天干对应临官/帝旺地支，阳者顺排，阴者逆排，唯有天干土局时对应地支为火，取义“火生土”
 * 
 * 甲 | 亥 子 丑 寅 卯 辰 巳 午 未 申 酉 戌
 * 
 * 乙 | 午 巳 辰 卯 寅 丑 子 亥 戌 酉 申 未
 * 
 * 丙 | 寅 卯 辰 巳 午 未 申 酉 戌 亥 子 丑
 * 
 * 丁 | 酉 申 未 午 巳 辰 卯 寅 丑 子 亥 戌
 * 
 * 戊 | 寅 卯 辰 巳 午 未 申 酉 戌 亥 子 丑
 * 
 * 己 | 酉 申 未 午 巳 辰 卯 寅 丑 子 亥 戌
 * 
 * 庚 | 巳 午 未 申 酉 戌 亥 子 丑 寅 卯 辰
 * 
 * 辛 | 子 亥 戌 酉 申 未 午 巳 辰 卯 寅 丑
 * 
 * 壬 | 申 酉 戌 亥 子 丑 寅 卯 辰 巳 午 未
 * 
 * 癸 | 卯 寅 丑 子 亥 戌 酉 申 未 午 巳 辰
 * 
 * @author dch
 */
class 生旺死绝 {
	public static $长生, $沐浴, $冠带, $临官, $帝旺, $衰, $病, $死, $墓, $绝, $胎, $养;
	private static $values = array();
	private static $生旺死绝表 = array();

	public static function __init() {
		self::$长生 = new 生旺死绝();
		self::$沐浴 = new 生旺死绝();
		self::$冠带 = new 生旺死绝();
		self::$临官 = new 生旺死绝();
		self::$帝旺 = new 生旺死绝();
		self::$衰 = new 生旺死绝();
		self::$病 = new 生旺死绝();
		self::$死 = new 生旺死绝();
		self::$墓 = new 生旺死绝();
		self::$绝 = new 生旺死绝();
		self::$胎 = new 生旺死绝();
		self::$养 = new 生旺死绝();
		
		self::$生旺死绝表 = array(
			array( 天干::$甲, 地支::$亥 ), array( 天干::$乙, 地支::$午 ),
			array( 天干::$丙, 地支::$寅 ), array( 天干::$丁, 地支::$酉 ),
			array( 天干::$戊, 地支::$寅 ), array( 天干::$己, 地支::$酉 ),
			array( 天干::$庚, 地支::$巳 ), array( 天干::$辛, 地支::$子 ),
			array( 天干::$壬, 地支::$申 ), array( 天干::$癸, 地支::$卯 ),
		);
		
		self::$values = array(
			self::$长生, self::$沐浴, self::$冠带, self::$临官, self::$帝旺, self::$衰, self::$病, self::$死, self::$墓, self::$绝, self::$胎, self::$养
		);
	}

	private $m_value = 0;

	public function getValue() {
		return $this->m_value;
	}

	public static function 天干生旺死绝($干, $支) {
		for ($i = count(self::$生旺死绝表) - 1; $i >= 0; --$i) {
			if (self::$生旺死绝表[$i][0] === $干) {
				$x = $支->术数()->getIndex() - self::$生旺死绝表[$i][1]->术数()->getIndex();
				if ($干->术数()->getIndex() % 2 == 0)
					$x = -$x;
				return self::$values[(x < 0 ? x + 12 : x)];
			}
		}
		return null;
	}

	public static function 寻支($干, $生旺死绝) {
		$x = self::$生旺死绝表[($干->术数()->getIndex() - 1 + 10) % 10][1]->术数()->getIndex();
		if ($干->术数()->getIndex() % 2 == 0)
			$x = $x - $生旺死绝->m_value;
		else
			$x = $x + $生旺死绝->m_value;
		return 地支::lookup($x);
	}
}

生旺死绝::__init();