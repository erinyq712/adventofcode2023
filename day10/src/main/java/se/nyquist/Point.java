package se.nyquist;

public record Point(int x, int y) {
    public Point add(Point delta) {
        return new Point(this.x + delta.x, this.y + delta.y);
    }
}
