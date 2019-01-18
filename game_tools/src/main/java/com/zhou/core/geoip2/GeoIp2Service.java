package com.zhou.core.geoip2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.zhou.config.ToolsConfig;
import com.zhou.util.JsonUtil;

/**
 * 根据ip 赛选 city和country
 * database更新地址：
 * 	https://dev.maxmind.com/zh-hans/geoip/geoip2/geolite2/#IP
 * 当前试用版本：2018-05-02
 * @author zhouyongjun
 *
 */
public class GeoIp2Service {
    private static Logger LOGGER = LoggerFactory.getLogger(GeoIp2Service.class);
    private static FileInputStream country_database = null;
    private static DatabaseReader country_reader = null;
    
    private static FileInputStream city_database = null;
    private static DatabaseReader city_reader = null;

    static {startUp();}

    
    public static String getCityCode(String ip) {
        String cityName = null;
        if (city_reader != null) {
            try {
                InetAddress ipAddress = InetAddress.getByName(ip);
                CityResponse response = city_reader.city(ipAddress);
                City city = response.getCity();
                cityName = city.getName();
                System.out.println(JsonUtil.toJson(city.getNames()));
            } catch (Exception e) {
            	 LOGGER.error("GeoIP  DatabaseReader["+ip+"] is not exist.");
            }
        } else {
            LOGGER.info("GeoIP DatabaseReader is null");
        }
        return cityName;
    }
    
    public static String getCountryCode(String ip) {
        String countryCode = null;
        if (country_reader != null) {
            try {
                InetAddress ipAddress = InetAddress.getByName(ip);
                CountryResponse response = country_reader.country(ipAddress);
                Country country = response.getCountry();
//                System.out.println(JsonUtil.toJson(country));
                countryCode = country.getIsoCode();
            } catch (Exception e) {
            	 LOGGER.error("GeoIP DatabaseReader["+ip+"] is not exist.");
            }
        } else {
            LOGGER.info("GeoIP DatabaseReader is null");
        }
        return countryCode;
    }
    
    public static void loadCityDatabase()
    {
    	URL url = ToolsConfig.class.getClassLoader().getResource(ToolsConfig.GEOIP2_CITY_FILE_NAME);
		if (url == null) return;
		String path = url.getPath();
		File file = new File(path);
		try {
			city_database = new FileInputStream(file);
			city_reader = new DatabaseReader.Builder(city_database).build();
		} catch (Exception e) {
			LOGGER.error("",e);
		}
    }

    public static void loadCountryDatabase()
    {
    	URL url = ToolsConfig.class.getClassLoader().getResource(ToolsConfig.GEOIP2_COUNTRY_FILE_NAME);
		if (url == null) return;
		String path = url.getPath();
		File file = new File(path);
		try {
			country_database = new FileInputStream(file);
			country_reader = new DatabaseReader.Builder(country_database).build();
		} catch (Exception e) {
			LOGGER.error("",e);
		}
    }

    
    public static void startUp()
    {
    	loadCityDatabase();
    	loadCountryDatabase();
    }
    
    public static void unloadCountryDatabase()
    {
        if (country_reader != null) {
            try {
                country_reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (country_database != null) {
            try {
                country_database.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LOGGER.info("GeoIpService country reader has been closed");
    }
    
    public static void unloadCityDatabase()
    {
        if (city_reader != null) {
            try {
            	city_reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (city_database != null) {
            try {
            	city_database.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LOGGER.info("GeoIpService  city reader has been closed");
    }
    
    public static void shutDown() {
    	unloadCityDatabase();
    	unloadCountryDatabase();
    }


}
