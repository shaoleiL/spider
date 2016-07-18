package com.hadoop.sinaSpider.process.impl;

import com.hadoop.sinaSpider.bean.BlogByAuthor;
import com.hadoop.sinaSpider.bean.Page;
import com.hadoop.sinaSpider.download.Downloadable;
import com.hadoop.sinaSpider.download.HttpClientDownload;
import com.hadoop.sinaSpider.process.Processable;
import com.hadoop.sinaSpider.utils.HtmlUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * sina解析
 * Created by ShaoleiLi on 2016/7/16 0016.
 */
public class SinaProcessable  implements Processable {

    private static Logger logger = LoggerFactory.getLogger(SinaProcessable.class);

    private Downloadable downloadable = new HttpClientDownload();	//下载
    /**
     * 解析具体内容的url页面
     * @param page 具体内容的url 页面
     */
    @Override
    public void processPage(Page page) {
        String context = page.getContext();
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        TagNode rootNode = htmlCleaner.clean(context);
        String tagText = HtmlUtils.getTagText(rootNode, "//*[@id=\"t_61fd04330102wu5i\"]");
        page.setTitle(tagText);
        logger.info("标题:{}",tagText);
        String coutent = HtmlUtils.getTagText(rootNode, "//*[@id=\"sina_keyword_ad_area2\"]");
        page.setText(context);
        logger.info("内容:{}",coutent);
        String releaseDate = HtmlUtils.getTagText(rootNode,"//*[@id=\"articlebody\"]/div[1]/span");
        page.setReleaseDate(releaseDate);
        logger.info("发布时间:{}",releaseDate);
    }

    /**
     * 解析博客的首页
     * @param blogByAuthor blogByAuthor
     */
    @Override
    public void processBlogByAuthor(BlogByAuthor blogByAuthor) {
        String blogNameContent = blogByAuthor.getBlogNameContent();
        //解析得到博客的名字
        String blogName = getBlogName(blogNameContent);
        blogByAuthor.setBlogName(blogName);

        String firstPageContent = blogByAuthor.getFirstPageContent();
        //博主的所有博客的url
        List<String> pageUrlList = new ArrayList<>();
        pageUrlList = getPageUrlList(firstPageContent,pageUrlList);
        blogByAuthor.setUrlList(pageUrlList);
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        TagNode rootNode = htmlCleaner.clean(firstPageContent);
        int pageSize = HtmlUtils.getPageSize(rootNode, "//*[@id=\"module_928\"]/div[2]/div[1]/div[3]/ul/span/text()");
        int i=2;
        while (pageSize>1 && i<=pageSize) {
            String pageUrl = "http://blog.sina.com.cn/s/articlelist_1643971635_0_"+i+".html";
            BlogByAuthor blogByAuthor2 = downloadable.downloadBlogByAuthor(pageUrl);
            String firstPageContent2 = blogByAuthor2.getFirstPageContent();
            getPageUrlList(firstPageContent2, pageUrlList);
            //blogByAuthor.getUrlList().addAll(pageUrlList2);
            i++;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 解析下载的博客名字的json内容
     * @param blogNameContent 下载得到的博客名字的json内容
     * @return 博客名字
     */
    private String getBlogName(String blogNameContent){
        JSONObject jsonObject = new JSONObject(blogNameContent);
        JSONArray userInfos = (JSONArray) jsonObject.get("UserInfos");
        return (String) ((JSONArray) ((JSONArray) userInfos.get(0)).get(2)).get(1);
    }

    /**
     * 解析得到博主的所有博客的url
     * @param firstPageContent 博客首页的内容
     * @return 博主的所有博客的url
     */
    private List<String> getPageUrlList(String firstPageContent, List<String> pageUrlList){
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        TagNode rootNode = htmlCleaner.clean(firstPageContent);
        pageUrlList = HtmlUtils.getUrlList(rootNode,"//*[@id=\"module_928\"]/div[2]/div[1]/div[2]/div[*]//a/@href",pageUrlList);
        return pageUrlList;
    }


}
