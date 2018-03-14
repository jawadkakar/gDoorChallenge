package com.gdoor.crawler.elastic;

import org.jsoup.Jsoup;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class UrlContentDownLoader {

    public StringBuilder contentDownloader(String url) throws IOException {
        StringBuilder content = new StringBuilder();
        URL u;
        InputStream is = null;
        DataInputStream dis;
        String s;

        try {

            u = new URL(url);
            is = u.openStream();         // throws an IOException
            dis = new DataInputStream(new BufferedInputStream(is));
            while ((s = dis.readLine()) != null) {
                content.append(html2text(s));
            }


        }finally {
            try {
                Optional<InputStream> input = Optional.ofNullable(is);
                if (input.isPresent()) {
                    is.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

        }
        return content;
    }

    /**
     * This method remove all the html from text.
     *
     * @param html
     * @return
     */
    private static String html2text(String html) {
        return Jsoup.parse(html).text();
    }


}
