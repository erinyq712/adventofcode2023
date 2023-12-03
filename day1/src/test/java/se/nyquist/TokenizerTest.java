package se.nyquist;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {
    @Test
    void allNamed() {
        Tokenizer tokenizer = new Tokenizer("onetwothreefourfivesixseveneightnine");
        assertEquals("123456789", tokenizer.getValue());
    }

    @Test
    void allNamed2() {
        Tokenizer tokenizer = new Tokenizer("aaanineeightetetsevenfhfhsixfivefourthreejdjdjdtwooone");
        assertEquals("aaa98etet7fhfh6543jdjdjd2o1", tokenizer.getValue());
    }

    @Test
    void mixed() {
        Tokenizer tokenizer = new Tokenizer("one2three4five6seven8nine");
        assertEquals("123456789", tokenizer.getValue());
    }
    @Test
    void mixed2() {
        Tokenizer tokenizer = new Tokenizer("12three4five6seven89");
        assertEquals("123456789", tokenizer.getValue());
    }

}
