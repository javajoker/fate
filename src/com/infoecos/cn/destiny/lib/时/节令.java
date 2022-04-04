package com.infoecos.cn.destiny.lib.时;

import com.infoecos.cn.lunisolar.LunarUtils;

import java.util.Date;

public class 节令 {

    /**
     * 获取指定日期的节气。
     * 前个节气称节气，后个节气称中气 农历每月对应一中气，某月无中气，为闰月。
     * 立春：立是开始的意思，春是蠢动，表示万物开始有生气，这一天春天开始。
     * 雨水：降雨开始，雨水将多。
     * 惊蛰：春雷响动，惊动蛰伏地下冬眠的生物，它们将开始出土活动。
     * 春分：这是春季九十天的中分点，这一天昼夜相等，所以古代曾称春分秋分为昼夜分。
     * 清明：明洁晴朗，气候温暖，草木开始萌发繁茂。
     * 谷雨：雨生百谷的意思。雨水增多，适时的降雨对谷物生长很为有利。
     * 立夏：夏天开始，万物渐将随温暖的气候而生长。
     * 小满：满指籽粒饱满，麦类等夏热作物这时开始结籽灌浆，即将饱满。
     * 芒种：有芒作物开始成熟，此时也是秋季作物播种的最繁忙时节。
     * 夏至：白天最长，黑夜最短，这一天中午太阳位置最高，日影短至终极，古代又称这一天为日北至或长日至。
     * 小暑：暑是炎热，此时还未到达最热。
     * 大暑：炎热的程度到达高峰。
     * 立秋：秋天开始，植物快成熟了。
     * 处暑：处是住的意思，表示暑气到此为止。
     * 白露：地面水气凝结为露，色白，是天气开始转凉了。
     * 秋分：秋季九十天的中间，这一天昼夜相等，同春分一样，太阳从正东升起正西落下。
     * 寒露：水露先白而后寒，是气候将逐渐转冷的意思。
     * 霜降：水气开始凝结成霜。
     * 立冬：冬是终了，作物收割后要收藏起来的意思，这一天起冬天开始。
     * 小雪：开始降雪，但还不多。
     * 大雪：雪量由小增大。
     * 冬至：这一天中午太阳在天空中位置最低，日影最长，白天最短， 黑夜最长，古代又称短日至或日南至。
     * 小寒：冷气积久而为寒，此时尚未冷到顶点。
     * 大寒：天候达到最寒冷的程度
     *
     * @param year
     * @param month
     * @return
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    public static 节气[] 节气(int year, int month) throws Exception {
        String[] lunarHoliDayName = {"小寒", "大寒", "立春", "雨水", "惊蛰", "春分", "清明",
                "谷雨", "立夏", "小满", "芒种", "夏至", "小暑", "大暑", "立秋", "处暑", "白露",
                "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至"};
        节气[] solarTerm = new 节气[2];

        for (int n = month * 2 - 1; n <= month * 2; n++) {
            节气 st = new 节气();
            double dd = term(year, n, true);
            double sd1 = LunarUtils.antiDayDifference(year, Math.floor(dd));
            // double sm1 = Math.floor(sd1 / 100);
            int h = (int) Math.floor(LunarUtils.tail(dd) * 24);
            int min = (int) Math
                    .floor((LunarUtils.tail(dd) * 24 - h) * 60);
            int mmonth = (int) Math.ceil((double) n / 2);
            int day = (int) sd1 % 100;
            st.setSolarTermDate(new Date(year - 1900, mmonth - 1, day, h, min,
                    0));
            st.setName(lunarHoliDayName[n - 1]);
            solarTerm[n - month * 2 + 1] = st;
        }
        return solarTerm;
    }

    /**
     * 获取指定日期的节气。
     * 我国农历里把节气分得很细，定出了二十四节气。
     * 它们的名称大都反应物候、农时或季节的起点与中点。
     * 由于节气实际反应太阳运行所引起的气候变化，故二十四节气为阳历的自然衍生的产物，与阴历无关。
     * <p>
     * 二十四节气中以立春、春分、立夏，夏至、立秋、秋分、立冬与冬至等八节气最为重要。 它们之间大约相隔46天。
     * 一年分为四季，「立」表示四季中每一个季节的开始，而「分」与「至」表示正处于这季节的中间。
     * 现代我国所使用的历法，皆依回归年制定，二十四节气基本上是一致的，前后的相差不会超过一两天。
     * 为了调合回归年(阳历)与朔望月(阴历)之间的差异，农历把二十四节气中，双数的叫中气，单数的叫节气。
     * 而且规定每一个中气标定在一个农历的月份。
     * 例如雨水必定在正月，春分必定在二月，谷雨必定在三月，其余依此类推。另外，月名也必须和相对应的中气相合。
     *
     * @param solarDateTime
     * @return
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    public static 节气[] 节气(Date solarDateTime) throws Exception {
        return 节气(solarDateTime.getYear() + 1900, solarDateTime.getMonth() + 1);
    }

    /**
     * 返回y年第n个节气（如小寒为1）的日差天数值（pd取值真假，分别表示平气和定气）
     *
     * @param y
     * @param n
     * @param pd
     * @return
     * @throws Exception
     */
    public static double term(int y, int n, boolean pd) throws Exception {
        double juD = y
                * (365.2423112 - 6.4e-14 * (y - 100) * (y - 100) - 3.047e-8 * (y - 100))
                + 15.218427 * n + 1721050.71301;// 儒略日
        double tht = 3e-4 * y - 0.372781384 - 0.2617913325 * n;// 角度
        double yrD = (1.945 * Math.sin(tht) - 0.01206 * Math.sin(2 * tht))
                * (1.048994 - 2.583e-5 * y);// 年差实均数
        double shuoD = -18e-4
                * Math.sin(2.313908653 * y - 0.439822951 - 3.0443 * n);// 朔差实均数
        double vs = (pd) ? (juD + yrD + shuoD
                - LunarUtils.equivalentStandardDay(y, 1, 0) - 1721425) : (juD
                - LunarUtils.equivalentStandardDay(y, 1, 0) - 1721425);
        return vs;
    }
}
