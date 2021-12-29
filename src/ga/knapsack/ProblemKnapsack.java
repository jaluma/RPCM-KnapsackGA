package ga.knapsack;

import ga.ssGA.Individual;
import ga.ssGA.Problem;

public class ProblemKnapsack extends Problem {

    @Override
    public double Evaluate(Individual indiv) {
        double fitness=0.0;
        // TODO: Evaluate the fitness of the individual

        indiv.set_fitness(fitness);
        return fitness;
    }
    
}
