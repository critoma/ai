/**
 *
 * ImageManager.java
 * @author	Mark G. Tacchi (mtacchi@next.com)
 * @version	0.8
 * Mar 19/1996
 *
 * ImageManager is used to force-load images at once to avoid taking
 * the hit during gameplay and distrupting game flow.  Images to be cached
 * are listed in a cache file located in the <codebase>/images directory.  The
 * default cache file is images/.cache.
 *
 */

package com.next.gt;

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;

public class ImageManager extends java.lang.Object{
  Gamelet	owner;

/**
  Cache those images which are listed in images/.cache.
*/
public ImageManager(Gamelet theOwner) {
  this(theOwner, ".cache");
} /*ImageManager()*/



/**
  Cache those images which are listed in the specified cache file.
  This cache file should exist under the images directory.
*/
public ImageManager(Gamelet theOwner, String cacheFile) {
  URL 				myURL= null;
  InputStream		myStream= null;
  DataInputStream	data=null;
  String			line;
  Image			theImage;
  Image 		offScreenBuffer;
  MediaTracker		tracker;
  int				imageCount= 0;

  owner= theOwner;

  //
  // create the offscreen buffer
  //
  offScreenBuffer= owner.createImage (1, 1);


  //
  // create URL that points to cache file.  the cache file lists all images
  // that are to be preloaded.
  //
  try {
    myURL= new URL (owner.getCodeBase().toString()+"/images/" + cacheFile);
  }
  catch(MalformedURLException e) {
    System.out.println("GT: ImageManager cannot read cache file; " + e.getMessage());
  }


  //
  // cycle through all images
  //
  try {
    myStream= myURL.openStream();
	data= new DataInputStream(new BufferedInputStream(myStream));
	while ((line= data.readLine())!=null) {
      imageCount++;
	  theImage = owner.getImage(owner.getCodeBase(), "images/"+line+".gif");
      tracker= new java.awt.MediaTracker(owner);

      tracker.addImage(theImage, imageCount);
      owner.showStatus ("GT: Caching image: " + line + ".");

	//
	// wait for images to be cached
	//
    try {
		tracker.waitForID(imageCount);
    }
    catch (InterruptedException e) {
	    System.out.println("GT: ImageManager ridiculous image; " + e.getMessage());
    }

     offScreenBuffer.getGraphics ().drawImage (theImage, 0, 0, owner);
    } /* endWhile */
  }
  catch(IOException e ){
    System.out.println("GOOF: ImageManager cannot getImage; " + e.getMessage());
  }

} /* ImageManager(,) */


} /* mageManager */

