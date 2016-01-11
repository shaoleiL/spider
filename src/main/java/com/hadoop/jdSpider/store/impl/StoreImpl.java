package com.hadoop.jdSpider.store.impl;

import com.hadoop.jdSpider.Page;
import com.hadoop.jdSpider.store.Storeable;

public class StoreImpl implements Storeable {

	@Override
	public void store(Page page) {
		System.out.println(page.getUrl() + "----" + page.getFieldMap().get("price"));
		
	}

}
