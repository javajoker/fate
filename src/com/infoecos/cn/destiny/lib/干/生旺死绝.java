package com.infoecos.cn.destiny.lib.干;

import java.util.HashMap;
import java.util.Map;

import com.infoecos.cn.destiny.lib.支.地支;


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
public enum 生旺死绝 {
	长生, 沐浴, 冠带, 临官, 帝旺, 衰, 病, 死, 墓, 绝, 胎, 养;

	private int value = 0;

	public int getValue() {
		return value;
	}

	private static Map<Integer, 生旺死绝> stems = new HashMap<Integer, 生旺死绝>();

	static {
		长生.value = 0;
		沐浴.value = 1;
		冠带.value = 2;
		临官.value = 3;
		帝旺.value = 4;
		衰.value = 5;
		病.value = 6;
		死.value = 7;
		墓.value = 8;
		绝.value = 9;
		胎.value = 10;
		养.value = 11;

		stems.put(长生.value, 长生);
		stems.put(沐浴.value, 沐浴);
		stems.put(冠带.value, 冠带);
		stems.put(临官.value, 临官);
		stems.put(帝旺.value, 帝旺);
		stems.put(衰.value, 衰);
		stems.put(病.value, 病);
		stems.put(死.value, 死);
		stems.put(墓.value, 墓);
		stems.put(绝.value, 绝);
		stems.put(胎.value, 胎);
		stems.put(养.value, 养);
	}

	private static Object[][] 生旺死绝表 = { { 天干.甲, 地支.亥 }, { 天干.乙, 地支.午 },
			{ 天干.丙, 地支.寅 }, { 天干.丁, 地支.酉 }, { 天干.戊, 地支.寅 }, { 天干.己, 地支.酉 },
			{ 天干.庚, 地支.巳 }, { 天干.辛, 地支.子 }, { 天干.壬, 地支.申 }, { 天干.癸, 地支.卯 }, };

	public static 生旺死绝 天干生旺死绝(天干 干, 地支 支) {
		for (int i = 生旺死绝表.length - 1; i >= 0; --i) {
			if (生旺死绝表[i][0].equals(干)) {
				int x = 支.术数().getIndex() - ((地支) 生旺死绝表[i][1]).术数().getIndex();
				if (干.术数().getIndex() % 2 == 0)
					x = -x;
				return stems.get(x < 0 ? x + 12 : x);
			}
		}
		return null;
	}

	public static 地支 寻支(天干 干, 生旺死绝 生旺死绝) {
		int x = ((地支) 生旺死绝表[(干.术数().getIndex() - 1 + 10) % 10][1]).术数()
				.getIndex();
		if (干.术数().getIndex() % 2 == 0)
			x = x - 生旺死绝.value;
		else
			x = x + 生旺死绝.value;
		return 地支.lookup(x);
	}
}
