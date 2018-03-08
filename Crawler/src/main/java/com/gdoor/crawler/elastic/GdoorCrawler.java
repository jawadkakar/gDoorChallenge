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

public class GdoorCrawler implements Crawler {

    ExecutorService executor = Executors.newFixedThreadPool(10);

    @Override
    public void crawl(ElasticDataHolder searchTaskHolder) {
        KeywordExtractor keywordExtractor = new KeywordExtractor();
        Optional<ElasticDataHolder> st = Optional.ofNullable(searchTaskHolder);
        String[] keywords;
        /** add executor services in here **/

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


                    hardWorkingCrawler(searchTaskHolder, keywords[i], taskNameProperty, isFirstRun);

                }
            }
        }
    }

    /**
     * This method does all the hard work, for each keyword it calls google to get the links
     * and for each link it downloads the content, etc.
     * @param searchTaskHolder
     * @param keyword
     * @param taskNameProperty
     * @param isFirstRun
     */
    private void hardWorkingCrawler(ElasticDataHolder searchTaskHolder, String keyword, Property taskNameProperty, boolean isFirstRun) {
        GoogleCrawler gCrawler = new GoogleCrawler();
        MetaDataExtractor m = new MetaDataExtractor();
        List<String> links = null;
        try {
            links = gCrawler.getUrlsFromGoogleFromASearchTerm(keyword.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Total result returned: " + links.size());
        System.out.println("Keyword: " + keyword);
        for (String link : links) {
            System.out.println();
            ElasticDataHolder holder = new ElasticDataHolder();
            SearchTaskImpl searchTask = new SearchTaskImpl();
            SearchTaskImpl.Properties properties = new SearchTaskImpl.Properties();
            UrlContentDownLoader contentDownLoader = new UrlContentDownLoader();
            try {
                properties.setTaskName(taskNameProperty);
                properties.setKeywords(new Property<>(keyword, "not_analyzed"));
                System.out.println("URL: " + link);

                MetaDataExtractor.PageAttributeHolder pageAttributeHolder = m.extractMetaData(link);
                /** build url property **/
                properties.setUrl(new Property<>(link, "analyzed"));
                /** Build title property **/
                Property<String> titleProperty = new Property<>(pageAttributeHolder.getTitle(), pageAttributeHolder.getTitle() != null ? "analyzed" : null);
                properties.setTitle(titleProperty);
                /** Build status code **/
                Property<String> httpStatusCode = new Property<>(pageAttributeHolder.getStatusCode() + "", "not_analyzed");
                properties.setHttpStatusCode(httpStatusCode);
                properties.setActive(new Property<>(true));
                //TODO complete createAt
                Property<String> createAtProperty = new Property<>(pageAttributeHolder.getLastModificationDate(), pageAttributeHolder.getLastModificationDate() != null ? "epoch" : null);
                properties.setCreateAt(createAtProperty);
                searchTask.setProperties(properties);

                StringBuilder content = contentDownLoader.contentDownloader(link);
                System.out.println("Content: " + content);
                /** Build content Property **/
                Property<java.io.Serializable> contentProperty = new Property<>(content, content != null ? "analyzed" : null);
                properties.setContent(contentProperty);
            } catch (MalformedURLException mfu) {
                String description = mfu.getMessage();
                properties.setError(new Property<>("MalformedURLException " + description, "analyzed"));
                mfu.printStackTrace();
            } catch (FileNotFoundException e) {
                String description = e.getMessage();
                properties.setError(new Property<>("FileNotFoundException " + description, "analyzed"));
                e.printStackTrace();
            } catch (IOException io) {
                String description = io.getMessage();
                properties.setError(new Property<>("IOException " + description, "analyzed"));
                io.printStackTrace();
            }

            holder.setSearchResult(searchTask);
            holder.setSearchTask(searchTaskHolder.getSearchTask());
            if (isFirstRun) {
                holder.setFirstRun(true);
            }
            delegateToPersister(holder);
        }
    }

    private void delegateToPersister(ElasticDataHolder taskHolder) {
        Injector persisterInjector = Guice.createInjector(new PersisterModule());
        Persister persister = persisterInjector.getInstance(Persister.class);
        persister.persist(taskHolder);
    }

    public static void main(String[] args) {
        GdoorCrawler gdoor = new GdoorCrawler();
        try {
            gdoor.crawl(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
