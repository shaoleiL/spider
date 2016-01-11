package com.hadoop.jdSpider.repository.impl;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.hadoop.jdSpider.repository.Repositoryable;

public class QueueRepositoryImpl implements Repositoryable{

	Queue<String> lowQueue = new ConcurrentLinkedQueue<String>();
	Queue<String> heighQueue = new ConcurrentLinkedQueue<String>();
	@Override
	public String poll() {
		String url = this.heighQueue.poll();
		if(url == null){
			return this.lowQueue.poll();
		}
		return url;
	}

	@Override
	public void add(String nextUrl) {
		this.lowQueue.add(nextUrl);
		
	}

	@Override
	public void addheigh(String heighUrl) {
		this.heighQueue.add(heighUrl);
		
	}
	
	

}
