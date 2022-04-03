package com.infoecos.cn.destiny.lib.支;

import java.util.HashMap;
import java.util.Map;

import com.infoecos.cn.destiny.lib.术数;
import com.infoecos.cn.destiny.lib.common.人体;
import com.infoecos.cn.destiny.lib.common.方位;
import com.infoecos.cn.destiny.lib.common.脏腑;
import com.infoecos.cn.destiny.lib.五行.五行;
import com.infoecos.cn.destiny.lib.干.天干;


public enum 地支 {
	子, 丑, 寅, 卯, 辰, 巳, 午, 未, 申, 酉, 戌, 亥;

	private static Map<Integer, 地支> stems = new HashMap<Integer, 地支>();

	/**
	 * 返回甲子数x对应的地支
	 * 
	 * @param x
	 * @return
	 */
	public static 地支 lookup(int x) {
		x = x % 12;
		return stems.get(x < 0 ? x + 12 : x);
	}

	private 术数 shushu;
	private 天干[] 支藏;

	private String key;

	private 方位 position;
	private 人体 body;
	private 脏腑[] organs;

	private void initialize(int value, String key, 天干[] 支藏, 人体 body, 脏腑[] organs) {
		value = value % 12;

		五行 element = null;

		switch (value / 3) {
		case 0:
			element = 五行.水;
			position = 方位.北;
			break;
		case 1:
			element = 五行.木;
			position = 方位.东;
			break;
		case 2:
			element = 五行.火;
			position = 方位.南;
			break;
		case 3:
			element = 五行.金;
			position = 方位.西;
			break;
		}
		if (value % 3 == 2)
			element = 五行.土;

		this.key = key;
		this.支藏 = 支藏;
		this.body = body;
		this.organs = organs;

		shushu = new 术数(value, element);
		stems.put(value, this);
	}

	static {
		子.initialize(1, "子", new 天干[] { 天干.癸 }, 人体.耳, new 脏腑[] { 脏腑.膀胱, 脏腑.三焦 });
		丑.initialize(2, "丑", new 天干[] { 天干.己, 天干.癸, 天干.辛 }, 人体.胞肚,
				new 脏腑[] { 脏腑.脾 });
		寅.initialize(3, "寅", new 天干[] { 天干.甲, 天干.丙, 天干.戊 }, 人体.手,
				new 脏腑[] { 脏腑.胆 });
		卯.initialize(4, "卯", new 天干[] { 天干.乙 }, 人体.指, new 脏腑[] { 脏腑.肝 });
		辰.initialize(5, "辰", new 天干[] { 天干.戊, 天干.乙, 天干.癸 }, 人体.肩胸,
				new 脏腑[] { 脏腑.胃 });
		巳.initialize(6, "巳", new 天干[] { 天干.丙, 天干.戊, 天干.庚 }, 人体.面咽齿,
				new 脏腑[] { 脏腑.心 });
		午.initialize(7, "午", new 天干[] { 天干.丁, 天干.己 }, 人体.眼, new 脏腑[] { 脏腑.小肠 });
		未.initialize(8, "未", new 天干[] { 天干.己, 天干.丁, 天干.乙 }, 人体.脊梁,
				new 脏腑[] { 脏腑.脾 });
		申.initialize(9, "申", new 天干[] { 天干.庚, 天干.壬, 天干.戊 }, 人体.经络,
				new 脏腑[] { 脏腑.大肠 });
		酉.initialize(10, "酉", new 天干[] { 天干.辛 }, 人体.精血, new 脏腑[] { 脏腑.肺 });
		戌.initialize(11, "戌", new 天干[] { 天干.戊, 天干.辛, 天干.丁 }, 人体.命门腿足,
				new 脏腑[] { 脏腑.胃 });
		亥.initialize(12, "亥", new 天干[] { 天干.壬, 天干.甲 }, 人体.头, new 脏腑[] { 脏腑.肾,
				脏腑.心包 });
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

	public 脏腑[] 脏腑() {
		return organs;
	}

	public 天干[] 支藏() {
		return 支藏;
	}

	@Override
	public String toString() {
		return key;
	}

	public 五行 六合(地支 x) {
		if ((子.equals(x) && 丑.equals(this)) || (子.equals(this) && 丑.equals(x)))
			return 五行.土;
		if ((寅.equals(x) && 亥.equals(this)) || (寅.equals(this) && 亥.equals(x)))
			return 五行.木;
		if ((卯.equals(x) && 戌.equals(this)) || (卯.equals(this) && 戌.equals(x)))
			return 五行.火;
		if ((辰.equals(x) && 酉.equals(this)) || (辰.equals(this) && 酉.equals(x)))
			return 五行.金;
		if ((巳.equals(x) && 申.equals(this)) || (巳.equals(this) && 申.equals(x)))
			return 五行.水;
		if ((午.equals(x) && 未.equals(this)) || (午.equals(this) && 未.equals(x)))
			return 五行.土;

		return null;
	}

	/**
	 * 会一方之气。
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static 五行 三会(地支 x, 地支 y, 地支 z) {
		if (亥.equals(x) || 亥.equals(y) || 亥.equals(z)) {
			if (子.equals(x) || 子.equals(y) || 子.equals(z)) {
				if (丑.equals(x) || 丑.equals(y) || 丑.equals(z))
					return 五行.水;
			}
		} else if (寅.equals(x) || 寅.equals(y) || 寅.equals(z)) {
			if (卯.equals(x) || 卯.equals(y) || 卯.equals(z)) {
				if (辰.equals(x) || 辰.equals(y) || 辰.equals(z))
					return 五行.木;
			}
		} else if (巳.equals(x) || 巳.equals(y) || 巳.equals(z)) {
			if (午.equals(x) || 午.equals(y) || 午.equals(z)) {
				if (未.equals(x) || 未.equals(y) || 未.equals(z))
					return 五行.火;
			}
		} else if (申.equals(x) || 申.equals(y) || 申.equals(z)) {
			if (酉.equals(x) || 酉.equals(y) || 酉.equals(z)) {
				if (戌.equals(x) || 戌.equals(y) || 戌.equals(z))
					return 五行.金;
			}
		}

		return null;
	}

	/**
	 * 生、旺、墓成局。三合局力量次于三会局。
	 * 
	 * 例，水生于申，旺于子，墓于辰，故申子辰合水局
	 * 
	 * 丑辰未戌全，方是土局
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static 五行 三合(地支 x, 地支 y, 地支 z) {
		if (申.equals(x) || 申.equals(y) || 申.equals(z)) {
			if (子.equals(x) || 子.equals(y) || 子.equals(z)) {
				if (辰.equals(x) || 辰.equals(y) || 辰.equals(z))
					return 五行.水;
			}
		} else if (巳.equals(x) || 巳.equals(y) || 巳.equals(z)) {
			if (酉.equals(x) || 酉.equals(y) || 酉.equals(z)) {
				if (丑.equals(x) || 丑.equals(y) || 丑.equals(z))
					return 五行.金;
			}
		} else if (寅.equals(x) || 寅.equals(y) || 寅.equals(z)) {
			if (午.equals(x) || 午.equals(y) || 午.equals(z)) {
				if (戌.equals(x) || 戌.equals(y) || 戌.equals(z))
					return 五行.火;
			}
		} else if (亥.equals(x) || 亥.equals(y) || 亥.equals(z)) {
			if (卯.equals(x) || 卯.equals(y) || 卯.equals(z)) {
				if (未.equals(x) || 未.equals(y) || 未.equals(z))
					return 五行.木;
			}
		}

		return null;
	}

	/**
	 * 三合半合局，力量次于三合局
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static 五行 生地半合(地支 x, 地支 y) {
		if ((卯.equals(x) && 亥.equals(y)) || (卯.equals(y) && 亥.equals(x)))
			return 五行.木;
		if ((巳.equals(x) && 酉.equals(y)) || (巳.equals(y) && 酉.equals(x)))
			return 五行.金;
		if ((子.equals(x) && 申.equals(y)) || (子.equals(y) && 申.equals(x)))
			return 五行.水;
		if ((午.equals(x) && 寅.equals(y)) || (午.equals(y) && 寅.equals(x)))
			return 五行.火;
		return null;
	}

	/**
	 * 三合半合局，力量次于生地半合
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static 五行 墓地半合(地支 x, 地支 y) {
		if ((未.equals(x) && 卯.equals(y)) || (未.equals(y) && 卯.equals(x)))
			return 五行.木;
		if ((丑.equals(x) && 酉.equals(y)) || (丑.equals(y) && 酉.equals(x)))
			return 五行.金;
		if ((子.equals(x) && 辰.equals(y)) || (子.equals(y) && 辰.equals(x)))
			return 五行.水;
		if ((午.equals(x) && 戌.equals(y)) || (午.equals(y) && 戌.equals(x)))
			return 五行.火;
		return null;
	}

	/**
	 * 三合半合局，力量次于生地半合
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static 五行 非旺半合(地支 x, 地支 y) {
		if ((未.equals(x) && 亥.equals(y)) || (未.equals(y) && 亥.equals(x)))
			return 五行.木;
		if ((丑.equals(x) && 巳.equals(y)) || (丑.equals(y) && 巳.equals(x)))
			return 五行.金;
		if ((申.equals(x) && 辰.equals(y)) || (申.equals(y) && 辰.equals(x)))
			return 五行.水;
		if ((寅.equals(x) && 戌.equals(y)) || (寅.equals(y) && 戌.equals(x)))
			return 五行.火;

		return null;
	}
}
