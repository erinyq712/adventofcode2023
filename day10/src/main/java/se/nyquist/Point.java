package se.nyquist;

public record Point(int x, int y) {
    public Point add(Point delta) {
        return new Point(this.x + delta.x, this.y + delta.y);
    }

    public Point addY(int y) {
        return new Point(this.x, this.y + y);
    }

    public Point addX(int x) {
        return new Point(this.x + x, this.y);
    }

    public Point delta(Point start) {
        return new Point(this.x - start.x, this.y - start.y);
    }

    public Point extend(int i) {
        return new Point(this.x + i, this.y + i);
    }
}
