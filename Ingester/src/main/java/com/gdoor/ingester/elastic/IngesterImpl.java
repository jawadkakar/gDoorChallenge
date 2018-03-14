package com.gdoor.ingester.elastic;


import com.gdoor.common.model.ElasticDataHolder;
import com.gdoor.common.model.SearchTaskImpl;
import com.gdoor.crawler.module.Crawler;
import com.gdoor.crawler.module.CrawlerModule;
import com.gdoor.ingester.model.DataStore;
import com.gdoor.ingester.module.Ingester;
import com.gdoor.ingester.util.DataStoreParser;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Optional;


public class IngesterImpl implements Ingester {

    /**
     * This method takes the yaml file and does the following
     * 0: Parse the yaml file and populates DataStore object, which mimics the input yaml file.
     * 1: Builds SearchTask from DataStore, since rest of code will deal with SearchTask object
     *
     * @param fileName yaml input file from program argument
     */
    @Override
    public void ingest(String fileName) {

        DataStore dataStore = null;
        try {
            dataStore = DataStoreParser.dataStoreParser(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Optional<DataStore> ods = Optional.ofNullable(dataStore);

        if (ods.isPresent()) {
            delegateToCrawler(dataStore);
        }
    }

    /**
     * This method builds SearchTask and then calls the crawler
     *
     * @param dataStore
     */
    private void delegateToCrawler(DataStore dataStore)  {
        Injector crawlerInjector = Guice.createInjector(new CrawlerModule());
        Crawler crawler = crawlerInjector.getInstance(Crawler.class);
        ElasticDataHolder searchTaskHolder = searchTaskBuilder(dataStore);
        crawler.crawl(searchTaskHolder);
    }

    /**
     * This method build a searchTask object from DataStore object
     *
     * @param dataStore all the keywords in the file.
     * @return SearchTask which is build from the input parameter.
     */
    private ElasticDataHolder searchTaskBuilder(DataStore dataStore) {
        ElasticDataHolder holder = new ElasticDataHolder();
        SearchTaskImpl task = new SearchTaskImpl();
        holder.setSearchTask(task);
        SearchTaskImpl.Property<String> taskName = new SearchTaskImpl.Property<>(dataStore.getTaskName(), "not_analyzed");
        SearchTaskImpl.Property<String> keyWords = new SearchTaskImpl.Property<>(dataStore.getKeywords(), "not_analyzed");

        long dateInSecond = 0;
        Optional<String> date = Optional.ofNullable(dataStore.getDate());
        if (date.isPresent()) {
            dateInSecond = convertDateStringToSeconds(dataStore.getDate());
        }

        SearchTaskImpl.Property<java.io.Serializable> createdAt = new SearchTaskImpl.Property<>(dateInSecond);
        createdAt.setFormat("epoch_millis");
        SearchTaskImpl.Property<Boolean> active = new SearchTaskImpl.Property<>(false);

        SearchTaskImpl.Properties properties = new SearchTaskImpl.Properties(taskName, keyWords, createdAt, active, null, null);

        task.setProperties(properties);
        holder.setSearchTask(task);

        ObjectMapper mapper = JsonFactory.create();
        String jsonString = mapper.toJson(holder);
        System.out.println(jsonString);
        return holder;
    }

    private long convertDateStringToSeconds(String inputDate) {
        long dateInSecond = 0;
        Optional<String> date = Optional.ofNullable(inputDate);
        try {
            if (date.isPresent()) {
                /** Change the date into epoch time **/
                DateTimeFormatter formatter =
                        new DateTimeFormatterBuilder().appendPattern("dd/MM/yyyy[ [HH][:mm][:ss][.SSS]]")
                                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                                .toFormatter();
                dateInSecond = LocalDateTime.parse(inputDate, formatter).getLong(ChronoField.EPOCH_DAY)*24*60*60;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateInSecond;
    }

    public static void main(String[] args) {
        DataStore dataStore = new DataStore();
        dataStore.setTaskName("GoldenHorn");
        dataStore.setDate("10/03/2018");
        dataStore.setKeywords("mediterranean food, aegean sea, Greece, Athens, Michael D. Higgins, Ireland");
        dataStore.setActive(true);
        IngesterImpl ingester = new IngesterImpl();
        ingester.searchTaskBuilder(dataStore);

    }
}
