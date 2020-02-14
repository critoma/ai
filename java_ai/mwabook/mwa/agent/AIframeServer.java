// mwa.agent.AIframeServer.java
// Copyright 1996 Mark Watson

package mwa.agent;

import java.io.*;
import java.net.*;
import mwa.gui.GUI;
import mwa.data.*;

// An AIframeServer instance adds the following services to the
// base class mwa.agent.Server:
//
//   1. return a list of all frame types
//   2. return a list of all names of a specified frame type
//   3. return a frame with a specified name
//   4. update the value of a frame with a specified name
//   5. add a new frame to object storage
//   6. print out all objects

public class AIframeServer extends Server {

    final static int MAXOBJS=200;
    public int NumObjects;
    public AIframe MyObjects[];
    public AIframeServer() {
        super(0, null);
        System.out.println("AIframeServer()");
        NumObjects=0;
        MyObjects=new AIframe[MAXOBJS];
    }
    public AIframeServer(int port) {
        super(port, null);
        System.out.println("AIframeServer(port)");
        NumObjects=0;
        MyObjects=new AIframe[MAXOBJS];
    }
    public AIframeServer(int port, GUI my_gui) {
        super(port, my_gui);
        System.out.println("AIframeServer(port, my_gui)");
        NumObjects=0;
        MyObjects=new AIframe[MAXOBJS];
    }
    public void run() {
        try {
            while(true) {
                Socket client_socket = input_socket.accept();
                if (MyGUI!=null) MyGUI.P("Opening socket...\n");
                Connection c =
                    new Connection(this, client_socket, MyGUI);
            }
        }
        catch (IOException e) {
            Error(e, "Error wating for connections");
        }
    }

    // public method defined in base class.  here we
    // redefine DoWork to provide 6 new AIframeServer services:
    public synchronized AIframe DoWork(AIframe request) {
      AIframe ret =new AIframe("return_values");
      AIframedata s1=request.get("service");
      if (s1==null) {
        System.out.println("No service slot in request frame");
        if (MyGUI!=null)
               MyGUI.P("No service slot in request frame\n");
        return null;
      }
      String service=s1.string;
      if (service.equals("get_types")) {
        int count=0;
        for (int i=0; i<NumObjects; i++) {
            AIframedata fd=MyObjects[i].get("type");
            if (fd!=null) {
                if (fd.type==AIframedata.STRING) {
                   ret.put("s"+count,
                           new AIframedata(fd.string));
                   count++;
                }
            }
        }
      }
      if (service.equals("get_names")) {
        AIframedata fdtype=request.get("type");
        if (fdtype!=null) {
            if (fdtype.type==AIframedata.STRING) {
              int count=0;
              for (int i=0; i<NumObjects; i++) {
                  AIframedata fd=MyObjects[i].get("type");
                  if (fd!=null) {
                      if (fd.type==AIframedata.STRING) {
                         if (fd.string.equals(fdtype.string)) {
                           ret.put("s"+count,
                                   new AIframedata(MyObjects[i].getName()));
                           count++;
                         }
                      }
                  }
              }
            }
        }
      }
      if (service.equals("fetch")) {
         AIframedata s2=request.get("name");
         if (s2==null) {
            System.out.println("No name slot in request frame");
            if (MyGUI!=null)
               MyGUI.P("No name slot in request frame\n");
            return null;
         }
         if (s2.type==AIframedata.STRING) {
            String name=s2.string;
            ret=null;
            for (int i=0; i<NumObjects; i++) {
               if (MyObjects[i].getName().equals(name)) {
                  ret=MyObjects[i];
               }
            }
         }
      }
      if (service.equals("update")) {
         AIframedata s2=request.get("name");
         if (s2==null) {
            System.out.println("No name slot in update request frame");
            if (MyGUI!=null)
               MyGUI.P("No name slot in update request frame\n");
            return null;
         }
         if (s2.type==AIframedata.STRING) {
            String name=s2.string;
            ret=null;
            for (int i=0; i<NumObjects; i++) {
               if (MyObjects[i].getName().equals(name)) {
                  request.remove("service");
                  MyObjects[NumObjects++]=request;
                  ret.put("status", new AIframedata("OK"));
               }
            }
         }
      }
      if (service.equals("add")) {
         if (NumObjects < (MAXOBJS-2)) {
            request.remove("service");
            MyObjects[NumObjects++]=request;
            ret.put("status", new AIframedata("OK"));
         }
      }
      if (service.equals("print")) {
         for (int i=0; i<NumObjects; i++) {
            if (MyGUI==null)
               MyObjects[i].PP();
            else
               MyObjects[i].PP(MyGUI);
         }
      }
      return ret;
    }
}
