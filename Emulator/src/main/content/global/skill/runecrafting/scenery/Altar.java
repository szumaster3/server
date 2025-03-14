package content.global.skill.runecrafting.scenery;

import content.global.skill.runecrafting.items.Talisman;
import content.global.skill.runecrafting.items.Tiara;
import content.global.skill.runecrafting.runes.Rune;
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
 * The enum Altar.
 */
public enum Altar {
    /**
     * Air altar.
     */
    AIR(
            org.rs.consts.Scenery.AIR_ALTAR_2478,
            org.rs.consts.Scenery.AIR_ALTAR_EXIT_2465,
            org.rs.consts.Scenery.AIR_RIFT_7139,
            MysteriousRuins.AIR, Rune.AIR
    ),
    /**
     * Mind altar.
     */
    MIND(
            org.rs.consts.Scenery.MIND_ALTAR_2479,
            org.rs.consts.Scenery.MIND_ALTAR_EXIT_2466,
            org.rs.consts.Scenery.MIND_RIFT_7140,
            MysteriousRuins.MIND, Rune.MIND
    ),
    /**
     * Water altar.
     */
    WATER(
            org.rs.consts.Scenery.WATER_ALTAR_2480,
            org.rs.consts.Scenery.WATER_ALTAR_EXIT_2467,
            org.rs.consts.Scenery.WATER_RIFT_7137,
            MysteriousRuins.WATER, Rune.WATER
    ),
    /**
     * Earth altar.
     */
    EARTH(
            org.rs.consts.Scenery.EARTH_ALTAR_2481,
            org.rs.consts.Scenery.EARTH_ALTAR_EXIT_2468,
            org.rs.consts.Scenery.EARTH_RIFT_7130,
            MysteriousRuins.EARTH, Rune.EARTH
    ),
    /**
     * Fire altar.
     */
    FIRE(
            org.rs.consts.Scenery.FIRE_ALTAR_2482,
            org.rs.consts.Scenery.FIRE_ALTAR_EXIT_2469,
            org.rs.consts.Scenery.FIRE_RIFT_7129,
            MysteriousRuins.FIRE, Rune.FIRE
    ),
    /**
     * Body altar.
     */
    BODY(
            org.rs.consts.Scenery.BODY_ALTAR_2483,
            org.rs.consts.Scenery.BODY_ALTAR_EXIT_2470,
            org.rs.consts.Scenery.BODY_RIFT_7131,
            MysteriousRuins.BODY, Rune.BODY
    ),
    /**
     * Cosmic altar.
     */
    COSMIC(
            org.rs.consts.Scenery.COSMIC_ALTAR_2484,
            org.rs.consts.Scenery.COSMIC_ALTAR_EXIT_2471,
            org.rs.consts.Scenery.COSMIC_RIFT_7132,
            MysteriousRuins.COSMIC, Rune.COSMIC
    ),
    /**
     * Chaos altar.
     */
    CHAOS(
            org.rs.consts.Scenery.CHAOS_ALTAR_2487,
            org.rs.consts.Scenery.CHAOS_ALTAR_EXIT_2474,
            org.rs.consts.Scenery.CHAOS_RIFT_7134,
            MysteriousRuins.CHAOS, Rune.CHAOS
    ),
    /**
     * Astral altar.
     */
    ASTRAL(
            org.rs.consts.Scenery.ALTAR_17010,
            0, 0, null, Rune.ASTRAL
    ),
    /**
     * Nature altar.
     */
    NATURE(
            org.rs.consts.Scenery.NATURE_ALTAR_2486,
            org.rs.consts.Scenery.NATURE_ALTAR_EXIT_2473,
            org.rs.consts.Scenery.NATURE_RIFT_7133,
            MysteriousRuins.NATURE, Rune.NATURE
    ),
    /**
     * Law altar.
     */
    LAW(
            org.rs.consts.Scenery.LAW_ALTAR_2485,
            org.rs.consts.Scenery.LAW_PORTAL_EXIT_2472,
            org.rs.consts.Scenery.LAW_RIFT_7135,
            MysteriousRuins.LAW, Rune.LAW
    ),
    /**
     * Death altar.
     */
    DEATH(
            org.rs.consts.Scenery.DEATH_ALTAR_2488,
            org.rs.consts.Scenery.DEATH_ALTAR_EXIT_2475,
            org.rs.consts.Scenery.DEATH_RIFT_7136,
            MysteriousRuins.DEATH, Rune.DEATH
    ),
    /**
     * Blood altar.
     */
    BLOOD(
            org.rs.consts.Scenery.BLOOD_ALTAR_30624,
            org.rs.consts.Scenery.BLOOD_ALTAR_EXIT_2477,
            org.rs.consts.Scenery.BLOOD_RIFT_7141,
            MysteriousRuins.BLOOD, Rune.BLOOD
    ),
    /**
     * Ourania altar.
     */
    OURANIA(
            org.rs.consts.Scenery.OURANIA_ALTAR_26847,
            0, 0, null, null
    );

    private final int objs;
    private final int exit;
    private final int rift;
    private final MysteriousRuins ruin;
    private final Rune rune;

    private static final Map<Integer, Altar> ALTAR_BY_SCENERY = new HashMap<>();
    private static final Map<Integer, Altar> ALTAR_BY_PORTAL = new HashMap<>();
    private static final Map<Integer, Altar> ALTAR_BY_RIFT_ID = new HashMap<>();

    static {
        for (Altar altar : values()) {
            ALTAR_BY_SCENERY.put(altar.objs, altar);
            ALTAR_BY_PORTAL.put(altar.exit, altar);
            ALTAR_BY_RIFT_ID.put(altar.rift, altar);
        }
    }

    Altar(int objs, int exit, int rift, MysteriousRuins ruin, Rune rune) {
        this.objs = objs;
        this.exit = exit;
        this.rift = rift;
        this.ruin = ruin;
        this.rune = rune;
    }

    /**
     * For scenery altar.
     *
     * @param scenery the scenery
     * @return the altar
     */
    public static Altar forScenery(Scenery scenery) {
        return ALTAR_BY_SCENERY.getOrDefault(
                scenery.getId(),
                ALTAR_BY_PORTAL.getOrDefault(
                        scenery.getId(),
                        ALTAR_BY_RIFT_ID.get(scenery.getId())
                )
        );
    }

    /**
     * Enter rift.
     *
     * @param player the player
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
            player.getProperties().setTeleportLocation(ruin.getEnd());
        }
    }

    /**
     * Gets rune.
     *
     * @return the rune
     */
    public Rune getRune() {
        return rune;
    }

    /**
     * Gets objs.
     *
     * @return the objs
     */
    public int getObjs() {
        return objs;
    }

    /**
     * Gets exit.
     *
     * @return the exit
     */
    public int getExit() {
        return exit;
    }

    /**
     * Gets rift.
     *
     * @return the rift
     */
    public int getRift() {
        return rift;
    }

    /**
     * Gets ruin.
     *
     * @return the ruin
     */
    public MysteriousRuins getRuin() {
        return ruin;
    }

    /**
     * Is ourania boolean.
     *
     * @return the boolean
     */
    public boolean isOurania() {
        return rune == null;
    }

    /**
     * Gets talisman.
     *
     * @return the talisman
     */
    public Talisman getTalisman() {
        return Arrays.stream(Talisman.values())
                .filter(talisman -> talisman.name().equalsIgnoreCase(this.name()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets tiara.
     *
     * @return the tiara
     */
    public Tiara getTiara() {
        return Arrays.stream(Tiara.values())
                .filter(tiara -> tiara.name().equalsIgnoreCase(this.name()))
                .findFirst()
                .orElse(null);
    }
}
