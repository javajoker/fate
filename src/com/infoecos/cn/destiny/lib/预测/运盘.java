package com.infoecos.cn.destiny.lib.预测;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class 运盘 {
    四柱盘 盘;
    int start = 四柱盘.年序, length = 四柱盘.时序 + 1;
    Date[] timespan;

    运盘(四柱盘 盘, Date[] timespan) {
        this.盘 = 盘;
        this.timespan = timespan;
    }

    public Date[] getTimespan() {
        return timespan;
    }

    public 干支数[] 柱() {
        return Arrays.copyOfRange(盘.柱数(), start, length);
    }

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        StringBuilder sb = new StringBuilder();
        for (干支数 i : 柱()) {
            if (null != i)
                sb.append(String.format("\t%s\t%s\t%s\r\n", i.干支().纳音(), i.干数(), i.支数()));
        }
        return String.format("%s ~ %s\r\n%s",
                df.format(timespan[0]), df.format(timespan[1]),
                sb);
    }
}
