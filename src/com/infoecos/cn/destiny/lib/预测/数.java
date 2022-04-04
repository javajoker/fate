package com.infoecos.cn.destiny.lib.预测;

import com.infoecos.cn.destiny.lib.术数;

public abstract class 数 {
	protected 术数 术数;

	public 数(术数 术数) {
		this.术数 = 术数;
	}

	public 术数 术数() {
		return 术数;
	}

	abstract void setWeight(float weight);

	public abstract float getWeight();
}
