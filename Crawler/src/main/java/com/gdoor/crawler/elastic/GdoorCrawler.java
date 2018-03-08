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

public class GdoorCrawler implements Crawler {
    @Override
    public void crawl(ElasticDataHolder searchTaskHolder)  {
        GoogleCrawler gCrawler = new GoogleCrawler();
        MetaDataExtractor m = new MetaDataExtractor();
        KeywordExtractor keywordExtractor = new KeywordExtractor();
        Optional<ElasticDataHolder> st = Optional.ofNullable(searchTaskHolder);
        String[] keywords;

        if(st.isPresent()) {
            keywords = keywordExtractor.extractKeyWord(searchTaskHolder);

            Optional<SearchTaskImpl> stOpt = Optional.ofNullable(searchTaskHolder.getSearchTask());
            /**
             * If searchTask is not present it will not be delegated to persistence layer
             */
            if(stOpt.isPresent()) {
                SearchTaskImpl.Properties searchHolderProperties = searchTaskHolder.getSearchTask().getProperties();
                Property taskNameProperty = searchHolderProperties.getTaskName();
                try {
                    for (int i = 0; i < keywords.length; i++) {
                        List<String> links = gCrawler.getUrlsFromGoogleFromASearchTerm(keywords[i].trim());
                        System.out.println("Total result returned: " + links.size());
                        System.out.println("Keyword: " + keywords[i]);
                        /** Build keyword property **/

                        for (String link : links) {
                            System.out.println();
                            ElasticDataHolder holder = new ElasticDataHolder();
                            SearchTaskImpl searchTask = new SearchTaskImpl();
                            SearchTaskImpl.Properties properties = new SearchTaskImpl.Properties();
                            properties.setTaskName(taskNameProperty);
                            properties.setKeywords(new Property<>(keywords[i], "not_analyzed"));
                            System.out.println("URL: " + link);

                            UrlContentDownLoader contentDownLoader = new UrlContentDownLoader();

                            MetaDataExtractor.PageAttributeHolder pageAttributeHolder = m.extractMetaData(link);
                            /** build url property **/
                            properties.setUrl(new Property<>(link, "analyzed"));
                            /** Build title property **/
                            Property<String> titleProperty = new Property<>(pageAttributeHolder.getTitle(), pageAttributeHolder.getTitle() != null ?"analyzed" : null);
                            properties.setTitle(titleProperty);
                            Property<String> httpStatusCode = new Property<>(pageAttributeHolder.getStatusCode()+"", "not_analyzed");
                            properties.setHttpStatusCode(httpStatusCode);
                            properties.setActive(new Property<>(true));


                            //TODO complete createAt
                            Property<String> createAtProperty = new Property<>(pageAttributeHolder.getLastModificationDate(), pageAttributeHolder.getLastModificationDate()!= null?"epoch":null);
                            properties.setCreateAt(createAtProperty);
                            searchTask.setProperties(properties);

                            try {
                                StringBuilder content = contentDownLoader.contentDownloader(link);
                                System.out.println("Content: " + content);
                                /** Build content Property **/
                                Property<java.io.Serializable> contentProperty = new Property<>(content, content != null ?"analyzed": null);
                                properties.setContent(contentProperty);
                            }catch (MalformedURLException mfu ){
                                String description = mfu.getMessage();
                                properties.setError(new Property<>("MalformedURLException "+ description, "analyzed"));
                                mfu.printStackTrace();
                            }
                            catch (FileNotFoundException e){
                                String description = e.getMessage();
                                properties.setError(new Property<>("FileNotFoundException "+ description,"analyzed"));
                                e.printStackTrace();
                            }
                            catch(IOException io){
                                String description = io.getMessage();
                                properties.setError(new Property<>("IOException "+ description,"analyzed"));
                                io.printStackTrace();
                            }

                            holder.setSearchResult(searchTask);
                            holder.setSearchTask(searchTaskHolder.getSearchTask());
                            delegateToPersister(holder);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }



    private void delegateToPersister(ElasticDataHolder taskHolder)  {
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
