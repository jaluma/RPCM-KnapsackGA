package ga.knapsack;

import ga.knapsack.interfaces.KnapsackInstance;

public class KnapsackInstanceImpl implements KnapsackInstance {
    public long n;
    public long m;
    public double optimal;
    public double[] coefficients;
    public double[][] constraints;
    public double[] rhs;

    public KnapsackInstanceImpl(long n, long m, double optimal, double[] coefficients, double[][] constraints, double[] rhs) {
        this.n = n;
        this.m = m;
        this.optimal = optimal;
        this.coefficients = coefficients;
        this.constraints = constraints;
        this.rhs = rhs;
    }

    public double getProfit(int i) {
        return coefficients[i];
    }

    public double getWeight(int i, int j) {
        return constraints[i][j];
    }

    public long getItems() {
        return n;
    }

    public long getConstraints() {
        return m;
    }

    public double getOptimal() {
        return optimal;
    }   

    public double getRhs(int i) {
        return rhs[i];
    }   
}
