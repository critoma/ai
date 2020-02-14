/**
 *
 * DirtyRectSet.java
 * Mar 15/1996
 *
 *
*/


package com.next.gt;

import java.util.Vector;
import java.awt.Rectangle;


public class DirtyRectSet extends java.lang.Object {

   private Vector rects;

   public DirtyRectSet() { rects = new Vector(); }
   public DirtyRectSet(int i) { rects = new Vector(i); } 

   public void addRect (Rectangle r)
   {
  		int size = rects.size ();

		for (int index = 0; index < size; index++)
		{
			Rectangle curr = (Rectangle)rects.elementAt (index);

			if (r.x > curr.x)
			{
				rects.insertElementAt (r, index);
				return;
			} 
		}
		
		rects.addElement (r);

   }

   final int GLUE = 64;

   final private boolean closeEnough (Rectangle r1, Rectangle r2)
   {
   	  boolean result;

      r1.width += GLUE;
	  r1.height += GLUE;
	  r2.width += GLUE;
	  r2.height += GLUE;

	  result = r1.intersects (r2);

      r1.width -= GLUE;
	  r1.height -= GLUE;
	  r2.width -= GLUE;
	  r2.height -= GLUE;

	  return result;
   }

   private void collapse ()
   {
		int index = 0;

		if (rects.size () < 2)
			return;

		Rectangle r1 = (Rectangle)rects.elementAt (index);
		Rectangle r2 = (Rectangle)rects.elementAt (index+1);

		while (true)
		{
			// collapse R1 and R2
			if (closeEnough (r1, r2))
			{
				r1 = r1.union (r2);

				rects.setElementAt (r1, index);
				rects.removeElementAt (index+1);

				if (index+1 < rects.size ())
					r2 = (Rectangle)rects.elementAt (index+1);
				else
					return;
			}

			// go to next pair
			else if (index+2 < rects.size ())
			{
				r1 = r2;
				r2 = (Rectangle)rects.elementAt (index+2);
				index += 1;
			}

			// done
			else
			{
				return;
			}
		}
	}


	public void drawImage (java.awt.Graphics g, java.awt.Image img, java.applet.Applet owner)
	{
		collapse ();

    	for (int i= 0; i< rects.size (); i++) {
    		Rectangle 	r 	= (Rectangle)rects.elementAt(i);
  			java.awt.Graphics 	g2 	= g.create (r.x, r.y, r.width, r.height);
  			g2.drawImage(img, -r.x, -r.y, owner);
  			g2.dispose ();

			// g.drawRect (r.x+1, r.y+1, r.width-2, r.height-2);
    	}
	}

}
