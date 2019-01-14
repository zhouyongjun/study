package com.zhou.util.calculator.unit.impl;

import com.zhou.util.calculator.unit.ICalculatorUnit;

/**
 * 人物体力
 * Created by xieshuqiang on 2015/7/25.
 */
public class AttPowerCalculatorUnit implements ICalculatorUnit {
    @Override
    public String getName() {
        return "attPower";
    }

    @Override
    public double getValue(Object param) {
        return /*((Player)param).gainAttVitality()*/0;
    }
}
