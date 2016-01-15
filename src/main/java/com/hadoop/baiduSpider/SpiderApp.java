package com.hadoop.baiduSpider;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.lang3.StringUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.component.BloomFilterDuplicateRemover;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by v-shaoleili on 2016/1/14.
 */
public class SpiderApp implements PageProcessor{

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setUseGzip(true);
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static Client client = ESClientHelper.getClient();
    private static final int SIZE_PER_PAGE = 1000;

    public static void main(String[] args) {
        //BloomFilterDuplicateRemover bloomFilterDuplicateRemover = new BloomFilterDuplicateRemover(9);
        us.codecraft.webmagic.Spider.create(new SpiderApp())
                .addUrl("http://baike.baidu.com/view/908354.htm")
                .addUrl("http://baike.baidu.com/view/3385550.htm")
                .addUrl("http://baike.baidu.com/subview/28283/5418753.htm")
                //.setScheduler(new QueueScheduler().setDuplicateRemover(bloomFilterDuplicateRemover))
                .addPipeline(new MyPipeline()).thread(5).start();
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

        try {
            xContentBuilder = jsonBuilder().startObject();
            xContentBuilder = xContentBuilder.field("title", page.getResultItems().get("title"));
            xContentBuilder = xContentBuilder.field("url", page.getUrl().get());
            page.putField("content", StringUtils.join(page.getHtml().xpath("//div[@ id='lemmaContent-0']//div[@ class='para']/allText()").all(),"<br>"));
            xContentBuilder = xContentBuilder.field("content", page.getResultItems().get("content"));
            String lastModifyTime = page.getHtml().xpath("//span[@ id='lastModifyTime']/text()").toString();
            try {
                Date date = dateFormat.parse(lastModifyTime);
                page.putField("lastModifyTime", date);
                xContentBuilder = xContentBuilder.field("lastModifyTime", page.getResultItems().get("lastModifyTime"));
            }catch (ParseException e) {
                if(lastModifyTime.equals("今天")){
                    page.putField("lastModifyTime", new Date());
                    xContentBuilder = xContentBuilder.field("lastModifyTime",page.getResultItems().get("lastModifyTime"));
                } else {
                    System.out.println("无法识别的编辑日期 : " + lastModifyTime);
                }
            }
            List<String> tagList = page.getHtml().xpath("//span[@ class='taglist']/test()").all();
            if(tagList.size() > 0){
                page.putField("taglist",tagList);
                xContentBuilder = xContentBuilder.field("taglist",page.getResultItems().get("taglist"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String source = xContentBuilder.endObject().toString();
            IndexRequestBuilder indexRequestBuilder = client.prepareIndex("baike", "baike").setSource(source);
            indexRequestBuilder.execute().actionGet();
            System.out.println(page.getResultItems().get("title") + "Index finish. At " + new Date());
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    private static SearchResponse excute(SearchRequestBuilder searchRequestBuilder, int page){
        SearchResponse response = searchRequestBuilder.addField("url").setFrom(page * SIZE_PER_PAGE).setSize(SIZE_PER_PAGE).execute().actionGet();
        return response;
    }

    @Override
    public Site getSite() {
        return site;
    }

    static class MyPipeline implements Pipeline{

        @Override
        public void process(ResultItems resultItems, Task task) {

        }
    }
}
