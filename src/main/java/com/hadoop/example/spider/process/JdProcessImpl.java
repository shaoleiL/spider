package com.hadoop.example.spider.process;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hadoop.example.spider.domain.Page;
import com.hadoop.example.spider.utils.RevUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.json.JSONArray;
import org.json.JSONObject;

import com.hadoop.example.spider.utils.PageUtils;

public class JdProcessImpl implements Processable{

	@Override
	public void process(Page page) {
		String content = page.getContent();
		HtmlCleaner htmlCleaner = new HtmlCleaner();
		TagNode rootNode = htmlCleaner.clean(content);
		if(page.getUrl().startsWith("http://item.jd.com/")){
			parsePrpduct(page, rootNode);
		}else{//解析列表页面
			try {
				//获取下一页
				Object[] nextUrlevaluateXPath = rootNode.evaluateXPath("//*[@id=\"J_topPage\"]/a[2]");
				if(nextUrlevaluateXPath!=null && nextUrlevaluateXPath.length>0){
					TagNode nextUrlNode = (TagNode)nextUrlevaluateXPath[0];
					String nexturl = nextUrlNode.getAttributeByName("href");
					if(!nexturl.equals("javascript:;")){
						page.addUrl("http://list.jd.com"+nexturl);
					}
				}
				//获取当前页面中商品的url
				Object[] aevaluateXPath = rootNode.evaluateXPath("//*[@id=\"plist\"]/ul/li/div/div[1]/a");
				for (Object object : aevaluateXPath) {
					TagNode aNode =(TagNode)object;
					page.addUrl("http:"+aNode.getAttributeByName("href"));
				}
				
			} catch (XPatherException e) {
				e.printStackTrace();
			}
			
			
		}
	}

	/**
	 * 解析商品详细信息
	 * @param page
	 * @param rootNode
	 */
	public void parsePrpduct(Page page, TagNode rootNode) {
		try {
			//标题
			Object[] titleevaluateXPath = rootNode.evaluateXPath("//*[@id=\"name\"]/h1");
			if(titleevaluateXPath!=null && titleevaluateXPath.length>0){
				TagNode titleNode = (TagNode)titleevaluateXPath[0];
				page.addField("title", titleNode.getText().toString());
			}
			//图片地址
			Object[] picurlevaluateXPath = rootNode.evaluateXPath("//*[@id=\"spec-n1\"]/img");
			if(picurlevaluateXPath!=null && picurlevaluateXPath.length>0){
				TagNode picurNode = (TagNode)picurlevaluateXPath[0];
				String picurl = picurNode.getAttributeByName("src");
				page.addField("picurl", "http:"+picurl);
			}
			//价格
			String url = page.getUrl();
			Pattern pattern = Pattern.compile("http://item.jd.com/([0-9]+).html");
			Matcher matcher = pattern.matcher(url);
			String goodsId = "";
			if(matcher.find()){
				goodsId = matcher.group(1);
			}
			page.setGoodsId(RevUtils.reverse(goodsId)+"_jd");
			String priceContent = PageUtils.getContent("http://p.3.cn/prices/get?skuid=J_"+goodsId);
			JSONArray jsonArray = new JSONArray(priceContent);
			JSONObject jsonObject = jsonArray.getJSONObject(0);
			page.addField("price", jsonObject.getString("p"));
			
			//解析规格参数
			JSONArray specjsonArray = new JSONArray();
			Object[] trevaluateXPath = rootNode.evaluateXPath("//*[@id=\"product-detail-2\"]/table/tbody/tr");
			if(trevaluateXPath!=null && trevaluateXPath.length>0){
				for (Object object : trevaluateXPath) {
					TagNode trNode = (TagNode)object;
					if(!trNode.getText().toString().trim().equals("")){
						JSONObject jsonObject2 = new JSONObject();
						Object[] thevaluateXPath = trNode.evaluateXPath("//th");
						if(thevaluateXPath!=null && thevaluateXPath.length>0){
							TagNode thNode = (TagNode)thevaluateXPath[0];
							jsonObject2.put("name", "");
							jsonObject2.put("value", thNode.getText().toString());
						}else{
							Object[] tdevaluateXPath = trNode.evaluateXPath("//td");
							TagNode tdNode1 = (TagNode)tdevaluateXPath[0];
							TagNode tdNode2 = (TagNode)tdevaluateXPath[1];
							jsonObject2.put("name", tdNode1.getText().toString());
							jsonObject2.put("value", tdNode2.getText().toString());
						}
						specjsonArray.put(jsonObject2);
					}
				}
			}
			page.addField("spec", specjsonArray.toString());
		} catch (XPatherException e) {
			e.printStackTrace();
		}
	}

}
