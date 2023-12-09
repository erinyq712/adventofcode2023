package se.nyquist;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RangeTest {
    @Test
    void intersection() {
        var r1 = new Range(55,14);
        var r2 = new Range(46, 2);
        var r3 = new Range(46, 14);
        var r4 = new Range(55,5);
        assertEquals(Range.NULL,r1.intersection(r2));
        assertEquals(r4,r1.intersection(r3));
        assertEquals(r2,r2.intersection(r3));
    }

    @Test
    void subtract() {
        var r1 = new Range(55,14);
        var r2 = new Range(46, 2);
        var r3 = new Range(46, 14);
        var r4 = new Range(55,5);
        var r5 = new Range(60,9);
        var r6 = new Range(48,12);
        var s1 = r1.subtract(r2);
        assertEquals(1, s1.size());
        assertEquals(r1, s1.get(0));
        var s2 = r1.subtract(r3);
        assertEquals(1, s2.size());
        assertEquals(r5, s2.get(0));
        var s3 = r3.subtract(r2);
        assertEquals(1, s3.size());
        assertEquals(r6,s3.get(0));
    }

    @Test
    void subtract2() {
        var r1 = new Range(1141059215,246466925);
        var r2 = new Range(1217101081, 21950620);
        var r3 = new Range(1141059215, 76041866);
        var r4 = new Range(1239051701, 148474439);
        var s1 = r1.subtract(r2);
        assertEquals(2, s1.size());
        assertEquals(r3, s1.get(0));
        assertEquals(r4, s1.get(1));
        var s2 = r1.subtract(r3);
    }

}
