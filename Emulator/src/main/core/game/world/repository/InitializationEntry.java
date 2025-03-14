package core.game.world.repository;

import core.game.node.Node;

/**
 * The type Initialization entry.
 */
public class InitializationEntry {

    private final Node node;

    private boolean removal;

    /**
     * Instantiates a new Initialization entry.
     *
     * @param node the node
     */
    public InitializationEntry(Node node) {
        this(node, false);
    }

    /**
     * Instantiates a new Initialization entry.
     *
     * @param node    the node
     * @param removal the removal
     */
    public InitializationEntry(Node node, boolean removal) {
        this.node = node;
        this.removal = removal;
    }

    /**
     * Initialize node.
     *
     * @return the node
     */
    public Node initialize() {
        node.setActive(true);
        return node;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof InitializationEntry)) {
            return false;
        }
        return ((InitializationEntry) o).node.equals(node);
    }

    @Override
    public int hashCode() {
        if (node != null) {
            return node.hashCode();
        }
        return super.hashCode();
    }

    /**
     * Gets node.
     *
     * @return the node
     */
    public Node getNode() {
        return node;
    }

    /**
     * Is removal boolean.
     *
     * @return the boolean
     */
    public boolean isRemoval() {
        return removal;
    }

    /**
     * Sets removal.
     *
     * @param removal the removal
     */
    public void setRemoval(boolean removal) {
        this.removal = removal;
    }
}