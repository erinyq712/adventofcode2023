package se.nyquist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
                var cards = new TreeMap<Integer, ScratchCard>();
                lines.stream().forEach(l -> createScratchCard(l,cards));
                // exercise1(cards.values());
                exercise2(cards);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void exercise2(Map<Integer, ScratchCard> cards) {
        Map<Integer, Integer> result = new HashMap<>();
        cards.keySet().stream().forEach(key -> {
            if (result.containsKey(key)) {
                result.put(key, result.get(key)+1);
            } else {
                result.put(key, 1);
            }
            var count = cards.get(key).scoringCount();
            IntStream.range(key+1,key+count+1).forEach(i -> {
                if (result.containsKey(i)) {
                    result.put(i, result.get(i)+result.get(key));
                } else {
                    result.put(i, result.get(key));
                }
            });
        });
        result.keySet().forEach(key -> System.out.println("Key " + key + " " + result.get(key)));
        System.out.println("Day 4 Exercise 2: " + result.keySet().stream().map(key -> result.get(key)).reduce(Integer::sum).orElse(0));
    }

    private static void exercise1(Collection<ScratchCard> cards) {
        var result = cards.stream().map(c -> c.ticket().score(c.winningNumbers())).reduce(Long::sum).orElse(0L);
        System.out.println("Day 4 Exercise 1: " + result);
    }

    private static class InvalidInput extends RuntimeException {
        public InvalidInput() {
            super("Invalid input");
        }
    }

    private static void createScratchCard(String l, Map<Integer, ScratchCard> cards) {
        var parts = l.split("[|:]");
        if (parts.length != 3) {
            throw new InvalidInput();
        }
        var card = Integer.parseInt(parts[0].substring(4).trim());
        var winning = getNumbers(parts[1]);
        var numbers = getNumbers(parts[2]);
        cards.put(card, new ScratchCard(winning, new Ticket(numbers)));
    }

    private static List<Integer> getNumbers(String part) {
        return Stream.of(part.trim().split("[ ]+"))
                .map(s -> Integer.parseInt(s)).toList();
    }
}

