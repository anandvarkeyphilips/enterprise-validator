package io.exnihilo.validator.util;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Provides utility methods that can be used anywhere and ties to
 * perform common operations.
 */
public final class UtilsTest{

        @Test
        public void testConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            Constructor<Utils> constructor = Utils.class.getDeclaredConstructor();
            assertTrue(Modifier.isPrivate(constructor.getModifiers()));
            constructor.setAccessible(true);
            constructor.newInstance();
        }

        @Test
        public void getNumberFromRegexMatcher_whenUserTriesInvalidPage_ThenFail() {
            assertEquals(22, Utils.getNumberFromRegexPattern("line ", ",", "exception at line 22, column 21"));
        }
}