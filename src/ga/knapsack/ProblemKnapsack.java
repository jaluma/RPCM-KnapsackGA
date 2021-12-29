package ga.knapsack;

import ga.knapsack.interfaces.KnapsackInstance;
import ga.ssGA.Individual;
import ga.ssGA.Problem;

/**
 *  The problem to be solved is:
 *    Max  sum{j=1,...,n} p(j)x(j)
 *    st   sum{j=1,...,n} r(i,j)x(j) <= b(i)       i=1,...,m
 *                        x(j)=0 or 1
 * 
 * @author  Javier
 */
public class ProblemKnapsack extends Problem {

    private KnapsackInstance instance;
    public ProblemKnapsack(KnapsackInstance instance) {
        super();
        this.instance = instance;
    }

    @Override
    public double Evaluate(Individual indiv) {
        double fitness=0.0;
        // TODO: Calculating the fitness of the individual

        indiv.set_fitness(fitness);
        return fitness;
    }
    
}
