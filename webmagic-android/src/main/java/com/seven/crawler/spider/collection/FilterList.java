package com.seven.crawler.spider.collection;

import java.util.ArrayList;
import java.util.Collection;

public class FilterList extends ArrayList<String> {

    public FilterList filterAdd(String url) {
        if(null == url || !url.startsWith("http"))
            return this;
        if(!contains(url)) {
            add(url);
        }
        return this;
    }

    public FilterList filterAddAll(Collection<String> urls) {
        for (String s : urls) {
            filterAdd(s);
        }
        return this;
    }

}
