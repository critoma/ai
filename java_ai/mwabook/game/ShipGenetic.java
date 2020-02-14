// ShipGenetic.java

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
//    6 if distance to player < 50 pixels, then skip next 2 instructions
//    7 if distance to player > 50 pixels, then skip next 2 instructions
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

class ShipGenetic extends Genetic {

   // Public data for calculating a fitness function
   // (The class GeneticGame will statically allocate one
   // instance of this class. The following data s avalible
   // to methods in GeneticGame and GAship):
   public float closest_dist[];  // to player
   public float furthest_dist[]; // to player
   public float average_dist[];  // to player
   public float max_vel2[];      // square of the maximum velocity
   public int num_times_destroyed[];
   // (These arrays are allocated one element for each instance
   // of the class GAship.)

   float move_sq[];  // square of max distance travelled

   ShipGenetic() {
       // Call the init() function with good game defaults
       // in the constructor, so that a ShipGenetic instance can
       // be allocated statically in the GAship class:
       init(30, GAship.MAX_GA_SHIPS);
   }
   public void init(int g, int c) {
       super.init(g, c);
       num_times_destroyed = new int[c];
       closest_dist = new float[c];  // to player
       furthest_dist = new float[c]; // to player
       average_dist = new float[c];  // to player
       max_vel2 = new float[c];
       move_sq = new float[c];
   }
   public void CalcFitness() {
      for (int i=0; i<NumChrom; i++) {
         // the distances are really distances squared.
         // take the square root to make distance
         // comparisons linear, rather than quadratic:
         average_dist[i] = (float)Math.sqrt(average_dist[i]);
         closest_dist[i] = (float)Math.sqrt(closest_dist[i]);
         furthest_dist[i] = (float)Math.sqrt(furthest_dist[i]);
         max_vel2[i] = (float)Math.sqrt(max_vel2[i]);
         move_sq[i] = (float)Math.sqrt(move_sq[i]);

         float fitness = 0.0f;
         fitness -= average_dist[i];
         fitness += 7.0f * (100.0f - closest_dist[i]);
         fitness -= 5.0f * furthest_dist[i];
         fitness -= 10 * num_times_destroyed[i];
         fitness -= max_vel2[i];
         fitness += 10.0f * move_sq[i];
         Fitness[i] = fitness;
      }
      //Print();
   }
   public void Print() {
      for (int i=0; i<NumChrom; i++) {
            System.out.print("Fitness for chromosome ");
            System.out.print(i);
            System.out.print(" is ");
            System.out.print(Fitness[i]);
            System.out.print(", average_dist=" + average_dist[i]);
            System.out.print(", closest_dist=" + closest_dist[i]);
            System.out.print(", furthest_dist=" + furthest_dist[i]);
            System.out.print(", num_times_destroyed=" +
                             num_times_destroyed[i]);
            System.out.print(", max_vel2=" + max_vel2[i]);
            System.out.println(", move_sq=" + move_sq[i]);
      }
   }
}
