/**
 *
 * GAship.java    NOTE: this file was Asteroid.java
 * Originally written by Mark G. Tacchi (mtacchi@next.com)
 *
 * Mark Watson: converted Mark Tacchi's original code.
 * NOTE: modified for the Genetic Algorithm demo: we only
 *       want the smallest size of asteroids and will use
 *       these to represent the GA-controlled opponents.
 */

//
//   How the GA works:
//
//  I invented a simple "machine language" for controlling
//  the enemy ships. There are eight instructions in this language:
//
//    0 move towards the player
//    1 move away from the player
//    2 move in +y direction (down the screen)
//    3 move in -y direction (up the screen)
//    4 if distance to player < 100 pixels, then skip next instruction
//    5 if distance to player > 100 pixels, then skip next instruction
//    6 if distance to player < 50 pixels, then skip next
//      2 instructions
//    7 if distance to player > 50 pixels, then skip next
//      2 instructions
//
// Each GAship has an instructon buffer that can hold 10 instructions.
// A program counter PC is used to index these instructions. When the
// program counter reaches the last instruction in the instruction
// buffer, it wraps around to the first instruction.
//
// It takes 3 bits to encode one "machine instruction", so the
// chromosome for each GAship is 30 bits long.
//
// Statistics are kept for each GAship in order to evaluate
// it's genetic fitness.
//
// The storage for all chromosomes (one for each GAship instance)
// is stored as static public class data.

import java.lang.Math;

import mwa.ai.genetic.*;

import com.next.gt.*;

public class GAship extends Actor {

  // The data for the genetic algorithm:

  final static int MAX_GA_SHIPS = 14;
  public static int NumGAships = 0;

  // Change the following constant to specify
  // the number of game simulaton cycles to use
  // for each machine instruction (i.e., increment
  // the "program counter" for the control programs
  // every 'NUM_TURNS_PER_INSTRUCTION' game iterations):
  final static int NUM_TURNS_PER_INSTRUCTION = 4;

  int repeat_count;


  //
  //                Setup for GA:
  public static ShipGenetic Chromosomes = new ShipGenetic();
  public static int Generation = 0;
  public static int MyTick = 0;

  private int MyChromosomeIndex;

  // data for "machine language" instruction buffer:
  int IB[];
  int IBindex = 0;

  //
  // The filename prefix.
  //
  String			name;

  GeneticGame GG;

float InitX, InitY;

GAship(Gamelet theOwner, String theName) {

  owner= theOwner;
  GG = (GeneticGame)theOwner;
  name= theName;

  IB = new int[10];

  MyChromosomeIndex = NumGAships++;
  if (NumGAships > MAX_GA_SHIPS) {
    System.out.println("FATAL error: too many GAships created.");
    NumGAships--;
  }

  // Copy the chromosome data to the instructionbuffer 'IB':
  ResetGA();

  IBindex=0;

  // Randomize the initial repeat count for the different ships:
  repeat_count = (int)((NUM_TURNS_PER_INSTRUCTION - 1)
                 * Math.random());

  // Setup for graphics:

  java.awt.Image theImage;

  // Define the initial position and velocity:
  x= (Math.random()*200) + owner.size().width/2;
  InitX = (float)x;
  y= (Math.random()*100) + owner.size().height/2;
  InitY = (float)y;
  velocity_x= (double)((int)Gamelet.randBetween(0.5,1.5)*2 - 1)
              * Gamelet.randBetween(8.,32.);
  velocity_y= (double)((int)Gamelet.randBetween(0.5,1.5)*2 - 1)
              * Gamelet.randBetween(8.,32.);

  theImage = owner.getImage(owner.getCodeBase(),
                            "images/" +theName + "S" + ".gif");
  setImage (theImage, 4, 32);

  currentFrame= (int)Gamelet.randBetween(0, numFrames);

} /*GAship()*/

public void tick() {  // override Actor class tick function

  // update the fitness statistics in the ShipGenetic object for
  // this particular instance of class GAship:
  float temp_sq = (float)((x - InitX)*(x - InitX) + (y - InitY)*(y - InitY));
  if (Chromosomes.move_sq[MyChromosomeIndex] < temp_sq)
     Chromosomes.move_sq[MyChromosomeIndex] = temp_sq;

  float dist2 = (float)((x - GG.player.x)*(x - GG.player.x) +
                        (y - GG.player.y)*(y - GG.player.y));
  if (dist2 < Chromosomes.closest_dist[MyChromosomeIndex])
     Chromosomes.closest_dist[MyChromosomeIndex] = dist2;
  if (dist2 > Chromosomes.closest_dist[MyChromosomeIndex])
     Chromosomes.furthest_dist[MyChromosomeIndex] = dist2;
  if (MyTick > 0) {
    float d1 = Chromosomes.average_dist[MyChromosomeIndex];
    float d2 = (float)(Math.sqrt(dist2) + MyTick * d1);
    d2 /= (float)MyTick;
    Chromosomes.average_dist[MyChromosomeIndex] = d2;
  }
  if (Chromosomes.max_vel2[MyChromosomeIndex] <
      velocity_x*velocity_x + velocity_y*velocity_y)
      Chromosomes.max_vel2[MyChromosomeIndex] =
         (float)(velocity_x*velocity_x + velocity_y*velocity_y);

  if (MyChromosomeIndex == 0) { // only for first GAship object
     // check for time to process all chromosomes for all GAships:
     if (MyTick++ > 500) {  // yup, time to update chromosomes
        Chromosomes.CalcFitness();
        Chromosomes.Sort();
        Chromosomes.DoCrossovers();
        Chromosomes.DoMutations();
        MyTick = 0;
        Generation++;
     }
  }

  // use instruction buffer to set velocity components:
  float del_x, del_y, scale;
  switch (IB[IBindex]) {
      case 0: // move towards player
         del_x = 0.5f * (float)(x - GG.player.x);
         del_y = (float)(y - GG.player.y);
         scale = 2000.0f / (0.001f + del_x*del_x + del_y*del_y);
         del_x *= scale;
         del_y *= scale;
         velocity_x -= del_x;
         velocity_y -= 2*del_y;
         break;
      case 1: // move away from player
         del_x = (float)(x - GG.player.x);
         del_y = (float)(y - GG.player.y);
         scale = 900.0f / (0.001f + del_x*del_x + del_y*del_y);
         del_x *= scale;
         del_y *= scale;
         velocity_x -= del_x;
         velocity_y -= del_y;
         break;
      case 2: // move in +y direction (down screen)
         velocity_y += 80;
         break;
      case 3: // move in -y direction (up screen)
         velocity_y -= 80;
         break;
      case 4: // if dist to player < 100 skip 1 instruction
         del_x = (float)(x - GG.player.x);
         del_y = (float)(y - GG.player.y);
         if (del_x*del_x + del_y*del_y < 100.0f*100.0f) {
            if (repeat_count > NUM_TURNS_PER_INSTRUCTION)
               IBindex += 1;
         }
         break;
      case 5: // if dist to player > 100 skip 1 instruction
         del_x = (float)(x - GG.player.x);
         del_y = (float)(y - GG.player.y);
         if (del_x*del_x + del_y*del_y > 100.0f*100.0f) {
            if (repeat_count > NUM_TURNS_PER_INSTRUCTION)
               IBindex += 1;
         }
         break;
      case 6: // if dist to player < 50 skip 2 instruction s
         del_x = (float)(x - GG.player.x);
         del_y = (float)(y - GG.player.y);
         if (del_x*del_x + del_y*del_y < 50.0f*50.0f) {
            if (repeat_count > NUM_TURNS_PER_INSTRUCTION)
               IBindex += 2;
         }
         break;
      case 7: // if dist to player > 50 skip 2 instruction s
         del_x = (float)(x - GG.player.x);
         del_y = (float)(y - GG.player.y);
         if (del_x*del_x + del_y*del_y > 50.0f*50.0f) {
            if (repeat_count > NUM_TURNS_PER_INSTRUCTION)
               IBindex += 2;
         }
         break;
      default:
         System.out.println("Illegal GAship instruction: " +
                            IB[IBindex] +
                            " at program counter " + IBindex);
         break;
  }

  if (repeat_count++ > NUM_TURNS_PER_INSTRUCTION) {
    repeat_count = 0;
    IBindex++;
    if (IBindex > 9)  IBindex -= 9;
  }

  // do not allow this ship to leave visible screen area:
  //if (x < 30) x = 30;
  if (x < 10) x = owner.size().width - 45;
  if (x > (owner.size().width - 45)) x = owner.size().width - 45;
  if (y < 80) y = 80;
  if (y > (owner.size().height - 100)) y = owner.size().height - 100;

  if (velocity_x > 40)  velocity_x = 40;
  if (velocity_x <-40)  velocity_x =-40;
  if (velocity_y > 40)  velocity_y = 40;
  if (velocity_y <-40)  velocity_y =-40;

  // call Actor.tick() for normal processing:
  super.tick();
}

void ResetGA() {
  currentFrame=0; // inherited from class Agent
  Chromosomes.num_times_destroyed[MyChromosomeIndex] = 0;
  Chromosomes.closest_dist[MyChromosomeIndex]=9999f*9999f; // to player
  Chromosomes.furthest_dist[MyChromosomeIndex]=0; // to player
  Chromosomes.average_dist[MyChromosomeIndex]=0;  // to player
  Chromosomes.max_vel2[MyChromosomeIndex]=0;
  Chromosomes.move_sq[MyChromosomeIndex]=0;

  // re-fill instruction buffer IB based on the
  // contents of this GAship's chromosome:
  int gene=0;
  for (int i=0; i<10; i++) {
    IB[i]=0;
    if (Chromosomes.GetGene(MyChromosomeIndex, gene++))
       IB[i]+=1;
    if (Chromosomes.GetGene(MyChromosomeIndex, gene++))
       IB[i]+=2;
    if (Chromosomes.GetGene(MyChromosomeIndex, gene++))
       IB[i]+=4;
  }
  x= (Math.random()*200) + owner.size().width/2;
  y= (Math.random()*100) + owner.size().height/2;
  velocity_x= (double)((int)Gamelet.randBetween(0.5,1.5)*2 - 1)
              * Gamelet.randBetween(8.,32.);
  velocity_y= (double)((int)Gamelet.randBetween(0.5,1.5)*2 - 1)
              * Gamelet.randBetween(8.,32.);
}

/**
 * Explode GAship.
 */
public void explode()
{
  Explosion anExplosion;

  anExplosion= new Explosion(owner, this);
  owner.actorManager.addActor(anExplosion);
  owner.play(owner.getCodeBase(), "sounds/explode1.au");

  //
  // The GAship is destroyed. Update statistics
  // for calculating the GA fitness function and
  // restart this ship off-screen:
  //

  float dist2 = (float)((x - GG.player.x)*(x - GG.player.x) +
                        (y - GG.player.y)*(y - GG.player.y));
  if (dist2 > 70*70) { // dist2 is distance squared
     Chromosomes.num_times_destroyed[MyChromosomeIndex] += 1;
  }

  x= (Math.random()*200) + owner.size().width/2;
  y= (Math.random()*100) + owner.size().height/2;
  velocity_x= (double)((int)Gamelet.randBetween(0.5,1.5)*2 - 1)
              * Gamelet.randBetween(8.,32.);
  velocity_y= (double)((int)Gamelet.randBetween(0.5,1.5)*2 - 1)
              * Gamelet.randBetween(8.,32.);

} /*explode*/



/**
 * Handle collision with an actor.
 */
protected void collideWithActor (Actor theActor)
{
  String theActorClassName= theActor.getClass().getName();

  if (theActorClassName.equals("Bullet") ||
      theActorClassName.equals("Ship") ) {
    explode();
  } /*endif*/

} /*collideWithActor*/

} /*GAship*/

