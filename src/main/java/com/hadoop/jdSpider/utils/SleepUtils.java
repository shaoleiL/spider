package com.hadoop.jdSpider.utils;

public class SleepUtils {
	
	/**
	 * @方法名: sleep 
	 * @描述: 线程休息 
	 * @参数: @param sleepMillons 
	 * @返回值类型: void
	 * @创建时间: 2015年9月5日 
	 * @创建人: 李绍磊
	 */
	public static void sleep(long sleepMillons){
		try {
			Thread.sleep(sleepMillons);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
