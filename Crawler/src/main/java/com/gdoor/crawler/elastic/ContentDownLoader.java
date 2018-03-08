package com.gdoor.crawler.elastic;

import com.gdoor.common.model.ElasticDataHolder;
import com.gdoor.common.model.SearchTaskImpl;
import com.gdoor.persister.module.Persister;
import com.gdoor.persister.module.PersisterModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Objects;

public class ContentDownLoader implements Runnable {
    private ElasticDataHolder searchTaskHolder;
    private String keyword;
    private SearchTaskImpl.Property taskName;
    private boolean isFirstRun;

    @Override
    public void run() {
        hardWorkingCrawler();
    }

    public ContentDownLoader(ElasticDataHolder searchTaskHolder, String keyword, SearchTaskImpl.Property taskName, boolean isFirstRun) {
        this.searchTaskHolder = searchTaskHolder;
        this.keyword = keyword;
        this.taskName = taskName;
        this.isFirstRun = isFirstRun;
        hardWorkingCrawler();
    }

    private void hardWorkingCrawler(/**ElasticDataHolder searchTaskHolder, String keyword, SearchTaskImpl.Property taskNameProperty, boolean isFirstRun*/) {
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
                properties.setTaskName(taskName);
                properties.setKeywords(new SearchTaskImpl.Property<>(keyword, "not_analyzed"));
                System.out.println("URL: " + link);

                MetaDataExtractor.PageAttributeHolder pageAttributeHolder = m.extractMetaData(link);
                /** build url property **/
                properties.setUrl(new SearchTaskImpl.Property<>(link, "analyzed"));
                /** Build title property **/
                SearchTaskImpl.Property<String> titleProperty = new SearchTaskImpl.Property<>(pageAttributeHolder.getTitle(), pageAttributeHolder.getTitle() != null ? "analyzed" : null);
                properties.setTitle(titleProperty);
                /** Build status code **/
                SearchTaskImpl.Property<String> httpStatusCode = new SearchTaskImpl.Property<>(pageAttributeHolder.getStatusCode() + "", "not_analyzed");
                properties.setHttpStatusCode(httpStatusCode);
                properties.setActive(new SearchTaskImpl.Property<>(true));
                //TODO complete createAt
                SearchTaskImpl.Property<String> createAtProperty = new SearchTaskImpl.Property<>(pageAttributeHolder.getLastModificationDate(), pageAttributeHolder.getLastModificationDate() != null ? "epoch" : null);
                properties.setCreateAt(createAtProperty);
                searchTask.setProperties(properties);

                StringBuilder content = contentDownLoader.contentDownloader(link);
                System.out.println("Content: " + content);
                /** Build content Property **/
                SearchTaskImpl.Property<Serializable> contentProperty = new SearchTaskImpl.Property<>(content, content != null ? "analyzed" : null);
                properties.setContent(contentProperty);
            } catch (MalformedURLException mfu) {
                String description = mfu.getMessage();
                properties.setError(new SearchTaskImpl.Property<>("MalformedURLException " + description, "analyzed"));
                mfu.printStackTrace();
            } catch (FileNotFoundException e) {
                String description = e.getMessage();
                properties.setError(new SearchTaskImpl.Property<>("FileNotFoundException " + description, "analyzed"));
                e.printStackTrace();
            } catch (IOException io) {
                String description = io.getMessage();
                properties.setError(new SearchTaskImpl.Property<>("IOException " + description, "analyzed"));
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContentDownLoader that = (ContentDownLoader) o;
        return isFirstRun == that.isFirstRun &&
                Objects.equals(searchTaskHolder, that.searchTaskHolder) &&
                Objects.equals(keyword, that.keyword) &&
                Objects.equals(taskName, that.taskName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(searchTaskHolder, keyword, taskName, isFirstRun);
    }

    public ElasticDataHolder getSearchTaskHolder() {
        return searchTaskHolder;
    }

    public void setSearchTaskHolder(ElasticDataHolder searchTaskHolder) {
        this.searchTaskHolder = searchTaskHolder;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public SearchTaskImpl.Property getTaskName() {
        return taskName;
    }

    public void setTaskName(SearchTaskImpl.Property taskName) {
        this.taskName = taskName;
    }

    public boolean isFirstRun() {
        return isFirstRun;
    }

    public void setFirstRun(boolean firstRun) {
        isFirstRun = firstRun;
    }
}
