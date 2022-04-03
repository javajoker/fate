package com.infoecos.cn.lunisolar;

import java.util.Date;

import com.infoecos.cn.destiny.lib.TimezoneLocation;
import com.infoecos.cn.destiny.lib.四柱;
import com.infoecos.cn.destiny.lib.干.天干;
import com.infoecos.cn.destiny.lib.支.地支;
import com.infoecos.cn.destiny.lib.时.节令;
import com.infoecos.cn.destiny.lib.时.节气;


public class 万年历 {
	/**
	 * 获取该农历对象的属相。
	 * 
	 * @param solarDateTime
	 * @return
	 * @throws Exception
	 */
	public static String 属相(Date solarDateTime, TimezoneLocation location)
			throws Exception {
		int idx = 四柱.年柱(solarDateTime, location).地支().术数().getIndex();
		return "猪鼠牛虎兔龙蛇马羊猴鸡狗".substring(idx, idx + 1);
	}

	/**
	 * 西方星座。
	 * 
	 * @param solarDateTime
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String 星座(Date solarDateTime) {
		int constellation = -1;
		int Y = solarDateTime.getMonth() + 1 * 100 + solarDateTime.getDate();
		if (((Y >= 321) && (Y <= 419))) {
			constellation = 0;
		} else if ((Y >= 420) && (Y <= 520)) {
			constellation = 1;
		} else if ((Y >= 521) && (Y <= 620)) {
			constellation = 2;
		} else if ((Y >= 621) && (Y <= 722)) {
			constellation = 3;
		} else if ((Y >= 723) && (Y <= 822)) {
			constellation = 4;
		} else if ((Y >= 823) && (Y <= 922)) {
			constellation = 5;
		} else if ((Y >= 923) && (Y <= 1022)) {
			constellation = 6;
		} else if ((Y >= 1023) && (Y <= 1121)) {
			constellation = 7;
		} else if ((Y >= 1122) && (Y <= 1221)) {
			constellation = 8;
		} else if ((Y >= 1222) || (Y <= 119)) {
			constellation = 9;
		} else if ((Y >= 120) && (Y <= 218)) {
			constellation = 10;
		} else if ((Y >= 219) && (Y <= 320)) {
			constellation = 11;
		}

		String con = "白羊金牛双子巨蟹狮子处女天秤天蝎射手摩羯水瓶双鱼";
		return con.substring(2 * constellation, 2 * constellation + 2) + "座";
	}

	// / <summary>
	// / 获取该农历日日食月食情况。
	// / </summary>
	// / <returns>Eclipse对象，包含该农历日日食月食情况。</returns>
	@SuppressWarnings("deprecation")
	public 蚀 蚀数(Date solarDateTime, double timeZone) throws Exception {
		蚀 re = new 蚀();
		double t = (solarDateTime.getYear() + 1900 - 1899.5) / 100;
		double ms = Math
				.floor((solarDateTime.getYear() + 1900 - 1900) * 12.3685);
		double rpi = 180 / Math.PI;
		double zone = timeZone; // 时区
		double f0 = LunarUtils.angle(ms, t, 0, 0.75933, 2.172e-4, 1.55e-7)
				+ 0.53058868 * ms - 8.37e-4 * t + zone / 24 + 0.5;
		double fc = 0.1734 - 3.93e-4 * t;
		double j0 = 693595 + 29 * ms;
		double aa0 = LunarUtils.angle(ms, t, 0.08084821133, 359.2242 / rpi,
				0.0000333 / rpi, 0.00000347 / rpi);
		double ab0 = LunarUtils.angle(ms, t, 7.171366127999999e-2,
				306.0253 / rpi, -0.0107306 / rpi, -0.00001236 / rpi);
		double ac0 = LunarUtils.angle(ms, t, 0.08519585128, 21.2964 / rpi,
				0.0016528 / rpi, 0.00000239 / rpi);
		// double leap=0; //闰月数,0则不闰
		double lunD = -1; // 农历日数
		// double shuoD=0; //本阴历月的阴历朔日数
		double shuoT = 0; // 本阴历月的朔时刻
		double wangD = 0; // 本阴历月的望时刻
		double wangT = 0; // 本阴历月的阴历望日数
		double k1 = 0;

		for (double k = -1; k <= 13; k += 0.5) { // k=整数为朔,k=半整数为望
			double aa = aa0 + 0.507984293 * k;
			double ab = ab0 + 6.73377553 * k;
			double ac = ac0 + 6.818486628 * k;
			double f1 = f0 + 1.53058868 * k + fc * Math.sin(aa) - 0.4068
					* Math.sin(ab) + 0.0021 * Math.sin(2 * aa) + 0.0161
					* Math.sin(2 * ab) + 0.0104 * Math.sin(2 * ac) - 0.0074
					* Math.sin(aa - ab) - 0.0051 * Math.sin(aa + ab);
			double j = j0 + 28 * k + f1; // 朔或望的等效标准天数及时刻

			// 记录当前日期的j值
			double lunD0 = LunarUtils.equivalentStandardDay(
					solarDateTime.getYear() + 1900,
					solarDateTime.getMonth() + 1, solarDateTime.getDate())
					- Math.floor(j); // 当前日距朔日的差值
			if (k == Math.floor(k) && lunD0 >= 0 && lunD0 <= 29) {
				k1 = k; // 记录当前时间对应的k值
				shuoT = LunarUtils.tail(j);
				lunD = lunD0 + 1;
			}
			if (k == (k1 + 0.5)) {
				wangT = LunarUtils.tail(j);
				wangD = Math.floor(j)
						- (LunarUtils.equivalentStandardDay(
								solarDateTime.getYear() + 1900,
								solarDateTime.getMonth() + 1,
								solarDateTime.getDate())
								- lunD + 1) + 1;
			}

			// 判断日月食
			re.setPhenomena(食.None);
			if ((lunD == 1 && k == k1) || (lunD == wangD && k == (k1 + 0.5))) {
				if (Math.abs(Math.sin(ac)) <= 0.36) {
					double s = 5.19595 - 0.0048 * Math.cos(aa) + 0.002
							* Math.cos(2 * aa) - 0.3283 * Math.cos(ab) - 0.006
							* Math.cos(aa + ab) + 0.0041 * Math.cos(aa - ab);
					double r = 0.207 * Math.sin(aa) + 0.0024 * Math.sin(2 * aa)
							- 0.039 * Math.sin(ab) + 0.0115 * Math.sin(2 * ab)
							- 0.0073 * Math.sin(aa + ab) - 0.0067
							* Math.sin(aa - ab) + 0.0117 * Math.sin(2 * ac);
					double p = Math.abs(s * Math.sin(ac) + r * Math.cos(ac));
					double q = 0.0059 + 0.0046 * Math.cos(aa) - 0.0182
							* Math.cos(ab) + 0.0004 * Math.cos(2 * ab) - 0.0005
							* Math.cos(aa + ab);
					if (p - q <= 1.5572) {
						re.setPhenomena(食.EclipseOfSun); // 日食
						if (k != Math.floor(k)) {
							if (p + q >= 1.0129)
								re.setPhenomena(食.PartialEclipseOfTheMoon); // 月偏食
							else
								re.setPhenomena(食.CompleteEclipseOfTheMoon); // 月全食
						}
					}
				}
			}
		}
		// k循环结束

		re.setSyzygy(朔望.None);
		if (lunD == 1) {
			re.setSyzygy(朔望.NewMoon);
			int h = (int) Math.floor(shuoT * 24);
			int min = (int) Math.floor((shuoT * 24 - h) * 60);
			re.setEclipseTime(new Date(solarDateTime.getYear(), solarDateTime
					.getMonth(), solarDateTime.getDate(), h, min, 0)); // 朔日则返回朔的时刻
		}
		if (lunD == wangD) {
			re.setSyzygy(朔望.FullMoon);
			int h = (int) Math.floor(wangT * 24);
			int min = (int) Math.floor((wangT * 24 - h) * 60);
			re.setEclipseTime(new Date(solarDateTime.getYear(), solarDateTime
					.getMonth(), solarDateTime.getDate(), h, min, 0)); // 望日则返回望的时刻
		}

		return re;
	}

	/**
	 * 返回农历日对应的字符串
	 * 
	 * @param v
	 * @return
	 */
	public static String ToStringWithChineseDay(int v) {
		String str = "十一二三四五六七八九初十廿三";
		int idx = (int) Math.floor((double) v / 10) + 10, idx2 = v % 10;
		String vstr = str.substring(idx, idx + 1)
				+ str.substring(idx2, idx2 + 1);
		if (v == 10)
			vstr = "初十";
		return vstr;
	}

	/**
	 * 获取指定时间的月代名词。
	 * 
	 * 一年分为春夏秋冬四时。
	 * 
	 * 后来又按夏历正月、二月、三月等十二个月分为
	 * 
	 * 孟春、仲春、季春、孟夏、仲夏、季夏、孟秋、仲秋、季秋、孟冬、仲冬、季冬。
	 * 
	 * 古书常把这些名称作为月份的代名词。
	 * 
	 * @param solarDateTime
	 * @return
	 * @throws Exception
	 */
	public static String GetChineseMonthPronoun(Date solarDateTime)
			throws Exception {
		String[] s = new String[] { "孟春", "仲春", "季春", "孟夏", "仲夏", "季夏", "孟秋",
				"仲秋", "季秋", "孟冬", "仲冬", "季冬" };
		return s[农历.GetMonth(solarDateTime) - 1];
	}

	/**
	 * 获取指定时间的七十二候。
	 * 
	 * 五天一候，一年365天（平年）为73候，为与24节气对应。
	 * 
	 * 规定三候为一节（气）、一年为72候。
	 * 
	 * 每一候均以一种物候现象作相应，叫“候应”。
	 * 
	 * 72候的“候”应包括非生物和生物两大类，前者如“水始涸”、“东风解冻”、“虹始见”、“地始冻”等；
	 * 
	 * 后者有动物和植物，如“鸿雁来”、“虎始交”、“萍始生”、“苦菜秀”、“桃始华”等。
	 * 
	 * 七十二候的起源很早，对农事活动曾起过一定作用。
	 * 
	 * 虽然其中有些物候描述不那么准确，其中还有不科学成份，但对于了解古代华北地区的气候及其变迁，仍然具有一定的参考价值。
	 * 
	 * 由于当时确定物候的始见单位较小而气候的实际及地区差别很大，所以很难广泛应用。
	 * 
	 * 现在黄河流域物候现象已发生变化，其他地区的物候更是千差万别，必须不断发展物候学，制定新的自然历，否则一味地机械照搬古书是行不通的。
	 * 
	 * @param solarDateTime
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static String 候(Date solarDateTime) throws Exception {
		String xs1 = "";
		节气[] solar = 节令.节气(solarDateTime);
		int jq = solar[0].getSolarTermDate().getDate();
		int zq = solar[1].getSolarTermDate().getDate();
		int c = solarDateTime.getDate() + 1;
		int month = solarDateTime.getMonth() + 1 - 1;

		if (month == 0) {
			if (c < jq - 1) {
				xs1 = "水泉动";
			} else if (c >= jq - 1 && c <= jq + 3) {
				xs1 = "雁北乡";
			} else if (c > jq + 3 && c <= jq + 8) {
				xs1 = "鹊始巢";
			} else if (c > jq + 8 && c < zq - 1) {
				xs1 = "鳺始鴝";
			} else if (c >= zq - 1 && c <= zq + 3) {
				xs1 = "鸡始乳";
			} else if (c > zq + 3 && c <= zq + 8) {
				xs1 = "征鸟厉疾";
			} else if (c > zq + 8) {
				xs1 = "水泽腹坚";
			}
		}
		if (month == 1) {
			if (c < jq - 1) {
				xs1 = "水泽腹坚";
			} else if (c >= jq - 1 && c <= jq + 3) {
				xs1 = "东风解冻";
			} else if (c > jq + 3 && c <= jq + 8) {
				xs1 = "蛰虫始振";
			} else if (c > jq + 8 && c < zq - 1) {
				xs1 = "鱼上冰";
			} else if (c >= zq - 1 && c <= zq + 3) {
				xs1 = "獭祭鱼";
			} else if (c > zq + 3 && c <= zq + 8) {
				xs1 = "候雁北";
			} else if (c > zq + 8) {
				xs1 = "草木萌动";
			}
		}
		if (month == 2) {
			if (c < jq - 1) {
				xs1 = "草木萌动";
			} else if (c >= jq - 1 && c <= jq + 3) {
				xs1 = "桃始华";
			} else if (c > jq + 3 && c <= jq + 8) {
				xs1 = "仓庚鸣";
			} else if (c > jq + 8 && c < zq - 1) {
				xs1 = "鹰化为鸠";
			} else if (c >= zq - 1 && c <= zq + 3) {
				xs1 = "玄鸟至";
			} else if (c > zq + 3 && c <= zq + 8) {
				xs1 = "雷乃发声";
			} else if (c > zq + 8) {
				xs1 = "始电";
			}
		}
		if (month == 3) {
			if (c < jq - 1) {
				xs1 = "始电";
			} else if (c >= jq - 1 && c <= jq + 3) {
				xs1 = "桐始华";
			} else if (c > jq + 3 && c <= jq + 8) {
				xs1 = "田鼠化为鴽";
			} else if (c > jq + 8 && c < zq - 1) {
				xs1 = "虹始见";
			} else if (c >= zq - 1 && c <= zq + 3) {
				xs1 = "萍始生";
			} else if (c > zq + 3 && c <= zq + 8) {
				xs1 = "鸣鸠拂其羽";
			} else if (c > zq + 8) {
				xs1 = "戴胜降于桑";
			}
		}
		if (month == 4) {
			if (c < jq - 1) {
				xs1 = "戴胜降于桑";
			} else if (c >= jq - 1 && c <= jq + 3) {
				xs1 = "蝼蝈鸣";
			} else if (c > jq + 3 && c <= jq + 8) {
				xs1 = "蚯蚓出";
			} else if (c > jq + 8 && c < zq - 1) {
				xs1 = "王瓜生";
			} else if (c >= zq - 1 && c <= zq + 3) {
				xs1 = "苦菜秀";
			} else if (c > zq + 3 && c <= zq + 8) {
				xs1 = "靡草死";
			} else if (c > zq + 8) {
				xs1 = "麦秋至";
			}
		}
		if (month == 5) {
			if (c < jq - 1) {
				xs1 = "麦秋至";
			} else if (c >= jq - 1 && c <= jq + 3) {
				xs1 = "螳螂生";
			} else if (c > jq + 3 && c <= jq + 8) {
				xs1 = "鵙始鸣";
			} else if (c > jq + 8 && c < zq - 1) {
				xs1 = "反舌无声";
			} else if (c >= zq - 1 && c <= zq + 3) {
				xs1 = "鹿角解";
			} else if (c > zq + 3 && c <= zq + 8) {
				xs1 = "蜩始鸣";
			} else if (c > zq + 8) {
				xs1 = "半夏生";
			}
		}
		if (month == 6) {
			if (c < jq - 1) {
				xs1 = "半夏生";
			} else if (c >= jq - 1 && c <= jq + 3) {
				xs1 = "温风至";
			} else if (c > jq + 3 && c <= jq + 8) {
				xs1 = "蟀蟋居壁";
			} else if (c > jq + 8 && c < zq - 1) {
				xs1 = "鹰如鸷";
			} else if (c >= zq - 1 && c <= zq + 3) {
				xs1 = "腐草为萤";
			} else if (c > zq + 3 && c <= zq + 8) {
				xs1 = "土润溽暑";
			} else if (c > zq + 8) {
				xs1 = "大雨时行";
			}
		}
		if (month == 7) {
			if (c < jq - 1) {
				xs1 = "大雨时行";
			} else if (c >= jq - 1 && c <= jq + 3) {
				xs1 = "凉风至";
			} else if (c > jq + 3 && c <= jq + 8) {
				xs1 = "白露降";
			} else if (c > jq + 8 && c < zq - 1) {
				xs1 = "寒蝉鸣";
			} else if (c >= zq - 1 && c <= zq + 3) {
				xs1 = "鹰乃祭鸟";
			} else if (c > zq + 3 && c <= zq + 8) {
				xs1 = "天地始肃";
			} else if (c > zq + 8) {
				xs1 = "禾乃登";
			}
		}
		if (month == 8) {
			if (c < jq - 1) {
				xs1 = "禾乃登";
			} else if (c >= jq - 1 && c <= jq + 3) {
				xs1 = "鸿雁来";
			} else if (c > jq + 3 && c <= jq + 8) {
				xs1 = "玄鸟归";
			} else if (c > jq + 8 && c < zq - 1) {
				xs1 = "群鸟养羞";
			} else if (c >= zq - 1 && c <= zq + 3) {
				xs1 = "雷乃收声";
			} else if (c > zq + 3 && c <= zq + 8) {
				xs1 = "蛰虫坯户";
			} else if (c > zq + 8) {
				xs1 = "水始涸";
			}
		}
		if (month == 9) {
			if (c < jq - 1) {
				xs1 = "水始涸";
			} else if (c >= jq - 1 && c <= jq + 3) {
				xs1 = "鸿雁来宾";
			} else if (c > jq + 3 && c <= jq + 8) {
				xs1 = "雀入大水为蛤";
			} else if (c > jq + 8 && c < zq - 1) {
				xs1 = "菊有黄花";
			} else if (c >= zq - 1 && c <= zq + 3) {
				xs1 = "豺乃祭兽";
			} else if (c > zq + 3 && c <= zq + 8) {
				xs1 = "草木黄落";
			} else if (c > zq + 8) {
				xs1 = "蛰虫咸俯";
			}
		}
		if (month == 10) {
			if (c < jq - 1) {
				xs1 = "蛰虫咸俯";
			} else if (c >= jq - 1 && c <= jq + 3) {
				xs1 = "水始冰";
			} else if (c > jq + 3 && c <= jq + 8) {
				xs1 = "地始冻";
			} else if (c > jq + 8 && c < zq - 1) {
				xs1 = "雉入大水为蜃";
			} else if (c >= zq - 1 && c <= zq + 3) {
				xs1 = "虹藏不见";
			} else if (c > zq + 3 && c <= zq + 8) {
				xs1 = "天气腾地气降";
			} else if (c > zq + 8) {
				xs1 = "闭塞成冬";
			}
		}
		if (month == 11) {
			if (c < jq - 1) {
				xs1 = "闭塞成冬";
			} else if (c >= jq - 1 && c <= jq + 3) {
				xs1 = "鹖鴠不鸣";
			} else if (c > jq + 3 && c <= jq + 8) {
				xs1 = "虎始交";
			} else if (c > jq + 8 && c < zq - 1) {
				xs1 = "荔挺出";
			} else if (c >= zq - 1 && c <= zq + 3) {
				xs1 = "蚯蚓结";
			} else if (c > zq + 3 && c <= zq + 8) {
				xs1 = "麋鹿解";
			} else if (c > zq + 8) {
				xs1 = "水泉动";
			}
		}
		return xs1;
	}

	/**
	 * 获取当日月相名。
	 * 
	 * @param solarDateTime
	 * @return
	 * @throws Exception
	 */
	public static String 月相名(Date solarDateTime) throws Exception {
		String mnname = "";
		int month = 农历.GetDayOfMonth(solarDateTime);

		if (month >= 24)
			mnname = "有明月";
		if (month <= 14)
			mnname = "宵月";
		if (month <= 7)
			mnname = "夕月";
		if (month == 1)
			mnname = "新(朔)月";
		if (month == 2)
			mnname = "既朔月";
		if (month == 3)
			mnname = "娥眉新月";
		if (month == 4)
			mnname = "娥眉新月";
		if (month == 5)
			mnname = "娥眉月";
		if (month == 7)
			mnname = "上弦月";
		if (month == 8)
			mnname = "上弦月";
		if (month == 9)
			mnname = "九夜月";
		if (month == 13)
			mnname = "渐盈凸月";
		if (month == 14)
			mnname = "小望月";
		if (month == 15)
			mnname = "满(望)月";
		if (month == 16)
			mnname = "既望月";
		if (month == 17)
			mnname = "立待月";
		if (month == 18)
			mnname = "居待月";
		if (month == 19)
			mnname = "寝待月";
		if (month == 20)
			mnname = "更待月";
		if (month == 21)
			mnname = "渐亏凸月";
		if (month == 22)
			mnname = "下弦月";
		if (month == 23)
			mnname = "下弦月";
		if (month == 26)
			mnname = "娥眉残月";
		if (month == 27)
			mnname = "娥眉残月";
		if (month == 28)
			mnname = "残月";
		if (month == 29)
			mnname = "晓月";
		if (month == 30)
			mnname = "晦月";

		return mnname;
	}

	/**
	 * 获取指定时间的日禄。
	 * 
	 * @param solarDateTime
	 * @return
	 */
	public static String 日禄(Date solarDateTime) {
		String dayglk = "寅卯巳午巳午申酉亥子";
		int bsg = 地支.lookup(LD(solarDateTime)).术数().getIndex();
		int bs = 天干.lookup(LD(solarDateTime)).术数().getIndex();

		String dayglus = "";
		if (bsg == 0) {
			dayglus = 天干.lookup(10).toString() + "命进禄";
		} else if (bsg == 2) {
			dayglus = 天干.lookup(1).toString() + "命进禄";
		} else if (bsg == 3) {
			dayglus = 天干.lookup(2).toString() + "命进禄";
		} else if (bsg == 5) {
			dayglus = 天干.lookup(3).toString() + "," + 天干.lookup(5).toString()
					+ "命进禄";
		} else if (bsg == 6) {
			dayglus = 天干.lookup(4).toString() + "," + 天干.lookup(6).toString()
					+ "命进禄";
		} else if (bsg == 8) {
			dayglus = 天干.lookup(7).toString() + "命进禄";
		} else if (bsg == 9) {
			dayglus = 天干.lookup(8).toString() + "命进禄";
		} else if (bsg == 11) {
			dayglus = 天干.lookup(9).toString() + "命进禄";
		} else {
			dayglus = "";
		}

		return dayglk.substring(bs, bs + 1) + "命互禄 " + dayglus;
	}

	/**
	 * 获取吉神方位。
	 * 
	 * @param solarDateTime
	 * @return
	 */
	public static String 吉神(Date solarDateTime) {
		int bs = 天干.lookup(LD(solarDateTime)).术数().getIndex();

		String xs = "", fs = "", cs = "";

		if (bs == 0 || bs == 5) {
			xs = "喜神：东北 ";
		} else if (bs == 1 || bs == 6) {
			xs = "喜神：西北 ";
		} else if (bs == 2 || bs == 7) {
			xs = "喜神：西南 ";
		} else if (bs == 3 || bs == 8) {
			xs = "喜神：正南 ";
		} else if (bs == 4 || bs == 9) {
			xs = "喜神：东南 ";
		}

		if (bs == 0 || bs == 1) {
			fs = "福神：东南 ";
		} else if (bs == 2 || bs == 3) {
			fs = "福神：正东 ";
		} else if (bs == 4) {
			fs = "福神：正北 ";
		} else if (bs == 5) {
			fs = "福神：正南 ";
		} else if (bs == 6 || bs == 7) {
			fs = "福神：西南 ";
		} else if (bs == 8) {
			fs = "福神：西北 ";
		} else if (bs == 9) {
			fs = "福神：正西 ";
		}

		if (bs == 0 || bs == 1) {
			cs = "财神：东北";
		} else if (bs == 2 || bs == 3) {
			cs = "财神：西南";
		} else if (bs == 4 || bs == 5) {
			cs = "财神：正北";
		} else if (bs == 6 || bs == 7) {
			cs = "财神：正东";
		} else if (bs == 8 || bs == 9) {
			cs = "财神：正南";
		}

		return xs + fs + cs;
	}

	/**
	 * 获取彭祖百忌。
	 * 
	 * @param solarDateTime
	 * @return
	 * @throws Exception
	 */
	public static String 彭祖百忌(Date solarDateTime, TimezoneLocation location)
			throws Exception {
		String[] g = new String[] { "甲不开仓 ", "乙不栽植 ", "丙不修灶 ", "丁不剃头 ",
				"戊不受田 ", "己不破券 ", "庚不经络 ", "辛不合酱 ", "壬不泱水 ", "癸不词讼 " };
		String[] z = new String[] { "子不问卜", "丑不冠带", "寅不祭祀", "卯不穿井", "辰不哭泣",
				"巳不远行", "午不苫盖", "未不服药", "申不安床", "酉不会客", "戌不吃犬", "亥不嫁娶" };
		int ceod = 四柱.日柱(solarDateTime, location).术数().getIndex() - 1;
		return g[ceod % 10] + z[ceod % 12];
	}

	/**
	 * 获取相冲。
	 * 
	 * “冲”即地支相冲。
	 * 
	 * 即子午相冲、丑未相冲、寅申相冲、卯酉相冲、辰戌相冲、巳亥相冲。
	 * 
	 * 再把十二地支配以十二属相：
	 * 
	 * 子鼠、丑牛、寅虎、卯兔、辰龙、巳蛇、午马、未羊、申猴、酉鸡、戌狗、亥猪。
	 * 
	 * 于是，凡子日，与午相冲，即为“冲马”；
	 * 
	 * 寅日，与申相冲，即为“冲猴”。
	 * 
	 * 黄历设立此款，是告诉人们，不要选用那些与自己属相相冲的日子。
	 * 
	 * @param solarDateTime
	 * @return
	 */
	public static String 地支相冲(Date solarDateTime) {
		String[] gan = new String[] { "戊", "己", "庚", "辛", "壬", "癸", "甲", "乙",
				"丙", "丁" };
		String[] zhi = new String[] { "午", "未", "申", "酉", "戌", "亥", "子", "丑",
				"寅", "卯", "辰", "巳" };
		String[] animal = new String[] { "鼠", "牛", "虎", "兔", "龙", "蛇", "马",
				"羊", "猴", "鸡", "狗", "猪" };
		String[] animal2 = new String[] { "马", "羊", "猴", "鸡", "狗", "猪", "鼠",
				"牛", "虎", "兔", "龙", "蛇" };

		int dd = 地支.lookup(LD(solarDateTime)).术数().getIndex();
		int d = 天干.lookup(LD(solarDateTime)).术数().getIndex();

		return animal[dd] + "日冲(" + gan[d] + zhi[dd] + ")" + animal2[dd];
	}

	/**
	 * 获取岁煞。
	 * 
	 * 岁煞，常居四季，成为“四季之阴气”，极其狠毒，能游行天上，所理之地不可穿凿、修营和移徙。
	 * 
	 * 不慎而冲犯这，家中子孙六畜将受伤害。
	 * 
	 * 然岁煞巡行的方位却极易寻觅。
	 * 
	 * 子日起正南，向东逆行，一日一位，四日一周，循环往复。
	 * 
	 * @param solarDateTime
	 * @return
	 */
	public static String 岁煞(Date solarDateTime) {
		String[] sfw = new String[] { "南", "东", "北", "西", "南", "东", "北", "西",
				"南", "东", "北", "西" };
		int dd = 地支.lookup(LD(solarDateTime)).术数().getIndex();
		return "岁煞" + sfw[dd];
	}

	/**
	 * 获取星宿。
	 * 
	 * @param solarDateTime
	 * @return
	 * @throws Exception
	 */
	public static String 星宿(Date solarDateTime) throws Exception {
		String[] Sukuyou = new String[] { "东方角木蛟-吉", "东方亢金龙-凶", "东方氐土貉-凶",
				"东方房日兔-吉", "东方心月狐-凶", "东方尾火虎-吉", "东方箕水豹-吉", "北方斗木獬-吉",
				"北方牛金牛-凶", "北方女土蝠-凶", "北方虚日鼠-凶", "北方危月燕-凶", "北方室火猪-吉",
				"北方壁水貐-吉", "西方奎木狼-凶", "西方娄金狗-吉", "西方胃土雉-吉", "西方昴日鸡-凶",
				"西方毕月乌-吉", "西方觜火猴-凶", "西方参水猿-凶", "南方井木犴-吉", "南方鬼金羊-凶",
				"南方柳土獐-凶", "南方星日马-凶", "南方张月鹿-吉", "南方翼火蛇-凶", "南方轸水蚓-吉" };
		int s = (int) (LunarUtils.getJulianDay(solarDateTime) + 12) % 28;
		return Sukuyou[s];
	}

	/**
	 * 获取六耀。
	 * 
	 * @param solarDateTime
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String 六耀(Date solarDateTime) {
		String[] Rokuyou = new String[] { "先胜", "友引", "先负", "佛灭", "大安", "赤口" };
		int k = (solarDateTime.getMonth() + 1 + solarDateTime.getDate() + 4) % 6;
		return Rokuyou[k];
	}

	/**
	 * 获取12建星。
	 * 
	 * @param solarDateTime
	 * @return
	 * @throws Exception
	 */
	public static String 建星12(Date solarDateTime) throws Exception {
		String[] jcName0 = new String[] { "建", "除", "满", "平", "定", "执", "破",
				"危", "成", "收", "开", "闭" };
		String[] jcName1 = new String[] { "闭", "建", "除", "满", "平", "定", "执",
				"破", "危", "成", "收", "开" };
		String[] jcName2 = new String[] { "开", "闭", "建", "除", "满", "平", "定",
				"执", "破", "危", "成", "收" };
		String[] jcName3 = new String[] { "收", "开", "闭", "建", "除", "满", "平",
				"定", "执", "破", "危", "成" };
		String[] jcName4 = new String[] { "成", "收", "开", "闭", "建", "除", "满",
				"平", "定", "执", "破", "危" };
		String[] jcName5 = new String[] { "危", "成", "收", "开", "闭", "建", "除",
				"满", "平", "定", "执", "破" };
		String[] jcName6 = new String[] { "破", "危", "成", "收", "开", "闭", "建",
				"除", "满", "平", "定", "执" };
		String[] jcName7 = new String[] { "执", "破", "危", "成", "收", "开", "闭",
				"建", "除", "满", "平", "定" };
		String[] jcName8 = new String[] { "定", "执", "破", "危", "成", "收", "开",
				"闭", "建", "除", "满", "平" };
		String[] jcName9 = new String[] { "平", "定", "执", "破", "危", "成", "收",
				"开", "闭", "建", "除", "满" };
		String[] jcName10 = new String[] { "满", "平", "定", "执", "破", "危", "成",
				"收", "开", "闭", "建", "除" };
		String[] jcName11 = new String[] { "除", "满", "平", "定", "执", "破", "危",
				"成", "收", "开", "闭", "建" };

		int num = 地支.lookup(LM(solarDateTime)).术数().getIndex();
		int num2 = 地支.lookup(LD(solarDateTime)).术数().getIndex();

		if (num == 0)
			return (jcName0[num2]);
		if (num == 1)
			return (jcName1[num2]);
		if (num == 2)
			return (jcName2[num2]);
		if (num == 3)
			return (jcName3[num2]);
		if (num == 4)
			return (jcName4[num2]);
		if (num == 5)
			return (jcName5[num2]);
		if (num == 6)
			return (jcName6[num2]);
		if (num == 7)
			return (jcName7[num2]);
		if (num == 8)
			return (jcName8[num2]);
		if (num == 9)
			return (jcName9[num2]);
		if (num == 10)
			return (jcName10[num2]);
		if (num == 11)
			return (jcName11[num2]);
		return "";
	}

	/**
	 * 获取值日星神
	 * 
	 * @param solarDateTime
	 * @return
	 * @throws Exception
	 */
	public static String 值日星神(Date solarDateTime) throws Exception {
		String[] zrxName1 = new String[] { "青龙(黄道日)", "明堂(黄道日)", "天刑(黑道日)",
				"朱雀(黑道日)", "金匮(黄道日)", "天德(黄道日)", "白虎(黑道日)", "玉堂(黄道日)",
				"天牢(黑道日)", "玄武(黑道日)", "司命(黄道日)", "勾陈(黑道日)" };
		String[] zrxName2 = new String[] { "司命(黄道日)", "勾陈(黑道日)", "青龙(黄道日)",
				"明堂(黄道日)", "天刑(黑道日)", "朱雀(黑道日)", "金匮(黄道日)", "天德(黄道日)",
				"白虎(黑道日)", "玉堂(黄道日)", "天牢(黑道日)", "玄武(黑道日)" };
		String[] zrxName3 = new String[] { "天牢(黑道日)", "玄武(黑道日)", "司命(黄道日)",
				"勾陈(黑道日)", "青龙(黄道日)", "明堂(黄道日)", "天刑(黑道日)", "朱雀(黑道日)",
				"金匮(黄道日)", "天德(黄道日)", "白虎(黑道日)", "玉堂(黄道日)" };
		String[] zrxName4 = new String[] { "白虎(黑道日)", "玉堂(黄道日)", "天牢(黑道日)",
				"玄武(黑道日)", "司命(黄道日)", "勾陈(黑道日)", "青龙(黄道日)", "明堂(黄道日)",
				"天刑(黑道日)", "朱雀(黑道日)", "金匮(黄道日)", "天德(黄道日)" };
		String[] zrxName5 = new String[] { "金匮(黄道日)", "天德(黄道日)", "白虎(黑道日)",
				"玉堂(黄道日)", "天牢(黑道日)", "玄武(黑道日)", "司命(黄道日)", "勾陈(黑道日)",
				"青龙(黄道日)", "明堂(黄道日)", "天刑(黑道日)", "朱雀(黑道日)" };
		String[] zrxName6 = new String[] { "天刑(黑道日)", "朱雀(黑道日)", "金匮(黄道日)",
				"天德(黄道日)", "白虎(黑道日)", "玉堂(黄道日)", "天牢(黑道日)", "玄武(黑道日)",
				"司命(黄道日)", "勾陈(黑道日)", "青龙(黄道日)", "明堂(黄道日)" };
		String[] zrxName7 = new String[] { "青龙(黄道日)", "明堂(黄道日)", "天刑(黑道日)",
				"朱雀(黑道日)", "金匮(黄道日)", "天德(黄道日)", "白虎(黑道日)", "玉堂(黄道日)",
				"天牢(黑道日)", "玄武(黑道日)", "司命(黄道日)", "勾陈(黑道日)" };
		String[] zrxName8 = new String[] { "司命(黄道日)", "勾陈(黑道日)", "青龙(黄道日)",
				"明堂(黄道日)", "天刑(黑道日)", "朱雀(黑道日)", "金匮(黄道日)", "天德(黄道日)",
				"白虎(黑道日)", "玉堂(黄道日)", "天牢(黑道日)", "玄武(黑道日)" };
		String[] zrxName9 = new String[] { "天牢(黑道日)", "玄武(黑道日)", "司命(黄道日)",
				"勾陈(黑道日)", "青龙(黄道日)", "明堂(黄道日)", "天刑(黑道日)", "朱雀(黑道日)",
				"金匮(黄道日)", "天德(黄道日)", "白虎(黑道日)", "玉堂(黄道日)" };
		String[] zrxName10 = new String[] { "白虎(黑道日)", "玉堂(黄道日)", "天牢(黑道日)",
				"玄武(黑道日)", "司命(黄道日)", "勾陈(黑道日)", "青龙(黄道日)", "明堂(黄道日)",
				"天刑(黑道日)", "朱雀(黑道日)", "金匮(黄道日)", "天德(黄道日)" };
		String[] zrxName11 = new String[] { "金匮(黄道日)", "天德(黄道日)", "白虎(黑道日)",
				"玉堂(黄道日)", "天牢(黑道日)", "玄武(黑道日)", "司命(黄道日)", "勾陈(黑道日)",
				"青龙(黄道日)", "明堂(黄道日)", "天刑(黑道日)", "朱雀(黑道日)" };
		String[] zrxName12 = new String[] { "天刑(黑道日)", "朱雀(黑道日)", "金匮(黄道日)",
				"天德(黄道日)", "白虎(黑道日)", "玉堂(黄道日)", "天牢(黑道日)", "玄武(黑道日)",
				"司命(黄道日)", "勾陈(黑道日)", "青龙(黄道日)", "明堂(黄道日)" };

		int num = 地支.lookup(LM(solarDateTime)).术数().getIndex();
		int num2 = 地支.lookup(LD(solarDateTime)).术数().getIndex();

		if (num == 2)
			return (zrxName1[num2]);
		if (num == 3)
			return (zrxName2[num2]);
		if (num == 4)
			return (zrxName3[num2]);
		if (num == 5)
			return (zrxName4[num2]);
		if (num == 6)
			return (zrxName5[num2]);
		if (num == 7)
			return (zrxName6[num2]);
		if (num == 8)
			return (zrxName7[num2]);
		if (num == 9)
			return (zrxName8[num2]);
		if (num == 10)
			return (zrxName9[num2]);
		if (num == 11)
			return (zrxName10[num2]);
		if (num == 0)
			return (zrxName11[num2]);
		if (num == 1)
			return (zrxName12[num2]);

		return "";
	}

	/**
	 * 获取几牛耕田。
	 * 
	 * 每年第一个丑日（丑为牛）在正月初几，就是“几牛耕田”。耕田的牛是多多益善，越多越好
	 * 
	 * @param year
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static String 几牛耕田(int year, TimezoneLocation location) throws Exception {
		int i = 四柱.日柱(new Date(year - 1900, 1 - 1, 1), location).地支().术数()
				.getIndex();// ,
		// netCalendar)));
		int t = (15 - i) % 12;
		String s = ToStringWithChineseDay(t);
		if (t <= 10) {
			s = s.substring(1);
		}
		return s + "牛耕田";
	}

	/**
	 * 获取几龙治水。
	 * 
	 * 是根据每年正月第一个辰日（辰为龙）在第几日决定的。
	 * 
	 * 如在正月初五，就叫“五龙治水”，在初六，就叫“六龙治水”，等等。
	 * 
	 * 据说，龙数越多，雨量越少，龙数越少，雨量就越多。民间自古就有“龙多不下雨”的谚语。
	 * 
	 * @param year
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static String 几龙治水(int year, TimezoneLocation location) throws Exception {
		int i = 四柱.日柱(new Date(year - 1900, 1 - 1, 1), location).地支().术数()
				.getIndex();// ,
		// netCalendar)));
		int t = (18 - i) % 12;
		String s = ToStringWithChineseDay(t);
		if (t <= 10) {
			s = s.substring(1);
		}
		return s + "龙治水";
	}

	@SuppressWarnings("deprecation")
	private static int LD(Date solarDateTime) {
		double dayCyclical = Date.UTC(solarDateTime.getYear(),
				solarDateTime.getMonth(), 1, 0, 0, 0) / 86400000 + 25567 + 9;
		return (int) (dayCyclical + solarDateTime.getDate());
	}

	@SuppressWarnings("deprecation")
	private static int LM(Date solarDateTime) throws Exception {
		int num = (solarDateTime.getYear() + 1900 - 1900) * 12
				+ solarDateTime.getMonth() + 1 + 11;
		if ((solarDateTime.getDate() + 1) >= 节令.节气(solarDateTime)[0]
				.getSolarTermDate().getDate()) {
			num += 1;
		}
		return num;
	}

	/**
	 * 节日纪念日
	 */

	// / <summary>
	// / 获取公历节日字符串。
	// / </summary>
	@SuppressWarnings("deprecation")
	public String GetHoliday(Date solarDateTime) {
		String re = "";
		for (String s : holiday) {
			if (solarDateTime.getMonth() + 1 == Integer.parseInt(s.substring(0,
					2))) {
				if (solarDateTime.getDate() == Integer.parseInt(s.substring(2,
						4))) {
					re = s.substring(4);
					break;
				}
			}
		}
		return re;
	}

	// / <summary>
	// / 获取名人纪念日字符串。
	// / </summary>
	@SuppressWarnings("deprecation")
	public String GetCommemoration(Date solarDateTime) {
		String re = "";
		for (String s : celebrity) {
			if (solarDateTime.getMonth() + 1 == Integer.parseInt(s.substring(0,
					2))) {
				if (solarDateTime.getDate() == Integer.parseInt(s.substring(2,
						4))) {
					re = s.substring(4);
					break;
				}
			}
		}
		return re;
	}

	// / <summary>
	// / 获取中国农历节日字符串。
	// / </summary>
	@SuppressWarnings("deprecation")
	public String GetChineseHoliday(Date solarDateTime, TimezoneLocation location)
			throws Exception {
		String re = "";
		int mon = 农历.GetMonth(solarDateTime);
		if (mon >= 农历.GetLeapMonth(solarDateTime.getYear() + 1900))
			mon -= 1;
		for (String s : chineseHoliday) {
			if (mon == Integer.parseInt(s.substring(0, 2))) {
				if (农历.GetDayOfMonth(solarDateTime) == Integer.parseInt(s
						.substring(2, 4))) {
					re = s.substring(4);
					break;
				}
			}
		}

		// 梅雨
		int y = solarDateTime.getYear() + 1900;
		int thisD0 = 农历.GetDayOfYear(solarDateTime);
		int dG = 四柱.日柱(solarDateTime, location).天干().术数().getIndex();
		int dZ = 四柱.日柱(solarDateTime, location).地支().术数().getIndex();

		int s11 = (int) Math.floor(节令.term(y, 11, true));
		if (thisD0 >= s11 && thisD0 < s11 + 10 && dG == 3)
			re += " 入梅";
		int s13 = (int) Math.floor(节令.term(y, 13, true));
		if (thisD0 >= s13 && thisD0 < s13 + 12 && dZ == 8)
			re += " 出梅";

		// "三伏"是指初伏、中伏和末伏，约在7月中旬到8月中旬这一段时间。夏至以后，虽然白天渐短，黑夜渐长，但是一天当中，白天还比黑夜长，每天地面吸收的热量仍比散发的多，近地面的温度也就一天比一天高。到"三伏"期间，地面吸收的热量几乎少于散发的热量，天气也就最热了。再往后，地面吸收的热量开始少于地面散发的热量，温度也就慢慢下降了。所以一年中最热的时候一般出现在夏至的"三伏"。
		// 从夏至后第三个“庚”日算起，初伏（10天）、中伏（10～20天）、末伏（立秋后的第一个庚日算起，10天），是一年中天气最热的时间。
		// 夏九九歌谣
		// “冬至”数九过冬寒，有的地方也有“夏至”数九过酷暑的歌谣。
		// “夏九九歌”：夏至入头九，羽扇握在手；二九一十八，脱冠着罗纱；三九二十七，出门汗欲滴；四九三十六，浑身汗湿透；五九四十五，炎秋似老虎；六九五十四，乘凉进庙祠；七九六十三，床头摸被单；八九七十二，半夜寻被子；九九八十一，开柜拿棉衣
		// 三伏
		int s12 = (int) Math.floor(节令.term(y, 12, true));
		int s15 = (int) Math.floor(节令.term(y, 15, true));
		int n = (dG - 7) % 10 + 1;
		if (n <= 0)
			n += 10;
		int firsrD0 = thisD0 - n + 1;
		if (firsrD0 >= s12 + 20 && firsrD0 < s12 + 30)
			re += " 初伏第" + n + "天";
		if (firsrD0 >= s15 && firsrD0 < s15 + 10)
			re += " 末伏第" + n + "天";
		else {
			if (firsrD0 >= s12 + 30 && firsrD0 < s12 + 40)
				re += " 中伏第" + n + "天";
			if (firsrD0 >= s12 + 40 && firsrD0 < s12 + 50)
				re += " 中伏第" + (n + 10) + "天";
		}

		// "三九"是指冬至后的第三个九天，约在1月中下旬。"三九"天为什么最冷呢？这要从当时地面吸收和散发热量的多少来看，冬季这时候虽然白昼短，地面吸收的太阳辐射热量最少，但此时地面散发的热量还多于吸收的热量，近地面的空气温度还要继续低下去，当地面吸收到的太阳热量几乎等于地面散发的热量，气温才达到最冷。到"三九"以后，地面吸收的热量又将多于地面散失的热量，近地面的空气温度也随着逐渐回升。因此，一年中最冷的时候一般出现在冬至后的"三九"前后。
		// 冬至这一天开始数九，这就是人们所说的“提冬数九”。数上9天是一九，再数9天是二九……数到“九九”就算“九”尽了，“九尽杨花开”，那时天就暖了。
		// 人说“冷在九、热在伏”，数九虽冷，但由于我国地域辽阔，冷也冷得不一样：
		// 黄河中下游的《九九歌》是：一九二九不出手；三九四 九河上走；五九六九沿河望柳；七九开河，八九雁来；九九又一九，耕牛遍地走。
		// 江南的《九九歌》是：一九二九相见弗出手；三九二十七，篱头吹筚篥(古代的一种乐器，意指寒风吹得篱笆噼噼响声)；四九三十六，夜晚如鹭宿(晚上寒冷象白鹤一样卷曲着身体睡眠)；五九四十五，太阳开门户，六九五十四，贫儿争意气；七九六十三，布袖担头担；八九七十二，猫儿寻阳地；九九八十一，犁耙一齐出。
		// 最冷的是三九、四九，在吉林：三九四九冻死狗，在江苏则是“三九四九拾粪老汉满街游”，可见气温相差很大。
		// 九九
		int s24 = (int) Math.floor(节令.term(y, 24, true));
		int s_24 = (int) Math.floor(节令.term(y - 1, 24, true));
		int d1 = thisD0 - s24;
		Date a1 = new Date(y - 1900 - 1, 12 - 1, 31);
		// Date a2=new Date(y-1,1,0);
		int d2 = thisD0 - s_24 + 农历.getDayOfYear(a1) - 1;
		int w, v;
		if (d1 >= 0 || d2 <= 80) {
			if (solarDateTime.getMonth() + 1 == 12) {
				w = 1;
				v = d1 + 1;
				if (v > 9) {
					w += 1;
					v -= 9;
				}
			} else {
				w = (int) Math.floor((double) d2 / 9) + 1;
				v = (int) Math.round(LunarUtils.rem(d2, 9)) + 1;
			}
			re += " " + ToStringWithChineseDay(w).substring(1, 2) + "九第" + v
					+ "天";
		}

		return re;
	}

	/**
	 * 节日变量
	 */
	private String[] holiday = { "0101元旦", "0202世界湿地日", "0207国际声援南非日",
			"0210国际气象节", "0214情人节", "0301国际海豹日", "0303全国爱耳日", "0305学雷锋活动日",
			"0308国际妇女节", "0312植树节", "0314国际警察日", "0315消费者权益日",
			"0317中国国医节 国际航海日", "0321世界森林日 消除种族歧视国际日 世界儿歌日", "0322世界水日",
			"0323世界气象日", "0324世界防治结核病日", "0325全国中小学生安全教育日", "0330巴勒斯坦国土日",
			"0401愚人节", "0407世界卫生日", "0422世界地球日", "0423世界图书和版权日",
			"0424亚非新闻工作者日", "0501国际劳动节", "0504五四青年节", "0505碘缺乏病防治日",
			"0508世界红十字日", "0512国际护士节", "0515国际家庭日", "0517世界电信日", "0518国际博物馆日",
			"0520全国学生营养日", "0523国际牛奶日", "0531世界无烟日", "0601国际儿童节", "0605世界环境日",
			"0606全国爱眼日", "0617防治荒漠化和干旱日", "0623国际奥林匹克日", "0625全国土地日",
			"0626国际反毒品日", "0701党的生日 香港回归纪念日 世界建筑日", "0702国际体育记者日",
			"0707中国人民抗日战争纪念日", "0711世界人口日", "0730非洲妇女日", "0801八一建军节",
			"0908国际扫盲日", "0910中国教师节", "0914世界清洁地球日", "0916国际和平日 国际臭氧层保护日",
			"0918九·一八事变纪念日", "0920作者的生日 国际爱牙日", "0927世界旅游日",
			"1001国庆节 国际音乐日 国际老人节", "1002国际和平与民主自由斗争日", "1004世界动物日",
			"1005世界住房日", "1008全国高血压日 世界视觉日", "1009世界邮政日",
			"1010辛亥革命纪念日 世界精神卫生日", "1013世界保健日 国际教师节", "1014世界标准日",
			"1015国际盲人节(白手杖节)", "1016世界粮食日", "1017世界消除贫困日", "1022世界传统医药日",
			"1024联合国日 世界发展信息日", "1031世界勤俭日 万圣节前夜", "1107十月社会主义革命纪念日",
			"1108中国记者日", "1109全国消防安全宣传教育日", "1110世界青年节", "1114世界糖尿病日",
			"1117国际大学生节 世界学生节", "1121世界问候日 世界电视日", "1129国际声援巴勒斯坦人民国际日",
			"1201世界爱滋病日", "1203世界残疾人日", "1205国际经济和社会发展志愿人员日", "1208国际儿童电视日",
			"1209纪念一二·九运动 世界足球日", "1210世界人权日", "1212西安事变纪念日",
			"1213南京大屠杀(1937年)纪念日！紧记血泪史！", "1221国际篮球日", "1224平安夜",
			"1220澳门回归纪念日", "1225圣诞节", "1229国际生物多样性日" };
	private String[] chineseHoliday = { "0101新年", "0103天庆节", "0105五路财神日",
			"0108江东神诞", "0109昊天皇帝诞", "0111太均娘娘诞", "01139散花灯 哥升节", "0115元宵节",
			"0116馄饨节 门神诞", "0119丘处机诞", "0120女娲补天日 黄道婆祭", "0125填仓节",
			"0127天地水三官诞", "0202龙头节 太昊伏羲氏祭", "0203文昌诞", "0208芳春节 插花节",
			"0210彩蛋节", "0212花朝节", "0215老子诞", "0219观音诞", "0228寒潮节 岱诞",
			"0303上巳节 踏青节", "0305大禹诞", "0310撒种节", "0315孙膑诞 龙华会", "0316蒙恬诞",
			"0318中岳节", "0320鲁班诞", "0322子孙娘娘诞", "0323天后玛祖诞", "0328仓颉先师诞",
			"0401清和节", "0402公输般日", "0408洗佛放生节 牛王诞 跳月节", "0410葛洪诞", "0411孔子祭",
			"0414吕洞宾诞 菖蒲日", "0415钟离权诞 外萨卡佛陀日", "0417金花女诞", "0418锡伯迁移节",
			"0419浣花日", "0426炎帝神农氏诞", "0428扁鹊诞", "0501女儿节", "0504采花节",
			"0505端午节", "0511范蠡祭", "0513关羽诞", "0522曹娥日", "0529祖娘节",
			"0606天贶节 盘古逝", "0612彭祖笺铿诞", "0615捕鱼祭", "0616爬坡节", "0619太阳日 观音日",
			"0623火神诞", "0624观莲节", "0707乞巧节", "0712地狱开门日", "0713轩辕诞", "0715中元节",
			"0723诸葛亮诞", "0727黄老诞", "0801天医节", "0803华佗诞", "0815中秋节", "0818观潮节",
			"0824稻节", "0909重阳节", "0913钉鞋日", "0916伯余诞", "0919观音逝", "0930采参节",
			"1001送寒衣节 祭祖节", "1015下元节 文成公主诞", "1016盘古节", "1208腊八节",
			"1212百福日 蚕花娘娘诞", "1223洗灶日", "1224小年", "1225上帝下界之辰" };
	private String[] celebrity = { "0104雅各布·格林诞辰", "0108周恩来逝世纪念日",
			"0106圣女贞德诞辰", "0112杰克·伦敦诞辰", "0115莫里哀诞辰", "0117富兰克林诞辰", "0119瓦特诞辰",
			"0122培根诞辰", "0123郎之万诞辰", "0127莫扎特诞辰", "0129罗曼·罗兰诞辰", "0130甘地诞辰",
			"0131舒柏特诞辰", "0203门德尔松诞辰", "0207门捷列夫诞辰", "0211爱迪生诞辰，狄更斯诞辰",
			"0212林肯，达尔文诞辰", "0217布鲁诺诞辰", "0218伏打诞辰", "0219哥白尼诞辰",
			"0222赫兹，叔本华，华盛顿诞辰", "0226雨果诞辰", "0302斯美塔那诞辰", "0304白求恩诞辰",
			"0305周恩来诞辰", "0306布朗宁，米开朗琪罗诞辰", "0307竺可桢诞辰", "0314爱因斯坦诞辰",
			"0321巴赫，穆索尔斯基诞辰", "0322贺龙诞辰", "0328高尔基诞辰", "0401海顿，果戈理诞辰",
			"0415达·芬奇诞辰", "0416卓别林诞辰", "0420祖冲之诞辰", "0422列宁，康德，奥本海默诞辰",
			"0423普朗克，莎士比亚诞辰", "0430高斯诞辰", "0505马克思诞辰", "0507柴可夫斯基，泰戈尔诞辰",
			"0511冼星海诞辰", "0511李比希诞辰", "0520巴尔扎克诞辰", "0522瓦格纳诞辰", "0531惠特曼诞辰",
			"0601杜威诞辰", "0602哈代诞辰", "0608舒曼诞辰", "0715伦勃朗诞辰", "0805阿贝尔诞辰",
			"0808狄拉克诞辰", "0826陈毅诞辰", "0828歌德诞辰", "0909***逝世纪念日", "0925鲁迅诞辰",
			"0926巴甫洛夫诞辰", "0928孔子诞辰", "0929奥斯特洛夫斯基诞辰", "1011伯辽兹诞辰",
			"1021诺贝尔诞辰", "1022李斯特诞辰", "1026伽罗瓦诞辰", "1029李大钊诞辰", "1007居里夫人诞辰",
			"1108哈雷诞辰", "1112孙中山诞辰", "1124刘少奇诞辰", "1128恩格斯诞辰", "1201朱德诞辰",
			"1205海森堡诞辰", "1211玻恩诞辰", "1213海涅诞辰", "1216贝多芬诞辰", "1221斯大林诞辰",
			"1225牛顿诞辰", "1226***诞辰", "1229阿·托尔斯泰诞辰" };
}
