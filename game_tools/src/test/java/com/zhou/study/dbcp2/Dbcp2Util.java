package com.zhou.study.dbcp2;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
  
/** 
 * Created by jack on 2015/12/26. 数据库操作助手类 
 */
public class Dbcp2Util { 
 // private static final Logger LOGGER= 
 // LoggerFactory.getLogger(DatabaseHelper.class); 
 private static final String DRIVER; 
 private static final String URL; 
 private static final String USERNAME; 
 private static final String PASSWORD; 
 //保证一个线程一个Connection，线程安全 
 private static final ThreadLocal<Connection> CONNECTION_HOLDER ; 
 //线程池 
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
  //数据库连接池参数配置：http://www.cnblogs.com/xdp-gacl/p/4002804.html 
  //http://greemranqq.iteye.com/blog/1969273 
  //http://blog.csdn.net/j903829182/article/details/50190337 
  //http://blog.csdn.net/jiutianhe/article/details/39670817 
  //http://bsr1983.iteye.com/blog/2092467 
  //http://blog.csdn.net/kerafan/article/details/50382998 
  //http://blog.csdn.net/a9529lty/article/details/43021801 
  ///设置空闲和借用的连接的最大总数量，同时可以激活。 
  DATA_SOURCE.setMaxTotal(60); 
  //设置初始大小 
  DATA_SOURCE.setInitialSize(10); 
  //最小空闲连接 
  DATA_SOURCE.setMinIdle(8); 
  //最大空闲连接 
  DATA_SOURCE.setMaxIdle(16); 
  //超时等待时间毫秒 
  DATA_SOURCE.setMaxWaitMillis(2*10000); 
  //只会发现当前连接失效，再创建一个连接供当前查询使用 
  DATA_SOURCE.setTestOnBorrow(true); 
  //removeAbandonedTimeout ：超过时间限制，回收没有用(废弃)的连接（默认为 300秒，调整为180） 
  DATA_SOURCE.setRemoveAbandonedTimeout(180); 
  //removeAbandoned ：超过removeAbandonedTimeout时间后，是否进 行没用连接（废弃）的回收（默认为false，调整为true) 
  //DATA_SOURCE.setRemoveAbandonedOnMaintenance(removeAbandonedOnMaintenance); 
  DATA_SOURCE.setRemoveAbandonedOnBorrow(true); 
  //testWhileIdle 
  DATA_SOURCE.setTestOnReturn(true); 
  //testOnReturn 
  DATA_SOURCE.setTestOnReturn(true); 
  //setRemoveAbandonedOnMaintenance 
  DATA_SOURCE.setRemoveAbandonedOnMaintenance(true); 
  //记录日志 
  DATA_SOURCE.setLogAbandoned(true); 
    
  //设置自动提交 
  DATA_SOURCE.setDefaultAutoCommit(true); 
  // DATA_SOURCE.setEnableAutoCommitOnReturn(true); 
  System.out.println("完成设置数据库连接池DATA_SOURCE的参数！！"); 
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
  * 获取数据库连接 
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
    System.out.println(" 最小空闲连接MinIdle="+DATA_SOURCE.getMinIdle()); 
    System.out.println(" 最大空闲连接MaxIdle="+DATA_SOURCE.getMaxIdle()); 
    System.out.println(" 最大连接数量MaxTotal="+DATA_SOURCE.getMaxTotal()); 
    System.out.println(" 初始大小InitialSize="+DATA_SOURCE.getInitialSize()); 
    System.out.println(" 超时等待时间MaxWaitMillis="+(DATA_SOURCE.getMaxWaitMillis()/1000)); 
    System.out.println(" 获取活动的连接数getNumActive()="+DATA_SOURCE.getNumActive()); 
    System.out.println(" 获取连接数getNumIdle="+DATA_SOURCE.getNumIdle()); 
    CONNECTION_HOLDER.set(conn); 
   } 
  } 
  return conn; 
 } 
  
 /** 
  * 关闭数据库连接 
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
  
 //进行数据库操作 
 public static synchronized void update(String sql,Object... params) { 
  Connection conn = getConnection(); 
  if(conn==null){ 
   System.err.println("update方法里面的()connection为null!!"); 
   return;
  } 
  PreparedStatement pstmt=null; 
  System.out.println("update开始!"); 
  try { 
   System.out.println("更新的sql语句为：sql->"+sql);
   pstmt = conn.prepareStatement(sql); 
   if(pstmt.executeUpdate()>0){ 
   } 
  } catch (SQLException e) { 
   //LOGGER.error("query entity list failure", e); 
   System.out.println("更新数据异常connection="+conn); 
   System.out.println("update t_wx_ltnrk failure:" + e); 
   throw new RuntimeException(e); 
  } finally { 
   if(pstmt!=null){ 
    try { 
     pstmt.close(); 
    } catch (SQLException e) { 
     // TODO Auto-generated catch block 
     e.printStackTrace(); 
     System.out.println("PreparedStatement失败"); 
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
   //移除线程里面的Connection，不移除会导致connection关闭以后，获取的connection是 关闭状态，不能进行数据操作 
   CONNECTION_HOLDER.remove(); 
  } 
 } 
   
   
} 