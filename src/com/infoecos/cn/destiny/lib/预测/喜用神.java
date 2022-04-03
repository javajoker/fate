package com.infoecos.cn.destiny.lib.预测;

import com.infoecos.cn.destiny.lib.五行.五行;

public class 喜用神 {
	private 五行[] 用神;
	private 五行 忌神 = null;

	public 喜用神(五行... 用神) {
		if (用神.length > 1)
			this.用神 = 用神;
		else
			this.用神 = new 五行[] { 用神[0], 用神[0].印() };
	}

	public 五行[] 用神集() {
		return 用神;
	}

	public 五行 用神() {
		return 用神[0];
	}

	public 五行 喜神() {
		return 用神[1];
	}

	public 五行 喜神2() {
		if (用神.length > 2)
			return 用神[2];
		else
			return null;
	}

	public void 忌神(五行 忌神) {
		this.忌神 = 忌神;
	}

	public 五行 忌神() {
		return (忌神 == null) ? 喜神().官() : 忌神;
	}

}
