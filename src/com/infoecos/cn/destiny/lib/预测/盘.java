package com.infoecos.cn.destiny.lib.预测;

import com.infoecos.cn.destiny.lib.TimezoneLocation;
import com.infoecos.cn.destiny.lib.命运;
import com.infoecos.cn.destiny.lib.干.十神;
import com.infoecos.cn.destiny.lib.干支;
import com.infoecos.cn.destiny.lib.术数;

import java.util.Calendar;
import java.util.Date;

/**
 * 胡子模型：
 * 天地人三元论事，不脱五行阴阳，十天干也。
 * 以一至十和五十又五为天地之数，人元内藏五行，又五得六十数，是为天地人轮回之数。
 * 由十天干而得地支十二。故曰，地支藏干，是为人元。
 * 年月日时四柱干支以八字论命，实以天干论阴阳五行之生克乘侮也。
 * 年柱为根，月柱成枝，日柱开花，时柱得果。
 * 年岁日时柱不同，唯有月支映天时，四柱之中，以月支为提纲，故称“月提”。
 * 胎成得灵，择日而生，故日干为“我”，曰“日主”。
 * 四柱预测，首论干支关系，是为“十干之地支生旺死绝”，实为五行生克官印各宫顺逆而行，地支藏干由此而得。
 * 所谓生旺死绝论得令得地增力者，实为比劫帮身增力，五行相生。以通根透干为干支比劫助力，次论五行生克，故得令得地不用。
 * 地支为天干生发之根，以其为根故，彼此不易沟通，故不直论生克而先观局。
 * 三会三合又及半合，借一点天干之性沟通。得透方可成局。不透干不成局故，无“合绊”之说。
 * 地支局中，一点本气或冲，或合，或刑，或害，皆以五行生克为要：
 * 冲破合局者，对冲泄气而“泄多为克”故；
 * 所谓“合绊”不入刑冲克害者，五行过弱生者不生，克者不克也。
 * 地支已定，乃论天干，合同支藏，于是生克，命局成矣。
 * 复次，观命局，定用神，成格局，得神煞。不脱阴阳五行指事尔。
 * 先天命既定，后天又行运，命运成也。又及流年，命内“谈”宫，流年行运论“星”，预测论命可也。
 * 由古至今预测或以命运流年六柱，或以四柱命大小运胎元流年八柱，知其然而渐不知其所以然。
 * 地支为里，天干是表，由里及表。所谓预测，知其表象尔。故曰，心若淡然，苦者何谓？
 * 胡子以“其然”逆推“所以然”，以期自圆其说。附会而已。
 */
public abstract class 盘 {
    protected final Date birthday;
    protected final boolean 乾造;
    protected final TimezoneLocation location;
    protected 干支数[] 柱数;
    protected 术数 日主;

    public 盘(Date solarDateTime, boolean 乾造, TimezoneLocation location)
            throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(solarDateTime);
        if (calendar.get(Calendar.YEAR) <= 1900
                || calendar.get(Calendar.YEAR) > 2100)
            throw new Exception("year 1901-2100 only");
        this.birthday = solarDateTime;
        this.乾造 = 乾造;
        this.location = location;
    }

    protected abstract 干支[] getInitializeParam() throws Exception;

    public 干支数[] 柱数() {
        return 柱数;
    }

    public 术数 日主() {
        return 日主;
    }

    public abstract int 日序();

    protected void initialize() throws Exception {
        干支[] 柱 = getInitializeParam();
        this.柱数 = new 干支数[柱.length];
        this.日主 = 柱[日序()].天干().术数();

        for (int i = 0; i < 柱.length; ++i) {
            柱数[i] = 柱[i] == null ? null : new 干支数(柱[i]);
        }

        配权();
        十神();
    }

    protected abstract void 配权();

    public 盘 运盘() throws Exception {
        initialize();

        if (Consts._VERBOSE) {
            System.err.println("=======================================");
            System.err.println(this);
        }

        // 地支指后天之气，以（木）星、日、月、地，各分十二
        // 地支生克，冲即为克，合即比生，刑害待新数入盘即成生克
        for (int[] pair : 柱递进序()) {
            int x = pair[0], y = pair[1];
            if (null == 柱数[x] || null == 柱数[y]) continue;
            五行生克数(柱数[x].支数(), 柱数[y].支数(), getDistance(x, y));
        }

        // 直坐干支生克
        for (int i = 0; i < 柱数.length; ++i) {
            if (null == 柱数[i]) continue;
            五行生克数(柱数[i].干数(), 柱数[i].支数(), 0);
        }
        // 干支生克
        for (int[] pair : 柱递进序()) {
            int x = pair[0], y = pair[1];
            if (null == 柱数[x] || null == 柱数[y]) continue;
            五行生克数(柱数[x].干数(), 柱数[y].支数(), getDistance(x, y) + 1);
            五行生克数(柱数[x].支数(), 柱数[y].干数(), getDistance(x, y) + 1);
        }
        // 天干生克
        for (int[] pair : 柱递进序()) {
            int x = pair[0], y = pair[1];
            if (null == 柱数[x] || null == 柱数[y]) continue;
            五行生克数(柱数[x].干数(), 柱数[y].干数(), getDistance(x, y));
        }

        if (Consts._VERBOSE) {
            System.err.println(this);
            System.err.println("=======================================");
        }

        return this;
    }

    /**
     * 柱间生克序，如树木生发，根发而枝，枝头开花，花落果实。
     *
     * @return [首柱序, 次柱序]
     */
    protected abstract int[][] 柱递进序();

    /**
     * @param x
     * @param y
     * @return 贴柱 = 1； 隔柱 = 2； 远柱 = 3
     */
    protected abstract int getDistance(int x, int y);

    private void 五行生克数(数 x, 数 y, int 柱距) {
        干数[] xs = (x instanceof 干数) ? new 干数[]{(干数) x} : ((支数) x).藏干数(),
                ys = (y instanceof 干数) ? new 干数[]{(干数) y} : ((支数) y).藏干数();

        for (int i = xs.length - 1; i >= 0; --i) {
            for (int j = ys.length - 1; j >= 0; --j)
                干数.生克(xs[i], ys[j], 柱距);
        }
    }

    protected void 十神() {
        for (int i = 0; i < 柱数.length; ++i) {
            if (柱数[i] == null) continue;
            for (干数 gan : 柱数[i].支数().藏干数()) {
                gan.神(十神.求神(日主, gan.术数()));
            }
            if (i == 日序())
                continue;
            柱数[i].干数().神(十神.求神(日主, 柱数[i].干数().术数()));
        }
    }

    public boolean 顺() {
        try {
            return 命运.顺行(birthday, 乾造, location);
        } catch (Exception e) {
            return 乾造;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (干支数 i : 柱数()) {
            if (null != i)
                sb.append(String.format("\t%s\t%s\t%s\r\n", i.干支().纳音(), i.干数(), i.支数()));
        }
        return sb.toString();
    }
}
