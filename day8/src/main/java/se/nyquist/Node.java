package se.nyquist;

import java.util.ArrayList;
import java.util.List;

public record Node(String position, Directions directions) {

    String next(Direction direction) {
        return switch(direction) {
            case LEFT -> directions.left();
            case RIGHT -> directions.right();
        };
    }

    public static final List<String> startNodes = new ArrayList<>();

    public static void addStartNode(String node) {
        startNodes.add(node);
    }
}
