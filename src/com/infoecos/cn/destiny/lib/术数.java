package com.infoecos.cn.destiny.lib;

import com.infoecos.cn.destiny.lib.common.阴阳;
import com.infoecos.cn.destiny.lib.五行.五行;


public class 术数 {
	private int index = -1;

	private 五行 element;
	private 阴阳 yinYang;

	public 术数(五行 element, 阴阳 yinYang) {
		this.element = element;
		this.yinYang = yinYang;
	}

	public 术数(int index, 五行 element) {
		this.index = index;
		this.element = element;
		yinYang = (Math.abs(index % 2) == 1) ? 阴阳.阳 : 阴阳.阴;
	}

	public int getIndex() {
		return index;
	}

	public 五行 五行() {
		return element;
	}

	public 阴阳 阴阳() {
		return yinYang;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof 术数))
			return false;
		术数 shu = (术数) obj;

		boolean ret = element.equals(shu.element)
				&& yinYang.equals(shu.yinYang);
		if (index >= 0 && shu.index >= 0)
			ret = ret && (index == shu.index);

		return ret;
	}
}
