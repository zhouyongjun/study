package com.zhou.test.geoip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhou.core.geoip2.GeoIp2Service;

public class GeoIp2ServiceTest {
	public static void main(String[] args) {
		Map<String,Result> results = new HashMap<>();
		try {
			String file = GeoIp2ServiceTest.class.getClassLoader().getResource("t.sql").getPath();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			while((line = br.readLine()) != null)
			{
				String[] values = line.split("\t");
				if (values.length < 2) continue;
				String oldCountry = values[0]; 
				String ip = values[1];
				if (results.containsKey(ip)) continue;
				String nowCountry = GeoIp2Service.getCountryCode(ip);
				Result result = new Result(oldCountry, ip, nowCountry);
				results.put(ip, result);
			}
			br.close();
			printResults(results);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(GeoIp2Service.getCountryCode("183.162.2.231"));
	}
	
	private static void printResults(Map<String, Result> results) {
		try {
			PrintWriter pw = new PrintWriter(new File("result.sql"));
			List<Result> list = new ArrayList<>(results.values());
			Collections.sort(list, new Comparator<Result>() {

				@Override
				public int compare(Result r1, Result r2) {
//					if (r1.getNowCountry() == null || r2.getNowCountry() == null) return -1;
					String o1 = r1.getOldCountry();
					String o2 = r2.getOldCountry();
					if(o1 == null || o2 == null){
	                    return -1;
	                }
	                if(o1.length() > o2.length()){
	                    return 1;
	                }
	                if(o1.length() < o2.length()){
	                    return -1;
	                }
	                if(o1.compareTo(o2) > 0){
	                    return 1;
	                }
	                if(o1.compareTo(o2) < 0){
	                    return -1;
	                }
	                if(o1.compareTo(o2) == 0){
	                    return 0;
	                }
	                return 0;
				}
			});
			for (Result result : list)
			{
				pw.println(result.toString());
			}
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	static class Result
	{
		String oldCountry;
		String ip;
		String nowCountry;
		
		public String getOldCountry() {
			return oldCountry;
		}
		public Result(String oldCountry, String ip, String nowCountry) {
			super();
			this.oldCountry = oldCountry;
			this.ip = ip;
			this.nowCountry = nowCountry;
		}
		public void setOldCountry(String oldCountry) {
			this.oldCountry = oldCountry;
		}
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public String getNowCountry() {
			return nowCountry;
		}
		public void setNowCountry(String nowCountry) {
			this.nowCountry = nowCountry;
		}
		
		@Override
		public String toString() {
			return "ip="+ip+",oldCountry="+oldCountry+",nowCountry="+nowCountry;
		}
	}
}
