package com.hadoop.example.spider.repository;

import com.hadoop.example.spider.utils.RedisUtils;

/**
 * 共享url队列
 * @author Administrator
 *
 */
public class RedisRepository implements Repository{
	
	RedisUtils redisUtils = new RedisUtils();
	@Override
	public String poll() {
		return redisUtils.poll(RedisUtils.key);
	}

	@Override
	public void add(String nextUrl) {
		redisUtils.add(RedisUtils.key, nextUrl);
	}

}
