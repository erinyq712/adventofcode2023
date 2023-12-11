package se.nyquist;

public record Node(String position, Directions directions) {
    public static final String start = "AAA";
    public static final String end = "ZZZ";

    String next(Direction direction) {
        return switch(direction) {
            case LEFT -> directions.left();
            case RIGHT -> directions.right();
        };
    }
}
