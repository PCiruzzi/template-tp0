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

    public String generate() throws InvalidRegExException {
        String matcher = "";
        int fieldsCount = 0;
        for (String regExField : this.regEx) {
            // It generates a random field of the regex of maximum size such that the next fields have space to
            // complete the regex (Having space to fill its field with at least 1 character).
            int maxFieldLength = maxLength - matcher.length() - regEx.size() + fieldsCount;
            System.out.println("");
            System.out.println("Generating field: " + regExField); //TODO: Clean
            System.out.println("maxFieldLength: " + maxFieldLength);
            matcher = matcher.concat(generateRandomField(regExField, maxFieldLength));
            fieldsCount++;
        }
        return matcher;
    }

    private String generateRandomField(String regExField, int maxLength) throws InvalidRegExException {
        int amountOfRepetitions = amountOfRepetitions(regExField, maxLength);
        System.out.println("Amount of repetitions: " + amountOfRepetitions); //TODO: Clean
        char first = regExField.charAt(0);
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

    private String generateRandomFieldSet(String regExField, int amountOfRepetitions) throws InvalidRegExException {
        int quantifier = hasQuantifier(regExField) ? 2 : 1;
        String dictionary = getSetDictionary(regExField, quantifier);
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
        System.out.println("Escaped/literal field: " + generated); //TODO: Clean
        return generated;
    }

    private String generateRandomFieldLiteral(String regExField, int amountOfRepetitions) {
        String literal = charToString(regExField.charAt(0));
        if (! literal.equals(".")) {
            return generateRandomFieldEscaped(regExField, amountOfRepetitions);
        } else {
            String generated = generateDot(amountOfRepetitions);
            System.out.println("Literal field: " + generated); //TODO: Clean
            return generated;
//            return generateDot(amountOfRepetitions);
        }
    }

    private String generateDot(int amountOfRepetitions) {
        String generated = "";
        for (int index = 0; index < amountOfRepetitions; index++) {
            int randomInt = randomBetween(MIN_LITERAL, MAX_LITERAL);
            // Excludes line feed and carriage return characters
            if (randomInt == 10 || randomInt == 13 || randomInt == 133) {
                index--;
                continue;
            }
            char randomChar = (char) randomInt;
            String toConcatenate = charToString(randomChar);
            generated = generated.concat(toConcatenate);
        }
        return generated;
    }

    private int amountOfRepetitions(String regExField, int maxLength) {
        char last = getLast(regExField);
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

    private char getLast(String regExField) {
        char last;
        if (isQuantifierEscaped(regExField) && regExField.length() == 2) {
            last = '\\'; // Anything but a quantifier
        } else {
            last = regExField.charAt(regExField.length() - 1);
        }
        return last;
    }

    private boolean isQuantifier(char character) {
        return (character == ZERO_OR_ONE || character == ZERO_OR_MORE || character == ONE_OR_MORE);
    }

    private boolean isQuantifierEscaped(String regExField) {
        if (regExField.length() == 1) {
            return false;
        }
        char escapedChar = regExField.charAt(1);
        return (regExField.charAt(0) == ESCAPED && isQuantifier(escapedChar));
    }

    private boolean hasQuantifier(String regExField) {
        char last = regExField.charAt(regExField.length() - 1);
        return (last == ZERO_OR_ONE || last == ZERO_OR_MORE || last == ONE_OR_MORE);
    }

    private int randomBetween(int min, int max) {
        Random random = new Random();
        return (random.nextInt(max - min + 1) + min);
    }

    private String getSetDictionary(String regExField, int quantifier) throws InvalidRegExException {
        String set = regExField.substring(1, regExField.length() - quantifier);
        boolean isEscaped = false;
        String dictionary = "";
        dictionary = makeDictionary(set, isEscaped, dictionary);
        if (dictionary.length() == 0) {
            throw new InvalidRegExException("The set is empty.");
        }
        return dictionary;
    }

    private String makeDictionary(String set, boolean isEscaped, String dictionary) throws InvalidRegExException {
        for (int index = 0; index < set.length(); index++) {
            char character = set.charAt(index);
            if (isQuantifier(character) && ! isEscaped) {
                throw new InvalidRegExException("The set includes a quantifier that isn't escaped.");
            }
            if (character == '\\') {
                isEscaped = true;
            } else {
                String toConcatenate = charToString(character);
                dictionary = dictionary.concat(toConcatenate);
                isEscaped = false;
            }
        }
        return dictionary;
    }
}