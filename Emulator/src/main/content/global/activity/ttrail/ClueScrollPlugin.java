package content.global.activity.ttrail;

import core.game.component.Component;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.plugin.Plugin;
import core.tools.Log;
import core.tools.RandomFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static core.api.ContentAPIKt.log;

/**
 * The type Clue scroll plugin.
 */
/*
 * <a href="https://runescape.wiki/w/Treasure_Trails?oldid=2037641#Types_of_clues">Basic source</a><br>
 *
 * TODO:
 *  [ ] - Anagrams
 *  [x] - Challenge scrolls
 *  [ ] - Simple clues
 *  [x] - Cryptics
 *  [ ] - Chest keys
 *  [ ] - Emote clues
 *  [ ] - Co-ordinates
 *  [ ] - Maps
 *  [x] - Puzzle boxes
 *  [ ] - Puzzles
 */
public abstract class ClueScrollPlugin extends MapZone implements Plugin<Object> {

    /**
     * The constant WILDERNESS_CAPES.
     */
    protected static final int[] WILDERNESS_CAPES = {
            4315, 4316, 4317, 4318, 4319, 4320, 4321, 4322, 4323, 4324,
            4325, 4326, 4327, 4328, 4329, 4330, 4331, 4332, 4333, 4334,
            4335, 4336, 4337, 4338, 4339, 4340, 4341, 4342, 4343, 4344,
            4345, 4346, 4347, 4348, 4349, 4350, 4351, 4352, 4353, 4354,
            4355, 4356, 4357, 4358, 4359, 4360, 4361, 4362, 4363, 4364,
            4365, 4366, 4367, 4368, 4369, 4370, 4371, 4372, 4373, 4374,
            4375, 4376, 4377, 4378, 4379, 4380, 4381, 4382, 4383, 4384,
            4385, 4386, 4387, 4388, 4389, 4390, 4391, 4392, 4393, 4394,
            4395, 4396, 4397, 4398, 4399, 4400, 4401, 4402, 4403, 4404,
            4405, 4406, 4407, 4408, 4409, 4410, 4411, 4412, 4413, 4414,
    };

    private static final Map<Integer, ClueScrollPlugin> CLUE_SCROLLS = new HashMap<>();
    private static final Map<ClueLevel, List<ClueScrollPlugin>> ORGANIZED = new HashMap<>();

    /**
     * The Clue id.
     */
    protected final int clueId;
    /**
     * The Level.
     */
    protected final ClueLevel level;
    /**
     * The Interface id.
     */
    protected final int interfaceId;
    /**
     * The Borders.
     */
    protected final ZoneBorders[] borders;

    /**
     * Instantiates a new Clue scroll plugin.
     *
     * @param name        the name
     * @param clueId      the clue id
     * @param level       the level
     * @param interfaceId the interface id
     * @param borders     the borders
     */
    public ClueScrollPlugin(final String name, final int clueId, ClueLevel level, final int interfaceId, final ZoneBorders... borders) {
        super(name, true);
        this.clueId = clueId;
        this.level = level;
        this.interfaceId = interfaceId;
        this.borders = borders;
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    /**
     * Reward.
     *
     * @param player the player
     * @param casket the casket
     */
    public void reward(Player player, boolean casket) {
        Item clue = player.getInventory().getItem(new Item(getClueId()));
        if (clue == null) {
            return;
        }
        nextStage(player, clue);
        if (casket) {
            player.getInventory().replace(level.getCasket(), clue.getSlot());
        } else {
            player.getInventory().remove(clue);
        }
    }

    /**
     * Reward.
     *
     * @param player the player
     */
    public void reward(Player player) {
        reward(player, true);
    }

    /**
     * Next stage.
     *
     * @param player the player
     * @param clue   the clue
     */
    public void nextStage(Player player, Item clue) {
        TreasureTrailManager manager = TreasureTrailManager.Companion.getInstance(player);
        if (!manager.hasTrail() || clue.getId() != manager.getClueId()) {
            manager.startTrail(this);
        }
        int currentStage = manager.getTrailStage();
        if (currentStage >= manager.getTrailLength()) {
            manager.clearTrail();
        } else {
            manager.incrementStage();
        }
    }

    /**
     * Read.
     *
     * @param player the player
     */
    public void read(Player player) {
        if (player.getInterfaceManager().isOpened()) {
            player.sendMessage("Please finish what you're doing first.");
            return;
        }
        player.getInterfaceManager().open(new Component(interfaceId));
    }

    /**
     * Register.
     *
     * @param clue the clue
     */
    public void register(ClueScrollPlugin clue) {
        if (clue.getClueId() == 2681) return;

        if (CLUE_SCROLLS.containsKey(clue.getClueId())) {
            log(this.getClass(), Log.ERR, "Error! Plugin already registered with clue id - " + clue.getClueId() + ", trying to register " + clue.getClass().getCanonicalName() + " the real plugin using the id is " + CLUE_SCROLLS.get(clue.getClueId()).getClass().getCanonicalName() + "!");
            return;
        }

        ORGANIZED.computeIfAbsent(clue.getLevel(), k -> new ArrayList<>(20)).add(clue);
        ZoneBuilder.configure(clue);
        CLUE_SCROLLS.put(clue.getClueId(), clue);
    }

    @Override
    public void configure() {
        if (borders != null) {
            for (ZoneBorders border : borders) {
                register(border);
            }
        }
    }

    /**
     * Gets clue.
     *
     * @param clueLevel the clue level
     * @return the clue
     */
    public static Item getClue(ClueLevel clueLevel) {
        List<ClueScrollPlugin> clues = ORGANIZED.get(clueLevel);
        if (clues == null) {
            log(ClueScrollPlugin.class, Log.ERR, "Error! There are no clues for level " + clueLevel + "!");
            return null;
        }
        ClueScrollPlugin clue = clues.get(RandomFunction.random(clues.size()));
        return new Item(clue.getClueId());
    }

    /**
     * Has equipment boolean.
     *
     * @param player    the player
     * @param equipment the equipment
     * @return the boolean
     */
    public boolean hasEquipment(Player player, int[][] equipment) {
        if (equipment == null || equipment.length == 0) {
            return true;
        }
        int hasAmt = 0;
        for (int[] equipSet : equipment) {
            for (int equip : equipSet) {
                if (player.getEquipment().contains(equip, 1)) {
                    hasAmt++;
                    break;
                }
            }
        }
        return hasAmt == equipment.length;
    }

    /**
     * Gets clue id.
     *
     * @return the clue id
     */
    public int getClueId() {
        return clueId;
    }

    /**
     * Gets level.
     *
     * @return the level
     */
    public ClueLevel getLevel() {
        return level;
    }

    /**
     * Gets clue scrolls.
     *
     * @return the clue scrolls
     */
    public static Map<Integer, ClueScrollPlugin> getClueScrolls() {
        return CLUE_SCROLLS;
    }

    /**
     * Gets interface id.
     *
     * @return the interface id
     */
    public int getInterfaceId() {
        return interfaceId;
    }
}
