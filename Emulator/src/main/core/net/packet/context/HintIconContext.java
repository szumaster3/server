package core.net.packet.context;

import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.net.packet.Context;

/**
 * The type Hint icon context.
 */
public final class HintIconContext implements Context {

    private final Player player;

    private final int slot;
    private final int index;
    private final int modelId;
    private final Location location;
    private int targetType;
    private int arrowId;
    private int height;

    /**
     * Instantiates a new Hint icon context.
     *
     * @param player  the player
     * @param slot    the slot
     * @param arrowId the arrow id
     * @param target  the target
     * @param modelId the model id
     */
    public HintIconContext(Player player, int slot, int arrowId, Node target, int modelId) {
        this(player, slot, arrowId, -1, target, modelId);
        targetType = 2;
        if (target instanceof Entity) {
            targetType = target instanceof Player ? 10 : 1;
        }
    }

    /**
     * Instantiates a new Hint icon context.
     *
     * @param player     the player
     * @param slot       the slot
     * @param arrowId    the arrow id
     * @param targetType the target type
     * @param target     the target
     * @param modelId    the model id
     */
    public HintIconContext(Player player, int slot, int arrowId, int targetType, Node target, int modelId) {
        this(player, slot, arrowId, targetType, target, modelId, 0);
    }

    /**
     * Instantiates a new Hint icon context.
     *
     * @param player     the player
     * @param slot       the slot
     * @param arrowId    the arrow id
     * @param targetType the target type
     * @param target     the target
     * @param modelId    the model id
     * @param height     the height
     */
    public HintIconContext(Player player, int slot, int arrowId, int targetType, Node target, int modelId, int height) {
        this.player = player;
        this.slot = slot;
        this.targetType = targetType;
        this.arrowId = arrowId;
        this.modelId = modelId;
        this.height = height;
        if (target instanceof Entity) {
            this.index = ((Entity) target).getIndex();
            this.location = null;
        } else {
            this.location = target.getLocation();
            this.index = -1;
        }
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets slot.
     *
     * @return the slot
     */
    public int getSlot() {
        return slot;
    }

    /**
     * Gets target type.
     *
     * @return the target type
     */
    public int getTargetType() {
        return targetType;
    }

    /**
     * Sets target type.
     *
     * @param targetType the target type
     */
    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }

    /**
     * Gets arrow id.
     *
     * @return the arrow id
     */
    public int getArrowId() {
        return arrowId;
    }

    /**
     * Sets arrow id.
     *
     * @param arrowId the arrow id
     */
    public void setArrowId(int arrowId) {
        this.arrowId = arrowId;
    }

    /**
     * Gets index.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets model id.
     *
     * @return the model id
     */
    public int getModelId() {
        return modelId;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Gets height.
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets height.
     *
     * @param height the height
     */
    public void setHeight(int height) {
        this.height = height;
    }

}