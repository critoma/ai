// mwa.data.AIframedata
//
// This class holds the "slot" data for instances
// of the class AIframe.

package mwa.data;

public class AIframedata {
  final static public int STRING=1;
  final static public int NUMBER=2;
  final static public int AIFRAME=3;
  public String string;
  public float number;
  public String aiframe;
  public int type;
  public AIframedata(String s) {
    if (s.startsWith("!!")) {
      // 'name' contains a complete AIframe encoded
      // into a String:
      aiframe = s.substring(2);
      type=AIFRAME;
    }  else  {
      string=s;
      type=STRING;
    }
  }
  public AIframedata(int x) {
    number=x;
    type=NUMBER;
  }
  public AIframedata(float x) {
    number=x;
    type=NUMBER;
  }

  public String toString() {
    String sb;
    switch (type) {
      case 1: sb=new String("!S!"+string);  break;
      case 2: sb=new String("!N!"+number);  break;
      case 3: sb=new String("!F!"+aiframe); break;
      default: sb=""; break;
    }
    return sb;
  }
}

