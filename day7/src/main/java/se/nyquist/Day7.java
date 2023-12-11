package se.nyquist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public class Day7 {
    public static void main(String[] args) {
        var input = "input.txt";
        // var input = "sample.txt";
        if (args.length > 0) {
            input = args[0];
        }
        try (var stream = Day7.class.getClassLoader().getResourceAsStream(input)) {
            if (stream != null) {
                var lines = new BufferedReader(new InputStreamReader(stream)).lines().toList();
                exercise("Day 7 Exercise 1: ", getPlayers(lines, s -> new Hand<HandType, CardType>(s, HandType::type, CardType::type)));
                exercise("Day 7 Exercise 2: ", getPlayers(lines, s -> new Hand<HandTypeWithJoker, CardTypeWithJoker>(s, HandTypeWithJoker::type, CardTypeWithJoker::type)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T extends Comparable<T>, C extends Comparable<C>> void exercise(String message, List<Player<Hand<T,C>>> players) {
        var ranking = players.stream().sorted(Comparator.comparing(Player::hand)).toList();
        System.out.println(message +
                IntStream.range(1, ranking.size()+1).mapToLong(i -> ranking.get(i-1).bid()*i).reduce(Long::sum).orElse(0L));
    }

    private static <T extends Comparable<T>> List<Player<T>> getPlayers(List<String> lines, Function<String, T> create) {
        return lines.stream().map(l -> {
            var input = l.split("\\s+");
            var bid = Long.parseLong(input[1]);
            return new Player<>(create.apply(input[0]), bid);
        }).toList();
    }


}

