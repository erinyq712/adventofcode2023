package se.nyquist;

public class InvalidHand extends RuntimeException {
    public InvalidHand(String cards) {
        super("Invalid hand: " + cards);
    }
}
