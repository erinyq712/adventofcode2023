package se.nyquist;

import java.util.List;
import java.util.stream.Collectors;

public enum HandType {
    FIVE(6),
    FOUR(5),
    FULL_HOUSE(4),
    THREE(3),
    TWO_PAIRS(2),
    ONE_PAIR(1),
    HIGHEST_CARD(0);

    private final int rank;

    HandType(int rank) {
        this.rank = rank;
    }

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
