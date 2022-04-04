package com.infoecos.cn.destiny.lib.预测;

import com.infoecos.cn.destiny.lib.TimezoneLocation;
import com.infoecos.cn.destiny.lib.四柱;
import com.infoecos.cn.destiny.lib.干支;

import java.util.Date;

public class 流月盘 extends 大运流年盘 {
    public static final int 流月序 = 流年序 + 1;
    public static float 流月权重 = 50.0f;
    protected final 干支 流月;

    protected 流月盘(Date solarDateTime, boolean 乾造, TimezoneLocation location, 干支 大运,
                  干支 流年, int 大运经年, 干支 流月) throws Exception {
        super(solarDateTime, 乾造, location, 大运, 流年, 大运经年);
        this.流月 = 流月;
    }

    @Override
    protected 干支[] getInitializeParam() throws Exception {
        return new 干支[]{
                四柱.年柱(birthday, location),
                四柱.月柱(birthday, location),
                四柱.日柱(birthday, location),
                四柱.时柱(birthday, location),
                大运,
                流年,
                流月};
    }

    @Override
    protected void 配权() {
        super.配权();
        柱数[流月序].干数().setWeight(流月权重);
        柱数[流月序].支数().setWeight(流月权重);
    }

    @Override
    protected int[][] 柱递进序() {
        return new int[][]{
                {大运序, 年序}, {大运序, 月序}, {大运序, 日序}, {大运序, 时序},
                {流年序, 年序}, {流年序, 月序}, {流年序, 日序}, {流年序, 时序},
                {流月序, 年序}, {流月序, 月序}, {流月序, 日序}, {流月序, 时序},
                {年序, 月序}, {年序, 日序}, {年序, 时序},
                {月序, 日序}, {月序, 时序},
                {日序, 时序},};
    }

    @Override
    protected int getDistance(int x, int y) {
        return (x <= 时序 && y <= 时序) ? Math.abs(x - y) : 1;
    }
}
