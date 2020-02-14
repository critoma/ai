// NeuralNet Java classes: tester for handwriting recognition
//
// Copyright 1996, Mark Watson.  All rights reserved.

package mwa.ai.neural;

import java.awt.*;

import mwa.gui.GUI;

public class testHand_4 extends GUI implements Runnable {

    // define the size of two-dimensional neural
    // input array:
    final static int XSIZE=5;
    final static int YSIZE=6;

    int Inputs[][] = new int[XSIZE][YSIZE];



    // data for determining when a new character
    // is being drawn:
    long TimeLastMouse = -1;  // in milliseconds
    // MouseState: 0=>no capture, 1=>currently capturing
    int MouseState = 0;
    int MousePointIndex = 0; // at the start of capture data


    int num_cap=0;
    int cap_x[] = new int[20000];
    int cap_y[] = new int[20000];

    // Neural network:
    Neural network;

    public String getAppletInfo() {
        return "Neural Network Simulator by Mark Watson";
    }

    private Thread MouseThread = null;
    // Number of characters to learn:
    final static int NUM = 26;
    String Chars[] = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
                      "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
                      "u", "v", "w", "x", "y", "z"};

    public void init() {

       NoInput = true;  // we do not need an input text field
       BigText=1;
       NoRunButton=true;
       NoResetButton=true;

       network = new Neural("hand_4.dat");

       super.init();

       if (MouseThread==null) {
        MouseThread = new Thread(this);
        MouseThread.start();
       }
    }

    public void paintToDoubleBuffer(Graphics g) {
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
                  if (TimeLastMouse < mtime - 800) {
                      MouseState=0;
                      PutChar();
                  }
               }
            }  catch (Exception e) {} ;
            try {Thread.sleep(20);} catch (Exception ex) { };
        }
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

