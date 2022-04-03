package com.infoecos.cn.destiny.lib.支;

public enum 地支相害 {
	子未害, 丑午害, 寅巳害, 卯辰害, 申亥害, 酉戌害;
	/*
	 * 月支害：孤独薄命。女命更甚；日时害：老年残疾。
	 */
	private String message = "";

	public String getMessage() {
		return message;
	}

	static {
		子未害.message = "不能利润骨肉";
		丑午害.message = "逢旺易怒，缺乏忍耐力，坐十二宫弱地，恐有残伤";
		卯辰害.message = "逢旺易怒，缺乏忍耐力，坐十二宫弱地，恐有残伤";
		寅巳害.message = "重金者，疾病缠身";
		酉戌害.message = "重聋哑或头面多恶疮";
	}

	public static 地支相害 害(地支 x, 地支 y) {
		if ((地支.子.equals(x) && 地支.未.equals(y))
				|| (地支.子.equals(y) && 地支.未.equals(x)))
			return 子未害;
		if ((地支.丑.equals(x) && 地支.午.equals(y))
				|| (地支.丑.equals(y) && 地支.午.equals(x)))
			return 丑午害;
		if ((地支.寅.equals(x) && 地支.巳.equals(y))
				|| (地支.寅.equals(y) && 地支.巳.equals(x)))
			return 寅巳害;
		if ((地支.卯.equals(x) && 地支.辰.equals(y))
				|| (地支.卯.equals(y) && 地支.辰.equals(x)))
			return 卯辰害;
		if ((地支.申.equals(x) && 地支.亥.equals(y))
				|| (地支.申.equals(y) && 地支.亥.equals(x)))
			return 申亥害;
		if ((地支.酉.equals(x) && 地支.戌.equals(y))
				|| (地支.酉.equals(y) && 地支.戌.equals(x)))
			return 酉戌害;

		return null;
	}
}
