package com.infoecos.cn.destiny.lib.五行;

public enum 五行生克关系 {
	/**
	 * 生我者
	 */
	印绶,
	/**
	 * 我生者
	 */
	食神,
	/**
	 * 克我者
	 */
	官杀,
	/**
	 * 我克者
	 */
	妻财,
	/**
	 * 比肩者
	 */
	比肩;

	private String stands;

	static {
		印绶.stands = "父母";
		食神.stands = "子孙";
		官杀.stands = "官鬼";
		妻财.stands = "妻财";
		比肩.stands = "兄弟";
	}

	public String 别名() {
		return stands;
	}
}
