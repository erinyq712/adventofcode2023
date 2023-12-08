package se.nyquist;

import java.util.List;

public record Ticket(List<Integer> numbers) {

    public long score(List<Integer> winningNumbers) {
        return getScoringNumbers(winningNumbers).stream().reduce(1L, (r,i) -> r * 2)/2;
    }

    public int scoringCount(List<Integer> winningNumbers) {
        return getScoringNumbers(winningNumbers).size();
    }

    private List<Long> getScoringNumbers(List<Integer> winningNumbers) {
        return numbers.stream().filter(winningNumbers::contains).map(n -> (long)n).toList();
    }
}
