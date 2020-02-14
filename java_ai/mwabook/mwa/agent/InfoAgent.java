// InfoAgent class
//
// Copyright 1996, Mark Watson.

package mwa.agent;

import java.awt.*;
import java.applet.Applet;
import java.lang.*;
import java.util.*;
import java.net.*;
import java.io.*;

import mwa.gui.GUI;

public class InfoAgent extends GUI {

    public String getAppletInfo() {
        return "InfoAgent class test application. By Mark Watson";
    }

    private int Depth;
    private int URLstack_index;
    private String URLstack[];
    static final int MAX_STACK=500;

    TextField KeyWordText;
    TextField NotKeyWordText;

    String EmailAddress = null;
    String EmailHost = null;
    String EmailPassword = null;

    // Add a toggle button for selecting either ASCII
    // of HTML saved document format:
    private Button toggleButton;
    final static private int ASCII = 1;
    final static private int HTML  = 2;
    private int textType;


    public void init() {
      // Disable graphics and input areas of standard GUI display:
      NoGraphics = true;
      NoInput=true;
      RunLabel   = new String("DoQuery on cached files");
      ResetLabel = new String("Access Internet to cache files");

      String param = getParameter("depth");
      if (param==null) Depth=2;
      else Depth = Integer.parseInt(param);

      BigText=3;

      super.init();

      // Add two extra input fields for search parameters:
      Panel panel2 = new Panel();
      panel2.setLayout(new FlowLayout());

      KeyWordText = new TextField("", 24);
      panel2.add(new Label("Containing"));
      panel2.add(KeyWordText);
      NotKeyWordText = new TextField("", 14);
      panel2.add(new Label("Not containing:"));
      panel2.add(NotKeyWordText);

      toggleButton = new Button("ASCII");
      textType = ASCII;
      panel2.add(toggleButton);

      add(panel2);

      P("Starting the InfoAgent applet\n");
      P("Search depth=" + Depth + "\n");

      URLstack = new String[MAX_STACK];

      // Start to fill the URL stack with web page addresses
      // specified as applet parameters, like:
      //    page_1=http://www.nec.com
      //    page_9=http://www.apple.com

      for (int i=0; i<MAX_STACK; i++) {
         String temp = new String("page_" + i);
         param = getParameter(temp);
         if (param!=null) {
            URLstack[URLstack_index++] = new String(param);
            P("Adding resource URL " + param + "\n");
         }
      }

      // Get the optional EMAIL address and password:
      EmailAddress   = getParameter("email");
      EmailPassword = getParameter("passwd");
      EmailHost     = getParameter("host");
    }

    // extend the action method defined in
    // class mwa.gui.GUI:

    public boolean action(Event e, Object o) {
       if (e.target == toggleButton) {
          if (textType==ASCII) {
             textType = HTML;
             toggleButton.setLabel("HTML");
             P("Now saving documents in HTML\n");
             return true;
          } else {
             textType = ASCII;
             toggleButton.setLabel("ASCII");
             P("Now saving documents in ASCII\n");
             return true;
          }
       }
       return super.action(e, o);
    }

    public void doResetButton() {
       // Fetch EMAIL messages:
       if (EmailAddress!=null &&
           EmailPassword!=null &&
           EmailHost!=null)
       {
          GetMail mailer = new GetMail(EmailHost,
                                       EmailAddress,
                                       EmailPassword);
          P("Number of EMAIL messages = " + mailer.NumMessages + "\n");
          for (int i=0; i<mailer.NumMessages; i++) {
             // Save to a file:
             String s = new String(mailer.Messages[i]);
             HTMLdata hd = new HTMLdata(s);
             // Save this document to the ../Data directory:
             String out_file =
                new String("." + File.separator + "Data" +
                           File.separator + "file_" + i);
             if (textType==ASCII) {
                hd.SaveAsText(out_file);
             } else {
                hd.Save(out_file);
             }
          }
          mailer=null;
       }

       // process all documents on theURLstack:
       int num = URLstack_index;
       int start = 0;
       for (int d=0; d<Depth; d++) {
          for (int i=start; i<num; i++) {
            if (URLstack[i].endsWith("/") ||
                URLstack[i].endsWith("html") ||
                URLstack[i].endsWith("HTML") ||
                URLstack[i].endsWith("HTM") ||
                URLstack[i].endsWith("htm"))
            {
              URLdata ud = new URLdata();
              // fetch the input file as text and
              // treat as a URL address:
              String s = ud.fetch(URLstack[i]);
              HTMLdata hd = new HTMLdata(s);
              // Save this document to the ../Data directory:
              String out_file =
                new String("." + File.separator + "Data" +
                           File.separator + "file_" + i);
              hd.SaveAsText(out_file);
              hd.fetchURLs();
              for (int j=0; j<hd.NumURLs; j++) {
                 URLstack[URLstack_index++] = new String(hd.URLs[j]);
                 P("Adding to URLstack[" + (URLstack_index - 1) + "]: "
                    + hd.URLs[j] + "\n");
              }
              hd=null;
            }
          }
          start = num;
          num=URLstack_index;
       }
    }

    // Calculate the relevance of a file's data:
    private int relevance(String data,
                          String keywords[],int num_key,
                          String not_keywords[], int num_not_key)
    {
        int start;
        int len = data.length();
        int count=0;
        for (int i=0; i<num_key; i++) {
           start = 0;
           while (start<(len-2)) {
              start = data.indexOf(keywords[i],start);
              if (start==-1)  break;
              start++;
              count++;
           }
        }
        for (int i=0; i<num_not_key; i++) {
           start = 0;
           while (start<(len-2)) {
              start = data.indexOf(not_keywords[i], start);
              if (start==-1)  break;
              start++;
              count--;
           }
        }
        return count;
    }

    public void doRunButton() {
        // get a list of strings for the keywords
        // and the not-keywords:
        String keywords[] = new String[10];
        String not_keywords[] = new String[10];
        int num_key=0, num_not_key=0;
        StringTokenizer sk =
          new StringTokenizer(KeyWordText.getText());
        while (sk.hasMoreTokens() && num_key<19) {
            keywords[num_key++] = new String(sk.nextToken());
            P("keyword:" + keywords[num_key-1] + "\n");
        }
        sk = new StringTokenizer(NotKeyWordText.getText());
        while (sk.hasMoreTokens() && num_not_key<19) {
            not_keywords[num_not_key++] = new String(sk.nextToken());
            P("not_keyword:" + not_keywords[num_not_key-1] + "\n");
        }
        sk=null;
        int fitness[] = new int[MAX_STACK];
        for (int i=0; i<MAX_STACK; i++) {
            fitness[i] = -9999;
            String input_file =
                new String("." + File.separator + "Data" +
                           File.separator + "file_" + i);
            showStatus("Processing file" + input_file + "....");
            HTMLdata hdata = new HTMLdata();
            if (hdata==null) continue;
            String data = hdata.Load(input_file);
            if (data==null) continue;
            if (data.compareTo("no document") != 0) {
                fitness[i] = relevance(data,
                                       keywords, num_key,
                                       not_keywords, num_not_key);
                P("fitness[" + i + "]=" + fitness[i] + "\n");
                showStatus("Processing file" + input_file +
                           " fitness=" + fitness[i]);
            }
        }
        // Find the best few documents:
        int used=0;
        int used_flag[] = new int[10];
        int best_fitness = 1;
        showStatus("Searching for best documents...");
        while (best_fitness>0 && used < 10) {
            best_fitness=-9999;
            int index=-1;
            int ok=1;
            for (int i=0; i<MAX_STACK; i++) {
                // see if index i has been used:
                for (int j=0; j<used; j++)
                    if (i == used_flag[j])  ok=0;
                if (ok == 1 && fitness[i] > best_fitness) {
                    best_fitness=fitness[i];
                    index=i;
                }
            }
            if (index>=0) {
               String input_file =
                   new String("." + File.separator + "Data" +
                              File.separator + "file_" + index);
               HTMLdata hdata = new HTMLdata();
               if (hdata!=null) {
                  String data = hdata.Load(input_file);
                  P("\nFile: " + input_file + " has relevance " +
                    best_fitness + "\n");
                  P(data + "\n\n- - - - - - -\n\n");
                  used_flag[used++] = index;
               }
            }
        }
    }
}

