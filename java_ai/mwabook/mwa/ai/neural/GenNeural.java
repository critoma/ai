// GenNeural class: parses neural network input files
// using the class NNfile and uses a genetic algorithm
// to optimize both the structure and training data for
// a neural network.

// Copyright 1996, Mark Watson

package mwa.ai.neural;


public class GenNeural extends NNgreedy {

  Neural MyNeural = null;

  // Increasing the GAINtraining reduces the number of training
  // cases that are discarded:
  static public float GAINtraining = 3.4f;

  // Increasing GAINinputs reduces the number of
  // input neurons that are ignored:
  static public float GAINinputs = 2.5f;

  public GenNeural() {
     System.out.println("\n\nError: GenNeural constructor " +
                        "requires a file name.\n");
  }

  public GenNeural(String input_file) {
    MyNeural = new Neural(input_file);
    NeuralFile = MyNeural.NeuralFile;
  }

  public void Save(String file) {
    NeuralFile.Save(file);
  }

  void RemoveTrainingData() {
    if (NeuralFile==null) return;
    // we need to refresh the active training cases from
    // the NNfile object into the Neural object:
    MyNeural.LoadTrainingCases();

    // Build a chromosome containing one bit for
    // each training case:
    MyNeural.IgnoreTraining = new boolean[NeuralFile.NumTraining];
    MyNeural.IgnoreInput=null;
    NNTrainingGenetic g = new NNTrainingGenetic();
    g.init(NeuralFile.NumTraining, 20, MyNeural);

    // The fitness functon is determined by:
    //
    //    How low the training error is
    //    Maximize # of training cases (i.e., we
    //    really want to avoid tossing out training
    //    cases unless they adversely affect the
    //    final training error):

    // Remove any bad training cases from the
    // NNfile object:

    for (int i=0; i<16; i++) {
       g.CalcFitness();
       g.Sort();
       if ((i%5)==0) {
           System.out.println("Generation " + i);
           g.Print();
       }
       g.DoCrossovers();
       g.DoMutations();
       g.DoMutations();
       g.DoMutations();
       g.DoMutations();
       g.DoMutations();
       g.DoMutations();
    }
    int num = NeuralFile.NumTraining;
    for (int i=num-1; i>=0; i--) {  // remove in reverse order!!!
       if (g.GetGene(0, i)) {
          System.out.println("Removing training case # " + i);
          NeuralFile.RemoveTraining(i);
       }
    }
    NeuralFile.Save("test_gen.trn");
  }

  void ChangeStructure() {
    if (NeuralFile==null) return;
    if (NeuralFile.NumHidden < 15) {
        System.out.println("WARNING: GenNeural.ChangeStructure " +
                           "requires a network with room for at");
        System.out.println("least 15 neurons.\n\n");
        return;
    }

    // Build a chromosome containing one bit for each
    // input neuron, and 4 bits to encode an
    // optimal number of hidden layer neurons:

    MyNeural.IgnoreTraining = null;
    MyNeural.IgnoreInput = new boolean[MyNeural.NumInputs];
    NNStructureGenetic g = new NNStructureGenetic();
    g.init(NeuralFile.NumInput + 4, 20, MyNeural);

    for (int i=0; i<16; i++) {
       g.CalcFitness();
       g.Sort();
       if ((i%5)==0) {
           System.out.println("Generation " + i);
           g.Print();
       }
       g.DoCrossovers();
       g.DoMutations();
       g.DoMutations();
       g.DoMutations();
       g.DoMutations();
       g.DoMutations();
       g.DoMutations();
    }

    // When done, add 'special' application-specific
    // data values to mark input neurons as "don't
    // care" inputs which are ignored, and modify the
    // parameter specifying the number of hidden layer
    // neurons:

    int numh = 0;
    int POW2[] = {1, 2, 4, 8};
    for (int i=0; i<4; i++) {
       if (g.GetGene(0, NeuralFile.NumInput + i)) {
         numh += POW2[i];
       }
    }
    NeuralFile.NumHidden = numh;
    System.out.println("Reset the number of hidden neurons to " + numh);

    for (int i=0; i<NeuralFile.NumInput; i++) {
       if (g.GetGene(0, i)) {
          System.out.println("Disabling input neuron " + i);
          NeuralFile.AddSpecial((float)i);
       }
    }
    NeuralFile.Save("test_gen.inp");
  }

}

class NNTrainingGenetic extends mwa.ai.genetic.Genetic {
   Neural MyNeural;

   NNTrainingGenetic() {
       System.out.println("Entered NNTrainingGenetic()\n");
   }
   public void init(int g, int c, Neural neural) {
       super.init(g, c);
       for (int j=0; j<g; j++)
         for (int i=0; i<c; i++)
            SetGene(i, j, false);
       MyNeural = neural;
   }
   public void CalcFitness() {
      for (int i=0; i<NumChrom; i++) {
         float fitness = 40.0f;
         int OnCount = 0;
         for (int j=0; j<NumGenes; j++) {
             MyNeural.IgnoreTraining[j] = GetGene(i, j);
             if (GetGene(i, j)) OnCount++;
         }
         for (int iter=0; iter<500; iter++) {
            float x = MyNeural.Train();
            if (iter > 460)  fitness -= x;
         }
         Fitness[i] =
            fitness - GenNeural.GAINtraining * (float)OnCount;
      }
   }
   public void Print() {
      for (int i=0; i<NumChrom; i++) {
            System.out.print("Fitness for chromosome ");
            System.out.print(i);
            System.out.print(" is ");
            System.out.print(Fitness[i] + ": ");
            for (int j=0; j<NumGenes; j++) {
               if (GetGene(i, j)) System.out.print("1 ");
               else               System.out.print("0 ");
            }
            System.out.println("");
      }
   }
}


class NNStructureGenetic extends mwa.ai.genetic.Genetic {
   Neural MyNeural;

   NNStructureGenetic() {
       System.out.println("Entered NNStructureGenetic()\n");
   }
   public void init(int g, int c, Neural neural) {
       super.init(g, c);
       for (int i=0; i<g; i++) {
         for (int j=0; j<c; j++) {
            if (i < (g - 4)) SetGene(j, i, false);
            else             SetGene(j, i, true); // # hidden neurons
         }
       }
       MyNeural = neural;
   }
   public void CalcFitness() {
      for (int i=0; i<NumChrom; i++) {
         float fitness = 100.0f;

         int numh = 0;
         int POW2[] = {1, 2, 4, 8};
         for (int j=NumGenes-4; j<NumGenes; j++) {
            if (GetGene(i, j)) {
              numh += POW2[j - (NumGenes - 4)];
            }
         }
         MyNeural.NumHidden = numh;
         MyNeural.randomizeWeights();

         int OnCount = 0;
         for (int j=0; j<MyNeural.NumInputs; j++) {
             MyNeural.IgnoreInput[j] = GetGene(i, j);
             if (GetGene(i, j)) OnCount++;
         }
         for (int iter=0; iter<500; iter++) {
            float x = MyNeural.Train();
            if (iter > 460)  fitness -= x;
         }
         Fitness[i] = fitness - 30.0f * (float)numh
                                       - GenNeural.GAINinputs * OnCount;
         if (numh < 3) Fitness[i] -= 100000.0f;
      }
   }
   public void Print() {
      for (int i=0; i<NumChrom; i++) {
            System.out.print("Fitness for chromosome ");
            System.out.print(i);
            System.out.print(" is ");
            System.out.print(Fitness[i] + ": ");
            for (int j=0; j<NumGenes-4; j++) {
               if (GetGene(i, j)) System.out.print("1 ");
               else               System.out.print("0 ");
            }
            System.out.print(" | ");

           // hard-coded to allow 2^4 = 16 hidden neurons:
           for (int j=NumGenes-4; j<NumGenes; j++) {
              if (GetGene(i, j)) System.out.print("1 ");
              else               System.out.print("0 ");
           }
           System.out.println("");
      }
   }
}
