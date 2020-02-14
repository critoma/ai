 /*
 * Genetic algorithm based game, BASED ON:
 *
 * Ship.java
 * @author	Mark G. Tacchi (mtacchi@next.com)
 * @version	0.8
 * Mar 11/1996
 *
 * The ship is controlled by the user, it registers for specific keyboard
 * events to handle control.
 *
 * This Actor collides with Asteroids and Goobies.  It is responsible for
 * creating an explosion object.
 *
 */

// NOTE: I modified this file to make the ship slide vertically (only)
//       along the left side of the screen in order to make this game
//       a "horizontal scroller".  -- Mark Watson

import java.applet.Applet;
import java.applet.AudioClip;
import java.lang.Math;
import java.awt.*;

import com.next.gt.*;

public class Ship extends Actor implements EventHandler{

  private static int 		MAX_NUM_BULLETS= 5;
  public int				numBullets= 0;

  public int				animationDirection= 1;
  public boolean			isAnimating= true;

  final int MOVING_UP=1;
  final int STOPPED=0;
  final int MOVING_DOWN=-1;
  public int			thrusting= STOPPED;

Ship(Gamelet theOwner) {
  super();

  Image			theImage;
  owner= theOwner;

  // play warp in sound
  owner.play(owner.getCodeBase(), "sounds/warp.au");

  x= 15;
  y= (owner.size().height/2.0);
  velocity_x= 0;
  velocity_y= 0;
  String	theImageName= "images/ship.gif";

  theImage= owner.getImage(owner.getCodeBase(), "images/ship.gif");
  setImage (theImage, 4, 24);
  isAnimating= false;

  int events[]=	{	Event.KEY_ACTION,
  					Event.KEY_ACTION_RELEASE,
					Event.KEY_PRESS,
					Event.KEY_RELEASE
				};

  owner.eventManager.registerForEventNotification(this,events);

} /*Ship()*/



/**
 * Handle keyboard events that control ship.
 */
public boolean handleRequestedEvent (Event theEvent) {
  switch(theEvent.id) {
  case Event.KEY_ACTION:
    switch(theEvent.key) {
	  case Event.RIGHT:
        this.rotateRight(true);
	    return true;
	  case Event.LEFT:
        this.rotateLeft(true);
	    return true;
	  case Event.UP:					//THRUST ON
        this.moveUp(true);
	    return true;
	  case Event.DOWN:
	    this.moveDown(true);
	    return true;
	} /*endSwitch*/
	break;
  case Event.KEY_ACTION_RELEASE:
    switch(theEvent.key) {
	  case Event.RIGHT:
        this.rotateRight(false);
	    return true;
	  case Event.LEFT:
        this.rotateLeft(false);
	    return true;
	  case Event.UP:					//THRUST OFF
        this.moveUp(false);
	    return true;
	  case Event.DOWN:
	    this.moveDown(false);
	} /*endSwitch*/
	break;
    case Event.KEY_PRESS:
      switch(theEvent.key) {
		case 32:
		  this.fire();
		  return true;
	  } /*endSwitch*/
    break;
    case Event.KEY_RELEASE:
      switch(theEvent.key) {
		case 32:
		  return true;
	  } /*endSwitch*/
    break;
  } /*endSwitch*/

  return false;

} /*handleRequestedEvent*/

/**
 * If ship is thrusting, then velocity is increasing.  Use friction if
 * not thrusting.
 */
public void calculateNewVelocity() {
  if (thrusting != 0) {
    velocity_x = 0;
    if (thrusting == MOVING_DOWN) {
      velocity_y += 5;
      if (y > (owner.size().height - 100)) {
        velocity_y = 0;
        // play a thud sound here
      }
    } else { // has to be MOVING_UP
      velocity_y -= 5;
      if (y < 110) {
        velocity_y = 0;
        // play a thud sound here
      }
    }
  }
  else {
    velocity_x = 0;
    velocity_y *= 0.97;
  }

} /*calculateNewVelocity*/

/**
 * Animation of the ship is based on theta, display accordingly.
 */
public void calculateCurrentFrame() {
   if (isAnimating) {
     if (animationDirection== -1) {
	   if (--currentFrame<=0) currentFrame= numFrames - 1;
	 }
	 else {
	  if (++currentFrame>=numFrames) currentFrame= 0;
	 }
    } /*endif*/

} /*calculateCurrentFrame*/



/**
 * Handle left rotation.
 */
public void rotateLeft (boolean keydown) {
  if (keydown) {
    isAnimating= true;
    animationDirection= 1;
  }
  else {
    isAnimating= false;
  }

} /*rotateLeft*/

/**
 * Handle right rotation.
 */
public void rotateRight (boolean keydown) {
  if (keydown) {
    animationDirection= -1;
    isAnimating= true;
  }
  else {
    isAnimating= false;
  }

} /*rotateRight*/



/**
 * Handle thrust.
 */
public void moveUp (boolean keydown) {
  if (keydown) {
    thrusting= MOVING_UP;
  }
  else {
    thrusting= STOPPED;
  }

} /*thrust*/

public void moveDown (boolean keydown) {
  if (keydown) {
    thrusting= MOVING_DOWN;
  }
  else {
    thrusting= STOPPED;
  }

} /*thrust*/


/**
 * Fire bullet.
 */
public void fire() {

  if (numBullets<MAX_NUM_BULLETS) {
    Bullet aBullet;

    numBullets++;
    owner.play(owner.getCodeBase(), "sounds/bullet.au");
    aBullet= new Bullet(owner, this);
    owner.actorManager.addActor(aBullet);
  } /*endif*/

} /*fire*/


/**
 * Accessor methods (bullet uses this).
 */

/**
 * Ship's angle.
 */
public double getTheta() {
  return (currentFrame*2*Math.PI/numFrames + Math.PI/2);
} /*getTheta*/


/**
 * Ship's speed.
 */
public double getSpeed() {
  return Math.sqrt(velocity_x*velocity_x + velocity_y*velocity_y);
} /*getSpeed*/



/**
 * Handle collision with an actor.
 */
protected void collideWithActor (Actor theActor)
{
  String theActorClassName= theActor.getClass().getName();

  if (theActorClassName.equals("Asteroid")) {
    explode();
  } /*endif*/

} /*collideWithActor*/



/**
 * Explode ship.
 */
public void explode()
{
  // NOTE: the player ship does not explode

} /*explode*/


} /*Ship*/
