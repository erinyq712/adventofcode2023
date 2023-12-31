package se.nyquist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
                var map = GalaxyMap.buildMap(lines);
                System.out.println("Exercise 1: " + map.sumOfDistances());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
