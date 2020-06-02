package io.exnihilo.validator.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Provides utility methods that can be used anywhere and ties to
 * perform common operations.
 */
public final class UtilsTest {

        @Test
        public void getNumberFromRegexMatcher_whenUserTriesInvalidPage_ThenFail() {
            assertEquals(22, Utils.getNumberFromRegexMatcher("line ", ",", new Exception("exception at line 22, column 21")));
        }
}