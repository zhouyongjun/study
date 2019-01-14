package com.zhou.study.thrift.calc;
/**生成Calculator类流程：
1.写一份 Calculator.thrift文件
	namespace java com.zhou.study.thrift.calc
	service Calculator{
		i32 add(1:i32 num1, 2:i32 num2)
		i64 multi(1:i32 num1, 2:i32 num2)
	}

2. 通过thrift-0.10.0.exe 命令生成java类
	thrift-0.10.0.exe -r -gen java Calculator.thrift，获得Calculator.java

3.CalculatorImpl 实现Calculator.Iface接口 
*/