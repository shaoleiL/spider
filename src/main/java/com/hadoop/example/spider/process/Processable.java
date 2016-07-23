package com.hadoop.example.spider.process;

import com.hadoop.example.spider.domain.Page;

public interface Processable {
	public void process(Page page);
}
