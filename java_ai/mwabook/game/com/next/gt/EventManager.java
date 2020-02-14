/**
 *
 * EventManager.java
 * @author	Mark G. Tacchi (mtacchi@next.com) 
 * @version	0.8
 * Mar 21/1996
 *
 * EventManager maintains a list of objects that require notification when
 * a particular Event occurs.  Objects register with a specific Event or a
 * list of Events that it would like notification on.
 *
*/

package com.next.gt;

import java.util.Vector;
import java.awt.Event;

public class EventManager extends java.lang.Object {
  protected	Vector			objectsToNotify;



public EventManager() {
  objectsToNotify= new Vector();
} /*EventManager()*/



/**
 * Register for notification of an event with a Vector of events. 
 */
public void registerForEventNotification (Object theObject, int[] theEventVector) {
  int		theEventID;
  
  for (int i= 0; i < theEventVector.length; i ++) {
    Vector		registerVector= new Vector(2);
	
    theEventID= theEventVector[i];
    registerVector.addElement(theObject);               // the object to notify
    registerVector.addElement(new Integer(theEventID)); // theEventID
    objectsToNotify.addElement(registerVector);
	
  } /*nexti*/
  
} /*registerForBonusNotification*/



/**
 * Register for a single notification of an event.
 */
public void registerForSingleEventNotification (Object theObject, int theEventID) {
  Vector	registerVector= new Vector(2);
  
  registerVector.addElement(theObject);                 // the object to notify
  registerVector.addElement(new Integer(theEventID));   // theEventID
  
  objectsToNotify.addElement(registerVector);
  
} /*registerForBonusNotification*/




/**
 * Remove object from notification registry.
 */
public void removeFromNotificationRegistry(Object theObject) {
  Vector	anObject;

  for (int i= 0; i<objectsToNotify.size(); i++) {
    anObject= (Vector) objectsToNotify.elementAt(i);
    if (anObject.contains(theObject)) {
	  objectsToNotify.removeElementAt(i);
	} /*endif*/
  
  } /*nexti*/
  
} /*removeFromNotificationRegistry*/



/**
 * Handle the event by notifying registered objects.
 *
 */
public boolean handleEvent (Event theEvent) {
  Vector	anObject;
  Integer	n1;
  Integer	anInteger;
  boolean	returnValue= false;
  
  for (int i= 0; i<objectsToNotify.size(); i++) {
    anObject= (Vector) objectsToNotify.elementAt(i);
	
    n1= (Integer) anObject.elementAt(1);

    if(theEvent.id==n1.intValue()) {
      EventHandler	theEventHandler= (EventHandler) anObject.elementAt(0);
      returnValue= theEventHandler.handleRequestedEvent(theEvent);
	} /*endif*/
		
  } /*nexti*/
  return returnValue;
  
} /*handleEvent*/

} /*EventManager*/
