package ar.fiuba.tdd.template.tp0;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class RegExGenerator {
    private int maxLength;

    public RegExGenerator(int maxLength) {
        this.maxLength = maxLength;
    }

    public List<String> generate(String regEx, int numberOfResults) throws InvalidRegExException {
        Queue<String> parsed = new RegExParser().parse(regEx);
        for (String parseado : parsed) { //TODO: Clean
            System.out.println("Parseado: " + parseado);
        }


        List<String> results = new ArrayList<>();
        int index = 0;
        while (index < numberOfResults) {
            String result = this.generateRandom(parsed);
            if (! results.contains(result) && results.add(result) ) {
                index++;
            }
            //TODO: If it repeats n times, it means that there are no much possibilities
            System.out.println("Resultado: " + result);
        }
        return results;
    }

    private String generateRandom(Queue<String> regEx) {
        RandomGenerator generator = new RandomGenerator(regEx, this.maxLength);
        return generator.generate();
    }
}