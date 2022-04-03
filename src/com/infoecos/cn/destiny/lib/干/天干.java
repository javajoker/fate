package com.infoecos.cn.destiny.lib.干;

import java.util.HashMap;
import java.util.Map;

import com.infoecos.cn.destiny.lib.术数;
import com.infoecos.cn.destiny.lib.common.人体;
import com.infoecos.cn.destiny.lib.common.方位;
import com.infoecos.cn.destiny.lib.common.脏腑;
import com.infoecos.cn.destiny.lib.common.阴阳;
import com.infoecos.cn.destiny.lib.五行.五行;


public enum 天干 {
	甲, 乙, 丙, 丁, 戊, 己, 庚, 辛, 壬, 癸;

	private static Map<Integer, 天干> stems = new HashMap<Integer, 天干>();

	/**
	 * 返回甲子数x对应的天干
	 * 
	 * @param x
	 * @return
	 */
	public static 天干 lookup(int x) {
		x = x % 10;
		return stems.get(x < 0 ? x + 10 : x);
	}

	public static 天干 lookup(五行 ele, 阴阳 yinYang) {
		for (天干 干 : stems.values()) {
			if (ele.equals(干.术数().五行()) && yinYang.equals(干.术数().阴阳()))
				return 干;
		}
		return null;
	}

	private 术数 shushu;

	private String key;

	private 方位 position;
	private 人体 body;
	private 脏腑 organ;
	private 六神 god;

	private void initialize(int value, String key, 人体 body, 脏腑 organ, 六神 god) {
		五行 element = null;

		switch ((value - 1) >> 1) {
		case 0:
			element = 五行.木;
			position = 方位.东;
			break;
		case 1:
			element = 五行.火;
			position = 方位.南;
			break;
		case 2:
			element = 五行.土;
			position = 方位.中;
			break;
		case 3:
			element = 五行.金;
			position = 方位.西;
			break;
		case 4:
			element = 五行.水;
			position = 方位.北;
			break;
		}

		this.key = key;
		this.body = body;
		this.organ = organ;
		this.god = god;

		value = value % 10;
		shushu = new 术数(value, element);
		stems.put(value, this);
	}

	static {
		甲.initialize(1, "甲", 人体.头, 脏腑.胆, 六神.青龙);
		乙.initialize(2, "乙", 人体.肩, 脏腑.肝, 六神.青龙);
		丙.initialize(3, "丙", 人体.额, 脏腑.小肠, 六神.朱雀);
		丁.initialize(4, "丁", 人体.齿舌, 脏腑.心, 六神.朱雀);
		戊.initialize(5, "戊", 人体.鼻面, 脏腑.胃, 六神.勾陈);
		己.initialize(6, "己", 人体.鼻面, 脏腑.脾, 六神.螣蛇);
		庚.initialize(7, "庚", 人体.筋, 脏腑.大肠, 六神.白虎);
		辛.initialize(8, "辛", 人体.胸, 脏腑.肺, 六神.白虎);
		壬.initialize(9, "壬", 人体.胫, 脏腑.膀胱, 六神.玄武);
		癸.initialize(10, "癸", 人体.足, 脏腑.肾, 六神.玄武);
	}

	public 术数 术数() {
		return shushu;
	}

	public 方位 方位() {
		return position;
	}

	public 人体 人体() {
		return body;
	}

	public 脏腑 脏腑() {
		return organ;
	}

	public 六神 六神() {
		return god;
	}

	@Override
	public String toString() {
		return key;
	}

	public 五行 五合(天干 x) {
		if ((天干.甲.equals(x) && 天干.己.equals(this))
				|| (天干.甲.equals(this) && 天干.己.equals(x)))
			return 五行.土;
		if ((天干.乙.equals(x) && 天干.庚.equals(this))
				|| (天干.乙.equals(this) && 天干.庚.equals(x)))
			return 五行.金;
		if ((天干.丙.equals(x) && 天干.辛.equals(this))
				|| (天干.丙.equals(this) && 天干.辛.equals(x)))
			return 五行.水;
		if ((天干.丁.equals(x) && 天干.壬.equals(this))
				|| (天干.丁.equals(this) && 天干.壬.equals(x)))
			return 五行.木;
		if ((天干.戊.equals(x) && 天干.癸.equals(this))
				|| (天干.戊.equals(this) && 天干.癸.equals(x)))
			return 五行.火;

		return null;
	}
}
