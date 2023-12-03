package se.nyquist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.IntStream;

public class Day1 {
    public static void main(String[] args) {
        var input = "input.txt";
        // var input = "sample2.txt";
        if (args.length > 0) {
            input = args[0];
        }
        try (var stream = Day1.class.getClassLoader().getResourceAsStream(input)) {
            if (stream != null) {
                var lines = new BufferedReader(new InputStreamReader(stream)).lines().toList();
                //exercise1(lines);
                exercise2(lines);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class NoDigitException extends RuntimeException {
        public NoDigitException() {
            super("No digit found");
        }
    }
    public static class NoResultsException extends RuntimeException {
        public NoResultsException() {
            super("No results found");
        }
    }
    private static void exercise1(List<String> lines) {
        System.out.println("1: Sum is: " + lines.stream().map(Day1::computeControl).reduce(Long::sum).orElseThrow(NoResultsException::new));
    }

    private static void exercise2(List<String> lines) {
        System.out.println("2: Sum is: " + lines.stream().map(Day1::computeControl2).reduce(Long::sum).orElseThrow(NoResultsException::new));
    }

    private static long computeControl(String s) {
        var firstDigit = s.chars().filter(Character::isDigit).findFirst().orElseThrow(NoDigitException::new);
        var chars = s.toCharArray();
        var lastDigit = IntStream.range(0,chars.length).map(i -> chars[chars.length-i-1])
                .filter(Character::isDigit).findFirst().orElseThrow(NoDigitException::new);
        var resultString = new String(new char[] { (char)firstDigit, (char)lastDigit});
        System.out.println(resultString);
        return Long.parseLong(resultString);
    }

    private static long computeControl2(String s) {
        var tokenizer = new Tokenizer(s);
        var control = tokenizer.getValue();
        // System.out.println(control);
        var result = computeControl(control);
        return result;
    }
}
