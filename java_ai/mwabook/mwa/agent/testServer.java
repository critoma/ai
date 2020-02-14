// Test server classes
//
// Copyright 1996, Mark Watson.  All rights reserved.

package mwa.agent;

import java.awt.*;
import java.util.*;

import mwa.gui.GUI;
import mwa.data.*;

public class testServer extends GUI {
    MyServer server;
    Graphics background;  // used for double buffering
    Image im;


    public String getAppletInfo() {
        return "Test the agent stuff.  By Mark Watson";
    }

    public void init() {
      // Disable all standard GUI display
      // components except output:
      NoGraphics    = true;
      NoInput       = true;
      NoRunButton   = true;
      NoResetButton = true;

      super.init();
      P("testServer applet");

      server = new MyServer(0, this);  // use default port
    }

}

class MyServer extends Server {
    // Derived class constructors just need to call
    // the appropraite super class constructor:
    public MyServer(int port) { super(port); }
    public MyServer(int port, GUI my_gui) {
        super(port, my_gui);
    }
    // public method redefined from parent class:
    public AIframe DoWork(AIframe request) {
      AIframe ret =new AIframe("My DoWork");
      ret.put("client_request", 
              new AIframedata(request.toString()));
      return ret;
    }
}
