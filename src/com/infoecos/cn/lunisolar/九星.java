package com.infoecos.cn.lunisolar;

import java.util.Date;

public class 九星 {
	private static int[] NKyuusei = new int[] { -1, -1, -1 };

	private static int Jd2Kyuusei(double JD) {
		int flag, b;
		int jD = (int) Math.floor(JD);
		if ((jD < NKyuusei[0]) || (jD >= NKyuusei[0] + NKyuusei[1])) {
			if (GetTenton(jD) < 0)
				return -1;
		}

		if (NKyuusei[2] < 0) {
			flag = -1;
		} else {
			flag = 1;
		}
		b = flag * NKyuusei[2] - 1 + 270;
		b += (jD - NKyuusei[0]) * flag;
		return b % 9;
	}

	private static int GetTenton(int JD) {
		int[] KyuuseiJD = new int[] { 2404030, 2404600, 2404810, 2408800,
				2409010, 2413000, 2413210, 2417200, 2417410, 2421220, 2421400,
				2421610, 2425420, 2425630, 2429620, 2429800, 2430010, 2433820,
				2434030, 2438020, 2438230, 2442220, 2442430, 2446420, 2446630,
				2450620, 2450830, 2454820, 2455030, 2458840, 2459020, 2459230,
				2463250, 2467240, 2467420, 2467630, 2471440, 2471650, 2475640,
				2475850, 2477650 };
		int[] KyuuseiJDF = new int[] { 1, -3, 1, 7, -9, -3, 1, 7, -9, 7, -3, 1,
				-3, 1, 7, -3, 1, -3, 1, 7, -9, -3, 1, 7, -9, -3, 1, 7, -9, 7,
				-3, 1, 1, 7, -3, 1, -3, 1, 7, -9, -9 };
		int KJD = 0, KJDF = 0, n = 0;
		int ne = KyuuseiJD.length;
		if (JD < KyuuseiJD[0])
			return -1;
		if (JD >= KyuuseiJD[ne - 1])
			return -1;

		for (n = 1; n < ne; n++) {
			if (JD < KyuuseiJD[n]) {
				KJD = KyuuseiJD[n - 1];
				KJDF = KyuuseiJDF[n - 1];
				ne = KyuuseiJD[n];
				break;
			}
		}
		do {
			NKyuusei[0] = KJD;
			KJD += 180;
			if (KJD + 61 > ne) {
				KJD = ne;
			}
			if (JD >= KJD) {
				KJDF = (KJDF < 0) ? 1 : -9;
			}
		} while (JD >= KJD);
		NKyuusei[1] = KJD - NKyuusei[0];
		NKyuusei[2] = KJDF;
		return NKyuusei[0];
	}

	/**
	 * 获取九星。
	 * 
	 * @param solarDateTime
	 * @return
	 * @throws Exception
	 */
	public static String 九星串(Date solarDateTime) throws Exception {
		String[] KyuuseiName = new String[] { "一白-太乙星(水)-吉神", "二黒-摄提星(土)-凶神",
				"三碧-轩辕星(木)-安神", "四緑-招摇星(木)-安神", "五黄-天符星(土)-凶神", "六白-青龙星(金)-吉神",
				"七赤-咸池星(金)-凶神", "八白-太阴星(土)-吉神", "九紫-天乙星(火)-吉神" };
		return KyuuseiName[Jd2Kyuusei(LunarUtils.getJulianDay(solarDateTime))];
	}

}
