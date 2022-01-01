package ga.benchmarks;

import java.text.DecimalFormat;
import java.util.Arrays;

public class BenchmarkData {
    private int iteration;
    private double[] config;
    private double fitness;
    private byte[] solution;
    private long time;
    private long iterations;
    private boolean success;

    public BenchmarkData(int iteration, double[] config, double fitness, byte[] solution, long time, long iterations, boolean success) {
      this.iteration = iteration;
      this.config = config;
      this.fitness = fitness;
      this.solution = solution;
      this.time = time;
      this.iterations = iterations;
      this.success = success;
    }

    public int getIteration() {
      return iteration;
    }

    public double[] getConfig() {
      return config;
    }

    public double getFitness() {
      return fitness;
    }

    public byte[] getSolution() {
      return solution;
    }

    public long getTime() {
      return time;
    }

    public long getIterations() {
      return iterations;
    }

    public boolean isSuccess() {
      return success;
    }

    private static final DecimalFormat df = new DecimalFormat("0.0000");

    // Create a tabulated array string representation of the data
    public String[] toArray() {
      String solution = "";
      for(int i = 0; i < this.solution.length; i++) {
        solution += this.solution[i];
      }
      
      return new String[] {
        String.valueOf(df.format(config[0])),
        String.valueOf(df.format(config[1])),
        String.valueOf(solution),
        String.valueOf(fitness),
        String.valueOf(time),
        String.valueOf(iterations),
        String.valueOf(success)
      };
    }

    @Override
    public String toString() {
      return String.format("%f,%f,%f,%d,%d,%b",
        config[0], config[1], fitness, time, iterations, success);
    }
}
