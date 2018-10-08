package com.seven.crawler.spider;

public enum CrawlState {

    IDLE("idle", 0), INIT("init", 1), WORKING("woring", 2), STOP("stop", 3);

    private String crawlState;
    private int code;

    private CrawlState(String state, int code) {
        crawlState = state;
        this.code = code;
    }

    public int stateCode() {
        return code;
    }

    @Override
    public String toString() {
        return crawlState;
    }
}
