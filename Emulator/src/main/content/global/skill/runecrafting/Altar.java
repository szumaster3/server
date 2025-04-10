package content.global.skill.runecrafting;

import core.cache.def.impl.ItemDefinition;
import core.game.node.entity.player.Player;
import core.game.node.scenery.Scenery;
import org.rs.consts.Quests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static core.api.ContentAPIKt.sendMessage;
import static core.api.quest.QuestAPIKt.hasRequirement;
import static core.api.quest.QuestAPIKt.isQuestComplete;

/**
 * Represents an altar used in the Runecrafting skill.
 * <p>
 * Each altar is associated with a unique scenery object, an exit portal, a rift ID,
 * its corresponding mysterious ruin, and the rune it produces.
 */
public enum Altar {
    /**
     * Air altar.
     */
    AIR(org.rs.consts.Scenery.AIR_ALTAR_2478, org.rs.consts.Scenery.AIR_ALTAR_EXIT_2465, org.rs.consts.Scenery.AIR_RIFT_7139, MysteriousRuins.AIR, Rune.AIR),
    /**
     * Mind altar.
     */
    MIND(org.rs.consts.Scenery.MIND_ALTAR_2479, org.rs.consts.Scenery.MIND_ALTAR_EXIT_2466, org.rs.consts.Scenery.MIND_RIFT_7140, MysteriousRuins.MIND, Rune.MIND),
    /**
     * Water altar.
     */
    WATER(org.rs.consts.Scenery.WATER_ALTAR_2480, org.rs.consts.Scenery.WATER_ALTAR_EXIT_2467, org.rs.consts.Scenery.WATER_RIFT_7137, MysteriousRuins.WATER, Rune.WATER),
    /**
     * Earth altar.
     */
    EARTH(org.rs.consts.Scenery.EARTH_ALTAR_2481, org.rs.consts.Scenery.EARTH_ALTAR_EXIT_2468, org.rs.consts.Scenery.EARTH_RIFT_7130, MysteriousRuins.EARTH, Rune.EARTH),
    /**
     * Fire altar.
     */
    FIRE(org.rs.consts.Scenery.FIRE_ALTAR_2482, org.rs.consts.Scenery.FIRE_ALTAR_EXIT_2469, org.rs.consts.Scenery.FIRE_RIFT_7129, MysteriousRuins.FIRE, Rune.FIRE),
    /**
     * Body altar.
     */
    BODY(org.rs.consts.Scenery.BODY_ALTAR_2483, org.rs.consts.Scenery.BODY_ALTAR_EXIT_2470, org.rs.consts.Scenery.BODY_RIFT_7131, MysteriousRuins.BODY, Rune.BODY),
    /**
     * Cosmic altar.
     */
    COSMIC(org.rs.consts.Scenery.COSMIC_ALTAR_2484, org.rs.consts.Scenery.COSMIC_ALTAR_EXIT_2471, org.rs.consts.Scenery.COSMIC_RIFT_7132, MysteriousRuins.COSMIC, Rune.COSMIC),
    /**
     * Chaos altar.
     */
    CHAOS(org.rs.consts.Scenery.CHAOS_ALTAR_2487, org.rs.consts.Scenery.CHAOS_ALTAR_EXIT_2474, org.rs.consts.Scenery.CHAOS_RIFT_7134, MysteriousRuins.CHAOS, Rune.CHAOS),
    /**
     * Astral altar.
     */
    ASTRAL(org.rs.consts.Scenery.ALTAR_17010, 0, 0, null, Rune.ASTRAL),
    /**
     * Nature altar.
     */
    NATURE(org.rs.consts.Scenery.NATURE_ALTAR_2486, org.rs.consts.Scenery.NATURE_ALTAR_EXIT_2473, org.rs.consts.Scenery.NATURE_RIFT_7133, MysteriousRuins.NATURE, Rune.NATURE),
    /**
     * Law altar.
     */
    LAW(org.rs.consts.Scenery.LAW_ALTAR_2485, org.rs.consts.Scenery.LAW_PORTAL_EXIT_2472, org.rs.consts.Scenery.LAW_RIFT_7135, MysteriousRuins.LAW, Rune.LAW),
    /**
     * Death altar.
     */
    DEATH(org.rs.consts.Scenery.DEATH_ALTAR_2488, org.rs.consts.Scenery.DEATH_ALTAR_EXIT_2475, org.rs.consts.Scenery.DEATH_RIFT_7136, MysteriousRuins.DEATH, Rune.DEATH),
    /**
     * Blood altar.
     */
    BLOOD(org.rs.consts.Scenery.BLOOD_ALTAR_30624, org.rs.consts.Scenery.BLOOD_ALTAR_EXIT_2477, org.rs.consts.Scenery.BLOOD_RIFT_7141, MysteriousRuins.BLOOD, Rune.BLOOD),
    /**
     * Ourania altar.
     */
    OURANIA(org.rs.consts.Scenery.OURANIA_ALTAR_26847, 0, 0, null, null);

    private final int scenery;
    private final int exit;
    private final int rift;
    private final MysteriousRuins ruin;
    private final Rune rune;

    private static final Map<Integer, Altar> ALTAR_BY_SCENERY = new HashMap<>();
    private static final Map<Integer, Altar> ALTAR_BY_PORTAL = new HashMap<>();
    private static final Map<Integer, Altar> ALTAR_BY_RIFT_ID = new HashMap<>();

    static {
        for (Altar altar : values()) {
            ALTAR_BY_SCENERY.put(altar.scenery, altar);
            ALTAR_BY_PORTAL.put(altar.exit, altar);
            ALTAR_BY_RIFT_ID.put(altar.rift, altar);
        }
    }

    Altar(int scenery, int exit, int rift, MysteriousRuins ruin, Rune rune) {
        this.scenery = scenery;
        this.exit = exit;
        this.rift = rift;
        this.ruin = ruin;
        this.rune = rune;
    }

    /**
     * Retrieves the corresponding altar based on the given scenery object.
     *
     * @param scenery the scenery object to find the corresponding altar for
     * @return the Altar associated with the given scenery
     */
    public static Altar forScenery(Scenery scenery) {
        return ALTAR_BY_SCENERY.getOrDefault(scenery.getId(), ALTAR_BY_PORTAL.getOrDefault(scenery.getId(), ALTAR_BY_RIFT_ID.get(scenery.getId())));
    }

    /**
     * Makes the player enter the rift for the current altar.
     *
     * @param player the player attempting to enter the rift
     */
    public void enterRift(Player player) {
        switch (this) {
            case ASTRAL:
                if (!hasRequirement(player, Quests.LUNAR_DIPLOMACY)) return;
                break;
            case DEATH:
                if (!hasRequirement(player, Quests.MOURNINGS_END_PART_II)) return;
                break;
            case BLOOD:
                if (!hasRequirement(player, Quests.LEGACY_OF_SEERGAZE)) return;
                break;
            case LAW:
                if (!ItemDefinition.canEnterEntrana(player)) {
                    sendMessage(player, "You can't take weapons and armour into the law rift.");
                    return;
                }
                break;
            case COSMIC:
                if (!isQuestComplete(player, Quests.LOST_CITY)) {
                    sendMessage(player, "You need to have completed the Lost City quest in order to do that.");
                    return;
                }
                break;
            default:
                break;
        }
        if (ruin != null) {
            player.getProperties().setTeleportLocation(ruin.end);
        }
    }

    /**
     * Gets the rune associated with the current altar.
     *
     * @return the Rune for the current altar
     */
    public Rune getRune() {
        return rune;
    }

    /**
     * Gets the scenery id associated with the current altar.
     *
     * @return the scenery id for the current altar
     */
    public int getScenery() {
        return scenery;
    }

    /**
     * Gets the exit id for the current altar.
     *
     * @return the exit id for the current altar
     */
    public int getExit() {
        return exit;
    }

    /**
     * Gets the rift id for the current altar.
     *
     * @return the rift id for the current altar
     */
    public int getRift() {
        return rift;
    }

    /**
     * Gets the ruin associated with the current altar.
     *
     * @return the MysteriousRuins for the current altar
     */
    public MysteriousRuins getRuin() {
        return ruin;
    }

    /**
     * Determines if the current altar is the Ourania altar.
     *
     * @return true if the altar is Ourania, false otherwise
     */
    public boolean isOurania() {
        return rune == null;
    }

    /**
     * Retrieves the talisman associated with the current altar.
     *
     * @return the Talisman corresponding to the current altar
     */
    public Talisman getTalisman() {
        return Arrays.stream(Talisman.values()).filter(talisman -> talisman.name().equalsIgnoreCase(this.name())).findFirst().orElse(null);
    }

    /**
     * Retrieves the tiara associated with the current altar.
     *
     * @return the Tiara corresponding to the current altar
     */
    public Tiara getTiara() {
        return Arrays.stream(Tiara.values()).filter(tiara -> tiara.name().equalsIgnoreCase(this.name())).findFirst().orElse(null);
    }
}
