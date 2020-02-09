package com.upgrad.tms.meeting;

public class UrlLocator {
    private static UrlLocator urlLocator;

    private UrlLocator() {
    }

    public synchronized static UrlLocator getInstance() {
        if (urlLocator == null) {
            urlLocator = new UrlLocator();
        }

        return urlLocator;
    }

    public synchronized String getUrlDetails(String url) {
        if (url == null) {
            return "There are not details as there is no url";
        }
        return "The details of the url are :"+url;
    }
}
