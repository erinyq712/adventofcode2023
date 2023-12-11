package se.nyquist;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HandTypeTest {
    @Test
    void type() {
        assertEquals(HandType.FIVE, HandType.type("AAAAA"));
        assertEquals(HandType.FOUR, HandType.type("AAAAK"));
        assertEquals(HandType.THREE, HandType.type("AAAKQ"));
        assertEquals(HandType.FULL_HOUSE, HandType.type("AAAKK"));
        assertEquals(HandType.TWO_PAIRS, HandType.type("QAAKK"));
        assertEquals(HandType.ONE_PAIR, HandType.type("QAJKK"));
        assertEquals(HandType.HIGHEST_CARD, HandType.type("QAJKT"));
    }
}
