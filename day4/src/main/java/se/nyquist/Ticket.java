package se.nyquist;

import java.util.List;

public record Ticket(List<Integer> numbers) {
    public Long score(List<Integer> winningNumbers) {
        var scoringNumbers = numbers.stream().filter(winningNumbers::contains).map(n -> (long)n).toList();
        return scoringNumbers.stream().reduce(1L, (r,i) -> r * 2)/2;
    }
}
