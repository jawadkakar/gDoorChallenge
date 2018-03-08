package com.gdoor.persister.elastic;

import com.gdoor.common.model.ElasticDataHolder;
import com.gdoor.persister.module.Persister;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Optional;

public class PersisterImpl implements Persister {

    Client client;

    public PersisterImpl() throws UnknownHostException {
        this.client = getClient();
    }

    @Override
    public void persist(ElasticDataHolder taskHolder) {
        ObjectMapper mapper = JsonFactory.create();
        String jsonString = mapper.toJson(taskHolder);
        if(Optional.ofNullable(jsonString).isPresent() && jsonString.length() > 0) {
            IndexResponse response = client.prepareIndex("gdoor", "SearchTask").setSource(jsonString, XContentType.JSON).get();
            System.out.println(response.toString());
        }
        // add another if statement for SearchTaskHolder

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
     * @return
     */
    private Map<String, Object> view(String id) {
        GetResponse getResponse = client.prepareGet("gdoor", "SearchTask", id).get();
       // System.out.println(getResponse.getSource().toString());
        return getResponse.getSource();


    }

    public static void main(String[] args) throws UnknownHostException {
        PersisterImpl impl = new PersisterImpl();
        impl.persist(null);
        Client client =null;
        try {
            client = getClient();
            SearchResponse response = client.prepareSearch().get();
            System.out.println(impl.view("1KmOAWIBMHLGVu_rkLjU").toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        /** to see all docs inside elastic search **/
        SearchResponse response = client.prepareSearch().get();
        System.out.println();
    }

    //TODO

    /**
     * Bulk processing with elastic search
     * <p>
     * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-bulk-processor.html
     */

  /*  void foo(User user){
        JSONObject dataAsJson = new JSONObject(dataAsJsonFormattedString);
        HashMap<String, Object> dataAsMap = new HashMap<String, Object>(dataAsJson.toMap());
        bulkRequestBuilder.add(new IndexRequest(index, "tweets_juan").source(dataAsMap, XContentType.JSON));
    }*/


}
