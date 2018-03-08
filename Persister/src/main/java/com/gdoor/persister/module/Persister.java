package com.gdoor.persister.module;


import com.gdoor.common.model.ElasticDataHolder;

public interface Persister {

    void persist(ElasticDataHolder resultTask);
}
