package io.exnihilo.validator.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides utility methods that can be used anywhere and ties to
 * perform common operations.
 */
public final class Utils {
    private Utils(){}

    public static int getNumberFromRegexMatcher(String pattern1, String pattern2, Exception e) {
        int number = 0;
        Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
        Matcher m = p.matcher(e.getMessage());
        if (m.find()) {
            number = Integer.parseInt(m.group(1));
        }
        return number;
    }
}