package com.seven.crawler.spider.downloader;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public abstract  class BaseDownloader implements Pipeline {

    protected String savePath;

    @Override
    public abstract void process(ResultItems resultItems, Task task);

    public void setSavePath(String p) {
        savePath = p;
    }

}
