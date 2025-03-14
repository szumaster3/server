package core.game.node.scenery;

import core.cache.def.impl.SceneryDefinition;
import core.cache.def.impl.VarbitDefinition;
import core.game.interaction.DestinationFlag;
import core.game.interaction.InteractPlugin;
import core.game.node.Node;
import core.game.node.entity.impl.GameAttributes;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static core.api.ContentAPIKt.setVarbit;
import static core.api.ContentAPIKt.setVarp;

/**
 * The type Scenery.
 */
public class Scenery extends Node {

    private final int id;

    private final int type;

    private int rotation;

    private final SceneryDefinition definition;

    private Pulse restorePulse;

    private Pulse destructionPulse;

    private int charge = 1000;

    private final GameAttributes attributes = new GameAttributes();

    private final Scenery[] childs;

    private Scenery wrapper;

    /**
     * Instantiates a new Scenery.
     *
     * @param id the id
     * @param x  the x
     * @param y  the y
     * @param z  the z
     */
    public Scenery(int id, int x, int y, int z) {
        this(id, Location.create(x, y, z), 10, 0);
    }

    /**
     * Instantiates a new Scenery.
     *
     * @param id       the id
     * @param location the location
     */
    public Scenery(int id, Location location) {
        this(id, location, 10, 0);
    }

    /**
     * Instantiates a new Scenery.
     *
     * @param id       the id
     * @param location the location
     * @param rotation the rotation
     */
    public Scenery(int id, Location location, int rotation) {
        this(id, location, 10, rotation);
    }

    /**
     * Instantiates a new Scenery.
     *
     * @param id        the id
     * @param location  the location
     * @param rotation  the rotation
     * @param direction the direction
     */
    public Scenery(int id, Location location, int rotation, Direction direction) {
        this(id, location, 10, rotation);
    }

    /**
     * Instantiates a new Scenery.
     *
     * @param id       the id
     * @param x        the x
     * @param y        the y
     * @param z        the z
     * @param type     the type
     * @param rotation the rotation
     */
    public Scenery(int id, int x, int y, int z, int type, int rotation) {
        this(id, Location.create(x, y, z), type, rotation);
    }

    /**
     * Instantiates a new Scenery.
     *
     * @param id       the id
     * @param type     the type
     * @param rotation the rotation
     */
    public Scenery(int id, int type, int rotation) {
        this(id, Location.create(0, 0, 0), type, rotation);
    }

    /**
     * Instantiates a new Scenery.
     *
     * @param id       the id
     * @param location the location
     * @param type     the type
     * @param rotation the rotation
     */
    public Scenery(int id, Location location, int type, int rotation) {
        super(SceneryDefinition.forId(id).getName(), location);
        if (rotation < 0) {
            rotation += 4;
        }
        if (id < 1) {
            type = 22;
        }
        super.destinationFlag = DestinationFlag.OBJECT;
        super.direction = Direction.get(rotation);
        super.interactPlugin = new InteractPlugin(this);
        this.rotation = rotation;
        this.id = id;
        this.location = location;
        this.type = type;
        this.definition = SceneryDefinition.forId(id);
        super.size = definition.sizeX;
        if (definition.configObjectIds != null && definition.configObjectIds.length > 0) {
            this.childs = new Scenery[definition.configObjectIds.length];
            for (int i = 0; i < childs.length; i++) {
                childs[i] = transform(definition.configObjectIds[i]);
                childs[i].wrapper = this;
            }
        } else {
            childs = null;
        }
    }

    /**
     * Instantiates a new Scenery.
     *
     * @param other the other
     */
    public Scenery(Scenery other) {
        this(other.getId(), other.getLocation(), other.getType(), other.getRotation());
    }

    /**
     * Remove.
     */
    public void remove() {
    }

    /**
     * Gets size x.
     *
     * @return the size x
     */
    public int getSizeX() {
        if (direction.toInteger() % 2 != 0) {
            return definition.sizeY;
        }
        return definition.sizeX;
    }

    /**
     * Gets size y.
     *
     * @return the size y
     */
    public int getSizeY() {
        if (direction.toInteger() % 2 != 0) {
            return definition.sizeX;
        }
        return definition.sizeY;
    }

    @Override
    public void setActive(boolean active) {
        if (super.active && !active && destructionPulse != null) {
            destructionPulse.pulse();
        }
        super.setActive(active);
    }

    /**
     * Gets child.
     *
     * @param player the player
     * @return the child
     */
    public Scenery getChild(Player player) {
        if (childs != null) {
            SceneryDefinition def = definition.getChildObject(player);
            for (Scenery child : childs) {
                if (child.getId() == def.getId()) {
                    return child;
                }
            }
        }
        return this;
    }

    /**
     * Sets child index.
     *
     * @param player the player
     * @param index  the index
     */
    public void setChildIndex(Player player, int index) {
        SceneryDefinition def = getDefinition();
        if (childs == null && wrapper != null) {
            def = wrapper.getDefinition();
        }
        if (def.getVarbitID() > -1) {
            VarbitDefinition config = def.getConfigFile();
            if (config != null) {
                setVarbit(player, def.getVarbitID(), index);
            }
        } else if (def.getConfigId() > -1) {
            setVarp(player, def.getConfigId(), index);
        }
    }

    /**
     * Transform scenery.
     *
     * @param id the id
     * @return the scenery
     */
    public Scenery transform(int id) {
        return new Scenery(id, location, type, rotation);
    }

    /**
     * Transform scenery.
     *
     * @param id       the id
     * @param rotation the rotation
     * @return the scenery
     */
    public Scenery transform(int id, int rotation) {
        return new Scenery(id, location, type, rotation);
    }

    /**
     * Transform scenery.
     *
     * @param id       the id
     * @param rotation the rotation
     * @param location the location
     * @return the scenery
     */
    public Scenery transform(int id, int rotation, Location location) {
        return new Scenery(id, location, type, rotation);
    }

    /**
     * Transform scenery.
     *
     * @param id       the id
     * @param rotation the rotation
     * @param type     the type
     * @return the scenery
     */
    public Scenery transform(int id, int rotation, int type) {
        return new Scenery(id, location, type, rotation);
    }

    /**
     * Is permanent boolean.
     *
     * @return the boolean
     */
    public boolean isPermanent() {
        return true;
    }

    /**
     * As constructed constructed.
     *
     * @return the constructed
     */
    public Constructed asConstructed() {
        return new Constructed(id, location, type, rotation);
    }

    public int getId() {
        return id;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * Gets rotation.
     *
     * @return the rotation
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * Sets rotation.
     *
     * @param rot the rot
     */
    public void setRotation(int rot) {
        rotation = rot;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    /**
     * Gets definition.
     *
     * @return the definition
     */
    public SceneryDefinition getDefinition() {
        return definition;
    }

    @Override
    public Location getCenterLocation() {
        return location.transform(getSizeX() >> 1, getSizeY() >> 1, 0);
    }

    @Override
    public boolean equals(java.lang.Object obj) {
        if (obj == null || !(obj instanceof Scenery)) {
            return false;
        }
        Scenery other = (Scenery) obj;
        return other.id == id && other.location.equals(location) && rotation == other.rotation && other.type == type;
    }

    @Override
    public String toString() {
        return "[Scenery " + id + ", " + location + ", type=" + type + ", rot=" + rotation + "]";
    }

    /**
     * Gets restore pulse.
     *
     * @return the restore pulse
     */
    public Pulse getRestorePulse() {
        return restorePulse;
    }

    /**
     * Sets restore pulse.
     *
     * @param restorePulse the restore pulse
     */
    public void setRestorePulse(Pulse restorePulse) {
        this.restorePulse = restorePulse;
    }

    /**
     * Gets charge.
     *
     * @return the charge
     */
    public int getCharge() {
        return charge;
    }

    /**
     * Sets charge.
     *
     * @param charge the charge
     */
    public void setCharge(int charge) {
        this.charge = charge;
    }

    /**
     * Gets destruction pulse.
     *
     * @return the destruction pulse
     */
    public Pulse getDestructionPulse() {
        return destructionPulse;
    }

    /**
     * Sets destruction pulse.
     *
     * @param destructionPulse the destruction pulse
     */
    public void setDestructionPulse(Pulse destructionPulse) {
        this.destructionPulse = destructionPulse;
    }

    /**
     * Gets attributes.
     *
     * @return the attributes
     */
    public GameAttributes getAttributes() {
        return attributes;
    }

    /**
     * Get childs scenery [ ].
     *
     * @return the scenery [ ]
     */
    public Scenery[] getChilds() {
        return childs;
    }

    /**
     * Gets wrapper.
     *
     * @return the wrapper
     */
    public Scenery getWrapper() {
        if (wrapper == null) {
            return this;
        }
        return wrapper;
    }

    /**
     * Sets wrapper.
     *
     * @param wrapper the wrapper
     */
    public void setWrapper(Scenery wrapper) {
        this.wrapper = wrapper;
    }

    /**
     * Gets occupied tiles.
     *
     * @return the occupied tiles
     */
    @SuppressWarnings("SuspiciousNameCombination")
    @NotNull
    public List<Location> getOccupiedTiles() {
        List<Location> occupied = new ArrayList<>();
        occupied.add(location);

        int sizeX = getSizeX();
        int sizeY = getSizeY();

        if (rotation % 2 == 1) {
            int tmp = sizeX;
            sizeX = sizeY;
            sizeY = tmp;
        }

        boolean sub = rotation >= 2;

        if (sizeX > 1) {
            for (int i = 1; i < sizeX; i++) {
                int modifier = sub ? -i : i;
                occupied.add(location.transform(modifier, 0, 0));
            }
        }

        if (sizeY > 1) {
            for (int i = 1; i < sizeY; i++) {
                int modifier = sub ? -i : i;
                occupied.add(location.transform(0, modifier, 0));
            }
        }

        return occupied;
    }
}
