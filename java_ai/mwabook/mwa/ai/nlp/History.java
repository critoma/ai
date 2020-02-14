// NLP Java classes: test program and new subclass of Parser
//
// Copyright 1996, Mark Watson.  All rights reserved.
// http://www.markwatson.com

package mwa.ai.nlp;

import java.awt.*;
import java.applet.Applet;
import java.lang.*;
import java.util.*;

import mwa.gui.GUI;
import mwa.data.*;
import mwa.agent.*;

public class History extends GUI {

    // use subclass defined at end of this file
    HistoryParser MyParser;

    static Client client = null;

    public String getAppletInfo() {
        return "NLP history of the world demo by Mark Watson";
    }

    public void init() {
       NoGraphics=true;
       BigText=3;
       RunLabel   = new String("Process input");
       ResetLabel = new String("Toggle debug on/off");

       client = new Client();

       super.init();

       MyParser = new HistoryParser();
       MyParser.MyGUI = this;
       MyParser.MyGUI.SetInputText("help");

       // Cache the current place names and places from the server:
       GetPlaces();
       GetPeople();
       GetEvents();
    }

    // The following utility is static so that the HistoryParser
    // class can use it:
    synchronized static public AIframe GetFrameFromServer(String frame_name) {
       // send a new frame to the AIframeServer:
       AIframe frame=new AIframe("test_query_frame");
       frame.put("service", new AIframedata("fetch"));
       frame.put("name", new AIframedata(frame_name));

       AIframe response = client.GetService(frame);
       // NOTE: response may be 'null' if frame not on server.
       frame=null;
       // NOTE: uncomment the following line for debug printout:
       //if (response!=null)  response.PP();
       return response;
    }

    synchronized public void doRunButton() {
       String input = GetInputText().trim();
       if (input.equals("help") || input.equals("Help")) {
          P("\nHelp information:  type one of:\n");
          P("help people\n");
          P("help places\n");
          P("help events\n");
          P("help examples\n");
          return;
       }

       if (input.equals("help people")) {
          P("\nPeople defined on remote AIframeServer:\n\n");
          for (int i=0; i<NumPeople; i++) {
            AIframe p = GetFrameFromServer(PeopleFrame[i]);
            if (p!=null) {
               p.PP(this);
            }
          }
          return;
       }

       if (input.equals("help places")) {
          P("\nPlaces defined on remote AIframeServer:\n\n");
          for (int i=0; i<NumPlaces; i++) {
            AIframe p = GetFrameFromServer(PlacesFrame[i]);
            if (p!=null) {
               p.PP(this);
            }
          }
          return;
       }

       if (input.equals("help events")) {
          P("\nEvents defined on remote AIframeServer:\n\n");
          for (int i=0; i<NumEvents; i++) {
            AIframe p = GetFrameFromServer(EventsFrame[i]);
            if (p!=null) {
               p.PP(this);
            }
          }
          return;
       }

       if (input.equals("help examples")) {
          P("\nExample NLP commands:\n\n");
          P("When was Buddha born?\n");
          P("Where did Caesar die?\n");
          P("What events occurred in Rome?\n");
          return;
       }

       MyParser.Parse(input);
       // Fetch out the semantic info from the last parse:
       AIframe cd_form = MyParser.sentences[MyParser.num_sentences-1];
       String actor = "";
       AIframedata fd = cd_form.get("actor");
       if (fd!=null) {
          if (fd.type==AIframedata.STRING) {
             if (fd.string.length() > 1) actor=fd.string;
          }
       }
       String action= "";
       fd = cd_form.get("action");
       if (fd!=null) {
          if (fd.type==AIframedata.STRING) {
             if (fd.string.length() > 1) action=fd.string;
          }
       }
       String object= "";
       fd = cd_form.get("object");
       if (fd!=null) {
          if (fd.type==AIframedata.STRING) {
             if (fd.string.length() > 1) object=fd.string;
          }
       }
       String recipient= "";
       fd = cd_form.get("recipient");
       if (fd!=null) {
          if (fd.type==AIframedata.STRING) {
             if (fd.string.length() > 1) recipient=fd.string;
          }
       }
       String place = "";
       fd = cd_form.get("place");
       if (fd!=null) {
          if (fd.type==AIframedata.STRING) {
             if (fd.string.length() > 1) place=fd.string;
          }
       }
       float time=0.0f;
       fd = cd_form.get("time");
       if (fd!=null) {
          if (fd.type==AIframedata.NUMBER) {
             time=fd.number;
          }
       }

       P("\n");
       if (!actor.equals("")) P("\nactor=" + actor + " ");
       if (!object.equals("")) P("object=" + object + " ");
       if (!recipient.equals("")) P("recipient=" + recipient + " ");
       if (!place.equals("")) P("place=" + place + " ");
       if (time != 0.0f) P("time=" + time);
       P("\n");

    }
    synchronized public void doResetButton() {
        ClearOutput();
    }
    // Utilities for getting information from
    // the remote object (frame) server:

    //    PLACES:

    static int NumPlaces=0;
    static String Places[]=null;
    static String PlacesFrame[]=null;
    static final int MAX_PLACES=200;

    synchronized private void GetPlaces() {
       if (Places==null) {
          P("Initializing " + MAX_PLACES + " Places....\n");
          Places = new String[MAX_PLACES];
          PlacesFrame = new String[MAX_PLACES];
          NumPlaces=0;
       }
       // Send request to AIframeServer:
       //Client client = new Client();
       String r = client.GetInfo();
       P(r + "\n");

       // send a new frame to the AIframeServer:
       AIframe frame=new AIframe("testframe1");
       frame.put("service", new AIframedata("get_names"));
       frame.put("type", new AIframedata("place"));
       AIframe response = client.GetService(frame);
       if (response!=null) {
          String res = response.toString();
          P("Response from server:" + res + "\n");
          for (int i=0; i<MAX_PLACES; i++) {
            String slot_name = new String("s" + i);
            AIframedata place_val = response.get(slot_name);
            if (place_val==null)  break; // we are done...
            // Still more places:
            if (place_val.type==AIframedata.STRING) {
               if (NumPlaces < (MAX_PLACES-1)) {
                 // Capture the frame name (although we
                 // may not save it):
                 PlacesFrame[NumPlaces]=place_val.string;
                 AIframe frame2 = new AIframe("fetch_2");
                 frame2.put("service", new AIframedata("fetch"));
                 frame2.put("name", new AIframedata(place_val.string));
                 AIframe response2 = client.GetService(frame2);
                 if (response2!=null) {
                    AIframedata place_name_slot = response2.get("name");
                    if (place_name_slot!=null) {
                      if (place_name_slot.type==AIframedata.STRING) {
                        Places[NumPlaces] =
                           new String(place_name_slot.string);
                        P("Place from server: " + Places[NumPlaces] + "\n");
                        NumPlaces++;
                      }
                    }
                 }
               }
            }
          }
       }  else  {
          P("No response from server\n");
       }
       //if (client!=null) client.CloseConnection();
    }


    //    PEOPLE:

    static public int NumPeople=0;
    static public String PeopleLast[]=null;
    static public String PeopleFirst[]=null;
    static public String PeopleFrame[]=null;
    static public final int MAX_PEOPLE=200;

    synchronized private void GetPeople() {
       if (PeopleLast==null) {
          P("Initializing " + MAX_PEOPLE + " People....\n");
          PeopleLast = new String[MAX_PEOPLE];
          PeopleFirst = new String[MAX_PEOPLE];
          PeopleFrame = new String[MAX_PEOPLE];
          NumPeople=0;
       }
       // Send request to AIframeServer:
       //Client client = new Client();
       String r = client.GetInfo();
       P(r + "\n");

       // send a new frame to the AIframeServer:
       AIframe frame=new AIframe("testframe1");
       frame.put("service", new AIframedata("get_names"));
       frame.put("type", new AIframedata("person"));
       AIframe response = client.GetService(frame);
       if (response!=null) {
          String res = response.toString();
          P("Response from server:" + res + "\n");
          for (int i=0; i<MAX_PEOPLE; i++) {
            String slot_name = new String("s" + i);
            AIframedata person_val = response.get(slot_name);
            if (person_val==null)  break; // we are done...
            // Still more places:
            if (person_val.type==AIframedata.STRING) {
               if (NumPeople < (MAX_PEOPLE-1)) {
                 // Capture the frame name (although we
                 // may not save it):
                 PeopleFrame[NumPeople]=person_val.string;
                 AIframe frame2 = new AIframe("fetch_2");
                 frame2.put("service", new AIframedata("fetch"));
                 frame2.put("name", new AIframedata(person_val.string));
                 AIframe response2 = client.GetService(frame2);
                 if (response2!=null) {
                    // last name:
                    AIframedata person_name_slot = response2.get("last");
                    if (person_name_slot!=null) {
                      if (person_name_slot.type==AIframedata.STRING) {
                        PeopleLast[NumPeople] =
                           new String(person_name_slot.string);
                        P("Last name from server: " + PeopleLast[NumPeople] + "\n");
                        NumPeople++;
                      }
                    } else {
                        break;  // we are done...
                    }
                    // first name:
                    person_name_slot = response2.get("first");
                    if (person_name_slot!=null) {
                      if (person_name_slot.type==AIframedata.STRING) {
                        // We have already incremented the counter 'NumPeople'
                        PeopleFirst[NumPeople-1] =
                           new String(person_name_slot.string);
                        P("First name from server: " + PeopleFirst[NumPeople] + "\n");
                      }
                    }
                 }
               }
            }
          }
       }  else  {
          P("No response from server\n");
       }
    }


    //    EVENTS:

    static public int NumEvents=0;
    static public String Events[]=null;
    static public String EventsFrame[]=null;
    static public final int MAX_EVENTS=200;

    synchronized private void GetEvents() {
       if (Events==null) {
          P("Initializing " + MAX_EVENTS + " Events....\n");
          Events = new String[MAX_EVENTS];
          EventsFrame = new String[MAX_EVENTS];
          NumEvents=0;
       }

       // send a request frame to the AIframeServer:
       AIframe frame=new AIframe("testframe1");
       frame.put("service", new AIframedata("get_names"));
       frame.put("type", new AIframedata("event"));
       AIframe response = client.GetService(frame);
       if (response!=null) {
          String res = response.toString();
          for (int i=0; i<MAX_EVENTS; i++) {
            String slot_name = new String("s" + i);
            AIframedata event_val = response.get(slot_name);
            if (event_val==null)  break;
            // Still more events:
            if (event_val.type==AIframedata.STRING) {
               if (NumEvents < (MAX_EVENTS-1)) {
                 // Capture the frame name (although we
                 // may not save it):
                 EventsFrame[NumEvents]=event_val.string;
                 AIframe frame2 = new AIframe("fetch_2");
                 frame2.put("service", new AIframedata("fetch"));
                 frame2.put("name", new AIframedata(event_val.string));
                 AIframe response2 = client.GetService(frame2);
                 if (response2!=null) {
                    AIframedata event_name_slot = response2.get("description");
                    if (event_name_slot!=null) {
                      if (event_name_slot.type==AIframedata.STRING) {
                        Events[NumEvents] =
                           new String(event_name_slot.string);
                        NumEvents++;
                      }
                    }
                 }
               }
            }
          }
       }  else  {
          P("No response from server\n");
       }
    }

    public void finalize() {
       if (client!=null)  client.CloseConnection();
    }

}

//
//      Extend the base class mwa.ai.nlp.Parser to
//      add a few new verbs and object names:
//

class HistoryParser extends Parser {

 final static int WHEN =1;
 final static int WHERE=2;
 final static int WHAT =3;
 final static int WHO  =4;
 int Wtype=0;

 private String synonyms[][] = {
     {"birth", "born"},
     {"die", "death", "died"}
 };
 private int num_synonyms=2;
 private int num_per_syn[] = {2, 3};

 private int synonymIndex(String s) {
    for (int i=0; i<num_synonyms; i++) {
       for (int j=0; j<num_per_syn[i]; j++) {
          if (synonyms[i][j].equals(s))     return i;
       }
    }
    return -1;
 }

 // Utilities for fuzzy matching:
 private int NumFuzzy = 0;
 private final int MAX_FUZZY = 20;
 private float FuzzyValues[] = new float[MAX_FUZZY];
 private AIframe FuzzyPersonFrames[] = new AIframe[MAX_FUZZY];
 private AIframe FuzzyEventFrames[]  = new AIframe[MAX_FUZZY];

 private void SortFuzzy() {
    for (int i=0; i<NumFuzzy; i++) {
       for (int j=i; j<NumFuzzy-1; j++) {
          if (FuzzyValues[j] < FuzzyValues[j+1]) {
             float temp = FuzzyValues[j];
             AIframe ftemp = FuzzyPersonFrames[j];
             FuzzyValues[j] = FuzzyValues[j+1];
             FuzzyValues[j+1] = temp;
             FuzzyPersonFrames[j] = FuzzyPersonFrames[j+1];
             FuzzyPersonFrames[j+1] = ftemp;
             ftemp = FuzzyEventFrames[j];
             FuzzyEventFrames[j] = FuzzyEventFrames[j+1];
             FuzzyEventFrames[j+1] = ftemp;
          }
       }
    }
 }

 private void AddFuzzy(AIframe person_frame,
                       AIframe event_frame,
                       float value) {
    int index = -1;
    for (int i=0; i<NumFuzzy; i++) {
       String name;
       if (person_frame==null) name = new String("");
       else                    name = person_frame.getName();
       String event = event_frame.getName();
       if (name.equals(FuzzyPersonFrames[i].getName()) &&
           event.equals(FuzzyEventFrames[i].getName())) {
          MyGUI.P("Fuzzy match for old frame at " + i +
                  ", value=" + value + "\n");
          FuzzyValues[i] += value;
          return;
       }
    }
    MyGUI.P("Fuzzy: adding frame at " + NumFuzzy +
            ", value=" + value + "\n");
    FuzzyPersonFrames[NumFuzzy] = person_frame;
    FuzzyEventFrames[NumFuzzy]  = event_frame;
    FuzzyValues[NumFuzzy] = value;
    if (NumFuzzy < (MAX_FUZZY - 1)) NumFuzzy++;
 }

 private int MaxFuzzy() {
    int max=-1;
    float val = -9999;
    for (int i=0; i<NumFuzzy; i++) {
       if (FuzzyValues[i] > val) {
          val = FuzzyValues[i];
          max = i;
       }
    }
    return max;
 }

 private boolean noiseWord(String s) {
    if (s.equals("a"))  return true;
    if (s.equals("an"))  return true;
    if (s.equals("the")) return true;
    if (s.equals("that")) return true;
    return false;
 }

 static private String goodness[] = {
        "Best", "Second best", "Third best", "Fourth best",
        "Fifth best", "Sixth best"};

 public void Parse(String sentence) {

    NumFuzzy = 0;
    Words = new String[20];
    try {
      GetWords(sentence);
    } catch (Exception E) {
        System.out.println("cannot process sentence:" + sentence);
    }
    DoWHwords();
    DoVerb();
    DoActor();
    DoObject();
    DoTime();

    // Special cases for the WH words:
    if (Wtype==WHO) {
        MyGUI.P("Processing WHO...\n");
        for (int i=0; i<NumWords; i++) {
            if (noiseWord(Words[i]))  continue;
            int s_index = synonymIndex(Words[i]);
            if (s_index==-1) {
              GetPersonFromEventDescription(Words[i]);
            }  else {
              // loop over all synonyms:
              for (int j=0; j<num_per_syn[s_index]; j++) {
                 GetPersonFromEventDescription(synonyms[s_index][j]);
              }
            }
        }
    }

    if (Wtype==WHAT) {
        MyGUI.P("Processing WHAT...\n");
        for (int i=0; i<NumWords; i++) {
            if (noiseWord(Words[i]))  continue;
            int s_index = synonymIndex(Words[i]);
            if (s_index==-1) {
              GetPersonFromEventDescription(Words[i]);
            }  else {
              // loop over all synonyms:
              for (int j=0; j<num_per_syn[s_index]; j++) {
                 GetPersonFromEventDescription(synonyms[s_index][j]);
              }
            }
        }
    }

    if (Wtype==WHERE) {
        MyGUI.P("Processing WHERE...\n");
        for (int i=0; i<NumWords; i++) {
            if (noiseWord(Words[i]))  continue;
            int s_index = synonymIndex(Words[i]);
            if (s_index==-1) {
              GetPersonFromEventDescription(Words[i]);
            }  else {
              // loop over all synonyms:
              for (int j=0; j<num_per_syn[s_index]; j++) {
                 GetPersonFromEventDescription(synonyms[s_index][j]);
              }
            }
        }
    }

    SortFuzzy();

    for (int i=0; i<NumFuzzy; i++) {
        if (FuzzyValues[i] < 0.3f) break;
        if (i<6) MyGUI.P("\n" + goodness[i] + " answer:\n");
        else     MyGUI.P("\nAnother answer:\n");
        if (Wtype==WHERE && FuzzyEventFrames[i]!=null) {
            AIframedata place = FuzzyEventFrames[i].get("place");
            if (place!=null) {
               if (place.type==AIframedata.AIFRAME) {
                  AIframe p = History.GetFrameFromServer(place.aiframe);
                  if (p!=null) {
                     MyGUI.P("Where:\n");
                     p.PP(MyGUI);
                  }
               }
            }
        }
        FuzzyPersonFrames[i].PP(MyGUI);
        if (Wtype==WHAT || Wtype==WHERE) {
           MyGUI.P("Did:\n");
           FuzzyEventFrames[i].PP(MyGUI);
        }
    }

    for (int i=0; i<20;i++)  Words[i]=null; // garbage collection

    num_sentences++;
  }
  private void DoWHwords() {
    Wtype=0;
    for (int i=0; i<NumWords; i++) {
       if (Words[i].equals("When") || Words[i].equals("when"))
          Wtype=WHEN;
       if (Words[i].equals("Where") || Words[i].equals("where"))
          Wtype=WHERE;
       if (Words[i].equals("What") || Words[i].equals("what"))
          Wtype=WHAT;
       if (Words[i].equals("Who") || Words[i].equals("who"))
          Wtype=WHO;
    }
  }

  protected void GetPersonFromEvent(AIframe event, float value) {
    AIframedata fd = event.get("person");
    boolean save_event = true;
    if (fd!=null) {
       if (fd.type==AIframedata.AIFRAME) {
          AIframe frm = History.GetFrameFromServer(fd.aiframe);
          if (frm != null) {
             AddFuzzy(frm, event, value);
             save_event = false;
          }
       }
    }
    if (save_event) {
       AddFuzzy(null, event, value);
    }
  }

  protected float FuzzyMatch(String ss1, String ss2) {
    if (ss1.length() < 3)  return 0.0f;
    String s1 = " " + ss1 + " ";
    String s2 = " " + ss2 + " ";
    if (ss2.indexOf(ss1) > -1) {
        if (s2.indexOf(s1) > -1) return 0.99f;
        else                     return 0.52f;
    }
    if (ss1.length() > 5) {
       String no_first_char = ss1.substring(1);
       if (ss2.indexOf(no_first_char) > -1)  return 0.7f;
       String no_last = s1.substring(0, s1.length()-1);
       if (ss2.indexOf(no_last) > -1) return 0.7f;
       String no_first_last = no_last.substring(1);
       if (ss2.indexOf(no_first_last) > -1) return 0.55f;
    }
    return 0.0f;
  }

  protected void GetPersonFromEventDescription(String description) {
    float match_value=0f;
    for (int i=0; i<History.NumEvents; i++) {
       match_value = FuzzyMatch(description, History.Events[i]);
       if (match_value > 0.5f) {
           AIframe event =
             History.GetFrameFromServer(History.EventsFrame[i]);
           if (event != null) {
              MyGUI.P("event frame match:\n");
              event.PP(MyGUI);
              GetPersonFromEvent(event, match_value);
           }
       }
    }
    return;
  }

  protected void DoVerb() {
    super.DoVerb();  // FIRST CALL SUPERCLASS FUNCTION
    for (int i=0; i<NumWords; i++) {
        if (Words[i].equals("move") || Words[i].equals("drive")) {
            sentences[num_sentences].put("tense",
                                 new AIframedata("present"));
            sentences[num_sentences].put("action",
                                 new AIframedata("ptrans"));
            if (MyGUI!=null) {
              MyGUI.P("Action: " + "ptrans\n");
            }
            if (i>0) {
                if (Words[i-1].equals("will")) {
                    sentences[num_sentences].put("tense",
                                         new AIframedata("future"));
                  if (MyGUI!=null) {
                     MyGUI.P("  (future tense)\n");
                  }
                }
            }
        }
        if (Words[i].equals("moved") || Words[i].equals("drove")) {
            sentences[num_sentences].put("tense", new AIframedata("past"));
            sentences[num_sentences].put("action", new AIframedata("ptrans"));
            if (MyGUI!=null) {
              MyGUI.P("Action: " + "ptrans (past tense)\n");
            }
        }
    }
  }

  protected boolean ProperName(String name) {
    if (super.ProperName(name)) return true;
    boolean ret = false;
    int num=History.NumPeople;
    for (int i=0; i<num; i++) {
        if (name.equals(History.PeopleLast[i])) {
           ret=true;
           if (MyGUI!=null) {
               MyGUI.P("  proper name: " + name);
               if (History.PeopleFirst[i]!=null)
                  MyGUI.P(", " + History.PeopleFirst[i]);
               MyGUI.P("\n");
           }
        }
    }
    return ret;
  }

  protected boolean Place(String name) {
    boolean ret = false;
    int num=History.NumPlaces;
    for (int i=0; i<num; i++) {
        if (name.equals(History.Places[i])) {
           ret=true;
           if (MyGUI!=null) {
               MyGUI.P("  place: " + name);
               if (History.Places[i]!=null)
                  MyGUI.P(", " + History.Places[i]);
               MyGUI.P("\n");
           }
        }
    }
    return ret;
  }

  protected boolean Event(String name) {
    if (name.length() < 4) return false; // arbitrary, but no short words.
    boolean ret = false;
    int num=History.NumEvents;
    for (int i=0; i<num; i++) {
        int pos = History.Events[i].indexOf(name);
        if (pos > -1) {
           ret=true;
           if (MyGUI!=null) {
               MyGUI.P("  (possible) event match: " + name +
                       ": " + History.Events[i] + "\n");
           }
        }
    }
    return ret;
  }

  static String objects[] = {"car", "money", "ball"};
  static int num_objects=3;
  protected void DoObject() {
    super.DoObject();  // FIRST CALL SUPERCLASS FUNCTION
    for (int i=0; i<NumWords; i++) {
      if (InList(Words[i], objects, num_objects)) {
         AIframedata fd = sentences[num_sentences].get("object");
         if (fd.string.equals("?")) {
             sentences[num_sentences].put("object",
                                  new AIframedata(Words[i]));
             if (MyGUI!=null) {
                MyGUI.P("Object: " + Words[i] + "\n");
             }
             break;
         }
      }
    }
  }
}
