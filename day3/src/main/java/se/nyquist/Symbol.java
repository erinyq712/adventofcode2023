package se.nyquist;

import java.util.List;

public record Symbol(String symbol, int row, int col) {

    public int gearNumber(List<PartNumber> parts) {
        var adjacentParts = parts.stream().filter(p -> p.isAdjacent(this)).toList();
        return adjacentParts.size()==2 ? adjacentParts.stream().map(PartNumber::value).reduce(1, (p,a) -> p * a) : 0;
    }
}
