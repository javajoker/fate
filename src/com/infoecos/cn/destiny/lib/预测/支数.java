package com.infoecos.cn.destiny.lib.预测;

import com.infoecos.cn.destiny.lib.干.天干;
import com.infoecos.cn.destiny.lib.支.地支;


public class 支数 extends 数 {
	public static float[][] 藏干权重 = { { 1.0f }, { 0.7f, 0.3f },
			{ 0.65f, 0.25f, 0.1f } };
	// public static float[] 藏干权重 = { 0.66f, 0.24f, 0.1f };

	private 地支 地支;
	private 干数[] 藏干数;

	public 支数(地支 地支) {
		this(地支, Consts.基础权重);
	}

	public 支数(地支 地支, float weight) {
		super(地支.术数());

		this.地支 = 地支;

		setWeight(weight);
	}

	public 地支 地支() {
		return 地支;
	}

	public 干数[] 藏干数() {
		return 藏干数;
	}

	@Override
	void setWeight(float weight) {
		int size = 地支.支藏().length;
		藏干数 = new 干数[size];

		int idx = 0;
		for (天干 干 : 地支.支藏()) {
			藏干数[idx] = new 干数(干, (weight * 藏干权重[size - 1][idx]));
			++idx;
		}
		// 藏干数 = new 干数[3];
		//
		// int idx = 0;
		// 天干 last = null;
		// for (天干 干 : 地支.支藏()) {
		// 藏干数[idx] = new 干数(干, (weight * 藏干权重[idx]));
		// ++idx;
		// last = 干;
		// }
		// for (; idx < 3; ++idx) {
		// 藏干数[idx] = new 干数(last, (weight * 藏干权重[idx]));
		// }

		// 支藏亦论生克
		for (int i = 藏干数.length - 1; i > 0; --i) {
			for (int j = i - 1; j >= 0; --j)
				干数.生克(藏干数[i], 藏干数[j]);
		}

		float sum = 0;
		for (int i = 0; i < 藏干数.length; ++i) {
			sum += 藏干数[i].getWeight();
		}
		for (int i = 0; i < 藏干数.length; ++i) {
			藏干数[i].setWeight(藏干数[i].getWeight() * weight / sum);
		}
	}

	@Override
	public float getWeight() {
		float weight = 0;
		for (干数 gan : 藏干数) {
			weight += gan.getWeight();
		}
		return weight;
	}

	@Override
	public String _DEBUG() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s(%s)\t藏干", 地支, 地支.术数().五行()));
		for (干数 x : 藏干数)
			sb.append(String.format("\t: %s(%d)", x.术数().五行(), x.getWeight()));
		return sb.toString();
	}
}
