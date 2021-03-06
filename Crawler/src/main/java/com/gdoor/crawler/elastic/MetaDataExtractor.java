package com.gdoor.crawler.elastic;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * This class extract meta data for a given url
 */
public class MetaDataExtractor {

    private String getMetaTag(Document document, String attr) {
        Elements elements = document.select("meta[name=" + attr + "]");
        for (Element element : elements) {
            final String s = element.attr("content");
            if (s != null) return s;
        }
        elements = document.select("meta[property=" + attr + "]");
        for (Element element : elements) {
            final String s = element.attr("content");
            if (s != null) return s;
        }
        return null;
    }

    //TODO change my name after creating an object which takes attributes in.

    public PageAttributeHolder extractMetaData(String forAGivenUrl) {
        PageAttributeHolder holder = null;
        Connection.Response re = null;
        try {
            holder = new PageAttributeHolder();
            MetaDataExtractor m = new MetaDataExtractor();

            re = Jsoup.connect(forAGivenUrl).execute();
            holder.setStatusCode(re.statusCode());
            Document doc =re.parse();

            //Document doc = Jsoup.connect(forAGivenUrl).get();
            String description = m.getMetaTag(doc, "description");
            holder.setDescription(description);
            if (description == null) {
                description = m.getMetaTag(doc, "og:description");
                holder.setDescription(description);
            }
            String pubDate = m.getMetaTag(doc, "pubdate");
            holder.setPublishedDate(pubDate);
            if (pubDate == null) {
                pubDate = m.getMetaTag(doc, "og:pubdate");
                holder.setPublishedDate(pubDate);
            }
            String lastModificationDate = m.getMetaTag(doc, "lastmod");
            holder.setLastModificationDate(lastModificationDate);
            if (lastModificationDate == null) {
                lastModificationDate = m.getMetaTag(doc, "og:lastmod");
                holder.setLastModificationDate(lastModificationDate);
            }
            String url = getMetaTag(doc, "url");
            holder.setUrl(url);
            if (url == null) {
                url = getMetaTag(doc, "og:url");
                holder.setUrl(url);
            }
            String title = getMetaTag(doc, "og:title");
            holder.setTitle(title);
            String twitterTitle = getMetaTag(doc, "twitter:title");
            holder.setTwitterTitle(twitterTitle);
            String twitterDescription = getMetaTag(doc, "twitter:description");
            holder.setTwitterDescription(twitterDescription);
            String siteName = getMetaTag(doc, "og:site_name");
            holder.setSiteName(siteName);
            // and others you need to
            System.out.println("Description: " + description);
            System.out.println("Published Date:  " + pubDate);
            System.out.println("Last Modification Date: " + lastModificationDate);
            System.out.println("URL: " + url);
            System.out.println("Title: " + title);
            System.out.println("Twitter Title: " + twitterTitle);
            System.out.println("Twitter Description: " + twitterDescription);
            System.out.println("Site Name: " + siteName);
            System.out.printf("Status Code: "+holder.getStatusCode());
            System.out.println();
        }catch (HttpStatusException e){
            holder.setStatusCode(e.getStatusCode());
            holder.setUrl(e.getUrl());
            holder.setDescription(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return holder;
    }

    protected static class PageAttributeHolder {
        private String title;
        private String description;
        private String publishedDate;
        private String url;
        private String twitterTitle;
        private String twitterDescription;
        private String siteName;
        private String lastModificationDate;
        private int statusCode;


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PageAttributeHolder that = (PageAttributeHolder) o;
            return Objects.equals(title, that.title) &&
                    Objects.equals(description, that.description) &&
                    Objects.equals(publishedDate, that.publishedDate) &&
                    Objects.equals(url, that.url) &&
                    Objects.equals(twitterTitle, that.twitterTitle) &&
                    Objects.equals(twitterDescription, that.twitterDescription) &&
                    Objects.equals(siteName, that.siteName) &&
                    Objects.equals(lastModificationDate, that.lastModificationDate) &&
                    Objects.equals(statusCode, that.statusCode);
        }

        @Override
        public int hashCode() {

            return Objects.hash(title, description, publishedDate, url, twitterTitle, twitterDescription, siteName, lastModificationDate, statusCode);
        }

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public String getLastModificationDate() {
            return lastModificationDate;
        }

        public void setLastModificationDate(String lastModificationDate) {
            this.lastModificationDate = lastModificationDate;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPublishedDate() {
            return publishedDate;
        }

        public void setPublishedDate(String publishedDate) {
            this.publishedDate = publishedDate;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTwitterTitle() {
            return twitterTitle;
        }

        public void setTwitterTitle(String twitterTitle) {
            this.twitterTitle = twitterTitle;
        }

        public String getTwitterDescription() {
            return twitterDescription;
        }

        public void setTwitterDescription(String twitterDescription) {
            this.twitterDescription = twitterDescription;
        }

        public String getSiteName() {
            return siteName;
        }

        public void setSiteName(String siteName) {
            this.siteName = siteName;
        }
    }
}
