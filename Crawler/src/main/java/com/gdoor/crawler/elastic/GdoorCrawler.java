package com.gdoor.crawler.elastic;

import com.gdoor.common.model.ElasticDataHolder;
import com.gdoor.common.model.SearchTaskImpl;
import com.gdoor.common.model.SearchTaskImpl.Property;
import com.gdoor.crawler.module.Crawler;
import com.gdoor.crawler.util.KeywordExtractor;
import com.gdoor.persister.module.Persister;
import com.gdoor.persister.module.PersisterModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.google.common.base.Stopwatch;

public class GdoorCrawler implements Crawler {

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public void crawl(ElasticDataHolder searchTaskHolder) {
        KeywordExtractor keywordExtractor = new KeywordExtractor();
        Optional<ElasticDataHolder> st = Optional.ofNullable(searchTaskHolder);
        String[] keywords;
        /** add executor services in here **/
        ExecutorService executor = Executors.newFixedThreadPool(10);
        Stopwatch stopwatch = Stopwatch.createStarted();
        if (st.isPresent()) {
            keywords = keywordExtractor.extractKeyWord(searchTaskHolder);

            Optional<SearchTaskImpl> stOpt = Optional.ofNullable(searchTaskHolder.getSearchTask());
            /**
             * If searchTask is not present it will not be delegated to persistence layer
             */
            if (stOpt.isPresent()) {
                SearchTaskImpl.Properties searchHolderProperties = searchTaskHolder.getSearchTask().getProperties();
                Property taskNameProperty = searchHolderProperties.getTaskName();
                for (int i = 0; i < keywords.length; i++) {

                    boolean isFirstRun = false;
                    if (i == 0) {
                        isFirstRun = true;
                    }

                    String query = keywords[i].trim().replaceAll("\\s+","&");
                    ContentDownLoader downLoader = new ContentDownLoader(searchTaskHolder, query, taskNameProperty, isFirstRun);
                    executor.submit(downLoader);


                }
            }

        }
        executor.shutdown();

    }



}
