package com.infoecos.cn.destiny.lib;

public enum 干支局 {
	天干五合,

	三会局, 三合局, 生地半合, 墓地半合, 非旺半合, 地支六合,

	冲, 刑, 害;

	private boolean 干局 = false;
	private boolean 合化局 = false;

	public boolean 干局() {
		return 干局;
	}

	public boolean 合化局() {
		return 合化局;
	}

	static {
		天干五合.干局 = true;

		天干五合.合化局 = true;
		地支六合.合化局 = true;
	}
}
