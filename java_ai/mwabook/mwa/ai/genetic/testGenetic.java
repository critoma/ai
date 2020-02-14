// File: textGenetic.java
// This file contains a text-mode test program
// for class Genetic.

package mwa.ai.genetic;


public class testGenetic {

   static public void main(String args[]) {
       MyGenetic G = new MyGenetic();
       G.init(30,15);  // 30 genes/chrom.  15 chrom in pop.
       for (int i=0; i<51; i++) {
           G.CalcFitness();
           G.Sort();
           if ((i%5)==0) {
               System.out.println("Generation " + i);
               G.Print();
           }
           G.DoCrossovers();
           G.DoMutations();
       }
   }
}

class MyGenetic extends Genetic {
   MyGenetic() {
       System.out.println("Entered MyGenetic::MyGenetic()\n");
   }
   public void init(int g, int c) {
       super.init(g, c);
   }
   public void CalcFitness() {
      for (int i=0; i<NumChrom; i++) {
         float fitness = 0.0f;
         for (int j=0; j<NumGenes; j++)  {
             if (GetGene(i, j)) fitness += 1.0f;
         }
         Fitness[i] = fitness;
      }
   }
   public void Print() {
      for (int i=0; i<2; i++) {
            System.out.print("Fitness for chromosome ");
            System.out.print(i);
            System.out.print(" is ");
            System.out.println(Fitness[i]);
      }
   }
}
