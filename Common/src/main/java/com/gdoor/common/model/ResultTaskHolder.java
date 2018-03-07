package com.gdoor.common.model;

import java.util.Objects;

public class ResultTaskHolder {

    private SearchTaskImpl searchResult;

    public SearchTaskImpl getSearchResult() {
        return searchResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultTaskHolder that = (ResultTaskHolder) o;
        return Objects.equals(searchResult, that.searchResult);
    }

    @Override
    public int hashCode() {

        return Objects.hash(searchResult);
    }

    public void setSearchResult(SearchTaskImpl searchResult) {
        this.searchResult = searchResult;
    }
}
