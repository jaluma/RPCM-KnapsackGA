package ga.knapsack.interfaces;

import java.io.IOException;

public interface Parser<TInstance> {
    public TInstance parse(String filePath) throws IOException;
}
