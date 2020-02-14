/**
 *
 * ActorManager.java
 * @author	Mark G. Tacchi (mtacchi@next.com)
 * @version	0.8
 * Mar 27/1996
 *
 * ActorManager maintains a list of all actors which are participating in
 * the game.  Actors may be added or removed from this list by requesting
 * a change in the list.  When it is safe to make changes to the list,
 * ActorManager will do so.
 *
 * Collision detection is performed in ActorManager.  If Actors collide,
 * a message is sent to each Actor indicating which Actor it has collided
 * with.
 *
 * Queries can be made to ActorManager to determine whether an Actor is in
 * a specific location.
*/

package com.next.gt;

import java.util.Vector;
import java.awt.Rectangle;


public class ActorManager extends java.lang.Object {

  //
  //  A reference back to the Gamelet this manager is servicing.
  //
  protected Gamelet owner;

  //
  //  The list of actors currently alive.
  //
  public Vector actors= new Vector();

  //
  //  The list of actors that will be removed or added when
  //  the current heartbeat is over.
  //
  private Vector actorsScheduledForRemoval= new Vector();
  private Vector actorsScheduledForInsertion= new Vector();

  private boolean	removeAllActors= false;

public ActorManager(Gamelet theOwner) {
  owner= theOwner;
}



/**
 * The Actor is added to a list of Actors to be added.
 */
public void addActor (Actor theActor)
{
  actorsScheduledForInsertion.addElement (theActor);
} /*addActor*/



/**
 * The Actor is added to a list of Actors to be removed.
 */
public void removeActor (Actor theActor)
{
  actorsScheduledForRemoval.addElement (theActor);
} /*removeActor*/



/**
 * Dump all references to Actors.  This is used when a game is restarted.
 */
public void removeAllActors ()
{
  //
  // give eventManager a chance to clean up
  //
  for (int i= 0; i< actors.size(); i++) {
    owner.eventManager.removeFromNotificationRegistry (actors.elementAt (i));
  } /*next_i*/

  //
  // destroy pending lists
  //
  actorsScheduledForRemoval.removeAllElements ();
  actorsScheduledForInsertion.removeAllElements ();

  //
  // destroy all actors
  //
  actors.removeAllElements ();

} /*removeAllActors*/



/**
 * Test if there is an actor at a specified position.
 */
public boolean isActorAt(double theX, double theY) {
  boolean returnValue= false;

  for (int i= 0; i < actors.size(); i++) {
    Actor anActor= (Actor)actors.elementAt(i);

    if (theX >= anActor.x) {
	  if ((theX <= (anActor.x + anActor.width)) &&
	      (theY >= anActor.y) &&
		  (theY <= (anActor.y+anActor.height))) {
        returnValue= true;
      } /*endif*/
    }
    else {
      break;
    }
  } /*next_i*/

  return returnValue;
} /*isActorAt*/



/**
 * Test if there is an actor within a specified Rectangle.
 */
public boolean isActorIn(Rectangle theRectangle) {
  boolean returnValue= false;
  double maxxj= theRectangle.x + theRectangle.width;

  for (int i= 0; i < actors.size(); i++) {
    Actor anActor= (Actor)actors.elementAt(i);

    if (maxxj >= anActor.x) {
      if (theRectangle.y <= anActor.y) {
        if (theRectangle.y + theRectangle.height > anActor.y)
          returnValue= true;
      }
      else {
        if (anActor.y + anActor.height > theRectangle.y)
          returnValue= true;
      }
    }
    else {
      break;
    }
  } /*next_i*/

  return returnValue;
} /*isActorIn*/



/**
 * Insertion sort is used to prep Actors for collision detection.  This
 * technique is ideal because actors are almost always already sorted.
 */
private final void sortActorsByXCoordinate()
{
  int i, j;
  int size= actors.size();

  for (j= 1; j < size; j++) {
     Actor aj= (Actor)actors.elementAt(j);

     for (i= j-1; i>=0; i--) {
        Actor ai= (Actor)actors.elementAt(i);

        if (aj.x < ai.x)
            actors.setElementAt (ai, i+1);
        else
            break;
     } /*next_i*/

     if (j != i+1)
       actors.setElementAt (aj, i+1);
  } /*next_j*/
} /*sortActorsByXCoordinate*/



/*
 * Perform collision detection based on rectangles.  Future versions will
 * detect against circles, and polygons.
 */
private final void detectCollision ()
{
  int i, j;
  int size= actors.size();

  for (j= 0; j+1 < size; j++) {
    Actor aj= (Actor)actors.elementAt(j);
    double maxxj= aj.x + aj.width;

    for (i= j+1; i < size; i++) {
      Actor ai = (Actor)actors.elementAt(i);

      if (maxxj >= ai.x) {
        if (aj.y <= ai.y) {
          if (aj.y + aj.height > ai.y)
            handleBBCollision (aj, ai);
        }
        else {
          if (ai.y + ai.height > aj.y)
            handleBBCollision (aj, ai);
        }
      }
      else {
        break;
      }

    } /*next_i*/
  } /*next_j*/

} /*detectCollision*/



/**
 * Tell each Actor that they've collided with each other.
 */
protected void handleBBCollision (Actor a1, Actor a2)
{
  a1.collideWithActor (a2);
  a2.collideWithActor (a1);
} /*handleBBCollision*/



/**
 * Add/delete Actors, send current Actors a tick message, and then
 * perform collision detection.
 */
public void tick() {

  //
  // add all actors which were scheduled for addtion
  //
  if (actorsScheduledForInsertion.size() > 0) {
    for (int i= 0; i < actorsScheduledForInsertion.size(); i++) {
      actors.addElement (actorsScheduledForInsertion.elementAt (i));
    } /*next_i*/
    actorsScheduledForInsertion.removeAllElements ();
  } /*endif*/


  //
  // remove all actors which were scheduled for removal
  //
  if (actorsScheduledForRemoval.size() > 0) {
      for (int i= 0; i < actorsScheduledForRemoval.size(); i++) {
        owner.eventManager.removeFromNotificationRegistry (actorsScheduledForRemoval.elementAt (i));
        actors.removeElement (actorsScheduledForRemoval.elementAt (i));
      } /*next_i*/
    actorsScheduledForRemoval.removeAllElements ();
  } /*endif*/


  //
  // send a tick to each actor
  //
  for (int i= 0; i< actors.size (); i++) {
     ((Actor)actors.elementAt(i)).tick();
  } /*next_i*/


  //
  // perform collision detection
  //
  sortActorsByXCoordinate ();
  detectCollision();

} /*tick*/

} /*ActorManager*/