package se.nyquist;

import java.util.List;

public record Races(List<RaceInfo> raceInfos) {
    public List<List<Race>> getWins() {
        return raceInfos.stream().map(RaceInfo::getWins).toList();
    }

    public List<Long> getWinCounts() {
        return getWins().stream().mapToLong(List::size).boxed().toList();
    }
}
