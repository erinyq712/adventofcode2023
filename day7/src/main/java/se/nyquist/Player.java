package se.nyquist;

public record Player<T extends Comparable<T>>(T hand, long bid) {
}
