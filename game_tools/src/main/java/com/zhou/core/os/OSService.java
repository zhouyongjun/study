package com.zhou.core.os;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import sun.jvmstat.monitor.MonitoredHost;
import sun.jvmstat.monitor.MonitoredVm;
import sun.jvmstat.monitor.MonitoredVmUtil;
import sun.jvmstat.monitor.VmIdentifier;

import com.sun.management.OperatingSystemMXBean;
import com.zhou.util.CalendarUtil;
import com.zhou.util.StringUtils;
/**
 * 系统服务类，提供JVM、系统基本信息、CPU、GC等情况
 * @author zhouyongjun
 *
 */
public final class OSService {
	protected static long startTime;
	/**
	 * 系统名称
	 * @return
	 */
	public static String getOSName()
	{
		return System.getProperty("os.name").toLowerCase();
	}
	
	public static String getOSArch()
	{
		return System.getProperty("os.arch");
	}
	
	/**
	 * 本机局域网主机名
	 * @return
	 */
	public static String getHostName()
	{
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 本机局域网IP
	 * @return
	 */
	public static String getHostAddress()
	{
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @descrption : 系统环境变量信息
	 * 		K[USERPROFILE]	V[C:\Users\******]
			K[JAVA_HOME]	V[D:\Program Files\Java\jdk1.8.0_77]
			K[PROGRAMDATA]	V[C:\ProgramData]
			K[REDIS_HOME]	V[D:\Program Files\redis]
			K[JAVA_HOME7]	V[D:\Program Files\Java\jdk1.7.0_45]
			K[CLASS_PATH]	V[.;%JAVA_HOME\lib;D:\Program Files\Java\jdk1.8.0_77\lib\tools.jar]
			K[MOZ_PLUGIN_PATH]	V[C:\Program Files (x86)\Foxit Software\Foxit Reader Plus\plugins\]
			K[COMMONPROGRAMFILES]	V[C:\Program Files\Common Files]
			K[PROCESSOR_REVISION]	V[3c03]
			K[USERDOMAIN]	V[******]
			K[ALLUSERSPROFILE]	V[C:\ProgramData]
			K[PROGRAMW6432]	V[C:\Program Files]
			K[SESSIONNAME]	V[Console]
			K[TMP]	V[C:\Users\******\AppData\Local\Temp]
			K[PSMODULEPATH]	V[C:\windows\system32\WindowsPowerShell\v1.0\Modules\]
			K[LOGONSERVER]	V[\\******]
			K[PATH]	V[D:\Program Files\Java\jdk1.7.0_45\jre\bin;.......]
			K[GRADLE_HOME]	V[D:\Program Files\gradle-2.4]
			K[PROMPT]	V[$P$G]
			K[VS100COMNTOOLS]	V[e:\Program Files (x86)\Microsoft Visual Studio 10.0\Common7\Tools\]
			K[COMMONPROGRAMW6432]	V[C:\Program Files\Common Files]
			K[PROCESSOR_LEVEL]	V[6]
			K[LOCALAPPDATA]	V[C:\Users\******\AppData\Local]
			K[GTK_BASEPATH]	V[C:\Program Files (x86)\GtkSharp\2.12\]
			K[COMPUTERNAME]	V[******]
			K[PHP_HOME]	V[E:\php\tools\php-5.6.28-Win32-VC11-x64]
			K[WINDOWS_TRACING_FLAGS]	V[3]
			K[USERNAME]	V[******]
			K[WINDIR]	V[C:\windows]
			K[APPDATA]	V[C:\Users\******\AppData\Roaming]
			K[PATHEXT]	V[.COM;.EXE;.BAT;.CMD;.VBS;.VBE;.JS;.JSE;.WSF;.WSH;.MSC]
			K[USERDNSDOMAIN]	V[******.COM]
			K[MAVEN_HOME]	V[D:\Program Files\apache-maven-3.3.9]
			K[ANDROID_SDK_HOME]	V[D:\Program Files (x86)\Android\android-sdk]
			K[PROGRAMFILES(X86)]	V[C:\Program Files (x86)]
			K[WINDOWS_TRACING_LOGFILE]	V[C:\BVTBin\Tests\installpackage\csilogfile.log]
			K[GOROOT]	V[E:\go\go\]
			K[PYTHONPATH]	V[E:\python\src_project\game_server]
			K[TEMP]	V[C:\Users\******\AppData\Local\Temp]
			K[HOMEDRIVE]	V[C:]
			K[SYSTEMDRIVE]	V[C:]
			K[COMMONPROGRAMFILES(X86)]	V[C:\Program Files (x86)\Common Files]
			K[PROCESSOR_IDENTIFIER]	V[Intel64 Family 6 Model 60 Stepping 3, GenuineIntel]
			K[PROCESSOR_ARCHITECTURE]	V[AMD64]
			K[OS]	V[Windows_NT]
			K[FP_NO_HOST_CHECK]	V[NO]
			K[HOMEPATH]	V[\Users\******]
			K[COMSPEC]	V[C:\windows\system32\cmd.exe]
			K[PHPSTORM_JDK]	V[E:\php\tools\PhpStorm 2016.2.2\jre]
			K[PROGRAMFILES]	V[C:\Program Files]
			K[VS120COMNTOOLS]	V[C:\Program Files (x86)\Microsoft Visual Studio 12.0\Common7\Tools\]
			K[NUMBER_OF_PROCESSORS]	V[8]
			K[PUBLIC]	V[C:\Users\Public]
			K[SYSTEMROOT]	V[C:\windows]
	 * @return
	 */
	public static Map<String,String> getEnv()
	{
		return System.getenv();
	}
	
	/**
	 * 系统参数
	 * 		
 			K[java.runtime.name]	V[Java(TM) SE Runtime Environment]
			K[sun.boot.library.path]	V[D:\Program Files\Java\jdk1.7.0_45\jre\bin]
			K[java.vm.version]	V[24.45-b08]
			K[java.vm.vendor]	V[Oracle Corporation]
			K[java.vendor.url]	V[http://java.oracle.com/]
			K[path.separator]	V[;]
			K[java.vm.name]	V[Java HotSpot(TM) 64-Bit Server VM]
			K[file.encoding.pkg]	V[sun.io]
			K[user.country]	V[CN]
			K[user.script]	V[]
			K[sun.java.launcher]	V[SUN_STANDARD]
			K[sun.os.patch.level]	V[Service Pack 1]
			K[java.vm.specification.name]	V[Java Virtual Machine Specification]
			K[user.dir]	V[D:\******\******]
			K[java.runtime.version]	V[1.7.0_45-b18]
			K[java.awt.graphicsenv]	V[sun.awt.Win32GraphicsEnvironment]
			K[java.endorsed.dirs]	V[D:\Program Files\Java\jdk1.7.0_45\jre\lib\endorsed]
			K[os.arch]	V[amd64]
			K[java.io.tmpdir]	V[C:\Users\******\AppData\Local\Temp\]
			K[line.separator]	V[
		]
			K[java.vm.specification.vendor]	V[Oracle Corporation]
			K[user.variant]	V[]
			K[os.name]	V[Windows 7]
			K[sun.jnu.encoding]	V[GBK]
			K[java.library.path]	V[D:\Program Files\Java\jdk1.7.0_45\bin;C:\windows\Sun\Java\bin;C:\windows\system32;C:\windows;D:\Program Files\Java\jdk1.7.0_45\jre\bin;.....]
			K[java.specification.name]	V[Java Platform API Specification]
			K[java.class.version]	V[51.0]
			K[sun.management.compiler]	V[HotSpot 64-Bit Tiered Compilers]
			K[os.version]	V[6.1]
			K[user.home]	V[C:\Users\******]
			K[user.timezone]	V[]
			K[java.awt.printerjob]	V[sun.awt.windows.WPrinterJob]
			K[file.encoding]	V[GBK]
			K[java.specification.version]	V[1.7]
			K[java.class.path]	V[D:\******\target\classes;G:\maven\.m2\repository\org\apache\logging\log4j\log4j-api\2.7\log4j-api-2.7.jar....]
			K[user.name]	V[******]
			K[java.vm.specification.version]	V[1.7]
			K[sun.java.command]	V[com.zhou.core.os.OSService]
			K[java.home]	V[D:\Program Files\Java\jdk1.7.0_45\jre]
			K[sun.arch.data.model]	V[64]
			K[user.language]	V[zh]
			K[java.specification.vendor]	V[Oracle Corporation]
			K[awt.toolkit]	V[sun.awt.windows.WToolkit]
			K[java.vm.info]	V[mixed mode]
			K[java.version]	V[1.7.0_45]
			K[java.ext.dirs]	V[D:\Program Files\Java\jdk1.7.0_45\jre\lib\ext;C:\windows\Sun\Java\lib\ext]
			K[sun.boot.class.path]	V[D:\Program Files\Java\jdk1.7.0_45\jre\lib\resources.jar;D:\Program Files\Java\jdk1.7.0_45\jre\lib\rt.jar;...]
			K[java.vendor]	V[Oracle Corporation]
			K[file.separator]	V[\]
			K[java.vendor.url.bug]	V[http://bugreport.sun.com/bugreport/]
			K[sun.io.unicode.encoding]	V[UnicodeLittle]
			K[sun.cpu.endian]	V[little]
			K[sun.desktop]	V[windows]
			K[sun.cpu.isalist]	V[amd64]
	 * @return
	 */
	public static Set<Entry<Object,Object>> getPropertiesEntrySet()
	{
		return System.getProperties().entrySet();
	}
	
	public static String showOSInformation()
	{
		
		List<String> list = new ArrayList<>();
		list.add("----------------------------------------------------------");
		list.addAll(showSystemInformation());
		list.add("----------------------------------------------------------");
		list.addAll(showJVMInformation());
		list.add("----------------------------------------------------------");
		list.addAll(showCpuInformation());
		list.add("----------------------------------------------------------");
		list.addAll(showMemoryInformation());		
		list.add("----------------------------------------------------------");
		
		return StringUtils.join(list, "\n");
	}
	/**
	 * 内存相关信息
	 * @return
	 */
	public static List<String> showMemoryInformation()
	{
		MemoryBean bean = MemoryBean.obtainMemory(); 
		return bean.show();
	}
	
	public static class MemoryBean
	{
		public long totalMemory;
		public long freeMemory;
		public long useMemory;
		public long maxMemory;
		public long totalPhysicalMemorySize;
		public long freePhysicalMemorySize;
		public long usePhysicalMemorySize;
		public long totalSwapSpaceSize;
		public long freeSwapSpaceSize;
		public long useSwapSpaceSize;
		public double systemLoadAverage;
		public double systemCpuLoad;
		public double processCpuLoad;
		public long processCpuTime;
		public long committedVirtualMemorySize;
		public long cachePhysicalMemorySize =-1;
		public String free_cmd_result;
		public String jstat_gc;
		
		public static MemoryBean obtainMemory()
		{
			OperatingSystemMXBean osmxBean = getOperatingSystemMXBean();
			MemoryBean bean = new MemoryBean();
			bean.totalMemory = getTotalMemory();
			bean.freeMemory = getFreeMemory();
			bean.useMemory = getUseMemory();
			bean.maxMemory = getMaxMemory();
			bean.totalPhysicalMemorySize = osmxBean.getTotalPhysicalMemorySize();
			bean.freePhysicalMemorySize = osmxBean.getFreePhysicalMemorySize();
			bean.usePhysicalMemorySize = bean.totalPhysicalMemorySize - bean.freePhysicalMemorySize;
			bean.totalSwapSpaceSize = osmxBean.getTotalSwapSpaceSize();
			bean.freeSwapSpaceSize = osmxBean.getFreeSwapSpaceSize();
			bean.useSwapSpaceSize = bean.totalSwapSpaceSize - bean.freeSwapSpaceSize;
			bean.committedVirtualMemorySize = osmxBean.getCommittedVirtualMemorySize();
			bean.systemLoadAverage = osmxBean.getSystemLoadAverage();
			bean.systemCpuLoad = osmxBean.getSystemCpuLoad();
			bean.processCpuLoad = osmxBean.getProcessCpuLoad();
			bean.processCpuTime = osmxBean.getProcessCpuTime();
			Object[] objs = getCmdOfFree_M();
			if (objs != null)
			{
				bean.cachePhysicalMemorySize = (Long)objs[0];
				bean.free_cmd_result = (String)objs[1];
			}
			return bean;
		}
		public List<String> show()
		{
			List<String> list = new ArrayList<String>();
			list.add("\t分配内存:"+getByteOfUnitMeasurement(totalMemory));
			list.add("\t 空闲内存:"+getByteOfUnitMeasurement(freeMemory));
			list.add("\t 使用内存:"+getByteOfUnitMeasurement(useMemory));
			list.add("\t最大内存："+getByteOfUnitMeasurement(maxMemory));
			list.add("\t系统总物理内存："+getByteOfUnitMeasurement(totalPhysicalMemorySize));
			list.add("\t系统已用物理内存："+getByteOfUnitMeasurement(usePhysicalMemorySize));
			list.add("\t系统剩余物理内存："+getByteOfUnitMeasurement(freePhysicalMemorySize));
			list.add("\t总交互空间："+getByteOfUnitMeasurement(totalSwapSpaceSize));
			list.add("\t已用交互空间："+getByteOfUnitMeasurement(useSwapSpaceSize));
			list.add("\t剩余交互空间："+getByteOfUnitMeasurement(freeSwapSpaceSize));
			list.add("\tJVM已分配内存："+getByteOfUnitMeasurement(committedVirtualMemorySize));
			list.add("\t系统平均负载:"+systemLoadAverage+"\t系统CPU负载："+systemCpuLoad + "\t进程CPU负载："+processCpuLoad);
			list.add("\t进程CPU使用时间:"+processCpuTime+"ns(纳秒)="+processCpuTime/1000000f+"ms(毫秒)");
			list.add("-----free -m -----");
			list.add("剩余缓存物理内存："+cachePhysicalMemorySize);
			list.add(free_cmd_result);
			return list;
		}
	}
	
	/**
	 * JVM相关信息
	 * @return
	 */
	public static Collection<? extends String> showJVMInformation() {
		List<String> list = new ArrayList<>();
		list.add("\tJVM："+getJVMame());
		list.add("\t版本："+getJVMVersion());
		list.add("\thome:"+getJVMHome());
		list.add("\tTimeZone时区："+getTimeZone());
		list.add("\t当前时间："+(new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss")).format(new Date()));
		return list;
	}
	public static Object[] getCmdOfFree_M() {
		if (getOSName().startsWith("win"))
			return null;
		long free_cache = -1; 
		List<String> result = cmd("free -m");
		if (result.size() == 0) return null;
		if (result.size() > 2)
		{
			StringTokenizer tonkenizer = new StringTokenizer(result.get(2));
			tonkenizer.nextToken();
			tonkenizer.nextToken();
			tonkenizer.nextToken();
			free_cache = Long.parseLong(tonkenizer.nextToken());
		}
		
		/*
		             total       used       free     shared    buffers     cached
Mem:         15950      15651        299          0        167       3412
-/+ buffers/cache:      12071       3879
Swap:            0          0          0
		 */
		return new Object[]{free_cache,StringUtils.join(result, "\r\n")};
	}

	/**
	 * 调用系统命令
	 * @param cmd
	 * @return
	 */
	public static List<String> cmd(String cmd)
	{
		List<String> result = new ArrayList<String>();
		BufferedReader br = null;
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
			br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while((line = br.readLine()) != null)
			{
				result.add(line);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}finally
		{
			try {
				if(br != null)br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	}
	
	/**
	 * 系统相关信息
	 * @return
	 */
	public static List<String> showSystemInformation() {
		List<String> list = new ArrayList<>();
		list.add("\t系统："+getOSName());
		list.add("\t名称："+getComputerName());
		list.add("\t用户名："+getUserName());
		list.add("\t处理器个数："+getAvailableProcessors());
		list.add("\tos arch:"+getOSArch());
		list.add("\t国家："+getSystemCountry());
		list.add("\t语言："+getSystemLanguage());
		list.add("\t编码："+getEncoding());
		list.add("\t绑定IP:"+getLocalIPForJava());
		list.add("\t项目目录："+getUserDir());		
		list.add("\t游戏服编号："+getGameServerId());
		list.add("\t游戏服名："+getGameServerName());
		list.add("\tPID: "+getSelfProcessID());
		list.add("\t进程开始时间: "+CalendarUtil.format(getProcessStartTime()));
		return list;
	}
	
	/**
	 * CPU相关信息
	 * @return
	 */
	public static List<String> showCpuInformation() {
		List<String> list = new ArrayList<>();
		list.addAll(OSGcBean.obtainGC().show());
		ThreadGroup rootGroup = getRootThreadGroup();
		list.add("\t活跃线程组数:"+rootGroup.activeGroupCount()+"\t活跃线程数："+rootGroup.activeCount());
		list.addAll(getCpuRate().show());
		return list;
	}

	public static class OSGcBean
	{
		public long gcTime;
		public int gcCount;
		public long ygcCount;
		public long ygcTime;
		public long fgcCount;
		public long fgcTime;
		public long getFinallzationCount;
		public String heapMemoryUsage;
		public String nonHeapMemoryUsage;
		public static OSGcBean obtainGC()
		{
			OSGcBean bean = new OSGcBean();
			try {
//				System.out.println("YGC:" + mbs.getAttribute(youngMBean, "CollectionCount"));  
//				System.out.println("FGC:" + mbs.getAttribute(tenuredMBean, "CollectionCount"));  
				for (GarbageCollectorMXBean mxbean: getGCMXBeans()) {
					bean.gcCount += mxbean.getCollectionCount();
					bean.gcTime += mxbean.getCollectionTime();
				}
				bean.getFinallzationCount = getMemoryMXBean().getObjectPendingFinalizationCount();
				bean.heapMemoryUsage = getMemoryMXBean().getHeapMemoryUsage().toString();
				bean.nonHeapMemoryUsage = getMemoryMXBean().getNonHeapMemoryUsage().toString();
				//gc
				MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();  
				ObjectName youngMBean = new ObjectName("java.lang:type=GarbageCollector,name=PS Scavenge");  
				ObjectName tenuredMBean = new ObjectName("java.lang:type=GarbageCollector,name=PS MarkSweep");
				bean.ygcCount = (Long)mbs.getAttribute(youngMBean, "CollectionCount");
				bean.ygcTime = (Long)mbs.getAttribute(youngMBean, "CollectionTime");
				bean.fgcCount = (Long)mbs.getAttribute(tenuredMBean, "CollectionCount");
				bean.fgcTime = (Long)mbs.getAttribute(tenuredMBean, "CollectionTime");
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
			return bean;
		}
		
//		public static long getFullGC(MBeanServerConnection mbeanServer) {  
//	        try {  
//	            ObjectName objectName;  
//	            if (mbeanServer.isRegistered(new ObjectName("java.lang:type=GarbageCollector,name=ConcurrentMarkSweep"))) {  
//	                objectName = new ObjectName("java.lang:type=GarbageCollector,name=ConcurrentMarkSweep");  
//	            } else if (mbeanServer.isRegistered(new ObjectName("java.lang:type=GarbageCollector,name=MarkSweepCompact"))) {  
//	                objectName = new ObjectName("java.lang:type=GarbageCollector,name=MarkSweepCompact");  
//	            } else {  
//	                objectName = new ObjectName("java.lang:type=GarbageCollector,name=PS MarkSweep");  
//	            }  
//	            return (Long) mbeanServer.getAttribute(objectName , "CollectionCount");  
//	        } catch (Exception e) {  
//	            throw new RuntimeException(e);  
//	        }  
//	    }  
//		
//		  public static long getYoungGC(MBeanServerConnection mbeanServer) {  
//		        try {  
//		            ObjectName objectName;  
//		            if (mbeanServer.isRegistered(new ObjectName("java.lang:type=GarbageCollector,name=ParNew"))) {  
//		                objectName = new ObjectName("java.lang:type=GarbageCollector,name=ParNew");  
//		            } else if (mbeanServer.isRegistered(new ObjectName("java.lang:type=GarbageCollector,name=Copy"))) {  
//		                objectName = new ObjectName("java.lang:type=GarbageCollector,name=Copy");  
//		            } else {  
//		                objectName = new ObjectName("java.lang:type=GarbageCollector,name=PS Scavenge");  
//		            }  
//		            return (Long) mbeanServer.getAttribute(objectName , "CollectionCount");  
//		        } catch (Exception e) {  
//		            throw new RuntimeException(e);  
//		        }  
//		    }  
		
		public List<String> show()
		{
			List<String> list = new ArrayList<>();
			list.add("\tgetObjectPendingFinalizationCount:"+getFinallzationCount);
			list.add("\tHeapMemoryUsage:"+heapMemoryUsage);
			list.add("\tNonMemoryUsage:"+nonHeapMemoryUsage);
			list.add("\tGC总次数:"+gcCount+"\t时间(毫秒):"+gcTime);
			list.add("\tYGC次数:"+ygcCount+ "\t时间(毫秒):"+ygcTime);
			list.add("\tFGC次数:"+fgcCount+"\t时间(毫秒):"+fgcTime);
			
			return list;
		}
	}
	
	public static OSGcBean getGcBean()
	{
		return OSGcBean.obtainGC();
	}
	
	/**
	 * 遍历线程组树，获取根线程组
	 * @return
	 */
	public static ThreadGroup getRootThreadGroup()
	{
		ThreadGroup group = Thread.currentThread().getThreadGroup();
		ThreadGroup rootGroup = null;
		while (group != null){
			rootGroup = group;
			group = group.getParent();
		}
		return rootGroup;
	}
	/**
	 * 获取计算机名称
	 * @return
	 */
	public static String getComputerName()
	{
		return getEnv().get("COMPUTERNAME");
	}
	/**
	 * 获取dns
	 * @return
	 */
	public static String getUserDnsDomain()
	{
		return getEnv().get("USERDNSDOMAIN");
	}
	/**
	 * 获取计算机域名
	 * @return
	 */
	public static String getUserDomain()
	{
		return getEnv().get("USERDOMAIN");
	}
	
	/**
	 * 用户名称
	 * @return
	 */
	public static String getUserName()
	{
		return System.getProperty("user.name");
	}
	/**
	 * 系统语言
	 * @return
	 */
	public static String getSystemLanguage()
	{
		return System.getProperty("user.language");
	}
	/**
	 * 系统国家
	 * @return
	 */
	public static String getSystemCountry()
	{
		return System.getProperty("user.country");
	}
	/**
	 * @param 工程目录
	 * @return
	 */
	public static String getUserDir()
	{
		return System.getProperty("user.dir");
	}
	/**
	 * 
	 * @param game server id,property : game.serverid
	 * @return System.getProperty("game.serverid")
	 */
	public static String getGameServerId()
	{
		return System.getProperty("game.serverid");
	}
	/**
	 * 
	 * @return
	 */
	public static String getGameServerName()
	{
		return System.getProperty("game.servername");
	}
	
	/**
	 * 虚拟机版本
	 * @return
	 */
	public static String getJVMVersion()
	{
		return System.getProperty("java.version");
	}
	
	public static String getJVMame()
	{
		return System.getProperty("java.vm.name");
	}
	
	
	public static String getJVMHome()
	{
		return System.getProperty("java.home");
	}
	/**
	 * class path
	 * @return
	 */
	public static String getClassPath()
	{
		return System.getProperty("java.class.path");
	}
	/**
	 *  编码类型
	 * @return
	 */
	public static String getEncoding()
	{
		return System.getProperty("sun.jnu.encoding");
	}
	/**
	 * 系统默认分隔符
	 * @return
	 */
	public static String getFileSeparator()
	{
		return System.getProperty("file.separator");
	}
	
	/**
	 * 获取所有本地绑定的IP,包括局域网IP、vpn-ip
	 * @return
	 */
	public static String getLocalIPForJava(){
	    StringBuilder sb = new StringBuilder();
	    try {
	    	Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); 
	        while (en.hasMoreElements()) {
	            NetworkInterface intf = (NetworkInterface) en.nextElement();
	            Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
	            while (enumIpAddr.hasMoreElements()) {
	                 InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
//	                 System.err.println(inetAddress.getHostAddress().toString());
	                 if (!inetAddress.isLoopbackAddress()  && !inetAddress.isLinkLocalAddress() 
	                		 	&& inetAddress.isSiteLocalAddress()) {
	                	 sb.append(inetAddress.getHostAddress().toString()+",");
	                 }
	             }
	          }
	        if(sb.length() > 0) sb.deleteCharAt(sb.length()-1);
	    } catch (SocketException e) {  }
	    return sb.toString();
	}
	/**
	 * OperatingSystemMXBean 方法测试返回值：
	 * 	getName() :Windows 7
		getArch() :amd64
		getAvailableProcessors() :8
		getSystemLoadAverage() :-1.0
		getVersion() :6.1
		getObjectName() :java.lang:type=OperatingSystem

	 * @return
	 */
	public static OperatingSystemMXBean getOperatingSystemMXBean() {
//		OperatingSystemMXBean osmxBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
//		println("getName : " + osmxBean.getName());
//		println("getArch : " + osmxBean.getArch());
//		println("getAvailableProcessors :" + osmxBean.getAvailableProcessors());
//		println("getSystemLoadAverage : " + osmxBean.getSystemLoadAverage());
//		println("getVersion : " + osmxBean.getVersion());
//		println("getObjectName :" + osmxBean.getObjectName());
//		println("getTotalPhysicalMemorySize :" +getByteOfUnitMeasurement(osmxBean.getTotalPhysicalMemorySize()));
//		println("getFreePhysicalMemorySize :" +getByteOfUnitMeasurement(osmxBean.getFreePhysicalMemorySize()));
//		println("getTotalSwapSpaceSize :" +getByteOfUnitMeasurement(osmxBean.getTotalSwapSpaceSize()));
//		println("getFreeSwapSpaceSize :" +getByteOfUnitMeasurement(osmxBean.getFreeSwapSpaceSize()));
//		println("getCommittedVirtualMemorySize :" +getByteOfUnitMeasurement(osmxBean.getCommittedVirtualMemorySize()));
//		println("getSystemCpuLoad :" + osmxBean.getSystemCpuLoad());
//		println("getProcessCpuLoad :" + osmxBean.getProcessCpuLoad());
//		println("getProcessCpuTime :" + osmxBean.getProcessCpuTime()+"ns(纳秒)="+osmxBean.getProcessCpuTime()/1000000f+"ms(毫秒)");
		return (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
	}
	

	
	
	private static String getByteOfUnitMeasurement(long size)
	{
		return size / 1024 +"K="+size/1024/1024 + "M";
	}
	
	/**
	 * 	getObjectPendingFinalizationCount() : 0
		getHeapMemoryUsage() : init = 267487744(261218K) used = 2684488(2621K) committed = 256376832(250368K) max = 3804758016(3715584K)
		getNonHeapMemoryUsage() : init = 24576000(24000K) used = 3086672(3014K) committed = 24576000(24000K) max = 136314880(133120K)
		getObjectName() : java.lang:type=Memory
	 * @return
	 */
	public static MemoryMXBean getMemoryMXBean() {
//		MemoryMXBean bean = ManagementFactory.getMemoryMXBean();
//		println("getObjectPendingFinalizationCount() : " + bean.getObjectPendingFinalizationCount());
//		println("getHeapMemoryUsage() : " + bean.getHeapMemoryUsage());
//		println("getNonHeapMemoryUsage() : " + bean.getNonHeapMemoryUsage());
//		println("getObjectName() : " + bean.getObjectName());
		return ManagementFactory.getMemoryMXBean();
	}
	
	public static List<GarbageCollectorMXBean> getGCMXBeans() {
		return ManagementFactory.getGarbageCollectorMXBeans();
	}
		/**
		 * 运行管理
		 * 测试参数值：
		 * getBootClassPath	D:\Program Files\Java\jdk1.7.0_45\jre\lib\resources.jar;D:\Program Files\Java\jdk1.7.0_45\jre\lib\rt.jar;D:\Program Files\Java\jdk1.7.0_45\jre\lib\sunrsasign.jar;D:\Program Files\Java\jdk1.7.0_45\jre\lib\jsse.jar;D:\Program Files\Java\jdk1.7.0_45\jre\lib\jce.jar;D:\Program Files\Java\jdk1.7.0_45\jre\lib\charsets.jar;D:\Program Files\Java\jdk1.7.0_45\jre\lib\jfr.jar;D:\Program Files\Java\jdk1.7.0_45\jre\classes
			getClassPath	D:\online_project\telnet_console\target\classes;G:\maven\.m2\repository\org\apache\logging\log4j\log4j-api\2.7\log4j-api-2.7.jar;G:\maven\.m2\repository\org\apache\logging\log4j\log4j-core\2.7\log4j-core-2.7.jar;G:\maven\.m2\repository\org\apache\logging\log4j\log4j-1.2-api\2.7\log4j-1.2-api-2.7.jar;G:\maven\.m2\repository\org\slf4j\slf4j-api\1.7.9\slf4j-api-1.7.9.jar;G:\maven\.m2\repository\org\slf4j\slf4j-log4j12\1.7.9\slf4j-log4j12-1.7.9.jar;G:\maven\.m2\repository\log4j\log4j\1.2.17\log4j-1.2.17.jar;G:\maven\.m2\repository\javax\mail\mail\1.4.5\mail-1.4.5.jar;G:\maven\.m2\repository\javax\activation\activation\1.1\activation-1.1.jar;G:\maven\.m2\repository\org\apache\tomcat\embed\tomcat-embed-core\7.0.73\tomcat-embed-core-7.0.73.jar;G:\maven\.m2\repository\org\apache\tomcat\tomcat-util\7.0.73\tomcat-util-7.0.73.jar;G:\maven\.m2\repository\org\apache\tomcat\tomcat-juli\7.0.73\tomcat-juli-7.0.73.jar;G:\maven\.m2\repository\org\apache\tomcat\tomcat-api\7.0.73\tomcat-api-7.0.73.jar;G:\maven\.m2\repository\org\apache\tomcat\tomcat-servlet-api\7.0.73\tomcat-servlet-api-7.0.73.jar;G:\maven\.m2\repository\org\apache\tomcat\embed\tomcat-embed-jasper\7.0.73\tomcat-embed-jasper-7.0.73.jar;G:\maven\.m2\repository\org\apache\tomcat\embed\tomcat-embed-el\7.0.73\tomcat-embed-el-7.0.73.jar;G:\maven\.m2\repository\org\eclipse\jdt\core\compiler\ecj\4.4.2\ecj-4.4.2.jar;G:\maven\.m2\repository\org\apache\tomcat\embed\tomcat-embed-logging-juli\7.0.73\tomcat-embed-logging-juli-7.0.73.jar;G:\maven\.m2\repository\org\apache\mina\mina-core\2.0.0-M6\mina-core-2.0.0-M6.jar;G:\maven\.m2\repository\org\lucee\dom4j\1.6.1\dom4j-1.6.1.jar
			getLibraryPath	D:\Program Files\Java\jdk1.7.0_45\bin;C:\windows\Sun\Java\bin;C:\windows\system32;C:\windows;D:\Program Files\Java\jdk1.7.0_45\jre\bin;D:/Program Files/Java/jdk1.8.0_77/bin/../jre/bin/server;D:/Program Files/Java/jdk1.8.0_77/bin/../jre/bin;D:/Program Files/Java/jdk1.8.0_77/bin/../jre/lib/amd64;E:\python\Python27-new;E:\python\Python27-new\Lib\site-packages\PyQt4;C:\Program Files (x86)\Intel\iCLS Client\;C:\Program Files\Intel\iCLS Client\;C:\windows\system32;C:\windows;C:\windows\System32\Wbem;C:\windows\System32\WindowsPowerShell\v1.0\;C:\Program Files\Intel\Intel(R) Management Engine Components\DAL;C:\Program Files\Intel\Intel(R) Management Engine Components\IPT;C:\Program Files (x86)\Intel\Intel(R) Management Engine Components\DAL;C:\Program Files (x86)\Intel\Intel(R) Management Engine Components\IPT;C:\Program Files (x86)\ATI Technologies\ATI.ACE\Core-Static;C:\Program Files (x86)\Windows Kits\8.1\Windows Performance Toolkit\;C:\Program Files\Microsoft SQL Server\110\Tools\Binn\;D:\Program Files\TortoiseSVN\bin;D:\Program Files\Java\jdk1.8.0_77\bin;C:\Program Files\VanDyke Software\Clients\;E:\go\go\bin;D:\Program Files (x86)\MySQL\MySQL Server 5.6\bin;E:\eclipse\apache-maven-3.3.3\bin;%ANDROID_SK_HOME%\tools;%ANDROID_SK_HOME%\platform-tools;D:\Program Files\apache-maven-3.3.9\bin;D:\Program Files\gradle-2.4\bin;E:\php\tools\php-5.6.28-Win32-VC11-x64\ext;D:\Program Files\redis;D:\Program Files\gradle-2.4\bin;C:\Program Files (x86)\GtkSharp\2.12\bin;;E:\eclipse\eclipse_4.3;;.
			getManagementSpecVersion	1.2
			getName	14468@letang796
			getSpecName	Java Virtual Machine Specification
			getSpecVendor	Oracle Corporation
			getSpecVersion	1.7
			getStartTime	1498032524784
			getUptime	106
			getVmName	Java HotSpot(TM) 64-Bit Server VM
			getVmVendor	Oracle Corporation
			getVmVersion	24.45-b08

		 * @return
		 */
	   public static RuntimeMXBean getRuntimeMXBean()
	    {
//	    	RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
//	    	println("getBootClassPath",bean.getBootClassPath());
//	    	println("getClassPath",bean.getClassPath());
//	    	println("getLibraryPath",bean.getLibraryPath());
//	    	println("getManagementSpecVersion",bean.getManagementSpecVersion());
//	    	println("getName",bean.getName());
//	    	println("getSpecName",bean.getSpecName());
//	    	println("getSpecVendor",bean.getSpecVendor());
//	    	println("getSpecVersion",bean.getSpecVersion());
//	    	println("getStartTime",CalendarUtil.format(bean.getStartTime()));
//	    	println("getUptime",bean.getUptime());
//	    	println("getVmName",bean.getVmName());
//	    	println("getVmVendor",bean.getVmVendor());
//	    	println("getVmVersion",bean.getVmVersion());
	    	return ManagementFactory.getRuntimeMXBean();
	    }
	   /**
	    * 获取本身进程ID
	    * @return
	    */
	 public static int getSelfProcessID()
	 {
		 //方式一
		 int id = Integer.parseInt(getRuntimeMXBean().getName().split("@")[0]);
		 return id;
	 }
	 
	 public static long getProcessStartTime()
	 {
		 if (startTime == 0) startTime = getRuntimeMXBean().getStartTime();
			 
		 return startTime;
	 }
	 
	
	 /**
	  * 需要 tools.jar 库 支持
	  * MonitoredHost 存在于 jdk/lib/tools.jar 中
	  * 
	  * @return
	  */
	 public static Map<Integer,String> getAllJavaProcessMap()
	 {
		 Map<Integer,String> result = new HashMap<Integer, String>();
		 try {
			 MonitoredHost local = MonitoredHost.getMonitoredHost("localhost");
			Set<Integer> set = new HashSet<Integer>(local.activeVms());
			for (Integer process : set) {
				MonitoredVm vm = local.getMonitoredVm(new VmIdentifier("//"+process));
				String processName = MonitoredVmUtil.mainClass(vm, true);
				result.put(process, processName);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return result;
	 }
	 
	 public static int getOnceProcesssId(String processMainClassName)
	 {
		 Map<Integer,String> result = getAllJavaProcessMap();
		 int id = 0;
		 for (Entry<Integer,String> entry : result.entrySet())
		 {
			 if (entry.getValue().contains(processMainClassName))
			 {
				 id = entry.getKey();
				break; 
			 }
		 }
		 return id;
	 }
	 
	/**
	 * 使用内存
	 * @return 返回bytes个数
	 */
	public static long getUseMemory()
	{
//		Runtime runtime = Runtime.getRuntime();
//		long totalMemory = runtime.totalMemory();
//		long freeMemory = runtime.freeMemory();
//		println("getMemory() : totalMemory=" +totalMemory/1024/1024+"M,freeMemory="+freeMemory/1024/1024+"M,useMemory="+(totalMemory-freeMemory)/1024/1024
//				+"M,maxMemory="+runtime.maxMemory()/1024/1024+"M,availableProcessors="+runtime.availableProcessors());
		return getTotalMemory() - getFreeMemory();
	}
	/**
	 * 总分配内存
	 * @return 返回bytes个数
	 */
	public static long getTotalMemory()
	{
		return Runtime.getRuntime().totalMemory();
	}
	/**
	 * 空闲内存
	 * @return 返回bytes个数
	 */
	public static long getFreeMemory()
	{
		return Runtime.getRuntime().freeMemory();
	}
	
	public static long getMaxMemory()
	{
		return Runtime.getRuntime().maxMemory();
	}
	
	
	/**
	 * 获取系统时区
	 * @returng
	 */
	public static String getTimeZone()
	{
		return TimeZone.getDefault().getDisplayName() +"  "+ TimeZone.getDefault().getID();
	}
	/**
	 * 处理器个数
	 * @return
	 */
	public static int getAvailableProcessors()
	{
		return Runtime.getRuntime().availableProcessors();
//		return getOperatingSystemMXBean().getAvailableProcessors();
	}
	
	private static void println(Object... objs) {
		System.out.println(StringUtils.join(objs, "\t"));
	}
	public static void main(String[] args) {
//		System.out.println("getOSName     : " + getOSName());
//		System.out.println("getHostName   : " + getHostName());
//		System.out.println("getHostAddress: " + getHostAddress());
//		System.out.println("getLocalIPForJava: " + getLocalIPForJava());
//		getMemory();
//		System.out.println("System.getenv().entrySet: ");
//		for (Entry<String,String> entry : getEnv().entrySet()) {
//			System.out.println("\tK["+entry.getKey()+"]\tV["+entry.getValue()+"]");
//		}
//		System.out.println("System.getProperties().entrySet: ");
//		for (Entry<Object,Object> entry : System.getProperties().entrySet()) {
//			System.out.println("\tK["+entry.getKey()+"]\tV["+entry.getValue()+"]");
//		}
//		getMemoryMXBean();
//		getUseMemory();
//		System.out.println(getTimeZone());
//		System.out.println(showOSInformation());
//		getRuntimeMXBean();
//		for (Entry<Integer, String> entry : getAllJavaProcessMap().entrySet())
//		{
//			System.out.println(entry.getKey()+" : " + entry.getValue());
//		}
//		System.out.println(".....................");
//		System.out.println(getSelfProcessID());
//		System.out.println(getOnceProcesssId("OSService"));
//		getCompilationMXBean();
		String s = "-/+ buffers/cache:      12071       3879";
		StringTokenizer t = new StringTokenizer(s);
		while (t.hasMoreTokens())
		{
			System.out.println(t.nextToken());	
		}
		
	}
	/**
	 * class loading
	 * 	getTotalLoadedClassCount	391 已经加载过的个数，从JVM启动开始算 
		getLoadedClassCount	392			当前加载的个数
		getUnloadedClassCount	0		未加载的个数		
		isVerbose	false			
	 * @return
	 */
	public static ClassLoadingMXBean  getClassLoadingMXBean()
	{
		ClassLoadingMXBean  classloadBean = ManagementFactory.getClassLoadingMXBean();
		println("getTotalLoadedClassCount",classloadBean.getTotalLoadedClassCount());
		println("getLoadedClassCount",classloadBean.getLoadedClassCount());
		println("getUnloadedClassCount",classloadBean.getUnloadedClassCount());
		println("isVerbose",classloadBean.isVerbose());
		return ManagementFactory.getClassLoadingMXBean();
	}
	
	/**
	 * 编译对象
	 * getTotalCompilationTime	6 编译时间
		isCompilationTimeMonitoringSupported	true
	 * @return
	 */
	public static CompilationMXBean getCompilationMXBean()
	{
		CompilationMXBean  bean = ManagementFactory.getCompilationMXBean();
		println("getTotalCompilationTime",bean.getTotalCompilationTime());
		println("isCompilationTimeMonitoringSupported",bean.isCompilationTimeMonitoringSupported());
		return ManagementFactory.getCompilationMXBean();
	}
	
	public static class CpuBean
	{
		public double cpu;
		public String msg;
		public String pid_info;
		public CpuBean() {
		}
		public List<String> show() {
			List<String> list = new ArrayList<String>();
			list.add("\tcpu: " +cpu+"%");
			list.add("\tpid cpu information: " +pid_info);
			list.add("\tsystem cpu information: " +msg);
			return list;
		}
		protected CpuBean(double cpu,String msg)
		{
			this.cpu = cpu;
			this.msg = msg;
		}
		protected CpuBean(double cpu,String msg,String pid_info)
		{
			this.cpu = cpu;
			this.msg = msg;
			this.pid_info = pid_info;
		}
		public static CpuBean obtain()
		{
			return getCpuRate();
		}
	}
	
	
	/**
	 * 获取CPU使用率
	 * @return
	 */
	private static CpuBean getCpuRate()
	{
		String osName = getOSName();
		if (osName.startsWith("window"))
		{
		return getCpuRatioForWindows();
		}
		else
		{
		return getCpuRateForLinux();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	 private static CpuBean getCpuRateForLinux(){   
	        InputStream is = null;   
	        InputStreamReader isr = null;   
	        BufferedReader brStat = null;   
	        StringBuffer sb = new StringBuffer();
	        int line_num = 0;
	        double cpu = 0;
	        String pid_info = null;
	        try{   
	        	int pid = getSelfProcessID();
	            Process process = Runtime.getRuntime().exec("top -b -n 1");   
	            is = process.getInputStream();                     
	            isr = new InputStreamReader(is);   
	            brStat = new BufferedReader(isr);
	            String line = null;
	            while ((line = brStat.readLine()) != null)
	            {
	            	line_num ++;
	            	if (line_num <= 5){
//	            		System.out.println("systemline:"+line);
	            		sb.append(line).append("\t\n");
	            	}
	            	line = line.trim();
	            	if (line.startsWith(pid+""))
	            	{
//	            		System.out.println("cpuline:"+line);
	            		pid_info = line;
	            		StringTokenizer tokenStat =  new StringTokenizer(line);
	            		for (int i=0;i<8;i++)
	            		{
	            			tokenStat.nextToken();
	            		}
	            		cpu = Double.parseDouble(tokenStat.nextToken());
	            		break;
	            	}
	            }
                 
                return new CpuBean(cpu, sb.toString(), pid_info);   
            
	              
	        } catch(IOException ioe){   
	            System.out.println(ioe.getMessage());   
	            freeResource(is, isr, brStat);   
	            return new CpuBean();   
	        } finally{   
	            freeResource(is, isr, brStat);   
	        }   
	  
	    }  
	
	/**
	 * 
	 * @deprecated
	 * @return
	 */
	 private static double getOldCpuRateForLinux(){   
	        InputStream is = null;   
	        InputStreamReader isr = null;   
	        BufferedReader brStat = null;   
	        StringTokenizer tokenStat = null;   
	        String osVersion = System.getProperty("os.version");
	        try{   
//	            System.out.println("Get usage rate of CUP , linux version: "+osVersion);   
	  
	            Process process = Runtime.getRuntime().exec("top -b -n 1");   
	            is = process.getInputStream();                     
	            isr = new InputStreamReader(is);   
	            brStat = new BufferedReader(isr);   
	             
	            if(osVersion.equals("2.4")){   
	                brStat.readLine();   
	                brStat.readLine();   
	                brStat.readLine();   
	                brStat.readLine();   
	                 
	                tokenStat = new StringTokenizer(brStat.readLine());   
	                tokenStat.nextToken();   
	                tokenStat.nextToken();   
	                String user = tokenStat.nextToken();   
	                tokenStat.nextToken();   
	                String system = tokenStat.nextToken();   
	                tokenStat.nextToken();   
	                String nice = tokenStat.nextToken();   
	                 
//	                System.out.println(user+" , "+system+" , "+nice);   
	                 
	                user = user.substring(0,user.indexOf("%"));   
	                system = system.substring(0,system.indexOf("%"));   
	                nice = nice.substring(0,nice.indexOf("%"));   
	                 
	                float userUsage = new Float(user).floatValue();   
	                float systemUsage = new Float(system).floatValue();   
	                float niceUsage = new Float(nice).floatValue();   
	                 
	                return (userUsage+systemUsage+niceUsage)/100;   
	            }else{   
	                brStat.readLine();   
	                brStat.readLine();   
	                     
	                tokenStat = new StringTokenizer(brStat.readLine());   
	                tokenStat.nextToken();   
	                tokenStat.nextToken();   
	                tokenStat.nextToken();   
	                tokenStat.nextToken();   
	                tokenStat.nextToken();   
	                tokenStat.nextToken();   
	                tokenStat.nextToken();   
	                String cpuUsage = tokenStat.nextToken();   
	                     
	                 
//	                System.out.println("CPU idle : "+cpuUsage);   
	                Float usage = new Float(cpuUsage.substring(0,cpuUsage.indexOf("%")));   
	                 
	                return (1-usage.floatValue()/100);   
	            }   
	  
	              
	        } catch(IOException ioe){   
	            System.out.println(ioe.getMessage());   
	            freeResource(is, isr, brStat);   
	            return 1;   
	        } finally{   
	            freeResource(is, isr, brStat);   
	        }   
	  
	    }   
	    private static void freeResource(InputStream is, InputStreamReader isr, BufferedReader br){   
	        try{   
	            if(is!=null)   
	                is.close();   
	            if(isr!=null)   
	                isr.close();   
	            if(br!=null)   
	                br.close();   
	        }catch(IOException ioe){   
	            System.out.println(ioe.getMessage());   
	        }   
	    }   
	  
	    /**  
	     * 获得CPU使用率.  
	     * @return 返回cpu使用率  
	     * @author GuoHuang  
	     */   
	    private static CpuBean getCpuRatioForWindows() {   
	        try {   
	            String procCmd = System.getenv("windir")   
	                    + "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,"   
	                    + "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";   
	            // 取进程信息   
	            long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));   
	            Thread.sleep(CPUTIME);   
	            long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));   
	            if (c0 != null && c1 != null) {   
	                long idletime = c1[0] - c0[0];   
	                long busytime = c1[1] - c0[1];   
	                return new CpuBean(Double.valueOf(   
	                        PERCENT * (busytime) / (busytime + idletime))   
	                        .doubleValue(),"");   
	            } else {   
	                return new CpuBean();   
	            }   
	        } catch (Exception ex) {   
	            ex.printStackTrace();   
	            return new CpuBean();   
	        }   
	    }   
	  
	    private static final int CPUTIME = 30;   
	    
	    private static final int PERCENT = 100;   
	  
	    private static final int FAULTLENGTH = 10;   
	    /**       
	 
	* 读取CPU信息.  
	     * @param proc  
	     * @return  
	     * @author GuoHuang  
	     */   
	    private static long[] readCpu(final Process proc) {   
	        long[] retn = new long[2];   
	        try {   
	            proc.getOutputStream().close();   
	            InputStreamReader ir = new InputStreamReader(proc.getInputStream());   
	            LineNumberReader input = new LineNumberReader(ir);   
	            String line = input.readLine();   
	            if (line == null || line.length() < FAULTLENGTH) {   
	                return null;   
	            }   
	            int capidx = line.indexOf("Caption");   
	            int cmdidx = line.indexOf("CommandLine");   
	            int rocidx = line.indexOf("ReadOperationCount");   
	            int umtidx = line.indexOf("UserModeTime");   
	            int kmtidx = line.indexOf("KernelModeTime");   
	            int wocidx = line.indexOf("WriteOperationCount");   
	            long idletime = 0;   
	            long kneltime = 0;   
	            long usertime = 0;   
	            while ((line = input.readLine()) != null) {   
	                if (line.length() < wocidx) {   
	                    continue;   
	                }   
	                // 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,   
	                // ThreadCount,UserModeTime,WriteOperation   
	                String caption = Bytes.substring(line, capidx, cmdidx - 1)   
	                        .trim();   
	                String cmd = Bytes.substring(line, cmdidx, kmtidx - 1).trim();   
	                if (cmd.indexOf("wmic.exe") >= 0) {   
	                    continue;   
	                }   
	                // log.info("line="+line);   
	                if (caption.equals("System Idle Process")   
	                        || caption.equals("System")) {   
	                    try {
							idletime += Long.valueOf(   
							        Bytes.substring(line, kmtidx, rocidx - 1).trim())   
							        .longValue();   
							idletime += Long.valueOf(   
							        Bytes.substring(line, umtidx, wocidx - 1).trim())   
							        .longValue();
						} catch (Exception e) {
						}   
	                    continue;   
	                }   
	  
	                try {
						kneltime += Long.valueOf(   
						        Bytes.substring(line, kmtidx, rocidx - 1).trim())   
						        .longValue();   
						usertime += Long.valueOf(   
						        Bytes.substring(line, umtidx, wocidx - 1).trim())   
						        .longValue();
					} catch (Exception e) {
					}   
	            }   
	            retn[0] = idletime;   
	            retn[1] = kneltime + usertime;   
	            return retn;   
	        } catch (Exception ex) {   
	            ex.printStackTrace();   
	        } finally {   
	            try {   
	                proc.getInputStream().close();   
	            } catch (Exception e) {   
	                e.printStackTrace();   
	            }   
	        }   
	        return null;   
	    }   
	    
	    /**
	     * 其中，Bytes类用来处理字符串   
	     *
	     */
	    public static class Bytes {   
	     public static String substring(String src, int start_idx, int end_idx){   
	         byte[] b = src.getBytes();   
	         String tgt = "";   
	         for(int i=start_idx; i<end_idx; i++){   
	             tgt +=(char)b[i];   
	         }   
	         return tgt;   
	     }  
	  }

	    public static String execSystemCmd(String cmd)
	    {

	    	StringBuilder sb = new StringBuilder();
			 InputStream is = null;   
		        InputStreamReader isr = null;   
		        BufferedReader brStat = null;   
		        try{   
		            Process process = Runtime.getRuntime().exec(cmd);   
		            is = process.getInputStream();                     
		            isr = new InputStreamReader(is);   
		            brStat = new BufferedReader(isr);
		            String line = null;
		            while ((line = brStat.readLine()) != null)
		            {
		            	sb.append(line).append("\r\n");
		            }
		            }catch(IOException e){
		            	e.printStackTrace();
			        }
		        return sb.toString();
	    	
	    }
}
