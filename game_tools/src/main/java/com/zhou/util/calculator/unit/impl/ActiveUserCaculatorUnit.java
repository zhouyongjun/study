package com.zhou.util.calculator.unit.impl;

import com.zhou.util.calculator.unit.ICalculatorUnit;

/**
 * ªÓ‘æ»À ˝
 * @author jkp
 *
 */
public class ActiveUserCaculatorUnit implements ICalculatorUnit{

	@Override
	public String getName() {
		return "activeUser";
	}

	@Override
	public double getValue(Object params) {
		return /*DynamicBossRefreshHelper.getDau()*/0;
	}

}
