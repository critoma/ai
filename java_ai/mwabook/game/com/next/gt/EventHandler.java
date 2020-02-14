/**
 *
 * EventHandler.java
 * @author	Mark G. Tacchi (mtacchi@next.com) 
 * @version	0.8
 * Mar 21/1996
 *
 * The objects which require notification for events should implement this.
*/

package com.next.gt;

import java.awt.Event;

public interface EventHandler {
public boolean handleRequestedEvent(Event theEvent);
} /*EventHandler*/
