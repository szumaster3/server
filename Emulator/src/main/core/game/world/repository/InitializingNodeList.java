package core.game.world.repository;

import core.game.node.Node;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The type Initializing node list.
 *
 * @param <T> the type parameter
 */
public final class InitializingNodeList<T extends Node> extends ArrayList<T> {

    private static final long serialVersionUID = 7727358901001709156L;

    private final Queue<InitializationEntry> queue = new ConcurrentLinkedQueue<>();

    /**
     * Instantiates a new Initializing node list.
     */
    public InitializingNodeList() {
        super();
    }

    /**
     * Sync.
     */
    @SuppressWarnings("unchecked")
    public void sync() {
        while (!queue.isEmpty()) {
            InitializationEntry entry = queue.poll();
            if (entry.isRemoval()) {
                super.remove(entry.getNode());
                entry.getNode().setRenderable(false);
            } else {
                Node n = entry.initialize();
                if (n != null) {
                    super.add((T) n);
                    n.setRenderable(true);
                }
            }
        }
    }

    @Override
    public boolean add(T node) {
        return !contains(node) && queue.add(new InitializationEntry(node, false));
    }

    @Override
    public boolean remove(Object node) {
        if (!contains(node)) {
            return false;
        }
        return queue.add(new InitializationEntry((Node) node, true));
    }
}