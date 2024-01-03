package se.nyquist;

import java.util.List;

public sealed interface Node permits StatusNode, UnknownNode {
    boolean hasChildren();
    List<Node> getChildren();
}
