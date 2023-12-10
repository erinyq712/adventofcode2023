package se.nyquist;

import java.util.List;
import java.util.regex.Pattern;

public class Hand {
    final static Pattern invalidClass = Pattern.compile("[^AKQJT98765432]");

    private String cards;
    private HandType type;

    public Hand(String cards) {
        var matcher = invalidClass.matcher(cards);
        if (matcher.find()) {
            throw new InvalidHand(cards);
        }
        this.cards = cards;
        this.type = HandType.type(cards);
    }

    public String cards() {
        return cards;
    }

    public HandType type() {
        return type;
    }
}
