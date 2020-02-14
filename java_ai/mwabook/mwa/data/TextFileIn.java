// TextFileIn.java

package mwa.data;

import java.io.*;
import java.net.URL;
import java.util.*;

public class TextFileIn {
  public TextFileIn() {
    System.out.println("TextFileIn constructor requires a file name");
  }
  FileInputStream is = null;
  StreamTokenizer st = null;
  boolean EOFflag = false;
  public TextFileIn(String input_file) {
    try {
      is = new FileInputStream(input_file);
      st = new StreamTokenizer(is);
      st.commentChar('#');
      st.eolIsSignificant(false);
      st.parseNumbers();
    } catch (Exception E) {
        System.out.println("can not open file " + input_file);
    }
  }
  protected void finalize() {
    try {
        is.close();
    } catch (Exception E) { }
    is=null;
    st=null;
  }
  public float readFloat() {
    if (EOFflag) return 0.0f;  // no more data
    try {
      if (is != null) {
        while (true) {
           switch(st.nextToken()) {
             case StreamTokenizer.TT_EOL:
               EOFflag=true;
               return 0.0f;
             case StreamTokenizer.TT_NUMBER:
               return (float)st.nval;
             default:
               // try reading another number..
             break;
           }
        }
      }
    }  catch (Exception E) {
        EOFflag=true;
        return 0.0f;
    }
    return 0.0f;
  }
}


