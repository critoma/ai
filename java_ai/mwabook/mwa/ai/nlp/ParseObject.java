// Conceptual Dependency Natural Language Processing Java classes
//
// Copyright 1996, Mark Watson.  All rights reserved.

package mwa.ai.nlp;

import mwa.data.*;

public class ParseObject extends mwa.data.AIframe {

 static public int instanceCount=0;
   ParseObject() {
    super("CDframe" + instanceCount++);
    put("actor", new AIframedata("?"));
    put("action", new AIframedata("?"));
    put("recipient", new AIframedata("?"));
    put("location", new AIframedata("?"));
    put("object", new AIframedata("?"));
    put("time", new AIframedata("?"));
    put("tense", new AIframedata("?"));
    ReverseVerb=0; // assume actor before verb in sentence
  }
  int fitness() {
    int ret_value=0;
    if (goodValue("actor")) ret_value++;
    if (goodValue("action")) ret_value++;
    if (goodValue("location")) ret_value++;
    if (goodValue("object")) ret_value++;
    if (goodValue("time")) ret_value++;
    return ret_value;
  }

  boolean goodValue(String key) {
    AIframedata fdata = get(key);
    if (fdata.type==AIframedata.STRING) {
        if (fdata.string != "?") {
            return true;
        }
    }
    return false;
  }
  public int ReverseVerb;
}

