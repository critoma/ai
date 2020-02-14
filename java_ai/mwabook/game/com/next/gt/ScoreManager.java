/**
 *
 * ScoreManager.java
 * @author	Mark G. Tacchi (mtacchi@next.com) 
 * @version	0.8
 * Mar 15/1996
 *
 * ScoreManager maintains the current score and provides accessor methods
 * for objects to access/update the score.  It also allows objects to register
 * for notification when a specific point level has been reached.  This is
 * most useful for rewarding player with some sort of bonus.
 *
*/

package com.next.gt;

import java.util.Vector;

public class ScoreManager extends java.lang.Object {
  public	int				score;
  private	Vector			objectsToNotify;

public ScoreManager() {
  objectsToNotify= new Vector();
} /*ScoreManager()*/



/**
 * Add to the score.
*/
public void addToScore(int theValue) {
  score+= theValue;
  checkForBonus(theValue);
} /*addToScore*/



/**
 * Set the score.
*/
public void setScore(int theValue) {
  score= theValue;
} /*setScore*/



/**
 * Subtract from the score.
*/
public void subtractFromScore(int theValue) {
  score-= theValue;
} /*subtractFromScore*/



/**
 * For every `bonus' points, notify requestor.
*/
public void registerForBonusNotification (Object theObject, int theValue) {
  Vector	registerVector= new Vector(3);
  
  registerVector.addElement(theObject);                 // the object to notify
  registerVector.addElement(new Integer(theValue));     // bonus every
  registerVector.addElement(new Integer(0));            // bonus counter
  
  objectsToNotify.addElement(registerVector);
} /*registerForBonusNotification*/



/**
 * Check if it's time for a bonus.
*/
private void checkForBonus(int theValue) {
  Vector	theObjectToNotify;
  Integer	n1, n2;
  Integer	anInteger;

  for (int i= 0; i<objectsToNotify.size(); i++) {
    theObjectToNotify= (Vector) objectsToNotify.elementAt(i);

	//
	// increment bonus counter
	//
	anInteger= (Integer) theObjectToNotify.elementAt(2);
    theObjectToNotify.setElementAt(new Integer(anInteger.intValue() + theValue), 2);
	
	//
	// check if bonus counter is greater than the bonus level
	//
	n2= (Integer) theObjectToNotify.elementAt(2);
	n1= (Integer) theObjectToNotify.elementAt(1);
	
	if (n2.intValue() >= n1.intValue()) {
	  BonusHandler	theBonusHandler= (BonusHandler) theObjectToNotify.elementAt(0);
      theObjectToNotify.setElementAt(new Integer(n2.intValue()- n1.intValue()), 2);
 	  theBonusHandler.didAchieveBonus();
   } /*endif*/
	
  } /*nexti*/

} /*checkForBonus*/


} /*ScoreManager*/