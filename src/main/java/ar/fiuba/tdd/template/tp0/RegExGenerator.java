package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class RegExGenerator {
    private int maxLength;

    public RegExGenerator(int maxLength) {
        this.maxLength = maxLength;
    }

    public List<String> generate(String regEx, int numberOfResults) throws InvalidRegExException, InvalidAmountOfResultsException {
        Queue<String> parsed = new RegExParser().parse(regEx);
        List<String> results = new ArrayList<>();
        int index = 0;
        int amountOfSameResults = 0;
        while (index < numberOfResults) {
            String result = this.generateRandom(parsed);
            if (! results.contains(result) && results.add(result) ) {
                index++;
                amountOfSameResults = 0;
            } else {
                amountOfSameResults++;
            }
            if (amountOfSameResults >= 20) {
                throw new InvalidAmountOfResultsException("There were generated 20 results and they all had been the same.");
            }
            System.out.println("Resultado: " + result);
        }
        return results;
    }

    private String generateRandom(Queue<String> regEx) throws InvalidRegExException {
        RandomGenerator generator = new RandomGenerator(regEx, this.maxLength);
        return generator.generate();
    }
}