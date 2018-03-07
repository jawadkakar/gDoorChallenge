package com.gdoor.common.model;

import java.util.Objects;

public class  SearchTaskHolder {

    private SearchTaskImpl searchTask;

    public SearchTaskHolder() {
    }

    public SearchTaskHolder(SearchTaskImpl searchTask) {
        this.searchTask = searchTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchTaskHolder that = (SearchTaskHolder) o;
        return Objects.equals(searchTask, that.searchTask);
    }

    @Override
    public int hashCode() {

        return Objects.hash(searchTask);
    }

    public SearchTaskImpl getSearchTask() {
        return searchTask;
    }

    public void setSearchTask(SearchTaskImpl searchTask) {
        this.searchTask = searchTask;
    }
}
