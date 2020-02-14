// testHand_2.java
//
// NeuralNet Java classes: tester for handwriting recognition
//
// Copyright 1996, Mark Watson.  All rights reserved.
// www.markwatson.com
//


package mwa.ai.neural;

import java.awt.*;

import mwa.gui.GUI;

public class testHand_2 extends GUI implements Runnable {

    // define the size of two-dimensional neural
    // input array:
    final static int XSIZE=5;
    final static int YSIZE=6;

    // define the Mode: 0 for training mode,
    // 1 for testing mode:
    public int Mode = 0;

    // Number of characters of each type to use
    // for training data:
    final static int NUM_EX=2;  // number of examples

    // Number of characters to learn:
    final static int NUM = 26;
    String Chars[] = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
                      "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
                      "u", "v", "w", "x", "y", "z"};
    // Data for partitioning GUI diplay for capturing
    // individual charaters:
    int X_Pos[] = {20, 60, 100, 140};
    int Y_Pos = 90;
    int Inputs[][] = new int[XSIZE][YSIZE];

    // Count[NUM] is used for counting drawn chars
    int Count[] = {0, 0, 0, 0};

    // data for determining when a new character
    // is being drawn:
    long TimeLastMouse = -1;  // in milliseconds
    // MouseState: 0=>no capture, 1=>currently capturing
    int MouseState = 0;
    int MousePointIndex = 0; // at the start of capture data


    int num_cap=0;
    int cap_x[] = new int[50000];
    int cap_y[] = new int[50000];

    int active = 0;  // if 1, then train network

    // Neural network:
    Neural network;

    public String getAppletInfo() {
        return "Neural Network Simulator by Mark Watson";
    }

    private Thread MouseThread = null;

    public void init() {

       Count    = new int[NUM];
       for (int i=0; i<NUM; i++) Count[i] = 0;

       NoInput = true;  // we do not need an input text field
       BigText=1;

       network = new Neural("hand_1.dat");
       //network.MyGUI = this;

       RunLabel = "Train";
       ResetLabel = "Test";

       X_Pos = new int[27];
       for (int i=0; i<27; i++) X_Pos[i] = 20 + 22 * i;

       super.init();

       if (MouseThread==null) {
        MouseThread = new Thread(this);
        MouseThread.start();
       }
    }

    public void train() {
      P("Saving training data to file hand_2.dat\n");
      network.Save("hand_2.dat");
      // we need to refresh the active training cases from
      // the NNfile object into the Neural object:
      network.LoadTrainingCases();
      P("Starting to train network..wait..\n");

      for (int i=0; i<20000; i++) {
        float error = network.Train();
        if ((i % 10) == 0) {
           P("Output error for iteration " +
             i + " =" + error + "\n");
        }
        if (error < 0.2f)  break;  // done training
        if (i > 5000 && error < 0.5f) break;
        if (i > 10000 && error < 0.9f) break;
      }
      // Save the training cases and the new weights:
      network.Save("hand_3.dat");
    }

    public void paintToDoubleBuffer(Graphics g) {
        g.drawString("Captured handwriting data",
                              X_Pos[0], Y_Pos - 15);

        for (int m=0; m<NUM; m++) {
            g.drawString(Chars[m], X_Pos[m], Y_Pos);
        }
        setForeground(Color.black);
        g.setColor(getForeground());
        for (int i=0; i<num_cap; i++) {
             g.drawLine(cap_x[i],
                        cap_y[i],
                        cap_x[i],
                        cap_y[i]+1);
        }
    }
    public void run() {
        P("in testHand::run()\n");
        while (true)  {
            try {
               if (MouseState==1) {
                  long mtime =
                    java.lang.System.currentTimeMillis();
                  if (TimeLastMouse < mtime - 1500) {
                      MouseState=0;
                      PutChar();
                  }
               }
            }  catch (Exception e) {} ;
            try {Thread.sleep(20);} catch (Exception ex) { };
        }
    }

    public void doRunButton() {
        train();
     }
    public void doResetButton() {
        P("Resetting to test mode.\n");
        Mode = 1;  // switch to test mode
    }

    public void PutChar() {
        int x_min=9999, x_max=-9999;
        int y_min=9999, y_max=-9999;
        for (int i=MousePointIndex; i<num_cap; i++) {
            if (cap_x[i] < x_min) x_min = cap_x[i];
            if (cap_x[i] > x_max) x_max = cap_x[i];
            if (cap_y[i] < y_min) y_min = cap_y[i];
            if (cap_y[i] > y_max) y_max = cap_y[i];
        }
        if (x_min+1 > x_max)  { x_min--; x_max++; }
        P("X,Y char bounds: " + x_min + ", " + x_max +
          ", " + y_min + ", " + y_max + "\n");

        // Special case:Mode==1 for testing:
        if (Mode==1) {
            int ic = 0;
            for (int x=0; x<XSIZE; x++) {
                for (int y=0; y<YSIZE; y++) {
                    network.Inputs[ic++] = -0.4f;
                }
            }
            for (int i=MousePointIndex; i<num_cap; i++) {
                float xx = (float)(cap_x[i] - x_min)
                          / (float)(x_max - x_min);
                xx *= XSIZE;
                float yy = (float)(cap_y[i] - y_min)
                          / (float)(y_max - y_min);
                yy *= YSIZE;
                int ix=(int)xx;
                int iy=(int)yy;
                if (ix<0) ix=0;
                if (ix>=XSIZE) ix=XSIZE-1;
                if (iy<0) iy=0;
                if (iy>=YSIZE) iy=YSIZE-1;
                network.Inputs[ix*YSIZE+iy] = +0.4f;
            }
            // Propagate input neuron values through
            // to the hidden, then output neuron layer:
            network.ForwardPass();
            // Find the largest output neuron value:
            int index=0;
            float maxVal=-99f;
            for (int i=0; i<NUM; i++) {
                if (network.Outputs[i]>maxVal) {
                   maxVal = network.Outputs[i];
                   index = i;
                }
            }
            P("\nCharacter recognized: " + Chars[index] + "\n");
            return;
        }

        // Capture training data: find which character is
        // drawn by checking the range of the x coordinates:
        int char_type = -1;
        for (int i=0; i<NUM; i++) {
            if (x_min - 5 < X_Pos[i] &&
                x_max + 5 > X_Pos[i] &&
                x_max + 5 < X_Pos[i + 1]) {
                    char_type = i;
                }
        }
        if (char_type==-1) {
           P("Error: character is not drawn in correct position\n");
           MousePointIndex = num_cap;
           return;
        }
        P("Character " + Chars[char_type] + " drawn. # "
          + Count[char_type] + "\n");
        if (Count[char_type] > (NUM_EX-1)) {
           P("Too many examples for this char type: ignoring!\n");
           MousePointIndex = num_cap;
           return;
        }
        for (int x=0; x<XSIZE; x++) {
            for (int y=0; y<YSIZE; y++) {
                Inputs[x][y] = 0;
            }
        }
        for (int i=MousePointIndex; i<num_cap; i++) {
            float xx = (float)(cap_x[i] - x_min)
                      / (float)(x_max - x_min);
            xx *= XSIZE;
            float yy = (float)(cap_y[i] - y_min)
                      / (float)(y_max - y_min);
            yy *= YSIZE;
            int ix=(int)xx;
            int iy=(int)yy;
            if (ix<0) ix=0;
            if (ix>=XSIZE) ix=XSIZE-1;
            if (iy<0) iy=0;
            if (iy>=YSIZE) iy=YSIZE-1;
            Inputs[ix][iy] = 1;
        }
        float inputs[] = new float[XSIZE*YSIZE];
        float outputs[]= new float[26];

        int c = 0;
        for (int x=0; x<XSIZE; x++) {
            for (int y=0; y<YSIZE; y++) {
              if (Inputs[x][y] == 0) {
                inputs[c++] = -0.4f;
              }  else  {
                inputs[c++] = +0.4f;
              }
            }
        }
        for (int i=0; i<26; i++) outputs[i] = -0.4f;
        outputs[char_type] = +0.4f;
        network.NeuralFile.AddTraining(inputs, outputs);

        MousePointIndex = num_cap;
        Count[char_type] += 1;
    }

    public void doMouseDown(int x, int y) {
        long mtime = java.lang.System.currentTimeMillis();
        if (MouseState==0) { // not yet in capture mode
            P("switch to capture mode\n");
            MouseState=1;
            MousePointIndex = num_cap;
        }

        TimeLastMouse = mtime;

        //System.out.println("Mouse x: " + x + ", y: " + y);
        if (num_cap<19999) {
            cap_x[num_cap] = x;
            cap_y[num_cap] = y;
            num_cap++;
            repaint();
        }
    }
}

