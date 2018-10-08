package com.seven.crawler.spider;

import android.os.Environment;

public class Debug {

    public static final boolean PRINT_LOG = true;
    public static final boolean DEBUG_SETTING = true;

    public static final String DEBUG_PATH =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

    public static final String DEBUG_CRAWL_URL_1 = "http://www.rosi44.com/";
    public static final String DEBUG_CRAWL_URL = "http://www.xiaohuar.com/hua";
    //public static final String DEBUG_CRAWL_URL = "http://699pic.com/zhuanti/shenqiushijie.html";

}
