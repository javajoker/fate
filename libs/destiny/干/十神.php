<?php
namespace DchLib;

class 十神 {
	/**
	 * 克我者为正官、七杀，男命为职权子息，女命为夫星。
	 */
	public static $正官, $七杀 /* 偏官 */;
	/**
	 * 生我者为正印、偏印，总称叫印绶，是母亲之谓。
	 */
	public static $正印, $偏印;
	/**
	 * 我克者为正财、偏财，男命为父亲、妻子、财产，女命为侍夫的才智。
	 */
	public static $正财, $偏财;
	/**
	 * 我生者为食神、伤官，男命为聪明才智，女命为子息。
	 */
	public static $食神, $伤官;
	/**
	 * 同类者为比肩、比劫，是兄弟姊妹之谓，也是分福耗财之谓。
	 */
	public static $比肩, $劫财 /* 比劫 */;
	/**
	 * 日主
	 */
	public static $NA;
	
	private static $values = array();
	
	public static function __init() {
		self::$正官 = new 十神(
			0, '正官',
			五行生克关系::$官杀,
			'正直负责，端庄严肃，循规蹈矩，但易流于刻板、墨守成规，反为意志不坚',
			array( '工作职权，仕途', '升迁机会', '官位不保' )
		);
		self::$七杀 = new 十神(
			1, '七杀',
			五行生克关系::$官杀,
			'豪爽侠义，积极进取，威严机敏，但易流于偏激，叛逆霸道，反为堕落极端',
			array( '江湖地位，权力', '大权在握', '权力不保' )
		);

		self::$正印 = new 十神(
			2, '正印',
			五行生克关系::$印绶,
			'聪颖仁慈，淡泊名利，逆来顺受，但易流于庸碌，缺乏进取，反为迟钝消极',
			array( '事业', '事业有成', '事业不稳' )
		);
		self::$偏印 = new 十神(
			3, '偏印',
			五行生克关系::$印绶,
			'精明干练，反应机警，多才多艺，但易流于孤独，缺乏人情，反为自私冷漠',
			array( '学业', '学业有成', '' )
		);

		self::$正财 = new 十神(
			4, '正财',
			五行生克关系::$妻财,
			'勤劳节俭，踏实保守，任劳任怨，但易流于苟且，缺乏进取，反为懦弱无能',
			array( '财富（明财）', '日常收入涨', '' )
		);
		self::$偏财 = new 十神(
			5, '偏财',
			五行生克关系::$妻财,
			'慷慨重情，聪敏机灵，乐观开朗，但易流于虚浮，缺乏节制，反为浮华风流',
			array( '财富（暗财）', '有意外暗财', '' )
		);

		self::$食神 = new 十神(
			6, '食神',
			五行生克关系::$食神,
			'温文随和，待人宽厚，善良体贴，但易流于虚伪，缺乏是非，反为迂腐懦怯',
			array( '技术能力，外在', '有所得', '有所失' )
		);
		self::$伤官 = new 十神(
			7, '伤官',
			五行生克关系::$食神,
			'聪明活跃，才华横溢，逞强好胜，但易流于任性，缺乏约束，反为桀傲不驯',
			array( '聪明才智，内在', '有所得', '有所失' )
		);

		self::$比肩 = new 十神(
			8, '比肩',
			五行生克关系::$比肩,
			'稳健刚毅，冒险勇敢，积极进取，但易流于孤僻，缺乏合群，反为孤立寡合',
			array( '朋友', '得道多助', '失道寡助' )
		);
		self::$劫财 = new 十神(
			9, '劫财',
			五行生克关系::$比肩,
			'热诚坦直，坚韧志旺，奋斗不屈，但易流于盲目，缺乏理智，反为蛮横冲动',
			array( '损友', '损友败财', '' )
		);
		self::$NA = new 十神( 10, '日主' );
		
		self::$values = array(
			self::$正官, self::$七杀, 
			self::$正印, self::$偏印, 
			self::$正财, self::$偏财, 
			self::$食神, self::$伤官, 
			self::$比肩, self::$劫财, 
			self::$NA
		);
	}
	
	public static function values() {
		return self::$values;
	}

	private $m_key;
	private $m_ordinal;

	private $m_relation;
	private $心性 = '';
	private $指事 = array(); // 指事, 旺, 衰
	
	private function __construct($ordinal, $key, $relation = null, $心性 = null, $指事 = null) {
		$this->m_ordinal = $ordinal;
		$this->m_key = $key;

		$this->m_relation = $relation;
		$this->心性 = $心性;
		$this->指事 = $指事;
	}

	public function toString() {
		return $this->m_key;
	}

	public function ordinal() {
		return $this->m_ordinal;
	}

	public function 五行生克关系() {
		return $this->m_relation;
	}

	public function 心性() {
		return $this->心性;
	}

	public function 指事() {
		return $this->指事[0];
	}

	public function 旺() {
		return $this->指事[1];
	}

	public function 衰() {
		return $this->指事[2];
	}

	public static function 求神($host, $x) {
		$relation = $host->五行()->关系($x->五行());
		if (五行生克关系::$印绶 === $relation) {
			if ($host->阴阳() === $x->阴阳()) {
				return self::$偏印;
			} else {
				return self::$正印;
			}
		}
		if (五行生克关系::$食神 === $relation) {
			if ($host->阴阳() === $x->阴阳()) {
				return self::$食神;
			} else {
				return self::$伤官;
			}
		}
		if (五行生克关系::$官杀 === $relation) {
			if ($host->阴阳() === $x->阴阳()) {
				return self::$七杀;
			} else {
				return self::$正官;
			}
		}
		if (五行生克关系::$妻财 === $relation) {
			if ($host->阴阳() === $x->阴阳()) {
				return self::$偏财;
			} else {
				return self::$正财;
			}
		}
		if (五行生克关系::$比肩 === $relation) {
			if ($host->阴阳() === $x->阴阳()) {
				return self::$比肩;
			} else {
				return self::$劫财;
			}
		}

		return null;
	}

	public static function 术数($host, $_神) {
		if (self::$正官 == $_神) {
			return new 术数($host->五行()->官(), $host->阴阳()->opposite());
		} else if (self::$七杀 == $_神) {
			return new 术数($host->五行()->官(), $host->阴阳());
		} else if (self::$正印 == $_神) {
			return new 术数($host->五行()->印(), $host->阴阳()->opposite());
		} else if (self::$偏印 == $_神) {
			return new 术数($host->五行()->印(), $host->阴阳());
		} else if (self::$正财 == $_神) {
			return new 术数($host->五行()->克(), $host->阴阳()->opposite());
		} else if (self::$偏财 == $_神) {
			return new 术数($host->五行()->克(), $host->阴阳());
		} else if (self::$伤官 == $_神) {
			return new 术数($host->五行()->生(), $host->阴阳()->opposite());
		} else if (self::$食神 == $_神) {
			return new 术数($host->五行()->生(), $host->阴阳());
		} else if (self::$劫财 == $_神) {
			return new 术数($host->五行(), $host->阴阳()->opposite());
		} else if (self::$比肩 == $_神) {
			return new 术数($host->五行(), $host->阴阳());
		}
		return null;
	}
}

十神::__init();