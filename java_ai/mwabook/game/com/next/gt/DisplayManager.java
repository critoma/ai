/**
 *
 * DisplayManager.java
 * @author	Mark G. Tacchi (mtacchi@next.com)
 * @version	0.8
 * Mar 28/1996
 *
 * DisplayManager provides optimized screen display. Images are drawn to
 * an offscreen buffer and blitted out to the display.  If images are close
 * to one another, they are coalesced and blitted as a single image.
 *
 * A read only cache is kept which represents an untainted background image.
 * This is used in the optimization algorithm as a source for a clean
 * background.
 *
 */

package com.next.gt;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;

public class DisplayManager extends java.lang.Object  {
  private Image			background;
  private Image 		offScreenBuffer;
  private DirtyRectSet	dirtyRects= new DirtyRectSet();

  /**
  *  A reference back to the gamelet this manager is servicing.
  */
  protected Gamelet owner;


public DisplayManager(Gamelet theOwner) {
  owner= theOwner;
} /*DisplayManager()*/



/**
 * Refresh screen.
 */
public void refresh () {
  dirtyRects.addRect (new Rectangle (0, 0, owner.size().width, owner.size().height));
} /*setBackground*/



/**
 * Set the background image to the specified image.
 */
public void setBackground (Image theImage) {
  MediaTracker    tracker= new java.awt.MediaTracker(owner);

  tracker.addImage(theImage, 0);
  try {
	tracker.waitForID(0);
  } catch (InterruptedException e) {
  }

  background= theImage;
  offScreenBuffer= owner.createImage (owner.size().width, owner.size().height);
  offScreenBuffer.getGraphics().drawImage (theImage, 0, 0, owner);

  dirtyRects.addRect (new Rectangle (0, 0, owner.size().width, owner.size().height));

} /*setBackground*/



/**
 * Tile the background with the specifed tile image.
 */
public void setBackgroundTile (Image theImage) {
  MediaTracker    tracker= new java.awt.MediaTracker(owner);

  tracker.addImage(theImage, 0);
  try {
	tracker.waitForID(0);
  } catch (InterruptedException e) {}

  setBackground(TiledImage.createTiledImage(theImage, owner.size().width, owner.size().height, owner));

} /*setBackgroundTile*/



/**
 * Display changed portions of screen.
 */
public void paint(Graphics g) {
  DirtyRectSet flushRects;
  Graphics osb= offScreenBuffer.getGraphics ();

  //
  // clear background behind actors...
  //
  dirtyRects.drawImage (osb, background, owner);

  flushRects= dirtyRects;
  dirtyRects= new DirtyRectSet(owner.actorManager.actors.size ());

  //
  // draw actors
  //
  for (int j= 0; j< owner.actorManager.actors.size (); j++) {
    Actor anActor= (Actor)owner.actorManager.actors.elementAt(j);
    Rectangle r= new Rectangle ((int)anActor.x, (int)anActor.y, anActor.width, anActor.height);
    dirtyRects.addRect (r);
    flushRects.addRect (r);
    anActor.draw (osb);
  } /*next_j*/

  flushRects.drawImage (g, offScreenBuffer, owner);

} /*paint*/


} /*DisplayManager*/