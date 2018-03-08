package com.gdoor.crawler.elastic;

import org.jsoup.Jsoup;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class UrlContentDownLoader {

    public StringBuilder contentDownloader(String url) throws MalformedURLException, FileNotFoundException,IOException {
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


        }/* catch (MalformedURLException mue) {

            System.out.println("Ouch - a MalformedURLException happened.");
            mue.printStackTrace();
        } catch (FileNotFoundException e){
            System.out.println("Oops- an FileNotFoundException happened.");
            e.printStackTrace();
        }
        catch (IOException e) {

            System.out.println("Oops- an Exception happened.");
            e.printStackTrace();
        } */finally {
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

   /* public static void main(String[] args) {
        UrlContentDownLoader contentDownLoader = new UrlContentDownLoader();
        StringBuilder content = contentDownLoader.contentDownloader("https://en.wikipedia.org/wiki/Afghanistan");
        System.out.println(content);
    }*/

}
