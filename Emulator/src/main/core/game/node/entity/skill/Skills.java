package core.game.node.entity.skill;

import content.data.GameAttributes;
import content.global.plugin.item.equipment.gloves.BrawlingGloves;
import content.global.plugin.item.equipment.gloves.BrawlingGlovesManager;
import core.game.event.DynamicSkillLevelChangeEvent;
import core.game.event.XPGainEvent;
import core.game.node.entity.Entity;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.PlayerMonitor;
import core.game.node.entity.player.link.request.assist.AssistSessionPulse;
import core.game.node.item.Item;
import core.game.world.GameWorld;
import core.game.world.repository.Repository;
import core.net.packet.PacketRepository;
import core.net.packet.context.SkillContext;
import core.net.packet.out.SkillLevel;
import core.plugin.type.ExperiencePlugins;
import kotlin.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.rs.consts.Items;
import org.rs.consts.Sounds;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import static core.api.ContentAPIKt.getWorldTicks;
import static core.api.ContentAPIKt.playAudio;
import static java.lang.Math.floor;

/**
 * Represents the skills system for an entity.
 */
public final class Skills {

    /**
     * The multiplier for experience gains.
     */
    public double experienceMultiplier = 1.0;

    /**
     * Array of skill names.
     */
    public static final String[] SKILL_NAME = {"Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting", "Hunter", "Construction", "Summoning"};

    /**
     * Skill index constants.
     */
    public static final int
            ATTACK = 0,
            DEFENCE = 1,
            STRENGTH = 2,
            HITPOINTS = 3,
            RANGE = 4,
            PRAYER = 5,
            MAGIC = 6,
            COOKING = 7,
            WOODCUTTING = 8,
            FLETCHING = 9,
            FISHING = 10,
            FIREMAKING = 11,
            CRAFTING = 12,
            SMITHING = 13,
            MINING = 14,
            HERBLORE = 15,
            AGILITY = 16,
            THIEVING = 17,
            SLAYER = 18,
            FARMING = 19,
            RUNECRAFTING = 20,
            HUNTER = 21,
            CONSTRUCTION = 22,
            SUMMONING = 23;

    /**
     * Total number of skills.
     */
    public static final int NUM_SKILLS = 24;

    /**
     * The entity associated with this skills object.
     */
    private final Entity entity;

    /**
     * Experience values for each skill.
     */
    private final double[] experience;

    /**
     * Stores the last experience update.
     */
    private double[] lastUpdateXp = null;

    /**
     * Last update tick.
     */
    private int lastUpdate = GameWorld.getTicks();

    /**
     * Base levels of each skill.
     */
    private final int[] staticLevels;

    /**
     * Current levels of each skill, affected by boosts or debuffs.
     */
    private final int[] dynamicLevels;

    /**
     * Current prayer points.
     */
    private double prayerPoints = 1.;

    /**
     * Current lifepoints.
     */
    private int lifepoints = 10;

    /**
     * Lifepoints increase tracker.
     */
    private int lifepointsIncrease = 0;

    /**
     * Tracks total experience gained.
     */
    private double experienceGained = 0;

    /**
     * Whether life points need updating.
     */
    private boolean lifepointsUpdate;

    /**
     * Milestone tracking for combat levels.
     */
    private int combatMilestone;

    /**
     * Milestone tracking for skill levels.
     */
    private int skillMilestone;

    /**
     * The last skill that was trained.
     */
    public int lastTrainedSkill = -1;

    /**
     * The last amount of experience gained.
     */
    public int lastXpGain = 0;

    /**
     * Constructs a Skills object for the given entity.
     * Initializes all skills to level 1, except Hitpoints which starts at 10.
     *
     * @param entity The entity to associate with this skills object.
     */
    public Skills(Entity entity) {
        this.entity = entity;
        this.experience = new double[24];
        this.staticLevels = new int[24];
        this.dynamicLevels = new int[24];
        for (int i = 0; i < 24; i++) {
            this.staticLevels[i] = 1;
            this.dynamicLevels[i] = 1;
        }
        this.experience[HITPOINTS] = 1154;
        this.dynamicLevels[HITPOINTS] = 10;
        this.staticLevels[HITPOINTS] = 10;
        entity.getProperties().setCombatLevel(3);
    }

    /**
     * Determines if a given skill is a combat-related skill.
     *
     * @param skill The skill index.
     * @return True if the skill is combat-related, false otherwise.
     */
    public boolean isCombat(int skill) {
        if ((skill >= ATTACK && skill <= MAGIC) || (skill == SUMMONING)) {
            return true;
        }
        return false;
    }

    /**
     * Configures skills-related settings.
     */
    public void configure() {
        updateCombatLevel();
    }

    /**
     * Performs periodic skill updates.
     */
    public void pulse() {
        if (lifepoints < 1) {
            return;
        }
    }

    /**
     * Copies the skill data from another Skills object.
     *
     * @param skills The skills object to copy from.
     */
    public void copy(Skills skills) {
        for (int i = 0; i < 24; i++) {
            this.staticLevels[i] = skills.staticLevels[i];
            this.dynamicLevels[i] = skills.dynamicLevels[i];
            this.experience[i] = skills.experience[i];
        }
        prayerPoints = skills.prayerPoints;
        lifepoints = skills.lifepoints;
        lifepointsIncrease = skills.lifepointsIncrease;
        experienceGained = skills.experienceGained;
    }

    /**
     * Adds experience to a given skill.
     *
     * @param slot       The skill index.
     * @param experience The amount of experience to add.
     * @param playerMod  Whether the experience gain is modified by the player.
     */
    public void addExperience(int slot, double experience, boolean playerMod) {
        if (lastUpdateXp == null)
            lastUpdateXp = this.experience.clone();
        double mod = getExperienceMod(slot, experience, playerMod, true);
        final Player player = entity instanceof Player ? ((Player) entity) : null;
        final AssistSessionPulse assist = entity.getExtension(AssistSessionPulse.class);
        if (assist != null && assist.translateExperience(player, slot, experience, mod)) {
            return;
        }
        boolean already200m = this.experience[slot] == 200000000;
        double experienceAdd = (experience * mod);
        // Check if a player has brawling gloves and, if equipped, modify xp.
        BrawlingGlovesManager bgManager = BrawlingGlovesManager.getInstance(player);
        if (!bgManager.GloveCharges.isEmpty()) {
            Item gloves = BrawlingGloves.forSkill(slot) == null ? null : new Item(BrawlingGloves.forSkill(slot).id);
            if (gloves == null && (slot == Skills.STRENGTH || slot == Skills.DEFENCE)) {
                gloves = new Item(BrawlingGloves.forSkill(Skills.ATTACK).id);
            }
            if (gloves != null && player.getEquipment().containsItem(gloves)) {
                experienceAdd += experienceAdd * bgManager.getExperienceBonus();
                bgManager.updateCharges(gloves.getId(), 1);
            }
        }
        // Check for Flame gloves and ring of Fire.
        if (player.getEquipment().containsItem(new Item(Items.FLAME_GLOVES_13660)) || player.getEquipment().containsItem(new Item(Items.RING_OF_FIRE_13659))) {
            if (slot == Skills.FIREMAKING) {
                int count = 0;
                if (player.getEquipment().containsItem(new Item(Items.FLAME_GLOVES_13660))) count += 1;
                if (player.getEquipment().containsItem(new Item(Items.RING_OF_FIRE_13659))) count += 1;
                if (count == 2) experienceAdd += (0.05 * experienceAdd);
                else experienceAdd += (0.02 * experienceAdd);
            }
        }
        this.experience[slot] += experienceAdd;
        if (this.experience[slot] >= 200000000) {
            if (!already200m && !player.isArtificial()) {
                Repository.sendNews(entity.asPlayer().getUsername() + " has just reached 200m experience in " + SKILL_NAME[slot] + "!");
            }
            this.experience[slot] = 200000000;
        }
        if (entity instanceof Player && this.experience[slot] > 175) {
            if (!player.getAttribute(GameAttributes.TUTORIAL_COMPLETE, false) && slot != HITPOINTS) {
                this.experience[slot] = 175;
            }
        }
        experienceGained += experienceAdd;
        ExperiencePlugins.run(player, slot, experienceAdd);
        int newLevel = getStaticLevelByExperience(slot);
        if (newLevel > staticLevels[slot]) {
            int amount = newLevel - staticLevels[slot];
            if (dynamicLevels[slot] < newLevel) {
                dynamicLevels[slot] += amount;
            }
            if (slot == HITPOINTS) {
                lifepoints += amount;
            }
            staticLevels[slot] = newLevel;

            if (entity instanceof Player) {
                player.updateAppearance();
                LevelUp.levelUp(player, slot, amount);
                updateCombatLevel();
            }
        }
        if (entity instanceof Player) {
            PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, slot));
            entity.dispatch(new XPGainEvent(slot, experienceAdd));
        }
        if (GameWorld.getTicks() - lastUpdate >= 200) {
            ArrayList<Pair<Integer, Double>> diffs = new ArrayList<>();
            for (int i = 0; i < this.experience.length; i++) {
                double diff = this.experience[i] - lastUpdateXp[i];
                if (diff != 0.0) {
                    diffs.add(new Pair<>(i, diff));
                }
            }
            PlayerMonitor.logXpGains(player, diffs);
            lastUpdateXp = this.experience.clone();
            lastUpdate = GameWorld.getTicks();
        }
        lastTrainedSkill = slot;
        lastXpGain = getWorldTicks();
    }

    private double getExperienceMod(int slot, double experience, boolean playerMod, boolean multiplyer) {
        return experienceMultiplier;

    }

    /**
     * Add experience.
     *
     * @param slot       the slot
     * @param experience the experience
     */
    public void addExperience(final int slot, double experience) {
        addExperience(slot, experience, false);
    }

    /**
     * Gets the highest combat skill by level.
     *
     * @return The index of the highest combat skill.
     */
    public int getHighestCombatSkillId() {
        int id = 0;
        int last = 0;
        for (int i = 0; i < 5; i++) {
            if (staticLevels[i] > last) {
                last = staticLevels[i];
                id = i;
            }
        }
        return id;
    }

    /**
     * Restores all skill levels to their base values.
     */
    public void restore() {
        for (int i = 0; i < 24; i++) {
            int staticLevel = getStaticLevel(i);
            setLevel(i, staticLevel);
        }
        if (entity instanceof Player) {
            playAudio(entity.asPlayer(), Sounds.PRAYER_RECHARGE_2674);
        }
        rechargePrayerPoints();
    }

    /**
     * Parses skill data from a byte buffer.
     *
     * @param buffer The byte buffer containing skill data.
     */
    public void parse(ByteBuffer buffer) {
        for (int i = 0; i < 24; i++) {
            experience[i] = ((double) buffer.getInt() / 10D);
            dynamicLevels[i] = buffer.get() & 0xFF;
            if (i == HITPOINTS) {
                lifepoints = dynamicLevels[i];
            } else if (i == PRAYER) {
                prayerPoints = dynamicLevels[i];
            }
            staticLevels[i] = buffer.get() & 0xFF;
        }
        experienceGained = buffer.getInt();
    }

    /**
     * Parses skill data from a JSONArray and updates skill levels and experience.
     *
     * @param skillData The JSONArray containing skill data.
     */
    public void parse(JSONArray skillData) {
        for (int i = 0; i < skillData.size(); i++) {
            JSONObject skill = (JSONObject) skillData.get(i);
            int id = Integer.parseInt(skill.get("id").toString());
            dynamicLevels[id] = Integer.parseInt(skill.get("dynamic").toString());
            if (id == HITPOINTS) {
                lifepoints = dynamicLevels[i];
            } else if (id == PRAYER) {
                prayerPoints = dynamicLevels[i];
            }
            staticLevels[id] = Integer.parseInt(skill.get("static").toString());
            experience[id] = Double.parseDouble(skill.get("experience").toString());
        }
    }

    /**
     * Corrects experience values by applying a divisor and updating skill levels.
     *
     * @param divisor The value to divide experience points by.
     */
    public void correct(double divisor) {
        for (int i = 0; i < staticLevels.length; i++) {
            experience[i] /= divisor;
            staticLevels[i] = getStaticLevelByExperience(i);
            dynamicLevels[i] = staticLevels[i];
            if (i == PRAYER) {
                setPrayerPoints(staticLevels[i]);
            }
            if (i == HITPOINTS) {
                setLifepoints(staticLevels[i]);
            }
        }
        experienceMultiplier = 1.0;
        updateCombatLevel();
    }

    /**
     * Parses experience rate from a ByteBuffer.
     *
     * @param buffer The ByteBuffer containing experience rate data.
     */
    public void parseExpRate(ByteBuffer buffer) {
        experienceMultiplier = buffer.getDouble();
        if (GameWorld.getSettings().getDefault_xp_rate() != experienceMultiplier) {
            experienceMultiplier = GameWorld.getSettings().getDefault_xp_rate();
        }
    }

    /**
     * Saves skill data to a ByteBuffer.
     *
     * @param buffer The ByteBuffer to save skill data into.
     */
    public void save(ByteBuffer buffer) {
        for (int i = 0; i < 24; i++) {
            buffer.putInt((int) (experience[i] * 10));
            if (i == HITPOINTS) {
                buffer.put((byte) lifepoints);
            } else if (i == PRAYER) {
                buffer.put((byte) Math.ceil(prayerPoints));
            } else {
                buffer.put((byte) dynamicLevels[i]);
            }
            buffer.put((byte) staticLevels[i]);
        }
        buffer.putInt((int) experienceGained);
    }

    /**
     * Saves experience rate to a ByteBuffer.
     *
     * @param buffer The ByteBuffer to save experience rate into.
     */
    public void saveExpRate(ByteBuffer buffer) {
        buffer.putDouble(experienceMultiplier);
    }

    /**
     * Refreshes the skill levels for the entity, sending updates to the player.
     */
    public void refresh() {
        if (!(entity instanceof Player)) {
            return;
        }
        Player player = (Player) entity;
        for (int i = 0; i < 24; i++) {
            PacketRepository.send(SkillLevel.class, new SkillContext(player, i));
        }
        LevelUp.sendFlashingIcons(player, -1);
    }

    /**
     * Calculates the static level based on experience points.
     *
     * @param slot The skill slot.
     * @return The calculated static level.
     */
    public int getStaticLevelByExperience(int slot) {
        double exp = experience[slot];

        int points = 0;
        int output = 0;
        for (byte lvl = 1; lvl < 100; lvl++) {
            points += floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            output = (int) floor(points / 4);
            if ((output - 1) >= exp) {
                return lvl;
            }
        }
        return 99;
    }

    /**
     * Level from xp int.
     *
     * @param exp the exp
     * @return the int
     */
    public int levelFromXP(double exp) {

        int points = 0;
        int output = 0;
        for (byte lvl = 1; lvl < 100; lvl++) {
            points += floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            output = (int) floor(points / 4);
            if ((output - 1) >= exp) {
                return lvl;
            }
        }
        return 99;
    }

    /**
     * Gets experience by level.
     *
     * @param level the level
     * @return the experience by level
     */
    public int getExperienceByLevel(int level) {
        int points = 0;
        int output = 0;
        for (int lvl = 1; lvl <= level; lvl++) {
            points += floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            if (lvl >= level) {
                return output;
            }
            output = (int) floor(points / 4);
        }
        return 0;
    }

    /**
     * Update combat level boolean.
     *
     * @return the boolean
     */
    @SuppressWarnings("deprecation")
    public boolean updateCombatLevel() {
        int level = calculateCombatLevel();
        boolean update = level != entity.getProperties().getCombatLevel();
        if (update) {
            entity.getProperties().setCombatLevel(level);
        }
        return update;
    }

    private int calculateCombatLevel() {
        if (entity instanceof NPC) {
            return ((NPC) entity).getDefinition().combatLevel;
        }

        int attackStrength = staticLevels[ATTACK] + staticLevels[STRENGTH];
        int ranged = staticLevels[RANGE] * 3 / 2;
        int magic = staticLevels[MAGIC] * 3 / 2;

        int maxCombatStat = Math.max(attackStrength, Math.max(ranged, magic));
        maxCombatStat = (maxCombatStat * 13) / 10;

        int baseStats = staticLevels[DEFENCE] + staticLevels[HITPOINTS] + staticLevels[PRAYER] / 2;

        if (GameWorld.getSettings().isMembers()) {
            baseStats += staticLevels[SUMMONING] / 2;
        }

        return (maxCombatStat + baseStats) / 4;
    }

    /**
     * Gets entity.
     *
     * @return the entity
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Retrieves the experience points for a given skill slot.
     *
     * @param slot The skill slot index.
     * @return The experience points for the given slot.
     */
    public double getExperience(int slot) {
        return experience[slot];
    }

    /**
     * Retrieves the static level for a given skill slot.
     *
     * @param slot The skill slot index.
     * @return The static level for the given slot.
     */
    public int getStaticLevel(int slot) {
        return staticLevels[slot];
    }

    /**
     * Sets experience gained.
     *
     * @param experienceGained the experience gained
     */
    public void setExperienceGained(double experienceGained) {
        this.experienceGained = experienceGained;
    }

    /**
     * Gets experience gained.
     *
     * @return the experience gained
     */
    public double getExperienceGained() {
        return experienceGained;
    }

    /**
     * Sets level.
     *
     * @param slot  the slot
     * @param level the level
     */
    public void setLevel(int slot, int level) {
        if (slot == HITPOINTS) {
            lifepoints = level;
        } else if (slot == PRAYER) {
            prayerPoints = level;
        }

        int previousLevel = dynamicLevels[slot];
        dynamicLevels[slot] = level;

        if (entity instanceof Player) {
            PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, slot));
            entity.dispatch(new DynamicSkillLevelChangeEvent(slot, previousLevel, level));
        }
    }

    /**
     * Gets level.
     *
     * @param slot          the slot
     * @param discardAssist the discard assist
     * @return the level
     */
    public int getLevel(int slot, boolean discardAssist) {
        if (!discardAssist) {
            if (entity instanceof Player) {
                final Player p = (Player) entity;
                final AssistSessionPulse assist = p.getExtension(AssistSessionPulse.class);
                if (assist != null && assist.getPlayer() != p) {
                    Player assister = assist.getPlayer();
                    int index = assist.getSkillIndex(slot);
                    if (index != -1 && !assist.isRestricted()) {

                        // assist.getSkills()[index] + ", " + SKILL_NAME[slot]);
                        if (assist.getSkills()[index]) {
                            int assistLevel = assister.getSkills().getLevel(slot);
                            int playerLevel = dynamicLevels[slot];
                            if (assistLevel > playerLevel) {
                                return assistLevel;
                            }
                        }
                    }
                }
            }
        }
        return dynamicLevels[slot];
    }

    /**
     * Gets level.
     *
     * @param slot the slot
     * @return the level
     */
    public int getLevel(int slot) {
        return getLevel(slot, false);
    }

    /**
     * Sets the entity's lifepoints.
     *
     * @param lifepoints The new lifepoints value.
     */
    public void setLifepoints(int lifepoints) {
        this.lifepoints = lifepoints;
        if (this.lifepoints < 0) {
            this.lifepoints = 0;
        }
        lifepointsUpdate = true;
    }

    /**
     * Gets the entity's current lifepoints.
     *
     * @return The current lifepoints.
     */
    public int getLifepoints() {
        return lifepoints;
    }

    /**
     * Gets maximum lifepoints.
     *
     * @return the maximum lifepoints
     */
    public int getMaximumLifepoints() {
        return staticLevels[HITPOINTS] + lifepointsIncrease;
    }

    /**
     * Sets lifepoints increase.
     *
     * @param amount the amount
     */
    public void setLifepointsIncrease(int amount) {
        this.lifepointsIncrease = amount;
    }

    /**
     * Heals the entity by a given amount, up to the maximum lifepoints.
     *
     * @param health The amount to heal.
     * @return The remaining health after healing, if the maximum is exceeded.
     */
    public int heal(int health) {
        lifepoints += health;
        int left = 0;
        if (lifepoints > getMaximumLifepoints()) {
            left = lifepoints - getMaximumLifepoints();
            lifepoints = getMaximumLifepoints();
        }
        lifepointsUpdate = true;
        return left;
    }

    /**
     * Heals the entity without any restrictions.
     *
     * @param amount The amount of lifepoints to add.
     */
    public void healNoRestrictions(int amount) {
        lifepoints += amount;
        lifepointsUpdate = true;
    }

    /**
     * Inflicts damage on the entity.
     *
     * @param damage The amount of damage to apply.
     * @return The excess damage beyond lifepoints.
     */
    public int hit(int damage) {
        lifepoints -= damage;
        int left = 0;
        if (lifepoints < 0) {
            left = -lifepoints;
            lifepoints = 0;
        }
        lifepointsUpdate = true;
        return left;
    }

    /**
     * Retrieves the entity's current prayer points.
     *
     * @return The current prayer points.
     */
    public double getPrayerPoints() {
        return prayerPoints;
    }

    /**
     * Fully restores the entity's prayer points.
     */
    public void rechargePrayerPoints() {
        prayerPoints = staticLevels[PRAYER];
        if (entity instanceof Player) {
            PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, PRAYER));
        }
    }

    /**
     * Decreases the entity's prayer points by a specified amount.
     *
     * @param amount The amount to decrement.
     */
    public void decrementPrayerPoints(double amount) {
        prayerPoints -= amount;
        if (prayerPoints < 0) {
            prayerPoints = 0;
        }
        // if (prayerPoints > staticLevels[PRAYER]) {
        // prayerPoints = staticLevels[PRAYER];
        // }
        if (entity instanceof Player) {
            PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, PRAYER));
        }
    }

    /**
     * Increases the entity's prayer points by a specified amount.
     *
     * @param amount The amount to increment.
     */
    public void incrementPrayerPoints(double amount) {
        prayerPoints += amount;
        if (prayerPoints < 0) {
            prayerPoints = 0;
        }
        if (prayerPoints > staticLevels[PRAYER]) {
            prayerPoints = staticLevels[PRAYER];
        }
        if (entity instanceof Player) {
            PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, PRAYER));
        }
    }

    /**
     * Sets the entity's prayer points to a specified value.
     *
     * @param amount The amount to set.
     */
    public void setPrayerPoints(double amount) {
        prayerPoints = amount;
        if (entity instanceof Player) {
            PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, PRAYER));
        }
    }

    /**
     * Updates the entity's skill level.
     *
     * @param skill   The skill ID.
     * @param amount  The amount to update.
     * @param maximum The maximum allowable level.
     * @return The remaining value after adjustment.
     */
    public int updateLevel(int skill, int amount, int maximum) {
        if (amount > 0 && dynamicLevels[skill] > maximum) {
            return -amount;
        }
        int left = (dynamicLevels[skill] + amount) - maximum;
        int level = dynamicLevels[skill] += amount;
        if (level < 0) {
            dynamicLevels[skill] = 0;
        } else if (amount < 0 && level < maximum) {
            dynamicLevels[skill] = maximum;
        } else if (amount > 0 && level > maximum) {
            dynamicLevels[skill] = maximum;
        }
        if (entity instanceof Player) {
            PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, skill));
        }
        return left;
    }

    /**
     * Updates the entity's skill level with a calculated maximum.
     *
     * @param skill  The skill ID.
     * @param amount The amount to update.
     * @return The remaining value after adjustment.
     */
    public int updateLevel(int skill, int amount) {
        return updateLevel(skill, amount, amount >= 0 ? getStaticLevel(skill) + amount : getStaticLevel(skill) - amount);
    }

    /**
     * Drains a skill level by a percentage.
     *
     * @param skill                  The skill ID.
     * @param drainPercentage        The percentage to drain.
     * @param maximumDrainPercentage The maximum allowable drain percentage.
     */
    public void drainLevel(int skill, double drainPercentage, double maximumDrainPercentage) {
        int drain = (int) (dynamicLevels[skill] * drainPercentage);
        int minimum = (int) (staticLevels[skill] * (1.0 - maximumDrainPercentage));
        updateLevel(skill, -drain, minimum);
    }

    /**
     * Sets a skill's static level and updates experience accordingly.
     *
     * @param skill The skill ID.
     * @param level The new level to set.
     */
    public void setStaticLevel(int skill, int level) {
        experience[skill] = getExperienceByLevel(staticLevels[skill] = dynamicLevels[skill] = level);
        if (entity instanceof Player) {
            PacketRepository.send(SkillLevel.class, new SkillContext((Player) entity, skill));
        }
    }

    /**
     * Gets mastered skills.
     *
     * @return the mastered skills
     */
    public int getMasteredSkills() {
        int count = 0;
        for (int i = 0; i < 23; i++) {
            if (getStaticLevel(i) >= 99) {
                count++;
            }
        }
        return count;
    }

    /**
     * Gets skill by name.
     *
     * @param name the name
     * @return the skill by name
     */
    public static int getSkillByName(final String name) {
        for (int i = 0; i < SKILL_NAME.length; i++) {
            if (SKILL_NAME[i].equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets total level.
     *
     * @return the total level
     */
    public int getTotalLevel() {
        int level = 0;
        for (int i = 0; i < 24; i++) {
            level += getStaticLevel(i);
        }
        return level;
    }

    /**
     * Gets total xp.
     *
     * @return the total xp
     */
    public int getTotalXp() {
        int total = 0;
        for (int skill = 0; skill < Skills.SKILL_NAME.length; skill++) {
            total += this.getExperience(skill);
        }
        return total;
    }

    /**
     * Is lifepoints update boolean.
     *
     * @return the boolean
     */
    public boolean isLifepointsUpdate() {
        return lifepointsUpdate;
    }

    /**
     * Sets lifepoints update.
     *
     * @param lifepointsUpdate the lifepoints update
     */
    public void setLifepointsUpdate(boolean lifepointsUpdate) {
        this.lifepointsUpdate = lifepointsUpdate;
    }

    /**
     * Get static levels int [ ].
     *
     * @return the int [ ]
     */
    public int[] getStaticLevels() {
        return staticLevels;
    }

    /**
     * Has level boolean.
     *
     * @param skillId the skill id
     * @param i       the
     * @return the boolean
     */
    public boolean hasLevel(int skillId, int i) {
        return getStaticLevel(skillId) >= i;
    }

    /**
     * Gets combat milestone.
     *
     * @return the combat milestone
     */
    public int getCombatMilestone() {
        return combatMilestone;
    }

    /**
     * Sets combat milestone.
     *
     * @param combatMilestone the combat milestone
     */
    public void setCombatMilestone(int combatMilestone) {
        this.combatMilestone = combatMilestone;
    }

    /**
     * Gets skill milestone.
     *
     * @return the skill milestone
     */
    public int getSkillMilestone() {
        return skillMilestone;
    }

    /**
     * Sets skill milestone.
     *
     * @param skillMilestone the skill milestone
     */
    public void setSkillMilestone(int skillMilestone) {
        this.skillMilestone = skillMilestone;
    }

    /**
     * Get dynamic levels int [ ].
     *
     * @return the int [ ]
     */
    public int[] getDynamicLevels() {
        return dynamicLevels;
    }
}
