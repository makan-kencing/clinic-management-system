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

    public static String stripStart(String s, char c) {
        int strLen = s.length();
        if (strLen == 0)
            return s;

        int start = 0;
        while (start != strLen && c == s.charAt(start))
            start++;

        return s.substring(start);
    }

    public static String stripEnd(String s, char c) {
        int end = s.length();
        if (end == 0) {
            return s;
        }

        while (end != 0 && c == s.charAt(end - 1))
            end--;

        return s.substring(0, end);
    }

    public static String strip(String s, char c) {
        s = stripStart(s, c);
        return stripEnd(s, c);
    }

    /**
     * Convert string in the format of an {@code array} into an array of strings.
     *
     * @param arrayString The string. Example {@code ["elem1", "elem2"]}
     * @return The array of strings.
     */
    public static String[] convertArrayStringToArray(String arrayString) {
        // trim the leading and tail "[" and "]" from the string
        arrayString = stripStart(arrayString, '[');
        arrayString = stripStart(arrayString, ']');

        if (arrayString.isEmpty())
            return new String[0];

        return arrayString.split(", ");
    }
}
