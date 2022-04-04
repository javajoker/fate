package com.infoecos.cn.destiny.lib.预测;

import com.infoecos.cn.destiny.lib.TimezoneLocation;
import com.infoecos.cn.destiny.lib.四柱;
import com.infoecos.cn.destiny.lib.干支;

import java.util.Date;


public class 大运流年盘 extends 四柱盘 {
    public static final int 大运序 = 时序 + 1;
    public static final int 流年序 = 大运序 + 1;

    public static float 太岁权重 = 140.0f;
    public static float 大运流年干支权和 = 220.0f;
    public static float 大运生发 = 20.0f;
    protected final int 大运经年;
    protected final 干支 大运, 流年;

    protected 大运流年盘(Date solarDateTime, boolean 乾造, TimezoneLocation location, 干支 大运,
                    干支 流年, int 大运经年) throws Exception {
        super(solarDateTime, 乾造, location);

        this.大运 = 大运;
        this.流年 = 流年;
        this.大运经年 = 大运 == null ? 0 : 大运经年;
    }

    @Override
    protected 干支[] getInitializeParam() throws Exception {
        return new 干支[]{四柱.年柱(birthday, location),
                四柱.月柱(birthday, location),
                四柱.日柱(birthday, location),
                四柱.时柱(birthday, location),
                大运,
                流年};
    }

    @Override
    protected void 配权() {
        super.配权();

        if (null != 柱数[大运序]) {
            柱数[大运序].干数().setWeight(大运流年干支权和 - (大运经年 + 1) * 大运生发);
            柱数[大运序].支数().setWeight((大运经年 + 1) * 大运生发);
        }

        柱数[流年序].干数().setWeight(大运流年干支权和 - 太岁权重);
        柱数[流年序].支数().setWeight(太岁权重); // 太岁
    }

    @Override
    protected int[][] 柱递进序() {
        return new int[][]{
                {大运序, 年序}, {大运序, 月序}, {大运序, 日序}, {大运序, 时序},
                {流年序, 年序}, {流年序, 月序}, {流年序, 日序}, {流年序, 时序},
                {年序, 月序}, {年序, 日序}, {年序, 时序},
                {月序, 日序}, {月序, 时序},
                {日序, 时序},};
    }

    @Override
    protected int getDistance(int x, int y) {
        return (x <= 时序 && y <= 时序) ? Math.abs(x - y) : 1;
    }
}
