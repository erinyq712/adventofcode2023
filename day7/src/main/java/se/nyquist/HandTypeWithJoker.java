package se.nyquist;

import java.util.List;
import java.util.stream.Collectors;

public enum HandTypeWithJoker implements Comparable<HandTypeWithJoker> {
    HIGHEST_CARD,
    ONE_PAIR,
    TWO_PAIRS,
    THREE,
    FULL_HOUSE,
    FOUR,
    FIVE;

    public static final HandTypeWithJoker type(String hand) {
        return type(hand.chars().boxed().toList());
    }
    public static final HandTypeWithJoker type(Hand hand) {
        return type(hand.cards());
    }

    public static final HandTypeWithJoker type(List<Integer> cards) {
        var cardCounts = cards.stream().collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        var jokers = cardCounts.containsKey((int)'J') ? cardCounts.get((int)'J') : 0;
        var pairsWithoutJokerCount = cardCounts.keySet().stream().filter(k -> k != (int)'J').filter(c -> 2 == cardCounts.get(c)).count();
        if (cardCounts.values().stream().anyMatch(c -> 5 == c)) {
            return FIVE;
        } else if (cardCounts.values().stream().anyMatch(c -> 4 == c)) {
            // Upgrade if Joker
            return jokers > 0 ? FIVE : FOUR;
        } else if (cardCounts.values().stream().anyMatch(c -> 3 == c)) {
            if (jokers == 3) {
                return pairsWithoutJokerCount > 0 ? FIVE : FOUR;
            } else if (jokers == 2) {
                return FIVE;
            } else if (jokers == 1) {
                return FULL_HOUSE;
            } else {
                if (cardCounts.values().stream().anyMatch(c -> 2 == c)) {
                    return FULL_HOUSE;
                } else {
                    return THREE;
                }
            }
        } else if (cardCounts.values().stream().anyMatch(c -> 2 == c)) {
            if (jokers == 2 && pairsWithoutJokerCount > 0) {
                return FOUR;
            } else if (jokers == 2) {
                return THREE;
            } else if (jokers == 1 && pairsWithoutJokerCount > 1) {
                return FULL_HOUSE;
            } else if (jokers == 1 && pairsWithoutJokerCount == 1) {
                return THREE;
            } else if (jokers == 0 && pairsWithoutJokerCount > 1) {
                return TWO_PAIRS;
            } else if (jokers == 0 && pairsWithoutJokerCount == 1) {
                return ONE_PAIR;
            }
            return ONE_PAIR;
        } else {
            return jokers == 1 ? ONE_PAIR : HIGHEST_CARD;
        }
    }
}
