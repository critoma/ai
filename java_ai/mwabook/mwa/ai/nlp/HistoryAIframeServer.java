// Test server classes
//
// Copyright 1996, Mark Watson.  All rights reserved.

package mwa.ai.nlp;

import java.awt.*;
import java.applet.Applet;
import java.lang.*;
import java.util.*;

import mwa.gui.*;
import mwa.agent.*;
import mwa.data.*;


public class HistoryAIframeServer extends GUI {

    AIframeServer server;

    public String getAppletInfo() {
        return "Test the agent stuff.  By Mark Watson";
    }

    public void init() {
      // Disable all standard GUI display components except output:
      NoGraphics    = true;
      NoInput       = true;
      NoRunButton   = true;
      NoResetButton = true;
      BigText=1;

      super.init();
      P("testServer applet");

      server = new AIframeServer(0, this);  // use default port

      AddData();
    }

    //public void run() {
    //    System.out.println("in testServer::run()\n");
    //    super.run();
    //}

    // Add the data procedurally:
    private void AddData() {
      AIframe place, event, person;
      // Place object: city of Knossos
      place = new AIframe("Knossos_0");
      place.put("type", new AIframedata("place"));
      place.put("name", new AIframedata("Knossos"));
      place.put("description", new AIframedata("city"));
      server.MyObjects[server.NumObjects++]=place;
      // Event object: founding of city of Knossos
      event = new AIframe("Knossos_1");
      event.put("type", new AIframedata("event"));
      event.put("place", new AIframedata("!!" + place.getName()));
      event.put("description",
                new AIframedata("Founding of Knossos"));
      event.put("action", new AIframedata("founding"));
      event.put("year", new AIframedata(-2500)); // negative for BC
      server.MyObjects[server.NumObjects++]=event;
      // Event object: destruction of city of Knossos
      event = new AIframe("Knossos_2");
      event.put("type", new AIframedata("event"));
      event.put("description",
                new AIframedata("Destruction of Knossos"));
      event.put("place", new AIframedata("!!" + place.getName()));
      event.put("action", new AIframedata("destruction"));
      event.put("year", new AIframedata(-1400)); // negative for BC
      server.MyObjects[server.NumObjects++]=event;
      // Person object: Buddha
      person = new AIframe("Buddha_0");
      person.put("last", new AIframedata("Buddha"));
      person.put("type", new AIframedata("person"));
      server.MyObjects[server.NumObjects++]=person;
      // Event object: birth of Buddha
      event = new AIframe("Buddha_1");
      event.put("type", new AIframedata("event"));
      event.put("person", new AIframedata("!!" + person.getName()));
      event.put("description",
                new AIframedata("Birth of the Buddha"));
      event.put("year", new AIframedata(-563));
      server.MyObjects[server.NumObjects++]=event;

      // Place object: city of Rome
      place = new AIframe("Rome_0");
      place.put("type", new AIframedata("place"));
      place.put("name", new AIframedata("Rome"));
      place.put("description", new AIframedata("city"));
      server.MyObjects[server.NumObjects++]=place;
      // Event object: founding of city of Rome
      event = new AIframe("Rome_1");
      event.put("type", new AIframedata("event"));
      event.put("place", new AIframedata("!!" + place.getName()));
      event.put("action", new AIframedata("founding"));
      event.put("description",
                new AIframedata("Founding of the city of Rome"));
      event.put("year", new AIframedata(-753)); // negative for BC
      server.MyObjects[server.NumObjects++]=event;
      // Event object: deposition of last Roman Emperor
      event = new AIframe("Roman_Emporer_0");
      event.put("type", new AIframedata("event"));
      event.put("place", new AIframedata("!!" + place.getName()));
      event.put("action", new AIframedata("deposition"));
      event.put("year", new AIframedata(+476)); // positive for AD
      event.put("description",
                new AIframedata("Deposition of the last emperor of Rome"));
      server.MyObjects[server.NumObjects++]=event;
      // Place object: city of Byzantium
      place = new AIframe("Byzantium_0");
      place.put("type", new AIframedata("place"));
      place.put("name", new AIframedata("Byzantium"));
      place.put("description", new AIframedata("city"));
      server.MyObjects[server.NumObjects++]=place;
      // Event object: founding of city of Byzantium
      event = new AIframe("Byzantium_1");
      event.put("type", new AIframedata("event"));
      event.put("place", new AIframedata("!!" + place.getName()));
      event.put("action", new AIframedata("founding"));
      event.put("year", new AIframedata(-660)); // negative for BC
      event.put("description",
                new AIframedata("Founding of the city of Byzantium"));
      server.MyObjects[server.NumObjects++]=event;
      // Place object: empire of Sumeria
      place = new AIframe("Sumeria_0");
      place.put("type", new AIframedata("place"));
      place.put("name", new AIframedata("Sumeria"));
      place.put("description", new AIframedata("empire"));
      server.MyObjects[server.NumObjects++]=place;
      // Event object: founding of empire of Sumeria
      event = new AIframe("Sumeria_1");
      event.put("type", new AIframedata("event"));
      event.put("place", new AIframedata("!!" + place.getName()));
      event.put("action", new AIframedata("founding"));
      event.put("year", new AIframedata(-2350)); // negative for BC
      event.put("description",
                new AIframedata("Founding of the empire of Sumeria"));
      server.MyObjects[server.NumObjects++]=event;
      // Place object: city of Troy
      place = new AIframe("Troy_0");
      place.put("type", new AIframedata("place"));
      place.put("name", new AIframedata("Troy"));
      place.put("description", new AIframedata("empire"));
      server.MyObjects[server.NumObjects++]=place;
      // Event object: destruction of city of Troy
      event = new AIframe("Troy_1");
      event.put("type", new AIframedata("event"));
      event.put("place", new AIframedata("!!" + place.getName()));
      event.put("action", new AIframedata("destruction"));
      event.put("year", new AIframedata(-1193)); // negative for BC
      event.put("description",
                new AIframedata("destruction of the city of Troy"));
      server.MyObjects[server.NumObjects++]=event;
      // Place object: Egypt
      place = new AIframe("Egypt_0");
      place.put("type", new AIframedata("place"));
      place.put("name", new AIframedata("Egypt"));
      place.put("description", new AIframedata("pyramid"));
      server.MyObjects[server.NumObjects++]=place;
      // Event object: first pyramid built in Egypt
      event = new AIframe("pyramid");
      event.put("type", new AIframedata("event"));
      event.put("place", new AIframedata("!!" + place.getName()));
      event.put("action", new AIframedata("built"));
      event.put("year", new AIframedata(-2780)); // negative for BC
      event.put("description",
                new AIframedata("first pyramid built in Egypt"));
      server.MyObjects[server.NumObjects++]=event;
      // Person object: Confucius
      person = new AIframe("Confucius_0");
      person.put("last", new AIframedata("Confucius"));
      server.MyObjects[server.NumObjects++]=person;
      // Event object: birth of Confucius
      event = new AIframe("Confucius_1");
      event.put("type", new AIframedata("event"));
      event.put("action", new AIframedata("birth"));
      event.put("person", new AIframedata("!!" + person.getName()));
      event.put("year", new AIframedata(-551));
      event.put("description",
                new AIframedata("birth of Confucius"));
      server.MyObjects[server.NumObjects++]=event;
      // Person object: Caligula
      person = new AIframe("Caligula_0");
      person.put("last", new AIframedata("Caligula"));
      server.MyObjects[server.NumObjects++]=person;
      // Event object: Caligula becomes Emporer of Rome
      event = new AIframe("Emporer_1");
      event.put("type", new AIframedata("event"));
      event.put("place", new AIframedata("!!" + place.getName()));
      event.put("action", new AIframedata("becomes"));
      event.put("year", new AIframedata(+1193)); // positive for AD
      event.put("description",
                new AIframedata("Caligula becomes first emporer of Rome"));
      server.MyObjects[server.NumObjects++]=event;
      // Person object: Jesus
      person = new AIframe("Jesus_0");
      person.put("last", new AIframedata("Jesus"));
      person.put("type", new AIframedata("person"));
      server.MyObjects[server.NumObjects++]=person;
      // Event object: birth of Jesus
      event = new AIframe("Jesus_1");
      event.put("type", new AIframedata("event"));
      event.put("action", new AIframedata("birth"));
      event.put("person", new AIframedata("!!" + person.getName()));
      event.put("year", new AIframedata(-5));
      event.put("description",
                new AIframedata("birth of Jesus"));
      server.MyObjects[server.NumObjects++]=event;
      // Person object: Muhammad
      person = new AIframe("Muhammad_0");
      person.put("last", new AIframedata("Muhammad"));
      server.MyObjects[server.NumObjects++]=person;
      // Place object: Mecca
      place = new AIframe("Mecca");
      place.put("type", new AIframedata("place"));
      place.put("name", new AIframedata("Mecca"));
      server.MyObjects[server.NumObjects++]=place;
      // Event object: birth of Muhammad at Mecca
      event = new AIframe("Muhammad_1");
      event.put("type", new AIframedata("event"));
      event.put("action", new AIframedata("birth"));
      event.put("person", new AIframedata("!!" + person.getName()));
      event.put("place", new AIframedata("!!" + place.getName()));
      event.put("year", new AIframedata(+570));
      event.put("description",
                new AIframedata("birth of Muhammad"));
      server.MyObjects[server.NumObjects++]=event;
      // Place object: Hastings
      place = new AIframe("Hastings_0");
      place.put("type", new AIframedata("place"));
      place.put("name", new AIframedata("Hastings"));
      server.MyObjects[server.NumObjects++]=place;
      // Event object: battle of Hastings
      event = new AIframe("Hastings_1");
      event.put("type", new AIframedata("event"));
      event.put("place", new AIframedata("!!" + place.getName()));
      event.put("action", new AIframedata("battle"));
      event.put("year", new AIframedata(+1066));
      event.put("description",
                new AIframedata("battle of Hastings"));
      server.MyObjects[server.NumObjects++]=event;
      // Event object: Magna Carta
      event = new AIframe("Magna Carta_0");
      event.put("type", new AIframedata("event"));
      event.put("place", new AIframedata("!!" + place.getName()));
      event.put("year", new AIframedata(+1215));
      event.put("description",
                new AIframedata("king of England signs Magna Carta"));
      server.MyObjects[server.NumObjects++]=event;
      // Place object: Europe
      place = new AIframe("Europe_0");
      place.put("type", new AIframedata("place"));
      place.put("name", new AIframedata("Hastings"));
      server.MyObjects[server.NumObjects++]=place;
      // Event object: Hundred Years War
      event = new AIframe("Hundred Years War_0");
      event.put("type", new AIframedata("event"));
      event.put("place", new AIframedata("!!" + place.getName()));
      event.put("year", new AIframedata(+1338));
      event.put("description",
                new AIframedata("hundred year war"));
      server.MyObjects[server.NumObjects++]=event;
      // Event object: Black Death ravages Europe
      event = new AIframe("Black Death_0");
      event.put("type", new AIframedata("event"));
      event.put("place", new AIframedata("!!" + place.getName()));
      event.put("year", new AIframedata(+1348));
      server.MyObjects[server.NumObjects++]=event;
      // Place object: Carthage
      place = new AIframe("Carthage_0");
      place.put("type", new AIframedata("place"));
      place.put("name", new AIframedata("Carthage"));
      server.MyObjects[server.NumObjects++]=place;
      // Event object: destruction of Carthage by Arabs
      event = new AIframe("Carthage_1");
      event.put("type", new AIframedata("event"));
      event.put("place", new AIframedata("!!" + place.getName()));
      event.put("action", new AIframedata("destruction"));
      event.put("year", new AIframedata(+697));
      event.put("description",
                new AIframedata("destruction of Carthage by Arabs"));
      server.MyObjects[server.NumObjects++]=event;
      // Place object: Tenochtitlan
      place = new AIframe("Tenochtitlan_0");
      place.put("type", new AIframedata("place"));
      place.put("name", new AIframedata("Tenochtitlan"));
      place.put("description", new AIframedata("city"));
      server.MyObjects[server.NumObjects++]=place;
      // Event object: founding of Tenochtitlan by Aztecs
      event = new AIframe("Tenochtitlan_1");
      event.put("type", new AIframedata("event"));
      event.put("place", new AIframedata("!!" + place.getName()));
      event.put("action", new AIframedata("founding"));
      event.put("year", new AIframedata(+1325));
      event.put("description",
                new AIframedata("founding of Tenochtitlan by Aztecs"));
      server.MyObjects[server.NumObjects++]=event;
      // Place object: Peru
      place = new AIframe("Peru_0");
      place.put("type", new AIframedata("place"));
      place.put("name", new AIframedata("Peru"));
      place.put("description", new AIframedata("city"));
      server.MyObjects[server.NumObjects++]=place;
      // Event object: Inca Empire established in Peru
      event = new AIframe("Peru_1");
      event.put("type", new AIframedata("event"));
      event.put("place", new AIframedata("!!" + place.getName()));
      event.put("action", new AIframedata("established"));
      event.put("person", new AIframedata("Incas"));
      event.put("year", new AIframedata(+1438));
      event.put("description",
                new AIframedata("Inca empire founded in Peru"));
      server.MyObjects[server.NumObjects++]=event;
      // Place object: West Indies
      place = new AIframe("West Indies_0");
      place.put("type", new AIframedata("place"));
      place.put("name", new AIframedata("West Indies"));
      place.put("description", new AIframedata("West Indies"));
      server.MyObjects[server.NumObjects++]=place;

     // Person object: Christopher Columbus
      person = new AIframe("Columbus_0");
      person.put("last", new AIframedata("Columbus"));
      person.put("first", new AIframedata("Christopher"));
      person.put("type", new AIframedata("person"));
      server.MyObjects[server.NumObjects++]=person;
      // Event object: Christopher Columbus discovers West Indies
      event = new AIframe("West_Indies_1");
      event.put("type", new AIframedata("event"));
      event.put("place", new AIframedata("!!" + place.getName()));
      event.put("action", new AIframedata("discovers"));
      event.put("person", new AIframedata("!!" + person.getName()));
      event.put("year", new AIframedata(+1492));
      event.put("description",
                new AIframedata("Christopher Columbus discovers West Indies"));
      server.MyObjects[server.NumObjects++]=event;
      // Event object: Christopher Columbus dies in poverty
      event = new AIframe("Christopher_Columbus_3");
      event.put("type", new AIframedata("event"));
      event.put("action", new AIframedata("dies"));
      event.put("person", new AIframedata("!!" + person.getName()));
      event.put("year", new AIframedata(+1506));
      event.put("description",
                new AIframedata("Christopher Columbus dies in poverty"));
      server.MyObjects[server.NumObjects++]=event;

      // Place object: Rome
      place = new AIframe("Rome_0");
      place.put("type", new AIframedata("place"));
      place.put("name", new AIframedata("Rome"));
      place.put("description",
       new AIframedata("Early seat of Roman empire and capital of Italy"));
      server.MyObjects[server.NumObjects++]=place;
      // Person object: Giovanni de Dolci
      person = new AIframe("Giovanni_de_Dolci_0");
      person.put("last", new AIframedata("Dolci"));
      person.put("first", new AIframedata("Giovanni"));
      person.put("type", new AIframedata("person"));
      server.MyObjects[server.NumObjects++]=person;
      // Event object: Sistine Chapel built by Giovanni de Dolci
      event = new AIframe("Sistine Chapel_0");
      event.put("type", new AIframedata("event"));
      event.put("place", new AIframedata("!!" + place.getName()));
      event.put("action", new AIframedata("built"));
      event.put("person", new AIframedata("!!" + person.getName()));
      event.put("year", new AIframedata(+1473));
      event.put("description",
                new AIframedata("Sistine Chapel built by Giovanni de Dolci"));
      server.MyObjects[server.NumObjects++]=event;
      // Person object: Leonardo da Vinci
      person = new AIframe("Leonardo_da_Vinci_0");
      person.put("last", new AIframedata("Vinci"));
      person.put("first", new AIframedata("Leonardo"));
      person.put("type", new AIframedata("person"));
      server.MyObjects[server.NumObjects++]=person;
      // Event object: birth of Leonardo da Vinci
      event = new AIframe("Leonardo da Vinci_1");
      event.put("type", new AIframedata("event"));
      event.put("action", new AIframedata("birth"));
      event.put("person", new AIframedata("!!" + person.getName()));
      event.put("year", new AIframedata(+1475));
      event.put("description",
                new AIframedata("birth of Leonardo da Vinci"));
      server.MyObjects[server.NumObjects++]=event;
      // Person object: Henry VIII, King of England
      person = new AIframe("Henry_VIII_0");
      person.put("last", new AIframedata("Henry VIII"));
      person.put("type", new AIframedata("person"));
      server.MyObjects[server.NumObjects++]=person;
      // Event object: Henry VIII, King of England, dies
      event = new AIframe("Henry VIII_1");
      event.put("type", new AIframedata("event"));
      event.put("person", new AIframedata("!!" + person.getName()));
      event.put("year", new AIframedata(1547));
      event.put("action", new AIframedata("dies"));
      event.put("description",
                new AIframedata("Henry VIII, King of England dies"));
      server.MyObjects[server.NumObjects++]=event;

    }
}
