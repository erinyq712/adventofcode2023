package se.nyquist;

import java.util.function.ToIntFunction;

public enum SpringStatus {
    UNKNOWN('?'),
    BROKEN('#'),
    OK('.');

    private final char c;

    SpringStatus(char c) {
        this.c = c;
    }

    public static SpringStatus of(int c) {
        if (UNKNOWN.representedBy(c)) {
            return UNKNOWN;
        } else if (BROKEN.representedBy(c)) {
            return BROKEN;
        } else if (OK.representedBy(c)) {
            return OK;
        }
        throw new IllegalArgumentException(String.valueOf(c));
    }

    public String toChar() {
        return String.valueOf(c);
    }

    public boolean representedBy(int c) {
        return this.c == c;
    }
}
