package edu.dsa.clinic.utils.table;

import edu.dsa.clinic.utils.StringUtils;

public enum Alignment {
    LEFT,
    CENTER,
    RIGHT;

    public String pad(String s, char pad, int n) {
        return switch (this) {
            case Alignment.LEFT -> StringUtils.padRight(s, pad, n);
            case Alignment.CENTER -> StringUtils.pad(s, pad, n);
            case Alignment.RIGHT -> StringUtils.padLeft(s, pad, n);
        };
    }
}
