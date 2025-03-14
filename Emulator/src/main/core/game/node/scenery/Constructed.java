package core.game.node.scenery;

import core.game.node.item.Item;
import core.game.world.map.Location;

/**
 * The type Constructed.
 */
public class Constructed extends Scenery {

    private Scenery replaced;

    private Item[] items;

    /**
     * Instantiates a new Constructed.
     *
     * @param id the id
     * @param x  the x
     * @param y  the y
     * @param z  the z
     */
    public Constructed(int id, int x, int y, int z) {
        super(id, Location.create(x, y, z), 10, 0);
    }

    /**
     * Instantiates a new Constructed.
     *
     * @param id       the id
     * @param location the location
     */
    public Constructed(int id, Location location) {
        super(id, location, 10, 0);
    }

    /**
     * Instantiates a new Constructed.
     *
     * @param id       the id
     * @param location the location
     * @param rotation the rotation
     */
    public Constructed(int id, Location location, int rotation) {
        super(id, location, 10, rotation);
    }

    /**
     * Instantiates a new Constructed.
     *
     * @param id       the id
     * @param x        the x
     * @param y        the y
     * @param z        the z
     * @param type     the type
     * @param rotation the rotation
     */
    public Constructed(int id, int x, int y, int z, int type, int rotation) {
        super(id, Location.create(x, y, z), type, rotation);
    }

    /**
     * Instantiates a new Constructed.
     *
     * @param id       the id
     * @param type     the type
     * @param rotation the rotation
     */
    public Constructed(int id, int type, int rotation) {
        super(id, Location.create(0, 0, 0), type, rotation);
    }

    /**
     * Instantiates a new Constructed.
     *
     * @param id       the id
     * @param location the location
     * @param type     the type
     * @param rotation the rotation
     */
    public Constructed(int id, Location location, int type, int rotation) {
        super(id, location, type, rotation);
    }

    @Override
    public boolean isPermanent() {
        return false;
    }

    @Override
    public Constructed asConstructed() {
        return this;
    }

    /**
     * Gets replaced.
     *
     * @return the replaced
     */
    public Scenery getReplaced() {
        return replaced;
    }

    /**
     * Sets replaced.
     *
     * @param replaced the replaced
     */
    public void setReplaced(Scenery replaced) {
        this.replaced = replaced;
    }

    /**
     * Get items item [ ].
     *
     * @return the item [ ]
     */
    public Item[] getItems() {
        return items;
    }

    /**
     * Sets items.
     *
     * @param items the items
     */
    public void setItems(Item[] items) {
        this.items = items;
    }
}
