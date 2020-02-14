// GUI Java classes
//
// Copyright 1996, Mark Watson.  All rights reserved.

package mwa.gui;

import java.awt.*;

public class testGUI extends GUI  {

  public void init() {
    NoInput = true;
    super.init();
  }
  public void paintToDoubleBuffer(Graphics g) {
    System.out.println("entered testGUI::paintToDoubleBuffer\n");
    paintGridCell(g, 20, 20, 110, 0.5f, 0.0f, 1.0f);
  }
  public void doRunButton() {
    P("OVERRIDDEN doRunButton()\n");
  }
}
