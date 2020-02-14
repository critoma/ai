/**
 * Genetic algorythm demo game.
 *
 * BASED ON:
 *
 * Roids.java
 * @author	Mark G. Tacchi (mtacchi@next.com)
 * @version	0.8
 * Mar 27/1996
 *
 *
 * Changes by Mark Watson for using a genetic algorithm
 * to dynamically modify enemy ship behaviors.
 *
 */


import java.applet.AudioClip;
import java.lang.Math;
import java.awt.*;

import com.next.gt.*;

import mwa.ai.genetic.*;


public class GeneticGame extends Gamelet
                         implements EventHandler {

  public Ship		player;
  public int		numShips;
  public int		badGuyCount;

  //
  // Genetic algorithm generation # label.
  //
  private Label	myLabel;

  /**
   * Initialize.
   */
  public void init() {
    //
    // cache images
    //
    new ImageManager(this);

    myLabel= new Label(" GA generation: " + GAship.Generation + "    ");
    add("South",myLabel);

    this.newGame();

    //
    // register for events
    //
    eventManager.
      registerForSingleEventNotification(this,
                                         Event.KEY_ACTION_RELEASE);

    this.requestFocus();

     //
    // paint background image
    //
    displayManager.setBackgroundTile(getImage(getCodeBase(),
                                     "images/background.gif"));

  } /*init*/



  /**
   * Set up the new game.
   */
  public void newGame() {
    numShips= 15;
    badGuyCount= 0;
    player= null;
    actorManager.removeAllActors();
    this.createActors();
  } /*newGame*/


  /**
   * Create the actors for this scene.
   */
  public void createActors() {
    for (int i= 0; i< (numShips - 1); i++) {
       actorManager.addActor (new GAship(this, "gumball"));
	   badGuyCount++;
    } /*nexti*/
    this.createPlayer();
  } /*createActors*/


  /**
   * Create the player object.
   */
  public void createPlayer() {
    if (player!=null) {
      actorManager.removeActor(player);
    } /*endif*/
    player= new Ship(this);
    actorManager.addActor (player);
  } /*createPlayer*/


  /**
   * Handle keyboard events to restart game.
   */
  public boolean handleRequestedEvent (Event theEvent) {
    switch(theEvent.id) {
    case Event.KEY_ACTION_RELEASE:
      switch(theEvent.key) {
	    case Event.F1:
         this.newGame();
	      return true;
      } /*endSwitch*/
    } /*endSwitch*/
    return false;
  } /*handleRequestedEvent*/



  /**
   * Override paint to display genetic algorithm
   * generation # printout
   */
  public void paint(Graphics g) {
    super.paint(g);
    myLabel.setText(" GA generation: " + GAship.Generation);
  } /*paint*/

} /*GeneticGame*/

