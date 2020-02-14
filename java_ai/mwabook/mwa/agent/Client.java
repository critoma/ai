// mwa.agent.Client
// Copyright 1996 Mark Watson

package mwa.agent;

import java.io.*;
import java.net.*;
import mwa.data.*;

public class Client {
    // define a default host name that is the name
    // of the machine that this program is running on:
    public static final String HOST = "127.0.0.1";
    // define a default port number:
    public static final int PORT = 6001;

    Socket socket;
    DataInputStream input_strm;
    PrintStream output_strm;

    public Client(int port, String host)  {
        Client_Helper(port, host);
    }

    public Client()  {
        Client_Helper(PORT, HOST);
    }

    public void Client_Helper(int port, String host)  {
        socket = null;
        try {
            // Create a socket:
            socket = new Socket(host, port);
            // Create socket based streams:
            input_strm = 
              new DataInputStream(socket.getInputStream());
            output_strm = 
              new PrintStream(socket.getOutputStream());
        }
        catch (IOException exception) {
            System.err.println(exception);
        }
    }
    public String GetInfo() {
      return  "Connected to " + socket.getInetAddress() +
              ":" + socket.getPort();
    }
    public AIframe GetService(AIframe query) {
      // Send the service request to the server:
      output_strm.println(query.toString());
      // Get the response from the server:
      String response="";
      try { response = input_strm.readLine(); }
      catch (IOException exception) {
         System.err.println("Client::GetService(): error reading "
                            + "response from server");
      };
      return new AIframe(response);
    }

    public void CloseConnection() {
      try { if (socket != null) { socket.close(); } }
          catch (IOException exception) { };
      socket=null;
    }
    public void finalize() {
       if (socket!=null)  CloseConnection();
    }
}
