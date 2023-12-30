package se.nyquist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static se.nyquist.CellType.canMoveDown;
import static se.nyquist.CellType.canMoveLeft;
import static se.nyquist.CellType.canMoveRight;
import static se.nyquist.CellType.canMoveUp;

public class Day10 {

    public static void main(String[] args) {
        var input = "input.txt";
        // var input = "sample24.txt";

        if (args.length > 0) {
            input = args[0];
        }
        try (var stream = Day10.class.getClassLoader().getResourceAsStream(input)) {
            if (stream != null) {
                var lines = new BufferedReader(new InputStreamReader(stream)).lines().filter(not(String::isEmpty)).toList();
                var map = new GroundMap(lines);
                var start = findStart(lines);
                var distances = execise1(start, map);
                exercise2(distances.visited(), map);
                exercise3(distances.route(map));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void exercise2(Set<Point> visited, GroundMap map) {
        Set<Point> remaining = map.subtract(visited);
        Set<Point> interior = new HashSet<>();
        interior.addAll(remaining.stream().filter(r -> isInterior(r, visited, map)).collect(Collectors.toSet()));
        System.out.println("Exercise 2: interior points = " + interior.size());
    }

    private static void exercise3(List<Point> route) {
        // Shoelace formula: https://en.wikipedia.org/wiki/Shoelace_formula
        var doubleArea = IntStream.range(0, route.size())
                .mapToLong(i -> {
                    var current = route.get(i);
                    var next = route.get((i+1) % route.size());
                    return (long) (current.y() + next.y()) * (current.x()-next.x());
                }).sum();
        var area = doubleArea/2;
        // Picks theorem: https://en.wikipedia.org/wiki/Pick%27s_theorem
        // A = interior point + (boundary points/2) -1
        var interior = area + 1 - route.size()/2;
        System.out.println("Exercise 3: interior points = " + interior);
    }

    private static boolean isInterior(Point point, Set<Point> visited, GroundMap map) {
        Point borders = map.borders();
        int limit = Math.min(borders.x()-point.x(), borders.y()-point.y());
        List<Point> foundDown = IntStream.range(1,limit)
                .mapToObj(point::extend)
                .filter(visited::contains)
                        .sorted(Comparator.comparingInt(Point::y))
                .toList();

        return hasOddIntersectingPoints(map, foundDown);
    }

    private static boolean hasOddIntersectingPoints(GroundMap map, List<Point> foundDown) {
        return foundDown.stream().filter(p-> map.get(p) != CellType.NORTH_EAST && map.get(p) != CellType.SOUTH_WEST).count() % 2 == 1;
    }

    private static Distances execise1(Point start, GroundMap map) {
        List<Point> connected = getConnected(start, map);
        // Set start type
        if (connected.contains(start.addX(1)) && connected.contains(start.addY(1))) {
            map.setCellType(start, CellType.SOUTH_EAST);
        } else if (connected.contains(start.addX(1)) && connected.contains(start.addY(-1))) {
            map.setCellType(start, CellType.NORTH_EAST);
        } else if (connected.contains(start.addX(-1)) && connected.contains(start.addY(-1))) {
            map.setCellType(start, CellType.NORTH_WEST);
        } else if (connected.contains(start.addX(-1)) && connected.contains(start.addY(1))) {
            map.setCellType(start, CellType.SOUTH_WEST);
        } else if (connected.contains(start.addX(-1)) && connected.contains(start.addX(1))) {
            map.setCellType(start, CellType.HORIZONTAL);
        } else if (connected.contains(start.addY(-1)) && connected.contains(start.addY(1))) {
            map.setCellType(start, CellType.VERTICAL);
        }
        Distances distances = new Distances(start);
        while(! connected.isEmpty()) {
            distances.incrementAndAdd(connected);
            connected = getConnected(connected, map, distances.visited());
        }
        System.out.println("Exercise 1: max distance " + distances.getMaxDistance() + " " + distances.getMostDistant());
        return distances;
    }

    private static List<Point> getConnected(Point start, GroundMap groundMap) {
        return Stream.of(new Point(0,1), new Point(0, -1), new Point(-1, 0), new Point(1,0))
                .filter(p -> groundMap.isConnected(start, p))
                .map(start::add).toList();
    }

    private static List<Point> getConnected(List<Point> border, GroundMap groundMap, Set<Point> visited) {
        return border.stream().flatMap(start -> Stream.of(new Point(0,1), new Point(0, -1), new Point(-1, 0), new Point(1,0))
                .filter(p -> groundMap.isConnected(start, p))
                .map(start::add))
                .distinct()
                .filter(p -> isNotVisited(p,visited))
                .toList();
    }

    private static boolean isNotVisited(Point p, Set<Point> points) {
        return ! points.contains(p);
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
