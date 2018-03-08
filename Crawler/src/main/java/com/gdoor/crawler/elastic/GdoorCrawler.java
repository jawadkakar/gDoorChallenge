package com.gdoor.crawler.elastic;

import com.gdoor.common.model.ElasticDataHolder;
import com.gdoor.common.model.SearchTaskImpl;
import com.gdoor.common.model.SearchTaskImpl.Property;
import com.gdoor.crawler.module.Crawler;
import com.gdoor.crawler.util.KeywordExtractor;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.List;

public class GdoorCrawler implements Crawler {
    @Override
    public void crawl(ElasticDataHolder searchTaskHolder)  {
        GoogleCrawler gCrawler = new GoogleCrawler();
        MetaDataExtractor m = new MetaDataExtractor();
        KeywordExtractor keywordExtractor = new KeywordExtractor();
        String[] keywords = keywordExtractor.extractKeyWord(searchTaskHolder);
        //keywords = new String[] {"Afghanistan"};
        //put it inside Optional
        SearchTaskImpl.Properties searchHolderProperties  = searchTaskHolder.getSearchTask().getProperties();
        Property taskNameProperty = searchHolderProperties.getTaskName();
        try {
            for (int i = 0; i < keywords.length; i++) {
                List<String> links = gCrawler.getUrlsFromGoogleFromASearchTerm(keywords[i].trim());
                System.out.println("Total result returned: " + links.size());
                System.out.println("Keyword: "+keywords[i]);
                for (String link : links) {
                    System.out.println();
                    ElasticDataHolder holder = new ElasticDataHolder();
                    SearchTaskImpl  searchTask = new SearchTaskImpl();
                    SearchTaskImpl.Properties properties = new SearchTaskImpl.Properties();
                    properties.setTaskName(taskNameProperty);
                    System.out.println("URL: " +link);
                    UrlContentDownLoader contentDownLoader = new UrlContentDownLoader();
                    StringBuilder content = contentDownLoader.contentDownloader(link);
                    System.out.println("Content: " + content);
                    /** Build content Property **/
                    Property contentProperty = new Property(content, "analyzed");
                    properties.setContent(contentProperty);
                    MetaDataExtractor.PageAttributeHolder pageAttributeHolder =  m.extractMetaData(link);
                    /** Build title property **/
                    Property titleProperty = new Property(pageAttributeHolder.getTitle(), "analyzed");
                    properties.setTitle(titleProperty);
                    Property httpStatusCode = new Property(pageAttributeHolder.getStatusCode());
                    properties.setHttpStatusCode(httpStatusCode);

                    //TODO complete createAt
                    Property createAtProperty = new Property(pageAttributeHolder.getLastModificationDate(), "epoch");
                    properties.setCreateAt(createAtProperty);
                    searchTask.setProperties(properties);
                    holder.setSearchResult(searchTask);
                    //delegateToPersister(holder);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }



/*
    private void delegateToPersister(ElasticDataHolder taskHolder)  {
        Injector persisterInjector = Guice.createInjector(new PersisterModule());
        Persister persister = persisterInjector.getInstance(Persister.class);
        persister.persist(taskHolder);
    }
*/

    public static void main(String[] args) {
        GdoorCrawler gdoor = new GdoorCrawler();
        try {
            gdoor.crawl(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
