// mwa.agent.HTMLdata
//
// Utility for working with HTML documents
//
// Copyright 1996, Mark Watson.
//

package mwa.agent;

import java.awt.*;
import java.applet.Applet;
import java.lang.*;
import java.util.*;
import java.net.*;
import java.io.*;

public class HTMLdata {
    public String data;
    static final int MAX_TAGS=100;
    public int NumURLs;
    public String URLs[];
    public HTMLdata(String html_data) {
        data=html_data;
        NumURLs=0;
        URLs = new String[MAX_TAGS];
    }
    public HTMLdata() {
        data=null;
        NumURLs=0;
        URLs = new String[MAX_TAGS];
    }
    public void fetchURLs() {
        // calculate an array of strings of all URLs contained
        // int the HTML document and place in the public
        // String array URLs. The public int NumURLs is
        // set to the number of URLs found in the document:
        NumURLs=0;
        int currentIndex=0;
        int len = data.length();
        while (currentIndex < (len-1)) {
            int index;
            index = data.indexOf("<a href=\"", currentIndex);
            if (index==-1)
               index = data.indexOf("<A HREF=\"", currentIndex);
            if (index==-1)
               index = data.indexOf("<A href=\"", currentIndex);
            if (index==-1)
               index = data.indexOf("<a HREF=\"", currentIndex);
            if (index==-1)  break;  // out of the while loop
            currentIndex=index + 8;
            if (currentIndex > (len-2))  break;
            index=data.indexOf("\"", currentIndex+1);
            if (index==-1) break;  // out of the while loop
            if (NumURLs < MAX_TAGS-1) {
               URLs[NumURLs++] =
                  new String(data.substring(currentIndex+1, index));
            }
            currentIndex=index+1;
        }
    }
    public void Save(String output_file) {
        File ofile = new File(output_file);
        FileOutputStream ostrm;
        try {
            ostrm = new FileOutputStream(ofile);
        } catch (IOException e) {
            System.out.println("Could not open " + output_file);
            return;
        }
        int len = data.length();
        for (int i=0; i<len; i++) {
            try {
                ostrm.write(data.charAt(i));
            } catch (IOException e) {
                System.out.println("Could not write to " + output_file);
                break;
            }
        }
        try { ostrm.close(); }
            catch (IOException e) {
            }
    }
    public String Load(String input_file) {
        File ifile = new File(input_file);
        FileInputStream istrm;
        data = null;
        try {
            istrm = new FileInputStream(ifile);
        } catch (IOException e) {
            System.out.println("Could not open " + input_file);
            return data;
        }
        byte bdata[] = new byte[32000];
        try {
           if (istrm.available() > 20) {
              int count = istrm.read(bdata, 0, 32000);
              if (count > -1) data = new String(bdata,0,0,count);
           }
        } catch (IOException e) {
           System.out.println("Could not read from " + input_file);
        }
        try {
            istrm.close();
        } catch (IOException e) {
        }
        return data;
    }
    public void SaveAsText(String output_file) {
        String data2 = toString();
        File ofile = new File(output_file);
        FileOutputStream ostrm;
        try {
            ostrm = new FileOutputStream(ofile);
        } catch (IOException e) {
            System.out.println("Could not open " + output_file);
            return;
        }
        int len = data2.length();
        for (int i=0; i<len; i++) {
            try {
                ostrm.write(data2.charAt(i));
            } catch (IOException e) {
                System.out.println("Could not write to " + output_file);
                break;
            }
        }
        try { ostrm.close(); }
            catch (IOException e) {
            }
    }
    public String toString() {
        // strip out all tags and return the
        // remaining text:
        StringBuffer sb = new StringBuffer(data.length()+1);
        int in_tag=0;
        int num=0;
        int len=data.length();
        for (int i=0; i<len; i++) {
            if (data.charAt(i) == '<')  in_tag++;
            if (in_tag==0)  sb.append(data.charAt(i));
            if (data.charAt(i) == '>')  in_tag--;
            if (in_tag < 0)  in_tag=0;
        }
        return new String(sb);
    }
}

