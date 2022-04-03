package com.infoecos.cn.destiny.lib.预测;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.infoecos.cn.destiny.lib.TimezoneLocation;
import com.infoecos.cn.destiny.lib.命运;
import com.infoecos.cn.destiny.lib.干支;
import com.infoecos.cn.destiny.lib.术数;
import com.infoecos.cn.destiny.lib.五行.五行;
import com.infoecos.cn.destiny.lib.干.十神;

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
public abstract class 盘 {
	public static final int 年序 = 0;
	public static final int 月序 = 年序 + 1;
	public static final int 日序 = 月序 + 1;
	public static final int 时序 = 日序 + 1;

	public static float 月提权重 = Consts.基础权重 * 3; // 得令故

	public static final String 宫[][] = { { "父祖荫", "母祖荫" },
			{ "父", "母", "兄弟姐妹" }, { "对象", "对象" }, { "女", "子" } };

	private Date birthday;
	private boolean 乾造;
	private TimezoneLocation location;

	public 盘(Date solarDateTime, boolean 乾造, TimezoneLocation location)
			throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(solarDateTime);
		if (calendar.get(Calendar.YEAR) <= 1900
				|| calendar.get(Calendar.YEAR) > 2100)
			throw new Exception("year 1901-2100 only");
		this.birthday = solarDateTime;
		this.乾造 = 乾造;
		this.location = location;
	}

	protected 干支数[] 柱数;
	protected 术数 日主;

	public 干支数[] 柱数() {
		return 柱数;
	}

	public 术数 日主() {
		return 日主;
	}

	protected abstract int 日序();

	protected void initialize(干支[] 柱) {
		this.柱数 = new 干支数[柱.length];
		this.日主 = 柱[日序()].天干().术数();

		for (int i = 0; i < 柱.length; ++i) {
			柱数[i] = new 干支数(柱[i]);
		}

		配权();
	}

	protected abstract void 配权();

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
	public void 运盘() {
		if (Consts._DEBUG) {
			观盘();
		}

		// 地支指后天之气，以（木）星、日、月、地，各分十二
		// 地支生克，冲即为克，合即比生，刑害待新数入盘即成生克
		for (int[] pair : 柱递进序()) {
			int x = pair[0], y = pair[1];
			五行生克数(柱数[x].支数(), 柱数[y].支数(), getDistance(x, y));
		}

		// 天干指先天之气，合五行阴阳，生而有之，先天同一。感后天之气而降生。
		// // 天干生克
		// for (int[] pair : 柱递进序()) {
		// int x = pair[0], y = pair[1];
		// 五行生克数(柱数[x].干数(), 柱数[y].干数(), getDistance(x, y));
		// }
		// float[] 五行比劫权重 = 支五行();
		// for (int[] pair : 柱递进序()) {
		// int x = pair[0], y = pair[1];
		// if (getDistance(x, y) > 1)
		// continue;
		// 五行化数(柱数[x].干数(), 柱数[y].干数(), 五行比劫权重);
		// }
		// 天干五合();

		// 直坐干支生克
		for (int i = 0; i < 柱数.length; ++i) {
			五行生克数(柱数[i].干数(), 柱数[i].支数(), 0);
		}
		// 干支生克
		for (int[] pair : 柱递进序()) {
			int x = pair[0], y = pair[1];
			五行生克数(柱数[x].干数(), 柱数[y].支数(), getDistance(x, y) + 1);
			五行生克数(柱数[x].支数(), 柱数[y].干数(), getDistance(x, y) + 1);
		}
		// 天干生克
		for (int[] pair : 柱递进序()) {
			int x = pair[0], y = pair[1];
			五行生克数(柱数[x].干数(), 柱数[y].干数(), getDistance(x, y));
		}

		十神();
	}

	/**
	 * 柱间生克序，如树木生发，根发而枝，枝头开花，花落果实。
	 * 
	 * @return [首柱序, 次柱序]
	 */
	protected abstract int[][] 柱递进序();

	protected float[] 支五行() {
		float[] eleWeights = new float[5];
		for (int i = 0; i < 柱数.length; ++i) {
			for (干数 gan : 柱数[i].支数().藏干数()) {
				eleWeights[gan.术数().五行().ordinal()] += gan.getWeight();
			}
		}

		return eleWeights;
	}

	/**
	 * @param x
	 * @param y
	 * @return 贴柱 = 1； 隔柱 = 2； 远柱 = 3
	 */
	protected abstract int getDistance(int x, int y);

	private void 五行生克数(数 x, 数 y, int 柱距) {
		干数[] xs = (x instanceof 干数) ? new 干数[] { (干数) x } : ((支数) x).藏干数(), ys = (y instanceof 干数) ? new 干数[] { (干数) y }
				: ((支数) y).藏干数();

		for (int i = xs.length - 1; i >= 0; --i) {
			for (int j = ys.length - 1; j >= 0; --j)
				干数.生克(xs[i], ys[j], 柱距);
		}

		if (Consts._DEBUG) {
			观盘();
		}
	}

	private void 五行生克数2(数 x, 数 y, int 柱距) {
		干数[] xs = (x instanceof 干数) ? new 干数[] { (干数) x } : ((支数) x).藏干数(), ys = (y instanceof 干数) ? new 干数[] { (干数) y }
				: ((支数) y).藏干数();

		for (int i = Math.min(xs.length, ys.length) - 1; i >= 0; --i) {
			干数.生克(xs[i], ys[i], 柱距);
		}

		if (Consts._DEBUG) {
			观盘();
		}
	}

	// private void 五行化数(干数 i, 干数 j, float[] 五行比劫权重) {
	// if (null == 五行比劫权重 || 五行比劫权重.length != 5)
	// return;
	//
	// 五行 x = i.术数().五行(), y = j.术数().五行();
	// if (x.equals(y))
	// return;
	//
	// 干数 host, client;
	// if (x.克().equals(y)) {
	// host = i;
	// client = j;
	// } else if (y.克().equals(x)) {
	// host = j;
	// client = i;
	// } else
	// return;
	//
	// if (!(阴阳.阳.equals(host.术数().阴阳()) && 阴阳.阴.equals(client.术数().阴阳())))
	// return;
	//
	// // 阳弱主克，阴强被克，由是反生，一如瓢水急入大火而火势愈猛
	// float hw = host.getWeight(), cw = client.getWeight();
	// if ((hw + 五行比劫权重[host.术数().五行().ordinal()]) < ((cw + 五行比劫权重[client.术数()
	// .五行().ordinal()]) / 2)) {
	// host.化(client.术数().五行());
	// }
	// }

	private void 天干五合() {
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
		float[] 五行比劫权重 = 支五行();
		int[] 合 = new int[柱数.length];
		五行[] eles = new 五行[柱数.length];
		for (int[] pair : 柱递进序()) {
			int x = pair[0], y = pair[1];
			if (getDistance(x, y) > 1)
				continue;

			五行 ele = 柱数[x].干数().天干().五合(柱数[y].干数().天干());
			if (ele == null || 五行比劫权重[ele.ordinal()] < (Consts.基础权重 / 2))
				continue;

			eles[x] = eles[y] = ele;
			合[x] |= (1 << x) | (1 << y) | 合[y];
			合[y] |= (1 << x) | (1 << y) | 合[x];
		}
		for (int i = 0; i < 柱数.length; ++i) {
			if (合[i] == 0)
				continue;

			int x = 合[i];
			List<Integer> ids = new ArrayList<Integer>();
			for (int j = 0; j < 柱数.length; ++j) {
				if ((x | (1 << j)) > 0) {
					ids.add(j);
				}
			}
			if ((ids.size() % 2) == 1) {
				for (int id : ids) {
					合[id] = 0;
				}
			}
		}
		for (int i = 0; i < 柱数.length; ++i) {
			if (合[i] == 0 || i == 日序())
				continue;
			柱数[i].干数().化(eles[i]);
		}
	}

	protected void 十神() {
		for (int i = 0; i < 柱数.length; ++i) {
			for (干数 gan : 柱数[i].支数().藏干数()) {
				gan.神(十神.求神(日主, gan.术数()));
			}
			if (i == 日序())
				continue;
			柱数[i].干数().神(十神.求神(日主, 柱数[i].干数().术数()));
		}
	}

	public boolean 顺() {
		try {
			return 命运.顺行(birthday, 乾造, location);
		} catch (Exception e) {
			return 乾造;
		}
	}

	public String 宫名(int 柱序, boolean 干) {
		if (柱序 == 日序 && 干)
			return "命主";

		return 宫[柱序][顺() ^ 干 ? 0 : 1];
	}

	public void 观盘() {
		System.out.println("=======================================");
		for (int i = 盘.年序; i <= 盘.时序; ++i) {
			干数 x = 柱数()[i].干数();

			System.out.print(String.format("\t%s(%s):\t%f\t%s 支藏 :", x.天干(), x
					.术数().五行(), x.getWeight(), 柱数()[i].支数().地支()));

			for (干数 y : 柱数()[i].支数().藏干数()) {
				System.out.print(String.format("\t- %s\t%f", y.术数().五行(),
						y.getWeight()));
			}
			System.out.println();
		}
	}
}
