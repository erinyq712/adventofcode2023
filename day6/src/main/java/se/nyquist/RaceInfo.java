package se.nyquist;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public record RaceInfo(long duration, long record) {
    List<Race> getRaces() {
        return LongStream.range(0,duration).mapToObj(Race::new).toList();
    }

    List<Race> getWins() {
        return getRaces().stream().filter(r -> r.duration(this) > record).toList();
    }
}
