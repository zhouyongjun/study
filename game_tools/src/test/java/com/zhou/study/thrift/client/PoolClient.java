package com.zhou.study.thrift.client;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import com.zhou.study.thrift.calc.Calculator;

public class PoolClient 
{
    public static void main( String[] args)throws Exception
    {
    	TTransport transport = new TSocket("127.0.0.1", 9988);  
        transport.open();  
        
        TProtocol protocol = new TCompactProtocol(transport);  
        
        TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol, "Calculator");
        
        Calculator.Client calc = new Calculator.Client(mp);
        
        System.out.println(calc.add(1, 222));
        System.out.println(calc.multi(22, 33));
        
        transport.close();
    }
}
