package se.nyquist;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum CellType {
    VERTICAL('|'),
    HORIZONTAL('-'),
    NORTH_EAST('L'),
    NORTH_WEST('J'),
    SOUTH_EAST('F'),
    SOUTH_WEST('7'),
    GROUND('.'),
    START('S');

    private static EnumSet<CellType> verticals = EnumSet.of(CellType.VERTICAL, CellType.NORTH_EAST, CellType.NORTH_WEST, CellType.SOUTH_EAST, CellType.SOUTH_WEST);
    private static EnumSet<CellType> horizontals = EnumSet.of(CellType.HORIZONTAL, CellType.NORTH_EAST, CellType.NORTH_WEST, CellType.SOUTH_EAST, CellType.SOUTH_WEST);
    private final char symbol;

    CellType(char c) {
        this.symbol = c;
    }

    static final Map<Character,CellType> cellTypes = new HashMap<>();

    static {
        Arrays.stream(CellType.values()).forEach(c -> cellTypes.put(c.symbol, c));
    }

    public static CellType of(Character c) {
        if (! cellTypes.containsKey(c)) {
            throw new IllegalArgumentException(String.valueOf(c));
        }
        return cellTypes.get(c);
    }
}
