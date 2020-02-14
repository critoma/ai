/**
 *
 * Explosion.java
 * @author	Mark G. Tacchi (mtacchi@next.com)
 * @version	0.8
 * Mar 12/1996
 *
 * A simple explosion Actor.
 *
*/


import java.awt.Graphics;

import com.next.gt.*;

public class Explosion extends Actor {

Explosion(Gamelet theOwner, Actor explodee) {
  super();
  java.awt.Image		theImage;
  java.awt.MediaTracker	tracker;

  owner= theOwner;

  //
  // play explosion sound
  //
  owner.play(owner.getCodeBase(), "sounds/explode1.au");

  //
  // load the image
  //
  theImage= owner.getImage(owner.getCodeBase(), "images/explosions.gif");

  //
  // set up key variables
  //
  setImage (theImage, 60, 60, 4, 16);
  x= (explodee.x - (width - explodee.width)/2.0);
  y= (explodee.y - (height - explodee.height)/2.0);
  velocity_x= explodee.velocity_x;
  velocity_y= explodee.velocity_y;

} /*Explosion()*/



/**
 * Calculates the current frame.  Flip through frames sequentially
 * and die when completed.
 */
public void calculateCurrentFrame() {
  if (++currentFrame>=numFrames) {
    owner.actorManager.removeActor(this);
  } /*endif*/
} /*calculateCurrentFrame*/

} /*Explosion*/