package com.infoecos.cn.destiny.lib.预测;

import java.util.Date;

import com.infoecos.cn.destiny.lib.TimezoneLocation;
import com.infoecos.cn.destiny.lib.四柱;
import com.infoecos.cn.destiny.lib.干支;
import com.infoecos.cn.destiny.lib.干.十神;


public class 四柱盘 extends 盘 {

	protected 四柱盘(Date solarDateTime, boolean 乾造, TimezoneLocation location)
			throws Exception {
		super(solarDateTime, 乾造, location);

		干支[] 柱 = new 干支[] { 四柱.年柱(solarDateTime, location),
				四柱.月柱(solarDateTime, location), 四柱.日柱(solarDateTime, location),
				四柱.时柱(solarDateTime, location) };

		if (Consts._VERBOSE) {
			System.out.println("== 四柱 ==");
			System.out.println(String.format(
					"\t年 : %s; 月 : %s; 日 : %s; 时 : %s", 柱[年序], 柱[月序], 柱[日序],
					柱[时序]));
		}

		initialize(柱);
	}

	@Override
	protected int 日序() {
		return 日序;
	}

	@Override
	protected void 配权() {
		柱数[月序].支数().setWeight(月提权重);
	}

	@Override
	protected int[][] 柱递进序() {
		return new int[][] { { 年序, 月序 }, { 年序, 日序 }, { 年序, 时序 }, { 月序, 日序 },
				{ 月序, 时序 }, { 日序, 时序 }, };
	}

	@Override
	protected int getDistance(int x, int y) {
		return Math.abs(x - y);
	}

	public float[] 五行局面() {
		float[] eleWeights = new float[5];
		for (int i = 0; i < 柱数.length; ++i) {
			eleWeights[柱数[i].干数().术数().五行().ordinal()] += 柱数[i].干数()
					.getWeight();
			for (干数 gan : 柱数[i].支数().藏干数()) {
				eleWeights[gan.术数().五行().ordinal()] += gan.getWeight();
			}
		}

		return eleWeights;
	}

	public 十神 心性() {
		float[] weights = new float[10];

		for (int i = 0; i < 柱数.length; ++i) {
			for (干数 gan : 柱数[i].支数().藏干数()) {
				weights[gan.神().ordinal()] += gan.getWeight();
			}
			if (i == 日序)
				continue;
			weights[柱数[i].干数().神().ordinal()] += 柱数[i].干数().getWeight();
		}

		float maxWeight = 0;
		int idx = 0;
		for (int i = 0; i < weights.length; ++i) {
			if (weights[i] > maxWeight) {
				maxWeight = weights[i];
				idx = i;
			}
		}

		return 十神.values()[idx];
	}
}
