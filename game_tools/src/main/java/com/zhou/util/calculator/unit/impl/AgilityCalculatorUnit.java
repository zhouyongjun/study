package com.zhou.util.calculator.unit.impl;

import com.zhou.util.calculator.unit.ICalculatorUnit;

/**
 * »ÀŒÔ√ÙΩ›
 * Created by xieshuqiang on 2015/7/25.
 */
public class AgilityCalculatorUnit implements ICalculatorUnit {
    @Override
    public String getName() {
        return "agility";
    }

    @Override
    public double getValue(Object param) {
        return /*((Player)param).gainAttAgile()*/0;
    }
}
