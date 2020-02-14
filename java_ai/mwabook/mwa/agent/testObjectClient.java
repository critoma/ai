// Test client classes
//
// Copyright 1996, Mark Watson.  All rights reserved.

package mwa.agent;

import java.awt.*;
import java.util.*;

import mwa.gui.*;
import mwa.data.*;

// Note: we can use the mwa.agent.Client class to
// test the mwa.agent.AIframeServer class (no separate
// ObjectClient class is required).

public class testObjectClient extends GUI {

    public String getAppletInfo() {
        return "Test the agent stuff.  By Mark Watson";
    }

    public void init() {
      // Disable graphics and input areas of standard GUI display:
      NoGraphics = true;
      NoResetButton=true;
      BigText=1;

      super.init();
      P("testObjectClient applet\n");
    }

    public void doRunButton() {
       Client client = new Client();
       String r = client.GetInfo();
       P(r + "\n");

       // send a new frame to the AIframeServer:
       AIframe frame=new AIframe("testframe1");
       frame.put("service", new AIframedata("add"));
       frame.put("type", new AIframedata("test_type"));
       frame.put("data", new AIframedata(GetInputText()));
       AIframe response = client.GetService(frame);
       if (response!=null) {
          String res = response.toString();
          P("Response from server:" + res + "\n");
       }  else  {
          P("No response from server\n");
       }

       // send a second frame to the AIframeServer:
       frame=new AIframe("testframe2");
       frame.put("service", new AIframedata("add"));
       frame.put("type", new AIframedata("test_type"));
       frame.put("zipcode", new AIframedata(92071));
       response = client.GetService(frame);
       if (response!=null) {
          String res = response.toString();
          P("Response from server:" + res + "\n");
       }  else  {
          P("No response from server\n");
       }

       client.CloseConnection();
       client = null;
    }
}

