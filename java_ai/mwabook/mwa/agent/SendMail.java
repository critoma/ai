/**
 * Copyright (c)1996 Harm Verbeek, All Rights Reserved.
 *
 * Description: Send E-mail (using SMTP) from within Java applet,
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


public class SendMail
{
  static Socket          socket;
  static InputStream     in;
  static OutputStream    out;
  static DataInputStream din;
  static PrintStream     prout;


  public SendMail(String mailhost, String domain, String my_address,
                  String to_address, String data)
  {
    int             SMTPport =  25;
    String          incoming = new String();

    String          MailHost = mailhost;

    String          HELO     = "HELO " + domain;
    String          MAILFROM = "MAIL FROM:<" + my_address + ">";
    String          RCPTTO   = "RCPT TO:<" + to_address + ">";
    String          DATA     = "DATA";

    /* you must terminate your message string with
       "\r\n.\r\n" to indicate end of message.
    */
    String          Msg      = data + "\r\n.\r\n";

    /* mailhost returns either "220" or "250"
       to indicate everything went OK
    */
    String          OKCmd    = "220|250";


/* connect to the mail server */
    System.out.println("Connecting to " + MailHost + "...");
    System.out.flush();

    try {
      socket = new Socket(MailHost, SMTPport);
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

 /* OK, we're connected, let's be friendly and say hello to the mail server... */
    prout.println(HELO);
    prout.flush();
    System.out.println("Sent: " + HELO);

    try {
      incoming = din.readLine();
    }
    catch (IOException e) {
      System.out.println("Error reading from socket.");
      return;
    }
    System.out.println("Received: " + incoming);

 /* let server know YOU wanna send mail... */
    prout.println(MAILFROM);
    prout.flush();
    System.out.println("Sent: " + MAILFROM);

    try {
      incoming = din.readLine();
    }
    catch (IOException e) {
      System.out.println("Error reading from socket.");
      return;
    }
    System.out.println("Received: " + incoming);

/* let server know WHOM you're gonna send mail to... */
    prout.println(RCPTTO);
    prout.flush();
    System.out.println("Sent: " + RCPTTO);

    try {
      incoming = din.readLine();
    }
    catch (IOException e) {
      System.out.println("Error reading from socket.");
      return;
    }
    System.out.println("Received: " + incoming);


/* let server know you're now gonna send the message contents... */
    prout.println(DATA);
    prout.flush();
    System.out.println("Sent: " + DATA);

    try {
      incoming = din.readLine();
    }
    catch (IOException e) {
      System.out.println("Error reading from socket.");
      return;
    }
    System.out.println("Received: " + incoming);

/* finally, send the message... */
    prout.println(Msg);
    prout.flush();
    System.out.println("Sent: " + Msg);

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

