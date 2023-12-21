package se.nyquist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
}
