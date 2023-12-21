package se.nyquist;

public enum Movement {
    RIGHT,
    LEFT,
    UP,
    DOWN;

    static Movement getMovement(Point delta) {
        if (delta.y() == 0 && delta.x() < 0) {
            return LEFT;
        } else if (delta.y() == 0 && delta.x() > 0) {
            return RIGHT;
        } else if (delta.y() > 0 && delta.x() == 0) {
            return DOWN;
        } else {
            return UP;
        }
    }
}
