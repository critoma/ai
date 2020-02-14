// Test AIframe and AIframedata classes
//
// Copyright 1996, Mark Watson.  All rights reserved.

package mwa.data;

import java.awt.*;
import java.util.*;

import mwa.gui.*;


public class testAIframe extends GUI {
    Graphics background;  // used for double buffering
    Image im;

    public String getAppletInfo() {
        return "Test the Frame stuff.  By Mark Watson";
    }

    public void init() {
      // Disable graphics and input areas of
      // standard GUI display:
      NoGraphics = true;
      NoResetButton=true;
      NoInput = true;
      BigText = 3;

      super.init();
      P("testFrame applet\n");
    }

    public void doRunButton() {
        P("entered doRunButton\n");
        AIframe frame1 = new AIframe("car1");
        P("created frame1\n");
        frame1.put("number of wheels", new AIframedata(4));
        frame1.put("color", new AIframedata("red"));
        frame1.put("type", new AIframedata("stingray"));
        AIframe sub_frame = new AIframe("driver");
        frame1.put("test_sub_frame", 
                   new AIframedata("!!" + 
                                   sub_frame.getName()));
        frame1.put("z_num_drivers", new AIframedata(1));
        frame1.put("slot_a", new AIframedata("a_value"));
        AIframe sub_frame2 = new AIframe("driver2");
        frame1.put("test_sub_frame2",
                   new AIframedata("!!" +
                                   sub_frame2.getName()));
        frame1.put("slot_b", new AIframedata("b_value"));
        frame1.put("slot_c", new AIframedata("c_value"));
        AIframe frame2 = new AIframe("car2", frame1);
        frame2.put("color", new AIframedata("blue"));
        P("created frame2\n");
        AIframedata s1=frame2.get("color");
        P("Color of car2 is " + s1.string + "\n");
        // now that we have created a reference to the first
        // frame, change a slot value to demonstrate that
        // two AIframe variables can reference the same frame:
        AIframe frame1A = frame1;
        frame1.put("number of wheels", new AIframedata(3));
        AIframedata s2=frame2.get("number of wheels");
        P("# of wheels for car2=" + s2.number + "\n");
        s2 = frame1A.get("number of wheels");
        P("# of wheels for car2 (frame1A)=" +
          s2.number + "\n");
        P("Dumping frame1 to a string:\n");
        String s = frame1.toString();
        P("frame1.toString        =" + s + "\n");
        AIframe restored_frame = new AIframe(s);
        s2 = restored_frame.get("number of wheels");
        P("# of wheels on frame from string=" +
          s2.number + "\n");
        s = restored_frame.toString();
        P("restored_frame.toString=" + s + "\n");
        P("leaving doRunButon\n");
        frame1.PP();
        frame1.PP(this);

        P("\n\nStarting toString() test:\n\n");
        String test1 = frame1.toString();
        AIframe ftest1 = new AIframe(test1);
        String test2 = ftest1.toString();
        P(test1 + "\n");
        P(test2 + "\n");
    }
}

