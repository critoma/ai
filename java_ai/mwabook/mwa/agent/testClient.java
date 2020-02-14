// Test client classes
//
// Copyright 1996, Mark Watson.  All rights reserved.

package mwa.agent;

import java.awt.*;
import java.util.*;

import mwa.gui.GUI;
import mwa.data.*;

public class testClient extends GUI {
    mwa.agent.Client client;

    public String getAppletInfo() {
        return "Test the agent stuff.  By Mark Watson";
    }

    public void init() {
      // Disable graphics and input areas of
      // standard GUI display:
      NoGraphics = true;
      NoResetButton=true;
      BigText=1;

      super.init();
      P("testClient applet\n");
    }

    public void doRunButton() {
       client = new Client();
       String r = client.GetInfo();
       P(r + "\n");
       AIframe frame=new AIframe("testframe");
       frame.put("name", new AIframedata(GetInputText()));
       AIframe response = client.GetService(frame);
       if (response!=null) {
          String res = response.toString();
          P("Response from server:" + res + "\n");
       }  else  {
          P("No response from server\n");
       }
       client.CloseConnection();
       client = null;
    }
    public void doResetButton() {
    }
}

