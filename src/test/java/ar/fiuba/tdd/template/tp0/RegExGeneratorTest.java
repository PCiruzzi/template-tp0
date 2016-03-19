package ar.fiuba.tdd.template.tp0;

import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ar.fiuba.tdd.template.tp0.Constants.*;

import static org.junit.Assert.assertTrue;

public class RegExGeneratorTest {

    private boolean validate(String regEx, int numberOfResults) throws InvalidRegExException {
        return validate(regEx, numberOfResults, DEFAULT_RESULTS);
    }

    private boolean validate(String regEx, int numberOfResults, int maxLength) throws InvalidRegExException {
        RegExGenerator generator = new RegExGenerator(maxLength);
        List<String> results = generator.generate(regEx, numberOfResults);
        // force matching the beginning and the end of the strings
        Pattern pattern = Pattern.compile("^" + regEx + "$");
        return results
                .stream()
                .reduce(true,
                    (acc, item) -> {
                        Matcher matcher = pattern.matcher(item);
                        return acc && matcher.find();
                    },
                    (item1, item2) -> item1 && item2);
    }

    //TODO: Uncomment these tests

    @Test
    public void testAnyCharacter() {
        try {
            assertTrue(validate(".", 1));
        } catch (InvalidRegExException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testMultipleCharacters() {
        try {
            assertTrue(validate("...", 1));
        } catch (InvalidRegExException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testLiteral() {
        try {
            assertTrue(validate("\\@", 1));
        } catch (InvalidRegExException e) {
            assertTrue(false);
        }
    }
/*
    @Test
    public void testLiteralDotCharacter() {
        assertTrue(validate("\\@..", 1));
    }

    @Test
    public void testZeroOrOneCharacter() {
        assertTrue(validate("\\@.h?", 1));
    }

    @Test
    public void testCharacterSet() {
        assertTrue(validate("[abc]", 1));
    }

    @Test
    public void testCharacterSetWithQuantifiers() {
        assertTrue(validate("[abc]+", 1));
    }
    */
    // TODO: Add more tests!!!

//    @Test
//    public void testGeneral() {
//        try {
//            assertTrue(validate("ab*.+\\h\\+[cde]?.[fg]", 3));
//        } catch (InvalidRegExException e) {
//            assertTrue(false);
//        }
//    }
}
