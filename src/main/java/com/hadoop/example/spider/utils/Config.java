package com.hadoop.example.spider.utils;

import java.io.IOException;
import java.util.Properties;

public class Config {
	static Properties properties;
	static{
		properties = new Properties();
		//加载资源文件中的内容
		try {
			properties.load(Config.class.getClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static int nThread = Integer.parseInt(properties.getProperty("nThread"));
	public static long millions_1 = Long.parseLong(properties.getProperty("millions_1"));
	public static long millions_5 = Long.parseLong(properties.getProperty("millions_5"));
	//XPATH页需要提取出来，可以保存到文件或者数据库中

}
