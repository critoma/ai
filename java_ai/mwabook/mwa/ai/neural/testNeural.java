// NeuralNet Java classes
//
// Copyright 1996, Mark Watson.  All rights reserved.

package mwa.ai.neural;

import java.awt.*;
import java.util.*;

import mwa.gui.*;

public class testNeural extends GUI implements Runnable {

    int active = 0;  // if 1, then train network

    // Neural network parameters:
    int NumInputs   = 3;
    int NumOutputs = 3;
    int NumHidden  = 3;

    Neural network;

    float Errors[];
    int NumErrors = 0;
    int width = 340;
    int height = 200;

    // For training:
    private float outs[];
    private float ins[];
    private float error = -9999.9f;
    private String param; // hides super class param.

    public String getAppletInfo() {
        return "Neural Network Simulator by Mark Watson";
    }

    private Thread workThread = null;

    public void init() {

       NoInput = true;  // we do not need an input text field

       param = getParameter("NumHidden");
       if (param != null) NumHidden = Integer.parseInt(param);

       param = getParameter("NumInputs");
       if (param != null) NumInputs = Integer.parseInt(param);

       param = getParameter("NumOutputs");
       if (param != null) NumOutputs = Integer.parseInt(param);

       network = new Neural(NumInputs, NumHidden, NumOutputs);
       network.MyGUI = this;

       // For testing the Ignore training data option:
       //network.Ignore = new boolean[100];
       //for (int i=0; i<100; i++) network.Ignore[i] = false;
       //network.Ignore[1] = true;

       Errors = new float[512];
       NumErrors = 0;
       outs = new float[20];
       ins   = new float[20];
       StringTokenizer st;
       param = getParameter("inputs");
       if (param != null)
          st = new StringTokenizer(param);
       else
          st = new StringTokenizer("-5 -5 5 5 -5 -5 -5 5 -5");
       int j;
       for(j=0;j<9;j++) {
             ins[j] = 0.08f *
                     (float)(Integer.parseInt(st.nextToken()));
       }
       param = getParameter("outputs");
       if (param != null)
          st = new StringTokenizer(param);
       else
          st = new StringTokenizer("5 -5 -5 -5 5 -5 -5 -5 5");
       for(j=0;j<9;j++) {
             outs[j] =  0.08f *
                        (float)(Integer.parseInt(st.nextToken()));
        }
        super.init();
        network.MyGUI = this;


       if (workThread==null) {
        workThread = new Thread(this);
        workThread.start();
       }
    }

    public void train() {
      if (NumErrors < 240) {
        Errors[NumErrors++] = error = network.Train(ins, outs, 3);
        P("Output error=");
        P(error);
        P("\n");
        repaint();
      } else {
        active = 0;
      }
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
        int y1 = height - (int)(50.0f * Errors[0]), y2;
        setForeground(Color.red);
        g.setColor(getForeground());
        for (int i=1; i<NumErrors-1; i++) {
             x2 = 2*i;
             y2 = height - (int)(50.0f * Errors[i]);
             g.drawLine(x1, y1, x2, y2);
             x1 = x2;
             y1 = y2;
        }
    }
    public void run() {
        System.out.println("Entering testNeural::run()");
        while (true)  {
            if (active == 1)  train();
	    try { Thread.sleep(10); } catch (Exception e) { }
        }
    }

    public void doRunButton() {
        System.out.println("Starting neural net example...");
	NumErrors = 0;
        active = 1;
     }
    public void doResetButton() {
	System.out.println("resetting neural net example...");
        active = 0;
        ClearOutput();
        network = new Neural(NumInputs, NumHidden, NumOutputs);
        network.MyGUI = this;
        NumErrors = 0;
    }
}

