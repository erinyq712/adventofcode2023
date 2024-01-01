package se.nyquist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.function.Predicate.not;

public class Day11 {

    public static void main(String[] args) {
        var input = "input.txt";
        // var input = "sample.txt";

        if (args.length > 0) {
            input = args[0];
        }
        try (var stream = Day11.class.getClassLoader().getResourceAsStream(input)) {
            if (stream != null) {
                var lines = new BufferedReader(new InputStreamReader(stream)).lines().filter(not(String::isEmpty)).toList();
                var galaxies = readGalalaxies(lines);
                var map = GalaxyMap.buildMap(galaxies, lines.getFirst().length(), lines.size(), 2);
                System.out.println("Exercise 1: " + map.sumOfDistances());
                var map2 = GalaxyMap.buildMap(galaxies, lines.getFirst().length(), lines.size(), 1000000);
                System.out.println("Exercise 2: " + map2.sumOfDistances());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Point> readGalalaxies(List<String> lines) {
        return IntStream.range(0,lines.size()).boxed().flatMap(y -> mapRow(y, lines.get(y)).stream()).toList();
    }

    private static List<Point> mapRow(int y, String line) {
        var result = new ArrayList<Point>();
        int pos = line.indexOf('#');
        while (pos > -1) {
            result.add(new Point(pos,y));
            pos = line.indexOf('#', pos+1);
        }
        return result;
    }
}
