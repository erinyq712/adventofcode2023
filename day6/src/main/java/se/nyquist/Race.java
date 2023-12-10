package se.nyquist;

public record Race(long loadTime) {
    long duration(RaceInfo info) {
        return (info.duration()-loadTime)*loadTime;
    }
}
