package se.nyquist;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HandTypeWithJokerWithJokerTest {
    @Test
    void type() {
        assertEquals(HandTypeWithJoker.FIVE, HandTypeWithJoker.type("AAAAA"));
        assertEquals(HandTypeWithJoker.FOUR, HandTypeWithJoker.type("AAAAK"));
        assertEquals(HandTypeWithJoker.THREE, HandTypeWithJoker.type("AAAKQ"));
        assertEquals(HandTypeWithJoker.FULL_HOUSE, HandTypeWithJoker.type("AAAKK"));
        assertEquals(HandTypeWithJoker.TWO_PAIRS, HandTypeWithJoker.type("QAAKK"));
        assertEquals(HandTypeWithJoker.THREE, HandTypeWithJoker.type("QAJKK"));
        assertEquals(HandTypeWithJoker.ONE_PAIR, HandTypeWithJoker.type("QAJKT"));
        assertEquals(HandTypeWithJoker.FIVE, HandTypeWithJoker.type("JJJJA"));
        assertEquals(HandTypeWithJoker.FIVE, HandTypeWithJoker.type("JJJAA"));
        assertEquals(HandTypeWithJoker.FOUR, HandTypeWithJoker.type("JJJAB"));
        assertEquals(HandTypeWithJoker.FOUR, HandTypeWithJoker.type("KTJJT"));
    }
}
