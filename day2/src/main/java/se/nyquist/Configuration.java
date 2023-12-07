package se.nyquist;

import java.math.BigDecimal;

public record Configuration(int blue, int green, int red) {
    public Configuration() {
        this(0, 0, 0);
    }

    public Configuration(int blue, int green, int red) {
        this.blue = blue;
        this.green = green;
        this.red = red;
        assertPositive(blue);
        assertPositive(green);
        assertPositive(red);
    }

    private void assertPositive(int number) {
        if (number < 0) throw new Configuration.Invalid();
    }

    public boolean isSubset(Configuration c) {
        return c.blue <= blue && c.green <= green && c.red <= red;
    }

    public static class Invalid extends RuntimeException {
        public Invalid() {
            super("Invalid configuration");
        }
    }

    public BigDecimal power() {
        return BigDecimal.valueOf(blue).multiply(BigDecimal.valueOf(green)).multiply(BigDecimal.valueOf(red));
    }
}
