package com.hadoop.example.spider.store;

import java.io.IOException;
import java.util.Map;

import com.hadoop.example.spider.domain.Page;
import com.hadoop.example.spider.utils.HbaseUtils;
import com.hadoop.example.spider.utils.RedisUtils;

/**
 * 在hbase中存储的时候，分为两个列簇
 * 标题、价格、图片地址
 * 规格参数
 * create 'spider','goodsinfo','spec'
 * alter 'spider',{NAME=>'goodsinfo',VERSIONS=>30}
 * 
 * rowkey
 * 使用商品编号和网站的唯一标识
 * 为了解决热点数据问题
 * rowkey需要这样设计：商品编号倒置_网站唯一标识
 * 
 * @author Administrator
 *
 */
public class HbaseStore implements Storeable{

	RedisUtils redisUtils = new RedisUtils();
	HbaseUtils hbaseUtils = new HbaseUtils();
	@Override
	public void store(Page page) {
		String goodsId = page.getGoodsId();
		Map<String, String> values = page.getValues();
		try {
			hbaseUtils.put(HbaseUtils.TABLE_NAME, goodsId, HbaseUtils.COLUMNFAMILY_1, HbaseUtils.COLUMNFAMILY_1_DATA_URL, page.getUrl());
			hbaseUtils.put(HbaseUtils.TABLE_NAME, goodsId, HbaseUtils.COLUMNFAMILY_1, HbaseUtils.COLUMNFAMILY_1_PIC_URL, values.get("picurl"));
			hbaseUtils.put(HbaseUtils.TABLE_NAME, goodsId, HbaseUtils.COLUMNFAMILY_1, HbaseUtils.COLUMNFAMILY_1_PRICE, values.get("price"));
			hbaseUtils.put(HbaseUtils.TABLE_NAME, goodsId, HbaseUtils.COLUMNFAMILY_1, HbaseUtils.COLUMNFAMILY_1_TITLE, values.get("title"));
			hbaseUtils.put(HbaseUtils.TABLE_NAME, goodsId, HbaseUtils.COLUMNFAMILY_2, HbaseUtils.COLUMNFAMILY_2_PARAM, values.get("spec"));
			
			redisUtils.add("solr_index", goodsId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
