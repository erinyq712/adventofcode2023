package se.nyquist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.function.Predicate.not;

public class Day8 {

    public static void main(String[] args) {
        var input = "input.txt";
        // var input = "sample.txt";
        // var input = "sample2.txt";
        if (args.length > 0) {
            input = args[0];
        }
        try (var stream = Day8.class.getClassLoader().getResourceAsStream(input)) {
            if (stream != null) {
                var lines = new BufferedReader(new InputStreamReader(stream)).lines().filter(not(String::isEmpty)).toList();
                NextDirection next = new NextDirection(lines.get(0));
                exercise1("Day 8 Exercise 1: ", next, lines, Day8::atStart1, Day8::atTerminalNode1);
                exercise2("Day 8 Exercise 2: ", next, lines, Day8::atStart2, Day8::atTerminalNode2);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void exercise1(String message, NextDirection it, List<String> lines, Predicate<String> atStart , Predicate<Node> atTerminal) {
        var nodes = IntStream.range(1, lines.size())
                .mapToObj(i -> createNode(lines.get(i), atStart))
                .collect(Collectors.toMap(Node::position, s->s));
        long counter = 0L;
        boolean found = false;
        var current = Node.startNodes.stream().map(s -> nodes.get(s)).toList();
        while(! found) {
            var direction = it.next(counter);
            current = current.stream().map(n -> nodes.get(n.next(direction))).toList();
            var atEndCount = current.stream().filter(atTerminal).count();
            found = atEndCount == current.size();
            counter++;
        }
        System.out.println(message + counter);
    }

    private static void exercise2(String message, NextDirection it, List<String> lines, Predicate<String> atStart , Predicate<Node> atTerminal) {
        var nodes = IntStream.range(1, lines.size())
                .mapToObj(i -> createNode(lines.get(i), atStart))
                .collect(Collectors.toMap(Node::position, s->s));
        var current = Node.startNodes.stream().map(s -> nodes.get(s)).toList();
        var cycles = current.stream().mapToLong(n -> {
            long counter = 0L;
            boolean found = false;
            var c = n;
            while(! found) {
                var direction = it.next(counter);
                c = nodes.get(c.next(direction));
                found = atTerminal.test(c);
                counter++;
            }
            return counter;
        }).boxed().toList();
        var gcd = cycles.stream().map(BigInteger::valueOf).reduce(BigInteger::gcd).orElse(BigInteger.ZERO);
        var numbers = cycles.stream().map(BigInteger::valueOf).map(i -> i.divide(gcd)).toList();
        var product = numbers.stream().reduce(BigInteger::multiply).orElse(BigInteger.ZERO);
        var lcd = product.multiply(gcd);
        System.out.println(message + lcd);
    }

    private static boolean atStart1(String s) {
        return s.equals("AAA");
    }

    private static boolean atStart2(String s) {
        return s.endsWith("A");
    }

    private static boolean atTerminalNode1(Node n) {
        var atTerminal = n.position().equals("ZZZ");
        return atTerminal;
    }

    private static boolean atTerminalNode2(Node n) {
        var atTerminal = n.position().endsWith("Z");
        return atTerminal;
    }

    private static Node createNode(String s, Predicate<String> isStartNode) {
        Pattern nodePattern = Pattern.compile("(\\S+) = \\(([^,]+), ([^)]+)\\)");
        var matcher = nodePattern.matcher(s);
        if (matcher.matches()) {
            var nodeName = matcher.group(1);
            var leftNode = matcher.group(2);
            var rightNode = matcher.group(3);
            if (isStartNode.test(nodeName)) {
                Node.addStartNode(nodeName);
            }
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
