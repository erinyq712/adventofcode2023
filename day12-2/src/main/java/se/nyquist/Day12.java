package se.nyquist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.function.Predicate.not;

public class Day12 {

    public static void main(String[] args) {
        var input = "input.txt";
        // var input = "sample.txt";

        if (args.length > 0) {
            input = args[0];
        }
        try (var stream = Day12.class.getClassLoader().getResourceAsStream(input)) {
            if (stream != null) {
                var lines = new BufferedReader(new InputStreamReader(stream)).lines().filter(not(String::isEmpty)).toList();
                {
                    var entries = createStateMachines(lines, 5);
                    var result = entries.stream().mapToLong(s -> s.combinations()).reduce(Long::sum).orElse(0);
                    System.out.println("Exercise 1: " + result);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<StateMachine> createStateMachines(List<String> lines, int count) {
        return lines.stream().map(line -> {
            var parts = line.split(" ");
            var base = parts[0].trim();
            var input = IntStream.range(0,count).mapToObj(i -> base).collect(Collectors.joining("?"));
            var errorBase = parts[1].trim();
            var errorStr = errorBase + ("," + errorBase).repeat(count-1);
            return new StateMachine(errorStr,input);
        }).toList();
    }
}
