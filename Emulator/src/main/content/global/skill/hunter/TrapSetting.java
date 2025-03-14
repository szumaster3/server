package content.global.skill.hunter;

import core.cache.def.impl.ItemDefinition;
import core.cache.def.impl.SceneryDefinition;
import core.game.node.Node;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.GroundItem;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.tools.RandomFunction;
import org.rs.consts.Sounds;

import static core.api.ContentAPIKt.playAudio;

/**
 * The type Trap setting.
 */
public class TrapSetting {
    private final int[] nodeIds;
    private final Item[] items;
    private final String option;
    private final int level;
    private final Animation setupAnimation;
    private final Animation dismantleAnimation;
    private final int failId;
    private final int[] objectIds;
    private final int[] baitIds;
    private final boolean objectTrap;

    /**
     * Instantiates a new Trap setting.
     *
     * @param nodeIds            the node ids
     * @param items              the items
     * @param objectIds          the object ids
     * @param baitIds            the bait ids
     * @param option             the option
     * @param level              the level
     * @param failId             the fail id
     * @param setupAnimation     the setup animation
     * @param dismantleAnimation the dismantle animation
     * @param objectTrap         the object trap
     */
    public TrapSetting(int[] nodeIds, Item[] items, int[] objectIds, final int[] baitIds, final String option, int level, final int failId, final Animation setupAnimation, final Animation dismantleAnimation, boolean objectTrap) {
        this.nodeIds = nodeIds;
        this.items = items;
        this.objectIds = objectIds;
        this.baitIds = baitIds;
        this.level = level;
        this.option = option;
        this.objectTrap = objectTrap;
        this.failId = failId;
        this.setupAnimation = setupAnimation;
        this.dismantleAnimation = dismantleAnimation;
    }

    /**
     * Instantiates a new Trap setting.
     *
     * @param nodeId             the node id
     * @param items              the items
     * @param objectIds          the object ids
     * @param baitIds            the bait ids
     * @param option             the option
     * @param level              the level
     * @param failId             the fail id
     * @param setupAnimation     the setup animation
     * @param dismantleAnimation the dismantle animation
     * @param objectTrap         the object trap
     */
    public TrapSetting(int nodeId, Item[] items, int[] objectIds, final int[] baitIds, final String option, int level, final int failId, final Animation setupAnimation, final Animation dismantleAnimation, boolean objectTrap) {
        this(new int[]{nodeId}, items, objectIds, baitIds, option, level, failId, setupAnimation, dismantleAnimation, objectTrap);
    }

    /**
     * Instantiates a new Trap setting.
     *
     * @param nodeId             the node id
     * @param objectIds          the object ids
     * @param baitIds            the bait ids
     * @param option             the option
     * @param failId             the fail id
     * @param setupAnimation     the setup animation
     * @param dismantleAnimation the dismantle animation
     * @param level              the level
     */
    public TrapSetting(int nodeId, int[] objectIds, final int[] baitIds, String option, final int failId, final Animation setupAnimation, final Animation dismantleAnimation, int level) {
        this(new int[]{nodeId}, objectIds, baitIds, option, level, failId, setupAnimation, dismantleAnimation, false);
    }

    /**
     * Instantiates a new Trap setting.
     *
     * @param nodeIds            the node ids
     * @param objectIds          the object ids
     * @param baitIds            the bait ids
     * @param option             the option
     * @param level              the level
     * @param failId             the fail id
     * @param setupAnimation     the setup animation
     * @param dismantleAnimation the dismantle animation
     * @param objectTrap         the object trap
     */
    public TrapSetting(int[] nodeIds, int[] objectIds, final int[] baitIds, String option, int level, final int failId, final Animation setupAnimation, final Animation dismantleAnimation, boolean objectTrap) {
        this(nodeIds, new Item[]{new Item(nodeIds[0])}, objectIds, baitIds, option, level, failId, setupAnimation, dismantleAnimation, objectTrap);
    }

    /**
     * Clear boolean.
     *
     * @param wrapper the wrapper
     * @param type    the type
     * @return the boolean
     */
    public boolean clear(TrapWrapper wrapper, int type) {
        Scenery scenery = wrapper.getObject();
        returnItems(scenery, wrapper, type);
        wrapper.getType().getHooks().remove(wrapper.getHook());
        removeObject(wrapper);
        return true;
    }

    /**
     * Return items.
     *
     * @param scenery the scenery
     * @param wrapper the wrapper
     * @param type    the type
     */
    public void returnItems(Scenery scenery, TrapWrapper wrapper, int type) {
        boolean ground = type == 0;
        Player player = wrapper.getPlayer();
        if (!isObjectTrap()) {
            if (ground) {
                createGroundItem(items[0], scenery.getLocation(), wrapper.getPlayer());
                return;
            }
            addTool(player, wrapper, type);
            player.getInventory().add(wrapper.getItems().toArray(new Item[]{}));
        } else {
            if (isObjectTrap() && !ground) {
                player.getInventory().add(wrapper.getItems().toArray(new Item[]{}));
            }
        }
    }

    /**
     * Add tool.
     *
     * @param player  the player
     * @param wrapper the wrapper
     * @param type    the type
     */
    public void addTool(Player player, TrapWrapper wrapper, int type) {
        player.getInventory().add(items[0]);
    }

    /**
     * Has items boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public boolean hasItems(Player player) {
        for (Item item : items) {
            if (!player.getInventory().containsItem(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Create ground item.
     *
     * @param item     the item
     * @param location the location
     * @param player   the player
     */
    public void createGroundItem(Item item, Location location, Player player) {
        GroundItemManager.create(new GroundItem(item, location, player));
    }

    /**
     * Remove object boolean.
     *
     * @param wrapper the wrapper
     * @return the boolean
     */
    public boolean removeObject(TrapWrapper wrapper) {
        return removeObject(wrapper.getObject());
    }

    /**
     * Remove object boolean.
     *
     * @param scenery the scenery
     * @return the boolean
     */
    public boolean removeObject(Scenery scenery) {
        return SceneryBuilder.remove(scenery);
    }

    /**
     * Build object scenery.
     *
     * @param player the player
     * @param node   the node
     * @return the scenery
     */
    public Scenery buildObject(Player player, Node node) {
        return new Scenery(objectIds[0], player.getLocation());
    }

    /**
     * Investigate.
     *
     * @param player  the player
     * @param scenery the scenery
     */
    public void investigate(Player player, Scenery scenery) {
        HunterManager instance = HunterManager.getInstance(player);
        if (!instance.isOwner(scenery)) {
            player.sendMessage("This isn't your trap.");
            return;
        }
        TrapWrapper wrapper = instance.getWrapper(scenery);
        player.sendMessage("This trap " + (wrapper.isSmoked() ? "has" : "hasn't") + " been smoked.");
    }

    /**
     * Catch npc.
     *
     * @param wrapper the wrapper
     * @param node    the node
     * @param npc     the npc
     */
    public void catchNpc(final TrapWrapper wrapper, final TrapNode node, final NPC npc) {
        final boolean success = isSuccess(wrapper.getPlayer(), node);
        int ticks = success ? 3 : 2;
        npc.lock(ticks);
        wrapper.setBusyTicks(ticks);
        npc.getWalkingQueue().reset();
        npc.getPulseManager().clear();
        wrapper.setTicks(wrapper.getTicks() + 4);
        GameWorld.getPulser().submit(getCatchPulse(wrapper, node, npc, success));
    }

    /**
     * Handle catch.
     *
     * @param counter the counter
     * @param wrapper the wrapper
     * @param node    the node
     * @param npc     the npc
     * @param success the success
     */
    public void handleCatch(int counter, TrapWrapper wrapper, TrapNode node, NPC npc, boolean success) {
    }

    /**
     * Gets catch pulse.
     *
     * @param wrapper the wrapper
     * @param node    the node
     * @param npc     the npc
     * @param success the success
     * @return the catch pulse
     */
    public Pulse getCatchPulse(final TrapWrapper wrapper, final TrapNode node, final NPC npc, final boolean success) {
        final Player player = wrapper.getPlayer();
        wrapper.setFailed(!success);
        return new Pulse(1) {
            int counter;

            @Override
            public boolean pulse() {
                switch (++counter) {
                    case 2:
                        handleCatch(counter, wrapper, node, npc, success);
                        if (success) {
                            int transformId = getTransformId(wrapper, node);
                            npc.setAttribute("hunter", GameWorld.getTicks() + 6);
                            npc.finalizeDeath(player);
                            if (transformId != -1) {
                                wrapper.setObject(getTransformId(wrapper, node));
                            }
                            npc.getProperties().setTeleportLocation(npc.getProperties().getSpawnLocation());
                            break;
                        }
                        npc.moveStep();
                        wrapper.setObject(getFailId(wrapper, node));
                        break;
                    case 3:
                        handleCatch(counter, wrapper, node, npc, success);
                        if (success) {
                            wrapper.setTicks(GameWorld.getTicks() + 100);
                            wrapper.setReward(node);
                            wrapper.setObject(getFinalId(wrapper, node));
                            switch (wrapper.getType()) {
                                case BIRD_SNARE:
                                    playAudio(player, Sounds.HUNTING_NOOSE_2637, 0, 1, wrapper.getObject().getLocation(), 10);
                                    playAudio(player, Sounds.HUNTING_BIRDCAUGHT_2625, 20, 1, wrapper.getObject().getLocation(), 10);
                                    break;
                                case BOX_TRAP:
                                    playAudio(player, Sounds.HUNTING_BOXTRAP_2627, 0, 1, wrapper.getObject().getLocation(), 10);
                                    break;
                                case NET_TRAP:
                                    playAudio(player, Sounds.HUNTING_TWITCHNET_2652, 0, 1, wrapper.getObject().getLocation(), 10);
                                    playAudio(player, Sounds.SALAMANDER_HIT_739, 20, 1, wrapper.getObject().getLocation(), 10);
                                    break;
                                case DEAD_FALL:
                                    playAudio(player, Sounds.HUNTING_DEADFALL_2631, 0, 1, wrapper.getObject().getLocation(), 10);
                                    break;
                            }
                            return true;
                        }
                        npc.moveStep();
                        return true;
                }
                return false;
            }

        };
    }

    /**
     * Is success boolean.
     *
     * @param player the player
     * @param node   the node
     * @return the boolean
     */
    public boolean isSuccess(Player player, final TrapNode node) {
        double level = player.skills.getStaticLevel(Skills.HUNTER);
        double req = node.level;
        double successChance = Math.ceil((level * 50 - req * 17) / req / 3 * 4);
        int roll = RandomFunction.random(99);
        return successChance >= roll;
    }

    /**
     * Create hook trap hook.
     *
     * @param wrapper the wrapper
     * @return the trap hook
     */
    public TrapHook createHook(TrapWrapper wrapper) {
        return new TrapHook(wrapper, new Location[]{wrapper.getObject().getLocation()});
    }

    /**
     * Reward.
     *
     * @param player  the player
     * @param node    the node
     * @param wrapper the wrapper
     */
    public void reward(Player player, Node node, TrapWrapper wrapper) {

    }

    /**
     * Can catch boolean.
     *
     * @param wrapper the wrapper
     * @param npc     the npc
     * @return the boolean
     */
    public boolean canCatch(TrapWrapper wrapper, NPC npc) {
        return true;
    }

    /**
     * Has bait boolean.
     *
     * @param bait the bait
     * @return the boolean
     */
    public boolean hasBait(Item bait) {
        for (int id : getBaitIds()) {
            if (id == bait.getId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets node for object id.
     *
     * @param objectId the object id
     * @return the node for object id
     */
    public int getNodeForObjectId(int objectId) {
        return getNodeForObject(getObjectIndex(objectId));
    }

    /**
     * Gets node for object.
     *
     * @param index the index
     * @return the node for object
     */
    public int getNodeForObject(int index) {
        return nodeIds[index];
    }

    /**
     * Gets object for node.
     *
     * @param node the node
     * @return the object for node
     */
    public int getObjectForNode(Node node) {
        return getObjectForNode(getNodeIndex(node));
    }

    /**
     * Gets object for node.
     *
     * @param index the index
     * @return the object for node
     */
    public int getObjectForNode(int index) {
        return objectIds[index];
    }

    /**
     * Gets node index.
     *
     * @param node the node
     * @return the node index
     */
    public int getNodeIndex(Node node) {
        for (int i = 0; i < nodeIds.length; i++) {
            if (node.getId() == nodeIds[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets object index.
     *
     * @param objectId the object id
     * @return the object index
     */
    public int getObjectIndex(int objectId) {
        for (int i = 0; i < objectIds.length; i++) {
            if (objectId == objectIds[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets transform id.
     *
     * @param wrapper the wrapper
     * @param node    the node
     * @return the transform id
     */
    public int getTransformId(TrapWrapper wrapper, TrapNode node) {
        return node.getTransformId();
    }

    /**
     * Gets final id.
     *
     * @param wrapper the wrapper
     * @param node    the node
     * @return the final id
     */
    public int getFinalId(TrapWrapper wrapper, TrapNode node) {
        return node.getFinalId();
    }

    /**
     * Gets fail id.
     *
     * @param wrapper the wrapper
     * @param node    the node
     * @return the fail id
     */
    public int getFailId(TrapWrapper wrapper, TrapNode node) {
        return failId;
    }

    /**
     * Gets limit message.
     *
     * @param player the player
     * @return the limit message
     */
    public String getLimitMessage(Player player) {
        HunterManager instance = HunterManager.getInstance(player);
        return "You don't have a high enough Hunter level to set up more than " + instance.getMaximumTraps() + " trap" + (instance.getMaximumTraps() == 1 ? "." : "s.");
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        if (isObjectTrap()) {
            return SceneryDefinition.forId(nodeIds[0]).getName().toLowerCase();
        }
        return ItemDefinition.forId(nodeIds[0]).getName().toLowerCase();
    }

    /**
     * Gets time up message.
     *
     * @return the time up message
     */
    public String getTimeUpMessage() {
        return "The " + getName() + " " + (isObjectTrap() ? "trap that you constructed has collapsed." : "that you laid has fallen over.");
    }

    /**
     * Exceeds limit boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public boolean exceedsLimit(Player player) {
        return false;
    }

    /**
     * Is object trap boolean.
     *
     * @return the boolean
     */
    public boolean isObjectTrap() {
        return objectTrap;
    }

    /**
     * Get node ids int [ ].
     *
     * @return the int [ ]
     */
    public int[] getNodeIds() {
        return nodeIds;
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
     * Gets option.
     *
     * @return the option
     */
    public String getOption() {
        return option;
    }

    /**
     * Gets level.
     *
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets setup animation.
     *
     * @return the setup animation
     */
    public Animation getSetupAnimation() {
        return setupAnimation;
    }

    /**
     * Get object ids int [ ].
     *
     * @return the int [ ]
     */
    public int[] getObjectIds() {
        return objectIds;
    }

    /**
     * Gets dismantle animation.
     *
     * @return the dismantle animation
     */
    public Animation getDismantleAnimation() {
        return dismantleAnimation;
    }

    /**
     * Gets fail id.
     *
     * @return the fail id
     */
    public int getFailId() {
        return failId;
    }

    /**
     * Get bait ids int [ ].
     *
     * @return the int [ ]
     */
    public int[] getBaitIds() {
        return baitIds;
    }

}
