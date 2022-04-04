package com.infoecos.cn.destiny.lib.预测;

import com.infoecos.cn.destiny.lib.干支;

/**
 * 皆以天干论术数，地支阴阳取藏干法，地支六合化阴阳取天干引
 */
public class 干支数 {
	private 干支 干支;

	private 干数 干数;
	private 支数 支数;

	public 干支数(干支 干支) {
		干数 = new 干数(干支.天干());
		支数 = new 支数(干支.地支());

		this.干支 = 干支;
	}

	public 干支 干支() {
		return 干支;
	}

	public 干数 干数() {
		return 干数;
	}

	public 支数 支数() {
		return 支数;
	}
}
