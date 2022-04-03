package com.infoecos.cn.destiny.lib;

public class 起宫 {
	public static 干支 胎元(干支 月柱) {
		return 干支.lookup(月柱.术数().getIndex() - 9);
	}

	public void 命宫() {
	}
}
