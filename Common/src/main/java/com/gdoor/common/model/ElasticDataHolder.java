package com.gdoor.common.model;

import java.util.Objects;

public class ElasticDataHolder {

    private SearchTaskImpl searchTask;
    private SearchTaskImpl searchResult;
    private boolean isFirstRun;


    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElasticDataHolder that = (ElasticDataHolder) o;
        return isFirstRun == that.isFirstRun &&
                Objects.equals(searchTask, that.searchTask) &&
                Objects.equals(searchResult, that.searchResult);
    }

    @Override
    public int hashCode() {

        return Objects.hash(searchTask, searchResult, isFirstRun);
    }

    public SearchTaskImpl getSearchTask() {
        return searchTask;
    }

    public void setSearchTask(SearchTaskImpl searchTask) {
        this.searchTask = searchTask;
    }

    public SearchTaskImpl getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchTaskImpl searchResult) {
        this.searchResult = searchResult;
    }

    public boolean isFirstRun() {
        return isFirstRun;
    }

    public void setFirstRun(boolean firstRun) {
        isFirstRun = firstRun;
    }
}
