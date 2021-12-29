package ga.knapsack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ga.knapsack.interfaces.KnapsackInstance;
import ga.knapsack.interfaces.Parser;

public class MKnap1Parser implements Parser<List<KnapsackInstance>> {

    /**
     * Parses a file containing a knapsack instance.
     *  The format of this data file is:
        number of test problems (K)
        then for each test problem k (k=1,...,K) in turn:
            number of variables (n), number of constraints (m), optimal solution value (zero if unavailable)
            the coefficients p(j); j=1,...,n
            for each constraint i (i=1,...,m): the coefficients r(i,j); j=1,...,n
            the constraint right-hand sides b(i); i=1,...,m
     * 
     * @param filePath
     *            The path to the file containing the knapsack instance.
     * @return A list containing the knapsack instance or  null if the file is not found, is empty or has an invalid format.
     * @throws IOException
     *             If the file cannot be read.
     */
    @Override
    public List<KnapsackInstance> parse(String filePath) {
        String[] data = loadFile(filePath);
        if (data == null) {
            return null;
        }

        long testProblems = Long.parseLong(data[0].trim());
        int currentLine = 2;
        List<KnapsackInstance> instances = new ArrayList<KnapsackInstance>();
        for (int block = 0; block < testProblems; block++) {

            // First line
            String[] firstLine = splitLine(data[currentLine]);
            long n = Long.parseLong(firstLine[0]);
            long m = Long.parseLong(firstLine[1]);
            Double parsed = firstLine.length >= 3 ? tryParse(firstLine[2]) : null;
            double optimalValue = parsed != null && parsed > 0 ? parsed : -1;  // If the optimal value is not available, it is set to -1

            currentLine++;

            // Second line
            double[] coefficients = splitLineToDouble(data[currentLine]);
            if (coefficients.length != n) {
                System.err.println("Error: The number of coefficients is not equal to the number of variables.");
                return null;
            }

            currentLine++;

            // Next lines until the end of the block less one
            double[][] constraints = new double[(int)m][(int)n];
            for(int i = 0; i < m; i++) {
                for(int j = 0; j < n; j++) {
                    constraints[i][j] = splitLineToLong(data[currentLine])[j];
                }
                currentLine++;
            }

            // Last line
            double[] rightHandSides = splitLineToDouble(data[currentLine]);
            if(rightHandSides.length != m) {
                System.out.println("Error: The number of right-hand sides is not equal to the number of constraints.");
                return null;
            }

            KnapsackInstance instance = new KnapsackInstanceImpl(n, m, optimalValue, coefficients, constraints, rightHandSides);
            instances.add(instance);

            currentLine += 2;
        }

        if (instances.size() != testProblems) {
            System.err.println("Error: The number of instances is not equal to the number of test problems.");
            return null;
        }

        return instances;
    }

    // Load file and return a string array with the content of the file line by line
    private String[] loadFile(String filePath) {
        String[] fileContent;
        try {
            fileContent = Files.readAllLines(Paths.get(filePath)).toArray(new String[0]);
        } catch (IOException e) {
            System.out.println("File not found");
            return null;
        }
        return fileContent;
    }

    // Split line using space as delimiter
    private String[] splitLine(String line) {
        String[] lineSplit = line.split(" ");
        return Arrays.stream(lineSplit)
            .map(x -> x.trim().replace(",", "."))
            .filter(x -> x != null && x.length() != 0)
            .toArray(String[]::new);
    }

    private long[] splitLineToLong(String line) {
        return Arrays.stream(splitLine(line))
        .map(x -> x.trim())
        .filter(x -> x != null && x.length() != 0)
        .mapToLong(Long::parseLong).toArray();
    }

    private double[] splitLineToDouble(String line) {
        return Arrays.stream(splitLine(line))
            .map(x -> x.trim().replace(",", "."))
            .filter(x -> x != null && x.length() != 0)
            .mapToDouble(Double::parseDouble).toArray();
    }
    
    private Double tryParse(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
