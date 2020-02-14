/**
 *
 * TiledImage.java
 * @author	Mark G. Tacchi (mtacchi@next.com) 
 * @version	0.8
 * Feb 24/1996
 * 
 * TiledImage is a simple class with the sole responsibility of
 * replicating an image tile to produce a large tiled image.  This is
 * useful for creating a background image.
 * 
*/

package com.next.gt;

import java.awt.Graphics;
import java.awt.Image;


public class TiledImage extends java.lang.Object {
  static Image		tiledImage;

public static Image createTiledImage(Image theImage, int width, int height, Gamelet owner) {
  int		i, j;
  
  tiledImage= owner.createImage(width,height);
  for (i= 0; i< width; i+= theImage.getWidth(null)) {
    for (j= 0; j< height; j+= theImage.getHeight(null)) {
	  tiledImage.getGraphics().drawImage(theImage,i,j,owner);
	}
  }
  return tiledImage;
} /*TiledImage(,,,)*/


} /*TiledImage*/