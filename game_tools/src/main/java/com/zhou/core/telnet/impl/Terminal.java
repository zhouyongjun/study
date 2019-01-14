package com.zhou.core.telnet.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zhou.core.telnet.TelnetHandle;
import com.zhou.util.StringUtils;

class Terminal implements Runnable
{
  private static final Logger logger = LogManager.getLogger(Terminal.class);
  private static final String TIP = "please input command:";
  private Socket conn;
  private PrintWriter writer;
  private BufferedReader reader;
  private TelnetHandle handler;

  public Terminal(Socket paramSocket, TelnetHandle paramConsoleService)
    throws Exception
  {
    this.conn = paramSocket;
    this.handler = paramConsoleService;
    this.writer = new PrintWriter(paramSocket.getOutputStream());
    this.reader = new BufferedReader(new InputStreamReader(paramSocket.getInputStream()));
    this.writer.println("**************************************");
    this.writer.println("welcome to telnet console");
    this.writer.println("**************************************");
    this.writer.println("please input command:");
    this.writer.flush();
  }

  public void run()
  {
    String readLine = null;
    try
    {
      while ((readLine = this.reader.readLine()) != null)
        try
        {
        String cmd  = getCmdWithoutCurosr(readLine);
        if (cmd.equals("quit") || cmd.equals("exit")) break;
          String result = this.handler.handle(cmd);
          if (result != null)
            this.writer.println("result: " + result);
          else
            this.writer.println("ERROR: invalid cmd");
        }
        catch (Exception localException)
        {
          this.writer.println("WARNING: illegal operation,"+StringUtils.toStringOfThrowable(localException));
        }
        finally
        {
          this.writer.println(TIP);
          this.writer.flush();
        }
    }
    catch (Exception localIOException3)
    {
    	logger.error(localIOException3);
    }
    finally
    {
      try
      {
        if (this.reader != null)
          this.reader.close();
        if (this.writer != null)
          this.writer.close();
      }
      catch (Exception localIOException4)
      {
        logger.error(localIOException4.toString());
      }
      logger.info("terminal close...");
    }
  }
  /**
   * ÅÅ³ý¹â±ê
   * @param cmd
   * @return
   */
	private String getCmdWithoutCurosr(String cmd) {
		char[] chars = cmd.toCharArray();
		StringBuffer sb =new StringBuffer();
		int mark = 0;
		for (int i=0;i<chars.length;i++)
		{
			char ch = chars[i];
			if (ch == 8)
			{
				mark ++;
				if (mark > sb.length()) mark = sb.length();
			}else 
			{
				if (mark > 0)
				{
					int del = sb.length()-mark;
					sb.replace(del, del+1, new StringBuffer().append(ch).toString());
					mark --;	
				}else
				{
					sb.append(ch);
				}
				
			}
			if (mark < 0) mark = 0;
//			System.err.println("char : " +ch+","+Integer.toHexString(ch)+",ch["+ch+"]="+(int)ch+",mark="+mark+",sb="+sb.toString());
		}
//		System.err.println("param : " + sb.toString());
	return sb.toString();
}

	public Socket getConn() {
		return conn;
	}
	
	public void setConn(Socket conn) {
		this.conn = conn;
	}
  
}

