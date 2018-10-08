package com.seven.crawler.spider.exception;

public class TaskStateErrorException extends Exception {

    private String exceptionMsgg;

    public TaskStateErrorException(String msg) {
        super(msg);
        exceptionMsgg = msg;
    }

}
