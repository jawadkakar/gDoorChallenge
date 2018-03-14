package com.gdoor.persister.elastic;

import com.gdoor.common.model.ElasticDataHolder;
import com.gdoor.persister.module.Persister;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Optional;

public class ElasticPersister implements Persister {

    Client client;

    public ElasticPersister() throws UnknownHostException {
        this.client = getClient();
    }

    /**
     * @param taskHolder
     */
    @Override
    public void persist(ElasticDataHolder taskHolder) {
        ObjectMapper mapper = JsonFactory.create();
        if (taskHolder.isFirstRun()) {
            persistSearchTask(taskHolder, mapper);
            persistSearchResult(taskHolder, mapper);
        } else {
            persistSearchResult(taskHolder, mapper);
        }
    }

    private void persistSearchTask(ElasticDataHolder taskHolder, ObjectMapper mapper) {

        String searchTaskJson = mapper.toJson(taskHolder.getSearchTask());
        if (Optional.ofNullable(searchTaskJson).isPresent() && searchTaskJson.length() > 0) {
            IndexResponse response = client.prepareIndex("lookingsearch", "searchTask").setSource(searchTaskJson, XContentType.JSON).get();
            System.out.println(response.toString());
        }

    }

    private void persistSearchResult(ElasticDataHolder taskHolder, ObjectMapper mapper) {
        String searchResultJson = mapper.toJson(taskHolder.getSearchResult());
        if (Optional.ofNullable(searchResultJson).isPresent() && searchResultJson.length() > 0) {
            IndexResponse response = client.prepareIndex("lookingresult", "searchResult").setSource(searchResultJson, XContentType.JSON).get();
            System.out.println(response.toString());
        }
    }

    private static Client getClient() throws UnknownHostException {
        TransportAddress address = new TransportAddress(InetAddress.getByName("localhost"), 9300);
        /** for client side load balancing. **/
        Settings settings = Settings.builder().put("client.transport.sniff", true).build();
        Client client = new PreBuiltTransportClient(settings).addTransportAddress(address);
        return client;
    }

    /**
     * To view the index for a given id
     *
     * @return
     */
    private Map<String, Object> view(String id) {
        GetResponse getResponse = client.prepareGet("gdoor", "SearchTask", id).get();
        return getResponse.getSource();
    }


}
