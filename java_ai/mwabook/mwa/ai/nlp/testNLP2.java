// NLP Java classes: test program and new subclass of Parser
//
// Copyright 1996, Mark Watson.  All rights reserved.

package mwa.ai.nlp;

import mwa.gui.GUI;
import mwa.data.*;

public class testNLP2 extends GUI {

    // use subclass defined at end of this file
    testParser MyParser;

    public String getAppletInfo() {
        return "NLP demo by Mark Watson";
    }

    public void init() {
       NoGraphics=true;
       BigText=1;
       NoResetButton=true;
       super.init();

       MyParser = new testParser();
       MyParser.MyGUI = this;
       MyParser.MyGUI.SetInputText(
           "Mark gave Carol a book yesterday");
    }

    public void doRunButton() {
        MyParser.Parse(GetInputText());
    }
    public void doResetButton() {
        ClearOutput();
    }
}

//
//      Extend the base class mwa.ai.nlp.Parser to
//      add a few new verbs and object names:
//

class testParser extends Parser {
  protected void DoVerb() {
    super.DoVerb();  // FIRST CALL SUPER CLASS FUNCTION
    for (int i=0; i<NumWords; i++) {
        if (Words[i].equals("move") || 
            Words[i].equals("drive"))
        {
            sentences[num_sentences].put("tense",
                                 new AIframedata("present"));
            sentences[num_sentences].put("action",
                                 new AIframedata("ptrans"));
            if (MyGUI!=null) {
              MyGUI.P("Action: " + "ptrans\n");
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
        if (Words[i].equals("moved") ||
            Words[i].equals("drove")) 
        {
            sentences[num_sentences].put("tense",
                                  new AIframedata("past"));
            sentences[num_sentences].put("action",
                                  new AIframedata("ptrans"));
            if (MyGUI!=null) {
              MyGUI.P("Action: " + "ptrans (past tense)\n");
            }
        }
    }
  }

  static String objects[] = {"car", "money", "ball"};
  static int num_objects=3;
  protected void DoObject() {
    super.DoObject();  // FIRST CALL SUPER CLASS FUNCTION
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
}
