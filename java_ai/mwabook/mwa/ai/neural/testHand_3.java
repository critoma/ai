// Neural Net Java class for testing GenNeural class
//
// Copyright 1996, Mark Watson.  All rights reserved.


package mwa.ai.neural;

import java.awt.*;

import mwa.gui.GUI;
import mwa.data.*;

public class testHand_3 extends GUI {

    int width = 410;
    int height = 310;

    public String getAppletInfo() {
        return "Neural Network training data tool by Mark Watson";
    }

    GenNeural gen = null;

    public void init() {
       NoInput = true;
       NoGraphics=true;
       RunLabel   = new String("Training data");
       ResetLabel = new String("Structure/Save");
       super.init();
       gen = new GenNeural("hand_3.dat");
     }

    public void doRunButton() {
       P("Starting greedy algorithm for training data.\n");
       gen.RemoveExtraData();
       P("Starting genetic algorithm for training data.\n");
       gen.RemoveTrainingData();
       gen = null;
       P("Done optimizing training data\n");
    }

    public void doResetButton() {
       P("Starting genetic algorithm for structure.\n");
       // reload from the saved results of the "Run"
       // button:
       gen = new GenNeural("test_gen.trn");
       // we need to refresh the active training cases from
       // the NNfile object into the Neural object:
       gen.MyNeural.LoadTrainingCases();
       gen.ChangeStructure();
       P("Output file is 'hand_4.dat'\n");
       P("Retrain the network...\n");
       // we need to refresh the active training cases from
       // the NNfile object into the Neural object:
       gen.MyNeural.LoadTrainingCases();
       for (int i=0; i<5000; i++) {
         float error = gen.MyNeural.Train();
         if ((i % 10) == 0) {
            P("Output error for iteration " +
              i + " =" + error + "\n");
         }
         if (error < 0.2f)  break;  // done training
         if (i > 2000 && error < 0.5f) break;
         if (i > 3000 && error < 0.9f) break;
       }

       gen.MyNeural.Save("hand_4.dat");
       gen = null;
   }

}
