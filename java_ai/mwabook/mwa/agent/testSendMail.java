// Test SendMail class
//
// Copyright 1996, Mark Watson.  No rights reserved.

package mwa.agent;

import mwa.gui.GUI;

public class testSendMail extends GUI {

    public String getAppletInfo() {
        return "Test the SendMail stuff.";
    }

    public void init() {
      // Disable graphics area of 
      // standard GUI display:
      NoGraphics = true;
      NoInput = true;
      NoResetButton=true;

      super.init();
      P("testSendMail applet\n");
    }

    public void doRunButton() {
       String mailhost="pacbell.net";
       String me="mwa@pacbell.net";
       String domain="pacbell.net";
       String to="mwa@netcom.com";
       String letter="This is\n test.\n";

       SendMail mailer;
       mailer = new SendMail(mailhost, domain, me, to, letter);
       mailer = null;

    }
}

