// Neural Net Java class for testing greedy algorithm
//
// Copyright 1996, Mark Watson.  All rights reserved.

package mwa.ai.neural;

import java.awt.*;

import mwa.gui.GUI;
import mwa.data.*;

public class testGreedy extends GUI {

    int width = 410;
    int height = 310;

    public String getAppletInfo() {
        return "Neural Network training data tool by Mark Watson";
    }

    public void init() {
       NoResetButton = true;
       NoInput = true;
       NoGraphics=true;
       super.init();
    }

    public void doRunButton() {
        // Remove redundent or overlapping data and
        // try to find 5 areas in the input space that
        // do not contain any training cases:
        P("Input file is test3.dat\n");
        NNgreedy g = new NNgreedy("test3.dat");
        g.RemoveExtraData();
        for (int i=0; i<10; i++)
           g.FindEmptyRegion();
        g.Save("test.net");
        P("Output file is 'test.net'\n");
    }

}

