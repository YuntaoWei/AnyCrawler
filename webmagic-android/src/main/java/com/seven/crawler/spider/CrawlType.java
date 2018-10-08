package com.seven.crawler.spider;

public enum CrawlType {

    PIC("images"), TEXT("text"), ALL("all");
    private final String contentType;

    private CrawlType(String type) {
        this.contentType = type;
    }

    public static CrawlType valueOfSelf(String value) {
        if(PIC.contentType.equals(value))
            return PIC;
        else if(TEXT.contentType.equals(value))
            return TEXT;
        else
            return ALL;
    }

    @Override
    public String toString() {
        return PIC.contentType;
    }
}
