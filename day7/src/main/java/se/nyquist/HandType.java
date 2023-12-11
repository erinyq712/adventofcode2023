package se.nyquist;

import java.util.List;
import java.util.stream.Collectors;

public enum HandType implements Comparable<HandType> {
    HIGHEST_CARD,
    ONE_PAIR,
    TWO_PAIRS,
    THREE,
    FULL_HOUSE,
    FOUR,
    FIVE;

    public static final HandType type(String hand) {
        return type(hand.chars().boxed().toList());
    }
    public static final HandType type(Hand hand) {
        return type(hand.cards());
    }

    public static final HandType type(List<Integer> cards) {
        var cardCounts = cards.stream().collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        if (cardCounts.values().stream().anyMatch(c -> 5 == c)) {
            return FIVE;
        } else if (cardCounts.values().stream().anyMatch(c -> 4 == c)) {
            return FOUR;
        } else if (cardCounts.values().stream().anyMatch(c -> 3 == c)) {
            if (cardCounts.values().stream().anyMatch(c -> 2 == c)) {
                return FULL_HOUSE;
            } else {
                return THREE;
            }
        } else if (cardCounts.values().stream().anyMatch(c -> 2 == c)) {
            if (cardCounts.values().stream().filter(c -> 2 == c).count() == 2) {
                return TWO_PAIRS;
            } else {
                return ONE_PAIR;
            }
        } else {
            return HIGHEST_CARD;
        }
    }
}
