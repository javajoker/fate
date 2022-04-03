package com.infoecos.cn.destiny.lib.预测;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.infoecos.cn.destiny.lib.TimezoneLocation;
import com.infoecos.cn.destiny.lib.命运;
import com.infoecos.cn.destiny.lib.四柱;
import com.infoecos.cn.destiny.lib.干支;
import com.infoecos.cn.destiny.lib.术数;
import com.infoecos.cn.destiny.lib.common.阴阳;
import com.infoecos.cn.destiny.lib.五行.五行;
import com.infoecos.cn.destiny.lib.干.十神;
import com.infoecos.cn.destiny.lib.干.天干;
import com.infoecos.cn.destiny.lib.时.节令;
import com.infoecos.cn.destiny.utils.Arrays2;
import com.infoecos.cn.lunisolar.LunarDate;
import com.infoecos.cn.lunisolar.农历;

public class 八字命评 {
	private 四柱盘 pan;
	private Date birthday;
	private boolean 乾造;
	private TimezoneLocation location;
	private List<Date[]> timespan;
	private Map<String, List<Float>> 宫;
	private Map<十神, List<Float>> 神;

	public 八字命评(Date solarDateTime, boolean 乾造, TimezoneLocation location)
			throws Exception {
		this.birthday = solarDateTime;
		this.乾造 = 乾造;
		this.location = location;
		this.pan = new 四柱盘(solarDateTime, 乾造, location);

		pan.运盘();

		if (Consts._VERBOSE) {
			System.out.println("== 用神喜忌 ==");
			System.out.println(String.format("\t用神：%s", 用神().用神()));
			System.out.println(String.format("\t喜神：%s", 用神().喜神()));
			System.out.println(String.format("\t忌神：%s", 用神().忌神()));

			观盘(pan);
		}
	}

	private 喜用神 用神 = null;

	public 喜用神 用神() {
		if (用神 == null) {
			float[] eleWeights = 盘五行(), deviations = new float[5];
			五行 host = pan.柱数()[盘.日序].干数().术数().五行();
			for (五行 ele : 五行.values()) {
				float[] newEleWeights = Arrays2.copyOf(eleWeights, 5), ws = new float[] {
						Consts.基础权重, 0 };
				五行 e = null;
				// float o = 0f;

				e = host.官();
				ws[1] = newEleWeights[e.ordinal()];
				ws = 干数.生克(ws, new 术数[] { new 术数(ele, 阴阳.阳), new 术数(e, 阴阳.阳) });
				newEleWeights[e.ordinal()] = ws[1];
				// o = ws[1] - newEleWeights[e.ordinal()];
				// newEleWeights[e.ordinal()] += o * (1 - 干数.克主耗气);
				// newEleWeights[host.ordinal()] -= o * 干数.克客失气;

				e = host.克();
				ws[1] = newEleWeights[e.ordinal()];
				ws = 干数.生克(ws, new 术数[] { new 术数(ele, 阴阳.阳), new 术数(e, 阴阳.阳) });
				newEleWeights[e.ordinal()] = ws[1];
				// o = ws[1] - newEleWeights[e.ordinal()];
				// newEleWeights[e.ordinal()] += o * (1 - 干数.克客失气);
				// newEleWeights[host.ordinal()] -= o * 干数.克主耗气;

				e = host.生();
				ws[1] = newEleWeights[e.ordinal()];
				ws = 干数.生克(ws, new 术数[] { new 术数(ele, 阴阳.阳), new 术数(e, 阴阳.阳) });
				newEleWeights[e.ordinal()] = ws[1];
				// o = ws[1] - newEleWeights[e.ordinal()];
				// newEleWeights[e.ordinal()] += o * (1 + 干数.生客得气);
				// newEleWeights[host.ordinal()] -= o * 干数.生主泄气;

				e = host;
				ws[1] = newEleWeights[e.ordinal()];
				ws = 干数.生克(ws, new 术数[] { new 术数(ele, 阴阳.阳), new 术数(e, 阴阳.阳) });
				newEleWeights[e.ordinal()] = ws[1];

				e = host.印();
				ws[1] = newEleWeights[e.ordinal()];
				ws = 干数.生克(ws, new 术数[] { new 术数(ele, 阴阳.阳), new 术数(e, 阴阳.阳) });
				newEleWeights[e.ordinal()] = ws[1];
				// o = ws[1] - newEleWeights[e.ordinal()];
				// newEleWeights[e.ordinal()] += o * (1 - 干数.生主泄气);
				// newEleWeights[host.ordinal()] += o * 干数.生客得气;

				// newEleWeights[ele.ordinal()] += ws[0];
				deviations[ele.ordinal()] = getStandardDeviation(newEleWeights);
			}
			float deviation = getStandardDeviation(eleWeights);

			五行 忌神 = null;
			List<五行> eles = new ArrayList<五行>();
			for (int i = 0; i < 5; ++i) {
				float minDeviation = Float.MAX_VALUE;
				int idx = -1;
				for (int j = 0; j < 5; ++j) {
					if (deviations[j] < minDeviation) {
						idx = j;
						minDeviation = deviations[j];
					}
				}
				if (minDeviation < deviation) {
					eles.add(五行.values()[idx]);
				} else {
					if (eles.size() == 0)
						eles.add(五行.values()[idx]);
					else
						忌神 = 五行.values()[idx];
				}
				deviations[idx] = Float.MAX_VALUE;
			}
			用神 = new 喜用神((五行[]) eles.toArray(new 五行[eles.size()]));
			用神.忌神(忌神);
		}
		return 用神;
	}

	private float[] 盘五行() {
		float[] eleWeights = pan.支五行();
		for (int i = 0; i < pan.柱数.length; ++i) {
			干数 gan = pan.柱数[i].干数();
			eleWeights[gan.术数().五行().ordinal()] += gan.getWeight();
		}

		return eleWeights;
	}

	private float getSum(float[] weights) {
		float sum = 0;
		for (float w : weights) {
			sum += w;
		}

		return sum;
	}

	private float getAverage(float[] weights) {
		return getSum(weights) / weights.length;
	}

	private float getStandardDeviation(float[] weights) {
		return getDeviation(weights, getAverage(weights));
	}

	private float getDeviation(float[] weights, float average) {
		float deviation = 0;
		for (float w : weights) {
			deviation += (w - average) * (w - average);
		}
		deviation /= weights.length;

		return (float) Math.sqrt(deviation);
	}

	public void 观盘(盘 pan) {
		System.out.println("== 盘 ==");
		int start = 盘.年序, end = pan.柱数().length;// 盘.时序 + 1;
		if (Consts._DEBUG) {
			start = 0;
			end = pan.柱数().length;
		}
		for (int i = start; i < end; ++i) {
			干数 x = pan.柱数()[i].干数();

			System.out.print(String.format("\t%s\t%s(%s):\t%s%s %f\t%s 支藏 :",
					pan.柱数()[i].干支().纳音(),

					x.天干(), x.术数().五行(), i == pan.日序() ? "(主)" : x.神(),
					i == pan.日序() ? "　　" : 喜用神(x), x.getWeight(),

					pan.柱数()[i].支数().地支()));

			for (干数 y : pan.柱数()[i].支数().藏干数()) {
				System.out.print(String.format("\t- %s\t%s%s\t%f", y.术数().五行(),
						y.神(), 喜用神(y), y.getWeight()));
			}
			System.out.println();
		}
	}

	private String 喜用神(干数 x) {
		return (x.术数().五行().equals(用神().用神()) ? "(用)" : x.术数().五行()
				.equals(用神().喜神()) ? "(喜)"
				: x.术数().五行().equals(用神().忌神()) ? "(忌)" : "　　");
	}

	public Map<String, String> 八字() {
		Map<String, String> result = new HashMap<String, String>();
		StringBuilder outside = new StringBuilder(), inside = new StringBuilder(), sickness = new StringBuilder(), work = new StringBuilder();

		float[] eleWeights = pan.五行局面();
		inside.append(String.format("%s\n", pan.心性().心性()));

		// 阴阳互通而五行易失和
		// 五行最强显于外，日主强弱为内，五行不平衡影响即为健康隐患
		五行 ele = null;
		float deviation = getStandardDeviation(eleWeights), average = getAverage(eleWeights);
		for (int i = 0; i < eleWeights.length; ++i) {
			if (eleWeights[i] == 0)
				continue;
			if (Math.abs(eleWeights[i] - average) < deviation)
				continue;

			ele = 五行.values()[i];
			inside.append(String.format("%s\n",
					(eleWeights[i] > average) ? ele.盛者人事() : ele.衰者人事()));
			outside.append(String.format("%s\n",
					(eleWeights[i] > average) ? ele.盛者外貌() : ele.衰者外貌()));

			天干 x = 天干.lookup(ele, 阴阳.阴), y = 天干.lookup(ele, 阴阳.阳);
			sickness.append(String.format("%s、%s容易病变，日常注意%s、%s的不适和变化。\n",
					x.脏腑(), y.脏腑(), x.人体(), y.人体()));
		}

		ele = 用神().用神();
		work.append(String.format("宜往%s的方向定居，从事如下方面的职业: %s\n", ele.方位(),
				ele.职业()));
		ele = 用神().喜神();
		work.append(String.format("或者往%s的方向定居，从事如下方面的职业: %s\n", ele.方位(),
				ele.职业()));
		ele = 用神().忌神();
		work.append(String.format("注意不宜往%s的方向定居，从事如下方面的职业: %s\n", ele.方位(),
				ele.职业()));

		result.put("心性", inside.toString());
		result.put("外貌", outside.toString());
		result.put("健康", sickness.toString());
		result.put("家宅工作", work.toString());

		return result;
	}

	public void 综合命评() throws Exception {
		Map<String, String> result = 八字();

		System.out.println("== 心性 ==");
		System.out.println(result.get("心性"));

		System.out.println("== 外貌 ==");
		System.out.println(result.get("外貌"));

		System.out.println("== 健康 ==");
		System.out.println(result.get("健康"));

		System.out.println("== 家宅工作 ==");
		System.out.println(result.get("家宅工作"));
	}

	public void 流年行运数() throws Exception {
		timespan = new ArrayList<Date[]>();
		宫 = new HashMap<String, List<Float>>();
		for (int i = 盘.年序; i <= 盘.时序; ++i) {
			宫.put(pan.宫名(i, true), new ArrayList<Float>());
			宫.put(pan.宫名(i, false), new ArrayList<Float>());
		}
		神 = new HashMap<十神, List<Float>>();
		for (十神 key : 十神.values()) {
			神.put(key, new ArrayList<Float>());
		}

		流年行运(命运.起运交脱(birthday, 乾造, location), 命运.大运(birthday, 乾造, location),
				timespan, 宫, 神);
	}

	private void 流年行运(Date 运, 干支[] 大运, List<Date[]> timespan,
			Map<String, List<Float>> 宫, Map<十神, List<Float>> 神)
			throws Exception {
		LunarDate ld = 农历.TimeToLunar(运);
		Date d1, d2 = 运, d3, now = new Date();
		for (int i = 0; i < 大运.length; ++i) {

			int jn = 0;
			d1 = d2;
			d2 = new Date(d2.getYear(), 0, (int) 节令.term(d2.getYear() + 1900,
					3, true));
			if (d2.getTime() > d1.getTime()) {
				流年行运命评(大运[i], d1, d2, jn, timespan, 宫, 神);
				d1 = d2;
			}
			ld.setYear(ld.getYear() + 1);
			for (int j = 0; j < 9; ++j) {
				if (d1.getYear() > now.getYear() + 5)
					return;
				d2 = new Date(d2.getYear() + 1, 0, (int) 节令.term(
						d2.getYear() + 1 + 1900, 3, true));
				流年行运命评(大运[i], d1, d2, jn, timespan, 宫, 神);
				++jn;
				d1 = d2;
				ld.setYear(ld.getYear() + 1);
			}
			d3 = new Date(d2.getYear() + 1, 0, (int) 节令.term(
					d2.getYear() + 1 + 1900, 3, true));
			d2 = 农历.LunarToTime(ld);
			if (d2.getTime() > d3.getTime()) {
				流年行运命评(大运[i], d1, d3, jn, timespan, 宫, 神);
				d1 = d3;
			}
			流年行运命评(大运[i], d1, d2, jn, timespan, 宫, 神);
		}
	}

	private void 流年行运命评(干支 运, Date 始, Date 终, int 大运经年, List<Date[]> timespan,
			Map<String, List<Float>> 宫, Map<十神, List<Float>> 神)
			throws Exception {
		大运流年盘 pan2 = new 大运流年盘(birthday, 乾造, location, 运, 四柱.年柱(始), 大运经年);
		pan2.运盘();
		String time = String.format("\t%s - %s", DateFormat.getDateInstance()
				.format(始), DateFormat.getDateInstance().format(终));
		if (Consts._VERBOSE) {
			System.out.println("---------------------------------------");
			System.out.println(time);
			观盘(pan2);
		}
		timespan.add(new Date[] { 始, 终 });

		Map<十神, Float> _神 = new HashMap<十神, Float>();
		for (int i = 0; i < pan2.柱数().length; ++i) {
			干数 gan = pan2.柱数()[i].干数();
			支数 zhi = pan2.柱数()[i].支数();

			float weight = 0;
			if (_神.containsKey(gan.神()))
				weight = _神.get(gan.神());
			weight += gan.getWeight();
			_神.put(gan.神(), weight);

			for (干数 g : zhi.藏干数()) {
				weight = 0;
				if (_神.containsKey(g.神()))
					weight = _神.get(g.神());
				weight += g.getWeight();
				_神.put(g.神(), weight);
			}

			if (i > 盘.时序)
				continue;
			宫.get(pan2.宫名(i, true)).add(gan.getWeight());
			宫.get(pan2.宫名(i, false)).add(zhi.getWeight());
		}
		for (十神 key : 十神.values()) {
			神.get(key).add(_神.containsKey(key) ? _神.get(key) : 0);
		}
	}

	public void 流年行运() throws Exception {
		流年行运数();
		System.out.println("== 流年行运 ==");
		System.out.println("\t所谓命运，只知以表象而论，如果内心平静，万事看开，又如何？");

		Map<String, String> destiny = 命评(
				(Date[][]) timespan.toArray(new Date[timespan.size()][]), 宫, 神);
		for (String key : destiny.keySet()) {
			System.out.println(String.format("\t=== %s ===", key));
			System.out.println(destiny.get(key));
		}
	}

	private Map<String, String> 命评(Date[][] dates, Map<String, List<Float>> 宫,
			Map<十神, List<Float>> 神) {
		List<Float> weights = 宫.get(pan.宫名(盘.日序, true));
		Float[] 日主序 = (Float[]) weights.toArray(new Float[weights.size()]);

		Map<String, String> destiny = new HashMap<String, String>();
		宫评0(dates, 宫, 日主序, destiny);
		宫评1(dates, 宫, 日主序, destiny);
		宫评2(dates, 宫, 日主序, destiny);
		神评(dates, 神, 日主序, destiny);

		return destiny;
	}

	public Map<String, String> 人事() {
		List<Float> weights = 宫.get(pan.宫名(盘.日序, true));
		Float[] 日主序 = (Float[]) weights.toArray(new Float[weights.size()]);
		Date[][] dates = (Date[][]) timespan
				.toArray(new Date[timespan.size()][]);

		Map<String, String> destiny = new HashMap<String, String>();
		宫评0(dates, 宫, 日主序, destiny);
		宫评1(dates, 宫, 日主序, destiny);
		return destiny;
	}

	public Map<String, String> 时运() {
		List<Float> weights = 宫.get(pan.宫名(盘.日序, true));
		Float[] 日主序 = (Float[]) weights.toArray(new Float[weights.size()]);
		Date[][] dates = (Date[][]) timespan
				.toArray(new Date[timespan.size()][]);

		Map<String, String> destiny = new HashMap<String, String>();
		神评(dates, 神, 日主序, destiny);
		return destiny;
	}

	public Map<String, String> 家人() {
		List<Float> weights = 宫.get(pan.宫名(盘.日序, true));
		Float[] 日主序 = (Float[]) weights.toArray(new Float[weights.size()]);
		Date[][] dates = (Date[][]) timespan
				.toArray(new Date[timespan.size()][]);

		Map<String, String> destiny = new HashMap<String, String>();
		宫评2(dates, 宫, 日主序, destiny);
		return destiny;
	}

	private void 宫评0(Date[][] dates, Map<String, List<Float>> 宫, Float[] 日主序,
			Map<String, String> destiny) {
		String key = null;
		List<Float> weights = null;
		float[] newWeights = null;
		int[] indexes = null;
		StringBuilder res = null;

		Date marriageInLaw = new Date(birthday.getTime());
		marriageInLaw.setYear(marriageInLaw.getYear() + 18);

		key = pan.宫名(盘.日序, false);
		weights = 宫.get(key);
		newWeights = 宫评权重((Float[]) weights.toArray(new Float[weights.size()]),
				pan.柱数()[盘.日序].支数().getWeight(), 日主序);
		if (乾造)
			indexes = getBottoms(newWeights);
		else
			indexes = getTops(newWeights);
		res = new StringBuilder();
		for (int i : indexes) {
			if (dates[i][1].before(marriageInLaw))
				continue;
			res.append(String.format("%s -\t%s\n", DateFormat.getDateInstance()
					.format(dates[i][0]),
					DateFormat.getDateInstance().format(dates[i][1])));
		}
		if (Consts._VERBOSE) {
			for (int i = 0; i < dates.length; ++i) {
				System.out.println(String.format("\t\t%s - %s\t\t%f",
						DateFormat.getDateInstance().format(dates[i][0]),
						DateFormat.getDateInstance().format(dates[i][1]),
						newWeights[i]));
			}
		}
		destiny.put("可能婚期", res.toString());

		key = pan.宫名(盘.时序, true);
		weights = 宫.get(key);
		newWeights = 宫评权重((Float[]) weights.toArray(new Float[weights.size()]),
				pan.柱数()[盘.时序].干数().getWeight(), 日主序);
		if (pan.顺())
			indexes = getTops(newWeights);
		else
			indexes = getBottoms(newWeights);
		res = new StringBuilder();
		for (int i : indexes) {
			if (dates[i][1].before(marriageInLaw))
				continue;
			res.append(String.format("%s -\t%s\n", DateFormat.getDateInstance()
					.format(dates[i][0]),
					DateFormat.getDateInstance().format(dates[i][1])));
		}
		if (Consts._VERBOSE) {
			for (int i = 0; i < dates.length; ++i) {
				System.out.println(String.format("\t\t%s - %s\t\t%f",
						DateFormat.getDateInstance().format(dates[i][0]),
						DateFormat.getDateInstance().format(dates[i][1]),
						newWeights[i]));
			}
		}
		destiny.put(String.format("可能得%s", key), res.toString());

		key = pan.宫名(盘.时序, false);
		weights = 宫.get(key);
		newWeights = 宫评权重((Float[]) weights.toArray(new Float[weights.size()]),
				pan.柱数()[盘.时序].支数().getWeight(), 日主序);
		if (pan.顺())
			indexes = getTops(newWeights);
		else
			indexes = getBottoms(newWeights);
		res = new StringBuilder();
		for (int i : indexes) {
			if (dates[i][1].before(marriageInLaw))
				continue;
			res.append(String.format("%s -\t%s\n", DateFormat.getDateInstance()
					.format(dates[i][0]),
					DateFormat.getDateInstance().format(dates[i][1])));
		}
		if (Consts._VERBOSE) {
			for (int i = 0; i < dates.length; ++i) {
				System.out.println(String.format("\t\t%s - %s\t\t%f",
						DateFormat.getDateInstance().format(dates[i][0]),
						DateFormat.getDateInstance().format(dates[i][1]),
						newWeights[i]));
			}
		}
		destiny.put(String.format("可能得%s", key), res.toString());
	}

	private void 宫评1(Date[][] dates, Map<String, List<Float>> 宫, Float[] 日主序,
			Map<String, String> destiny) {
		String key = null;
		List<Float> weights = null;
		float[] newWeights = null;
		int[] indexes = null;
		StringBuilder res = null;

		key = pan.宫名(盘.日序, true);
		weights = 宫.get(key);
		newWeights = 宫评权重((Float[]) weights.toArray(new Float[weights.size()]),
				pan.柱数()[盘.日序].干数().getWeight());
		indexes = getOverflow(newWeights);
		res = new StringBuilder();
		for (int i : indexes) {
			res.append(String.format("%s -\t%s\n", DateFormat.getDateInstance()
					.format(dates[i][0]),
					DateFormat.getDateInstance().format(dates[i][1])));
		}
		if (Consts._VERBOSE) {
			for (int i = 0; i < dates.length; ++i) {
				System.out.println(String.format("\t\t%s - %s\t\t%f",
						DateFormat.getDateInstance().format(dates[i][0]),
						DateFormat.getDateInstance().format(dates[i][1]),
						newWeights[i]));
			}
		}
		destiny.put("可能发生灾劫或巨变", res.toString());
	}

	private void 宫评2(Date[][] dates, Map<String, List<Float>> 宫, Float[] 日主序,
			Map<String, String> destiny) {
		String key = null;
		List<Float> weights = null;
		float[] newWeights = null;
		int[] indexes = null;
		StringBuilder res = null;

		for (int x = 盘.月序; x <= 盘.时序; ++x) {
			for (int y = 0; y < 2; ++y) {
				boolean flag = (y == 0);
				if (x == 盘.日序 && flag)
					continue;
				key = pan.宫名(x, flag);
				weights = 宫.get(key);
				newWeights = 宫评权重((Float[]) weights.toArray(new Float[weights
						.size()]),
						flag ? pan.柱数()[x].干数().getWeight() : pan.柱数()[x].支数()
								.getWeight());
				indexes = getOverflow(newWeights);
				res = new StringBuilder();
				for (int i : indexes) {
					res.append(String.format("%s -\t%s\n", DateFormat
							.getDateInstance().format(dates[i][0]), DateFormat
							.getDateInstance().format(dates[i][1])));
				}
				if (Consts._VERBOSE) {
					for (int i = 0; i < dates.length; ++i) {
						System.out.println(String.format("\t\t%s - %s\t\t%f",
								DateFormat.getDateInstance()
										.format(dates[i][0]), DateFormat
										.getDateInstance().format(dates[i][1]),
								newWeights[i]));
					}
				}
				destiny.put(key, res.toString());
			}
		}
	}

	private float[] 宫评权重(Float[] weights, float baseWeight) {
		return 宫评权重(weights, baseWeight, null);
	}

	private float[] 宫评权重(Float[] weights, float baseWeight, Float[] 日主序) {
		if (weights.length == 0)
			return new float[0];

		float[] newWeights = new float[weights.length];

		for (int i = 0; i < weights.length; ++i) {
			float w = weights[i];

			if (日主序 != null) {
				w = w * pan.柱数()[盘.日序].干数().getWeight() / 日主序[i];
			}
			newWeights[i] = w / baseWeight;

			// if (newWeights[i] > 2)
			// newWeights[i] = 2;
		}

		return newWeights;
	}

	private void 神评(Date[][] dates, Map<十神, List<Float>> 神, Float[] 日主序,
			Map<String, String> destiny) {
		StringBuilder res = null;
		for (十神 _神 : 神.keySet()) {
			if (十神.NA.equals(_神))
				continue;

			List<Float> weights = 神.get(_神);
			if (weights.isEmpty())
				continue;

			float[] newWeights = new float[weights.size()];
			for (int i = 0; i < dates.length; ++i) {
				newWeights[i] = weights.get(i) / 日主序[i];
			}

			res = new StringBuilder();

			float average = getAverage(newWeights);
			int[] indexes = getOverflow(newWeights);
			for (int i : indexes) {
				if (newWeights[i] == 0)
					continue;
				String 评 = newWeights[i] > average ? _神.旺() : _神.衰();
				if ("".equals(评))
					continue;
				res.append(String.format("%s -\t%s :\t%s\n", DateFormat
						.getDateInstance().format(dates[i][0]), DateFormat
						.getDateInstance().format(dates[i][1]), 评));
			}
			if (Consts._VERBOSE) {
				for (int i = 0; i < dates.length; ++i) {
					System.out.println(String.format("\t\t%s - %s\t\t%f",
							DateFormat.getDateInstance().format(dates[i][0]),
							DateFormat.getDateInstance().format(dates[i][1]),
							newWeights[i]));
				}
			}
			destiny.put(String.format("%s", _神.指事()), res.toString());
		}
	}

	// private static final float _1sigma = .6526f;
	// private static final float _2sigma = .9544f;
	// private static final float _3sigma = .9974f;
	private static final int _maxSum = 7;

	private int[] sort(Set<Integer> arr) {
		Integer[] ar = (Integer[]) arr.toArray(new Integer[arr.size()]);
		int[] ret = new int[ar.length];
		for (int i = ar.length - 1; i >= 0; --i) {
			int id = i;
			for (int j = 0; j < i; ++j) {
				if (ar[j] > ar[id])
					id = j;
			}
			ret[i] = ar[id];
			ar[id] = ar[i];
		}
		return ret;
	}

	private int[] getOverflow(float[] weights) {
		float[] newWeights = Arrays2.copyOf(weights, weights.length);
		float deviation = getStandardDeviation(newWeights), average = getAverage(newWeights);
		List<Integer> exts = new ArrayList<Integer>();
		for (int i = 1; i < newWeights.length; ++i) {
			if (Math.abs(newWeights[i - 1] - average) < deviation
					&& Math.abs(newWeights[i] - average) < deviation)
				continue;
			if (Math.max(newWeights[i], newWeights[i - 1])
					/ Math.min(newWeights[i], newWeights[i - 1]) < 2)
				continue;
			exts.add(i);
			// if (Math.abs(newWeights[i] - average) < deviation)
			// continue;
			//
			// if ((newWeights[i - 1] < newWeights[i] && newWeights[i] >
			// newWeights[i + 1])
			// || (newWeights[i - 1] > newWeights[i] && newWeights[i] <
			// newWeights[i + 1]))
			// exts.add(i);
		}

		int[] ret = new int[exts.size()];
		for (int i = exts.size() - 1; i >= 0; --i) {
			ret[i] = exts.get(i);
		}
		return ret;
	}

	private int[] getTops(float[] weights) {
		float[] newWeights = Arrays2.copyOf(weights, weights.length);
		for (int i = 0; i < newWeights.length; ++i) {
			if (newWeights[i] > 2)
				newWeights[i] = 2;
		}
		float deviation = getDeviation(newWeights, 1), valve = (deviation > .5f) ? .5f
				: deviation;
		Set<Integer> tops = new HashSet<Integer>();
		for (int i = 1; i < newWeights.length - 1; ++i) {
			if (newWeights[i - 1] < newWeights[i]
					&& newWeights[i] > newWeights[i + 1]) {
				if (Math.abs(newWeights[i] - 1) > valve)
					continue;
				tops.add(i);
			}
		}

		float[] sort = Arrays2.copyOf(weights, weights.length);
		for (int i = sort.length - 1; i > 0; --i) {
			float max = sort[i];
			for (int j = 0; j < i; ++j) {
				if (max < sort[j]) {
					max = sort[j];
					sort[j] = sort[i];
					sort[i] = max;
				}
			}
		}
		for (int x = sort.length - 1, sum = 0; x >= 0 && sum < _maxSum; --x) {
			if (sort[x] == 2)
				continue;
			for (int i = 1; i < weights.length - 1; ++i) {
				if (sort[x] == weights[i]) {
					if (weights[i - 1] < weights[i]
							&& weights[i] > weights[i + 1]) {
						tops.add(i);
						++sum;
						break;
					}
				}
			}
		}

		// for (int i = 0; i < newWeights.length; ++i) {
		// if (newWeights[i] < (1 + deviation))
		// newWeights[i] = 2;
		// }
		// valve = getDeviation(newWeights, 2);
		// for (int i = 1; i < newWeights.length - 1; ++i) {
		// if (newWeights[i] == 2)
		// continue;
		// if ((newWeights[i - 1] == 2 || newWeights[i - 1] < newWeights[i])
		// && (newWeights[i] > newWeights[i + 1] || newWeights[i + 1] == 2)) {
		// if (Math.abs(newWeights[i] - 2) > valve)
		// continue;
		// tops.add(i);
		// }
		// }

		return sort(tops);
	}

	private int[] getBottoms(float[] weights) {
		float[] newWeights = Arrays2.copyOf(weights, weights.length);
		for (int i = 0; i < newWeights.length; ++i) {
			if (newWeights[i] > 2)
				newWeights[i] = 2;
		}
		float deviation = getDeviation(newWeights, 1), valve = (deviation > .5f) ? .5f
				: deviation;
		Set<Integer> bottoms = new HashSet<Integer>();
		for (int i = 1; i < newWeights.length - 1; ++i) {
			if (newWeights[i - 1] > newWeights[i]
					&& newWeights[i] < newWeights[i + 1]) {
				if (Math.abs(newWeights[i] - 1) > valve)
					continue;
				bottoms.add(i);
			}
		}

		float[] sort = Arrays2.copyOf(weights, weights.length);
		for (int i = sort.length - 1; i > 0; --i) {
			float min = sort[i];
			for (int j = 0; j < i; ++j) {
				if (min > sort[j]) {
					min = sort[j];
					sort[j] = sort[i];
					sort[i] = min;
				}
			}
		}
		for (int x = sort.length - 1, sum = 0; x >= 0 && sum < _maxSum; --x) {
			if (sort[x] == 0)
				continue;
			for (int i = 1; i < weights.length - 1; ++i) {
				if (sort[x] == weights[i]) {
					if (weights[i - 1] > weights[i]
							&& weights[i] < weights[i + 1]) {
						bottoms.add(i);
						++sum;
						break;
					}
				}
			}
		}

		// for (int i = 0; i < newWeights.length; ++i) {
		// if (newWeights[i] > (1 - deviation))
		// newWeights[i] = 0;
		// }
		// valve = getDeviation(newWeights, 0);
		// for (int i = 1; i < newWeights.length - 1; ++i) {
		// if (newWeights[i] == 0)
		// continue;
		// if ((newWeights[i - 1] == 0 || newWeights[i - 1] > newWeights[i])
		// && (newWeights[i] < newWeights[i + 1] || newWeights[i + 1] == 0)) {
		// if (Math.abs(newWeights[i] - 0) > valve)
		// continue;
		// bottoms.add(i);
		// }
		// }

		return sort(bottoms);
	}
}
