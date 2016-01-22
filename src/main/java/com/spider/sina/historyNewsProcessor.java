package com.spider.sina;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Iterator;

/**
 * http://history.sina.com.cn/news/list/33.shtml
 * Created by v-shaoleili on 2016/1/22.
 */
public class historyNewsProcessor  implements PageProcessor {

    public static final String URL_LIST = "http://history\\.sina\\.com\\.cn/list/33\\.shtml";

    public static final String URL_POST = "http://history\\.sina\\.com\\.cn/bk/jds/\\S+/\\S+\\.html";
    //http://history.sina.com.cn/bk/jds/2014-04-06/221687030.shtml
    private Site site = Site
            .me()
            .setDomain("history.sina.com.cn")
            .setSleepTime(3000)
            .setUserAgent(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

    @Override
    public void process(Page page) {
        //列表页
        //if (page.getUrl().regex(URL_LIST).match()) {
        //    page.addTargetRequests(page.getHtml().xpath("//*[@id=\"listF\"]/ul[1]/li[1]/a").links().regex(URL_POST).all());
        //} else {  //文章页
        //    page.putField("title", page.getHtml().xpath("//*[@id=\"listF\"]//ul//text()").toString());
        Iterator<String> iterator = page.getJson().regex("title\\S+").regex("url\\S+").all().iterator();
        if(iterator.hasNext()){
            String next = iterator.next();
            System.out.println(next);
        }
        //page.putField("title", page.getJson().regex("title\\S+").regex("url\\S+").all().iterator().toString());

        //}
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new historyNewsProcessor()).addUrl("http://history.sina.com.cn/news/list/33.shtml")
                .start();
    }
}
