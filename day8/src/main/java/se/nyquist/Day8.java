package se.nyquist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.function.Predicate.not;

public class Day8 {

    public static void main(String[] args) {
        // var input = "input.txt";
        // var input = "sample.txt";
        var input = "sample2.txt";
        if (args.length > 0) {
            input = args[0];
        }
        try (var stream = Day8.class.getClassLoader().getResourceAsStream(input)) {
            if (stream != null) {
                var lines = new BufferedReader(new InputStreamReader(stream)).lines().filter(not(String::isEmpty)).toList();
                NextDirection next = new NextDirection(lines.get(0));
                var nodes = IntStream.range(1, lines.size()).mapToObj(i -> createNode(lines.get(i))).collect(Collectors.toMap(Node::position, s->s));
                exercise1("Day 8 Exercise 1: ", next, nodes);
               //  exercise("Day 7 Exercise 2: ", getPlayers(lines, s -> new Hand<HandTypeWithJoker, CardTypeWithJoker>(s, HandTypeWithJoker::type, CardTypeWithJoker::type)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void exercise1(String message, NextDirection it, Map<String, Node> nodes) {
        System.out.print(message);
        var start = nodes.get(Node.start);
        var counter = 1;
        var current = start.next(it.next());
        while(! current.equals(Node.end)) {
            counter++;
            current = nodes.get(current).next(it.next());
        }
        System.out.println(counter);
    }

    private static Node createNode(String s) {
        Pattern nodePattern = Pattern.compile("(\\S+) = \\(([^,]+), ([^)]+)\\)");
        var matcher = nodePattern.matcher(s);
        if (matcher.matches()) {
            var nodeName = matcher.group(1);
            var leftNode = matcher.group(2);
            var rightNode = matcher.group(3);
            return new Node(nodeName, new Directions(leftNode, rightNode));
        }
        throw new InvalidInput(s);
    }

    private static class InvalidInput extends RuntimeException {
        public InvalidInput(String s) {
            super("Invalid input " + s);
        }
    }
}
