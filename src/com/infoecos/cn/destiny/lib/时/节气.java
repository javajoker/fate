package com.infoecos.cn.destiny.lib.时;

import java.util.Date;

public class 节气 {
	// 节气的时间。
	private Date solarTermDate;
	// 节气名。
	private String name;

	public Date getSolarTermDate() {
		return solarTermDate;
	}

	public void setSolarTermDate(Date solarTermDate) {
		this.solarTermDate = solarTermDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
