/**
*
 * Bullet.java
 * @author	Mark G. Tacchi (mtacchi@next.com)
 * @version	0.8
 * Mar 12/1996
*
 * A Bullet is created by the Ship object when it is sent a fire message.
 * Bullets live for a specified time to live (ttl).
 *
 * This Actor collides with all but Ships and Explosions.
 *
 */

import java.awt.*;

import com.next.gt.*;

public class Bullet extends Actor {

  //
  // variable used to compare against for auto death
  //
  long startTime;

  //
  // time to live
  //
  long ttl= 2000;

  //
  // the ship object
  //
  Ship	explodee;

Bullet(Gamelet theOwner, Ship theExplodee) {
  super();

  double			explodeeVelocity= theExplodee.getSpeed();
  double			explodeeTheta= theExplodee.getTheta();
  Image				theImage;

  owner= theOwner;
  explodee= theExplodee;

  x= (explodee.x + (explodee.width/2.0));
  y= (explodee.y + (explodee.height/2.0));

  theImage= owner.getImage(owner.getCodeBase(), "images/bullet.gif");
  setImage (theImage, 4, 16);

  velocity_x= Math.cos(explodeeTheta)*(explodeeVelocity + 150.);
  velocity_y= Math.sin(explodeeTheta+Math.PI)*(explodeeVelocity + 150.);

  x+= (velocity_x * .1);
  y+= (velocity_y * .1);


  startTime= owner.currentTickTimeMillis;

} /*Bullet()*/



/**
 * Override tick to implement a timed death.
 */
public void tick()
{
  super.tick();

  if (owner.currentTickTimeMillis - startTime > ttl) {
    if (explodee.numBullets>0)explodee.numBullets--;
      owner.actorManager.removeActor (this);
  } /*endif*/

} /*tick*/



/**
 * Handle collision with an actor.
 */
protected void collideWithActor (Actor theActor)
{
  String theActorClassName= theActor.getClass().getName();

  if (theActorClassName.equals("Asteroid")) {
	if (explodee.numBullets>0)explodee.numBullets--;
    owner.actorManager.removeActor(this);
  } /*endif*/

} /*collideWithActor*/
} /*Bullet*/
