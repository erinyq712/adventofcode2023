package se.nyquist;

import java.util.HashMap;
import java.util.Map;

public enum Direction {
    LEFT,
    RIGHT;

    public static Direction of(char c) {
        return switch(c) {
            case 'L' -> Direction.LEFT;
            case 'R' -> Direction.RIGHT;
            default -> throw new IllegalDirection(String.valueOf(c));
        };
    }

    private static class IllegalDirection extends RuntimeException {
        public IllegalDirection(String s) {
            super("IllegaL direction: " + s);
        }
    }
}
