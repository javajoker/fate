package com.infoecos.cn.destiny.lib.支;

public enum 地支相刑 {
	无礼刑, 恃势刑, 无恩刑, 自刑;

	private String message = "";

	public String getMessage() {
		return message;
	}

	static {
		无礼刑.message = "缺乏独立自主，行事有始无终，固执已见，常陷困境，且容貌鄙劣，内心险毒。与死绝同柱者。思虑浅薄，重者致疾。生日有此刑，夫妻有疾；生时有此刑，子病弱。四柱有二组自刑者，其凶兆更甚；四柱命佳，反有贵之诱力。";
		恃势刑.message = "恃自己之势，过于猛进，易遭挫折失败。与十二宫中长生、沐浴、冠带、临官、帝旺同柱：精神刚毅。与死、绝同柱：卑屈或多狡猾，常罹疾招灾。女命则孤独。";
		无恩刑.message = "性情冷酷薄义，或遭人陷害及凶事发生。若再坐十二宫死绝者，更甚。女命遇此刑易损孕。";
	}

	public static 地支相刑 刑(地支 x, 地支 y) {
		if ((地支.子.equals(x) && 地支.卯.equals(y))
				|| (地支.子.equals(y) && 地支.卯.equals(x)))
			return 无礼刑;

		if ((地支.寅.equals(x) && 地支.巳.equals(y))
				|| (地支.寅.equals(y) && 地支.巳.equals(x)))
			return 恃势刑;
		if ((地支.巳.equals(x) && 地支.申.equals(y))
				|| (地支.巳.equals(y) && 地支.申.equals(x)))
			return 恃势刑;
		if ((地支.申.equals(x) && 地支.寅.equals(y))
				|| (地支.申.equals(y) && 地支.寅.equals(x)))
			return 恃势刑;

		if ((地支.丑.equals(x) && 地支.戌.equals(y))
				|| (地支.丑.equals(y) && 地支.戌.equals(x)))
			return 无恩刑;
		if ((地支.戌.equals(x) && 地支.未.equals(y))
				|| (地支.戌.equals(y) && 地支.未.equals(x)))
			return 无恩刑;
		if ((地支.未.equals(x) && 地支.丑.equals(y))
				|| (地支.未.equals(y) && 地支.丑.equals(x)))
			return 无恩刑;

		if (地支.辰.equals(x) && 地支.辰.equals(y))
			return 自刑;
		if (地支.午.equals(x) && 地支.午.equals(y))
			return 自刑;
		if (地支.酉.equals(x) && 地支.酉.equals(y))
			return 自刑;
		if (地支.亥.equals(x) && 地支.亥.equals(y))
			return 自刑;

		return null;
	}

}