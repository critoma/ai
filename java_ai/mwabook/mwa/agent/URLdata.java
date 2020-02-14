// mwa.agent.URLdata
//
// Encapsulates the use of the java.net.URL class
//
// Copyright 1996, Mark Watson.
//

package mwa.agent;

import java.net.*;
import java.io.*;

public class URLdata {
    public URLdata() {
    }
    String fetch(String url) {
        URL a_url;
        try {
            a_url = new URL(url);
        }
        catch (MalformedURLException e) {
            System.out.println("Unable to fetch " +
                               url + "(MalformedURLException)");
            return new String("no document");
        }
        try {
            URLConnection uc = a_url.openConnection();
            StringBuffer sb = new StringBuffer();
            DataInputStream dis = new DataInputStream(uc.getInputStream());
            while (true) {
                String s = dis.readLine();
                if (s == null) break;
                sb.append(s + "\n");
            }
            return new String(sb);
        }
        catch (IOException e) {
            System.out.println("Unable to fetch " +
                               url + "(IOexception)");
            return new String("no document");
        }
     }
}

