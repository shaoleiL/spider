package com.hadoop.sinaSpider.bean;

import java.util.List;

/**
 * blog的所有链接
 * Created by ShaoleiLi on 2016/7/17 0017.
 */
public class BlogByAuthor {

    /**
     * blog id
     */
    public String id;

    /**
     * 该作者的blog的url
     */
    public String url;

    /**
     * 作者的博客的所有url连接
     */
    public List<String> urlList;

    /**
     * Blog的名字
     */
    public String blogName;

    /**
     * 第一页的所有内容
     */
    public String firstPageContent;

    /**
     * blog的具体内容
     */
    public List<Page> pageList;

    /**
     * 获取博客名字下载的内容
     */
    public String blogNameContent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public String getBlogName() {
        return blogName;
    }

    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }

    public String getFirstPageContent() {
        return firstPageContent;
    }

    public void setFirstPageContent(String firstPageContent) {
        this.firstPageContent = firstPageContent;
    }

    public List<Page> getPageList() {
        return pageList;
    }

    public void setPageList(List<Page> pageList) {
        this.pageList = pageList;
    }

    public String getBlogNameContent() {
        return blogNameContent;
    }

    public void setBlogNameContent(String blogNameContent) {
        this.blogNameContent = blogNameContent;
    }
}
