package com.gdoor.common.model;

import java.util.Objects;

public class ElasticDataHolder {

    private SearchTaskImpl searchTask;
    private SearchTaskImpl searchResult;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElasticDataHolder that = (ElasticDataHolder) o;
        return Objects.equals(searchResult, that.searchResult) &&
                Objects.equals(searchTask, that.searchTask);
    }

    @Override
    public int hashCode() {

        return Objects.hash(searchResult, searchTask);
    }

    public SearchTaskImpl getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchTaskImpl searchResult) {
        this.searchResult = searchResult;
    }

    public SearchTaskImpl getSearchTask() {
        return searchTask;
    }

    public void setSearchTask(SearchTaskImpl searchTask) {
        this.searchTask = searchTask;
    }
}
