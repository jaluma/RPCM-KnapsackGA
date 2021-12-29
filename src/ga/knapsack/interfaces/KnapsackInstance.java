package ga.knapsack.interfaces;

public interface KnapsackInstance extends Instance {
    public double getProfit(int j);
    public double getWeight(int i, int j);
    public long getItems();
    public long getConstraints();
    public double getOptimal();
    public double getRhs(int i);
}
