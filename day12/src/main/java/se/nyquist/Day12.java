package se.nyquist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
                var entries = createEntries(lines);
                List<Integer> result = exercise1(entries);
                System.out.println("Exercise 1: " + result.stream().mapToInt(Integer::intValue).sum());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Integer> exercise1(List<Entry> entries) {
        return entries.stream().map(Day12::process).toList();
    }

    private static int process(Entry entry) {
        if (entry.errorNodes().isEmpty()) {
            return 0;
        }
        List<List<SpringStatus>> result = new ArrayList<>();
        var errors = entry.errorNodes();
        var brokenCount = errors.getFirst();
        var current = entry.nodes();
        int c = current.getFirst();
        if (SpringStatus.UNKNOWN.representedBy(c)) {
            result.addAll(Stream.concat(
                            process(List.of(SpringStatus.OK), brokenCount, current.subList(1, current.size()), errors.subList(1, errors.size())),
                            processBroken(List.of(), brokenCount, current, errors.subList(1, errors.size())))
                    .toList());
        } else if (SpringStatus.BROKEN.representedBy(c)) {
            result.addAll(processBroken(List.of(), brokenCount, current, errors.subList(1, errors.size())).toList());
        } else {
            result.addAll(process(List.of(SpringStatus.OK), brokenCount, current.subList(1, current.size()), errors.subList(1, errors.size())).toList());
        }
        var results = result.stream().map(l -> l.stream().map(SpringStatus::toChar).collect(Collectors.joining())).distinct().toList();
        results.stream().forEach(System.out::println);
        results.stream().forEach(s -> Day12.validate(entry,s));
        return results.size();
    }

    private static Pattern brokenPattern = Pattern.compile("#+");
    private static void validate(Entry entry, String s) {
        var matcher = brokenPattern.matcher(s);
        var current = 0;
        var entryString = entry.nodes().stream().map(Character::toString).collect(Collectors.joining());
        if (entryString.length() != s.length()) {
            throw new RuntimeException("Entry " + entryString + " Length mismatch: " + s);
        }
        while(matcher.find()) {
            if (! entry.errorNodes().get(current).equals(matcher.group().length())) {
                throw new RuntimeException("Entry " + entryString + " No match of " + entry.errorNodes().get(current) + " with: " + s);
            }
            current++;
        }

    }

    private static Stream<List<SpringStatus>> process(List<SpringStatus> current, int brokenCount, List<Integer> tail, List<Integer> errors) {
        List<List<SpringStatus>> result = new ArrayList<>();
        if (tail.isEmpty()) {
            return Stream.of();
        }
        if (brokenCount > tail.size()) {
            return Stream.of();
        }
        int c = tail.getFirst();
        if (SpringStatus.OK.representedBy(c)) {
            var currentList = Stream.concat(current.stream(),Stream.of(SpringStatus.OK)).toList();
            return process(currentList, brokenCount, tail.subList(1, tail.size()), errors);
        } else if (SpringStatus.UNKNOWN.representedBy(c)) {
            return Stream.concat(
                    process(Stream.concat(current.stream(), Stream.of(SpringStatus.OK)).toList(), brokenCount, tail.subList(1, tail.size()), errors),
                    processBroken(current, brokenCount, tail, errors));
        } else {
            return processBroken(current, brokenCount, tail, errors);
        }
    }

    private static Stream<List<SpringStatus>> processBroken(List<SpringStatus> current, int brokenCount, List<Integer> tail, List<Integer> errors) {
        if (brokenCount > 0 && tail.subList(0,brokenCount).stream().anyMatch(SpringStatus.OK::representedBy)) {
            // Invalid count of broken
            return Stream.of();
        } else if (brokenCount == tail.size()) {
            if (errors.isEmpty()) {
                var brokenStream = IntStream.range(0, brokenCount).mapToObj(i -> SpringStatus.BROKEN);
                var currentList = Stream.concat(current.stream(), brokenStream).toList();
                return Stream.of(currentList);
            } else {
                return Stream.of();
            }
        } else if (SpringStatus.BROKEN.representedBy(tail.get(brokenCount))) {
            // Invalid count of broken
            return Stream.of();
        } else {
            var brokenStream = IntStream.range(0,brokenCount).mapToObj(i -> SpringStatus.BROKEN);
            var currentList = Stream.concat(Stream.concat(current.stream(),brokenStream), Stream.of(SpringStatus.OK)).toList();
            if (errors.isEmpty()) {
                int remaining = tail.size()-brokenCount-1;
                if (remaining > 0) {
                    var okStream = IntStream.range(0, remaining).mapToObj(i -> SpringStatus.OK);
                    return Stream.of(Stream.concat(currentList.stream(), okStream).toList());
                } else {
                    return Stream.of(currentList.stream().toList());
                }
            } else {
                var nextBroken = errors.getFirst();
                return process(currentList, nextBroken, tail.subList(brokenCount+1, tail.size()), errors.subList(1, errors.size()));
            }
        }
    }

    private static List<Entry> createEntries(List<String> lines) {
        return lines.stream().map(line -> {
            var parts = line.split(" ");
            var nodes = parts[0].trim().chars().boxed().toList();
            var errors = Arrays.stream(parts[1].trim().split(",")).map(Integer::parseInt).toList();
            return new Entry(nodes,errors);
        }).toList();
    }
}
