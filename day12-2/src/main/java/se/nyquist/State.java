package se.nyquist;

import java.util.List;
import java.util.function.IntFunction;

public class State {

    public void increment() {
        counter++;
    }

    public int counter() {
        return counter;
    }

    public State setFindFirst() {
        description = "setFindFirst";
        transitionRule = State::processFindFirst;
        return this;
    }

    public State setFinalOK() {
        description = "setFinalOK";
        transitionRule = State::processFinalOK;
        return this;
    }

    public static record Position(int current, int counter) {

        List<Position> getNext(int c, StateMachine stateMachine) {
            var currentState = stateMachine.get(current);
            return switch(currentState.getNext(c)) {
                case FOUND -> List.of(new Position(current+1, counter+1));
                case STAY -> List.of(this);
                case SPLITANDFOUND -> List.of(this, new Position(current+1, counter+1));
                case SPLIT -> List.of(this, new Position(current+1, counter));
                case NEXT -> List.of(new Position(current+1, counter));
                default -> List.of();
            };
        }
    }

    private Action getNext(int c) {
        return transitionRule.apply(c);
    }

    public State setZeroOrMoreOK() {
        description = "setZeroOrMoreOK";
        transitionRule = State::processZeroOrMoreOk;
        return this;
    }

    public State setMatchBroken() {
        description = "setMatchBroken";
        transitionRule = State::matchBroken;
        return this;
    }

    public State setMatchOK() {
        description = "setMatchOK";
        transitionRule = State::processMatchOK;
        return this;
    }

    public State setMatchFinalBroken() {
        description = "setMatchFinalBroken";
        transitionRule = State::matchFinalBroken;
        return this;
    }

    enum Action {
        STAY, NEXT, STOP, SPLIT, SPLITANDFOUND, FOUND
    }

    private static Action defaultTransition(int c) {
        return Action.STOP;
    }

    public static State build(int counter) {
        return new State(counter);
    }

    public static State build() {
        return new State(0);
    }

    private static Action processZeroOrMoreOk(int c) {
        return switch(c) {
            case '.' -> Action.STAY;
            case '#' -> Action.NEXT;
            case '?' -> Action.SPLIT;
            default -> Action.STOP;
        };
    }

    private static Action processFindFirst(int c) {
        return switch(c) {
            case '.' -> Action.STAY;
            case '#' -> Action.FOUND;
            case '?' -> Action.SPLITANDFOUND;
            default -> Action.STOP;
        };
    }

    private static Action processMatchOK(int c) {
        return switch(c) {
            case '.' -> Action.NEXT;
            case '#' -> Action.STOP;
            case '?' -> Action.NEXT;
            default -> Action.STOP;
        };
    }

    private static Action matchBroken(int c) {
        return switch(c) {
            case '.' -> Action.STOP;
            case '#' -> Action.NEXT;
            case '?' -> Action.NEXT;
            default -> Action.STOP;
        };
    }

    private static Action matchFinalBroken(int c) {
        return switch(c) {
            case '.' -> Action.STOP;
            case '#' -> Action.FOUND;
            case '?' -> Action.FOUND;
            default -> Action.STOP;
        };
    }


    private static Action processFinalOK(int c) {
        return switch(c) {
            case '.' -> Action.STAY;
            case '#' -> Action.STOP;
            case '?' -> Action.STAY;
            default -> Action.STOP;
        };
    }

    String description;

    IntFunction<Action> transitionRule;
    int counter;

    public State(int counter) {
        transitionRule = State::defaultTransition;
        description = "Default";
        this.counter = counter;
    }
}
