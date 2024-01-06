package se.nyquist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class StateMachine {
    private final List<State> states;
    private List<State.Position> positions;

    private final String input;
    private final int counter;

    private List<Integer> errors;

    public StateMachine(String numbers, String input) {
        this.errors = Arrays.stream(numbers.split(",")).map(Integer::parseInt).toList();
        this.states = createStates(errors);
        this.positions = List.of(new State.Position(0, 0));
        this.input = input;
        this.counter = errors.size();
    }

    private static List<State> createStates(List<Integer> errors) {
        int statesCount = errors.stream().reduce(Integer::sum).orElse(0) + errors.size();
        var states = new ArrayList<State>(statesCount);
        int current = 0;
        while (current < errors.size()) {
            var broken = errors.get(current);
            if (broken == 1) {
                states.add(State.build().setFindFirst());
                states.add(State.build().setMatchOK());
            } else {
                states.add(State.build().setZeroOrMoreOK());
                for (int i = 0; i < broken-2; i++) {
                    states.add(State.build().setMatchBroken());
                }
                states.add(State.build().setMatchFinalBroken());
                states.add(State.build().setMatchOK());
            }
            current++;
        }
        states.add(State.build().setFinalOK());
        return states;
    }

    public State get(int current) {
        return states.get(current);
    }

    public void process(int c) {
        final int minCounter = getMinCounter();
        positions = positions.stream().flatMap(p -> p.getNext(c,this).stream()
                .filter(f -> f.counter() >= minCounter)
                .filter(f -> f.current() < states.size())).toList();
    }

    public long combinations() {
        input.chars().forEach(this::process);
        var headPositions = positions.stream()
                .filter(p -> p.counter()==counter)
                .collect(Collectors.groupingBy(State.Position::current));
        return headPositions.keySet().stream()
                .filter(key -> key == states.size()-2 || key == states.size()-1)
                .map(headPositions::get)
                .mapToLong(Collection::size)
                .reduce(Long::sum).orElse(0L);
    }


}
