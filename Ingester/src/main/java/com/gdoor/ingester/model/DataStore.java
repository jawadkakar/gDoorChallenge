package com.gdoor.ingester.model;

import java.util.Objects;

/**
 * taskName: GoldenHorn
 * date: 2018-3-10
 * keywords: mediterranean food, aegean sea, Greece, Athens, Michael D. Higgins, Ireland
 * active: true
 */
public class DataStore {
    private String taskName;
    private String date;
    private String keywords;
    private boolean active;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataStore dataStore = (DataStore) o;
        return active == dataStore.active &&
                Objects.equals(taskName, dataStore.taskName) &&
                Objects.equals(date, dataStore.date) &&
                Objects.equals(keywords, dataStore.keywords);
    }

    @Override
    public int hashCode() {

        return Objects.hash(taskName, date, keywords, active);
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
