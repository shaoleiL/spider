package com.hadoop.baiduSpider;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.component.BloomFilterDuplicateRemover;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by v-shaoleili on 2016/1/14.
 */
public class SpiderApp implements PageProcessor{

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setUseGzip(true);
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static Client client = ESClientHelper.getClient();
    private final int SIZE_PER_PAGE = 1000;

    public static void main(String[] args) {
        BloomFilterDuplicateRemover bloomFilterDuplicateRemover = new BloomFilterDuplicateRemover(9999);
        us.codecraft.webmagic.Spider.create(new SpiderApp()).addUrl("","").setScheduler(new QueueScheduler().setDuplicateRemover(bloomFilterDuplicateRemover)).addPipeline(new MyPipeline()).thread(5).start();
        System.out.println("爬虫退出");
    }
    @Override
    public void process(Page page) {

        XContentBuilder xContentBuilder = null;
        page.addTargetRequests(page.getHtml().links().regex("http://baike\\.baidu\\.com/view/\\d+\\.htm").all());
        page.addTargetRequests(page.getHtml().links().regex("http://baike\\.baidu\\.com/subview/\\d+\\.htm").all());
        page.putField("title",page.getHtml().xpath("//span[@ class='lemmaTitleH1']//allText()").toString());
        if(page.getResultItems().get("title") == null){
            page.setSkip(true);
            return;
        }
    }

    @Override
    public Site getSite() {
        return null;
    }

    static class MyPipeline implements Pipeline{

        @Override
        public void process(ResultItems resultItems, Task task) {

        }
    }
}
