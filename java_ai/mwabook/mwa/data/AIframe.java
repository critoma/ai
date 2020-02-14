// mwa.data.AIframe
// AIframe class. Copyright 1997 Mark Watson.

package mwa.data;

import java.io.*;
import java.net.*;
import java.util.*;
import mwa.gui.*;

public class AIframe {
  // support single inheritence with a single 'parent' pointer:
  protected AIframe Parent = null;
  public AIframe(String name, AIframe parent) {
    Helper(name, parent);
  }
  public AIframe(String name) {
    Helper(name, null);
  }
  protected void Helper(String name, AIframe parent) {
    if (name.startsWith("!!")) {
       // 'name' contains a complete AIframe
       // encoded into a String:
       String sb = new String(name);
       int index = 2;
       int next_bang = sb.indexOf('!',index);
       String frameName = sb.substring(2, next_bang);
       MyName = frameName;
       Table = new Hashtable(6, 0.99f);
       Parent = parent;
       index = next_bang+1;
       next_bang = sb.indexOf('!',index);
       String num_string = sb.substring(index, next_bang);
       int num_slots;
       if (num_string.length()<1) num_slots=0;
       else num_slots = (new Integer(num_string)).intValue();
       for (int i=0; i<num_slots; i++) {
         index = next_bang+1;
         next_bang = sb.indexOf('!',index);
         String slot_name = sb.substring(index, next_bang);
         // skip "!!" before AIframedata type:
         index = next_bang+2;
         AIframedata fd;
         int ch = sb.charAt(index);
         index += 2; // skip over "!!"
         next_bang = sb.indexOf('!',index);
         if (ch=='S') {
           fd = new AIframedata(sb.substring(index, next_bang));
         } else if (ch=='N') {
           Float f = new Float(sb.substring(index, next_bang));
           fd = new AIframedata(f.floatValue());
         } else { // has to be "F" == an AIframe object
           fd = new AIframedata("!!" + 
                                sb.substring(index, next_bang));
         }
         Table.put(slot_name, fd);
       }
     } else {
       // Normal constructor: 'name' is name of frame:
       MyName = name;
       Table = new Hashtable(6, 0.99f);
       Parent = parent;
     }
  }
  public synchronized AIframedata get(String key) {
  AIframedata s = (AIframedata)Table.get(key);
  if (s==null && Parent!=null)
    s = Parent.get(key);
    return s;
  }
  public synchronized void put(String key,
                               AIframedata value) {
    Table.put(key, value);
  }
  public synchronized void remove(String key) {
    Table.remove(key);
  }
  public String getName() { return MyName; }
  public String toString() {
    StringBuffer sb =
      new StringBuffer("!!" + MyName + "!"
                       + Table.size() + "!");
    for (Enumeration e = Table.keys() ;
         e.hasMoreElements() ;)
    {
      String s = e.nextElement().toString();
      sb.append(s).append("!");
      sb.append(Table.get(s)).append("!");
    }
    return new String(sb);
  }
  public void PP() {
    System.out.println("Data for AIrame: " + MyName);
    for (Enumeration e = Table.keys() ; e.hasMoreElements() ;) {
      String s = e.nextElement().toString();
      String sv = "" + Table.get(s);
      if (sv.endsWith("!")) {
        int index=sv.indexOf("!!!");
        int index2=sv.indexOf("!", index+4);
        String s2 = sv.substring(index+3, index2);
      } else {
        int index=sv.lastIndexOf("!");
        String s2 = sv.substring(index+1);
      }
    }
  }
  public void PP(GUI MyGUI) {
    MyGUI.P("Data for AIrame: " + MyName + "\n");
    for (Enumeration e = Table.keys();
         e.hasMoreElements() ;)
    {
      String s = e.nextElement().toString();
      String sv = "" + Table.get(s);
      if (sv.endsWith("!")) {
        int index=sv.indexOf("!!!");
        int index2=sv.indexOf("!", index+4);
        String s2 = sv.substring(index+3, index2);
        MyGUI.P("  Slot: " + s + ", Sub-frame name: " +
                s2 + "\n");
      } else {
        int index=sv.lastIndexOf("!");
        String s2 = sv.substring(index+1);
        MyGUI.P("  Slot: " + s + ", Value: " + s2 + "\n");
      }
    }
  }

  private String MyName;
  private Hashtable Table;

};

