package com.zhou.util.calculator;

import io.netty.util.internal.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhou.util.RandomUtils;
import com.zhou.util.calculator.unit.ICalculatorUnit;

/**
 * 计算器 加减乘除^ 函数拓展
 * Created by xieshuqiang on 2015/7/25.
 */
public class Calculator {
    private static final boolean IsDebugging = false;
    private static final Logger logger = LoggerFactory.getLogger(Calculator.class);
    public static final int S_HEAD = 0;
    public static final int S_NUMBER = 1;
    public static final int S_PARAM = 2;
    public static final int S_OPERATOR = 3;
    public static final Set<Character> OPERATORS = Collections.unmodifiableSet(new HashSet<Character>() {{
        add('+');
        add('-');
        add('*');
        add('/');
        add('^');
    }});

    /**
     * 计算单元
     */
    private static final Map<String, ICalculatorUnit> CalculatorUnits = new HashMap<>();


    protected static void init(List<Class<?>> classes) {
        for (Class<?> aClass : classes) {
            try {
                ICalculatorUnit instance = (ICalculatorUnit) aClass.newInstance();
                CalculatorUnits.put(instance.getName(), instance);
            } catch (Exception e) {
                logger.error("计算单元加载错误:{}", e);
            }
        }
    }
    //////////////////////////////////////////////内部类//////////////////////////////////////////////

    /**
     * calculator unit
     */
    private abstract class Unit {
        abstract double getValue(Object param);
    }

    /**
     * calculator param
     */
    private class Param extends Unit {
        final String name;

        public Param(String name) {
            this.name = name;
        }

        @Override
        double getValue(Object param) {
            double value = 0;
            Number number = Calculator.this.getValue(param, name);
            if (number != null) {
                value = number.doubleValue();
            }
            return value;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * 常熟节点
     */
    private class Const extends Param {
        public Const(String name) {
            super(name);
        }

        @Override
        double getValue(Object param) {
            double value = 0;
            if (!StringUtil.isNullOrEmpty(name)) {
                value = Double.parseDouble(name);
            }
            return value;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * 操作符枚举
     */
    private enum OperatorEnum {
        ADD(1, "+"),
        SUBTRACT(1, "-"),
        MULTIPLE(2, "*"),
        DIVIDE(2, "/"),
        POWER(3, "^"),
        NEGATIVE(5, 1, "-"),
        FUNCTION(6, "Function"),
        COMMA(-1, ","),
        LEFT_PARENTHESIS(-1, 0, "("),
        RIGHT_PARENTHESIS(-1, 0, ")"),;
        final int prior;
        final int param;
        final String show;

        OperatorEnum(int prior, String show) {
            this.prior = prior;
            this.param = 2;
            this.show = show;
        }

        OperatorEnum(int prior, int param, String show) {
            this.prior = prior;
            this.param = param;
            this.show = show;
        }

    }

    private class Operator extends Unit {
        final OperatorEnum operator;
        Unit left;
        Unit right;

        public Operator(OperatorEnum operator) {
            if (operator == null) {
                throw new RuntimeException("操作符为空");
            }
            this.operator = operator;
            this.right = null;
            this.left = null;
        }

        @Override
        double getValue(Object param) {
            double leftValue = 0;
            double rightValue = 0;
            if (left != null) {
                leftValue = left.getValue(param);
            }
            if (right != null) {
                rightValue = right.getValue(param);
            }

            switch (this.operator) {
                case ADD: {
                    if (IsDebugging) {
                        System.out.printf(" [%s](%g) + [%s](%g) = %g \n", left, leftValue, right, rightValue, leftValue + rightValue);
                    }
                    return leftValue + rightValue;
                }
                case SUBTRACT: {
                    if (IsDebugging) {
                        System.out.printf(" [%s](%g) - [%s](%g) = %g \n", left, leftValue, right, rightValue, leftValue - rightValue);
                    }
                    return leftValue - rightValue;
                }
                case MULTIPLE: {
                    if (IsDebugging) {
                        System.out.printf(" [%s](%g) * [%s](%g) = %g \n", left, leftValue, right, rightValue, leftValue * rightValue);
                    }
                    return leftValue * rightValue;
                }
                case DIVIDE: {
                    if (IsDebugging) {
                        System.out.printf(" [%s](%g) / [%s](%g) = %g \n", left, leftValue, right, rightValue, leftValue / rightValue);
                    }
                    return leftValue / rightValue;
                }
                case POWER: {
                    if (IsDebugging) {
                        System.out.printf(" [%s](%g) ^ [%s](%g) = %g \n", left, leftValue, right, rightValue, Math.pow(leftValue, rightValue));
                    }
                    return Math.pow(leftValue, rightValue);
                }
                case NEGATIVE: {
                    if (IsDebugging) {
                        System.out.printf(" -[%s](%g) = %g \n", right, rightValue, -rightValue);
                    }
                    return -rightValue;
                }
            }
            return 0;
        }

        @Override
        public String toString() {
            return String.format("[%s %s %s]", left, operator.show, right);
        }
    }

    private class Function extends Unit {
        final String function;
        final List<Unit> parameters = new ArrayList<>(2);

        public Function(String function) {
            this.function = function;
        }

        @Override
        double getValue(Object param) {
            switch (function) {
                case "max": {
                    double value = Double.MIN_VALUE;
                    for (Unit unit : this.parameters) {
                        value = Math.max(value, unit.getValue(param));
                    }
                    return value;
                }
                case "min": {
                    double value = Double.MAX_VALUE;
                    for (Unit unit : this.parameters) {
                        value = Math.min(value, unit.getValue(param));
                    }
                    return value;
                }
                case "random": {
                    switch (this.parameters.size()) {
                        case 0: {
                            return RandomUtils.random(Integer.MAX_VALUE);
                        }
                        case 1: {
                            return RandomUtils.random((int) this.parameters.get(0).getValue(param));
                        }
                        case 2: {
                            return RandomUtils.randomFloatValue((float) this.parameters.get(0).getValue(param), (float) this.parameters.get(1).getValue(param));
                        }
                    }
                }
                case "int":{
                    return (int)this.parameters.get(0).getValue(param);
                }
                case "sqrt":{
                    return Math.sqrt(this.parameters.get(0).getValue(param));
                }
            }
            return 0;
        }

        @Override
        public String toString() {
            return function + "(" + parameters + ")";
        }
    }

    private class Value extends Unit {

        public Unit value;

        public Value(Unit unit) {
            this.value = unit;
        }

        @Override
        double getValue(Object param) {
            return null == value ? 0 : value.getValue(param);
        }

        @Override
        public String toString() {
            return value == null ? "" : value.toString();
        }
    }


    private final String expression;
    private Unit root;

    protected Calculator(String expression) {
        this.expression = expression;
        this.parse();
    }

    public double calculate(Object param) {
        return root.getValue(param);
    }

    private Number getValue(Object param, String key) {
        if (param instanceof Map) {
            return (Number) ((Map) param).get(key);
        } else {
            ICalculatorUnit unit = CalculatorUnits.get(key);
            if (unit != null) {
                return unit.getValue(param);
            }
            return 0;
        }
    }

    /**
     * 解释公式
     * a+b+c+d*e+a
     */
    private void parse() {
        Stack<Unit> stack = new Stack<>();
        int state = 0;// 0 首字母 ，1数值，2变量，3操作符
        StringBuilder number = new StringBuilder();
        StringBuilder param = new StringBuilder();
        String expression = this.expression.trim().replace(" ", "");
        Operator previous = null;
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (IsDebugging) {
                System.out.print(c);
            }
            switch (state) {
                case S_HEAD: {
                    char tmp = (char) (c | 0x20);
                    if ('a' <= tmp && tmp <= 'z') {
                        state = S_PARAM;
                        param.append(c);
                    } else if (OPERATORS.contains(c)) {
                        state = S_OPERATOR;
                        i--;
                    } else if (('0' <= c && c <= '9') || '.' == c) {
                        state = S_NUMBER;
                        number.append(c);
                    } else if ('(' == c) {
                        Operator left_parenthesis = new Operator(OperatorEnum.LEFT_PARENTHESIS);
                        stack.push(left_parenthesis);
                        previous = left_parenthesis;
                    } else if (')' == c) {
                        if (i >= expression.length() - 1) {
                            System.out.println("");
                        }
                        boolean isFunction = false;
                        Deque<Unit> cache = new LinkedList<>();

                        Unit pop = stack.pop();
                        cache.offer(pop);
                        while (!(pop instanceof Operator) || (((Operator) pop).operator) != OperatorEnum.LEFT_PARENTHESIS) {
                            pop = stack.pop();
                            cache.offer(pop);
                        }
                        //左括号的左边
                        if (!stack.isEmpty()) {
                            pop = stack.pop();
                            cache.offer(pop);
                            if (pop instanceof Function) {
                                isFunction = true;
                            }
                            if (!isFunction) {
                                stack.push(cache.pollLast());
                            }
                        }
                        if (!isFunction) {
                            // ( a+b*c
                            cache.pollLast();
                            //a+b*c
                            Unit convergence = convergence(cache);
                            if(!stack.isEmpty()){
                                Unit peek = stack.peek();
                                if(peek instanceof Operator) {
                                    previous = (Operator) peek;
                                }else{
                                    previous = null;
                                }
                            }else{
                                previous = null;
                            }
                            stack.push(convergence);
                        } else {
                            //func(a,b+c*d^-e,+,b
                            Deque<Unit> parameters = new LinkedList<>();
                            while (!cache.isEmpty()) {
                                Deque<Unit> deque = new LinkedList<>();
                                pop = cache.pop();
                                deque.offer(pop);
                                while (!(pop instanceof Operator) || (OperatorEnum.COMMA != ((Operator) pop).operator && OperatorEnum.LEFT_PARENTHESIS != ((Operator) pop).operator)) {
                                    pop = cache.pop();
                                    deque.offer(pop);
                                }
                                deque.pollLast();
                                Unit convergence = convergence(deque);
                                parameters.offer(convergence);

                                if (OperatorEnum.LEFT_PARENTHESIS == ((Operator) pop).operator) {
                                    Function function = (Function) cache.pop();
                                    while (!parameters.isEmpty()) {
                                        function.parameters.add(parameters.pop());
                                    }
                                    Value value = new Value(function);
                                    stack.push(value);
                                }
                            }
                            previous = null;

                        }

                    } else if (',' == c) {
                        Operator comma = new Operator(OperatorEnum.COMMA);
                        stack.push(comma);
                    }
                    break;
                }
                case S_NUMBER: {
                    if (('0' <= c && c <= '9') || '.' == c) {
                        number.append(c);
                    } else {
                        stack.push(new Const(number.toString()));
                        number.delete(0, number.length());
                        state = S_HEAD;
                        i--;
                    }
                    break;
                }
                case S_PARAM: {
                    char tmp = (char) (c | 0x20);
                    if (('a' <= tmp && tmp <= 'z') || ('0' <= c && c <= '9')) {
                        param.append(c);
                    } else {
                        if ('(' == c) {
                            stack.push(new Function(param.toString()));
                        } else {
                            stack.push(new Param(param.toString()));
                        }
                        param.delete(0, param.length());
                        state = S_HEAD;
                        i--;
                    }
                    break;
                }
                case S_OPERATOR: {
                    if (OPERATORS.contains(c)) {
                        Operator operator = null;
                        switch (c) {
                            case '+': {
                                operator = new Operator(OperatorEnum.ADD);
                                stack.push(operator);
                                break;
                            }
                            case '-': {
                                if (stack.isEmpty()) {
                                    operator = new Operator(OperatorEnum.NEGATIVE);
                                } else {
                                    char lastChar = expression.charAt(i - 1);
                                    if (OPERATORS.contains(lastChar) || '(' == lastChar) {
                                        operator = new Operator(OperatorEnum.NEGATIVE);
                                    } else {
                                        operator = new Operator(OperatorEnum.SUBTRACT);
                                    }
                                }
                                stack.push(operator);
                                break;
                            }
                            case '*': {
                                operator = new Operator(OperatorEnum.MULTIPLE);
                                stack.push(operator);
                                break;
                            }
                            case '/': {
                                operator = new Operator(OperatorEnum.DIVIDE);
                                stack.push(operator);
                                break;
                            }
                            case '^': {
                                operator = new Operator(OperatorEnum.POWER);
                                stack.push(operator);
                                break;
                            }
                        }
                        if (previous != null) {
                            if (operator.operator.prior <= previous.operator.prior) {
                                Unit curr = stack.pop();
                                Unit right = stack.pop();

                                Operator op = (Operator) stack.pop();
                                Unit left = null;
                                if (op.operator.param > 1 && !stack.isEmpty()) {
                                    left = stack.pop();
                                }
                                op.left = left;
                                op.right = right;
                                Value value = new Value(op);
                                stack.push(value);
                                stack.push(curr);
                            }
                        }
                        if (',' != c) {
                            previous = operator;
                        } else {
                            previous = null;
                        }
                        state = S_HEAD;
                    } else {
                        state = S_HEAD;
                        i--;
                    }
                    break;
                }
            }
        }
        switch (state) {
            case S_NUMBER: {
                stack.push(new Const(number.toString()));
                break;
            }
            case S_PARAM: {
                stack.push(new Param(param.toString()));
                break;
            }
        }
        root = convergence(stack);
        if (IsDebugging) {
            System.out.println();
        }
    }

    private Unit convergence(Stack<Unit> ref) {
        if (ref.size() > 1) {
            Stack<Unit> stack = new Stack<>();
            Operator previous = null;
            for (Unit unit : ref) {
                if (unit instanceof Operator) {
                    if (null != previous) {
                        Operator operator = (Operator) unit;
                        if (operator.operator.prior <= previous.operator.prior) {
                            Unit right = stack.pop();
                            Unit pop = stack.pop();
                            Operator op = (Operator) pop;
                            Unit left = null;
                            if (op.operator.param > 1 && !stack.isEmpty()) {
                                left = stack.pop();
                            }
                            op.left = left;
                            op.right = right;
                            Value value = new Value(op);
                            stack.push(value);
                        }
                    }
                    previous = (Operator) unit;
                }
                stack.push(unit);

            }
            ref = stack;
            while (!ref.isEmpty()) {
                Unit right = ref.pop();
                Operator op = (Operator) ref.pop();
                Unit left = null;
                if (op.operator.param > 1) {
                    if (!ref.isEmpty()) {
                        left = ref.pop();
                    }
                }
                op.left = left;
                op.right = right;
                Value value = new Value(op);
                if (!ref.isEmpty()) {
                    ref.push(value);
                } else {
                    return value;
                }
            }
            return null;
        } else {
            return ref.pop();
        }
    }

    private Unit convergence(Deque<Unit> ref) {
        if (ref.size() > 1) {
            Stack<Unit> stack = new Stack<>();
            Operator previous = null;
            Iterator<Unit> descendingIterator = ref.descendingIterator();
            for (; descendingIterator.hasNext(); ) {
                Unit unit = descendingIterator.next();
                if (unit instanceof Operator) {
                    if (null != previous) {
                        Operator operator = (Operator) unit;
                        if (operator.operator.prior <= previous.operator.prior) {
                            Unit right = stack.pop();
                            Unit pop = stack.pop();
                            Operator op = (Operator) pop;
                            Unit left = null;
                            if (op.operator.param > 1 && !stack.isEmpty()) {
                                left = stack.pop();
                            }
                            op.left = left;
                            op.right = right;
                            Value value = new Value(op);
                            stack.push(value);
                        }
                    }
                    previous = (Operator) unit;

                }
                stack.push(unit);

            }
            while (!stack.isEmpty()) {
                Unit right = stack.pop();
                Operator op = (Operator) stack.pop();
                Unit left = null;
                if (op.operator.param > 1) {
                    if (!stack.isEmpty()) {
                        left = stack.pop();
                    }
                }
                op.left = left;
                op.right = right;
                Value value = new Value(op);
                if (!stack.isEmpty()) {
                    stack.push(value);
                } else {
                    return value;
                }
            }
            return null;
        } else {
            return ref.pop();
        }
    }

    @Override
    public String toString() {
        return String.format("%s => %s",this.expression,this.root.toString());
    }

    //    String exp = "(((attack-attackCoefficient*attack*(1-ignorePercentage/10000))*fixedPercentage/10000*(1+addInjure/10000)*(1-maxDamagePercentage/10000+maxDamagePercentage/10000*(1.2+maxDamage))*(1-remarkablePercentage/10000+remarkablePercentage/10000*(1.4+remarkableDamage))+fixedAdd)*100/(100-attackSpeed/10000)*hitTimes/10000+hitTimes/10000*petAttack*petDamage";
    public static void main(String[] arg) {
        Calculator calculator;

//        Map<String, Number> values = new HashMap<>();
//        values.put("playerLevel", 10);
//        values.put("monsterLevel", 2);
//        values.put("monsterExp", 100);
//        values.put("buffAdd", 0.5);
//        values.put("teamAdd", 0.3);
//        values.put("teamTotalLevel", 20);
//
//        values.put("attack", 100);
//        values.put("attackCoefficient", 100);
//        values.put("ignorePercentage", 100);
//        values.put("fixedPercentage", 100);
//        values.put("addInjure", 100);
//        values.put("maxDamagePercentage", 100);
//        values.put("maxDamage", 100);
//        values.put("remarkablePercentage", 100);
//        values.put("remarkableDamage", 100);
//        values.put("fixedAdd", 100);
//        values.put("attackSpeed", 100);
//        values.put("hitTimes", 100);
//        values.put("petAttack", 100);
//        values.put("petDamage", 100);


//        double attack = 100.f;
//        double attackCoefficient = 100.f;
//        double ignorePercentage = 100.f;
//        double fixedPercentage = 100.f;
//        double addInjure = 100.f;
//        double maxDamagePercentage = 100.f;
//        double maxDamage = 100.f;
//        double remarkablePercentage = 100.f;
//        double remarkableDamage = 100.f;
//        double fixedAdd = 100.f;
//        double attackSpeed = 100.f;
//        double hitTimes = 100.f;
//        double petAttack = 100.f;
//        double petDamage = 100.f;
//        double r = (((attack - attackCoefficient * attack * (1 - ignorePercentage / 10000)) * fixedPercentage / 10000 * (1 + addInjure / 10000) * (1 - maxDamagePercentage / 10000 + maxDamagePercentage / 10000 * (1.2 + maxDamage)) * (1 - remarkablePercentage / 10000 + remarkablePercentage / 10000 * (1.4 + remarkableDamage)) + fixedAdd) * 100 / (100 - attackSpeed / 10000) * hitTimes / 10000 + hitTimes / 10000 * petAttack * petDamage);
//        System.out.println(String.format("result: %g", r));
//        double a = 100;
//        a = (((a - a * a * (1 - a / 10000)) * a / 10000 * (1 + a / 10000) * (1 - a / 10000 + a / 10000 * (1.2 + a)) * (1 - a / 10000 + a / 10000 * (1.4 + a)) + a) * 100 / (100 - a / 10000) * a / 10000 + a / 10000 * a * a);
//        System.out.println(String.format("result: %g", a));
//        String exp = "(((attack-attackCoefficient*attack*(1-ignorePercentage/10000))*fixedPercentage/10000*(1+addInjure/10000)*(1-maxDamagePercentage/10000+maxDamagePercentage/10000*(1.2+maxDamage))*(1-remarkablePercentage/10000+remarkablePercentage/10000*(1.4+remarkableDamage))+fixedAdd)*100/(100-attackSpeed/10000)*hitTimes/10000+hitTimes/10000*petAttack*petDamage";
//        exp = "(((a - a * a * (1 - a / 10000)) * a / 10000 * (1 + a / 10000) * (1 - a / 10000 + a / 10000 * (1.2 + a)) * (1 - a / 10000 + a / 10000 * (1.4 + a)) + a) * 100 / (100 - a / 10000) * a / 10000 + a / 10000 * a * a)";
//        calculator = new Calculator(exp);
//        values.put("a",100);
//        System.out.println(calculator.calculate(values) + "___" + 1);

//        double a = 100;
//        double b = 100;
//        double c = 100;
//        double d = 100;
//        double e = 100;
//        double f = 100;
//        double g = 100;
//        double h = 100;
//        String exp = "(a*b)*(c*d)+(e*f)*(g*h)";
//        calculator = new Calculator(exp);
//        values.put("a", 100);
//        values.put("b", 100);
//        values.put("c", 100);
//        values.put("d", 100);
//        values.put("e", 100);
//        values.put("f", 100);
//        values.put("g", 100);
//        values.put("h", 100);
//        System.out.println(calculator.calculate(values) + "___" + ((a*b)*(c*d)+(e*f)*(g*h)));

                Map<String, Number> values = new HashMap<>();
        values.put("a", 1);
        values.put("b", 2);
        values.put("c", 2);
        values.put("d", 3);
        values.put("e", 4);

        calculator = new Calculator("(a+b)-c");
        System.out.println(calculator.calculate(values) + "___" + 1);

        calculator = new Calculator("a+b*c^-d");
        System.out.println(calculator.calculate(values) + "___" + 1.25);

        calculator = new Calculator("min(a,b)");
        System.out.println(calculator.calculate(values) + "___" + 1);

        calculator = new Calculator("a+b+c^d");
        System.out.println(calculator.calculate(values) + "___" + 11);

        calculator = new Calculator("a^b+c^d");
        System.out.println(calculator.calculate(values) + "___" + 9);

        calculator = new Calculator("a+b^c+d");
        System.out.println(calculator.calculate(values) + "___" + 8);

        calculator = new Calculator("a^b+c+d");
        System.out.println(calculator.calculate(values) + "___" + 5);


        calculator = new Calculator("a+b-c+d");
        System.out.println(calculator.calculate(values) + "___" + 4);

        calculator = new Calculator("a+(b-c)+d");
        System.out.println(calculator.calculate(values) + "___" + 4);

        calculator = new Calculator("a*b+c+d");
        System.out.println(calculator.calculate(values) + "___" + 7);

        calculator = new Calculator("a*b+c*d");
        System.out.println(calculator.calculate(values) + "___" + 8);

        calculator = new Calculator("a/b*c+d");
        System.out.println(calculator.calculate(values) + "___" + 4);
    }
}
