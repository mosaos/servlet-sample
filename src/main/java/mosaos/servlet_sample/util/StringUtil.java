package mosaos.servlet_sample.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringUtil {
    /**
     * Get first n lines from input String.
     * @param input source String
     * @param n lines
     * @return first n lines
     */
    public static String getFirstNLines(String input, int n) {
        return Arrays.stream(input.split("\n"))
                     .limit(n)
                     .collect(Collectors.joining("\n"));
    }
}
