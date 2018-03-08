package com.gdoor.crawler.module;

import com.gdoor.crawler.elastic.GdoorCrawler;
import com.google.inject.AbstractModule;

public class CrawlerModule extends AbstractModule  {
    @Override
    protected void configure() {
        bind(Crawler.class).to(GdoorCrawler.class);
    }
}
