package content.global.skill.hunter.bnet;

import core.cache.def.impl.NPCDefinition;
import core.game.container.impl.EquipmentContainer;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Graphics;

/**
 * The type B net node.
 */
public class BNetNode {

    private static final Item BUTTERFLY_JAR = new Item(10012);

    /**
     * The constant IMPLING_JAR.
     */
    protected static final Item IMPLING_JAR = new Item(11260);

    private final int[] npcs;

    private final int[] levels;

    private final double[] experience;

    private final Graphics[] graphics;

    private final Item reward;

    /**
     * Instantiates a new B net node.
     *
     * @param npcs       the npcs
     * @param levels     the levels
     * @param experience the experience
     * @param graphics   the graphics
     * @param reward     the reward
     */
    public BNetNode(int[] npcs, int[] levels, double[] experience, Graphics[] graphics, Item reward) {
        this.npcs = npcs;
        this.levels = levels;
        this.experience = experience;
        this.graphics = graphics;
        this.reward = reward;
    }

    /**
     * Reward.
     *
     * @param player the player
     * @param npc    the npc
     */
    public void reward(Player player, NPC npc) {
        if (!isBareHand(player)) {
            if (player.getInventory().remove(getJar())) {
                final Item item = getReward();
                player.getInventory().add(item);
                player.getSkills().addExperience(Skills.HUNTER, getExperience(player), true);
            }
        } else {
            player.graphics(graphics[0]);
            player.getSkills().addExperience(Skills.HUNTER, getExperiences()[1], true);
            player.getSkills().addExperience(Skills.AGILITY, getExperiences()[2], true);
        }
    }

    /**
     * Message.
     *
     * @param player  the player
     * @param type    the type
     * @param success the success
     */
    public void message(Player player, int type, boolean success) {
        if (!success) {
            return;
        }
        switch (type) {
            case 1:
                player.getPacketDispatch().sendMessage("You manage to catch the butterfly.");
                if (isBareHand(player)) {
                    player.getPacketDispatch().sendMessage("You release the " + NPCDefinition.forId(npcs[0]).getName().toLowerCase() + " butterfly.");
                }
                break;
        }
    }

    /**
     * Has jar boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public boolean hasJar(Player player) {
        return player.getInventory().containsItem(getJar());
    }

    /**
     * Has weapon boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public boolean hasWeapon(Player player) {
        Item item = player.getEquipment().get(EquipmentContainer.SLOT_WEAPON);
        return item != null && (item.getId() != 10010 && item.getId() != 11259);
    }

    /**
     * Has net boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public boolean hasNet(Player player) {
        return player.getEquipment().contains(10010, 1) || player.getEquipment().contains(11259, 1);
    }

    /**
     * Is bare hand boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public boolean isBareHand(Player player) {
        return !hasNet(player) && player.getSkills().getLevel(Skills.HUNTER) >= getBareHandLevel() && player.getSkills().getLevel(Skills.AGILITY) >= getAgilityLevel();
    }

    /**
     * Gets experience.
     *
     * @param player the player
     * @return the experience
     */
    public double getExperience(Player player) {
        return experience[0];
    }

    /**
     * Gets level.
     *
     * @return the level
     */
    public int getLevel() {
        return levels[0];
    }

    /**
     * Gets agility level.
     *
     * @return the agility level
     */
    public int getAgilityLevel() {
        return levels[2];
    }

    /**
     * Gets bare hand level.
     *
     * @return the bare hand level
     */
    public int getBareHandLevel() {
        return levels[1];
    }

    /**
     * Get npcs int [ ].
     *
     * @return the int [ ]
     */
    public int[] getNpcs() {
        return npcs;
    }

    /**
     * Get levels int [ ].
     *
     * @return the int [ ]
     */
    public int[] getLevels() {
        return levels;
    }

    /**
     * Get experiences double [ ].
     *
     * @return the double [ ]
     */
    public double[] getExperiences() {
        return experience;
    }

    /**
     * Get graphics graphics [ ].
     *
     * @return the graphics [ ]
     */
    public Graphics[] getGraphics() {
        return graphics;
    }

    /**
     * Gets reward.
     *
     * @return the reward
     */
    public Item getReward() {
        return reward;
    }

    /**
     * Gets jar.
     *
     * @return the jar
     */
    public Item getJar() {
        return BUTTERFLY_JAR;
    }

}
