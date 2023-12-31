package se.nyquist;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Math.abs;

public record GalaxyMap(List<Point> map) {

    public static GalaxyMap buildMap(List<String> lines) {
        List<String> expandVertically = expand(lines);
        var transposed = transpose(expandVertically);
        var expandedMap = transpose(expand(transposed));

        return new GalaxyMap(IntStream.range(0,expandedMap.size()).boxed().flatMap(y -> {
            var galaxies = new ArrayList<Point>();
            var line = expandedMap.get(y);
            int from = 0;
            while(from >= 0) {
                from = line.indexOf('#', from);
                if (from >= 0) {
                    galaxies.add(new Point(from, y));
                    from++;
                }
            }
            return galaxies.stream();
        }).toList());
    }

    public static List<String> expand(List<String> lines) {
        return lines.stream().flatMap(l -> {
            if (l.equals(".".repeat(l.length()))) {
                return Stream.of(l, l);
            } else {
                return Stream.of(l);
            }}).toList();
    }

    public static List<String> transpose(List<String> lines) {
        return IntStream.range(0, lines.get(0).length())
                .mapToObj(x -> lines.stream().map(line -> String.valueOf(line.charAt(x))).collect(Collectors.joining()))
                .toList();
    }

    public List<Pair<Point>> getPairs() {
        var result = new ArrayList<Pair<Point>>();
        int start = 0;
        int limit = map.size()-1;
        while (start < limit) {
            final Point current = map.get(start);
            result.addAll(IntStream.range(start+1,map.size()).mapToObj(i -> new Pair<Point>(current, map.get(i))).toList());
            start++;
        }
        return result;
    }

    public int sumOfDistances() {
        return getPairs().stream().mapToInt(GalaxyMap::getDistance).sum();
    }

    private static int getDistance(Pair<Point> pair) {
        var result =  abs(pair.left().y() - pair.right().y()) + abs(pair.left().x() - pair.right().x());
        return result;
    }
}
