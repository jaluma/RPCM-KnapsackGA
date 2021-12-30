package ga.benchmarks;

import java.text.DecimalFormat;

public class BenchmarkData {
    private double[] config;
    private double fitness;
    private byte[] solution;
    private long time;
    private long iterations;
    private boolean success;

    public BenchmarkData(double[] config, double fitness, byte[] solution, long time, long iterations, boolean success) {
      this.config = config;
      this.fitness = fitness;
      this.solution = solution;
      this.time = time;
      this.iterations = iterations;
      this.success = success;
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
      return new String[] {
        String.valueOf(df.format(config[0])),
        String.valueOf(df.format(config[1])),
        String.valueOf(df.format(fitness)),
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
