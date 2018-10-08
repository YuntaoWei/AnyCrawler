package com.seven.crawler.spider.downloader;


import com.seven.crawler.spider.processor.BaseProcessor;

import us.codecraft.webmagic.Page;

public class TextContentProcesser extends BaseProcessor {
    public TextContentProcesser(String xpath) {
        super(xpath);
    }

    @Override
    public void processContent(Page page) {

    }
}
