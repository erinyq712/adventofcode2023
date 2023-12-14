package se.nyquist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class Day9 {

    public static void main(String[] args) {
        var input = "input.txt";
        // var input = "sample.txt";

        if (args.length > 0) {
            input = args[0];
        }
        try (var stream = Day9.class.getClassLoader().getResourceAsStream(input)) {
            if (stream != null) {
                var lines = new BufferedReader(new InputStreamReader(stream)).lines().filter(not(String::isEmpty)).toList();
                execise1(lines);
                execise2(lines);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void execise1(List<String> lines) {
        System.out.println("Exercise 1: " + lines.stream().flatMap(Day9::history).map(BigInteger::valueOf).reduce(BigInteger::add).orElse(BigInteger.ZERO));
    }

    private static void execise2(List<String> lines) {
        System.out.println("Exercise 2: " + lines.stream().flatMap(Day9::history2).map(BigInteger::valueOf).reduce(BigInteger::add).orElse(BigInteger.ZERO));
    }

    private static ArrayList<List<Long>> populate(String line) {
        var numbers = Stream.of(line.split("\\s+")).map(Long::parseLong).toList();
        var diffs = computeDiffs(numbers);
        var history = new ArrayList<List<Long>>();
        history.add(numbers);
        history.add(diffs);
        while (diffs.stream().anyMatch(n -> n != 0)) {
            diffs = computeDiffs(diffs);
            history.add(diffs);
        }
        return history;
    }

    private static List<Long> computeDiffs(List<Long> numbers) {
        return IntStream.range(0, numbers.size() - 1).mapToLong(i -> numbers.get(i + 1) - numbers.get(i)).boxed().toList();
    }

    private static Stream<Long> history(String line) {
        var history = populate(line);
        int i = history.size()-1;
        ArrayList<Long> next = new ArrayList<>(history.get(i));
        next.add(0L);
        while(i > 0) {
            var prev = next;
            next = new ArrayList<>(history.get(i-1));
            var extra = next.getLast() + prev.getLast();
            next.add(extra);
            i--;
        }
        // System.out.println(next.getLast());
        return Stream.of(next.getLast());
    }


    private static Stream<Long> history2(String line) {
        var history = populate(line);
        int i = history.size()-1;
        ArrayList<Long> next = new ArrayList<>(history.get(i));
        next.add(0, 0L);
        while(i > 0) {
            var prev = next;
            next = new ArrayList<>(history.get(i-1));
            var extra = next.getFirst() - prev.getFirst();
            next.add(0, extra);
            i--;
        }
        // System.out.println(next.getLast());
        return Stream.of(next.getFirst());
    }

}
