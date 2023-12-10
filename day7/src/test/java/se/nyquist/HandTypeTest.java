package se.nyquist;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HandTypeTest {
    @Test
    void type() {
        assertEquals(HandType.FIVE, HandType.type("AAAAA"));
    }
}
