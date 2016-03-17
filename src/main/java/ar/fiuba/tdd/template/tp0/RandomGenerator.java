package ar.fiuba.tdd.template.tp0;

import java.util.Queue;

public class RandomGenerator {

    private Queue<String> regEx;
    private int maxLength;

    public RandomGenerator(Queue<String> regEx, int maxLength) {
        this.regEx = regEx;
        this.maxLength = maxLength;
    }

    public String generate() {
        for (String regExField : this.regEx) {

        }
        return "hola";
    }
}
