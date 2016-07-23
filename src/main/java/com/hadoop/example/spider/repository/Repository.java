package com.hadoop.example.spider.repository;

public interface Repository {

	String poll();

	void add(String nextUrl);

}
