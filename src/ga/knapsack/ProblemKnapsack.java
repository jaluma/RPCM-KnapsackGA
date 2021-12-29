package ga.knapsack;

import java.util.Arrays;

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
        double fitness = 0.0;
        for (int j = 0; j < instance.getItems(); j++) {
            if(indiv.get_allele(j) == 1) {
                fitness += instance.getProfit(j);
            }
        }

        double[] totalWeight = new double[(int) instance.getConstraints()];
        int overWeightLimit = 0;

        for (int i = 0; i < instance.getConstraints(); i++) {
            for (int j = 0; j < instance.getItems(); j++) {
                if(indiv.get_allele(j) == 1) {
                    totalWeight[i] += instance.getWeight(i, j);
                }
            }
            if(totalWeight[i] > instance.getRhs(i)) {
                overWeightLimit++;
            }
        }

        if(overWeightLimit > 0) {
            fitness = 0.0;
        }

        indiv.set_fitness(fitness);
        return fitness;
    }
    
}
