package com.hadoop.jdSpider.Thread.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hadoop.jdSpider.Thread.ThreadPool;
import com.hadoop.jdSpider.utils.Config;

public class FixedThreadPoolImpl implements ThreadPool {
	ExecutorService newFixedThreadPool =  Executors.newFixedThreadPool(Config.nThread);
	@Override
	public void execute(Runnable command) {
		newFixedThreadPool.execute(command);
	}

}
