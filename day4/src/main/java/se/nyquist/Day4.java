package se.nyquist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Day4 {
    public static void main(String[] args) {
        var input = "input.txt";
        // var input = "sample.txt";
        if (args.length > 0) {
            input = args[0];
        }
        try (var stream = Day4.class.getClassLoader().getResourceAsStream(input)) {
            if (stream != null) {
                var lines = new BufferedReader(new InputStreamReader(stream)).lines().toList();
                var cards = lines.stream().map(Day4::createScratchCard).toList();
                exercise1(cards);
                // exercise2(parts, symbols);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void exercise1(List<ScratchCard> cards) {
        var result = cards.stream().map(c -> c.ticket().score(c.winningNumbers())).reduce(Long::sum).orElse(0L);
        System.out.println("Day 4 Exercise 1: " + result);
    }

    private static class InvalidInput extends RuntimeException {
        public InvalidInput() {
            super("Invalid input");
        }
    }

    private static ScratchCard createScratchCard(String l) {
        var parts = l.split("[|:]");
        if (parts.length != 3) {
            throw new InvalidInput();
        }
        var winning = getNumbers(parts[1]);
        var numbers = getNumbers(parts[2]);
        return new ScratchCard(winning, new Ticket(numbers));
    }

    private static List<Integer> getNumbers(String part) {
        return Stream.of(part.trim().split("[ ]+"))
                .map(s -> Integer.parseInt(s)).toList();
    }
}

