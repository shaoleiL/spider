package com.hadoop.example.spider.utils;

public class SleepUtils {
	public static void sleep(long millions){
		try {
			Thread.sleep(millions);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
