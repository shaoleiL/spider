package com.hadoop.jdSpider.repository;

public interface Repositoryable {

	public String poll();

	void add(String nextUrl);

	void addheigh(String heighUrl);
}
