package com.infoecos.cn.destiny.lib.干;

public enum 六神 {
	青龙, 朱雀, 勾陈, 螣蛇, 白虎, 玄武;
	private String 主;

	public String 主() {
		return 主;
	}

	static {
		青龙.主 = "喜庆";
		朱雀.主 = "口舌官非";
		勾陈.主 = "牢狱";
		螣蛇.主 = "虚惊";
		白虎.主 = "血光丧服";
		玄武.主 = "匪盗暗昧";
	}
}
