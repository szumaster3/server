package content.global.skill.runecrafting

import content.global.plugins.item.equipment.gloves.FOGGlovesListener.Companion.updateCharges
import content.global.skill.runecrafting.Talisman.Companion.forName
import core.ServerConstants
import core.api.*
import core.game.container.impl.EquipmentContainer
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.command.sets.STATS_BASE
import core.game.system.command.sets.STATS_RC
import core.game.world.GameWorld.ticks
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Sounds
import kotlin.math.max

/**
 * Handles the process of Runecrafting at altars.
 */
class RunecraftPulse(
    player: Player?,
    node: Item?,
    val altar: Altar,
    private val combination: Boolean,
    private val combo: CombinationRune?,
) : SkillPulse<Item?>(player, node) {
    private val rune: Rune
    private var talisman: Talisman? = null

    init {
        this.rune = altar.rune!!
        this.resetAnimation = false
    }

    override fun checkRequirements(): Boolean {
        if (altar == Altar.ASTRAL) {
            if (!hasRequirement(player, Quests.LUNAR_DIPLOMACY)) return false
        }
        if (altar == Altar.DEATH) {
            if (!hasRequirement(player, Quests.MOURNINGS_END_PART_II)) return false
        }
        if (altar == Altar.BLOOD) {
            if (!hasRequirement(player, Quests.LEGACY_OF_SEERGAZE)) return false
        }
        if (altar.isOurania() && !inInventory(player, PURE_ESSENCE)) {
            sendMessage(player, "You need pure essence to craft this rune.")
            return false
        }
        if (!altar.isOurania() && !rune.isNormal && !inInventory(player, PURE_ESSENCE)) {
            sendMessage(player, "You need pure essence to craft this rune.")
            return false
        }
        if (!altar.isOurania() && getStatLevel(player, Skills.RUNECRAFTING) < rune.level) {
            sendMessage(player, "You need a runecrafting level of at least " + rune.level + " to craft this rune.")
            return false
        }
        if (!altar.isOurania() && rune.isNormal && !inInventory(player, PURE_ESSENCE) && !inInventory(
                player, RUNE_ESSENCE
            )
        ) {
            sendMessage(player, "You need rune essence or pure essence in order to craft this rune.")
            return false
        }
        if (combination && !inInventory(player, PURE_ESSENCE)) {
            sendMessage(player, "You need pure essence to craft this rune.")
            return false
        }
        if (combination && getStatLevel(player, Skills.RUNECRAFTING) < combo!!.level) {
            sendMessage(player, "You need a runecrafting level of at least " + combo.level + " to combine this rune.")
            return false
        }
        if (node != null) {
            if (node!!.name.contains("rune") && !hasSpellImbue()) {
                val r = Rune.forItem(node!!)
                val t = Talisman.forName(r!!.name)
                if (!inInventory(player, t!!.item)) {
                    sendMessage(player, "You don't have the correct talisman to combine this rune.")
                    return false
                }
                talisman = t
            }
        }
        lock(player, 4)
        return true
    }

    override fun animate() {
        visualize(player, ANIMATION, Graphics)
        playAudio(player, Sounds.BIND_RUNES_2710)
    }

    override fun reward(): Boolean {
        if (!combination) {
            craft()
        } else {
            combine()
        }
        return true
    }

    override fun message(type: Int) {
        when (type) {
            1 -> if (altar == Altar.OURANIA) {
                sendMessage(player, "You bind the temple's power into runes.")
            } else {
                sendMessage(
                    player,
                    "You bind the temple's power into " + (if (combination) getItemName(combo!!.rune) else rune.rune.name.lowercase() + "s."),
                )
            }
        }
    }

    /**
     * Handles craft the standard runes at an altar.
     */
    private fun craft() {
        val item = Item(essence.id, essenceAmount)
        val amount = player.inventory.getAmount(item)

        if (!altar.isOurania()) {
            val hasExplorerRing = inEquipment(player, Items.EXPLORERS_RING_2_13561, 1)
            var total = 0

            for (j in 0 until amount) {
                total += multiplier

                /*
                 * 10% chance of crafting an additional runes with explorer's ring 2.
                 */
                if (hasExplorerRing && rune in listOf(Rune.AIR, Rune.EARTH, Rune.FIRE, Rune.WATER)) {
                    if (RandomFunction.getRandom(9) == 0) {
                        total += 1
                        rewardXP(player, Skills.RUNECRAFTING, rune.experience)
                    }
                }
            }

            val craftedRunes = Item(rune.rune.id, total)
            if (removeItem(player, item) && hasSpaceFor(player, craftedRunes)) {
                addItem(player, craftedRunes.id, craftedRunes.amount)
                player.incrementAttribute("/save:$STATS_BASE:$STATS_RC", amount)
                sendMessage(
                    player,
                    "You bind the temple's power into " + (if (combination) getItemName(combo!!.rune) else rune.rune.name.lowercase()) + "s."
                )

                var xp = rune.experience * amount
                if ((altar == Altar.AIR && inEquipment(player, Items.AIR_RUNECRAFTING_GLOVES_12863, 1)) ||
                    (altar == Altar.WATER && inEquipment(player, Items.WATER_RUNECRAFTING_GLOVES_12864, 1)) ||
                    (altar == Altar.EARTH && inEquipment(player, Items.EARTH_RUNECRAFTING_GLOVES_12865, 1))
                ) {
                    xp += xp * updateCharges(player, amount) / amount
                }
                rewardXP(player, Skills.RUNECRAFTING, xp)

                // Diary checks
                if (altar == Altar.NATURE) finishDiaryTask(player, DiaryType.KARAMJA, 2, 3)
                if (altar == Altar.AIR && craftedRunes.amount >= 196) finishDiaryTask(player, DiaryType.FALADOR, 2, 2)
                if (altar == Altar.WATER && rune == Rune.WATER) finishDiaryTask(player, DiaryType.LUMBRIDGE, 1, 11)
            }
        } else {
            if (removeItem(player, item)) {
                sendMessage(player, "You bind the temple's power into runes.")
                for (i in 0 until amount) {
                    var rune: Rune? = null
                    while (rune == null) {
                        val temp = Rune.values()[RandomFunction.random(Rune.values().size)]
                        if (getStatLevel(player, Skills.RUNECRAFTING) >= temp.level) {
                            rune = temp
                        } else if (RandomFunction.random(3) == 1) {
                            rune = temp
                        }
                    }
                    rewardXP(player, Skills.RUNECRAFTING, rune.experience * 2)
                    addItem(player, rune.rune.id)
                }
            }
        }
    }

    /**
     * Handles craft the combination runes.
     */
    private fun combine() {
        val remove = if (node!!.name.contains("talisman")) {
            node!!
        } else if (talisman != null) {
            talisman!!.item
        } else {
            forName(Rune.forItem(node!!)!!.name)!!.item
        }
        val imbued = hasSpellImbue()
        if (if (!imbued) removeItem(player, remove) else imbued) {
            var amount = 0
            val essenceAmt = player.inventory.getAmount(PURE_ESSENCE)
            val rune = if (node!!.name.contains("rune")) {
                Rune.forItem(node!!)!!.rune
            } else {
                Rune.forName(Talisman.forItem(node!!)!!.name)!!.rune
            }
            val runeAmt = player.inventory.getAmount(rune)
            amount = if (essenceAmt > runeAmt) {
                runeAmt
            } else {
                essenceAmt
            }
            if (player.inventory.remove(Item(PURE_ESSENCE, amount)) && player.inventory.remove(Item(rune.id, amount))) {
                for (i in 0 until amount) {
                    if (RandomFunction.random(1, 3) == 1 || hasBindingNecklace()) {
                        player.inventory.add(Item(combo!!.rune, 1))
                        player.getSkills().addExperience(Skills.RUNECRAFTING, combo.experience, true)
                    }
                }
                if (hasBindingNecklace()) {
                    player.equipment[EquipmentContainer.SLOT_AMULET].charge -= 1
                    if (1000 - player.equipment[EquipmentContainer.SLOT_AMULET].charge > 14) {
                        player.equipment.remove(Item(Items.BINDING_NECKLACE_5521), true)
                        player.packetDispatch.sendMessage("Your binding necklace crumbles into dust.")
                    }
                }
            }
        }
    }

    /**
     * @return true if the player currently has the spell Imbue active.
     */
    private fun hasSpellImbue(): Boolean = player.getAttribute("spell:imbue", 0) > ticks

    /**
     * Determines the total number of essence that can be used,
     * depending on altar type and rune type.
     */
    private val essenceAmount: Int
        get() {
            if (altar.isOurania() && inInventory(player, Items.PURE_ESSENCE_7936)) {
                return amountInInventory(player, Items.PURE_ESSENCE_7936)
            }
            return if (!rune.isNormal && inInventory(player, Items.PURE_ESSENCE_7936)) {
                amountInInventory(player, Items.PURE_ESSENCE_7936)
            } else if (rune.isNormal && inInventory(player, Items.PURE_ESSENCE_7936)) {
                amountInInventory(player, Items.PURE_ESSENCE_7936)
            } else {
                amountInInventory(player, Items.RUNE_ESSENCE_1436)
            }
        }

    /**
     * Determines which type of essence item will be consumed,
     * depending on altar type and rune type.
     */
    private val essence: Item
        get() {
            if (altar.isOurania() && inInventory(player, Items.PURE_ESSENCE_7936)) {
                return Item(PURE_ESSENCE)
            }
            return if (!rune.isNormal && inInventory(player, Items.PURE_ESSENCE_7936)) {
                Item(PURE_ESSENCE)
            } else if (rune.isNormal && inInventory(player, Items.PURE_ESSENCE_7936)) {
                Item(PURE_ESSENCE)
            } else {
                Item(RUNE_ESSENCE)
            }
        }

    /**
     * Multiplier for runes created per essence at the altar.
     *
     * @see getMultiplier
     */
    val multiplier: Int
        get() {
            if (altar.isOurania()) {
                return 1
            }
            val rcLevel = getStatLevel(player, Skills.RUNECRAFTING)
            val runecraftingFormulaRevision = ServerConstants.RUNECRAFTING_FORMULA_REVISION
            return getMultiplier(rcLevel, rune, runecraftingFormulaRevision)
        }

    /**
     * @return true if the player has a Binding Necklace equipped.
     */
    private fun hasBindingNecklace(): Boolean = inEquipment(player, BINDING_NECKLACE)

    companion object {
        private const val RUNE_ESSENCE = Items.RUNE_ESSENCE_1436
        private const val PURE_ESSENCE = Items.PURE_ESSENCE_7936
        private const val BINDING_NECKLACE = Items.BINDING_NECKLACE_5521
        private val ANIMATION = Animation(Animations.OLD_RUNECRAFT_791, Priority.HIGH)
        private val Graphics = Graphics(shared.consts.Graphics.RUNECRAFTING_GRAPHIC_186, 100)

        /**
         * Calculates the multiplier of runes created per essence,
         * based on RC level and the altars rune type.
         */
        fun getMultiplier(rcLevel: Int, rune: Rune, rcFormulaRevision: Int): Int {
            val multipleLevels = rune.getMultiple() ?: return 1
            var i = 0

            for (level in multipleLevels) {
                if (rcLevel >= level) {
                    i++
                }
            }

            if (multipleLevels.size > i && rcFormulaRevision >= 573) {
                val a = max(multipleLevels[i - 1].toDouble(), rune.level.toDouble()).toInt()
                val b = multipleLevels[i]
                if (b <= 99 || rcFormulaRevision >= 581) {
                    val chance = (rcLevel - a).toDouble() / (b - a).toDouble()
                    if (RandomFunction.random(0.0, 1.0) < chance) {
                        i++
                    }
                }
            }

            return if (i > 0) i else 1
        }
    }
}
