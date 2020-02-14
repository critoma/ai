/**
 *
 * Actor.java
 * @author	Mark G. Tacchi (mtacchi@next.com)
 * @version	0.8
 * Feb 23/1996
 *
 * Actor is an abstract superclass which defines behaviour used in the Actors
 * the developer creates.
 *
 * Actors are responsible for calculating their positions each tick.  They are
 * also handed a <em>g</em> and expected to draw themselves into it.
 *
 */

package com.next.gt;

import java.util.Date;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.applet.Applet;
import java.lang.Math;

public abstract class Actor extends java.lang.Object {

  //
  // Image and animation variables.
  //
  protected Image	image;
  protected int 	numFrames;
  public int 	width;
  public int 	height;
  public int 	currentFrame;
  protected int 	hFrames;

  //
  // Position and velocity.
  //
  public double	x, y;
  public double	x_old, y_old;
  public double	velocity_x, velocity_y;

  //
  // The object that owns the Actor.
  //
  public Gamelet	owner;

  //
  // Flag indicating whether Actor should wrap at edge of screen.
  //
  public boolean wrapAround;


public Actor() {
  currentFrame= 0;
  wrapAround= true;
} /*Actor()*/



/**
 * Change animation frame, calculate new position, and calculate new velocity..
 */
public void tick() {
  calculateCurrentFrame();
  calculateNewPosition();
  calculateNewVelocity();
} /*tick*/



/**
 * Set the image used for animation.
 */
protected void setImage (Image theImage,
				 int frameXSize,
				 int frameYSize,
				 int framesHorizontally,
				 int totalFrames) {
  width= frameXSize;
  height= frameYSize;
  numFrames= totalFrames;
  hFrames= framesHorizontally;
  image= theImage;

} /*setImage(,,,,,)*/



/**
 * Set the image used for animation.  Good for an image that has all frames
 * within it and none empty.
 */
protected void setImage (Image theImage) {
  int imageHeight;
  int imageWidth;

  do {
    imageHeight= theImage.getHeight (owner);
  } while (imageHeight == -1);
  do {
    imageWidth= theImage.getWidth (owner);
  } while (imageWidth == -1);

  setImage (theImage, imageWidth, imageHeight, 1, 1);
} /*setImage*/



/**
 * Set the image used for animation.  Good for an image that has some empty
 * frames within it.
 */
protected void setImage (Image theImage,
				 int horizontalFrames,
				 int totalFrames) {
  int imageHeight;
  int imageWidth;

  do {
    imageHeight= theImage.getHeight (owner);
  } while (imageHeight == -1);
  do {
    imageWidth= theImage.getWidth (owner);
  } while (imageWidth == -1);

  setImage (theImage, imageWidth/horizontalFrames,
	         imageHeight / (int)Math.ceil((double)totalFrames/(double)horizontalFrames),
		     horizontalFrames,
			 totalFrames
		    );
} /*setImage,,,*/



/**
 * Calculates the new X and Y position based on velocity and time.  Also may check if
 * Actor needs to wrap around at the edges if the <em>wraparound</em> flag is set.
 */
protected void calculateNewPosition() {

  double	deltaTime= owner.deltaTickTimeMillis()/1000.0;

  //
  // save old position
  //
  x_old= x;
  y_old= y;

  //
  // calculate position based on velocity and time
  //
  x+= velocity_x*deltaTime;
  y+= velocity_y*deltaTime;

  if (wrapAround) checkForOutOfBounds();

} /*calculateNewPosition*/



/**
 * Override this to provide your own behaviour.
 */
protected void calculateNewVelocity() {
}



/**
 * Calculates the current frame.  Behaviour is to flip through frames sequentially
 * and loop.
 */
protected void calculateCurrentFrame() {
  if (++currentFrame>=numFrames) currentFrame= 0;
} /*calculateCurrentFrame*/



/**
 * Check for out of bounds and wrap if it is.
 */
protected void checkForOutOfBounds() {

  //
  // check for out of bounds and wrap
  //
  if (x > (owner.size().width + width + width)) {
    x= -width;
  }
  else if (x < -width) {
    x= owner.size().width + width + width;
  }

  if (y > (owner.size().height + height + height)) {
    y= -height;
  }
  else if (y < -height) {
    y= owner.size().height + height + height;
  }

} /*checkForOutOfBounds*/



/**
 * Each Actor is handed a <em>g</em> and is expected to draw itself in it.
 */
public void draw (Graphics g) {
  double	offsetx= -(currentFrame%hFrames)*width;
  double	offsety= -Math.floor(currentFrame/hFrames) * height;

  Graphics 	g2= g.create ((int)x, (int)y, width, height);
  g2.drawImage(image, (int)offsetx, (int)offsety, owner);
  g2.dispose ();

} /*draw*/



/**
 * Override this method to handle the case when Actor collides with another.
 */
protected void collideWithActor (Actor theActor){
} /*collideWithActor*/



/**
 * Bounce off the specified Actor.  Changes it's velocity so that it appears
 * to bounce off.
 */
public void bounceOff(Actor theActor) {
  double		myCenterX= width/2 + x_old, myCenterY= height/2 + y_old;
  double		actorCenterX= theActor.width/2 + theActor.x;
  double		actorCenterY= theActor.height/2 + theActor.y;
  double	slope= (myCenterY - actorCenterY)/(myCenterX - actorCenterX);
  double		b= myCenterY - slope*myCenterX;


  double		intersectY, intersectX;

  //
  // Check if segments intersect.
  //

  //
  // Check RIGHT side
  //
  if (theActor.x>=myCenterX) {
    intersectY= slope*theActor.x + b;
    if (intersectY>=theActor.y && intersectY<=theActor.y+theActor.height) {
	  velocity_x= theActor.velocity_x - velocity_x;
	  x= x_old;
    } /*endif*/
  }
  //
  // Check LEFT side
  //
  else if (theActor.x+theActor.width<=myCenterX) {
    intersectY= slope*(theActor.x + theActor.width) + b;
    if (intersectY>=theActor.y && intersectY<=theActor.y+theActor.height) {
	  velocity_x= theActor.velocity_x - velocity_x;
	  x= x_old;
    } /*endif*/
  }

  //
  // Check BOTTOM side
  //
  else if (theActor.y>=myCenterY) {
    intersectX= (theActor.y - b)/slope;
    if (intersectX>=theActor.x && intersectX<=theActor.x+theActor.width) {
	  velocity_y= theActor.velocity_y - velocity_y;
	  y= y_old;
    } /*endif*/
  }
  //
  // Check TOP side
  //
  else if (theActor.y+theActor.height<=myCenterY) {
    intersectX= (theActor.y + theActor.height - b)/slope;
    if (intersectX>=theActor.x && intersectX<=theActor.x+theActor.width) {
	  velocity_y= theActor.velocity_y - velocity_y;
	  y= y_old;
    } /*endif*/
  }

} /*bounceOff*/

} /*BOActor*/





