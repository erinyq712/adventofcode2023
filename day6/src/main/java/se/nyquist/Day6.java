package se.nyquist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day6 {
    public static void main(String[] args) {
        var input = "input.txt";
        // var input = "sample.txt";
        if (args.length > 0) {
            input = args[0];
        }
        try (var stream = Day6.class.getClassLoader().getResourceAsStream(input)) {
            if (stream != null) {
                var lines = new BufferedReader(new InputStreamReader(stream)).lines().toList();
                exercise("Day 6 Exercise 1: ", getRaces(lines));
                exercise("Day 6 Exercise 2: ", getRaces2(lines));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void exercise(String label, Races races) {
        var winProduct = races.getWinCounts().stream().reduce(1L, (l,s) -> l*s);
        System.out.println(label + winProduct);
    }

    private static Races getRaces(List<String> lines) {
        return getRaces(lines, s -> s.split("\\s+"));
    }

    private static Races getRaces2(List<String> lines) {
        return getRaces(lines, s -> new String[]{s.replaceAll("\\s+", "")});
    }

    private static Races getRaces(List<String> lines, Function<String, String[]> lambda) {
        var durations = new ArrayList<Long>();
        var records = new ArrayList<Long>();
        var durationsLine = lines.get(0);
        var recordsLine = lines.get(1);
        final var labelPattern  = Pattern.compile("[^:]+:\\s+");
        var durationMatcher = labelPattern.matcher(durationsLine);
        var recordMatcher = labelPattern.matcher(recordsLine);
        if (durationMatcher.find()) {
            durations.addAll(Stream.of(lambda.apply(durationsLine.substring(durationMatcher.end()))).map(Long::parseLong).toList());
        }
        if (recordMatcher.find()) {
            records.addAll(Stream.of(lambda.apply(recordsLine.substring(recordMatcher.end()))).map(Long::parseLong).toList());
        }
        return new Races(IntStream.range(0,durations.size()).mapToObj(i -> new RaceInfo(durations.get(i), records.get(i))).toList());
    }
}

