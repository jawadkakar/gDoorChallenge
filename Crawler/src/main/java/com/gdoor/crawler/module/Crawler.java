package com.gdoor.crawler.module;

import com.gdoor.common.model.ElasticDataHolder;

public interface Crawler {

    void crawl(ElasticDataHolder holder);
}
