package se.nyquist;

import java.util.List;

public class NextDirection {
    private List<Direction> directions;
    public NextDirection(String s) {
        directions = s.chars().mapToObj(i -> Direction.of((char)i)).toList();
    }

    public Direction next(long counter) {
        int current = (int)(counter % directions.size());
        return directions.get(current);
    }
}
