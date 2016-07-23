package com.hadoop.example.spider;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.hadoop.example.spider.utils.RedisUtils;

public class UrlJob implements Job{

	RedisUtils redisUtils = new RedisUtils();
	/**
	 * 满足条件这个方法会被调用
	 */
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		//获取所有的入口url
		List<String> urls = redisUtils.lrange(RedisUtils.start_url, 0,-1);
		for (String url : urls) {
			//循环吧所有入口url都添加到url仓库中
			redisUtils.add(RedisUtils.key, url);
		}
	}

}
