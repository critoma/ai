// Test URLdata class
//
// Copyright 1996, Mark Watson.

package mwa.agent;

import mwa.gui.GUI;

public class testURLdata extends GUI {

    public String getAppletInfo() {
        return "Test the URLdata stuff.  By Mark Watson";
    }

    public void init() {
        // Disable graphics and input areas of standard GUI display:
        NoGraphics = true;
        NoResetButton=true;
        BigText=1;

        super.init();
        P("testURLdata applet\n");
        //SetInputText("http://www.markwatson.com");
        /**
         * NOTE: with new default security manager, we cn not
         * easily access remote web servers, so you must have
         * a local web server running:
         */
        SetInputText("http://127.0.0.1");
    }

    public void doRunButton() {
        URLdata ud = new URLdata();
        // fetch the input filed text and treat as a URL address:
        String s = ud.fetch(GetInputText());
        P(s + "\n");
    }
}
