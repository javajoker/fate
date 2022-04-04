package com.infoecos.cn.destiny.lib.预测;

import com.infoecos.cn.destiny.lib.TimezoneLocation;
import com.infoecos.cn.destiny.lib.四柱;
import com.infoecos.cn.destiny.lib.干支;

import java.util.Date;


public class 四柱盘 extends 盘 {
    public static final int 年序 = 0;
    public static final int 月序 = 年序 + 1;
    public static final int 日序 = 月序 + 1;
    public static final int 时序 = 日序 + 1;
    public static float 月提权重 = Consts.基础权重 * 3; // 得令故

    protected 四柱盘(Date solarDateTime, boolean 乾造, TimezoneLocation location)
            throws Exception {
        super(solarDateTime, 乾造, location);
    }

    @Override
    protected 干支[] getInitializeParam() throws Exception {
        return new 干支[]{四柱.年柱(birthday, location),
                四柱.月柱(birthday, location),
                四柱.日柱(birthday, location),
                四柱.时柱(birthday, location)};
    }

    @Override
    public int 日序() {
        return 日序;
    }

    @Override
    protected void 配权() {
        柱数[月序].支数().setWeight(月提权重);
    }

    @Override
    protected int[][] 柱递进序() {
        return new int[][]{
                {年序, 月序}, {年序, 日序}, {年序, 时序},
                {月序, 日序}, {月序, 时序},
                {日序, 时序},};
    }

    @Override
    protected int getDistance(int x, int y) {
        return Math.abs(x - y);
    }
}
