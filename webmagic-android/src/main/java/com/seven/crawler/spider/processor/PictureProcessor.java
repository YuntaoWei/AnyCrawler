package com.seven.crawler.spider.processor;


import android.text.TextUtils;

import com.seven.crawler.spider.CrawlType;
import com.seven.crawler.spider.collection.FilterList;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.utils.LogPrinter;

public class PictureProcessor extends BaseProcessor {

    private final String TAG = "PictureProcessor";
    private static String PIC_XPATH = "//img/@src";

    public PictureProcessor(String xpath) {
        super(xpath);
    }

    @Override
    public void processContent(Page page) {
        List<String> pages = page.getHtml().links().all();
        page.addTargetRequests(pages);
        page.putField(HOST, page.getRequest().getUrl());
        page.putField(TITLE, page.getHtml().xpath(TITLE_XPATH).all());
        page.putField(CrawlType.PIC.toString(),
                new FilterList().filterAddAll(page.getHtml().xpath(TextUtils.isEmpty(xpath) ? PIC_XPATH : xpath).all()));
    }

}
