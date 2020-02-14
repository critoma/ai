// Conceptual Dependency Natural Language Processing Java classes
//
// Copyright 1996, Mark Watson.  All rights reserved.

package mwa.ai.nlp;

import java.util.*;
import java.io.*;

import mwa.gui.GUI;
import mwa.data.*;

public class Parser {

  // For debug output:
  GUI MyGUI = null;
  // Handle up to 16 sentences:
  static final int MAX_CDS=16;
  public ParseObject sentences[];
  public int num_sentences;

  String Words[] = null;
  int NumWords;

  public Parser() {
    sentences = new ParseObject[MAX_CDS];
    for (int i=0; i<MAX_CDS; i++) {
        sentences[i] = new ParseObject();
    }
    // senetnce count is incremented when a new
    // test CD form is created:
    num_sentences = 0;
    Words = new String[20];
  }

  public void Parse(String sentence) {

    try {
      GetWords(sentence);
    } catch (Exception E) {
        System.out.println("Can not process:" + sentence);
    }
    for (int i=0; i<NumWords; i++) {
        System.out.println("Word[" + i + "]=" + Words[i]);
        if (MyGUI!=null) {
            MyGUI.P("Word[" + i + "]=" + Words[i] + "\n");
        }
    }
    DoVerb();
    DoActor();
    DoObject();
    DoTime();

    num_sentences++;
  }
  protected void DoVerb() {
    for (int i=0; i<NumWords; i++) {
        if (Words[i].equals("give")) {
            sentences[num_sentences].put("tense",
                                 new AIframedata("present"));
            sentences[num_sentences].put("action",
                                 new AIframedata("atrans"));
            if (MyGUI!=null) {
              MyGUI.P("Action: " + "atrans\n");
            }
            if (i>0) {
                if (Words[i-1].equals("will")) {
                    sentences[num_sentences].put("tense",
                                 new AIframedata("future"));
                  if (MyGUI!=null) {
                     MyGUI.P("  (future tense)\n");
                  }
                }
            }
        }
        if (Words[i].equals("gave")) {
            sentences[num_sentences].put("tense",
                                  new AIframedata("past"));
            sentences[num_sentences].put("action",
                                  new AIframedata("atrans"));
            if (MyGUI!=null) {
              MyGUI.P("Action: " + "atrans (past tense)\n");
            }
        }
        if (Words[i].equals("received")) {
            sentences[num_sentences].put("tense",
                                  new AIframedata("past"));
            sentences[num_sentences].put("action",
                                  new AIframedata("atrans"));
            sentences[num_sentences].ReverseVerb=1;
            if (MyGUI!=null) {
              MyGUI.P("Action: " + "atrans (past tense) " +
                      "(verb reversed)\n");
            }
        }
        if (Words[i].equals("receive")) {
            sentences[num_sentences].put("tense",
                                  new AIframedata("present"));
            sentences[num_sentences].put("action",
                                  new AIframedata("atrans"));
            if (i>0) {
                if (Words[i-1].equals("will")) {
                    sentences[num_sentences].put("tense",
                                  new AIframedata("future"));
                }
            }
        }
    }
  }
  protected void DoActor() {
    // First fill in actor:
    if (sentences[num_sentences].ReverseVerb==0) {
      for (int i=0; i<NumWords; i++) {
        System.out.println("  processing " + Words[i]);
        if (ProperName(Words[i])) {
          AIframedata fd = 
             sentences[num_sentences].get("actor");
          if (fd.string.equals("?")) {
             sentences[num_sentences].put("actor",
                                  new AIframedata(Words[i]));
             if (MyGUI!=null) {
                MyGUI.P("Actor: " + Words[i] + "\n");
             }
             break;
          }
        }
      }
    }  else  {
      for (int i=NumWords-1; i>=0; i--) {
        System.out.println("  processing " + Words[i]);
        if (ProperName(Words[i])) {
          AIframedata fd = 
                sentences[num_sentences].get("actor");
          if (fd.string.equals("?")) {
             sentences[num_sentences].put("actor",
                                 new AIframedata(Words[i]));
             if (MyGUI!=null) {
                MyGUI.P("Actor: " + Words[i] + "\n");
             }
             break;
          }
        }
      }
    }
    // Then fill in recipient:
    if (sentences[num_sentences].ReverseVerb==1) {
      for (int i=0; i<NumWords; i++) {
        System.out.println("  processing " + Words[i]);
        if (ProperName(Words[i])) {
          AIframedata fd = 
                  sentences[num_sentences].get("recipient");
          if (fd.string.equals("?")) {
             sentences[num_sentences].put("recipient",
                                 new AIframedata(Words[i]));
             if (MyGUI!=null) {
                MyGUI.P("Recipient: " + Words[i] + "\n");
             }
             break;
          }
        }
      }
    }  else  {
      for (int i=NumWords-1; i>=0; i--) {
        System.out.println("  processing " + Words[i]);
        if (ProperName(Words[i])) {
          AIframedata fd = 
             sentences[num_sentences].get("recipient");
          if (fd.string.equals("?")) {
             sentences[num_sentences].put("recipient",
                                  new AIframedata(Words[i]));
             if (MyGUI!=null) {
                MyGUI.P("Recipient: " + Words[i] + "\n");
             }
             break;
          }
        }
      }
    }
  }
  static String objects[] = {"book", "ball"};
  static int num_objects=2;
  protected void DoObject() {
    for (int i=0; i<NumWords; i++) {
      if (InList(Words[i], objects, num_objects)) {
         AIframedata fd = 
             sentences[num_sentences].get("object");
         if (fd.string.equals("?")) {
             sentences[num_sentences].put("object",
                                 new AIframedata(Words[i]));
             if (MyGUI!=null) {
                MyGUI.P("Object: " + Words[i] + "\n");
             }
             break;
         }
      }
    }
  }
  protected void DoTime() {
    for (int i=0; i<NumWords; i++) {
      if (Words[i].equals("yesterday")) {
        AIframedata fd = 
            sentences[num_sentences].get("time");
         if (fd.string.equals("?")) {
             sentences[num_sentences].put("time",
                                new AIframedata(-24.0f));
             if (MyGUI!=null) {
                MyGUI.P("Time: -24.0\n");
             }
             break;
         }
      }
    }
  }

  protected boolean InList(String s, String lst[], int num) {
    for (int i=0; i<num; i++) {
        if (s.equals(lst[i])) return true;
    }
    return false;
  }

  protected void GetWords(String sentence)
            throws IOException, StreamFormatException {
    StringTokenizer st;
    st = new StringTokenizer(sentence);
    NumWords=0;
    // Fill in the Word array:
    process:
    while (st.hasMoreTokens()) {
      System.out.println("Top of while loop");
          Words[NumWords]=new String(st.nextToken());
          NumWords++;
    }
    System.out.println("Done with while loop");
    if (NumWords==0)
      throw new StreamFormatException(st.toString());
  }
  protected boolean ProperName(String name) {
    boolean ret = false;
    if (name.equals("Mark"))  ret = true;
    if (name.equals("Carol")) ret = true;
    if (ret) {
        if (MyGUI!=null) {
            MyGUI.P("   proper name: " + name + "\n");
        }
    }
    return ret;
  }

};

class StreamFormatException extends Exception {
  public StreamFormatException(String str) {
    super(str);
  }
}
