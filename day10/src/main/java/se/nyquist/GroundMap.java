package se.nyquist;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GroundMap {
    private List<List<CellType>> map;

    public GroundMap(List<String> lines) {
        this.map = buildMap(lines);
    }

    public CellType get(int x, int y) {
        return map.get(y).get(x);
    }

    private static List<List<CellType>> buildMap(List<String> lines) {
        return lines.stream().map(l -> l.chars().boxed().map(i -> CellType.of((char)i.intValue())).toList()).toList();
    }

    public boolean contains(Point p) {
        return p.x() >= 0 && p.y() >= 0 && p.x() < map.getFirst().size() && p.y() < map.size();
    }

    public Set<Point> subtract(Set<Point> visited) {
        return IntStream.range(0,map.size()).boxed().flatMap(y -> IntStream.range(0,map.get(y).size()).mapToObj(x -> new Point(x,y)).filter(p -> ! visited.contains(p))).collect(Collectors.toSet());
    }

    public Point borders() {
        return new Point(map.getFirst().size(), map.size());
    }
}
