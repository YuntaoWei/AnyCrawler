package com.seven.crawler.spider.downloader;



import com.seven.crawler.spider.CrawlType;

import java.util.List;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.utils.LogPrinter;

public abstract class PicDownLoader extends BaseDownloader {

    private static final String TAG = "PicDownLoader";

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<String> picUrls = resultItems.get(CrawlType.PIC.toString());
        List<String> title = resultItems.get("Title");
        String url = resultItems.get("Host");
        startDownLoad(picUrls, title, url);
    }

    public abstract void startDownLoad(List<String> urls, List<String> pageTitle, String pageUrl);

}
