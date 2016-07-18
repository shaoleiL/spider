package com.hadoop.sinaSpider;


import com.hadoop.sinaSpider.bean.BlogByAuthor;
import com.hadoop.sinaSpider.bean.Page;
import com.hadoop.sinaSpider.download.Downloadable;
import com.hadoop.sinaSpider.download.HttpClientDownload;
import com.hadoop.sinaSpider.process.Processable;
import com.hadoop.sinaSpider.process.impl.SinaProcessable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * sina blog爬虫
 * Created by ShaoleiLi on 2016/7/16 0016.
 */
public class Spider {
    private static Logger logger = LoggerFactory.getLogger(Spider.class);
    //private static String url = "http://blog.sina.com.cn/s/blog_61fd04330102wu5i.html";

    private static String url = "http://blog.sina.com.cn/s/articlelist_1643971635_0_1.html";
    private Downloadable downloadable = new HttpClientDownload();	//下载
    private Processable processable;	//解析

    public static void main(String[] args) {
//        String price = PageUtils.getContext(url);
//        HtmlCleaner htmlCleaner = new HtmlCleaner();
//        TagNode rootNode = htmlCleaner.clean(price);
//        String tagText = HtmlUtils.getTagText(rootNode, "//*[@id=\"t_61fd04330102wu5i\"]");
//        logger.info("标题:{}",tagText);
//        String coutent = HtmlUtils.getTagText(rootNode, "//*[@id=\"sina_keyword_ad_area2\"]");
//        logger.info("内容:{}",coutent);
//        String releaseDate = HtmlUtils.getTagText(rootNode,"//*[@id=\"articlebody\"]/div[1]/span");
//        logger.info("发布时间:{}",releaseDate);
        Spider spider = new Spider();
        spider.setProcessable(new SinaProcessable());
        spider.start();


    }

    /**
     * 启动爬虫
     * 1.先获取要爬取的所有博客的url地址
     * 2.遍历url地址的list，爬取内容
     */
    private void start() {
        logger.info("启动爬虫......");
        BlogByAuthor bba = Spider.this.downloadable.downloadBlogByAuthor(url);  //下载博客首页
        Spider.this.processable.processBlogByAuthor(bba);  //解析博客首页
        //Page page = Spider.this.downloadable.download(url);
        //Spider.this.processable.process(page);
        //System.out.println(page.toString());

    }

    /**
     * 获取URL
     * @return
     */
    private List<String> getURL(){

        return null;
    }

    public Downloadable getDownloadable() {
        return downloadable;
    }

    public void setDownloadable(Downloadable downloadable) {
        this.downloadable = downloadable;
    }

    public Processable getProcessable() {
        return processable;
    }

    public void setProcessable(Processable processable) {
        this.processable = processable;
    }
}
