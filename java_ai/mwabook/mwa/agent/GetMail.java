/**
 * Copyright (c)1996 Harm Verbeek, All Rights Reserved.
 *
 * Description: Retrieve E-mail (using POP) from within Java applet,
 *              won't work with applets in WWW-browser
 *              (security violation !!!).
 *
 * Usage Notes: Java 2.0 Beta
 *
 * Date       : 01/23/96
 *
 *
 * Permission to use, copy, modify, and distribute this software
 * and its documentation for NON-COMMERCIAL or COMMERCIAL purposes and
 * without fee is hereby granted.
 *
 * (API modifications by Mark Watson)
**/

package mwa.agent;

import java.io.*;
import java.net.*;
import java.lang.*;
import java.applet.*;


public class GetMail
{
  static Socket          socket;
  static InputStream     in;
  static OutputStream    out;
  static DataInputStream din;
  static PrintStream     prout;

  public StringBuffer Messages[];
  public int NumMessages;

  static int getNumberofMessagesfromString(String m)
  {
    int i,j;
    String s;

    i = m.indexOf(" mes");
    s = m.substring(4, i);

    i = s.lastIndexOf(" ");
    s = s.substring(i+1);

    i = Integer.parseInt(s);

    return i;
  }


  public GetMail(String mailhost, String my_user_name,
                 String my_password)
  {
    int             POPport  = 110;
    String          incoming = new String();

    String          MailHost = mailhost;
    String          user     = my_user_name;
    String          password = my_password;
    String          USER     = "USER " + user;
    String          PASSWORD = "PASS " + password;
    String          STAT     = "STAT";
    String          RETR     = "RETR ";
    String          DELE     = "DELE ";
    String          QUIT     = "QUIT";

    /* send this command to the server if you
       want to keep connected while you're busy
       doing something else
    */
    String          DONTQUIT = "NOOP";

    /* mailhost returns string that starts with
       "+OK" to indicate everything went OK.
    */
    String          OK       = "+OK";

    /* mailhost returns string that starts with
       "-ERR" to indicate something went wrong.
    */
    String          Err      = "-ERR";


/* connect to the mail server */
    System.out.println("Connecting to " + MailHost + "...");
    System.out.flush();

    try {
      socket = new Socket(MailHost, POPport);
    } catch (IOException e) {
      System.out.println("Error opening socket.");
      return;
    }

    try {
      in    = socket.getInputStream();
      din   = new DataInputStream(in);

      out   = socket.getOutputStream();
      prout = new PrintStream(out);
    }
    catch (IOException e) {
      System.out.println("Error opening inputstream.");
      return;
    }

 /* let server know who's calling... */
    prout.println(USER);
    prout.flush();
    System.out.println("Sent: " + USER);

    try {
      incoming = din.readLine();
    }
    catch (IOException e) {
      System.out.println("Error reading from socket.");
      return;
    }
    System.out.println("Received: " + incoming);

/* give server your password... */
    prout.println(PASSWORD);
    prout.flush();
    System.out.println("Sent: " + PASSWORD);

    try {
      incoming = din.readLine();
    }
    catch (IOException e) {
      System.out.println("Error reading from socket.");
      return;
    }
    System.out.println("Received: " + incoming);


/* ask server if there's mail for you... */
    prout.println(STAT);
    prout.flush();
    System.out.println("Sent: " + STAT);

    try {
      incoming = din.readLine();
    }
    catch (IOException e) {
      System.out.println("Error reading from socket.");
      return;
    }
    System.out.println("Received: " + incoming);

    int n = getNumberofMessagesfromString(incoming);

    NumMessages = n;

/* retrieve mail... */
    if (n>0) {

      Messages = new StringBuffer[NumMessages+1];

      for (int i=1; i<=n; i++) {
        Messages[i-1] = new StringBuffer();
        prout.println(RETR + i);
        prout.flush();
        System.out.println("Sent: " + RETR + i);

        try {
          incoming = din.readLine();

          while (!incoming.equals(".")) {
            System.out.println("Received: " + incoming);
            Messages[i-1].append(incoming);
            Messages[i-1].append("\n");
            incoming = din.readLine();
          }
        }
        catch (IOException e) {
          System.out.println("Error reading from socket.");
          return;
        }

        /* delete retrieved mail from server... */
        prout.println(DELE + i);
        prout.flush();
        System.out.println("Sent: " + DELE + i);

        try {
          incoming = din.readLine();
        }
        catch (IOException e) {
          System.out.println("Error reading from socket.");
          return;
        }
        System.out.println("Received: " + incoming);
      }
    }

/* ready, let's quit... */
    prout.println(QUIT);
    prout.flush();
    System.out.println("Sent: " + QUIT);

    try {
      incoming = din.readLine();
    }
    catch (IOException e) {
      System.out.println("Error reading from socket.");
      return;
    }
    System.out.println("Received: " + incoming);

/* we're done, disconnect from server */
   System.out.print("Disconnecting...");
    try {
      socket.close();
    }
    catch (IOException e) {
      System.out.println("Error closing socket.");
    }

    System.out.println("done.");
  }

}

