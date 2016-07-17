package com.hadoop.sinaSpider.download;


import com.hadoop.sinaSpider.bean.BlogByAuthor;
import com.hadoop.sinaSpider.bean.Page;
import com.hadoop.sinaSpider.utils.PageUtils;

public class HttpClientDownload implements Downloadable {

    /**
     * 下载作者的博客
     * @param url 作者的blog url
     * @return 所有blog链接
     */
    @Override
    public BlogByAuthor downloadBlogByAuthor(String url) {
        String content = PageUtils.getContext(url);
        BlogByAuthor bba = new BlogByAuthor();
        String blogId = getUrlId(url, 38, 48);
        String titleUrl = "http://uic.sso.sina.com.cn/uic/Mutiquery.php" +
                "?UID=0&Check=null&UIDS=["+blogId+"]&UserInfoTypes=[1]&ProductType=2";
        String titleContent = PageUtils.getContext(titleUrl);
        bba.setBlogNameContent(titleContent);
        bba.setFirstPageContent(content);
        bba.setUrl(url);
        bba.setId(blogId);
        return bba;
    }

    /**
     * 下载具体的内容blog
     * @param url 具体内容blog的url
     * @return page
     */
    @Override
    public Page downloadPage(String url) {
        String context = PageUtils.getContext(url);
        Page page = new Page();
        page.setId(getUrlId(url,31,47));
        page.setContext(context);
        page.setUrl(url);
        return page;
    }

    /**
     * 从url中匹配出url的ID
     * @param url url
     * @param startIndex 截取的开始位置
     * @param endIndex 截取的结束位置
     * @return id
     */
    private String getUrlId(String url, int startIndex, int endIndex){
        return url.substring(startIndex, endIndex);
    }

}
