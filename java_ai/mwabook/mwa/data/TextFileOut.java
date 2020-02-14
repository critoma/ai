// TextFileOut.java

package mwa.data;

import java.io.*;
import java.net.URL;
import java.util.*;

public class TextFileOut {
  public TextFileOut() {
    System.out.println("TextFileOut constructor requires a file name");
  }
  PrintStream ps = null;
  FileOutputStream os = null;
  public TextFileOut(String output_file) {
    try {
      os = new FileOutputStream(output_file);
    } catch (Exception E) {
        System.out.println("can not open file output stream" + output_file);
    }
    try {
      ps = new PrintStream(os);
    } catch (Exception E) {
        System.out.println("can not open output file " + output_file);
    }
  }
  protected void finalize() {
    try {
        os.close();
        ps.close();
    } catch (Exception E) { }
    os=null;
    ps=null;
  }
  protected void close() {
    try {
        os.close();
        ps.close();
    } catch (Exception E) { }
    os=null;
    ps=null;
  }
  public void writeFloat(float data) {
    ps.print(data + " ");
  }
  public void writeComment(String s) {
    ps.println(s);
  }
}


