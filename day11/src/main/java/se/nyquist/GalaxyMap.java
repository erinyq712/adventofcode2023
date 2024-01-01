package se.nyquist;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

public record GalaxyMap(List<Point> map) {

    public static GalaxyMap buildMap(List<Point> galaxies, int xLimit, int yLimit, int factor) {
        var emptySpaceHorizontally = getEmptySpace(galaxies, xLimit, Point::x);
        var expandedHorizontally = expand(galaxies, emptySpaceHorizontally, factor, Point::x, (p,e) -> new Point(p.x()+e, p.y()));
        var emptySpaceVertically = getEmptySpace(galaxies, yLimit, Point::y);
        var expanded = expand(expandedHorizontally, emptySpaceVertically, factor, Point::y, (p,e) -> new Point(p.x(), p.y()+e));
        return new GalaxyMap(expanded);
    }

    private static List<Point> expand(List<Point> galaxies, List<Range> emptySpace, long factor, Function<Point,Long> accessor, BiFunction<Point,Long,Point> creator) {
        if (emptySpace.isEmpty()) {
            return galaxies;
        }
        var result = new ArrayList<>(galaxies.stream().filter(g -> accessor.apply(g) < emptySpace.get(0).start()).toList());
        int currentEmptyIndex = 0;
        var prevEmpty = emptySpace.get(currentEmptyIndex);
        var prevExpansion = prevEmpty.length() * (factor-1);
        while (currentEmptyIndex < emptySpace.size()-1) {
            final var lowerThreshold = prevEmpty.start();
            final var nextEmpty = emptySpace.get(currentEmptyIndex+1);
            final var upperThreshold = nextEmpty.start();
            final long expansion = prevExpansion;
            result.addAll(galaxies.stream().filter(g -> accessor.apply(g) > lowerThreshold && accessor.apply(g) < upperThreshold)
                    .map(g -> creator.apply(g, expansion)).toList());
            currentEmptyIndex++;
            prevEmpty = nextEmpty;
            prevExpansion += prevEmpty.length() * (factor-1);
        }
        final var lowerThreshold = prevEmpty.start();
        final long expansion = prevExpansion;
        result.addAll(galaxies.stream().filter(g -> accessor.apply(g) > lowerThreshold)
                .map(g -> creator.apply(g, expansion)).toList());
        return result;
    }

    private static List<Range> getEmptySpace(List<Point> space, int length, Function<Point,Long> accessor) {
        var cols = space.stream().map(accessor).sorted().distinct().toList();
        long start = 0;
        var ranges = new ArrayList<Range>();
        for(var x : cols) {
            if (x > start) {
                ranges.add(new Range(start, x-start));
            }
            start = x+1;
        }
        if (start < length) {
            ranges.add(new Range(start, length-start));
        }
        return ranges;
    }

    public List<Pair<Point>> getPairs() {
        var result = new ArrayList<Pair<Point>>();
        int start = 0;
        int limit = map.size()-1;
        while (start < limit) {
            final Point current = map.get(start);
            result.addAll(IntStream.range(start+1,map.size()).mapToObj(i -> new Pair<>(current, map.get(i))).toList());
            start++;
        }
        return result;
    }

    public long sumOfDistances() {
        // map.stream().sorted(Point::compare).forEach(System.out::println);
        return getPairs().stream().mapToLong(GalaxyMap::getDistance).sum();
    }

    private static long getDistance(Pair<Point> pair) {
        return abs(pair.left().y() - pair.right().y()) + abs(pair.left().x() - pair.right().x());
    }
}
