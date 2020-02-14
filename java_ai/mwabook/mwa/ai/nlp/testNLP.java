// Test NLP Java classes
//
// Copyright 1996, Mark Watson.  All rights reserved.

package mwa.ai.nlp;

import java.awt.*;

import mwa.gui.GUI;

public class testNLP extends GUI {

    mwa.ai.nlp.Parser MyParser;

    public String getAppletInfo() {
        return "NLP demo by Mark Watson";
    }

    public void init() {
       NoGraphics=true;
       BigText=1;
       NoResetButton=true;
       super.init();

       MyParser = new mwa.ai.nlp.Parser();
       MyParser.MyGUI = this;
       MyParser.MyGUI.SetInputText(
              "Mark gave Carol a book yesterday");
    }

    public void paintToDoubleBuffer(Graphics g) {
        g.drawString("NLP demo", 10, 10);
        setForeground(Color.red);
        g.setColor(getForeground());
        g.drawLine(10, 10, 100, 150);
    }

    public void doRunButton() {
        MyParser.Parse(GetInputText());
    }
    public void doResetButton() {
        ClearOutput();
    }
}

