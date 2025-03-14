package core.game.node.entity.player.link.prayer;

import core.game.event.PrayerActivatedEvent;
import core.game.event.PrayerDeactivatedEvent;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.audio.Audio;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.entity.skill.SkillBonus;
import core.game.node.entity.skill.Skills;
import core.game.world.map.zone.ZoneBorders;
import core.tools.StringUtils;
import org.rs.consts.Sounds;

import java.util.List;

import static core.api.ContentAPIKt.playAudio;
import static core.api.ContentAPIKt.setVarp;

/**
 * The enum Prayer type.
 */
public enum PrayerType {

    /**
     * The Thick skin.
     */
    THICK_SKIN(1, 12, 83, 5, PrayerCategory.LIGHT_BLUE, Sounds.THICK_SKIN_2690, new SkillBonus(Skills.DEFENCE, 0.05)),

    /**
     * The Burst of strength.
     */
    BURST_OF_STRENGTH(4, 12, 84, 7, PrayerCategory.GREEN, Sounds.STRENGTH_BURST_2688, new SkillBonus(Skills.STRENGTH, 0.05)),

    /**
     * The Clarity of thought.
     */
    CLARITY_OF_THOUGHT(7, 12, 85, 9, PrayerCategory.PINK, Sounds.CLARITY_2664, new SkillBonus(Skills.ATTACK, 0.05)),

    /**
     * The Sharp eye.
     */
    SHARP_EYE(8, 12, 862, 11, PrayerCategory.LIME_GREEN, Sounds.SHARP_EYE_2685, new SkillBonus(Skills.RANGE, 0.05)),

    /**
     * The Mystic will.
     */
    MYSTIC_WILL(9, 12, 863, 13, PrayerCategory.LIME_GREEN, Sounds.MYSTIC_WILL_2670, new SkillBonus(Skills.MAGIC, 0.05)),

    /**
     * The Rock skin.
     */
    ROCK_SKIN(10, 6, 86, 15, PrayerCategory.LIGHT_BLUE, Sounds.ROCK_SKIN_2684, new SkillBonus(Skills.DEFENCE, 0.1)),

    /**
     * The Superhuman strength.
     */
    SUPERHUMAN_STRENGTH(13, 6, 87, 17, PrayerCategory.GREEN, Sounds.SUPERHUMAN_STRENGTH_2689, new SkillBonus(Skills.STRENGTH, 0.1)),

    /**
     * The Improved reflexes.
     */
    IMPROVED_REFLEXES(16, 6, 88, 19, PrayerCategory.PINK, Sounds.IMPROVED_REFLEXES_2662, new SkillBonus(Skills.ATTACK, 0.1)),

    /**
     * Rapid restore prayer type.
     */
    RAPID_RESTORE(19, 26, 89, 21, PrayerCategory.PURPLE, Sounds.RAPID_RESTORE_2679),

    /**
     * Rapid heal prayer type.
     */
    RAPID_HEAL(22, 18, 90, 23, PrayerCategory.PURPLE, Sounds.RAPID_HEAL_2678),

    /**
     * Protect items prayer type.
     */
    PROTECT_ITEMS(25, 18, 91, 25, PrayerCategory.DARK_GREEN, Sounds.PROTECT_ITEMS_1982),

    /**
     * The Hawk eye.
     */
    HAWK_EYE(26, 6, 864, 27, PrayerCategory.LIME_GREEN, Sounds.HAWK_EYE_2666, new SkillBonus(Skills.RANGE, 0.1)),

    /**
     * The Mystic lore.
     */
    MYSTIC_LORE(27, 6, 865, 29, PrayerCategory.LIME_GREEN, Sounds.MYSTIC_2668, new SkillBonus(Skills.MAGIC, 0.1)),

    /**
     * The Steel skin.
     */
    STEEL_SKIN(28, 3, 92, 31, PrayerCategory.LIGHT_BLUE, Sounds.STEEL_SKIN_2687, new SkillBonus(Skills.DEFENCE, 0.15)),

    /**
     * The Ultimate strength.
     */
    ULTIMATE_STRENGTH(31, 3, 93, 33, PrayerCategory.GREEN, Sounds.ULTIMATE_STRENGTH_2691, new SkillBonus(Skills.STRENGTH, 0.15)),

    /**
     * The Incredible reflexes.
     */
    INCREDIBLE_REFLEXES(34, 3, 94, 35, PrayerCategory.PINK, Sounds.INCREDIBLE_REFLEXES_2667, new SkillBonus(Skills.ATTACK, 0.15)),

    /**
     * The Protect from summoning.
     */
    PROTECT_FROM_SUMMONING(35, 2, 1168, 53, PrayerCategory.DARK_BROWN, PrayerCategory.MAGENTA, new Audio(4262)),

    /**
     * Protect from magic prayer type.
     */
    PROTECT_FROM_MAGIC(37, 3, 95, 37, PrayerCategory.LIGHT_BROWN, Sounds.PROTECT_FROM_MAGIC_2675),

    /**
     * Protect from missiles prayer type.
     */
    PROTECT_FROM_MISSILES(40, 3, 96, 39, PrayerCategory.LIGHT_BROWN, Sounds.PROTECT_FROM_MISSILES_2677),

    /**
     * Protect from melee prayer type.
     */
    PROTECT_FROM_MELEE(43, 4, 97, 41, PrayerCategory.LIGHT_BROWN, Sounds.PROTECT_FROM_MELEE_2676),

    /**
     * The Eagle eye.
     */
    EAGLE_EYE(44, 3, 866, 43, PrayerCategory.LIME_GREEN, Sounds.EAGLE_EYE_2665, new SkillBonus(Skills.RANGE, 0.15)),

    /**
     * The Mystic might.
     */
    MYSTIC_MIGHT(45, 3, 867, 45, PrayerCategory.LIME_GREEN, Sounds.MYSTIC_MIGHT_2669, new SkillBonus(Skills.MAGIC, 0.15)),

    /**
     * The Retribution.
     */
    RETRIBUTION(46, 12, 98, 47, PrayerCategory.LIGHT_BROWN, PrayerCategory.MAGENTA, new Audio(2682)),

    /**
     * The Redemption.
     */
    REDEMPTION(49, 6, 99, 49, PrayerCategory.LIGHT_BROWN, PrayerCategory.MAGENTA, new Audio(2680)),

    /**
     * The Smite.
     */
    SMITE(52, 2, 100, 51, PrayerCategory.LIGHT_BROWN, PrayerCategory.MAGENTA, new Audio(2686)),

    /**
     * The Chivalry.
     */
    CHIVALRY(60, 2, 1052, 55, PrayerCategory.PINK, Sounds.KR_CHIVALRY_3826, 65, new SkillBonus(Skills.DEFENCE, 0.2), new SkillBonus(Skills.STRENGTH, 0.18), new SkillBonus(Skills.ATTACK, 0.15)),

    /**
     * The Piety.
     */
    PIETY(70, 2, 1053, 57, PrayerCategory.PINK, Sounds.KR_PIETY_3825, 70, new SkillBonus(Skills.DEFENCE, 0.25), new SkillBonus(Skills.STRENGTH, 0.23), new SkillBonus(Skills.ATTACK, 0.2));

    private final static Object[][] ICON_CACHE = new Object[][]{{REDEMPTION, 5}, {RETRIBUTION, 3}, {SMITE, 4}, {PROTECT_FROM_MAGIC, 2, 10}, {PROTECT_FROM_MELEE, 0, 8}, {PROTECT_FROM_MISSILES, 1, 9}, {PROTECT_FROM_SUMMONING, 7, PROTECT_FROM_MELEE, 8, PROTECT_FROM_MISSILES, 9, PROTECT_FROM_MAGIC, 10}};

    private final int level;

    private final int drain;

    private final int config;

    private final int button;

    private final PrayerCategory restriction;

    private final PrayerCategory secondRestriction;

    private Audio sound;

    private int defenceReq;

    private final SkillBonus[] bonuses;

    PrayerType(int level, int drain, int config, int button, PrayerCategory restriction, int soundId, SkillBonus... bonuses) {
        this(level, drain, config, button, restriction, null, new Audio(soundId), bonuses);
    }

    PrayerType(int level, int drain, int config, int button, PrayerCategory restriction, int soundId, int defenceReq, SkillBonus... bonuses) {
        this(level, drain, config, button, restriction, null, new Audio(soundId), defenceReq, bonuses);
    }

    PrayerType(int level, int drain, int config, int button, PrayerCategory restriction, PrayerCategory secondRestriction, Audio sound, SkillBonus... bonuses) {
        this(level, drain, config, button, restriction, secondRestriction, sound, 1, bonuses);
    }

    PrayerType(int level, int drain, int config, int button, PrayerCategory restriction, PrayerCategory secondRestriction, Audio sound, int defenceReq, SkillBonus... bonuses) {
        this.level = level;
        this.drain = drain;
        this.config = config;
        this.button = button;
        this.restriction = restriction;
        this.secondRestriction = secondRestriction;
        this.sound = sound;
        this.bonuses = bonuses;
        this.defenceReq = defenceReq;
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
     * Gets drain.
     *
     * @return the drain
     */
    public int getDrain() {
        return drain;
    }

    /**
     * Gets config.
     *
     * @return the config
     */
    public int getConfig() {
        return config;
    }

    /**
     * Gets button.
     *
     * @return the button
     */
    public int getButton() {
        return button;
    }

    /**
     * Gets restriction.
     *
     * @return the restriction
     */
    public PrayerCategory getRestriction() {
        return restriction;
    }

    /**
     * Get bonuses skill bonus [ ].
     *
     * @return the skill bonus [ ]
     */
    public SkillBonus[] getBonuses() {
        return bonuses;
    }

    /**
     * Permitted boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public boolean permitted(final Player player) {
        if (player.getSkills().getStaticLevel(Skills.PRAYER) < getLevel() || player.getSkills().getStaticLevel(Skills.DEFENCE) < defenceReq) {
            playAudio(player, Sounds.PRAYER_OFF_2673);
            player.getDialogueInterpreter().sendDialogue("You need a <col=08088A>Prayer level of " + getLevel() + (player.getSkills().getStaticLevel(Skills.DEFENCE) < defenceReq ? (" and a Defence level of " + defenceReq) : "") + " to use " + StringUtils.formatDisplayName(name().toLowerCase().replace("_", " ")) + ".");
            return false;
        }
        return true;
    }

    /**
     * Toggle boolean.
     *
     * @param player the player
     * @param on     the on
     * @return the boolean
     */
    public boolean toggle(final Player player, final boolean on) {
        setVarp(player, getConfig(), on ? 1 : 0);
        if (on) {
            flag(player, this);
            player.getPrayer().getActive().add(this);
            iconify(player, getIcon(player, this));
            playAudio(player, sound.id);

            if (this == PrayerType.PIETY
                && new ZoneBorders(2732, 3467, 2739, 3471, 0).insideBorder(player)) {
                player.getAchievementDiaryManager().finishTask(player, DiaryType.SEERS_VILLAGE, 2, 3);
            }
            player.dispatch(new PrayerActivatedEvent(this));
        } else {
            player.getPrayer().getActive().remove(this);
            playAudio(player, Sounds.CANCEL_PRAYER_2663);
            findNextIcon(player);
            player.dispatch(new PrayerDeactivatedEvent(this));
        }
        return true;
    }

    /**
     * Flag.
     *
     * @param player the player
     * @param type   the type
     */
    public void flag(final Player player, final PrayerType type) {
        final List<PrayerType> active = player.getPrayer().getActive();
        final PrayerType[] remove = new PrayerType[active.size() + 10];
        int index = 0;
        for (int i = 0; i < active.size(); i++) {
            if (active.get(i).getRestriction() == type.getRestriction() || active.get(i).getSecondRestriction() != null && type.getSecondRestriction() != null && active.get(i).getSecondRestriction() == type.getSecondRestriction()) {
                remove[index++] = active.get(i);
                continue;
            }
            for (SkillBonus b : active.get(i).getBonuses()) {
                for (SkillBonus bb : type.getBonuses()) {
                    if ((bb.getSkillId() == b.getSkillId()) || (b.getSkillId() == Skills.STRENGTH || b.getSkillId() == Skills.ATTACK) && (bb.getSkillId() == Skills.MAGIC || bb.getSkillId() == Skills.RANGE) || (b.getSkillId() == Skills.RANGE || b.getSkillId() == Skills.MAGIC) && (bb.getSkillId() == Skills.ATTACK || bb.getSkillId() == Skills.STRENGTH) || (b.getSkillId() == Skills.DEFENCE && bb.getSkillId() == Skills.DEFENCE)) {
                        remove[index++] = active.get(i);
                    }
                }
            }
        }
        for (int i = 0; i < index; i++) {
            remove[i].toggle(player, false);
        }
    }

    /**
     * Iconify boolean.
     *
     * @param player the player
     * @param icon   the icon
     * @return the boolean
     */
    public boolean iconify(final Player player, final int icon) {
        if (icon == -1) {
            return false;
        }
        player.getAppearance().setHeadIcon(icon);
        player.getAppearance().sync();
        return false;
    }

    /**
     * Find next icon.
     *
     * @param player the player
     */
    public void findNextIcon(final Player player) {
        if (!hasIcon(player)) {
            player.getAppearance().setHeadIcon(-1);
            player.getAppearance().sync();
        }
        if ((this == PROTECT_FROM_MELEE || this == PROTECT_FROM_MISSILES || this == PROTECT_FROM_MAGIC) && player.getPrayer().get(PROTECT_FROM_SUMMONING)) {
            iconify(player, 7);
        } else if (this == PROTECT_FROM_SUMMONING) {
            for (PrayerType t : player.getPrayer().getActive()) {
                iconify(player, getIcon(player, t));
            }
            if (player.getAppearance().getHeadIcon() == 7) {
                player.getAppearance().setHeadIcon(-1);
                player.getAppearance().sync();
            }
        }
    }

    /**
     * Gets icon.
     *
     * @param player the player
     * @param type   the type
     * @return the icon
     */
    public int getIcon(final Player player, final PrayerType type) {
        List<PrayerType> active = player.getPrayer().getActive();
        for (int i = 0; i < ICON_CACHE.length; i++) {
            if (ICON_CACHE[i].length == 2 && type == ((PrayerType) ICON_CACHE[i][0])) {
                return (int) ICON_CACHE[i][1];
            } else if (ICON_CACHE[i].length == 3 && type == ((PrayerType) ICON_CACHE[i][0])) {
                if (active.contains(PROTECT_FROM_SUMMONING)) {
                    return (int) ICON_CACHE[i][2];
                }
                return (int) ICON_CACHE[i][1];
            } else if (ICON_CACHE[i].length == 8 && type == ((PrayerType) ICON_CACHE[i][0])) {
                for (int k = 2; k < ICON_CACHE[i].length; k++) {
                    if (active.contains(ICON_CACHE[i][k])) {
                        return (int) ICON_CACHE[i][k + 1];
                    }
                }
                return (int) ICON_CACHE[i][1];
            }
        }
        return -1;
    }

    /**
     * Has icon boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public boolean hasIcon(final Player player) {
        int count = 0;
        for (PrayerType type : player.getPrayer().getActive()) {
            if (getIcon(player, type) != -1) {
                count++;
            }
        }
        return count != 0;
    }

    /**
     * Get prayer type.
     *
     * @param button the button
     * @return the prayer type
     */
    public static PrayerType get(int button) {
        for (PrayerType type : PrayerType.values()) {
            if (type.getButton() == button) {
                return type;
            }
        }
        return null;
    }

    /**
     * Get melee types prayer type [ ].
     *
     * @return the prayer type [ ]
     */
    public static PrayerType[] getMeleeTypes() {
        return getByBonus(Skills.ATTACK, Skills.STRENGTH);
    }

    /**
     * Get range types prayer type [ ].
     *
     * @return the prayer type [ ]
     */
    public static PrayerType[] getRangeTypes() {
        return getByBonus(Skills.RANGE);
    }

    /**
     * Get magic types prayer type [ ].
     *
     * @return the prayer type [ ]
     */
    public static PrayerType[] getMagicTypes() {
        return getByBonus(Skills.MAGIC);
    }

    /**
     * Get by bonus prayer type [ ].
     *
     * @param ids the ids
     * @return the prayer type [ ]
     */
    public static PrayerType[] getByBonus(int... ids) {
        PrayerType[] types = new PrayerType[values().length];
        int count = 0;
        for (PrayerType type : values()) {
            for (SkillBonus b : type.getBonuses()) {
                for (int i : ids) {
                    if (i == b.getSkillId()) {
                        types[count] = type;
                        count++;
                    }
                }
            }
        }
        return types;
    }

    /**
     * Gets second restriction.
     *
     * @return the second restriction
     */
    public PrayerCategory getSecondRestriction() {
        return secondRestriction;
    }

    /**
     * Gets sound.
     *
     * @return the sound
     */
    public Audio getSound() {
        return sound;
    }

    /**
     * Gets defence req.
     *
     * @return the defence req
     */
    public int getDefenceReq() {
        return defenceReq;
    }

}
