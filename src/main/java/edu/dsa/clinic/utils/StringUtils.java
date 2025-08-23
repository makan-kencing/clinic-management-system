package edu.dsa.clinic.utils;

import org.jetbrains.annotations.Range;

public final class StringUtils {
    private StringUtils() {
    }

    public static String padLeft(String s, char padding, @Range(from = 0, to = Integer.MAX_VALUE) int n) {
        int repeats = Math.max(0, n - s.length());

        return Character.toString(padding).repeat(repeats) + s;
    }

    public static String padRight(String s, char padding, @Range(from = 0, to = Integer.MAX_VALUE) int n) {
        int repeats = Math.max(0, n - s.length());

        return s + Character.toString(padding).repeat(repeats);
    }

    public static String pad(String s, char padding, @Range(from = 0, to = Integer.MAX_VALUE) int n) {
        int repeats = Math.max(0, (n - s.length()) / 2);
        int offset = Math.max(0, (n - s.length()) % 2);

        return Character.toString(padding).repeat(repeats)
                + s
                + Character.toString(padding).repeat(repeats + offset);
    }

    public static String toTitlecase(String s) {
        // https://stackoverflow.com/questions/1086123/is-there-a-method-for-string-conversion-to-title-case
        final String ACTIONABLE_DELIMITERS = " '-/"; // these cause the character following
        // to be capitalized

        StringBuilder sb = new StringBuilder();
        boolean capNext = true;

        for (char c : s.toCharArray()) {
            c = (capNext)
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c);
            sb.append(c);
            capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0); // explicit cast not needed
        }
        return sb.toString();
    }
}
