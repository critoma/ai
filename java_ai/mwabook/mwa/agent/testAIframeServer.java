// Test server classes
//
// Copyright 1996, Mark Watson.  All rights reserved.

package mwa.agent;

import java.awt.*;
import java.util.*;

import mwa.gui.*;


public class testAIframeServer extends GUI {

    AIframeServer server;

    public String getAppletInfo() {
        return "Test the agent stuff.  By Mark Watson";
    }

    public void init() {
      // Disable all standard GUI display components except output:
      NoGraphics    = true;
      NoInput       = true;
      NoRunButton   = true;
      NoResetButton = true;
      BigText=1;

      super.init();
      P("testServer applet\n");

      server = new AIframeServer(0, this);  // use default port
    }

}
