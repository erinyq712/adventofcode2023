package se.nyquist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class Distances {
    private Point start;
    private Map<Point, Integer> distanceMap;

    public Distances() {
        distanceMap = new HashMap<>();
    }

    public Distances(Point p) {
        this();
        add(List.of(p));
        start = p;
    }

    public void increment() {
        distanceMap.keySet().stream().forEach(p -> distanceMap.computeIfPresent(p, (q,i) -> i + 1));
    }

    public void add(List<Point> points) {
        points.stream().forEach(p -> distanceMap.put(p,0));
    }

    public void incrementAndAdd(List<Point> points) {
        increment();
        add(points);
    }

    public Set<Point> visited() {
        return distanceMap.keySet();
    }

    public int distance(Point p) {
        return distanceMap.get(p);
    }

    public List<Point> getMostDistant() {
        return distanceMap.entrySet().stream().filter(e -> e.getValue() == 0).map(Map.Entry::getKey).toList();
    }

    public int getMaxDistance() {
        return distanceMap.get(start);
    }

    public List<Point> getWithDistance(int distance) {
        return distanceMap.entrySet().stream().filter(e -> e.getValue() == distance).map(Map.Entry::getKey).toList();
    }

    public List<Point> route(GroundMap map) {
        var result = new ArrayList<Point>();
        result.add(start);
        var resultTmp = new ArrayList<Point>();
        var currentDistance = getMaxDistance()-1;
        var nextList = getWithDistance(currentDistance);
        while (currentDistance > 0) {
            currentDistance--;
            final int distance = currentDistance;
            final var points = nextList;
            IntStream.range(0,nextList.size()).forEach(i -> {
                if (i % 2 == 0) {
                    result.add(points.get(i));
                } else {
                    resultTmp.addFirst(points.get(i));
                }
            });
            nextList = nextList.stream().flatMap(p -> getWithDistance(distance).stream().filter(q -> map.isConnectedWith(p,q))).distinct().toList();
        }
        result.addAll(resultTmp);
        return result;
    }
}
