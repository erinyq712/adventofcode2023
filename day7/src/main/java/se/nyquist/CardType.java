package se.nyquist;

import java.util.HashMap;
import java.util.Map;

public enum CardType {
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    KNIGHT,
    QUEEN,
    KING,
    ACE;

    private static Map<Character, CardType> valueMap = new HashMap<>();
    static {
        valueMap.put('2', TWO);
        valueMap.put('3', THREE);
        valueMap.put('4', FOUR);
        valueMap.put('5', FIVE);
        valueMap.put('6', SIX);
        valueMap.put('7', SEVEN);
        valueMap.put('8', EIGHT);
        valueMap.put('9', NINE);
        valueMap.put('T', TEN);
        valueMap.put('J', KNIGHT);
        valueMap.put('Q', QUEEN);
        valueMap.put('K', KING);
        valueMap.put('A', ACE);
    }

    public static CardType type(Character c) {
        return valueMap.get(c);
    }
}
