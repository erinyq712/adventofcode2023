package se.nyquist;

import java.util.function.Function;
import java.util.regex.Pattern;

public class Hand<T extends Comparable<T>, C extends Comparable<C>> implements Comparable<Hand<T, C>> {
    final static Pattern invalidClass = Pattern.compile("[^AKQJT98765432]");

    private String cards;
    private T type;

    private Function<Character, C> cardType;

    public Hand(String cards, Function<String, T> type, Function<Character, C> cardType) {
        var matcher = invalidClass.matcher(cards);
        if (matcher.find()) {
            throw new InvalidHand(cards);
        }
        this.cards = cards;
        this.type = type.apply(cards);
        this.cardType = cardType;
    }

    public String cards() {
        return cards;
    }

    public T type() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Hand h) {
            return cards.equals(h.cards);
        }
        return false;
    }

    @Override
    public int compareTo(Hand<T,C> o) {
        var typeCompare = type.compareTo(o.type);
        if (typeCompare != 0) {
            return typeCompare;
        } else {
            for(int i = 0; i < 5; i++) {
                var cardCompare = cardType.apply(cards.charAt(i)).compareTo(cardType.apply(o.cards.charAt(i)));
                if (cardCompare != 0) {
                    return cardCompare;
                }
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Hand{" +
                "cards='" + cards + '\'' +
                ", type=" + type +
                '}';
    }
}
