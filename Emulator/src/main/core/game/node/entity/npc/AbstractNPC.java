package core.game.node.entity.npc;

import core.game.world.map.Location;
import core.plugin.Plugin;
import core.tools.Log;

import java.util.HashMap;
import java.util.Map;

import static core.api.ContentAPIKt.log;

/**
 * The type Abstract npc.
 */
public abstract class AbstractNPC extends NPC implements Plugin<Object> {

    /**
     * The constant mapping.
     */
    protected static Map<Integer, AbstractNPC> mapping = new HashMap<>();

    /**
     * Instantiates a new Abstract npc.
     *
     * @param id       the id
     * @param location the location
     */
    public AbstractNPC(int id, Location location) {
        this(id, location, true);
    }

    /**
     * Instantiates a new Abstract npc.
     *
     * @param id       the id
     * @param location the location
     * @param autowalk the autowalk
     */
    public AbstractNPC(int id, Location location, boolean autowalk) {
        super(id, location);
        super.setWalks(autowalk);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for (int id : getIds()) {
            if (mapping.containsKey(id)) {
                String name = mapping.get(id).getClass().getSimpleName();
                if (name != getClass().getSimpleName()) {
                    log(this.getClass(), Log.ERR, "[" + getClass().getSimpleName() + "] - Warning: Mapping already contained NPC id " + id + "! (" + name + ")");
                    continue;
                }
            }
            mapping.put(id, this);
        }
        return this;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    /**
     * Construct abstract npc.
     *
     * @param id       the id
     * @param location the location
     * @param objects  the objects
     * @return the abstract npc
     */
    public abstract AbstractNPC construct(int id, Location location, Object... objects);

    /**
     * Get ids int [ ].
     *
     * @return the int [ ]
     */
    public abstract int[] getIds();

    /**
     * For id abstract npc.
     *
     * @param npcId the npc id
     * @return the abstract npc
     */
    public static AbstractNPC forId(int npcId) {
        return mapping.get(npcId);
    }

}
