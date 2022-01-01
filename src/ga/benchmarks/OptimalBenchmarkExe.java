///////////////////////////////////////////////////////////////////////////////
///            Steady State Genetic Algorithm v1.0                          ///
///                by Enrique Alba, July 2000                               ///
///                                                                         ///
///   Executable: set parameters, problem, and execution details here       ///
///////////////////////////////////////////////////////////////////////////////

package ga.benchmarks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ga.knapsack.MKnap1Parser;
import ga.knapsack.ProblemKnapsack;
import ga.knapsack.interfaces.KnapsackInstance;
import ga.ssGA.Algorithm;
import ga.ssGA.Individual;
import ga.ssGA.Problem;
import java.io.PrintStream;

public class OptimalBenchmarkExe {
  public static void main(String args[]) throws Exception {
    MKnap1Parser parser = new MKnap1Parser();
    String name = "mknap1";
    List<KnapsackInstance> instances = parser.parse(args.length > 0 ? args[0] : String.format("resources/inputs/knapsack/%s.txt", name));
    if (instances == null) {
      System.out.println("Error: The file is not found, is empty or has an invalid format.");
      return;
    }

    double minPc = 0.5;
    double maxPc = 0.9;
    int iterPc = 3;

    double minAlphaPm = 0.8;
    double maxAlphaPm = 1.0;
    int iterAlphaPm = 3;

    // Population size
    int popsize = 512;

    // Maximum number of iterations
    long MAX_ISTEPS = 50000;
    int rt = 30;

    // int initialK = 5;
    // int finalK = initialK + 1;

    int initialK = 0;
    int finalK = instances.size();

    BenchmarkData[][][][] exportData = new BenchmarkData[instances.size()][rt][iterPc+1][iterAlphaPm+1];
    for (int k = initialK; k < finalK; k++) {
      for(int time = 0; time < rt; time++) {
        KnapsackInstance instance = instances.get(k);

        Problem problem; // The problem being solved
        problem = new ProblemKnapsack(instance);
  
        // PARAMETERS KNAPSACK
        // Gene number
        int gn = (int) instance.getItems();
  
        // Gene length
        int gl = 1; 
  
        for(int i = 0; i <= iterPc; i++) {
  
          // Crossover probability
          double pc = minPc + (maxPc - minPc) * i / iterPc; 
  
          for(int j = 0; j <= iterAlphaPm; j++) {
            // Mutation probability
            double pm = (minAlphaPm + (maxAlphaPm - minAlphaPm) * j / iterAlphaPm) / (double) ((double) gn * (double) gl); 
  
            // Target fitness being sought
            double tf = instance.getOptimal(); 
  
            problem.set_geneN(gn);
            problem.set_geneL(gl);
            problem.set_target_fitness(tf);
  
            long startTime = System.nanoTime();
  
            Algorithm ga; // The ssGA being used
            PrintStream[] out = new PrintStream[] { System.out, new PrintStream(new FileOutputStream("out/logs/" + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now()) +".txt", true)) };
            DecimalFormat formatter = new DecimalFormat("0.0000");
            String n =  + k + "-" + formatter.format(pc)  + "-" + formatter.format(pm);
            PrintStream ps = new PrintStream(new FileOutputStream("out/optimal/" + n + "-" + time + ".txt", false));
            List<Individual> solutions = new ArrayList<Individual>();
            ga = new Algorithm(problem, popsize, gn, gl, pc, pm);
  
            long elapsedTime = -1;
            long iterations = -1;
  
            for (int step = 0; step < MAX_ISTEPS; step++) {
              ga.go_one_step();
              solutions.add(ga.get_solution());
              if ((problem.tf_known()) &&
                  (ga.get_solution()).get_fitness() >= problem.get_target_fitness()) {
                elapsedTime = System.nanoTime() - startTime;
                iterations = step;
                break;
              }
            }
  
            if (elapsedTime == -1) {
              elapsedTime = System.nanoTime() - startTime;
              iterations = MAX_ISTEPS;
            }

            for(int s = 0; s < solutions.size(); s++) {
              ps.println((s + 1) + ": " + solutions.get(s).get_fitness());
            }
  
            if(ga.out != null) {
              ga.out.close();
            }
  
            Individual sol = ga.get_solution();
            byte[] solution = new byte[gn * gl];
            for (int s = 0; s < gn * gl; s++) {
              solution[s] = sol.get_allele(s);
            }
  
            exportData[k][time][i][j] = new BenchmarkData(
              time,
              new double[] { pc, pm },
              sol.get_fitness(),
              solution,
              elapsedTime,
              iterations + 1,
              iterations != MAX_ISTEPS
            );
            for(PrintStream p : out) {
              p.println("Instance: " + k + " Time: " + time + " pc: " + pc + " pm: " + pm + " Fitness: " + sol.get_fitness() + " Time: " + elapsedTime + " Iterations: " + iterations);
            }
            out[1].close();
          }
        }
      }
    }

    String exportFile = "out/optimal/benchmark-%s.csv";
    for (int k = 0; k < instances.size(); k++) {
      String instanceName = name + (k + 1);
      List<String[]> ret = new ArrayList<String[]>();
      for (int time = 0; time < exportData[k].length; time++) {
        for (int i = 0; i < exportData[k][time].length; i++) {
          for (int j = 0; j < exportData[k][time][i].length; j++) {
            BenchmarkData data = exportData[k][time][i][j];
            if (data != null) {
              ret.add(data.toArray());
            }
          }
        }
      }
      String fileName = String.format(exportFile, instanceName);

      // Create directory and file if not exists
      File directory = new File(fileName).getParentFile();
      if (!directory.exists()) {
        directory.mkdirs();
      }

      if(saveCsvData(fileName, ret)) {
        System.out.println(String.format("Done. Results exported to out/benchmark-%s.csv", instanceName));
      }
    }
  }

  public static boolean saveCsvData(String filename, List<String[]> data) throws IOException {
    File csvOutputFile = new File(filename);
    try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
        data.stream()
          .map(OptimalBenchmarkExe::convertToCSV)
          .forEach(pw::println);
    }
    return csvOutputFile.exists();
}

  private static String convertToCSV(String[] data) {
    if(data.length == 0) {
      return "";
    }

    return Stream.of(data)
      .map(OptimalBenchmarkExe::escapeSpecialCharacters)
      .collect(Collectors.joining(","));
  }

  private static String escapeSpecialCharacters(String data) {
    String escapedData = data.replaceAll("\\R", " ");
    if (data.contains(",") || data.contains("\"") || data.contains("'")) {
        data = data.replace("\"", "\"\"");
        escapedData = "\"" + data + "\"";
    }
    return escapedData;
  }
}
// END OF CLASS: Exe
