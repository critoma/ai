// Test GetMail class
//
// Copyright 1996, Mark Watson.  No rights reserved.

package mwa.agent;

import mwa.gui.GUI;
import mwa.data.*;

public class testGetMail extends GUI {

    public String getAppletInfo() {
        return "Test the SendMail stuff.";
    }

    public void init() {
      // Disable graphics area of standard GUI display:
      NoGraphics = true;
      NoResetButton=true;
      BigText=1;

      super.init();
      P("testGetMail applet\n");
    }

    public void doRunButton() {
       String r =GetInputText();
       int space=r.indexOf(" ");
       String name=r.substring(0,space);
       String password=r.substring(space+1,r.length());
       P("name =     |" + name + "|\n");
       P("password = |" + "*******" + "|\n"); // do not print
       String mailhost="netcom.com";
       GetMail mailer = new GetMail(mailhost, name, password);
       P("Number of EMAIL messages = " + mailer.NumMessages + "\n");
       for (int i=0; i<mailer.NumMessages; i++)
          P("\n" + mailer.Messages[i] + "\n******\n");
       mailer=null;
    }
}

