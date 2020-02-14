// mwa.agent.Server.java
// Copyright 1996 Mark Watson

package mwa.agent;

import java.io.*;
import java.net.*;
import mwa.gui.*;
import mwa.data.*;

abstract public class Server extends Thread {
    // Reference to a mwa.gui.GUI application object:
    GUI MyGUI = null;
    // input socket for listening for new clients:
    protected ServerSocket input_socket;
    // a default port number:
    public static final int PORT = 6001;
    protected int port;

    public void Error(Exception exception, String message) {
        System.err.println(message + " " +  exception);
    }
    public Server() {
        //System.out.println("Server()");
        //Server_helper(0, null);
    }
    public Server(int port) {
        System.out.println("Server(port)");
        Server_helper(port, null);
    }
    public Server(int port, GUI my_gui) {
        System.out.println("Server(port, my_gui)");
        Server_helper(port, my_gui);
    }
    protected void Server_helper(int port, GUI my_gui) {
        if (port == 0) port = PORT;
        if (my_gui!=null) MyGUI = my_gui;
        if (MyGUI!=null) MyGUI.P("in Server_helper()\n");
        this.port = port;
        try { input_socket = new ServerSocket(port); }
        catch (IOException exception) {
          Error(exception, "Error creating server socket");
        }
        if (MyGUI != null)
          MyGUI.P("Server is listening on port " + port + "\n");
        this.start();
    }
    public void run() {
        try {
            while(true) {
                Socket client_socket = input_socket.accept();
                if (MyGUI!=null) MyGUI.P("Opening socket...\n");
                Connection c =
                    new Connection(this, client_socket, MyGUI);
                if (c==null) {
                   System.out.println("Bad new Connection");
                }
            }
        }
        catch (IOException e) {
            Error(e, "Error wating for connections");
        }
    }

    // public method to be redefined in derived classes:
    public synchronized AIframe DoWork(AIframe request) {
      AIframe ret =new AIframe("Stub DoWork");
      ret.put("client_req", new AIframedata(request.toString()));
      return ret;
    }
}

