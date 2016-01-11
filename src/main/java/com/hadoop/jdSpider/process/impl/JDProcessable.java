package com.hadoop.jdSpider.process.impl;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.json.JSONArray;
import org.json.JSONObject;

import com.hadoop.jdSpider.Page;
import com.hadoop.jdSpider.process.Processable;
import com.hadoop.jdSpider.utils.HtmlUtils;

public class JDProcessable implements Processable{

	@Override
	public void process(Page page) {
		String context = page.getContext();
		HtmlCleaner htmlCleaner = new HtmlCleaner();
		TagNode rootNode = htmlCleaner.clean(context);
		String url = page.getUrl();
		if(url.startsWith("http://list.jd.com/list.html")){
			//解析下一页
			String href = HtmlUtils.getAttribute(rootNode, "href", "//*[@id=\"J_topPage\"]/a[2]").replace("&amp;", "&");
			if(!href.equals("javascript:;")){
				String nextPageUrl = "http://list.jd.com"+href;
				page.addNextUrl(nextPageUrl);
			}
			try {
				Object[] evaluateXPath = rootNode.evaluateXPath("//*[@id=\"plist\"]/ul/li/div/div[1]/a");
				for (Object object : evaluateXPath) {
					TagNode tagNode = (TagNode)object;
					String urlhref = tagNode.getAttributeByName("href");
					page.addNextUrl(urlhref);
				}
			} catch (XPatherException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}else{
			parseProduct(page, rootNode);
		}
	}

	/**
	 * @方法名: parseProduct 
	 * @描述: 解析商品明细信息
	 * @参数: @param page
	 * @参数: @param rootNode 
	 * @返回值类型: void
	 * @创建时间: 2015年9月3日
	 * @创建人: 李绍磊
	 */
	public void parseProduct(Page page, TagNode rootNode) {
		try {
			//获取标题
			page.addField("title",HtmlUtils.getTagText(rootNode, "//*[@id=\"name\"]/h1"));

			//获取图片URL地址
			page.addField("picurl",HtmlUtils.getAttribute(rootNode, "//*[@id=\"spec-n1\"]/img", "src"));
			//价格
			page.addField("price", HtmlUtils.getPricce(page,"http://item.jd.com/([0-9]+).html","http://p.3.cn/prices/get?skuid=J_","p"));
			
			//规格参数
			JSONArray jsonArray = new JSONArray();
			Object[] evaluateXPath = rootNode.evaluateXPath("//*[@id=\"product-detail-2\"]/table/tbody/tr");
			if(evaluateXPath.length > 0){
				for (int i = 0; i < evaluateXPath.length; i++) {
					TagNode trNode = (TagNode)evaluateXPath[i];
					if(!("").equals(trNode.getText().toString().trim())){
						Object[] thEvaluateXPath = trNode.evaluateXPath("/th");
						JSONObject jsonObject = new JSONObject();
						if(thEvaluateXPath.length > 0){
							TagNode thNode = (TagNode)thEvaluateXPath[0];
							jsonObject.put(thNode.getText().toString(), "");
						}
						Object[] tdEvaluateXPath = trNode.evaluateXPath("/td");
						if(tdEvaluateXPath.length > 0){
							TagNode tdNode1 = (TagNode)tdEvaluateXPath[0];
							TagNode tdNode2 = (TagNode)tdEvaluateXPath[1];
							jsonObject.put(tdNode1.getText().toString(), tdNode2.getText().toString());
						}
						jsonArray.put(jsonObject);
					}
				}
			}
			page.addField("parameter", jsonArray.toString());
			
			
		} catch (XPatherException e) {
			e.printStackTrace();
		}
	}

}
