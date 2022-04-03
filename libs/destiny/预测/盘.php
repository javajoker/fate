<?php

/**
 * 胡子模型：
 * 
 * 天地人三元论事，不脱五行阴阳，十天干也。
 * 
 * 以一至十和五十又五为天地之数，人元内藏五行，又五得六十数，是为天地人轮回之数。
 * 
 * 由十天干而得地支十二。故曰，地支藏干，是为人元。
 * 
 * 年月日时四柱干支以八字论命，实以天干论阴阳五行之生克乘侮也。
 * 
 * 年柱为根，月柱成枝，日柱开花，时柱得果。
 * 
 * 年岁日时柱不同，唯有月支映天时，四柱之中，以月支为提纲，故称“月提”。
 * 
 * 胎成得灵，择日而生，故日干为“我”，曰“日主”。
 * 
 * 四柱预测，首论干支关系，是为“十干之地支生旺死绝”，实为五行生克官印各宫顺逆而行，地支藏干由此而得。
 * 
 * 所谓生旺死绝论得令得地增力者，实为比劫帮身增力，五行相生。以通根透干为干支比劫助力，次论五行生克，故得令得地不用。
 * 
 * 地支为天干生发之根，以其为根故，彼此不易沟通，故不直论生克而先观局。
 * 
 * 三会三合又及半合，借一点天干之性沟通。得透方可成局。不透干不成局故，无“合绊”之说。
 * 
 * 地支局中，一点本气或冲，或合，或刑，或害，皆以五行生克为要：
 * 
 * 冲破合局者，对冲泄气而“泄多为克”故；
 * 
 * 所谓“合绊”不入刑冲克害者，五行过弱生者不生，克者不克也。
 * 
 * 地支已定，乃论天干，合同支藏，于是生克，命局成矣。
 * 
 * 复次，观命局，定用神，成格局，得神煞。不脱阴阳五行指事尔。
 * 
 * 先天命既定，后天又行运，命运成也。又及流年，命内“谈”宫，流年行运论“星”，预测论命可也。
 * 
 * 由古至今预测或以命运流年六柱，或以四柱命大小运胎元流年八柱，知其然而渐不知其所以然。
 * 
 * 地支为里，天干是表，由里及表。所谓预测，知其表象尔。故曰，心若淡然，苦者何谓？
 * 
 * 胡子以“其然”逆推“所以然”，以期自圆其说。附会而已。
 */
define('年序', 0);
define('月序', 年序 + 1);
define('日序', 月序 + 1);
define('时序', 日序 + 1);

$宫义 = array( 
	array( '父祖荫', '母祖荫' ),
	array( '父', '母', '兄弟姐妹' ), 
	array( '对象', '对象' ), 
	array( '女', '子' )
	);

abstract class 盘 {
	private $m_birthday, $乾造, $m_location;

	protected function __construct($solarDateTime, $乾造, $location) {
		$this->m_birthday = $solarDateTime;
		$this->乾造 = $乾造;
		$this->m_location = $location;
	}

	protected $柱数 = array(), $日主;

	public function 柱数() {
		return $this->柱数;
	}

	public function 日主() {
		return $this->日主;
	}

	protected function initialize($柱) {
		$this->日主 = $柱[日序]->天干()->术数();

		for ($i = 0; $i < count($柱); ++$i) {
			$this->柱数[$i] = new 干支数($柱[$i]);
		}

		$this->配权();
	}

	protected abstract function 配权();

	/**
	 * 组局
	 * 
	 * 1) 冲，各合见冲
	 * 
	 * 2) 三会局 > 三合局 > 旺半合（生地半合、墓地半合） > 六合 > 非旺半合（夹拱）
	 * 
	 * 3) 刑害入盘
	 * 
	 * 数论
	 * 
	 * 1) 各合论化
	 * 
	 * 2) 干支相克
	 */
	public function 运盘() {
		global $__DEBUG;
		if ($__DEBUG) $this->观盘();

		// 地支指后天之气，以（木）星、日、月、地，各分十二
		// 地支生克，冲即为克，合即比生，刑害待新数入盘即成生克
		foreach ($this->柱递进序() as $pair) {
			$x = $pair[0];
			$y = $pair[1];
			$this->五行生克数($this->柱数[$x]->支数(), $this->柱数[$y]->支数(), $this->getDistance($x, $y));
		}

		// 直坐干支生克
		for ($i = 0; $i < count($this->柱数); ++$i) {
			$this->五行生克数($this->柱数[$i]->干数(), $this->柱数[$i]->支数(), 0);
		}
		// 干支生克
		foreach ($this->柱递进序() as $pair) {
			$x = $pair[0];
			$y = $pair[1];
			$this->五行生克数($this->柱数[$x]->干数(), $this->柱数[$y]->支数(), $this->getDistance($x, $y) + 1);
			$this->五行生克数($this->柱数[$x]->支数(), $this->柱数[$y]->干数(), $this->getDistance($x, $y) + 1);
		}
		// 天干生克
		foreach ($this->柱递进序() as $pair) {
			$x = $pair[0];
			$y = $pair[1];
			$this->五行生克数($this->柱数[$x]->干数(), $this->柱数[$y]->干数(), $this->getDistance($x, $y));
		}

		$this->十神();
	}

	/**
	 * 柱间生克序，如树木生发，根发而枝，枝头开花，花落果实。
	 * 
	 * @return [首柱序, 次柱序]
	 */
	protected abstract function 柱递进序();

	public function 支五行() {
		$eleWeights = array();
		for ($i = 0; $i < count($this->柱数); ++$i) {
			foreach ($this->柱数[$i]->支数()->藏干数() as $gan) {
				$eleWeights[$gan->术数()->五行()->ordinal()] += $gan->getWeight();
			}
		}

		return $eleWeights;
	}

	/**
	 * @param $x
	 * @param $y
	 * @return 贴柱 = 1； 隔柱 = 2； 远柱 = 3
	 */
	protected abstract function getDistance($x, $y);

	private function 五行生克数($x, $y, $柱距) {
		$xs = ($x instanceof 干数) ? array( $x ) : $x->藏干数();
		$ys = ($y instanceof 干数) ? array( $y ) : $y->藏干数();

		for ($i = count($xs) - 1; $i >= 0; --$i) {
			for ($j = count($ys) - 1; $j >= 0; --$j)
				干数::生克($xs[$i], $ys[$j], $柱距);
		}

		global $__DEBUG;
		if ($__DEBUG) $this->观盘();
	}

	private function 五行生克数2($x, $y, $柱距) {
		$xs = ($x instanceof 干数) ? array( $x ) : $x->藏干数();
		$ys = ($y instanceof 干数) ? array( $y ) : $y->藏干数();

		for ($i = min(count($xs), count($ys)) - 1; $i >= 0; --$i) {
			干数::生克($xs[$i], $ys[$i], $柱距);
		}

		global $__DEBUG;
		if ($__DEBUG) $this->观盘();
	}

	private function 天干五合() {
		/**
		 * 合化之義，以十干陰陽相配而成。河圖之數，以一二三四五配六七八十，先天之道也。故始於太陰之水，而終於沖氣之土，以氣而語其生之序也。
		 * 蓋未有五行之先
		 * ，必先有陰陽老少，而後沖氣，故生以土。終之既有五行，則萬物又生於土，而水火木金，亦寄質焉，故以土先之。是以甲己相合之始，則化為土
		 * ；土則生金，
		 * 故乙庚化金次之；金生水，故丙辛化水又次之；水生木，故丁壬化木又次之；木生火，故戊癸化火又次之，而五行遍焉。先之以土，相生之序，自然如此
		 * 。此十干合化之義也。
		 * 
		 * 十干配合，源於《易》天一、地二、天三、地四、天五、地六、天七、地八、天九、地十之數，而以為十干之合即河圖之合，其實非也。河圖一六共宗（水）
		 * ，二七同道（金），三八為朋（木），四九為友（火），五十同途（土）。堪輿之學，以盤為體，根於河圖，以運為用，基於洛書，此與命理不同。
		 * 命理十干之合，與醫道同源，出於〈內經·五運大論〉。曰　：丹天之氣，經於牛女戊分；黅天之氣，經於心尾己分；蒼天之氣，經於危室柳鬼；素天之氣，
		 * 經於亢氐昴畢
		 * ；玄天之氣，經於張翼奎婁．所謂戊己之間，奎璧角軫，乃天地之門戶也．戌亥之間，奎璧之分也；辰己之間，角軫之分也．故五運皆起於角軫．
		 * 甲己之歲，戊己黅天之氣
		 * ，經於角軫，角屬辰軫屬巳，其歲月建，得戊辰己巳，干皆土，故為土運．乙庚之歲，庚辛素天之氣，經於角軫，其歲月建，得庚辰辛巳
		 * ，干皆金，故為金運．
		 * 丙辛之歲，壬癸玄天之氣，經於角軫，其歲月建，得甲辰乙巳，干皆木，故為木運。戊癸之歲，丙丁丹天之氣經天於角軫，其歲月建得丙辰丁巳
		 * ，干皆火，故為火運。夫十干各有本氣，是為五行，若五合所化，則為五運。
		 */
		// 天干合化，所谓天干五合，阳弱主克，阴强被克，由是反生，一如瓢水急入大火而火势愈猛，所谓丁壬化木云云，不解
		$五行比劫权重 = $this->支五行();
		$合 = array();
		foreach ($this->柱递进序() as $pair) {
			$x = $pair[0];
			$y = $pair[1];
			if ($this->getDistance($x, $y) > 1)
				continue;

			$ele = $this->柱数[$x]->干数()->天干()->五合($this->柱数[$y]->干数()->天干());
			if ($ele == null || $五行比劫权重[$ele->ordinal()] < (基础权重 / 2))
				continue;

			$eles[$x] = $eles[$y] = $ele;
			$合[$x] |= (1 << $x) | (1 << $y) | $合[$y];
			$合[$y] |= (1 << $x) | (1 << $y) | $合[$x];
		}
		for ($i = 0; $i < count($this->柱数); ++$i) {
			if ($合[$i] == 0)
				continue;

			$x = $合[$i];
			$ids = array();
			for ($j = 0; $j < count($this->柱数); ++$j) {
				if (($x | (1 << $j)) > 0) {
					$ids[] = $j;
				}
			}
			if ((count($ids) % 2) == 1) {
				foreach ($ids as $id) {
					$合[$id] = 0;
				}
			}
		}
		for ($i = 0; $i < count($this->柱数); ++$i) {
			if ($合[$i] == 0 || $i == 日序)
				continue;
			$this->柱数[$i]->干数()->化($eles[$i]);
		}
	}

	protected function 十神() {
		for ($i = 0; $i < count($this->柱数); ++$i) {
			foreach ($this->柱数[$i]->支数()->藏干数() as $gan) {
				$gan->神(十神::求神($this->日主, $gan->术数()));
			}
			if ($i == 日序)
				continue;
			$this->柱数[$i]->干数()->神(十神::求神($this->日主, $this->柱数[$i]->干数()->术数()));
		}
	}

	public function 顺() {
		return 命运::顺行($this->m_birthday, $this->乾造, $this->m_location);
	}

	public function 宫名($柱序, $干) {
		if ($柱序 == 日序 && $干)
			return '命主';

		global $宫义;
		return $宫义[$柱序][$this->顺() ^ $干 ? 0 : 1];
	}

	public function 观盘() {
		printf('=======================================' . "\n");
		for ($i = 年序; $i <= 时序; ++$i) {
			$zs = $this->柱数();
			$x = $zs[$i]->干数();

			printf('	%s(%s):	%f	%s 支藏 :', 
					$x->天干()->toString(), $x->术数()->五行()->toString(), 
					$x->getWeight(), 
					$zs[$i]->支数()->地支()->toString());

			foreach ($zs[$i]->支数()->藏干数() as $y) {
				printf('	- %s	%f', $y->术数()->五行()->toString(), $y->getWeight());
			}
			printf("\n");
		}
	}
}
