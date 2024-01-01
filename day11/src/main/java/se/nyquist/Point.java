package se.nyquist;

public record Point(int x, int y) {
    public static int compare(Point p1, Point p2) {
        var yCompare = Long.compare(p1.y,p2.y);
        return yCompare == 0 ? Long.compare(p1.x,p2.x) : yCompare;
    }

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
