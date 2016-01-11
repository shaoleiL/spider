package com.hadoop.jdSpider.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.json.JSONArray;
import org.json.JSONObject;

import com.hadoop.jdSpider.Page;

public class HtmlUtils {

	/**
	 * @方法名:获取标签的内容
	 * @描述: TODO(这里用一句话描述这个方法的作用) 
	 * @参数: @return 
	 * @返回值类型: String
	 * @创建时间: 2015年8月29日 
	 * @创建人: 李绍磊
	 */
	public static String getTagText(TagNode rootNode, String regxString){
		String result = "";
		try {
			Object[] evaluateXPath = rootNode.evaluateXPath(regxString);
			if(evaluateXPath.length > 0){
				TagNode titleNode = (TagNode)evaluateXPath[0];
				CharSequence text = titleNode.getText();
				result = text.toString();
			}
		} catch (XPatherException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @方法名: 获取页面属性 
	 * @描述: TODO(这里用一句话描述这个方法的作用) 
	 * @参数: @param rootNode
	 * @参数: @param regxString
	 * @参数: @param source
	 * @参数: @return 
	 * @返回值类型: String
	 * @创建时间: 2015年8月29日 
	 * @创建人: 李绍磊
	 */
	public static String getAttribute(TagNode rootNode, String attr,String xpath){
		String result = "";
		Object[] evaluateXPath;
		try {
			evaluateXPath = rootNode.evaluateXPath(xpath);
			if(evaluateXPath.length > 0){
				TagNode imageNode = (TagNode)evaluateXPath[0];
				String attributeByName = imageNode.getAttributeByName(attr);
				result = attributeByName;
			}
		} catch (XPatherException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @方法名: getPricce 
	 * @描述: 获取价格
	 * @参数: @param page
	 * @参数: @param string
	 * @参数: @param string2
	 * @参数: @param string3
	 * @参数: @return 
	 * @返回值类型: String
	 * @创建时间: 2015年8月29日 
	 * @创建人: 李绍磊
	 */
	public static String getPricce(Page page, String urlReg, String priceUrl, String attribute) {
		String url = page.getUrl();
		Pattern compile = Pattern.compile(urlReg);
		Matcher matcher = compile.matcher(url);
		String goodsId="";
		if(matcher.find()){
			goodsId = matcher.group(1);
			page.setGoodsid("jd_" + goodsId);
		}
		String priceString = priceUrl+goodsId;
		String price = PageUtils.getContext(priceString);
		JSONArray jsonArray = new JSONArray(price);
		JSONObject object = (JSONObject) jsonArray.get(0);
		String result = object.get(attribute).toString();
		return result;
	}
}
