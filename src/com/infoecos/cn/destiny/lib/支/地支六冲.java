package com.infoecos.cn.destiny.lib.支;

public enum 地支六冲 {
	子午冲, 丑未冲, 寅申冲, 卯酉冲, 辰戌冲, 巳亥冲;
	/*
	 * 年与月支冲：离祖别乡；
	 * 
	 * 年与日支冲：与亲不和；
	 * 
	 * 年与时支冲：与子不和；
	 * 
	 * 年与日月时支冲：性暴躁或易患疾；
	 * 
	 * 日冲月支：犯父母兄弟；
	 * 
	 * 四柱逢冲：多不居父母家；
	 */

	private String message;
	private String realtimeMessage;

	public String getMessage() {
		return message;
	}

	public String getRealtimeMessage() {
		return realtimeMessage;
	}

	static {
		子午冲.message = "一身不安";
		丑未冲.message = "事多阻逆";
		寅申冲.message = "多情且好管闲事";
		卯酉冲.message = "背约失信，忧郁多劳，色情纠纷";
		辰戌冲.message = "克亲伤子寿短";
		巳亥冲.message = "多事、喜助人";

		子午冲.realtimeMessage = "居住地变迁，职业不变";
		丑未冲.realtimeMessage = "居住地不变，职业变动";
		寅申冲.realtimeMessage = "居住地和职业均改变";
		卯酉冲.realtimeMessage = "居住地变迁，职业不变";
		辰戌冲.realtimeMessage = "居住地不变，职业变动";
		巳亥冲.realtimeMessage = "居住地和职业均改变";
	}

	public static 地支六冲 冲(地支 x, 地支 y) {
		if ((地支.子.equals(x) && 地支.午.equals(y))
				|| (地支.子.equals(y) && 地支.午.equals(x)))
			return 子午冲;
		if ((地支.丑.equals(x) && 地支.未.equals(y))
				|| (地支.丑.equals(y) && 地支.未.equals(x)))
			return 丑未冲;
		if ((地支.寅.equals(x) && 地支.申.equals(y))
				|| (地支.寅.equals(y) && 地支.申.equals(x)))
			return 寅申冲;
		if ((地支.卯.equals(x) && 地支.酉.equals(y))
				|| (地支.卯.equals(y) && 地支.酉.equals(x)))
			return 卯酉冲;
		if ((地支.辰.equals(x) && 地支.戌.equals(y))
				|| (地支.辰.equals(y) && 地支.戌.equals(x)))
			return 辰戌冲;
		if ((地支.巳.equals(x) && 地支.亥.equals(y))
				|| (地支.巳.equals(y) && 地支.亥.equals(x)))
			return 巳亥冲;

		return null;
	}
}