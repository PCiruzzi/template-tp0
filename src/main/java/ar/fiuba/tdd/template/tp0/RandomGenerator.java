package ar.fiuba.tdd.template.tp0;

import java.util.Queue;
import java.util.Random;

import static ar.fiuba.tdd.template.tp0.Constants.*;

public class RandomGenerator {

    private Queue<String> regEx;
    private int maxLength;

    public RandomGenerator(Queue<String> regEx, int maxLength) {
        this.regEx = regEx;
        this.maxLength = maxLength;
    }

    public String generate() {
        String matcher = "";
        int fieldsCount = 0;
        for (String regExField : this.regEx) {
            // It generates a random field of the regex of maximum size such that the next fields have space to
            // complete the regex (Having space to fill its field with at least 1 character).
            System.out.println("Generando field: " + regExField); //TODO: Clean
            matcher = matcher.concat(generateRandomField(regExField, maxLength - regEx.size() + fieldsCount));
            fieldsCount++;
        }
        return matcher;
    }

    private String generateRandomField(String regExField, int maxLength) {
        char first = regExField.charAt(0);
        int amountOfRepetitions = amountOfRepetitions(regExField.charAt(regExField.length() - 1), maxLength);
        System.out.println("Amount of repetitions: " + amountOfRepetitions); //TODO: Clean
        if (first == OPEN_SET) {
            return generateRandomFieldSet(regExField, amountOfRepetitions);
        } else if (first == ESCAPED) {
            return generateRandomFieldEscaped(regExField, amountOfRepetitions);
        } else {
            return generateRandomFieldLiteral(regExField, amountOfRepetitions);
        }
    }

    private String charToString(char character) {
        return new StringBuilder().append(character).toString();
    }

    private String generateRandomFieldSet(String regExField, int amountOfRepetitions) {
        int quantifier = hasQuantifier(regExField) ? 2 : 1;
        String dictionary = regExField.substring(1, regExField.length() - quantifier);
        String generated = "";
        for (int index = 0; index < amountOfRepetitions; index++) {
            char randomChar = dictionary.charAt(randomBetween(0, dictionary.length() - 1));
            String toConcatenate = charToString(randomChar);
            generated = generated.concat(toConcatenate);
        }
        System.out.println("Set field: " + generated); //TODO: Clean
        return generated;
    }

    private String generateRandomFieldEscaped(String regExField, int amountOfRepetitions) {
        String unEscaped = regExField.replace(ESCAPED_STR, ""); //Remove escape
        String character = charToString(unEscaped.charAt(0));
        String generated = "";
        for (int index = 0; index < amountOfRepetitions; index++) {
            generated = generated.concat(character);
        }
        System.out.println("Escaped/literal field: " + unEscaped); //TODO: Clean
        return generated;
    }

    private String generateRandomFieldLiteral(String regExField, int amountOfRepetitions) {
        String literal = charToString(regExField.charAt(0));
        System.out.println("Literal: " + literal); //TODO: Clean
        if (! literal.equals(".")) {
            return generateRandomFieldEscaped(regExField, amountOfRepetitions);
        } else {
            String generated = "";
            for (int index = 0; index < amountOfRepetitions; index++) {
                char randomChar = (char) randomBetween(MIN_LITERAL, MAX_LITERAL);
                String toConcatenate = charToString(randomChar);
                generated = generated.concat(toConcatenate);
            }
            System.out.println("Literal field: " + generated); //TODO: Clean
            return generated;
        }
    }

    private int amountOfRepetitions(char last, int maxLength) {
        switch (last) {
            case ZERO_OR_ONE: {
                return randomBetween(0, 1);
            }
            case ZERO_OR_MORE: {
                return randomBetween(0, maxLength);
            }
            case ONE_OR_MORE: {
                return randomBetween(1, maxLength);
            }
            default: {
                return 1;
            }
        }
    }

    private boolean hasQuantifier(String regExField) {
        char last = regExField.charAt(regExField.length() - 1);
        return (last == ZERO_OR_ONE || last == ZERO_OR_MORE || last == ONE_OR_MORE);
    }

    private int randomBetween(int min, int max) {
        Random random = new Random();
        return (random.nextInt(max - min + 1) + min);
    }
}
