package com.hadoop.example.spider.repository;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueRepository implements Repository{
	/**
	 * url队列
	 */
	Queue<String> queue = new ConcurrentLinkedQueue<String>();
	@Override
	public String poll() {
		return this.queue.poll();
	}

	@Override
	public void add(String nextUrl) {
		this.queue.add(nextUrl);
	}

}
