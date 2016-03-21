package ar.fiuba.tdd.template.tp0;

import java.util.*;

import static ar.fiuba.tdd.template.tp0.Constants.*;


public class RegExParser {
    public RegExParser() {

    }

    private boolean isQuantifier(char character) {
        return (character == ZERO_OR_ONE || character == ZERO_OR_MORE || character == ONE_OR_MORE);
    }

    public Queue<String> parse(String regEx) throws InvalidRegExException {
        Queue<String> parsed = new LinkedList<>();
        for (int index = 0; index < regEx.length(); index++) {
            char character = regEx.charAt(index);
            if (character == OPEN_SET) {
                index = readSet(parsed, regEx, index);
            } else if (character == ESCAPED) {
                index = readEscaped(parsed, regEx, index);
            } else {
                index = readLiteral(parsed, regEx, index);
            }
        }
        return parsed;
    }
    //TODO: Exception when there is a quantifier non-escaped inside a set
    //TODO: Exception when there is a closing set square bracket, without being opened
    //TODO: Exception when there is an empty set
    //TODO: Allow escaped characters inside a set
    //This method doesn't allow that a '\]' is in the set
    private int readSet(Queue<String> parsed, String regEx, int index) throws InvalidRegExException {
        int indexOfSquareBracket = regEx.indexOf(CLOSE_SET, index);
        if (indexOfSquareBracket == -1) {
            throw new InvalidRegExException("The RegEx has open a set and it's never been closed.");
        }
        return readFromAndTo(parsed, regEx, index, indexOfSquareBracket + 1);
    }

    private int readEscaped(Queue<String> parsed, String regEx, int index) {
        return readFromAndTo(parsed, regEx, index, index + 2);
    }

    private int readLiteral(Queue<String> parsed, String regEx, int index) throws InvalidRegExException {
        if (isQuantifier(regEx.charAt(index))) {
            throw new InvalidRegExException("A RegEx 'field' cannot begin with a quantifier.");
        }
        return readFromAndTo(parsed, regEx, index, index + 1);
    }

    private int readFromAndTo(Queue<String> parsed, String regEx, int from, int to) {
        String regExField = regEx.substring(from, to);
        char possibleQuantifier = ' ';
        if (to < regEx.length()) {
            possibleQuantifier = regEx.charAt(to);
        }
        if (isQuantifier(possibleQuantifier)) {
            regExField += possibleQuantifier;
        } else {
            to--; // The last char read is the previous one
        }
        parsed.add(regExField);
        return to;
    }
}