package com.gdoor.common.model;


import java.util.Objects;

public class SearchTaskImpl implements SearchTask {
    /**
     * {
     * <p>
     * "SearchTask" : {
     * <p>
     * "properties" : {
     * <p>
     * "taskName" : { "type" : "string", "index" : "not_analyzed" },
     * <p>
     * "keywords" : { "type" : "string", "index" : "not_analyzed" }
     * <p>
     * "createdAt" : { "type" : "date", "format": "epoch_millis||dateOptionalTime" },
     * <p>
     * "active" : { "type" : "boolean" }
     * <p>
     * }
     * <p>
     * }
     */

    private Properties properties;

    public SearchTaskImpl() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchTaskImpl that = (SearchTaskImpl) o;
        return Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {

        return Objects.hash(properties);
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }


}
