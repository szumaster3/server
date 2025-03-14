package core.game.world.update.flag;

import core.game.node.entity.Entity;
import core.net.packet.IoBuffer;

/**
 * The type Update flag.
 *
 * @param <T> the type parameter
 */
public abstract class UpdateFlag<T> implements Comparable<UpdateFlag<?>> {

    /**
     * The Context.
     */
    protected T context;

    /**
     * Instantiates a new Update flag.
     *
     * @param context the context
     */
    public UpdateFlag(T context) {
        this.context = context;
    }

    /**
     * Write.
     *
     * @param buffer the buffer
     */
    public abstract void write(IoBuffer buffer);

    /**
     * Write dynamic.
     *
     * @param buffer the buffer
     * @param e      the e
     */
    public void writeDynamic(IoBuffer buffer, Entity e) {
        write(buffer);
    }

    /**
     * Data int.
     *
     * @return the int
     */
    public abstract int data();

    /**
     * Ordinal int.
     *
     * @return the int
     */
    public abstract int ordinal();

    @Override
    public int compareTo(UpdateFlag<?> flag) {
        if (flag.ordinal() == ordinal()) {
            return 0;
        }
        if (flag.ordinal() < ordinal()) {
            return 1;
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UpdateFlag)) {
            return false;
        }
        return ((UpdateFlag<?>) o).data() == data() && ((UpdateFlag<?>) o).ordinal() == ordinal();
    }

    /**
     * Gets context.
     *
     * @return the context
     */
    public T getContext() {
        return context;
    }

    /**
     * Sets context.
     *
     * @param context the context
     */
    public void setContext(T context) {
        this.context = context;
    }
}
