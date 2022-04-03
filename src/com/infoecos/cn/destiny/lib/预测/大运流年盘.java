package com.infoecos.cn.destiny.lib.预测;

import java.util.Date;

import com.infoecos.cn.destiny.lib.TimezoneLocation;
import com.infoecos.cn.destiny.lib.四柱;
import com.infoecos.cn.destiny.lib.干支;


public class 大运流年盘 extends 盘 {
	public static final int 大运序 = 时序 + 1;
	public static final int 流年序 = 大运序 + 1;

	public static float 太岁权重 = 140.0f;
	public static float 大运流年干支权和 = 220.0f;
	public static float 大运生发 = 20.0f;

	private int 大运经年;

	protected 大运流年盘(Date solarDateTime, boolean 乾造, TimezoneLocation location, 干支 大运,
			干支 流年, int 大运经年) throws Exception {
		super(solarDateTime, 乾造, location);

		this.大运经年 = 大运经年;

		initialize(new 干支[] { 四柱.年柱(solarDateTime, location),
				四柱.月柱(solarDateTime, location), 四柱.日柱(solarDateTime, location),
				四柱.时柱(solarDateTime, location), 大运, 流年 });
	}

	@Override
	protected int 日序() {
		return 日序;
	}

	@Override
	protected void 配权() {
		柱数[月序].支数().setWeight(四柱盘.月提权重);

		柱数[大运序].干数().setWeight(大运流年干支权和 - (大运经年 + 1) * 大运生发);
		柱数[大运序].支数().setWeight((大运经年 + 1) * 大运生发);

		柱数[流年序].干数().setWeight(大运流年干支权和 - 太岁权重);
		柱数[流年序].支数().setWeight(太岁权重); // 太岁

	}

	@Override
	protected int[][] 柱递进序() {
		return new int[][] { { 大运序, 年序 }, { 大运序, 月序 }, { 大运序, 日序 },
				{ 大运序, 时序 }, { 流年序, 年序 }, { 流年序, 月序 }, { 流年序, 日序 },
				{ 流年序, 时序 }, { 年序, 月序 }, { 年序, 日序 }, { 年序, 时序 }, { 月序, 日序 },
				{ 月序, 时序 }, { 日序, 时序 }, };
	}

	@Override
	protected int getDistance(int x, int y) {
		return (x <= 时序 && y <= 时序) ? Math.abs(x - y) : 1;
	}
}
