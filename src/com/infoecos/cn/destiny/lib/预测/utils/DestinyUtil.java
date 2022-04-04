package com.infoecos.cn.destiny.lib.预测.utils;

import com.infoecos.cn.destiny.lib.common.阴阳;
import com.infoecos.cn.destiny.lib.五行.五行;
import com.infoecos.cn.destiny.lib.干.十神;
import com.infoecos.cn.destiny.lib.术数;
import com.infoecos.cn.destiny.lib.预测.*;
import com.infoecos.cn.destiny.utils.Arrays2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DestinyUtil {
    public static float[] 五行局面(盘 pan) {
        return 五行局面(pan, 0, 0);
    }

    public static float[] 五行局面(盘 pan, int start, int length) {
        float[] eleWeights = new float[5];
        干支数[] 柱数 = pan.柱数();
        if (length <= 0 || length > 柱数.length) length = 柱数.length;
        for (int i = start; i < length; ++i) {
            eleWeights[柱数[i].干数().术数().五行().ordinal()] += 柱数[i].干数()
                    .getWeight();
            for (干数 gan : 柱数[i].支数().藏干数()) {
                eleWeights[gan.术数().五行().ordinal()] += gan.getWeight();
            }
        }

        return eleWeights;
    }

    public static 五行[] sort5Ele(盘 pan) {
        float[] e = 五行局面(pan);
        int[] order = {0, 1, 2, 3, 4};
        for (int i = 0; i < 4; ++i) {
            for (int j = i + 1; j < 5; ++j) {
                if (e[i] > e[j]) {
                    float f = e[i];
                    int o = order[i];
                    e[i] = e[j];
                    order[i] = order[j];
                    e[j] = f;
                    order[j] = o;
                }
            }
        }
        五行[] ele = new 五行[5];
        for (int i = 0; i < 5; ++i) {
            ele[i] = 五行.values()[order[i]];
        }
        return ele;
    }

    public static 喜用神 用神(盘 pan) {
        喜用神 用神 = null;
        float[] eleWeights = 五行局面(pan), deviations = new float[5];
        五行 host = pan.柱数()[pan.日序()].干数().术数().五行();
        for (五行 ele : 五行.values()) {
            float[] newEleWeights = Arrays2.copyOf(eleWeights, 5), ws = new float[]{
                    Consts.基础权重, 0};
            五行 e = null;

            e = host.官();
            ws[1] = newEleWeights[e.ordinal()];
            ws = 干数.生克(ws, new 术数[]{new 术数(ele, 阴阳.阳), new 术数(e, 阴阳.阳)});
            newEleWeights[e.ordinal()] = ws[1];

            e = host.克();
            ws[1] = newEleWeights[e.ordinal()];
            ws = 干数.生克(ws, new 术数[]{new 术数(ele, 阴阳.阳), new 术数(e, 阴阳.阳)});
            newEleWeights[e.ordinal()] = ws[1];

            e = host.生();
            ws[1] = newEleWeights[e.ordinal()];
            ws = 干数.生克(ws, new 术数[]{new 术数(ele, 阴阳.阳), new 术数(e, 阴阳.阳)});
            newEleWeights[e.ordinal()] = ws[1];

            e = host;
            ws[1] = newEleWeights[e.ordinal()];
            ws = 干数.生克(ws, new 术数[]{new 术数(ele, 阴阳.阳), new 术数(e, 阴阳.阳)});
            newEleWeights[e.ordinal()] = ws[1];

            e = host.印();
            ws[1] = newEleWeights[e.ordinal()];
            ws = 干数.生克(ws, new 术数[]{new 术数(ele, 阴阳.阳), new 术数(e, 阴阳.阳)});
            newEleWeights[e.ordinal()] = ws[1];

            deviations[ele.ordinal()] = MathUtil.getStandardDeviation(newEleWeights);
        }
        float deviation = MathUtil.getStandardDeviation(eleWeights);
        五行 忌神 = null;
        List<五行> eles = new ArrayList<五行>();
        for (int i = 0; i < 5; ++i) {
            float minDeviation = Float.MAX_VALUE;
            int idx = -1;
            for (int j = 0; j < 5; ++j) {
                if (deviations[j] < minDeviation) {
                    idx = j;
                    minDeviation = deviations[j];
                }
            }
            if (minDeviation < deviation) {
                eles.add(五行.values()[idx]);
            } else {
                if (eles.size() == 0)
                    eles.add(五行.values()[idx]);
                else
                    忌神 = 五行.values()[idx];
            }
            deviations[idx] = Float.MAX_VALUE;
        }
        用神 = new 喜用神(eles.toArray(new 五行[eles.size()]));
        用神.忌神(忌神);

        if (Consts._VERBOSE) {
            System.err.println("== 用神喜忌 ==");
            System.err.println(String.format("\t用神：%s", 用神.用神()));
            System.err.println(String.format("\t喜神：%s", 用神.喜神()));
            System.err.println(String.format("\t喜神2：%s", 用神.喜神2()));
            System.err.println(String.format("\t忌神：%s", 用神.忌神()));
        }
        return 用神;
    }


    public static Map<十神, Float> 十神(盘 pan) {
        return 十神(pan, 0, 0);
    }

    public static Map<十神, Float> 十神(盘 pan, int start, int length) {
        return 十神(pan.柱数(), start, length);
    }

    public static Map<十神, Float> 十神(干支数[] 柱数) {
        return 十神(柱数, 0, 0);
    }

    public static Map<十神, Float> 十神(干支数[] 柱数, int start, int length) {
        Map<十神, Float> 神 = new HashMap<十神, Float>();
        if (length <= 0 || length > 柱数.length) length = 柱数.length;
        for (int i = start; i < length; ++i) {
            干数 gan = 柱数[i].干数();
            支数 zhi = 柱数[i].支数();

            float weight = 0;
            if (神.containsKey(gan.神()))
                weight = 神.get(gan.神());
            weight += gan.getWeight();
            神.put(gan.神(), weight);

            for (干数 g : zhi.藏干数()) {
                weight = 0;
                if (神.containsKey(g.神()))
                    weight = 神.get(g.神());
                weight += g.getWeight();
                神.put(g.神(), weight);
            }
        }
        return 神;
    }

    public static 十神[][] 十神心性(盘 pan) {
        Map<十神, Float> sw = 十神(pan);
        float host = 100f;
        float[] weights = new float[10];
        for (十神 s : sw.keySet()) {
            if (十神.NA.equals(s)) {
                host = sw.get(s);
            } else {
                weights[s.ordinal()] = sw.get(s);
            }
        }
        Integer[][] sigmaIdx = MathUtil.sigmaIdxUponDeviation(weights, host);

        十神[][] xx = new 十神[2][];
        xx[0] = new 十神[sigmaIdx[0].length + sigmaIdx[1].length];
        int idx = 0;
        for (int i : sigmaIdx[0]) {
            xx[0][idx] = 十神.values()[i];
            ++idx;
        }
        for (int i : sigmaIdx[1]) {
            xx[0][idx] = 十神.values()[i];
            ++idx;
        }
        xx[1] = new 十神[sigmaIdx[3].length + sigmaIdx[4].length];
        idx = 0;
        for (int i : sigmaIdx[4]) {
            xx[1][idx] = 十神.values()[i];
            ++idx;
        }
        for (int i : sigmaIdx[3]) {
            xx[1][idx] = 十神.values()[i];
            ++idx;
        }

        return xx;
    }

    public static 运[] 推运(八字排盘 destiny) {
        运[] ff = new 运[destiny.运盘().size()];

        float[][] dFate = new float[10][], dRelation = new float[8][];
        for (int i = 0; i < 10; ++i) dFate[i] = new float[destiny.运盘().size()];
        for (int i = 0; i < 8; ++i) dRelation[i] = new float[destiny.运盘().size()];

        干支数[] p0 = destiny.盘().柱数(), p1;
        int idx = 0;
        for (运盘 p : destiny.运盘()) {
            ff[idx] = new 运(p.getTimespan(), new HashMap<>(), new HashMap<>());
            p1 = p.柱();
            Map<十神, Float> s0 = 十神(p0), s1 = 十神(p1);
            for (十神 s : s0.keySet()) {
                if (十神.NA.equals(s)) continue;
                dFate[s.ordinal()][idx] = s1.get(s) - s0.get(s);
            }
            for (int i = 0; i < p0.length; ++i) {
                干数 g = p0[i].干数();
                支数 z = p0[i].支数();
                dRelation[i << 1][idx] = p1[i].干数().getWeight() - p0[i].干数().getWeight();
                dRelation[(i << 1) + 1][idx] = p1[i].支数().getWeight() - p0[i].支数().getWeight();
            }
            p0 = p1;
            ++idx;
        }

        for (int i = 0; i < dFate.length; ++i) {
            float[] f = dFate[i];
            List<Integer> l = updateWeight(f);
            for (int j : l) ff[j].getFate().put(十神.values()[i], f[j]);
        }
        for (int i = 0; i < dRelation.length; ++i) {
            float[] r = dRelation[i];
            List<Integer> l = updateWeight(r);
            for (int j : l) ff[j].getRelation().put(i, r[j]);
        }
        List<运> fate = new ArrayList<>();
        for (运 f : ff) {
            if (f.getRelation().size() > 0 && f.getFate().size() > 0) fate.add(f);
        }
        return fate.toArray(new 运[fate.size()]);
    }

    private static List<Integer> updateWeight(float[] ws) {
        List<Integer> l = new ArrayList<>();
        float sd = MathUtil.getStandardDeviation(ws);
        for (int i = 0; i < ws.length; ++i) {
            float w = ws[i] / sd;
            ws[i] = w > 2 ? w : (w < -2 ? w : 0);
            if (ws[i] != 0) l.add(i);
        }
        return l;
    }

    public static void _VERBOSE(八字排盘 destiny) {
        DateFormat dfm = new SimpleDateFormat("MM-dd-yyyy");
        干支数[] p0 = destiny.盘().柱数(), p1;
// ----- head -----
        System.out.println(",,四柱");
        System.out.print(",,");
        for (int i = 0; i < 4; ++i) {
            干数 g = p0[i].干数();
            支数 z = p0[i].支数();
            System.out.print(String.format("%s-%s,%s,",
                    g.天干(), g.神(),
                    z.地支()));
            for (int j = 1; j < z.藏干数().length; ++j) {
                System.out.print(",");
            }
        }
        System.out.println("十神");
        System.out.print(",,");
        for (int i = 0; i < 4; ++i) {
            支数 z = p0[i].支数();
            System.out.print(",");
            for (int j = 0; j < z.藏干数().length; ++j) {
                干数 g2 = z.藏干数()[j];
                System.out.print(String.format("%s-%s,",
                        g2.天干(), g2.神()));
            }
        }
        for (int i = 0; i < 10; ++i) System.out.print(String.format("%s,", 十神.values()[i]));
        System.out.println();
// ----- head -----
        System.out.print(",,");
        for (int i = 0; i < 4; ++i) {
            干数 g = p0[i].干数();
            支数 z = p0[i].支数();
            System.out.print(String.format("%f,", g.getWeight()));
            for (int j = 0; j < z.藏干数().length; ++j) {
                干数 g2 = z.藏干数()[j];
                System.out.print(String.format("%f,", g2.getWeight()));
            }
        }
        System.out.println();
// ----- origin -----
        for (运盘 p : destiny.运盘()) {
            System.out.print(String.format("%s,%s,",
                    dfm.format(p.getTimespan()[0]),
                    dfm.format(p.getTimespan()[1])));
            p1 = p.柱();
            for (int i = 0; i < 4; ++i) {
                干数 g = p0[i].干数();
                支数 z = p0[i].支数();
                System.out.print(String.format("%f,", p1[i].干数().getWeight() - g.getWeight()));
                for (int j = 0; j < z.藏干数().length; ++j) {
                    干数 g2 = z.藏干数()[j];
                    System.out.print(String.format("%f,", p1[i].支数().藏干数()[j].getWeight() - g2.getWeight()));
                }
            }
            p0 = p1;
            System.out.println();
        }
    }

//    public String 宫名(int 柱序, boolean 干) {
//        if (柱序 == 日序() && 干)
//            return "命主";
//
//        return 宫[柱序][顺() ^ 干 ? 0 : 1];
//    }
}
