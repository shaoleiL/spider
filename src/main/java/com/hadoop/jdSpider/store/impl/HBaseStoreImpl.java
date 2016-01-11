package com.hadoop.jdSpider.store.impl;

import java.io.IOException;
import java.util.Map;

import com.hadoop.jdSpider.Page;
import com.hadoop.jdSpider.store.Storeable;
import com.hadoop.jdSpider.utils.HbaseUtils;

public class HBaseStoreImpl implements Storeable {
	
	HbaseUtils hbaseUtils = new HbaseUtils();
	@Override
	public void store(Page page) {
		String url = page.getUrl();
		String goodsid = page.getGoodsid();
		Map<String, String> values = page.getFieldMap();
		try {
			hbaseUtils.put(hbaseUtils.TABLE_NAME, goodsid, hbaseUtils.COLUMNFAMILY_1, hbaseUtils.COLUMNFAMILY_1_TITLE, values.get("title"));
			hbaseUtils.put(hbaseUtils.TABLE_NAME, goodsid, hbaseUtils.COLUMNFAMILY_1, hbaseUtils.COLUMNFAMILY_1_DATA_URL, url);
			hbaseUtils.put(hbaseUtils.TABLE_NAME, goodsid, hbaseUtils.COLUMNFAMILY_1, hbaseUtils.COLUMNFAMILY_1_PIC_URL, values.get("picurl"));
			hbaseUtils.put(hbaseUtils.TABLE_NAME, goodsid, hbaseUtils.COLUMNFAMILY_1, hbaseUtils.COLUMNFAMILY_1_PRICE, values.get("price"));
			
			hbaseUtils.put(hbaseUtils.TABLE_NAME, goodsid, hbaseUtils.COLUMNFAMILY_2, hbaseUtils.COLUMNFAMILY_2_PARAM, values.get("parameter"));
		
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
