package com.gdoor.common.model;


import java.util.Objects;

public class SearchTaskImpl  {
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
    public static  class Properties {

        private Property taskName;
        private Property keywords;
        private Property createAt;
        private Property active;
        private Property title;
        private Property httpStatusCode;
        private Property content;

        public Properties() {

        }

        public Properties(Property taskName, Property keywords, Property createAt, Property active, Property title, Property httpStatusCode) {
            this.taskName = taskName;
            this.keywords = keywords;
            this.createAt = createAt;
            this.active = active;
            this.title = title;
            this.httpStatusCode = httpStatusCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Properties that = (Properties) o;
            return Objects.equals(taskName, that.taskName) &&
                    Objects.equals(keywords, that.keywords) &&
                    Objects.equals(createAt, that.createAt) &&
                    Objects.equals(active, that.active) &&
                    Objects.equals(title, that.title) &&
                    Objects.equals(httpStatusCode, that.httpStatusCode) &&
                    Objects.equals(content, that.content);
        }

        @Override
        public int hashCode() {

            return Objects.hash(taskName, keywords, createAt, active, title, httpStatusCode, content);
        }

        public Property getTaskName() {
            return taskName;
        }

        public void setTaskName(Property taskName) {
            this.taskName = taskName;
        }

        public Property getKeywords() {
            return keywords;
        }

        public void setKeywords(Property keywords) {
            this.keywords = keywords;
        }

        public Property getCreateAt() {
            return createAt;
        }

        public void setCreateAt(Property createAt) {
            this.createAt = createAt;
        }

        public Property getActive() {
            return active;
        }

        public void setActive(Property active) {
            this.active = active;
        }

        public Property getTitle() {
            return title;
        }

        public void setTitle(Property title) {
            this.title = title;
        }

        public Property getHttpStatusCode() {
            return httpStatusCode;
        }

        public void setHttpStatusCode(Property httpStatusCode) {
            this.httpStatusCode = httpStatusCode;
        }

        public Property getContent() {
            return content;
        }

        public void setContent(Property content) {
            this.content = content;
        }
    }

    public static class Property<T> {

        private T type;
        private T index;
        private T format;

        public Property(T type) {
            this.type = type;
        }

        public Property(T type, T index) {
            this.type = type;
            this.index = index;
        }

        public T getFormat() {
            return format;
        }

        public void setFormat(T format) {
            this.format = format;
        }

        public T getType() {
            return type;
        }

        public void setType(T type) {
            this.type = type;
        }

        public T getIndex() {
            return index;
        }

        public void setIndex(T index) {
            this.index = index;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Property<?> property = (Property<?>) o;
            return Objects.equals(type, property.type) &&
                    Objects.equals(index, property.index);
        }

        @Override
        public int hashCode() {

            return Objects.hash(type, index);
        }
    }

}
