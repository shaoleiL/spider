package com.hadoop.baiduSpider;

import com.hadoop.jdSpider.utils.HtmlUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 百度百科爬虫
 * Created by shaolei on 2016/1/11 23:03.
 */
public class Spider {
    private static Logger logger = LoggerFactory.getLogger(Spider.class);
    public static void main(String[] args) throws XPatherException {
        String url = "http://baike.baidu.com/view/3080925.htm";
        String context = getContext(url);

        Document doc = Jsoup.parse(context);
        Elements title = doc.select("dd > h1");
        for (Element element :title) {
            String text = element.text();
            System.out.println(text);
        }
        Elements lemma_summary = doc.select("div.lemma-summary");
        for (Element element :lemma_summary){
            String text = element.text(); //获取标题
            System.out.println(text);
        }
        //basic-info cmn-clearfix
        Elements basic_info_cmn_clearfix = doc.select("div[class=basic-info cmn-clearfix]");

        Map<String, String> map = new HashMap<>();  //获取属性
        for (Element element :basic_info_cmn_clearfix){
            String text = element.text();
            System.out.println(text);
            List<Node> nodes = element.childNodes();
            for (Node node :nodes) {
                List<Node> nodes1 = node.childNodes();
                if(nodes1.size() > 0){
                    String name = null;
                    String value = null;
                    for (Node node1 : nodes1) {
                        if (node1.childNodes().size() > 0) {
                            if(node1.attributes().toString().contains("name")){
                                name = ((Element) node1).text();
                                map.put(name,value);
                            }
                            if(node1.attributes().toString().contains("value")){
                                value = ((Element) node1).text();
                                map.put(name,value);
                            }
                        }
                    }

                }

            }
            System.out.println(map);
        }

        Elements level_2 = doc.select("h2[class=para-title level-2]");
        int j = 0;
        for (int i=0; i<level_2.size(); i++){
            Element element = level_2.get(i);
            String titleContent = element.text();
            System.out.println(titleContent);
            Elements siblingElements = element.siblingElements();
            String text;
            for(; j<siblingElements.size(); j++){
                Element element1 = siblingElements.get(j);
                if(element1.select("div[class=para]").size() > 0 || element1.select("h3[class=para-title level-3]").size() > 0){
                    text = element1.select("div[class=para]").text();
                    text += element1.select("h3[class=para-title level-3]").text();
                    System.out.println(text);
                }
                
                if(element1.equals(level_2.get(i+1))) {
                    break;
                }
            }
        }


    }

    public static String getContext(String url){
        String context = "";
        HttpClientBuilder builder = HttpClients.custom();
        CloseableHttpClient client = builder.build();
        HttpGet httpGet = new HttpGet(url);
        try {
            long starttime = System.currentTimeMillis();
            CloseableHttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();

            context = EntityUtils.toString(entity, "UTF-8");
            //logger.info(Thread.currentThread().getId() +"页面下载成功:{},消耗时间:{}",url,System.currentTimeMillis() - starttime);
        } catch (ParseException | IOException e) {
            //logger.error("页面下载失败:{}",url);
            e.printStackTrace();
        }
        return context;
    }
}
