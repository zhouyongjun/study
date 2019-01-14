package com.zhou.util.calculator;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ������ �Ӽ��˳�������power��
 * �̲߳���ȫ��ʹ�������
 * Created by xieshuqiang on 2015/7/25.
 */
public class CalculatorManager {

    private final Logger logger = LoggerFactory.getLogger(CalculatorManager.class);


    private static CalculatorManager inst = null;

    /**
     * Ԥ���빫ʽ
     */
    private final Map<String, Calculator> Calculators = new HashMap<>();


    private CalculatorManager() {
        init();
    }

    public static CalculatorManager getInst() {
        if (null != inst) {
            return inst;
        }
        synchronized (CalculatorManager.class) {
            if (inst == null) {
                inst = new CalculatorManager();
            }
        }
        return inst;
    }

    private void init() {
        /*List<Class<?>> classes = ClassUtils.scanPackage(CalculatorManager.class.getPackage().getName(), new ClassFilterImpl(new Class[]{ICalculatorUnit.class}));
        Calculator.init(classes);

        CommonConfig[] config = new CommonConfig[]{
                CommonConfig.WARRIOR_MAXHP,
                CommonConfig.WARRIOR_MAXMP,
                CommonConfig.WARRIOR_ATK,
                CommonConfig.WARRIOR_DEF,
                CommonConfig.WARRIOR_HIT,
                CommonConfig.WARRIOR_DODGE,
                CommonConfig.WARRIOR_ATK_SPEED,
                CommonConfig.MAGICIAN_MAXHP,
                CommonConfig.MAGICIAN_MAXMP,
                CommonConfig.MAGICIAN_ATK,
                CommonConfig.MAGICIAN_DEF,
                CommonConfig.MAGICIAN_HIT,
                CommonConfig.MAGICIAN_DODGE,
                CommonConfig.MAGICIAN_ATK_SPEED,
                CommonConfig.ARCHER_MAXHP,
                CommonConfig.ARCHER_MAXMP,
                CommonConfig.ARCHER_ATK,
                CommonConfig.ARCHER_DEF,
                CommonConfig.ARCHER_HIT,
                CommonConfig.ARCHER_DODGE,
                CommonConfig.ARCHER_ATK_SPEED,
                CommonConfig.DAILY_TASK_EXP,
                CommonConfig.DAILY_TASK_MONEY,
                CommonConfig.DYNAMIC_BOSS_FORMULAR_CONF,
                CommonConfig.DYNAMIC_NPC_FORMULAR_CONF,
                CommonConfig.DYNAMIC_ELITE_FORMULAR_CONF,
        };

        for (CommonConfig commonConfig : config) {
               getCalculator(commonConfig.getGlobalStrValue());
        }*/


    }

    public Calculator getCalculator(String expression) {
        Calculator calculator = Calculators.get(expression);
        if (calculator != null) {
            return calculator;
        }
        synchronized (Calculators) {
            calculator = Calculators.get(expression);
            if (calculator == null) {
                try {
                    calculator = new Calculator(expression);
                    logger.info("ע�ṫʽ {}",calculator);
                    Calculators.put(expression,calculator);
                } catch (Exception e) {
                    logger.error("��ʽ������:"+expression, e);
                }
            }
            return calculator;
        }
    }

    public static void main(String[] arg) {
        CalculatorManager.getInst();
        Map<String, Number> values = new HashMap<>();
        values.put("a", 1);
        values.put("b", 2);
        values.put("c", 3);
        values.put("d", 4);
        Calculator calculator = null;

        calculator = CalculatorManager.getInst().getCalculator("0.100");
        System.out.println(calculator.calculate(values) + "___" + 0.1);

        calculator = CalculatorManager.getInst().getCalculator("a+b+c+d");
        System.out.println(calculator.calculate(values) + "___" + 10);

        calculator = CalculatorManager.getInst().getCalculator("a+b-c+d");
        System.out.println(calculator.calculate(values) + "___" + 4);

        calculator = CalculatorManager.getInst().getCalculator("a+(b-c)+d");
        System.out.println(calculator.calculate(values) + "___" + 4);

        calculator = CalculatorManager.getInst().getCalculator("a*b+c+d");
        System.out.println(calculator.calculate(values) + "___" + 9);

        calculator = CalculatorManager.getInst().getCalculator("a*b+c*d");
        System.out.println(calculator.calculate(values) + "___" + 14);

        calculator = CalculatorManager.getInst().getCalculator("a/b*c+d");
        System.out.println(calculator.calculate(values) + "___" + 5.5);
    }
}
