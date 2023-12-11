package se.nyquist;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HandTypeWithJokerWithJokerTest {
    @Test
    void noJoker() {
        // No joker
        assertEquals(HandTypeWithJoker.FIVE, HandTypeWithJoker.type("AAAAA"));
        assertEquals(HandTypeWithJoker.FOUR, HandTypeWithJoker.type("AAAAK"));
        assertEquals(HandTypeWithJoker.THREE, HandTypeWithJoker.type("AAAKQ"));
        assertEquals(HandTypeWithJoker.FULL_HOUSE, HandTypeWithJoker.type("AAAKK"));
        assertEquals(HandTypeWithJoker.TWO_PAIRS, HandTypeWithJoker.type("QAAKK"));
    }
    @Test
    void oneJoker() {
        assertEquals(HandTypeWithJoker.FIVE, HandTypeWithJoker.type("KKJKK"));
        assertEquals(HandTypeWithJoker.FOUR, HandTypeWithJoker.type("QKJKK"));
        assertEquals(HandTypeWithJoker.THREE, HandTypeWithJoker.type("QAJKK"));
        assertEquals(HandTypeWithJoker.FULL_HOUSE, HandTypeWithJoker.type("AAJKK"));
        assertEquals(HandTypeWithJoker.THREE, HandTypeWithJoker.type("QAJKK"));
        assertEquals(HandTypeWithJoker.ONE_PAIR, HandTypeWithJoker.type("QAJKT"));
    }
    @Test
    void twoJokers() {
        assertEquals(HandTypeWithJoker.FIVE, HandTypeWithJoker.type("TTJJT"));
        assertEquals(HandTypeWithJoker.FOUR, HandTypeWithJoker.type("KTJJT"));
        assertEquals(HandTypeWithJoker.FOUR, HandTypeWithJoker.type("QQJJT"));
        assertEquals(HandTypeWithJoker.THREE, HandTypeWithJoker.type("KQJJT"));
    }
    @Test
    void threeJokers() {
        assertEquals(HandTypeWithJoker.FIVE, HandTypeWithJoker.type("JJJAA"));
        assertEquals(HandTypeWithJoker.FOUR, HandTypeWithJoker.type("JJJAB"));
    }
}
