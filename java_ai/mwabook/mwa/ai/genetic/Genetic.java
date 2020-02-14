// Genetic Algorithm Java classes
//
// Copyright 1996, Mark Watson.  All rights reserved.

package mwa.ai.genetic;

import java.util.*;

public class Genetic  extends Object {

   protected int NumGenes;  // number of genes per chromosome
   protected int NumChrom;  // number of chromosomes
   protected BitSet Genes[];
   protected float Fitness[];

   public Genetic() {
       System.out.println("In dummy Genetic constructor");
       NumGenes = NumChrom = 0;
   }

   public void init(int g, int c) {
       System.out.println("In Genetic::init(...)");
       Genes = new BitSet[c];
       for (int i=0; i<c; i++) {
          Genes[i] = new BitSet(g);
          for (int j=0; j<g; j++) {
             if (Math.random() < 0.5) Genes[i].set(j);
          }
       }
       NumChrom = c;
       NumGenes = g;
       Fitness = new float[c];
       for (int f=0; f<c; f++)         Fitness[f] = -999;
       Sort();
    }
    public boolean GetGene(int chromosome, int gene) {
       return Genes[chromosome].get(gene);
    }
    public void SetGene(int chromosome, int gene, int value) {
        if (value == 0)  Genes[chromosome].clear(gene);
        else             Genes[chromosome].set(gene);
    }
    public void SetGene(int chromosome, int gene, boolean value) {
        if (value)  Genes[chromosome].set(gene);
        else        Genes[chromosome].clear(gene);
    }
    public void Sort() {
       BitSet btemp;
       for (int c=0; c<NumChrom; c++) {
           for (int d=(NumChrom - 2); d>=c; d--) {
                if (Fitness[d] < Fitness[d+1]) {
                   btemp = Genes[d];
                   float x = Fitness[d];
                   Genes[d] = Genes[d+1];
                   Fitness[d] = Fitness[d+1];
                   Genes[d+1] = btemp;
                   Fitness[d+1] = x;
                }
            }
        }
    }
    public void DoCrossovers()  {
      for (int m=0; m<NumChrom/2; m++) {
          CopyGene(m + NumChrom/2, m);
      }

      // copy the 2 best genes best genes so that their
      // genetc material is replicated frequently:
      for (int i=0; i<NumGenes; i++) {
        SetGene(NumChrom - 1, i, GetGene(0, i));
        SetGene(NumChrom - 2, i, GetGene(0, i));
        SetGene(NumChrom - 3, i, GetGene(0, i));
        SetGene(NumChrom - 4, i, GetGene(1, i));
        SetGene(NumChrom - 5, i, GetGene(1, i));
      }

      int num = NumChrom / 4;
      for (int i=0; i<num; i++) {
        int c1 = 2+ (int)((NumChrom - 2) * Math.random() * 0.99);
        int c2 = 2 + (int)((NumChrom - 2) * Math.random() * 0.99);
        if (c1 != c2) {
           int locus = 2 + (int)((NumGenes - 3) * Math.random());
           for (int g=0; g<locus; g++) {
                 boolean temp = GetGene(c1, i);
                 SetGene(c1, i, GetGene(c2, i));
                 SetGene(c2, i, temp);
           }
        }
      }
    }
    // 'to' and 'from' are 'sorted' indices:
    private void CopyGene(int to, int from) {
       for (int i=0; i<NumGenes; i++)
            if (GetGene(from, i))  SetGene(to, i, 1);
            else                   SetGene(to, i, 0);
    }
    public void DoMutations() {
       int c = 2 + (int)((NumChrom - 2) * Math.random() * 0.95);
       int g = (int)(NumGenes * Math.random() * 0.95);
       if (GetGene(c, g))  SetGene(c, g, 0);
       else                SetGene(c, g, 1);
    }
    // Override the following function in sub-classes:
    public void CalcFitness() {
    }
}


