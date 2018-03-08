package com.gdoor.crawler.elastic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class gets url for a given search query using google.com
 */
public class GoogleCrawler {

    /**
     * Method to convert the {@link InputStream} to {@link String}
     *
     * @param is
     *            the {@link InputStream} object
     * @return the {@link String} object returned
     */
    private static String getString(InputStream is) {
        StringBuilder sb = new StringBuilder();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            /** finally block to close the {@link BufferedReader} */
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * The method will return the search page result in a {@link String} object
     *
     * @param path
     *            the google search query
     * @return the content as {@link String} object
     * @throws Exception
     */
    private  String getSearchContent(String path) throws Exception {
        final String agent = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
        URL url = new URL(path);
        final URLConnection connection = url.openConnection();
        /**
         * User-Agent is mandatory otherwise Google will return HTTP response
         * code: 403
         */
        connection.setRequestProperty("User-Agent", agent);
        final InputStream stream = connection.getInputStream();
        return getString(stream);
    }

    /**
     * Parse all links
     *
     * @param html
     *            the page
     * @return the list with all URLSs
     * @throws Exception
     */
    private List<String> parseLinks(final String html) throws Exception {
        List<String> result = new ArrayList<String>();
        String pattern1 = "<h3 class=\"r\"><a href=\"/url?q=";
        String pattern2 = "\">";
        Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
        Matcher m = p.matcher(html);

        while (m.find()) {
            String domainName = m.group(0).trim();

            /** remove the unwanted text */
            domainName = domainName.substring(domainName.indexOf("/url?q=") + 7);
            domainName = domainName.substring(0, domainName.indexOf("&amp;"));

            result.add(domainName);
        }
        return result;
    }


    public List<String> getUrlsFromGoogleFromASearchTerm(String searchTerm) throws Exception {
        //TODO extract it in config file.
        String query = "https://www.google.com/search?q="+ searchTerm + "&num=12&as_qdr=all";
        String page = getSearchContent(query);
        List<String> links = parseLinks(page);
        return links;
    }
    public static void main(String[] args) throws Exception {
        String searchTerm = "Afghanistan";
        System.out.println("Google Search Parser Tutorial");
        System.out.println("Searching for: " + searchTerm);
        /*//String query = "https://www.google.com/search?q=" + searchTerm + "&num=10";
        String query = "https://www.google.com/search?q="+ searchTerm + "&num=12&as_qdr=all";
        String page = getSearchContent(query);
        List<String> links = parseLinks(page);*/
        System.out.println();
        GoogleCrawler googleCrawler = new GoogleCrawler();
        List<String> links = googleCrawler.getUrlsFromGoogleFromASearchTerm("Afghanistan");
        System.out.println("Results:");
        for (int i = 0; i < links.size(); i++) {
            System.out.println(links.get(i));
        }
    }
}