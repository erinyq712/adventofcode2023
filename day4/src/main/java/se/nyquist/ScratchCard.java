package se.nyquist;

import java.util.List;

public record ScratchCard(List<Integer> winningNumbers, Ticket ticket) {
    public long score() {
        return ticket.score(winningNumbers);
    }

    public int scoringCount() {
        return ticket.scoringCount(winningNumbers);
    }
}
