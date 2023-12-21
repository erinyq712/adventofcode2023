package se.nyquist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class Day10 {

    public static void main(String[] args) {
        var input = "input.txt";
        // var input = "sample1.txt";

        if (args.length > 0) {
            input = args[0];
        }
        try (var stream = Day10.class.getClassLoader().getResourceAsStream(input)) {
            if (stream != null) {
                var lines = new BufferedReader(new InputStreamReader(stream)).lines().filter(not(String::isEmpty)).toList();
                var map = new GroundMap(lines);
                var start = findStart(lines);
                execise1(start, map);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void execise1(Point start, GroundMap map) {
        List<Point> connected = getConnected(start, map);
        Distances distances = new Distances(start);
        while(! connected.isEmpty()) {
            distances.incrementAndAdd(connected);
            connected = getConnected(connected, map, distances.visited());
        }
        System.out.println("Exercise 1: max distance " + distances.getMaxDistance() + " " + distances.getMostDistant());
    }

    private static List<Point> getConnected(Point start, GroundMap groundMap) {
        return Stream.of(new Point(0,1), new Point(0, -1), new Point(-1, 0), new Point(1,0))
                .filter(p -> isConnected(start, groundMap, p))
                .map(start::add).toList();
    }

    private static List<Point> getConnected(List<Point> border, GroundMap groundMap, Set<Point> visited) {
        return border.stream().flatMap(start -> Stream.of(new Point(0,1), new Point(0, -1), new Point(-1, 0), new Point(1,0))
                .filter(p -> isConnected(start, groundMap, p))
                .map(start::add))
                .distinct()
                .filter(p -> isNotVisited(p,visited))
                .toList();
    }

    private static boolean isNotVisited(Point p, Set<Point> points) {
        return ! points.contains(p);
    }

    
    private static boolean isConnected(Point start, GroundMap groundMap, Point delta) {
        if (groundMap.contains(start.add(delta))) {
            var startCellType = groundMap.get(start.x(), start.y());
            var cellType = groundMap.get(start.x() + delta.x(), start.y() + delta.y());
            return switch (Movement.getMovement(delta)) {
                case UP -> canMoveUp(startCellType, cellType);
                case DOWN -> canMoveDown(startCellType, cellType);
                case RIGHT -> canMoveRight(startCellType, cellType);
                case LEFT -> canMoveLeft(startCellType, cellType);
            };
        }
        return false;
    }

    private static Set<CellType> upStartCellTypes = Set.of(CellType.START, CellType.VERTICAL, CellType.NORTH_EAST, CellType.NORTH_WEST);
    private static Set<CellType> upTargetCellTypes = Set.of(CellType.START, CellType.VERTICAL, CellType.SOUTH_EAST, CellType.SOUTH_WEST);

    private static boolean canMoveUp(CellType startCellType, CellType cellType) {
        return upStartCellTypes.contains(startCellType) && upTargetCellTypes.contains(cellType);
    }

    private static boolean canMoveDown(CellType startCellType, CellType cellType) {
        return canMoveUp(cellType, startCellType);
    }

    private static Set<CellType> rightStartCellTypes = Set.of(CellType.START, CellType.HORIZONTAL, CellType.NORTH_EAST, CellType.SOUTH_EAST);
    private static Set<CellType> rightTargetCellTypes = Set.of(CellType.START, CellType.HORIZONTAL, CellType.NORTH_WEST, CellType.SOUTH_WEST);

    private static boolean canMoveRight(CellType startCellType, CellType cellType) {
        return rightStartCellTypes.contains(startCellType) && rightTargetCellTypes.contains(cellType);
    }

    private static boolean canMoveLeft(CellType startCellType, CellType cellType) {
        return canMoveRight(cellType, startCellType);
    }

    private static Point findStart(List<String> lines) {
        return IntStream.range(0,lines.size())
                .boxed()
                .flatMap(y ->
                        IntStream.range(0, lines.get(y).length()).filter(x -> isStart(lines, y, x))
                        .mapToObj(x -> new Point(x,y))).findFirst().orElseThrow(() -> new RuntimeException("Help"));
    }

    private static boolean isStart(List<String> lines, int y, int x) {
        return CellType.of(lines.get(y).charAt(x)).equals(CellType.START);
    }
}
