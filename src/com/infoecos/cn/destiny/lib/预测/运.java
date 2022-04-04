package com.infoecos.cn.destiny.lib.预测;

import com.infoecos.cn.destiny.lib.干.十神;

import java.util.Date;
import java.util.Map;

public class 运 {
    Date[] timespan;
    Map<十神, Float> fate;
    Map<Integer, Float> relation;

    public 运(Date[] timespan, Map<十神, Float> fate, Map<Integer, Float> relation) {
        this.timespan = timespan;
        this.fate = fate;
        this.relation = relation;
    }

    public Date[] getTimespan() {
        return timespan;
    }

    public Map<十神, Float> getFate() {
        return fate;
    }

    public Map<Integer, Float> getRelation() {
        return relation;
    }
}
