// NNgreedy class: parses neural network input files
// using the class NNfile and reports areas of input
// data space that contain no training examples and
// also uses a greedy algorithm to remove conflicting
// training examples

package mwa.ai.neural;

import java.applet.Applet;
import java.io.*;
import java.net.URL;
import java.util.*;


public class NNgreedy {

  public NNfile NeuralFile = null;

  public NNgreedy() {
     System.out.println("\n\nError: NNgreedy constructor " +
                        "requires a file name.\n");
  }

  public NNgreedy(String input_file) {
    NeuralFile = new NNfile(input_file);
  }

  public void Save(String file) {
    NeuralFile.Save(file);
  }

  void RemoveExtraData() {
      if (NeuralFile==null) return;
      for (int m=0; m<NeuralFile.NumTraining; m++) {
         for (int k=m+1; k<NeuralFile.NumTraining; k++) {
            float dist = 0.0f;
            for (int i=0; i<NeuralFile.NumInput; i++) {
               float x = NeuralFile.GetInput(m, i) -
                         NeuralFile.GetInput(k, i);
               dist += x*x;
            }
            if (dist < 0.005f) { // adjustable threshold
               // Remove training data item # k:
               NeuralFile.RemoveTraining(k);
               System.out.println("Deleted training case " + k);
               break;
            }
         }
      }
      System.out.println("Done deleting redundant training cases.");
  }

  float FindEmptyRegion() {
      int iter = NeuralFile.NumInput*NeuralFile.NumOutput*100;
      float max_dist = 0.000001f;
      float inputs[] = new float[NeuralFile.NumInput];
      float best_inputs[] = new float[NeuralFile.NumInput];
      for (int i=0; i<iter; i++) {
         for (int j=0; j<NeuralFile.NumInput; j++) {
            inputs[j] = (float)(Math.random() - 0.5f);
         }
         int best_index = 0;
         for (int m=0; m<NeuralFile.NumTraining; m++) {
            float x = 0;
            for (int ii=0; ii<NeuralFile.NumInput; ii++) {
               float z = inputs[ii] - NeuralFile.GetInput(m, ii);
               x += z*z;
            }
            if (x > max_dist) {
               max_dist = x;
               best_index = i;
               for (int k=0; k<NeuralFile.NumInput; k++) {
                   best_inputs[k]=inputs[k];
               }
            }
         }
      }
      float outputs[] = new float[NeuralFile.NumOutput];
      for (int o=0; o<NeuralFile.NumOutput; o++) outputs[o] = 99999.9f;
      NeuralFile.AddTraining(best_inputs, outputs);
      return max_dist;
  }

}

