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
}
