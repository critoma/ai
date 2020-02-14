// mwa.agent.Connection.java
// Copyright 1996 Mark Watson

package mwa.agent;

import java.io.*;
import java.net.*;
import mwa.gui.*;
import mwa.data.*;

public class Connection extends Thread {
    protected Socket my_socket;
    protected DataInputStream input_strm;
    protected PrintStream output_strm;
    protected Server MyServer;
    public GUI MyGUI=null;

    public Connection(Server server,
                      Socket client_socket,
                      GUI my_gui) {
      MyGUI = my_gui;
      MyServer = server;
      my_socket = client_socket;
      try {
        input_strm  = 
           new DataInputStream(my_socket.getInputStream());
        output_strm = 
           new PrintStream(my_socket.getOutputStream());
      }
      catch (IOException io_exception) {
        try { my_socket.close(); } catch (IOException io_ex2) 
        {  };
        System.err.println("Exception: getting socket streams " +
                           io_exception);
        if (MyGUI!=null)
            MyGUI.P("Exception: getting socket streams " +
                    io_exception);
        return;
      }
      this.start();
    }

    public void run() {
        String input_buf;
        try {
            while (true) {
                input_buf = input_strm.readLine();
                if (input_buf == null) break;
                // call the 'DoWork' function that should
                // be redefined in derived classes and return
                // string value to my_socket:
                if (MyGUI!=null) {
                    MyGUI.P("Connection::run(): request="
                            + input_buf + "\n");
                }
                // construct an AIframe from String:
                AIframe request = new AIframe(input_buf);
                AIframe frame = MyServer.DoWork(request);
                if (MyGUI!=null) {
                    MyGUI.P("Connection::run():" + 
                             " return from DoWork: "
                            + frame.toString() + "\n");
                }
                output_strm.println(frame.toString());
            }
        }
        catch (IOException exception) { }
        finally {
            try {
             my_socket.close();
            }
            catch (IOException exception) { };
        }
    }
}
