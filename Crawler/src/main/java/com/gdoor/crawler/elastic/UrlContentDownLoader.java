package com.gdoor.crawler.elastic;

//------------------------------------------------------------//
//  UrlContentDownLoader.java:                                          //
//------------------------------------------------------------//
//  A Java program that demonstrates a procedure that can be  //
//  used to download the contents of a specified URL.         //
//------------------------------------------------------------//
//  Code created by Developer's Daily                         //
//  http://www.DevDaily.com                                   //
//------------------------------------------------------------//

import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class UrlContentDownLoader {

    public StringBuilder contentDownloader (String url) {
        StringBuilder content = new StringBuilder();
        //-----------------------------------------------------//
        //  Step 1:  Start creating a few objects we'll need.
        //-----------------------------------------------------//

        URL u;
        InputStream is = null;
        DataInputStream dis;
        String s;

        try {

            //------------------------------------------------------------//
            // Step 2:  Create the URL.                                   //
            //------------------------------------------------------------//
            // Note: Put your real URL here, or better yet, read it as a  //
            // command-line arg, or read it from a file.                  //
            //------------------------------------------------------------//

            //u = new URL("https://en.wikipedia.org/wiki/Afghanistan");
              u = new URL(url);
            //----------------------------------------------//
            // Step 3:  Open an input stream from the url.  //
            //----------------------------------------------//

            is = u.openStream();         // throws an IOException

            //-------------------------------------------------------------//
            // Step 4:                                                     //
            //-------------------------------------------------------------//
            // Convert the InputStream to a buffered DataInputStream.      //
            // Buffering the stream makes the reading faster; the          //
            // readLine() method of the DataInputStream makes the reading  //
            // easier.                                                     //
            //-------------------------------------------------------------//

            dis = new DataInputStream(new BufferedInputStream(is));

            //------------------------------------------------------------//
            // Step 5:                                                    //
            //------------------------------------------------------------//
            // Now just read each record of the input stream, and print   //
            // it out.  Note that it's assumed that this problem is run   //
            // from a command-line, not from an application or applet.    //
            //------------------------------------------------------------//

            while ((s = dis.readLine()) != null) {
                content.append(html2text(s));
                //System.out.println(html2text(s));
            }



        } catch (MalformedURLException mue) {

            System.out.println("Ouch - a MalformedURLException happened.");
            mue.printStackTrace();


        } catch (IOException ioe) {

            System.out.println("Oops- an IOException happened.");
            ioe.printStackTrace();


        } finally {

            //---------------------------------//
            // Step 6:  Close the InputStream  //
            //---------------------------------//

            try {
                Optional<InputStream> input = Optional.ofNullable(is);
                if(input.isPresent()) {
                    is.close();
                }
            } catch (IOException ioe) {
                // just going to ignore this one
            }

        } // end of 'finally' clause
        return content;
    }  // end of main

    /**
     * This method remove all the html from text.
     * @param html
     * @return
     */
    private static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    public static void main(String[] args) {
        UrlContentDownLoader contentDownLoader = new UrlContentDownLoader();
        StringBuilder content = contentDownLoader.contentDownloader("https://en.wikipedia.org/wiki/Afghanistan");
        System.out.println(content);
    }

} // end of class definition
