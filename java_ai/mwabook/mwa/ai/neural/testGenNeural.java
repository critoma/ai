// Neural Net Java class for testing GenNeural class
//
// Copyright 1996, Mark Watson.  All rights reserved.

package mwa.ai.neural;

import java.awt.*;

import mwa.gui.GUI;
import mwa.data.*;

public class testGenNeural extends GUI {

    int width = 410;
    int height = 310;

    public String getAppletInfo() {
        return "Neural Network training data tool by Mark Watson";
    }

    GenNeural gen = null;

    public void init() {
       NoInput = true;
       NoGraphics=true;
       RunLabel   = new String("Training");
       ResetLabel = new String("Structure");
       super.init();
     }

    public void doRunButton() {
       P("Starting genetic algorithm for training data.\n");
       gen = new GenNeural("test10.dat");
       gen.RemoveTrainingData();
       gen = null;
       P("Output file is 'test_gen.trn'\n");
       gen.Save("test_gen.trn");
    }

    public void doResetButton() {
       P("Starting genetic algorithm for structure.\n");
       gen = new GenNeural("test10.dat");
       gen.ChangeStructure();
       P("Output file is 'test_gen.str'\n");
       gen.Save("test_gen.str");
       gen = null;
    }

}

