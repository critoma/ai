// NeuralNet Java classes
//
// Copyright 1996, Mark Watson.  All rights reserved.

package mwa.ai.neural;

import java.awt.*;

import mwa.gui.GUI;
import mwa.data.*;

public class testNeuralFile extends GUI {

    Neural network;

    float Errors[];
    int NumErrors = 0;
    int width = 380;
    int height = 260;

    // For training:
    private float outs[];
    private float ins[];
    private float error = -9999.9f;

    public String getAppletInfo() {
        return "Neural Network Simulator by Mark Watson";
    }

    public void init() {

       NoInput = true;  // we do not need an input text field
       //ResetLabel = new String("Save file");

       Errors = new float[512];
       NumErrors = 0;

       super.init();

       network = new Neural("test.dat");
       network.MyGUI = this;
    }

    private void paintNeuronLayer(Graphics g, int x, int y,
                                  String title, float values[],
                                  int num) {
         for (int i=0; i<num; i++) {
              paintGridCell(g, x+60 + i*12, y, 10, values[i],
                            -0.5f, 0.5f);
         }
         g.drawString(title, x, y+10);
    }

    private void paintWeights(Graphics g, int x, int y, String title,
                              float values[][], int num1, int num2) {
         for (int i=0; i<num1; i++) {
              for (int j=0; j<num2; j++)  {
                    paintGridCell(g, x+60 + i*12, y + j * 12, 10,
                                  values[i][j], -1.5f, 1.5f);
              }
         }
         g.drawString(title, x, y+10);
    }

    public void paintToDoubleBuffer(Graphics g) {
        // Draw the input/hidden/output neuron layers:
        paintNeuronLayer(g, 170, 10, "Inputs:",
                         network.Inputs, network.NumInputs);
        paintNeuronLayer(g, 170, 80, "Hidden:",
                         network.Hidden, network.NumHidden);
        paintNeuronLayer(g, 170, 140, "Outputs:",
                         network.Outputs, network.NumOutputs);
        paintWeights(g, 170, 30, "Weights 1:",
                     network.W1, network.NumInputs,
                     network.NumHidden);
        paintWeights(g, 170, 100, "Weights 2:", network.W2,
                              network.NumHidden, network.NumOutputs);
        // Draw a plot of error summed over output neurons:
        g.drawString("Cumulative error summed over output neurons",
                              10, height - 10);
        if (NumErrors < 2)  return;
        int x1= 0, x2;
        int y1 = height - (int)(100.0f * Errors[0]), y2;
        setForeground(Color.red);
        g.setColor(getForeground());
        for (int i=1; i<NumErrors-1; i++) {
             x2 = 2*i;
             y2 = height - (int)(100.0f * Errors[i]);
             g.drawLine(x1, y1, x2, y2);
             x1 = x2;
             y1 = y2;
        }
    }


    public void doRunButton() {
        for (int i=0; i<30; i++) {
           Errors[NumErrors++] = error = network.Train();
           P("Output error=" + error + "\n");
           repaint();
        }
    }

    public void doResetButton() {
        ClearOutput();
        P("Weights saved to file test2.dat\n");
        network.NeuralFile.AddSpecial(1001.0f);
        network.NeuralFile.AddSpecial(1002.0f);
        network.NeuralFile.AddSpecial(1003.0f);
        network.NeuralFile.AddSpecial(1004.0f);
        network.NeuralFile.RemoveTraining(2);
        network.NeuralFile.AddSpecial(1005.0f);
        network.NeuralFile.AddSpecial(1006.0f);
        network.Save("test2.dat");
    }
}

