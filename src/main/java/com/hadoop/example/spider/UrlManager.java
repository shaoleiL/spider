package com.hadoop.example.spider;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

public class UrlManager {
	
	public static void main(String[] args) {
		try {
			//获取默认调度器
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			//开启调度器
			scheduler.start();
			
			String simpleName = UrlJob.class.getSimpleName();
			JobDetail jobDetail = new JobDetail(simpleName, Scheduler.DEFAULT_GROUP, UrlJob.class);
			
			CronTrigger trigger = new CronTrigger(simpleName, Scheduler.DEFAULT_GROUP, "0 00 01 ? * *  ");
			scheduler.scheduleJob(jobDetail , trigger);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	

}
