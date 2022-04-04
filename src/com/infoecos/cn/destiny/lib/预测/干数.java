package com.infoecos.cn.destiny.lib.预测;

import com.infoecos.cn.destiny.lib.五行.五行;
import com.infoecos.cn.destiny.lib.干.十神;
import com.infoecos.cn.destiny.lib.干.天干;
import com.infoecos.cn.destiny.lib.术数;


public class 干数 extends 数 {
    /**************************** 生克 ************************************************/
    public static final float 生主泄气 = 1.0f / 4;
    public static final float 生客得气 = 2.0f / 4;
    public static final float 克主耗气 = 2.0f / 4;
    public static final float 克客失气 = 3.0f / 4;

    private static final float 异性衰减 = 3.0f / 4;
    private final 天干 天干;
    private float weight;
    private 术数 化数 = null;
    private 十神 神 = 十神.NA;

    public 干数(天干 天干) {
        this(天干, Consts.基础权重);
    }

    public 干数(天干 天干, float weight) {
        super(天干.术数());

        this.天干 = 天干;
        this.weight = weight;
    }

    /**
     * 同性之生，其生力大于异性；生者减气，受生者得益。
     * <p>
     * 同性之克，其克力大于异性；两者均受损伤、被克者损伤大；
     */
    public static void 生克(干数 i, 干数 j) {
        生克(i, j, 0);
    }

    public static void 生克(干数 i, 干数 j, int 柱距) {
        float[] weights = 生克(new float[]{i.getWeight(), j.getWeight()},
                new 术数[]{i.术数(), j.术数()}, 柱距);
        i.setWeight(weights[0]);
        j.setWeight(weights[1]);
    }

    public static float[] 生克(float[] weights, 术数[] eles) {
        return 生克(weights, eles, 0);
    }

    public static float[] 生克(float[] weights, 术数[] eles, int 柱距) {
        if (eles[0].equals(eles[1]))
            return weights;

        float a = weights[0], b = weights[1], c, d;
        术数 x = eles[0], y = eles[1];

        if (x.五行().生().equals(y.五行()) || y.五行().生().equals(x.五行())) {
            // 反生为克，是指五行相生双方，主生者旺，被生者衰的现象。
            // 泄多为克，是指五行相生双方，被生者旺，主生者弱的现象。
            c = d = a > b ? b : a;
            c *= 生主泄气;
            d *= 生客得气;
        } else if (x.五行().克().equals(y.五行()) || y.五行().克().equals(x.五行())) {
            // 五行反克是指相克双方为主克者弱，被克者强的一种特殊现象。
            c = d = a > b ? b : a;
            c *= 克主耗气;
            d *= 克客失气;
        } else
            return weights;

        if (!x.阴阳().equals(y.阴阳())) {
            c *= 异性衰减;
            d *= 异性衰减;
        }

        --柱距;
        double 衰减 = 1 / Math.pow(2, (柱距 <= 0) ? 0 : 柱距);
        c *= 衰减;
        d *= 衰减;

        if (x.五行().生().equals(y.五行())) {
            a -= c;
            b += d;
        } else if (y.五行().生().equals(x.五行())) {
            b -= c;
            a += d;
        } else if (x.五行().克().equals(y.五行())) {
            a -= c;
            b -= d;
        } else if (y.五行().克().equals(x.五行())) {
            b -= c;
            a -= d;
        }

        if (a < 0)
            a = 0;
        if (b < 0)
            b = 0;

        return new float[]{a, b};
    }

    public 天干 天干() {
        return 天干;
    }

    public void 化(五行 ele) {
        if (ele.equals(术数.五行()))
            return;
        化数 = new 术数(ele, 术数.阴阳());
    }

    public 十神 神() {
        return 神;
    }

    public void 神(十神 神) {
        this.神 = 神;
    }

    @Override
    public 术数 术数() {
        return (化数 == null) ? 术数 : 化数;
    }

    @Override
    public float getWeight() {
        return weight;
    }

    @Override
    void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return String.format("%s(%s-%s):\t%f", 天干, 天干.术数().五行(), 神(), getWeight());
    }
}
