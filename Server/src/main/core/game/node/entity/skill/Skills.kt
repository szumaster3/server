package core.game.node.entity.skill

import com.google.gson.JsonArray
import content.data.GameAttributes
import content.global.plugins.item.equipment.brawling_gloves.BrawlingGloves.Companion.forSkill
import content.global.plugins.item.equipment.brawling_gloves.BrawlingGlovesManager
import core.api.getWorldTicks
import core.api.playAudio
import core.game.event.DynamicSkillLevelChangeEvent
import core.game.event.XPGainEvent
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.PlayerMonitor.logXpGains
import core.game.node.entity.player.link.request.assist.AssistSessionPulse
import core.game.node.entity.skill.LevelUp.levelup
import core.game.node.entity.skill.LevelUp.sendFlashingIcons
import core.game.node.item.Item
import core.game.world.GameWorld.settings
import core.game.world.GameWorld.ticks
import core.game.world.repository.Repository.sendNews
import core.net.packet.PacketRepository
import core.net.packet.context.SkillContext
import core.net.packet.out.SkillLevel
import core.plugin.type.ExperiencePlugins.run
import java.nio.ByteBuffer
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.pow
import shared.consts.Items
import shared.consts.Sounds

/** Represents the skills system for an entity. */
class Skills(
    /** The entity associated with this skills object. */
    val entity: Entity
) {
    /** The multiplier for experience gains. */
    var experienceMultiplier: Double = 1.0

    /** Experience values for each skill. */
    val experience = DoubleArray(24)

    /** Stores the last experience update. */
    var lastUpdateXp: DoubleArray? = null

    /** Last update tick. */
    var lastUpdate = ticks

    /** Base levels of each skill. */
    val staticLevels: IntArray = IntArray(24)

    /** Current levels of each skill, affected by boosts or debuffs. */
    @JvmField val dynamicLevels: IntArray = IntArray(24)

    /** Current prayer points. */
    var prayerPoints = 1.0

    /** Current lifepoints. */
    var lifepoints = 10

    /** Lifepoints increase tracker. */
    var lifepointsIncrease = 0

    /** Tracks total experience gained. */
    var experienceGained: Double = 0.0

    /** Whether life points need updating. */
    var isLifepointsUpdate: Boolean = false

    /** Milestone tracking for combat levels. */
    var combatMilestone: Int = 0

    /** Milestone tracking for skill levels. */
    var skillMilestone: Int = 0

    /** The last skill that was trained. */
    var lastTrainedSkill: Int = -1

    /** The last amount of experience gained. */
    var lastXpGain: Int = 0

    /**
     * Constructs a Skills object for the given entity. Initializes all skills to level 1, except
     * Hitpoints which starts at 10.
     *
     * @param entity The entity to associate with this skills object.
     */
    init {
        for (i in 0..23) {
            staticLevels[i] = 1
            dynamicLevels[i] = 1
        }
        experience[HITPOINTS] = 1154.0
        dynamicLevels[HITPOINTS] = 10
        staticLevels[HITPOINTS] = 10
        entity.properties.combatLevel = 3
    }

    /**
     * Determines if a given skill is a combat-related skill.
     *
     * @param skill The skill index.
     * @return True if the skill is combat-related, false otherwise.
     */
    fun isCombat(skill: Int): Boolean {
        if ((skill >= ATTACK && skill <= MAGIC) || (skill == SUMMONING)) {
            return true
        }
        return false
    }

    /** Configures skills-related settings. */
    fun configure() {
        updateCombatLevel()
    }

    /** Performs periodic skill updates. */
    fun pulse() {
        if (lifepoints < 1) {
            return
        }
    }

    /**
     * Copies the skill data from another Skills object.
     *
     * @param skills The skills object to copy from.
     */
    fun copy(skills: Skills) {
        for (i in 0..23) {
            staticLevels[i] = skills.staticLevels[i]
            dynamicLevels[i] = skills.dynamicLevels[i]
            experience[i] = skills.experience[i]
        }
        prayerPoints = skills.prayerPoints
        lifepoints = skills.lifepoints
        lifepointsIncrease = skills.lifepointsIncrease
        experienceGained = skills.experienceGained
    }

    /**
     * Adds experience to a given skill.
     *
     * @param slot The skill index.
     * @param experience The amount of experience to add.
     * @param playerMod Whether the experience gain is modified by the player.
     */
    @JvmOverloads
    fun addExperience(slot: Int, experience: Double, playerMod: Boolean = false) {
        if (lastUpdateXp == null) lastUpdateXp = this.experience.clone()
        val mod = getExperienceMod(slot, experience, playerMod, true)
        val player = entity as? Player
        val assist = entity.getExtension<AssistSessionPulse>(AssistSessionPulse::class.java)
        if (assist != null && player != null && assist.translateExperience(player, slot, experience, mod)) {
            return
        }
        val already200m = this.experience[slot] == 200000000.0
        var experienceAdd = (experience * mod)
        // Check if a player has brawling gloves and, if equipped, modify xp.
        val bgManager = BrawlingGlovesManager.getInstance(player)
        if (!bgManager.GloveCharges.isEmpty()) {
            var gloves = if (forSkill(slot) == null) null else Item(forSkill(slot)!!.id)
            if (gloves == null && (slot == STRENGTH || slot == DEFENCE)) {
                gloves = Item(forSkill(ATTACK)!!.id)
            }
            if (gloves != null && player!!.equipment.containsItem(gloves)) {
                experienceAdd += experienceAdd * bgManager.experienceBonus
                bgManager.updateCharges(gloves.id, 1)
            }
        }
        // Check for Flame gloves and ring of Fire.
        if (
            player!!.equipment.containsItem(Item(Items.FLAME_GLOVES_13660)) ||
            player.equipment.containsItem(Item(Items.RING_OF_FIRE_13659))
        ) {
            if (slot == FIREMAKING) {
                var count = 0
                if (player.equipment.containsItem(Item(Items.FLAME_GLOVES_13660))) count += 1
                if (player.equipment.containsItem(Item(Items.RING_OF_FIRE_13659))) count += 1
                experienceAdd += if (count == 2) 0.05 * experienceAdd else 0.02 * experienceAdd
            }
        }
        this.experience[slot] += experienceAdd
        if (this.experience[slot] >= 200000000) {
            if (!already200m && !player.isArtificial) {
                sendNews(
                    entity.asPlayer().username +
                            " has just reached 200m experience in " +
                            SKILL_NAME[slot] +
                            "!"
                )
            }
            this.experience[slot] = 200000000.0
        }
        if (entity is Player && this.experience[slot] > 175) {
            if (
                !player.getAttribute(GameAttributes.TUTORIAL_COMPLETE, false) && slot != HITPOINTS
            ) {
                this.experience[slot] = 175.0
            }
        }
        experienceGained += experienceAdd
        run(player, slot, experienceAdd)
        val newLevel = getStaticLevelByExperience(slot)
        if (newLevel > staticLevels[slot]) {
            val amount = newLevel - staticLevels[slot]
            if (dynamicLevels[slot] < newLevel) {
                dynamicLevels[slot] += amount
            }
            if (slot == HITPOINTS) {
                lifepoints += amount
            }
            staticLevels[slot] = newLevel

            if (entity is Player) {
                player.updateAppearance()
                levelup(player, slot, amount)
                updateCombatLevel()
            }
        }
        if (entity is Player) {
            PacketRepository.send(SkillLevel::class.java, SkillContext(entity, slot))
            entity.dispatch(XPGainEvent(slot, experienceAdd))
        }
        if (ticks - lastUpdate >= 200) {
            val diffs = ArrayList<Pair<Int, Double>>()
            for (i in this.experience.indices) {
                val diff = this.experience[i] - lastUpdateXp!![i]
                if (diff != 0.0) {
                    diffs.add(Pair(i, diff))
                }
            }
            logXpGains(player, diffs)
            lastUpdateXp = this.experience.clone()
            lastUpdate = ticks
        }
        lastTrainedSkill = slot
        lastXpGain = getWorldTicks()
    }

    private fun getExperienceMod(
        slot: Int,
        experience: Double,
        playerMod: Boolean,
        multiplyer: Boolean
    ): Double {
        return experienceMultiplier
    }

    val highestCombatSkillId: Int
        /**
         * Gets the highest combat skill by level.
         *
         * @return The index of the highest combat skill.
         */
        get() {
            var id = 0
            var last = 0
            for (i in 0..4) {
                if (staticLevels[i] > last) {
                    last = staticLevels[i]
                    id = i
                }
            }
            return id
        }

    /** Restores all skill levels to their base values. */
    fun restore() {
        for (i in 0..23) {
            val staticLevel = getStaticLevel(i)
            setLevel(i, staticLevel)
        }
        if (entity is Player) {
            playAudio(entity.asPlayer(), Sounds.PRAYER_RECHARGE_2674)
        }
        rechargePrayerPoints()
    }

    /**
     * Parses skill data from a byte buffer.
     *
     * @param buffer The byte buffer containing skill data.
     */
    fun parse(buffer: ByteBuffer) {
        for (i in 0..23) {
            experience[i] = (buffer.getInt().toDouble() / 10.0)
            dynamicLevels[i] = buffer.get().toInt() and 0xFF
            if (i == HITPOINTS) {
                lifepoints = dynamicLevels[i]
            } else if (i == PRAYER) {
                prayerPoints = dynamicLevels[i].toDouble()
            }
            staticLevels[i] = buffer.get().toInt() and 0xFF
        }
        experienceGained = buffer.getInt().toDouble()
    }

    /**
     * Parses skill data from a json and updates skill levels and experience.
     *
     * @param skillData The containing skill data.
     */
    fun parse(skillData: JsonArray) {
        for (i in 0 until skillData.size()) {
            val skill = skillData[i].asJsonObject
            val id = skill["id"].asInt
            dynamicLevels[id] = skill["dynamic"].asInt

            if (id == HITPOINTS) {
                lifepoints = dynamicLevels[id]
            } else if (id == PRAYER) {
                prayerPoints = dynamicLevels[id].toDouble()
            }

            staticLevels[id] = skill["static"].asInt
            experience[id] = skill["experience"].asDouble
        }
    }

    /**
     * Corrects experience values by applying a divisor and updating skill levels.
     *
     * @param divisor The value to divide experience points by.
     */
    fun correct(divisor: Double) {
        for (i in staticLevels.indices) {
            experience[i] /= divisor
            staticLevels[i] = getStaticLevelByExperience(i)
            dynamicLevels[i] = staticLevels[i]
            if (i == PRAYER) {
                updatePrayerPoints(staticLevels[i].toDouble())
            }
            if (i == HITPOINTS) {
                updateHitpoints(staticLevels[i])
            }
        }
        experienceMultiplier = 1.0
        updateCombatLevel()
    }

    /**
     * Saves skill data to a ByteBuffer.
     *
     * @param buffer The ByteBuffer to save skill data into.
     */
    fun save(buffer: ByteBuffer) {
        for (i in 0..23) {
            buffer.putInt((experience[i] * 10).toInt())
            if (i == HITPOINTS) {
                buffer.put(lifepoints.toByte())
            } else if (i == PRAYER) {
                buffer.put(ceil(prayerPoints).toInt().toByte())
            } else {
                buffer.put(dynamicLevels[i].toByte())
            }
            buffer.put(staticLevels[i].toByte())
        }
        buffer.putInt(experienceGained.toInt())
    }

    /**
     * Saves experience rate to a ByteBuffer.
     *
     * @param buffer The ByteBuffer to save experience rate into.
     */
    fun saveExpRate(buffer: ByteBuffer) {
        buffer.putDouble(experienceMultiplier)
    }

    /** Refreshes the skill levels for the entity, sending updates to the player. */
    fun refresh() {
        if (entity !is Player) {
            return
        }
        val player = entity
        for (i in 0..23) {
            PacketRepository.send(SkillLevel::class.java, SkillContext(player, i))
        }
        sendFlashingIcons(player, -1)
    }

    /**
     * Calculates the static level based on experience points.
     *
     * @param slot The skill slot.
     * @return The calculated static level.
     */
    private fun getStaticLevelByExperience(slot: Int): Int {
        val exp = experience[slot]

        var points = 0
        var output: Int
        for (lvl in 1..99) {
            points += floor(lvl + 300.0 * 2.0.pow(lvl / 7.0)).toInt()
            output = points / 4
            if (output - 1 >= exp) {
                return lvl
            }
        }
        return 99
    }

    /**
     * Level from XP.
     *
     * @param exp the experience points
     * @return the level
     */
    fun levelFromXP(exp: Double): Int {
        var points = 0
        var output: Int
        for (lvl in 1..99) {
            points += floor(lvl + 300.0 * 2.0.pow(lvl / 7.0)).toInt()
            output = points / 4
            if (output - 1 >= exp) {
                return lvl
            }
        }
        return 99
    }

    /**
     * Gets experience by level.
     *
     * @param level the level
     * @return the experience by level
     */
    fun getExperienceByLevel(level: Int): Int {
        var points = 0
        var output = 0
        for (lvl in 1..level) {
            points += floor(lvl + 300.0 * 2.0.pow(lvl / 7.0)).toInt()
            if (lvl >= level) {
                return output
            }
            output = floor((points / 4).toDouble()).toInt()
        }
        return 0
    }

    /**
     * Update combat level boolean.
     *
     * @return the boolean
     */
    @Suppress("deprecation")
    fun updateCombatLevel(): Boolean {
        val level = calculateCombatLevel()
        val update = level != entity.properties.combatLevel
        if (update) {
            entity.properties.combatLevel = level
        }
        return update
    }

    private fun calculateCombatLevel(): Int {
        if (entity is NPC) {
            return entity.definition.combatLevel
        }

        val attackStrength = staticLevels[ATTACK] + staticLevels[STRENGTH]
        val ranged = staticLevels[RANGE] * 3 / 2
        val magic = staticLevels[MAGIC] * 3 / 2

        var maxCombatStat =
            max(attackStrength.toDouble(), max(ranged.toDouble(), magic.toDouble())).toInt()
        maxCombatStat = (maxCombatStat * 13) / 10

        var baseStats = staticLevels[DEFENCE] + staticLevels[HITPOINTS] + staticLevels[PRAYER] / 2

        if (settings!!.isMembers) {
            baseStats += staticLevels[SUMMONING] / 2
        }

        return (maxCombatStat + baseStats) / 4
    }

    /**
     * Retrieves the experience points for a given skill slot.
     *
     * @param slot The skill slot index.
     * @return The experience points for the given slot.
     */
    fun getExperience(slot: Int): Double {
        return experience[slot]
    }

    /**
     * Retrieves the static level for a given skill slot.
     *
     * @param slot The skill slot index.
     * @return The static level for the given slot.
     */
    fun getStaticLevel(slot: Int): Int {
        return staticLevels[slot]
    }

    /**
     * Sets level.
     *
     * @param slot the slot
     * @param level the level
     */
    fun setLevel(slot: Int, level: Int) {
        if (slot == HITPOINTS) {
            lifepoints = level
        } else if (slot == PRAYER) {
            prayerPoints = level.toDouble()
        }

        val previousLevel = dynamicLevels[slot]
        dynamicLevels[slot] = level

        if (entity is Player) {
            PacketRepository.send(SkillLevel::class.java, SkillContext(entity, slot))
            entity.dispatch(DynamicSkillLevelChangeEvent(slot, previousLevel, level))
        }
    }

    /**
     * Gets level.
     *
     * @param slot the slot
     * @param discardAssist the discard assist
     * @return the level
     */
    fun getLevel(slot: Int, discardAssist: Boolean): Int {
        if (!discardAssist) {
            if (entity is Player) {
                val p = entity
                val assist = p.getExtension<AssistSessionPulse>(AssistSessionPulse::class.java)
                if (assist != null && assist.player !== p) {
                    val assister = assist.player
                    val index = assist.getSkillIndex(slot)
                    if (index != -1 && !assist.isRestricted) {
                        // assist.getSkills()[index] + ", " + SKILL_NAME[slot]);

                        if (assist.skills[index]) {
                            val assistLevel = assister!!.getSkills().getLevel(slot)
                            val playerLevel = dynamicLevels[slot]
                            if (assistLevel > playerLevel) {
                                return assistLevel
                            }
                        }
                    }
                }
            }
        }
        return dynamicLevels[slot]
    }

    /**
     * Gets level.
     *
     * @param slot the slot
     * @return the level
     */
    fun getLevel(slot: Int): Int {
        return getLevel(slot, false)
    }

    fun updateHitpoints(lifepoints: Int) {
        this.lifepoints = lifepoints
        if (this.lifepoints < 0) {
            this.lifepoints = 0
        }
        isLifepointsUpdate = true
    }

    val maximumLifepoints: Int
        /**
         * Gets maximum lifepoints.
         *
         * @return the maximum lifepoints
         */
        get() = staticLevels[HITPOINTS] + lifepointsIncrease

    /**
     * Heals the entity by a given amount, up to the maximum lifepoints.
     *
     * @param health The amount to heal.
     * @return The remaining health after healing, if the maximum is exceeded.
     */
    fun heal(health: Int): Int {
        lifepoints += health
        var left = 0
        if (lifepoints > maximumLifepoints) {
            left = lifepoints - maximumLifepoints
            lifepoints = maximumLifepoints
        }
        isLifepointsUpdate = true
        return left
    }

    /**
     * Heals the entity without any restrictions.
     *
     * @param amount The amount of lifepoints to add.
     */
    fun healNoRestrictions(amount: Int) {
        lifepoints += amount
        isLifepointsUpdate = true
    }

    /**
     * Inflicts damage on the entity.
     *
     * @param damage The amount of damage to apply.
     * @return The excess damage beyond lifepoints.
     */
    fun hit(damage: Int): Int {
        lifepoints -= damage
        var left = 0
        if (lifepoints < 0) {
            left = -lifepoints
            lifepoints = 0
        }
        isLifepointsUpdate = true
        return left
    }

    /** Fully restores the entity's prayer points. */
    fun rechargePrayerPoints() {
        prayerPoints = staticLevels[PRAYER].toDouble()
        if (entity is Player) {
            PacketRepository.send(SkillLevel::class.java, SkillContext(entity, PRAYER))
        }
    }

    /**
     * Decreases the entity's prayer points by a specified amount.
     *
     * @param amount The amount to decrement.
     */
    fun decrementPrayerPoints(amount: Double) {
        prayerPoints -= amount
        if (prayerPoints < 0) {
            prayerPoints = 0.0
        }
        // if (prayerPoints > staticLevels[PRAYER]) {
        // prayerPoints = staticLevels[PRAYER];
        // }
        if (entity is Player) {
            PacketRepository.send(SkillLevel::class.java, SkillContext(entity, PRAYER))
        }
    }

    /**
     * Increases the entity's prayer points by a specified amount.
     *
     * @param amount The amount to increment.
     */
    fun incrementPrayerPoints(amount: Double) {
        prayerPoints += amount
        if (prayerPoints < 0) {
            prayerPoints = 0.0
        }
        if (prayerPoints > staticLevels[PRAYER]) {
            prayerPoints = staticLevels[PRAYER].toDouble()
        }
        if (entity is Player) {
            PacketRepository.send(SkillLevel::class.java, SkillContext(entity, PRAYER))
        }
    }

    fun updatePrayerPoints(amount: Double) {
        prayerPoints = amount
        if (entity is Player) {
            PacketRepository.send(SkillLevel::class.java, SkillContext(entity, PRAYER))
        }
    }

    /**
     * Updates the entity's skill level.
     *
     * @param skill The skill ID.
     * @param amount The amount to update.
     * @param maximum The maximum allowable level.
     * @return The remaining value after adjustment.
     */
    @JvmOverloads
    fun updateLevel(
        skill: Int,
        amount: Int,
        maximum: Int =
            if (amount >= 0) getStaticLevel(skill) + amount else getStaticLevel(skill) - amount
    ): Int {
        if (amount > 0 && dynamicLevels[skill] > maximum) {
            return -amount
        }
        val left = (dynamicLevels[skill] + amount) - maximum
        dynamicLevels[skill] += amount
        val level = dynamicLevels[skill]
        if (level < 0) {
            dynamicLevels[skill] = 0
        } else if (amount < 0 && level < maximum) {
            dynamicLevels[skill] = maximum
        } else if (amount > 0 && level > maximum) {
            dynamicLevels[skill] = maximum
        }
        if (entity is Player) {
            PacketRepository.send(SkillLevel::class.java, SkillContext(entity, skill))
        }
        return left
    }

    /**
     * Drains a skill level by a percentage.
     *
     * @param skill The skill ID.
     * @param drainPercentage The percentage to drain.
     * @param maximumDrainPercentage The maximum allowable drain percentage.
     */
    fun drainLevel(skill: Int, drainPercentage: Double, maximumDrainPercentage: Double) {
        val drain = (dynamicLevels[skill] * drainPercentage).toInt()
        val minimum = (staticLevels[skill] * (1.0 - maximumDrainPercentage)).toInt()
        updateLevel(skill, -drain, minimum)
    }

    /**
     * Sets a skill's static level and updates experience accordingly.
     *
     * @param skill The skill ID.
     * @param level The new level to set.
     */
    fun setStaticLevel(skill: Int, level: Int) {
        experience[skill] =
            getExperienceByLevel(
                level.also { dynamicLevels[skill] = it }.also { staticLevels[skill] = it }
            )
                .toDouble()
        if (entity is Player) {
            PacketRepository.send(SkillLevel::class.java, SkillContext(entity, skill))
        }
    }

    val masteredSkills: Int
        /**
         * Gets mastered skills.
         *
         * @return the mastered skills
         */
        get() {
            var count = 0
            for (i in 0..22) {
                if (getStaticLevel(i) >= 99) {
                    count++
                }
            }
            return count
        }

    val totalLevel: Int
        /**
         * Gets total level.
         *
         * @return the total level
         */
        get() {
            var level = 0
            for (i in 0..23) {
                level += getStaticLevel(i)
            }
            return level
        }

    val totalXp: Int
        /**
         * Gets total xp.
         *
         * @return the total xp
         */
        get() {
            var total = 0
            for (skill in SKILL_NAME.indices) {
                total = (total + this.getExperience(skill)).toInt()
            }
            return total
        }

    /**
     * Checks if the player has the required level.
     *
     * @param skillId the skill id.
     * @param i the level.
     * @return `True` if so.
     */
    fun hasLevel(skillId: Int, i: Int): Boolean {
        return getStaticLevel(skillId) >= i
    }

    companion object {
        /** Array of skill names. */
        @JvmField
        val SKILL_NAME: Array<String> =
            arrayOf(
                "Attack",
                "Defence",
                "Strength",
                "Hitpoints",
                "Ranged",
                "Prayer",
                "Magic",
                "Cooking",
                "Woodcutting",
                "Fletching",
                "Fishing",
                "Firemaking",
                "Crafting",
                "Smithing",
                "Mining",
                "Herblore",
                "Agility",
                "Thieving",
                "Slayer",
                "Farming",
                "Runecrafting",
                "Hunter",
                "Construction",
                "Summoning"
            )

        /** Skill index constants. */
        const val ATTACK: Int = 0
        const val DEFENCE: Int = 1
        const val STRENGTH: Int = 2
        const val HITPOINTS: Int = 3
        const val RANGE: Int = 4
        const val PRAYER: Int = 5
        const val MAGIC: Int = 6
        const val COOKING: Int = 7
        const val WOODCUTTING: Int = 8
        const val FLETCHING: Int = 9
        const val FISHING: Int = 10
        const val FIREMAKING: Int = 11
        const val CRAFTING: Int = 12
        const val SMITHING: Int = 13
        const val MINING: Int = 14
        const val HERBLORE: Int = 15
        const val AGILITY: Int = 16
        const val THIEVING: Int = 17
        const val SLAYER: Int = 18
        const val FARMING: Int = 19
        const val RUNECRAFTING: Int = 20
        const val HUNTER: Int = 21
        const val CONSTRUCTION: Int = 22
        const val SUMMONING: Int = 23

        /** Total number of skills. */
        const val NUM_SKILLS: Int = 24

        /**
         * Gets skill by name.
         *
         * @param name the name
         * @return the skill by name
         */
        fun getSkillByName(name: String?): Int {
            for (i in SKILL_NAME.indices) {
                if (SKILL_NAME[i].equals(name, ignoreCase = true)) {
                    return i
                }
            }
            return -1
        }
    }
}
