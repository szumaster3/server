package core.game.world.map.path;

/**
 * The interface Clip mask supplier.
 */
@FunctionalInterface
public interface ClipMaskSupplier {

    /**
     * Gets clipping flag.
     *
     * @param z the z
     * @param x the x
     * @param y the y
     * @return the clipping flag
     */
    int getClippingFlag(int z, int x, int y);
}
