package com.hadoop.sinaSpider.utils;


import org.apache.commons.lang.StringUtils;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import java.util.List;

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
	 * @方法名: 获取总页数
	 * @描述: TODO(这里用一句话描述这个方法的作用)
	 * @参数: @param rootNode
	 * @参数: @param regxString
	 * @参数: @param source
	 * @参数: @return
	 * @返回值类型: String
	 * @创建时间: 2015年8月29日
	 * @创建人: 李绍磊
	 */
	public static int getPageSize(TagNode rootNode, String xpath){
		int pageSize = 1;
		Object[] evaluateXPath;
		try {
			evaluateXPath = rootNode.evaluateXPath(xpath);
			if(evaluateXPath.length > 0){
				String pageSizeStr =  evaluateXPath[0].toString();
				String substring = pageSizeStr.substring(1, 3);
				pageSize = Integer.parseInt(substring);
				System.out.println(pageSizeStr);
			}
		} catch (XPatherException e) {
			e.printStackTrace();
		}
		return pageSize;
	}

	public static List<String> getUrlList(TagNode rootNode, String regxString, List<String> pageUrlList) {

		try {
			Object[] evaluateXPath = rootNode.evaluateXPath(regxString);
			if(evaluateXPath.length > 0){
				for (Object anEvaluateXPath : evaluateXPath) {
					String url = (String) anEvaluateXPath;
					if (url.length() == 52) {
						pageUrlList.add(url);
					}
				}
			}
		} catch (XPatherException e) {
			e.printStackTrace();
		}
		return pageUrlList;
	}
}
