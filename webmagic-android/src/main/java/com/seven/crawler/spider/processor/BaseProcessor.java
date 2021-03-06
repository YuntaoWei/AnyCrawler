package com.seven.crawler.spider.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public abstract class BaseProcessor implements PageProcessor {

    final  String HOST = "Host";
    final String TITLE = "Title";

    String TITLE_XPATH = "[@class='title']";

    String xpath;

    public BaseProcessor(String xpath) {
        this.xpath = xpath;
    }

    @Override
    public void process(Page page) {
        processContent(page);
    }

    @Override
    public Site getSite() {
        return Site.me();
    }

    public abstract void processContent(Page page);

}
