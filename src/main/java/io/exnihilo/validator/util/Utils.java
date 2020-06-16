package io.exnihilo.validator.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Provides utility methods that can be used anywhere and ties to perform common operations. */
public final class Utils {
  private Utils() {}

  /**
   * if startingPhrase is "line ", endingPhrase is "]" and string is "Error at line 21]", the method
   * will return 21.
   *
   * @param startingPhrase
   * @param endingPhrase
   * @param searchText
   * @return matched number from enclosing string pattern
   */
  public static int getNumberFromRegexPattern(String startingPhrase, String endingPhrase, String searchText) {
    int number = 0;
    Pattern p = Pattern.compile(Pattern.quote(startingPhrase) + "(.*?)" + Pattern.quote(endingPhrase));
    Matcher m = p.matcher(searchText);
    while (m.find()) {
      number = Integer.parseInt(m.group(1));
    }
    return number;
  }
}
