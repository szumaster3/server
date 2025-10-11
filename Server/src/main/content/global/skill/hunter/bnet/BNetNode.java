package content.global.skill.hunter.bnet;

import core.cache.def.impl.NPCDefinition;
import core.game.container.impl.EquipmentContainer;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Graphics;

/**
 * Represents a general "Butterfly Net node, used for capturing
 * butterflies and implings using nets.
 */
public class BNetNode {

    /**
     * The butterfly jar item.
     */
    private static final Item BUTTERFLY_JAR = new Item(10012);

    /**
     * The impling jar item.
     */
    protected static final Item IMPLING_JAR = new Item(11260);

    /**
     * The NPCs associated with this node.
     */
    private final int[] npcs;

    /**
     * The level requirements:
     * [0] = Hunter
     * [1] = Bare-hand Hunter
     * [2] = Agility
     */
    private final int[] levels;

    /**
     * The experience rewards:
     * [0] = regular catch
     * [1] = bare-hand Hunter XP
     * [2] = bare-hand Agility XP
     */
    private final double[] experience;

    /**
     * The graphics send during capture.
     */
    private final Graphics[] graphics;

    /**
     * The reward item given upon successful capture.
     */
    private final Item reward;

    /**
     * Constructs a new {@code BNetNode}.
     *
     * @param npcs       the NPC IDs that can be caught
     * @param levels     the level requirements (Hunter, Bare-hand Hunter, Agility)
     * @param experience the experience values (Hunter, Bare-hand Hunter, Agility)
     * @param graphics   the graphics effects to display during capture
     * @param reward     the reward item given when captured
     */
    public BNetNode(int[] npcs, int[] levels, double[] experience, Graphics[] graphics, Item reward) {
        this.npcs = npcs;
        this.levels = levels;
        this.experience = experience;
        this.graphics = graphics;
        this.reward = reward;
    }

    /**
     * Grants rewards and experience to the player after a successful catch.
     *
     * @param player the player who caught the NPC
     * @param npc    the caught NPC
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
     * Sends contextual messages to the player upon catching.
     *
     * @param player  the player
     * @param type    the message type (1 = caught)
     * @param success whether the capture succeeded
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
     * Checks if the player has the required jar to capture the NPC.
     *
     * @param player the player
     * @return {@code true} if the player has the jar, otherwise {@code false}
     */
    public boolean hasJar(Player player) {
        return player.getInventory().containsItem(getJar());
    }

    /**
     * Checks if the player has a weapon equipped (prevents net catching).
     *
     * @param player the player
     * @return {@code true} if the player has a non-net weapon equipped
     */
    public boolean hasWeapon(Player player) {
        Item item = player.getEquipment().get(EquipmentContainer.SLOT_WEAPON);
        return item != null && (item.getId() != 10010 && item.getId() != 11259);
    }

    /**
     * Checks if the player has a butterfly or impling net equipped.
     *
     * @param player the player
     * @return {@code true} if the player has a valid net equipped
     */
    public boolean hasNet(Player player) {
        return player.getEquipment().contains(10010, 1) || player.getEquipment().contains(11259, 1);
    }

    /**
     * Determines whether the player is eligible to catch using bare hands.
     *
     * @param player the player
     * @return {@code true} if the player meets both Hunter and Agility requirements
     */
    public boolean isBareHand(Player player) {
        return !hasNet(player) && player.getSkills().getLevel(Skills.HUNTER) >= getBareHandLevel() && player.getSkills().getLevel(Skills.AGILITY) >= getAgilityLevel();
    }

    /**
     * Gets the Hunter experience awarded for a successful capture.
     *
     * @param player the player
     * @return the base Hunter experience
     */
    public double getExperience(Player player) {
        return experience[0];
    }

    /**
     * Gets the Hunter level required to catch the NPC with a net.
     *
     * @return the required Hunter level
     */
    public int getLevel() {
        return levels[0];
    }

    /**
     * Gets the Agility level required for bare-hand catching.
     *
     * @return the required Agility level
     */
    public int getAgilityLevel() {
        return levels[2];
    }

    /**
     * Gets the Hunter level required for bare-hand catching.
     *
     * @return the required bare-hand Hunter level
     */
    public int getBareHandLevel() {
        return levels[1];
    }

    /**
     * Gets the array of NPCs that this node applies to.
     *
     * @return the NPCs
     */
    public int[] getNpcs() {
        return npcs;
    }

    /**
     * Gets all level requirement values.
     *
     * @return the level array
     */
    public int[] getLevels() {
        return levels;
    }

    /**
     * Gets all experience values for the node.
     *
     * @return the experience array
     */
    public double[] getExperiences() {
        return experience;
    }

    /**
     * Gets the capture graphics effects.
     *
     * @return the graphics array
     */
    public Graphics[] getGraphics() {
        return graphics;
    }

    /**
     * Gets the reward item given upon successful capture.
     *
     * @return the reward item
     */
    public Item getReward() {
        return reward;
    }

    /**
     * Gets the jar item required for this node (default: butterfly jar).
     *
     * @return the jar item
     */
    public Item getJar() {
        return BUTTERFLY_JAR;
    }
}
