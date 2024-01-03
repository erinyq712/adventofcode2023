package se.nyquist;

import java.util.ArrayList;
import java.util.List;

public record UnknownNode(List<Node> alternatives) implements Node {

    public UnknownNode() {
        this(new ArrayList<>());
    }

    @Override
    public boolean hasChildren() {
        return true;
    }

    @Override
    public List<Node> getChildren() {
        return alternatives;
    }
}
