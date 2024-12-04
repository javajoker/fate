<?php
namespace DchLib;

class 五行 {
	/**
	 * 木主仁，其性直，其情和。木盛的人长得丰姿秀丽，骨骼修长，手足细腻，口尖发美，面色清白。为人有博爱恻隐之心，慈祥恺悌之意，清高慷慨，质朴无伪。
	 * 木衰之人则个子瘦长，头发稀少，性格偏狭，嫉妒不仁。木气死绝之人则眉眼不正，项长喉结，肌肉干燥，为人鄙下吝啬。
	 * 
	 * 宜木者喜东方。可从事木材、木器、家具、装潢、木成品、纸业、种植业、养花业、育树苗、敬神物品、香料、植物性素食品等经营和事业。
	 * 
	 * 仁寿之合/淫匿之合：心地仁慈，长命多寿。妇命若命局水过旺泄木，则为淫欲之合。若坐死绝者，酒色破家。
	 */
	public static $木 = null;
	/**
	 * 火主礼，其性急，其情恭。火盛之人头小脚长，上尖下阔，浓眉小耳，精神闪烁，为人谦和恭敬，淳朴急躁。火衰之人则黄瘦尖楞，语言妄诞，诡诈妒毒，
	 * 做事有始无终。
	 * 
	 * 宜火者喜南方，可从事放光、照明、照光、光学、高热、液热、易烧易燃、油类、酒精类热炊食、食品、理发、化妆品、人身装饰品、文艺、文学、文具、文化学生、
	 * 文人、作家、写作、教员、校长、秘书、出版、公务、政界等方面的经营和事业。
	 * 
	 * 无情之合：相貌俊秀，薄情乏义，男多抱玩世之心，女则多嫁俊夫。
	 */
	public static $火 = null;
	/**
	 * 土主信，其性重，其情厚。土盛的人圆腰阔鼻，眉清目秀，口才声重。为人忠孝至诚，肚量宽厚，言必信，行必果。土气太过则头脑僵化，愚拙不明，内向好静。
	 * 不及之人面色忧滞，面扁鼻低，为人狠毒乖戾，不讲信用，不讲情理。
	 * 
	 * 宜土者，喜中央之地，本地。可从事土产、地产、农村、畜牧兽类农人等类、布匹、服装、纺织、石、石灰、山地、水泥、建筑、房产买卖、挡水的雨衣、雨伞、雨帆
	 * 、筑提、容水物品、当铺、古董、中间人、律师、管理、买卖、设计、顾问、丧业、筑墓、墓地管理、僧尼等工作和经营。
	 * 
	 * 中正之合：主安分守己、重信讲义。若命局无它土，又带七杀，则缺乏情义、诡计多端、不知廉耻，性刚。
	 */
	public static $土 = null;
	/**
	 * 金主义，其性刚，其情烈。金盛之人骨肉相称，面方白净，眉高眼深，体健神清。为人果断则毅，疏财仗义，深知廉耻。太过则有勇无谋，贪欲不仁。不及则身材瘦小
	 * ，为人刻薄，喜淫好杀，吝啬贪婪。
	 * 
	 * 宜金者，喜西方，可从事精纤材料或金属材料、坚硬、决断、武术、鉴定、清官、总管、汽车、交通、金融、工程、种子、开矿、民意代表、伐木、机械等行业和经营
	 * 。
	 * 
	 * 仁义之合：刚柔兼备，重仁守义。若有偏官或坐死绝等弱运者，反固执己见，轻仁寡义。
	 */
	public static $金 = null;
	/**
	 * 水主智，其性聪，其情善。水旺之人面黑有彩，语言清和，为人深思熟虑，足智多谋，学识过人。太过则好说是非、飘荡贪淫。不及则人物短小，性情无常，胆小无略
	 * ，行事反复。
	 * 
	 * 宜水者，喜北方。可从事航海、冷温不燃液体、冰水、鱼类、水产、水利、水物、冷藏、冷冻、打捞、洁洗、扫除、流水、港内、泳池、湖池塘、浴池、冷食物买卖、
	 * 漂流、奔波、流动、连续性、易变化、属水性质、音响性质、清洁性质、冷温不燃性化学、海上作业、迁旅、特技表演、运动、导游、旅行、玩具、魔术、采访记者、
	 * 侦探、旅社、灭火器具、医药业、药物经营、医生、护士、占卜等经营和工作。
	 * 
	 * 威制之合：仪表威严，智力优秀。若带七杀或坐死绝者，反性酷无情，乖僻寡合。女命逢支冲，合化之水，主性感纵欲。
	 */
	public static $水 = null;

	private static $values = array();
	
	public static function __init() {
		self::$木 = new 五行(
			0, '木', '东', 
			array('丰姿秀丽，骨骼修长，手足细腻，口尖发美，面色清白。', '个子瘦长，头发稀少。'), 
			array('为人有博爱恻隐之心，慈祥恺悌之意，清高慷慨，质朴无伪。', '', '性格偏狭，嫉妒不仁。', '眉眼不正，项长喉结，肌肉干燥，为人鄙下吝啬。'), 
			'可从事木材、木器、家具、装潢、木成品、纸业、种植业、养花业、育树苗、敬神物品、香料、植物性素食品等经营和事业。'
		);
		self::$火 = new 五行(
			1, '火', '南', 
			array('头小脚长，上尖下阔，浓眉小耳。', '黄瘦尖楞。'), 
			array('精神闪烁，为人谦和恭敬，淳朴急躁。', '', '语言妄诞，诡诈妒毒，做事有始无终。'), 
			'放光、照明、照光、光学、高热、液热、易烧易燃、油类、酒精类热炊食、食品、理发、化妆品、人身装饰品、文艺、文学、文具、文化学生、文人、作家、写作、教员、校长、秘书、出版、公务、政界等方面的经营和事业。'
		);
		self::$土 = new 五行(
			2, '土', '中央之地，本地', 
			array('圆腰阔鼻，眉清目秀，口才声重。', '面色忧滞，面扁鼻低。'), 
			array('为人忠孝至诚，肚量宽厚，言必信，行必果。', '头脑僵化，愚拙不明，内向好静。', '为人狠毒乖戾，不讲信用，不讲情理。'), 
			'土产、地产、农村、畜牧兽类农人等类、布匹、服装、纺织、石、石灰、山地、水泥、建筑、房产买卖、挡水的雨衣、雨伞、雨帆、筑提、容水物品、当铺、古董、中间人、律师、管理、买卖、设计、顾问、丧业、筑墓、墓地管理、僧尼等工作和经营。'
		);
		self::$金 = new 五行(
			3, '金', '西', 
			array('骨肉相称，面方白净，眉高眼深，体健神清。', '身材瘦小。'), 
			array('为人果断则毅，疏财仗义，深知廉耻。', '有勇无谋，贪欲不仁。', '为人刻薄，喜淫好杀，吝啬贪婪。'), 
			'精纤材料或金属材料、坚硬、决断、武术、鉴定、清官、总管、汽车、交通、金融、工程、种子、开矿、民意代表、伐木、机械等行业和经营。'
		);
		self::$水 = new 五行(
			4, '水', '北', 
			array('面黑有彩。', '人物短小。'), 
			array('语言清和，为人深思熟虑，足智多谋，学识过人。', '好说是非、飘荡贪淫。', '性情无常，胆小无略，行事反复。'), 
			'航海、冷温不燃液体、冰水、鱼类、水产、水利、水物、冷藏、冷冻、打捞、洁洗、扫除、流水、港内、泳池、湖池塘、浴池、冷食物买卖、漂流、奔波、流动、连续性、易变化、属水性质、音响性质、清洁性质、冷温不燃性化学、海上作业、迁旅、特技表演、运动、导游、旅行、玩具、魔术、采访记者、侦探、旅社、灭火器具、医药业、药物经营、医生、护士、占卜等经营和工作。'
		);
		
		self::$values = array( self::$木, self::$火, self::$土, self::$金, self::$水 );
	}
	
	public static function values() {
		return self::$values;
	}

	private $m_key;
	private $m_ordinal;

	private $人事 = array();
	private $外貌 = array();
	private $方位;
	private $职业;

	private function __construct($ordinal, $key, $方位, $外貌, $人事, $职业) {
		$this->m_ordinal = $ordinal;
		$this->m_key = $key;

		$this->方位 = $方位;
		$this->外貌 = $外貌;
		$this->人事 = $人事;
		$this->职业 = $职业;
	}

	public function toString() {
		return $this->m_key;
	}

	public function ordinal() {
		return $this->m_ordinal;
	}
	
	public function 盛者外貌() {
		return $this->外貌[0];
	}

	public function 衰者外貌() {
		return $this->外貌[1];
	}

	public function 盛者人事() {
		return $this->人事[0];
	}

	public function 过盛人事() {
		if ($this->人事[1] != '')
			return "{$this->人事[0]}但须注意{$this->人事[1]}";
		return $this->人事[0];
	}

	public function 衰者人事() {
		return $this->人事[2];
	}

	public function 死绝人事() {
		if (count($this->人事) > 3)
			return $this->人事[3];
		return $this->人事[2];
	}

	public function 方位() {
		return $this->方位;
	}

	public function 职业() {
		return $this->职业;
	}

	public function 生() {
		if (self::$土 === $this)
			return self::$金;
		if (self::$金 === $this)
			return self::$水;
		if (self::$水 === $this)
			return self::$木;
		if (self::$木 === $this)
			return self::$火;
		if (self::$火 === $this)
			return self::$土;

		return null;
	}

	public function 克() {
		if (self::$土 === $this)
			return self::$水;
		if (self::$水 === $this)
			return self::$火;
		if (self::$火 === $this)
			return self::$金;
		if (self::$金 === $this)
			return self::$木;
		if (self::$木 === $this)
			return self::$土;

		return null;
	}

	public function 印() {
		if (self::$土 === $this)
			return self::$火;
		if (self::$金 === $this)
			return self::$土;
		if (self::$水 === $this)
			return self::$金;
		if (self::$木 === $this)
			return self::$水;
		if (self::$火 === $this)
			return self::$木;

		return null;
	}

	public function 官() {
		if (self::$土 === $this)
			return self::$木;
		if (self::$水 === $this)
			return self::$土;
		if (self::$火 === $this)
			return self::$水;
		if (self::$金 === $this)
			return self::$火;
		if (self::$木 === $this)
			return self::$金;

		return null;
	}

	public function 关系($target) {
		if ($this === $target)
			return 五行生克关系::$比肩;
		if ($this->生() === $target)
			return 五行生克关系::$食神;
		if ($target->生() === $this)
			return 五行生克关系::$印绶;
		if ($this->克() === $target)
			return 五行生克关系::$妻财;
		if ($target->克() === $this)
			return 五行生克关系::$官杀;

		return null;
	}
}

五行::__init();