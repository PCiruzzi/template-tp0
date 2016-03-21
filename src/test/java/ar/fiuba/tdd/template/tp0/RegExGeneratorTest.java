package ar.fiuba.tdd.template.tp0;

import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ar.fiuba.tdd.template.tp0.Constants.*;

import static org.junit.Assert.assertTrue;

public class RegExGeneratorTest {

    private boolean validate(String regEx, int numberOfResults) throws InvalidRegExException, InvalidAmountOfResultsException {
        return validate(regEx, numberOfResults, DEFAULT_RESULTS);
    }

    private boolean validate(String regEx, int numberOfResults, int maxLength)
            throws InvalidRegExException, InvalidAmountOfResultsException {
        RegExGenerator generator = new RegExGenerator(maxLength);
        List<String> results = generator.generate(regEx, numberOfResults);
        // force matching the beginning and the end of the strings
        Pattern pattern = Pattern.compile("^" + regEx + "$", Pattern.UNICODE_CHARACTER_CLASS);
        return results
                .stream()
                .reduce(true,
                    (acc, item) -> {
                        Matcher matcher = pattern.matcher(item);
                        return acc && matcher.find();
                    },
                    (item1, item2) -> item1 && item2);
    }

    @Test
    public void testAnyCharacter() {
        try {
            assertTrue(validate(".", 1));
        } catch (InvalidRegExException | InvalidAmountOfResultsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testMultipleCharacters() {
        try {
            assertTrue(validate("...", 1));
        } catch (InvalidRegExException | InvalidAmountOfResultsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testLiteral() {
        try {
            assertTrue(validate("\\@", 1));
        } catch (InvalidRegExException | InvalidAmountOfResultsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testLiteralDotCharacter() {
        try {
            assertTrue(validate("\\@..", 1));
        } catch (InvalidRegExException | InvalidAmountOfResultsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testZeroOrOneCharacter() {
        try {
            assertTrue(validate("\\@.h?", 1));
        } catch (InvalidRegExException | InvalidAmountOfResultsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testCharacterSet() {
        try {
            assertTrue(validate("[abc]", 1));
        } catch (InvalidRegExException | InvalidAmountOfResultsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testCharacterSetWithQuantifiers() {
        try {
            assertTrue(validate("[abc]+", 1));
        } catch (InvalidRegExException | InvalidAmountOfResultsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testEscapedQuantifier() {
        try {
            assertTrue(validate("\\+*.", 1));
        } catch (InvalidRegExException | InvalidAmountOfResultsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testEscapedQuantifiersTogether() {
        try {
            assertTrue(validate("\\+\\*.", 1));
        } catch (InvalidRegExException | InvalidAmountOfResultsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testZeroOrMoreCharacters() {
        try {
            assertTrue(validate("ab*.", 1));
        } catch (InvalidRegExException | InvalidAmountOfResultsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testBackslashDot() {
        try {
            assertTrue(validate("a\\.+", 1));
        } catch (InvalidRegExException | InvalidAmountOfResultsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testEscapedSetCharacters() {
        try {
            assertTrue(validate("\\[hola\\]", 1));
        } catch (InvalidRegExException | InvalidAmountOfResultsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testDotsQuantifiers() {
        try {
            assertTrue(validate(".*&&.?@@.+", 10));
        } catch (InvalidRegExException | InvalidAmountOfResultsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testLiteralsQuantifiers() {
        try {
            assertTrue(validate("\\.*&+\\[?", 10));
        } catch (InvalidRegExException | InvalidAmountOfResultsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testOnlyEscapedQuantifier() {
        try {
            assertTrue(validate("\\?", 1));
        } catch (InvalidRegExException | InvalidAmountOfResultsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testIntegration() {
        try {
            assertTrue(validate("ab*.+\\[\\+\\*?[cde]+.[fg]", 3));
        } catch (InvalidRegExException | InvalidAmountOfResultsException e) {
            assertTrue(false);
        }
    }

    // Exceptions tests

    @Test
    public void testAmountException() {
        try {
            validate("a", 2);
            assertTrue(false); //In case an exception isn't thrown
        } catch (InvalidRegExException e) {
            assertTrue(false);
        } catch (InvalidAmountOfResultsException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testOpenSetWithoutCloseException() {
        try {
            validate("[abc.", 1);
            assertTrue(false); //In case an exception isn't thrown
        } catch (InvalidRegExException e) {
            assertTrue(true);
        } catch (InvalidAmountOfResultsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testQuantifiersTogether() {
        try {
            validate("ab+*", 1);
            assertTrue(false); //In case an exception isn't thrown
        } catch (InvalidRegExException e) {
            assertTrue(true);
        } catch (InvalidAmountOfResultsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testOnlyQuantifier() {
        try {
            validate("+", 1);
            assertTrue(false); //In case an exception isn't thrown
        } catch (InvalidRegExException e) {
            assertTrue(true);
        } catch (InvalidAmountOfResultsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testEmptySet() {
        try {
            validate("[]", 1);
            assertTrue(false); //In case an exception isn't thrown
        } catch (InvalidRegExException e) {
            assertTrue(true);
        } catch (InvalidAmountOfResultsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testQuantifierUnescapedInSet() {
        try {
            validate("a[bc*]d", 1);
            assertTrue(false); //In case an exception isn't thrown
        } catch (InvalidRegExException e) {
            assertTrue(true);
        } catch (InvalidAmountOfResultsException e) {
            assertTrue(false);
        }
    }

    @Test
    public void testEscapedCharactersInSet() {
        try {
            assertTrue(validate("a[bc\\*\\@]d", 1));
        } catch (InvalidRegExException | InvalidAmountOfResultsException e) {
            assertTrue(false);
        }
    }
}
