///////////////////////////////////////////////////////////////////////////////
///            Steady State Genetic Algorithm v1.0                          ///
///                by Enrique Alba, July 2000                               ///
///                                                                         ///
///   Executable: set parameters, problem, and execution details here       ///
///////////////////////////////////////////////////////////////////////////////

package ga.knapsack;

import java.util.List;

import ga.knapsack.interfaces.KnapsackInstance;
import ga.ssGA.Algorithm;
import ga.ssGA.Problem;

public class CustomExe {
  public static void main(String args[]) throws Exception {
    MKnap1Parser parser = new MKnap1Parser();
    List<KnapsackInstance> instances = parser.parse(args.length > 0 ? args[0] : "resources/inputs/knapsack/mknap1.txt");
    if (instances == null) {
      System.out.println("Error: The file is not found, is empty or has an invalid format.");
      return;
    }

    for (KnapsackInstance instance : instances) {
      Problem problem; // The problem being solved
      problem = new ProblemKnapsack(instance);

      // PARAMETERS KNAPSACK

      // Gene number
      int gn = (int) instance.getItems(); 
      // Gene length
      int gl = 1; 

      // Population size
      int popsize = 512; 

      // Crossover probability
      double pc = 0.8; 

      // Mutation probability
      double pm = 1.0 / (double) ((double) gn * (double) gl); 

      // Target fitness being sought
      double tf = instance.getOptimal(); 

      // Maximum number of iterations
      long MAX_ISTEPS = 50000;

      problem.set_geneN(gn);
      problem.set_geneL(gl);
      problem.set_target_fitness(tf);

      Algorithm ga; // The ssGA being used
      ga = new Algorithm(problem, popsize, gn, gl, pc, pm);

      for (int step = 0; step < MAX_ISTEPS; step++) {
        ga.go_one_step();
        System.out.print(step);
        System.out.print("  ");
        System.out.println(ga.get_bestf());

        if ((problem.tf_known()) &&
            (ga.get_solution()).get_fitness() >= problem.get_target_fitness()) {
          System.out.print("Solution Found! After ");
          System.out.print(problem.get_fitness_counter());
          System.out.println(" evaluations");
          break;
        }

      }

      // Print the solution
      for (int i = 0; i < gn * gl; i++)
        System.out.print((ga.get_solution()).get_allele(i));
      System.out.println();
      System.out.println((ga.get_solution()).get_fitness());
    }
  }
}
// END OF CLASS: Exe
