

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

public class LogAnalysisMain
{
  private static Properties configs = new Properties();
  private static Set<String> anaLogFileSet = new HashSet<>();
  private static Set<String> msgSet = new HashSet<>();
  private static Map<String, List<String>> resultMap = new TreeMap<>();

  public static void main(String[] args) throws Exception
 {
		//初始化配置
		init();
		if ((args == null) || (args.length == 0)) {
			System.out.println("please input msg...");
			return;
		}
		StringBuffer show_msgs = new StringBuffer();
		String[] msg = args[0].split("\\@");
		show_msgs.append("condition[");
		//0:条件
		for (String s : msg) {
			if (s.trim().length() == 0) {
				continue;
			}
			msgSet.add(s.trim());
			show_msgs.append(s.trim()).append(",");
		}
		show_msgs.append("]");
		//4:搜索路径
		List<String> fileNames = getConfigFileNames();
		String defalutFileName = "./log/";
		if (args.length > 4) {
			fileNames.add(args[4]);
		}else
		{
			if (!fileNames.contains(defalutFileName)) fileNames.add(defalutFileName);
		}
		long beginTime = 0L;
		long endTime = System.currentTimeMillis() + 3600000L;
		int orderByTime = 0;
		//3:是否排序  1：正序 2：倒序
		if (args.length > 3) {
			orderByTime = Integer.parseInt(args[3]);
		}
		//2:结束时间配置
		//1:开始时间配置
		if (args.length > 2) {
			String b = args[1];
			String e = args[2];
			beginTime = parseTime(b);
			endTime = parseTime(e);
		} else if (args.length > 1) {
			String b = args[1];
			beginTime = parseTime(b);
		}
		String endFormat = formatTime(endTime);
		System.out.println("beginTime : " + beginTime + " "
				+ formatTime(beginTime));
		System.out.println("endTime : " + endTime + " " + endFormat);
		System.out.println(show_msgs.toString());
		System.out.println("orderByTime:" + orderByTime);
		long all_date = System.currentTimeMillis();
		for (String fileName : fileNames)
		{
			File file = new File(fileName);
			System.out.println("  file path : " + file.getPath());
			long ana_date = System.currentTimeMillis();
			//分析文件
			analysisLog(file, beginTime, endTime);
			System.out.println("\tanalysis log use millisecond :"
					+ (System.currentTimeMillis() - ana_date));
		}
		System.out.println("all analysis log use millisecond :"
				+ (System.currentTimeMillis() - all_date));
		//是否排序
		if ((orderByTime == 1) || (orderByTime == 2)) {
			resultLogOrderByTime(msg[0], orderByTime, endFormat);
		} else {
			resultLog(msg[0]);
		}
	}

  private static List<String> getConfigFileNames() {
	  List<String> list = new ArrayList<String>();
	  if (configs != null)
	  {
		  String line = configs.getProperty("ana.filepath");
		  if (line != null && line.length() > 0)
		  {
			  list.addAll(Arrays.asList(line.trim().split(";")));
			  list.remove("");
		  }
	  }
	return list;
}

private static void resultLogOrderByTime(String msg, int orderByTime, String endFormat)
  {
    List<LogAnalysisMain.OrderByLine> resultOfOrder = resultOrderByTime(orderByTime, endFormat);
    File file = new File("./result/result." + msg + "." + System.currentTimeMillis() + ".log");
    PrintWriter pw = null;
    try {
      int bytes = 0;
      File path = file.getParentFile();
      path.mkdirs();
      pw = new PrintWriter(file, "UTF-8");
      for (LogAnalysisMain.OrderByLine order : resultOfOrder) {
        StringBuilder sb = new StringBuilder();
        sb.append(order.msg).append(" -- ").append(order.fileName);
        bytes += sb.length();
        pw.println(sb.toString());
      }

      pw.flush();
      System.out.println("result bytes=" + bytes + ",log:" + file.getAbsolutePath());
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (pw != null)
        pw.flush();
    }
  }

  private static List<LogAnalysisMain.OrderByLine> resultOrderByTime(final int orderByTime, String endFormat)
  {
    List<LogAnalysisMain.OrderByLine> orders = new ArrayList<LogAnalysisMain.OrderByLine>();
    List<LogAnalysisMain.OrderByLine> notOrders = new ArrayList<LogAnalysisMain.OrderByLine>();
    for (Map.Entry<String, List<String>> entry : resultMap.entrySet())
    {
      for (String msg : entry.getValue()) {
        try
        {
          String timeFormat = null;
          int index = msg.indexOf(".");
          if ((index < 8) || (index > 25)) {
            boolean isNot = true;
            if (msg.length() > 20)
            {
              String parse = msg.substring(0, 19);
              if ((parse.contains("-")) && (parse.contains(":")))
              {
                isNot = false;
                index = 19;
              }
            }
            if (isNot)
            {
              LogAnalysisMain.OrderByLine order = new LogAnalysisMain.OrderByLine();
              order.time = 0L;
              order.msg = msg;
              order.fileName = ((String)entry.getKey());
              notOrders.add(order);
              continue;
            }
          }
          else
          {
            index += 4;
          }
          timeFormat = msg.substring(0, index);
          if (timeFormat.indexOf(":") < 4)
          {
            timeFormat = endFormat + " " + timeFormat;
          }

          if (!timeFormat.contains("."))
          {
            timeFormat = timeFormat + ".000";
          }
          LogAnalysisMain.OrderByLine order = new LogAnalysisMain.OrderByLine();
          order.time = parseFullTime(timeFormat);
          order.msg = msg;
          order.fileName = ((String)entry.getKey());
          orders.add(order);
        } catch (Exception e) {
          e.printStackTrace();
          LogAnalysisMain.OrderByLine order = new LogAnalysisMain.OrderByLine();
          order.time = 0L;
          order.msg = msg;
          order.fileName = ((String)entry.getKey());
          notOrders.add(order);
        }
      }
    }

    resultMap.clear();
    Collections.sort(orders, new Comparator<LogAnalysisMain.OrderByLine>()
    {
      public int compare(LogAnalysisMain.OrderByLine o1, LogAnalysisMain.OrderByLine o2)
      {
        long detal = 0L;
        long t1 = o1.time;
        long t2 = o2.time;
        if (orderByTime == 1)
        {
          detal = t1 - t2;
        }
        else
        {
          detal = t2 - t1;
        }
        if (detal < 0L) return -1;
        if (detal > 0L) return 1;
        return 0;
      }
    });
    if (!notOrders.isEmpty())
    {
      LogAnalysisMain.OrderByLine order = new LogAnalysisMain.OrderByLine();
      order.msg = "\n-----------not order by-------------------";
      order.fileName = "";
      orders.add(order);
      orders.addAll(notOrders);
    }

    return orders;
  }

  private static void resultLog(String msg)
  {
    int bytes = 0;
    File file = new File("./result/result." + msg + "." + System.currentTimeMillis() + ".log");
    PrintWriter pw = null;
    try {
      File path = file.getParentFile();
      path.mkdirs();
      pw = new PrintWriter(file, "UTF-8");
      for (Map.Entry<String, List<String>> entry : resultMap.entrySet()) {
        pw.println("-------" + (String)entry.getKey());
        for (String str : entry.getValue()) {
          pw.println("\t" + str);
          bytes += str.length();
        }
        pw.println("\n");
      }
      pw.flush();
      System.out.println("result bytes=" + bytes + ",log:" + file.getAbsolutePath());
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (pw != null)
        pw.flush();
    }
  }

  private static void analysisLog(File file, long beginTime, long endTime)
  {
    try
    {
      if (file.isFile()) {
        String[] names = file.getName().split("\\.");
        if ((!file.getName().contains(".log")) || (file.getName().contains(".bak")) || (names[0].contains("result")) || (names[0].contains("init")) || (
          (anaLogFileSet.size() > 0) && (!anaLogFileSet.contains(names[0])))) {
          return;
        }
        long time = System.currentTimeMillis();
        if (names.length > 2)
          time = parseTime(names[(names.length - 1)]);
        else {
          time = parseTime(formatTime(time));
        }
        if ((time < beginTime) || (time > endTime)) {
          return;
        }
        anaFileMsg(file);
      }
      else
      {
        for (File sub_file : file.listFiles())
          analysisLog(sub_file, beginTime, endTime);
      }
    }
    catch (Exception e) {
      System.out.println(file.getName() + " is error!!!");
      e.printStackTrace();
    }
  }

  private static void anaFileMsg(File file) {
    BufferedReader br = null;
    List<String> list = new ArrayList<>(10240);
    try {
      br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
      String line = null;
      while ((line = br.readLine()) != null) {
        boolean isTrue = false;
        if (msgSet.size() == 0) isTrue = true;
        else {
          for (String msg : msgSet) {
            if (line.contains(msg)) {
              isTrue = true;
              break;
            }
          }
        }
        if (!isTrue) continue;
        list.add(line);
      }
    } catch (Exception e) {
      e.printStackTrace();
      if (list.size() > 0) {
        resultMap.put(file.getPath(), list);
      }
      if (br != null) {
        try {
          br.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }

      }

      if (list.size() > 0) {
        resultMap.put(file.getPath(), list);
      }
      if (br != null)
        try {
          br.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
    }
    finally
    {
      if (list.size() > 0) {
        resultMap.put(file.getPath(), list);
      }
      if (br != null)
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
  }

  private static long parseTime(String strTime) throws ParseException
  {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    long time = 0L;
    time = format.parse(strTime).getTime();
    return time;
  }

  private static long parseFullTime(String strTime) throws ParseException
  {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    long time = 0L;
    time = format.parse(strTime).getTime();
    return time;
  }

  private static String formatTime(long time) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    return format.format(new Date(time));
  }
/*
 * 配置初始化
 */
  private static void init() {
	  Set<String> set = new HashSet<>();
	  try {
//		  String path = LogAnalysisMain.class.getClassLoader().getResource("resources/logana.properties").toURI().getPath();
//		  System.out.println("init file:"+path);
//		  File file = new File(path);
//		  System.out.println(LogAnalysisMain.class.getResource("/").getPath());
//		  String filePath = LogAnalysisMain.class.getClassLoader().getResource("logana.properties");
//	    File file = new File(filePath);
//	    if (!file.exists()) {
//	    	System.out.println(file.getPath() +" not exist.");
//	      return;
//	    }
//	    configs.load(new BufferedReader(new InputStreamReader(new FileInputStream(file))));
	    configs.load(new BufferedReader(new InputStreamReader( LogAnalysisMain.class.getClassLoader().getResource("logana.properties").openStream())));
	    //配置指定筛选文件
	      String line = configs.getProperty("search.file.scope");
	      if (line != null && line.length() > 0)
	      {
	    	  for (String scope : line.split(";"))
		      {
		        if (scope == null)
		        {
		          continue;
		        }
		        scope = scope.trim();
		        if (scope.length() == 0)
		        {
		          continue;
		        }
		        set.add(scope.toLowerCase());
		      }
	    	  anaLogFileSet = set;
	      }
	     
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static class OrderByLine
  {
    long time;
    String msg;
    String fileName;
  }
}
