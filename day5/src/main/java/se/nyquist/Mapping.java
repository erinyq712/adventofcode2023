package se.nyquist;

public record Mapping(long destination, long start, long length) {
    public Range getDestinationRange(Range r) {
        var destStart = destination + r.start()-start;
        return new Range(destStart, r.length());
    }
}
