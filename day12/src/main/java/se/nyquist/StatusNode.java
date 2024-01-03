package se.nyquist;

import java.util.List;

public record StatusNode(boolean ok) implements Node {
    @Override
    public boolean hasChildren() {
        return true;
    }

    @Override
    public List<Node> getChildren() {
        return List.of();
    }
}
