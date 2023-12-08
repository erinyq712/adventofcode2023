package se.nyquist;

import java.util.List;

public record ScratchCard(List<Integer> winningNumbers, Ticket ticket) {
}
