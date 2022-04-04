package com.infoecos.cn.destiny.lib.预测;

import com.infoecos.cn.destiny.lib.TimezoneLocation;
import com.infoecos.cn.destiny.lib.命运;
import com.infoecos.cn.destiny.lib.四柱;
import com.infoecos.cn.destiny.lib.干支;
import com.infoecos.cn.destiny.lib.时.节令;
import com.infoecos.cn.lunisolar.LunarDate;
import com.infoecos.cn.lunisolar.农历;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class 八字排盘 {
    private final 四柱盘 pan;
    private final Date birthday;
    private final boolean 乾造;
    private final TimezoneLocation location;
    private List<运盘> pans;

    public 八字排盘(Date solarDateTime, boolean 乾造, TimezoneLocation location)
            throws Exception {
        this.birthday = solarDateTime;
        this.乾造 = 乾造;
        this.location = location;
        this.pan = new 四柱盘(solarDateTime, 乾造, location);

        pan.运盘();
    }

    public 盘 盘() {
        return pan;
    }

    public List<运盘> 运盘() {
        return pans;
    }

    public void 行运排盘() throws Exception {
        行运排盘(false);
    }

    public void 行运排盘(boolean 流月) throws Exception {
        pans = new ArrayList<>();

        Date 运 = 命运.起运交脱(birthday, 乾造, location);
        干支[] 大运 = 命运.大运(birthday, 乾造, location);

        LunarDate ld = 农历.TimeToLunar(运);
        Date d1 = birthday, d2, d3, now = new Date();

        d2 = new Date(d1.getYear(), 0, (int) 节令.term(d1.getYear() + 1900,
                3, true));
        if (d2.getTime() > d1.getTime()) {
            流年(null, d1, d2, 0, 流月);
            d1 = d2;
        }
        while (true) {
            d2 = new Date(d2.getYear() + 1, 0, (int) 节令.term(
                    d2.getYear() + 1 + 1900, 3, true));
            if (d2.getTime() >= 运.getTime()) {
                流年(null, d1, 运, 0, 流月);
                d2 = 运;
                break;
            }
            流年(null, d1, d2, 0, 流月);
            d1 = d2;
        }
        for (int i = 0; i < 大运.length; ++i) {
            int jn = 0;
            d1 = d2;
            d2 = new Date(d2.getYear(), 0, (int) 节令.term(d2.getYear() + 1900,
                    3, true));
            if (d2.getTime() > d1.getTime()) {
                流年(大运[i], d1, d2, jn, 流月);
                d1 = d2;
            }
            ld.setYear(ld.getYear() + 1);
            for (int j = 0; j < 9; ++j) {
                if (d1.getYear() > now.getYear() + 5)
                    return;
                d2 = new Date(d2.getYear() + 1, 0, (int) 节令.term(
                        d2.getYear() + 1 + 1900, 3, true));
                流年(大运[i], d1, d2, jn, 流月);
                ++jn;
                d1 = d2;
                ld.setYear(ld.getYear() + 1);
            }
            d3 = new Date(d2.getYear() + 1, 0, (int) 节令.term(
                    d2.getYear() + 1 + 1900, 3, true));
            d2 = 农历.LunarToTime(ld);
            if (d2.getTime() > d3.getTime()) {
                流年(大运[i], d1, d3, jn, 流月);
                d1 = d3;
            }
            流年(大运[i], d1, d2, jn, 流月);
        }
    }

    private void 流年(干支 运, Date 始, Date 终, int 大运经年, boolean 流月)
            throws Exception {
        if (始.getTime() == 终.getTime()) return;
        if (!流月) {
            pans.add(new 运盘(
                    (大运流年盘) new 大运流年盘(birthday, 乾造, location, 运, 四柱.年柱(始), 大运经年).运盘(),
                    new Date[]{始, 终}
            ));
        } else {
            LunarDate ld = 农历.TimeToLunar(始);
            Date d1, d2;
            do {
                d1 = 农历.LunarToTime(ld);
                ld.setMonth(ld.getMonth() + 1);
                d2 = 农历.LunarToTime(ld);
                pans.add(new 运盘(
                        (流月盘) new 流月盘(birthday, 乾造, location, 运, 四柱.年柱(始), 大运经年, 四柱.月柱(d1)).运盘(),
                        new Date[]{d1, d2.getTime() > 终.getTime() ? 终 : d2}
                ));
            } while (d2.getTime() < 终.getTime());
        }
    }
}
