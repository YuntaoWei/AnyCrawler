package com.seven.crawler.spider.downloader;



import com.seven.crawler.spider.CrawlType;

import java.util.List;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

public abstract class PicDownLoader extends BaseDownloader {

    private static final String TAG = "PicDownLoader";

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<String> picUrls = resultItems.get(CrawlType.PIC.toString());
        startDownLoad(picUrls);
    }

    public abstract void startDownLoad(List<String> urls);

}
