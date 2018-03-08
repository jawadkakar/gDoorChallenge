package com.gdoor.crawler.util;

import com.gdoor.common.model.ElasticDataHolder;
import com.gdoor.common.model.SearchTaskImpl;

import java.util.Optional;

public class KeywordExtractor {

    public String[] extractKeyWord(ElasticDataHolder searchTaskHolder){
        String [] keywordArray = new String[]{};
        Optional<ElasticDataHolder> holderOp = Optional.ofNullable(searchTaskHolder);
        if(holderOp.isPresent()){
            Optional<SearchTaskImpl> searchTaskOp = Optional.ofNullable(searchTaskHolder.getSearchTask());
            if(searchTaskOp.isPresent()){
                Optional<SearchTaskImpl.Properties> propertiesOptional = Optional.ofNullable(searchTaskHolder.getSearchTask().getProperties());
                if(propertiesOptional.isPresent()){
                    SearchTaskImpl.Properties properties = searchTaskHolder.getSearchTask().getProperties();
                     Optional<SearchTaskImpl.Property> keywordOptional = Optional.ofNullable(properties.getKeywords());
                     if(keywordOptional.isPresent()){
                         String keywords = (String) keywordOptional.get().getType();
                         if(keywords != null){
                             keywordArray = keywords.split(",");
                         }
                     }
                }
            }
        }
        return keywordArray;
    }
}
