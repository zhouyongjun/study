package com.zhou.study.thrift.calc;
/**����Calculator�����̣�
1.дһ�� Calculator.thrift�ļ�
	namespace java com.zhou.study.thrift.calc
	service Calculator{
		i32 add(1:i32 num1, 2:i32 num2)
		i64 multi(1:i32 num1, 2:i32 num2)
	}

2. ͨ��thrift-0.10.0.exe ��������java��
	thrift-0.10.0.exe -r -gen java Calculator.thrift�����Calculator.java

3.CalculatorImpl ʵ��Calculator.Iface�ӿ� 
*/