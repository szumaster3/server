package core.game.world.repository;

import core.game.node.Node;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The type Node list iterator.
 *
 * @param <E> the type parameter
 */
public class NodeListIterator<E extends Node> implements Iterator<E> {

    private Node[] nodes;

    private NodeList<E> entityList;

    private int lastIndex = -1;

    private int cursor = 0;

    private int size;

    /**
     * Instantiates a new Node list iterator.
     *
     * @param nodeList the node list
     */
    public NodeListIterator(NodeList<E> nodeList) {
        this.entityList = nodeList;
        nodes = nodeList.toArray(new Node[0]);
        size = nodes.length;
    }

    @Override
    public boolean hasNext() {
        while (cursor < size) {
            if (nodes[cursor] != null) {
                return true;
            }
            cursor++;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        lastIndex = cursor++;
        return (E) nodes[lastIndex];
    }

    @Override
    public void remove() {
        if (lastIndex == -1) {
            throw new IllegalStateException();
        }
        entityList.remove(nodes[lastIndex]);
    }

}
