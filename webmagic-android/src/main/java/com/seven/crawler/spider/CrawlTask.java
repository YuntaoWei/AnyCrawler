package com.seven.crawler.spider;


import com.seven.crawler.spider.downloader.AllContentProcesser;
import com.seven.crawler.spider.downloader.BaseDownloader;
import com.seven.crawler.spider.downloader.TextContentProcesser;
import com.seven.crawler.spider.processor.PictureProcessor;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.utils.LogPrinter;

public class CrawlTask {

    private static final String TAG = "CrawlTask";
    private static CrawlTask crawlTask = new CrawlTask();
    private Crawler crawler;
    private Spider mSpider;
    private CrawlState crawlState = CrawlState.IDLE;

    private CrawlTask() {}

    public static CrawlTask getInstance() {
        return crawlTask;
    }

    public void setCrawlerConfiguration(Crawler c) {
        crawler = c;
        init();
    }

    private void init() {
        createSpider();
        baseInit();
        crawlState = CrawlState.INIT;
    }

    private void createSpider() {
        if(crawler.crawlType == CrawlType.PIC) {
            mSpider = Spider.create(new PictureProcessor(crawler.xPath));
        } else if(crawler.crawlType == CrawlType.TEXT) {
            mSpider = Spider.create(new TextContentProcesser(crawler.xPath));
        } else {
            mSpider = Spider.create(new AllContentProcesser(crawler.xPath));
        }
    }

    private void baseInit() {
        mSpider.addUrl(crawler.crawlUrl)
                .thread(crawler.crawlThreadNum)
                .setExitWhenComplete(true);
        mSpider.setEmptySleepTime((int)crawler.crawlInterval);
    }

    public void setDownLoader(BaseDownloader downloader) {
        if(crawlState == CrawlState.WORKING)
            throw new IllegalStateException("Spide is workking, you can stop it,and reset the PageProcesser," +
                    " and then you can restart it.");

        if(mSpider == null) {
            throw new IllegalStateException("You should call method setCrawlerConfiguration first of all.");
        }
        downloader.setSavePath(crawler.savePath);
        mSpider.addPipeline(downloader);
        crawlState = CrawlState.INIT;
    }

    public void start() {
        if(crawlState == CrawlState.WORKING || crawlState == CrawlState.IDLE) {
            return;
        }
        LogPrinter.i(TAG, "start" + crawler.toString());
        mSpider.runAsync();;
        crawlState = CrawlState.WORKING;
    }

    public CrawlState getCrawlState() {
        return crawlState;
    }

    public void pause() {
        mSpider.pause();
    }

    public void resume() {
        mSpider.resume();
    }

    public void stop() {
        mSpider.stop();
        crawlState = CrawlState.STOP;
    }

    public void dump() {
        LogPrinter.i(TAG, crawler.toString() + "\nTask state ； " + crawlState.toString());
    }

}
