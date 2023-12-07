package se.nyquist;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public record PartNumber(int value, int row, int mincol, int maxcol) {

    public PartNumber(int value, int row, int mincol, int maxcol) {
            this.value = value;
            this.row = row;
            this.mincol = mincol;
            this.maxcol = maxcol;
            assertPositive(value);
            assertPositive(row);
            assertPositive(mincol);
            assertPositive(maxcol);

        }

    public boolean isAdjacent(List<Symbol> symbols) {
        return symbols.stream().anyMatch(s -> isAdjacent(s));
    }

    public boolean isAdjacent(Symbol s) {
        return abs(s.row()-row) < 2 && (abs(s.col()-mincol) < 2 || abs(s.col()-maxcol) < 2 );
    }

    public static class Invalid extends RuntimeException {
        public Invalid() {
            super("Invalid part number");
        }
    }
    private void assertPositive(int number) {
            if (number < 0) throw new PartNumber.Invalid();
        }

}
