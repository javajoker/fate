package com.infoecos.cn.destiny.lib;

import java.util.HashMap;
import java.util.Map;

import com.infoecos.cn.destiny.lib.五行.五行;
import com.infoecos.cn.destiny.lib.干.天干;
import com.infoecos.cn.destiny.lib.支.地支;

/**
 * 获取指定数的纳音五行。
 * 
 * 六十甲子和五音十二律结合起来，其中一律含五音，总数共为六十的“纳音五行”。
 * 
 * 音声流转，金传火，火传木，木传水，水传土
 * 
 * 甲戌乙亥山头火 丙申丁酉山下火 戊午己未天上火 庚辰辛巳白腊金 壬寅癸卯金箔金 甲子乙丑海中金
 * 
 * 丙戌丁亥屋上土 戊申己酉大驿土 庚午辛未路旁土 壬辰癸巳长流水 甲寅乙卯大溪水 丙子丁丑涧下水
 * 
 * 戊戌己亥平地木 庚申辛酉石榴木 壬午癸未杨柳木 甲辰乙巳佛灯火 丙寅丁卯炉中火 戊子己丑霹雳火
 * 
 * 庚戌辛亥钗钏金 壬申癸酉剑锋金 甲午乙未沙中金 丙辰丁巳沙中土 戊寅己卯城头土 庚子辛丑壁上土
 * 
 * 壬戌癸亥大海水 甲申乙酉泉中水 丙午丁未天河水 戊辰己巳大林木 庚寅辛卯松柏木 壬子癸丑桑枝木
 * 
 * @author dch
 * 
 */
public class 干支 {
	private static Map<Integer, 干支> stems = new HashMap<Integer, 干支>();

	/**
	 * 返回甲子数x对应的干支
	 * 
	 * @param x
	 * @return
	 */
	public static 干支 lookup(int x) {
		x = x % 60;
		return stems.get(x < 0 ? x + 60 : x);
	}

	/**
	 * 返回对应的干支
	 * 
	 * @return
	 */
	public static 干支 lookup(天干 i, 地支 j) {
		for (int y = 0; y < 6; ++y) {
			int a = 12 * y + j.术数().getIndex() - i.术数().getIndex();
			if (a % 10 == 0) {
				int x = a + i.术数().getIndex();
				x = x % 60;
				return stems.get(x < 0 ? x + 60 : x);
			}
		}
		return null;
	}

	private 术数 shushu;

	private String modifier;
	private 天干 干;
	private 地支 支;

	private 干支(int value, 五行 element, String 形容) {
		this.modifier = 形容;
		干 = 天干.lookup(value);
		支 = 地支.lookup(value);

		value = value % 60;
		shushu = new 术数(value, element);
	}

	public 术数 术数() {
		return shushu;
	}

	public static String[] 纳音 = {
			// 1
			"海中金", "炉中火", "大林木", "路旁土", "剑锋金", "山头火",
			// 2
			"涧下水", "城墙土", "白腊金", "杨柳木", "泉中水", "屋上土",
			// 3
			"霹雷火", "松柏木", "常流水", "沙中金", "山下火", "平地木",
			// 4
			"壁上土", "金箔金", "佛灯火", "天河水", "大驿土", "钗钏金",
			// 5
			"桑松木", "大溪水", "沙中土", "天上火", "石榴木", "大海水" };

	static {
		for (int i = 0; i < 60; ++i) {
			String s = 纳音[i >> 1];
			五行 wx = null;
			switch (s.charAt(2)) {
			case '金':
				wx = 五行.金;
				break;
			case '水':
				wx = 五行.水;
				break;
			case '火':
				wx = 五行.火;
				break;
			case '土':
				wx = 五行.土;
				break;
			case '木':
				wx = 五行.木;
				break;
			}
			stems.put((i + 1) % 60, new 干支(i + 1, wx, s.substring(0, 2)));
		}
	}

	public 天干 天干() {
		return 干;
	}

	public 地支 地支() {
		return 支;
	}

	public 地支 副支() {
		int x = shushu.getIndex();
		x = (x % 2 == 1) ? x + 1 : x - 1;
		return 地支.lookup(x < 0 ? x + 60 : x);
	}

	public String 形容() {
		return modifier;
	}

	public String 纳音() {
		return modifier + shushu.五行();
	}

	@Override
	public String toString() {
		return 干.toString() + 支.toString();
	}
}
