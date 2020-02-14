// Neural Network Java classes
//
// Copyright 1996, Mark Watson.  All rights reserved.

package mwa.ai.neural;

import java.awt.*;
import java.applet.Applet;
import java.lang.*;
import java.util.*;

import mwa.gui.GUI;
import mwa.data.*;

class Neural extends Object {

   // For debug output:
   GUI MyGUI = null;

   protected int NumInputs;
   protected int NumHidden;
   protected int NumOutputs;

   protected int NumTraining;
   protected int WeightsFlag;
   protected int SpecialFlag;

   public float Inputs[];
   protected float Hidden[];
   public float Outputs[];

   protected float W1[][];
   protected float W2[][];

   protected float output_errors[];
   protected float hidden_errors[];

   protected float InputTraining[];
   protected float OutputTraining[];

   // mask of training examples to ignore (true -> ignore):
   public boolean IgnoreTraining[] = null;
   // mask of Input neurons to ignore:
   public boolean IgnoreInput[] = null;

   Neural() {
    NumInputs = NumHidden = NumOutputs = 0;
   }
   public NNfile NeuralFile=null;

   Neural(String file_name) {
     NeuralFile = new NNfile(file_name);
     NumInputs = NeuralFile.NumInput;
     NumHidden = NeuralFile.NumHidden;
     NumOutputs = NeuralFile.NumOutput;
     NumTraining= NeuralFile.NumTraining;
     WeightsFlag= NeuralFile.WeightFlag;
     SpecialFlag= NeuralFile.SpecialFlag;

     Inputs = new float[NumInputs];
     Hidden = new float[NumHidden];
     Outputs = new float[NumOutputs];
     W1 = new float[NumInputs][NumHidden];
     W2 = new float[NumHidden][NumOutputs];
     // Retrieve the weight values from the NNfile object:
     if (WeightsFlag!=0) {
       for (int i=0; i<NumInputs; i++) {
          for (int h=0; h<NumHidden; h++) {
            W1[i][h] = NeuralFile.GetW1(i, h);
          }
       }
       for (int h=0; h<NumHidden; h++) {
          for (int o=0; o<NumOutputs; o++) {
            W2[h][o] = NeuralFile.GetW2(h, o);
          }
       }
     } else {
        randomizeWeights();
     }

     output_errors = new float[NumOutputs];
     hidden_errors = new float[NumHidden];

     // Get the training cases (if any) from the training file:
     LoadTrainingCases();

   }

   public void LoadTrainingCases() {
     NumTraining = NeuralFile.NumTraining;
     if (NumTraining > 0) {
        InputTraining  = new float[NumTraining * NumInputs];
        OutputTraining = new float[NumTraining * NumOutputs];
     }
     int ic=0, oc=0;

     for (int k=0; k<NumTraining; k++) {
        for (int i=0; i<NumInputs; i++)
            InputTraining[ic++] = NeuralFile.GetInput(k, i);
        for (int o=0; o<NumOutputs; o++)
            OutputTraining[oc++] = NeuralFile.GetOutput(k, o);
     }
   }

   Neural(int i, int h, int o) {
     System.out.println("In BackProp constructor");
     Inputs = new float[i];
     Hidden = new float[h];
     Outputs = new float[o];
     W1 = new float[i][h];
     W2 = new float[h][o];
     NumInputs = i;
     NumHidden = h;
     NumOutputs = o;
     output_errors = new float[NumOutputs];
     hidden_errors = new float[NumHidden];

     // Randomize weights here:
     randomizeWeights();
     
     printANNType();
     printANN("");
   }
   
   void printANNType() {
   	System.out.println("ANN with \n (i) inputs neurons = " + NumInputs + ", \n (h) hidden neurons = " + NumHidden 
     + ", \n (o) output = " + NumOutputs + ", \n the W1 weights are a matrix [i][h] and \n the W2 weights are a matrix[h][o]"
     + "\n output_errors[o] and hidden_errors[h]");
   }
   
   void printANN(String msg) {
   	System.out.println("\n ####### print ANN start ####### " + msg + "\n");
   	System.out.println("\n\n Input Canvas:");
   	for (int i = 0; i < 6; i++) {
   		System.out.println();
   		for (int j = 0; j < 5; j++)
   			if (Inputs[i*5+j] > 0)
   				System.out.print(" 11");
   			else
   				System.out.print(" 00");
   	}
   	
   	System.out.println("\n\n Input neurons:");
   	for (int i = 0; i < NumInputs; i++)
   		System.out.print(" " + Inputs[i]);
   		
   	System.out.println("\n\n W1 Weights neurons:");
   	for (int i = 0; i < NumInputs; i++) {
   		System.out.println();
   		for (int j = 0; j < NumHidden; j++)
   			if (W1[i][j] >= 0)
   				System.out.printf(" +%.3f", W1[i][j]);
   			else
   				System.out.printf(" %.3f", W1[i][j]);
   	}
   		
   	System.out.println("\n\n Hidden neurons:");
   	for (int i = 0; i < NumHidden; i++)
   		System.out.print(" " + Hidden[i]);
   		
   	System.out.println("\n\n Hidden errors:");
   	for (int i = 0; i < NumHidden; i++)
   		System.out.print(" " + hidden_errors[i]);
   		
   	System.out.println("\n\n W2 Weights neurons:");
   	for (int i = 0; i < NumHidden; i++) {
   		System.out.println();
   		for (int j = 0; j < NumOutputs; j++)
   			if (W2[i][j] >= 0)
   				System.out.printf(" +%.3f", W2[i][j]);
   			else
   				System.out.printf(" %.3f", W2[i][j]);
   	}
   		
   	System.out.println("\n\n Output neurons:");
   	for (int i = 0; i < NumOutputs; i++)
   		System.out.print(" " + Outputs[i]);
   		
   	System.out.println("\n\n Output errors:");
   	for (int i = 0; i < NumOutputs; i++)
   		System.out.print(" " + output_errors[i]);
   		
   	System.out.println("\n ####### print ANN stop ####### " + msg + "\n");
   }

   void Save(String output_file) {
     if (NeuralFile==null) {
        System.out.println("Error: no NeuralFile object in Neual::Save");
     } else {
        for (int i=0; i<NumInputs; i++) {
          for (int h=0; h<NumHidden; h++) {
            NeuralFile.SetW1(i, h, W1[i][h]);
          }
        }
        for (int h=0; h<NumHidden; h++) {
          for (int o=0; o<NumOutputs; o++) {
            NeuralFile.SetW2(h, o, W2[h][o]);
          }
        }
        NeuralFile.Save(output_file);
     }
   }

   public void randomizeWeights() {
    // Randomize weights here:
     for (int ii=0; ii<NumInputs; ii++)
        for (int hh=0; hh<NumHidden; hh++)
           W1[ii][hh] =
              0.1f * (float)Math.random() - 0.05f;
     for (int hh=0; hh<NumHidden; hh++)
        for (int oo=0; oo<NumOutputs; oo++)
           W2[hh][oo] =
              0.1f * (float)Math.random() - 0.05f;
   }

   public void ForwardPass() {
       int i, h, o;
       for (h=0; h<NumHidden; h++) {
         Hidden[h] = 0.0f;
       }
       for (i=0; i<NumInputs; i++) {
           for (h=0; h<NumHidden; h++) {
                Hidden[h] +=
                   Inputs[i] * W1[i][h];
           }
       }
       for (o=0; o<NumOutputs; o++)
         Outputs[o] = 0.0f;
       for (h=0; h<NumHidden; h++) {
           for (o=0; o<NumOutputs; o++) {
                Outputs[o] +=
                   Sigmoid(Hidden[h]) * W2[h][o];
           }
       }
       for (o=0; o<NumOutputs; o++)
         Outputs[o] = Sigmoid(Outputs[o]);
         
    	printANN(" ForwardPass() ");
  }

  public float Train() {
     return Train(InputTraining, OutputTraining, NumTraining);
  }

  public float Train(float ins[],
                     float outs[],
                     int num_cases) {
	System.out.println("\n\n ####### START TRAIN ####### \n");
    int i, h, o;
    int in_count=0, out_count=0;
    float error = 0.0f;
    for (int example=0; example<num_cases; example++) {
      if (IgnoreTraining != null)
         if (IgnoreTraining[example]) continue;  // skip this case
      // zero out error arrays:
      for (h=0; h<NumHidden; h++)
         hidden_errors[h] = 0.0f;
      for (o=0; o<NumOutputs; o++)
         output_errors[o] = 0.0f;
      // copy the input values:
      for (i=0; i<NumInputs; i++) {
         Inputs[i] = ins[in_count++];
      }

      if (IgnoreInput != null) {
        for (int ii=0; ii<NumInputs; ii++) {
           if (IgnoreInput[ii]) {
             for (int hh=0; hh<NumHidden; hh++) {
                W1[ii][hh] = 0;
             }
           }
        }
      }

      // perform a forward pass through the network:

      ForwardPass();

      if (MyGUI != null)  MyGUI.repaint();
      for (o=0; o<NumOutputs; o++)  {
          output_errors[o] =
            (outs[out_count++] -
             Outputs[o])
            *SigmoidP(Outputs[o]);
      }
      for (h=0; h<NumHidden; h++) {
        hidden_errors[h] = 0.0f;
        for (o=0; o<NumOutputs; o++) {
           hidden_errors[h] +=
               output_errors[o]*W2[h][o];
        }
      }
      for (h=0; h<NumHidden; h++) {
         hidden_errors[h] =
           hidden_errors[h]*SigmoidP(Hidden[h]);
      }
      // update the hidden to output weights:
      for (o=0; o<NumOutputs; o++) {
         for (h=0; h<NumHidden; h++) {
            W2[h][o] +=
               0.5 * output_errors[o] * Hidden[h];
         }
      }
      // update the input to hidden weights:
      for (h=0; h<NumHidden; h++) {
         for (i=0; i<NumInputs; i++) {
             W1[i][h] +=
                0.5 * hidden_errors[h] * Inputs[i];
         }
      }
      for (o=0; o<NumOutputs; o++)
          error += Math.abs(output_errors[o]);
    }
    System.out.println("\n\n ####### STOP TRAIN ####### \n");
    return error;
  }

  protected float Sigmoid(float x) {
    return
     (float)((1.0f/(1.0f+Math.exp((double)(-x))))-0.5f);
  }

  protected float SigmoidP(float x) {
    double z = Sigmoid(x) + 0.5f;
    return (float)(z * (1.0f - z));
  }

}
