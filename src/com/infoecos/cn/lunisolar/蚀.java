package com.infoecos.cn.lunisolar;

import java.util.Date;

public class 蚀 {
	// 日食月食的时间
	private Date eclipseTime;
	// 日食月食的类型
	private 食 phenomena;
	// 朔望
	private 朔望 syzygy;

	public Date getEclipseTime() {
		return eclipseTime;
	}

	public void setEclipseTime(Date eclipseTime) {
		this.eclipseTime = eclipseTime;
	}

	public 食 getPhenomena() {
		return phenomena;
	}

	public void setPhenomena(食 phenomena) {
		this.phenomena = phenomena;
	}

	public 朔望 getSyzygy() {
		return syzygy;
	}

	public void setSyzygy(朔望 syzygy) {
		this.syzygy = syzygy;
	}
}
