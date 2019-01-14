package com.zhou.study.dbcp2;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
  
/** 
 * Created by jack on 2015/12/26. ���ݿ���������� 
 */
public class Dbcp2Util { 
 // private static final Logger LOGGER= 
 // LoggerFactory.getLogger(DatabaseHelper.class); 
 private static final String DRIVER; 
 private static final String URL; 
 private static final String USERNAME; 
 private static final String PASSWORD; 
 //��֤һ���߳�һ��Connection���̰߳�ȫ 
 private static final ThreadLocal<Connection> CONNECTION_HOLDER ; 
 //�̳߳� 
 private static final BasicDataSource DATA_SOURCE; 
 static { 
   CONNECTION_HOLDER = new ThreadLocal<Connection>(); 
    
  Properties conf = new Properties();
  DRIVER = conf.getProperty("jdbc.driver","com.mysql.jdbc.Driver"); 
  URL = conf.getProperty("jdbc.url","jdbc:mysql://10.80.9.37/cs_exp?useUnicode=true&characterEncoding=utf8"); 
  USERNAME = conf.getProperty("jdbc.username","root"); 
  PASSWORD = conf.getProperty("jdbc.password","lt.123"); 
    
//  String driver = conf.getProperty("jdbc.driver"); 
//  String url = conf.getProperty("jdbc.url"); 
//  String username = conf.getProperty("jdbc.username"); 
//  String passwrod = conf.getProperty("jdbc.password"); 
    
  DATA_SOURCE=new BasicDataSource(); 
  DATA_SOURCE.setDriverClassName(DRIVER); 
  DATA_SOURCE.setUrl(URL); 
  DATA_SOURCE.setUsername(USERNAME); 
  DATA_SOURCE.setPassword(PASSWORD); 
  //���ݿ����ӳز������ã�http://www.cnblogs.com/xdp-gacl/p/4002804.html 
  //http://greemranqq.iteye.com/blog/1969273 
  //http://blog.csdn.net/j903829182/article/details/50190337 
  //http://blog.csdn.net/jiutianhe/article/details/39670817 
  //http://bsr1983.iteye.com/blog/2092467 
  //http://blog.csdn.net/kerafan/article/details/50382998 
  //http://blog.csdn.net/a9529lty/article/details/43021801 
  ///���ÿ��кͽ��õ����ӵ������������ͬʱ���Լ�� 
  DATA_SOURCE.setMaxTotal(60); 
  //���ó�ʼ��С 
  DATA_SOURCE.setInitialSize(10); 
  //��С�������� 
  DATA_SOURCE.setMinIdle(8); 
  //���������� 
  DATA_SOURCE.setMaxIdle(16); 
  //��ʱ�ȴ�ʱ����� 
  DATA_SOURCE.setMaxWaitMillis(2*10000); 
  //ֻ�ᷢ�ֵ�ǰ����ʧЧ���ٴ���һ�����ӹ���ǰ��ѯʹ�� 
  DATA_SOURCE.setTestOnBorrow(true); 
  //removeAbandonedTimeout ������ʱ�����ƣ�����û����(����)�����ӣ�Ĭ��Ϊ 300�룬����Ϊ180�� 
  DATA_SOURCE.setRemoveAbandonedTimeout(180); 
  //removeAbandoned ������removeAbandonedTimeoutʱ����Ƿ�� ��û�����ӣ��������Ļ��գ�Ĭ��Ϊfalse������Ϊtrue) 
  //DATA_SOURCE.setRemoveAbandonedOnMaintenance(removeAbandonedOnMaintenance); 
  DATA_SOURCE.setRemoveAbandonedOnBorrow(true); 
  //testWhileIdle 
  DATA_SOURCE.setTestOnReturn(true); 
  //testOnReturn 
  DATA_SOURCE.setTestOnReturn(true); 
  //setRemoveAbandonedOnMaintenance 
  DATA_SOURCE.setRemoveAbandonedOnMaintenance(true); 
  //��¼��־ 
  DATA_SOURCE.setLogAbandoned(true); 
    
  //�����Զ��ύ 
  DATA_SOURCE.setDefaultAutoCommit(true); 
  // DATA_SOURCE.setEnableAutoCommitOnReturn(true); 
  System.out.println("����������ݿ����ӳ�DATA_SOURCE�Ĳ�������"); 
  /*try { 
   Class.forName(DRIVER); 
   System.out.println("load jdbc driver success"); 
  } catch (ClassNotFoundException e) { 
   // LOGGER.error("can not load jdbc driver",e); 
   System.out.println("can not load jdbc driver:" + e); 
  }finally{ 
     
  }*/ 
 } 
 //private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<Connection>(); 
  
 /** 
  * ��ȡ���ݿ����� 
  */ 
 public static Connection getConnection() { 
  Connection conn = CONNECTION_HOLDER.get();// 1 
  if (conn == null) { 
   try { 
    //conn = DriverManager.getConnection(URL, USERNAME, PASSWORD); 
    conn = DATA_SOURCE.getConnection(); 
    System.out.println("get connection success"); 
   } catch (SQLException e) { 
    // LOGGER.error("get connection failure", e); 
    System.out.println("get connection failure:" + e); 
   } finally { 
    System.out.println(" ��С��������MinIdle="+DATA_SOURCE.getMinIdle()); 
    System.out.println(" ����������MaxIdle="+DATA_SOURCE.getMaxIdle()); 
    System.out.println(" �����������MaxTotal="+DATA_SOURCE.getMaxTotal()); 
    System.out.println(" ��ʼ��СInitialSize="+DATA_SOURCE.getInitialSize()); 
    System.out.println(" ��ʱ�ȴ�ʱ��MaxWaitMillis="+(DATA_SOURCE.getMaxWaitMillis()/1000)); 
    System.out.println(" ��ȡ���������getNumActive()="+DATA_SOURCE.getNumActive()); 
    System.out.println(" ��ȡ������getNumIdle="+DATA_SOURCE.getNumIdle()); 
    CONNECTION_HOLDER.set(conn); 
   } 
  } 
  return conn; 
 } 
  
 /** 
  * �ر����ݿ����� 
  */ 
 public static void closeConnection() { 
  Connection conn = CONNECTION_HOLDER.get();// 1 
  if (conn != null) { 
   try { 
    conn.close(); 
    System.out.println("close connection success"); 
   } catch (SQLException e) { 
    System.out.println("close connection failure:" + e); 
    throw new RuntimeException(e); 
   } finally { 
    CONNECTION_HOLDER.remove(); 
   } 
  } 
 } 
  
 //�������ݿ���� 
 public static synchronized void update(String sql,Object... params) { 
  Connection conn = getConnection(); 
  if(conn==null){ 
   System.err.println("update���������()connectionΪnull!!"); 
   return;
  } 
  PreparedStatement pstmt=null; 
  System.out.println("update��ʼ!"); 
  try { 
   System.out.println("���µ�sql���Ϊ��sql->"+sql);
   pstmt = conn.prepareStatement(sql); 
   if(pstmt.executeUpdate()>0){ 
   } 
  } catch (SQLException e) { 
   //LOGGER.error("query entity list failure", e); 
   System.out.println("���������쳣connection="+conn); 
   System.out.println("update t_wx_ltnrk failure:" + e); 
   throw new RuntimeException(e); 
  } finally { 
   if(pstmt!=null){ 
    try { 
     pstmt.close(); 
    } catch (SQLException e) { 
     // TODO Auto-generated catch block 
     e.printStackTrace(); 
     System.out.println("PreparedStatementʧ��"); 
    } 
   } 
     
   if(conn!=null){ 
    try { 
     conn.close(); 
    } catch (SQLException e) { 
     // TODO Auto-generated catch block 
     e.printStackTrace(); 
    } 
   } 
   //�Ƴ��߳������Connection�����Ƴ��ᵼ��connection�ر��Ժ󣬻�ȡ��connection�� �ر�״̬�����ܽ������ݲ��� 
   CONNECTION_HOLDER.remove(); 
  } 
 } 
   
   
} 