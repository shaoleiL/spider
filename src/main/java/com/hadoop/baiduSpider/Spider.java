package com.hadoop.baiduSpider;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 百度百科爬虫
 * Created by shaolei on 2016/1/11 23:03.
 */
public class Spider {
    private static Logger logger = LoggerFactory.getLogger(Spider.class);
    public static void main(String[] args) {
        String url = "http://baike.baidu.com/view/3080925.htm";
        String context = getContext(url);
        System.out.println(context);
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
