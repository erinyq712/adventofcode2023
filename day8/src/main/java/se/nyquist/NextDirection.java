package se.nyquist;

import java.util.List;

public class NextDirection {
    private List<Direction> directions;
    private int current;
    public NextDirection(String s) {
        directions = s.chars().mapToObj(i -> Direction.of((char)i)).toList();
        current = -1;
    }

    public Direction next() {
        current = (current + 1) % directions.size();
        return directions.get(current);
    }
}
