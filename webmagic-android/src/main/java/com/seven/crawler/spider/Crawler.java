package com.seven.crawler.spider;

import java.io.File;


public class Crawler {

    public static final String URL = "url";
    public static final String KEY_WORD = "key_word";
    public static final String XPATH = "x_path";
    public static final String CRAW_TYPE = "craw_type";
    public static final String SAVE_LOCATION = "save_location";

    public static final String DEFAULT_URL = Debug.DEBUG_CRAWL_URL;
    public static final String DEFAULT_KEYWORD = "";
    public static final String DEFAULT_CRAWL_TYPE = CrawlType.PIC.toString();
    public static final String DEFAULT_XPATH = "//img/@src";
    public static final String DEFAULT_SAVE_LOCATION = Debug.DEBUG_PATH;


    //basic configurations
    public String savePath;
    public String crawlUrl;
    public CrawlType crawlType;
    public String keyWord;
    public long crawlInterval;
    public int crawlThreadNum;
    public String xPath = "//img/@src";

    CrawlTask crawlTask;

    private Crawler() {}

    public static class Builder{

        Crawler mCrawler;
        public Builder() {
            mCrawler = new Crawler();
        }

        public Builder setSavePath(File f) {
            mCrawler.savePath = f.getAbsolutePath();
            return this;
        }

        public Builder setSavePath(String path) {
            mCrawler.savePath = path;
            return this;
        }

        public Builder setCrawlerUrl(String url) {
            mCrawler.crawlUrl = url;
            return this;
        }

        public Builder setKeyWord(String keyWord) {
            mCrawler.keyWord = keyWord;
            return this;
        }

        public Builder crawlPic() {
            mCrawler.crawlType = CrawlType.PIC;
            return this;
        }

        public Builder crawlText() {
            mCrawler.crawlType = CrawlType.TEXT;
            return this;
        }

        public Builder crawlAll() {
            mCrawler.crawlType = CrawlType.ALL;
            return this;
        }

        public Builder setCrawlThreadCount(int count) {
            mCrawler.crawlThreadNum = count;
            return this;
        }

        public Builder setCrawlInterval(long timeInMi) {
            mCrawler.crawlInterval = timeInMi;
            return this;
        }

        public Builder setXpath(String path) {
            mCrawler.xPath = path;
            return this;
        }

        public Crawler build() {
            return mCrawler;
        }

    }

    @Override
    public String toString() {
        return "Crawler{" +
                "savePath='" + savePath + '\'' +
                ", crawlUrl='" + crawlUrl + '\'' +
                ", crawlType=" + crawlType +
                ", keyWord='" + keyWord + '\'' +
                ", crawlInterval=" + crawlInterval +
                ", crawlThreadNum=" + crawlThreadNum +
                ", crawlTask=" + crawlTask +
                ", xPath=" + xPath +
                '}';
    }
}
