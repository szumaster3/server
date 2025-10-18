package content.minigame.pestcontrol.plugin

import content.global.skill.herblore.herbs.HerbItem
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import java.util.*
import shared.consts.Components
import shared.consts.Items

@Initializable
class PCRewardInterface : ComponentPlugin() {

    override fun newInstance(arg: Any?): Plugin<Any?> {
        ComponentDefinition.forId(Components.PEST_REWARDS_267).plugin = this
        return this
    }

    override fun handle(
        player: Player,
        component: Component,
        opcode: Int,
        button: Int,
        slot: Int,
        itemId: Int
    ): Boolean {
        when (button) {
            96 -> confirm(player)
            in 34..86 -> {
                if (player.getSavedData().activityData.pestPoints == 0) {
                    sendMessage(player, "You don't have enough points.")
                } else {
                    select(player, button)
                }
            }
        }
        return true
    }

    private fun select(player: Player, button: Int) {
        val reward = Reward.forButton(button) ?: return
        val optionIndex = reward.getOptionIndex(button).takeIf { it >= 0 } ?: return
        if (!reward.checkRequirements(player, optionIndex)) return
        cacheReward(player, reward, optionIndex)
    }

    private fun deselect(player: Player, reward: Reward? = getReward(player)): Boolean {
        val r = reward ?: return false
        clear(player)
        r.deselect(player, getCachedOption(player))
        removeAttribute(player, "pc-reward")
        removeAttribute(player, "pc-reward:option")
        return true
    }

    private fun cacheReward(player: Player, reward: Reward, optionIndex: Int) {
        deselect(player)
        reward.select(player, optionIndex)
        sendString(player, "<col=F7DF22>Confirm:", 106)
        sendString(player, reward.text, 104)
        setAttribute(player, "pc-reward", reward)
        setAttribute(player, "pc-reward:option", optionIndex)
    }

    private fun confirm(player: Player) {
        val reward = getReward(player)
        if (reward == null) {
            sendMessage(player, "Please choose a reward first.")
            return
        }

        val option = getCachedOption(player)
        if (option < 0) {
            sendMessage(player, "Please choose a reward first.")
            return
        }

        val points = reward.getPoints(option)
        val ttlpoints = player.getSavedData().activityData.pestPoints

        if (ttlpoints < points) {
            sendMessage(player, "You don't have enough commendation points.")
            return
        }

        if (!reward.isSkillReward && freeSlots(player) < 1) {
            sendDialogue(player, "You need at least one free inventory slot.")
            return
        }

        lock(player, 1)
        player.getSavedData().activityData.decreasePestPoints(points)
        if (reward.isSkillReward && reward.skill != null) {
            val xp = calculateExperience(player, reward.skill, points)
            rewardXP(player, reward.skill, xp.toDouble())
            sendDialogueLines(
                player,
                "The Void Knight has granted you $xp ${Skills.SKILL_NAME[reward.skill]} experience.",
                "<col=571D07>Remaining points: ${player.getSavedData().activityData.pestPoints}"
            )
        } else {
            if (!reward.checkItemRequirement(player, option)) return
            reward.giveToPlayer(player, option)
            sendDialogueLines(
                player,
                "The Void Knight has given you ${reward.text}.",
                "<col=571D07>Remaining points: ${player.getSavedData().activityData.pestPoints}"
            )
        }

        removeAttribute(player, "pc-reward")
        removeAttribute(player, "pc-reward:option")
        clear(player)
    }

    enum class Reward(
        val skill: Int? = null,
        val basePoints: Int = 0,
        private val items: IntArray? = intArrayOf(),
        val childs: IntArray = intArrayOf(),
        private val charm: Boolean = false,
        private val customName: String? = null
    ) {
        ATTACK(Skills.ATTACK, 0, null, intArrayOf(10, 34, 49, 56), false),
        STRENGTH(Skills.STRENGTH, 0, null, intArrayOf(11, 35, 50, 57), false),
        DEFENCE(Skills.DEFENCE, 0, null, intArrayOf(12, 36, 51, 58), false),
        RANGE(Skills.RANGE, 0, null, intArrayOf(13, 37, 52, 59), false),
        MAGIC(Skills.MAGIC, 0, null, intArrayOf(14, 38, 53, 60), false),
        HITPOINTS(Skills.HITPOINTS, 0, null, intArrayOf(15, 39, 54, 61), false),
        PRAYER(Skills.PRAYER, 0, null, intArrayOf(16, 40, 55, 62), false),
        HERB_PACK(
            null,
            30,
            intArrayOf(
                HerbItem.HARRALANDER.herb.id,
                HerbItem.RANARR.herb.id,
                HerbItem.TOADFLAX.herb.id,
                HerbItem.IRIT.herb.id,
                HerbItem.AVANTOE.herb.id,
                HerbItem.KWUARM.herb.id,
                HerbItem.GUAM.herb.id,
                HerbItem.MARRENTILL.herb.id
            ),
            intArrayOf(32, 45),
            false,
            "Herb Pack"
        ) {
            override fun checkItemRequirement(player: Player, optionIndex: Int): Boolean {
                if (getStatLevel(player, Skills.HERBLORE) < 25) {
                    sendMessage(player, "You need level 25 herblore to purchase this pack.")
                    return false
                }
                return true
            }
        },
        MINERAL_PACK(
            null,
            15,
            intArrayOf(Items.COAL_453, Items.IRON_ORE_440),
            intArrayOf(47, 46),
            false,
            "Mineral Pack"
        ) {
            override fun checkItemRequirement(player: Player, optionIndex: Int): Boolean {
                if (getStatLevel(player, Skills.MINING) < 25) {
                    sendMessage(player, "You need level 25 mining to purchase this pack.")
                    return false
                }
                return true
            }
        },
        SEED_PACK(
            null,
            15,
            intArrayOf(Items.SWEETCORN_SEED_5320, Items.TOMATO_SEED_5322, Items.LIMPWURT_SEED_5100),
            intArrayOf(33, 48),
            false,
            "Seed Pack"
        ) {
            override fun checkItemRequirement(player: Player, optionIndex: Int): Boolean {
                if (getStatLevel(player, Skills.FARMING) < 25) {
                    sendMessage(player, "You need level 25 farming to purchase this pack.")
                    return false
                }
                return true
            }
        },
        VOID_MACE(
            null,
            250,
            intArrayOf(Items.VOID_KNIGHT_MACE_8841),
            intArrayOf(28, 41),
            false,
            "Void Knight Mace"
        ) {
            override fun checkItemRequirement(player: Player, optionIndex: Int) =
                hasVoidSkills(player)
        },
        VOID_TOP(
            null,
            250,
            intArrayOf(Items.VOID_KNIGHT_TOP_8839),
            intArrayOf(29, 42),
            false,
            "Void Knight Top"
        ) {
            override fun checkItemRequirement(player: Player, optionIndex: Int) =
                hasVoidSkills(player)
        },
        VOID_ROBES(
            null,
            250,
            intArrayOf(Items.VOID_KNIGHT_ROBE_8840),
            intArrayOf(30, 43),
            false,
            "Void Knight Robes"
        ) {
            override fun checkItemRequirement(player: Player, optionIndex: Int) =
                hasVoidSkills(player)
        },
        VOID_GLOVES(
            null,
            150,
            intArrayOf(Items.VOID_KNIGHT_GLOVES_8842),
            intArrayOf(31, 44),
            false,
            "Void Knight Gloves"
        ) {
            override fun checkItemRequirement(player: Player, optionIndex: Int) =
                hasVoidSkills(player)
        },
        VOID_MAGE_HELM(
            null,
            200,
            intArrayOf(Items.VOID_MAGE_HELM_11663),
            intArrayOf(63, 67),
            false,
            "Void Knight Mage Helm"
        ) {
            override fun checkItemRequirement(player: Player, optionIndex: Int) =
                hasVoidSkills(player)
        },
        VOID_RANGER_HELM(
            null,
            200,
            intArrayOf(Items.VOID_RANGER_HELM_11664),
            intArrayOf(64, 68),
            false,
            "Void Knight Ranger Helm"
        ) {
            override fun checkItemRequirement(player: Player, optionIndex: Int) =
                hasVoidSkills(player)
        },
        VOID_MELEE_HELM(
            null,
            200,
            intArrayOf(Items.VOID_MELEE_HELM_11665),
            intArrayOf(65, 69),
            false,
            "Void Knight Melee Helm"
        ) {
            override fun checkItemRequirement(player: Player, optionIndex: Int) =
                hasVoidSkills(player)
        },
        VOID_KNIGHT_SEAL(
            null,
            10,
            intArrayOf(Items.VOID_SEAL8_11666),
            intArrayOf(66, 70),
            false,
            "Void Knight Seal"
        ),
        SPINNER_CHARM(
            null,
            0,
            intArrayOf(Items.SPINNER_CHARM_12166),
            intArrayOf(71, 75, 76, 77),
            true,
            "Spinner Charm"
        ),
        RAVAGER_CHARM(
            null,
            0,
            intArrayOf(Items.RAVAGER_CHARM_12164),
            intArrayOf(72, 81, 82, 83),
            true,
            "Ravager Charm"
        ),
        TORCHER_CHARM(
            null,
            0,
            intArrayOf(Items.TORCHER_CHARM_12167),
            intArrayOf(74, 78, 79, 80),
            true,
            "Torcher Charm"
        ),
        SHIFTER_CHAR(
            null,
            0,
            intArrayOf(Items.SHIFTER_CHARM_12165),
            intArrayOf(73, 84, 85, 86),
            true,
            "Shifter Charm"
        );

        companion object {
            private const val MAX_BUILD = 18
            private const val MIN_BUILD = 13

            fun forButton(button: Int): Reward? {
                return values().firstOrNull { reward -> reward.childs.any { it == button } }
            }
        }

        val isCharm: Boolean
            get() = charm

        val isSkillReward: Boolean
            get() = skill != null && childs.size > 2

        val header: Int
            get() = childs[0]

        val text: String
            get() = customName ?: (skill?.let { Skills.SKILL_NAME[it] + " xp" } ?: "")

        fun getOptionIndex(button: Int): Int {
            return childs.indexOf(button)
        }

        fun getPoints(optionIndex: Int): Int {
            return when {
                isCharm -> CHARM_POINTS.getOrNull(optionIndex) ?: Int.MAX_VALUE
                isSkillReward -> SKILL_POINTS.getOrNull(optionIndex) ?: Int.MAX_VALUE
                else -> basePoints
            }
        }

        fun checkRequirements(player: Player, optionIndex: Int): Boolean {
            val required = getPoints(optionIndex)
            if (player.getSavedData().activityData.pestPoints < required) {
                sendMessage(player, "You don't have enough points.")
                return false
            }
            return if (isSkillReward) checkSkillRequirement(player, optionIndex)
            else checkItemRequirement(player, optionIndex)
        }

        private fun checkSkillRequirement(player: Player, option: Int): Boolean {
            if (getStatLevel(player, skill!!) < 25) {
                sendMessages(
                    player,
                    "The Void Knights will not offer training in skills which you have a level of",
                    "less than 25."
                )
                return false
            }
            return true
        }

        fun select(player: Player, optionIndex: Int) {
            if (isSkillReward) {
                val skillId = skill ?: return
                val skillName = Skills.SKILL_NAME[skillId]
                val xpAmount = calculateExperience(player, skillId, 1)

                sendString(player, "${core.tools.WHITE}$skillName - $xpAmount xp", header)
                sendString(player, "${core.tools.WHITE}${getOptionString(optionIndex)}", childs[optionIndex])
                return
            }
            sendString(player, "${core.tools.WHITE}$text", header)
            if (isCharm) {
                sendString(player, "${core.tools.WHITE}${getOptionString(optionIndex)}", childs[optionIndex])
            }
        }

        fun deselect(player: Player, optionIndex: Int) {
            if (isSkillReward || isCharm) {
                sendString(player, "<col=784F1C>${getOptionString(optionIndex)}", childs[optionIndex])
            }
        }

        private fun getOptionString(optionIndex: Int): String {
            val points = if (isCharm) {
                arrayOf("(2 Pts)", "(28 Pts)", "(56 Pts)")
            } else {
                arrayOf("(1 Pt)", "(10 Pts)", "(100 Pts)")
            }
            return points.getOrElse(optionIndex) { "(?)" }
        }

        open fun checkItemRequirement(player: Player, optionIndex: Int): Boolean = true

        fun giveToPlayer(player: Player, optionIndex: Int) {
            if (isCharm) {
                val charmItem = items?.first()
                val amount = CHARM_AMOUNTS.getOrNull(optionIndex) ?: 0
                repeat(amount) {
                    if (!player.inventory.add(charmItem?.let { it1 -> Item(it1) })) {
                        GroundItemManager.create(charmItem?.let { it1 -> Item(it1) }, player)
                    }
                }
                return
            }

            if (items!!.isEmpty()) return

            if (items.size > 1) {
                val pack = constructPack()
                for (i in pack) {
                    if (!player.inventory.add(i)) {
                        GroundItemManager.create(i, player)
                    }
                }
            } else {
                if (!player.inventory.add(Item(items[0]))) {
                    GroundItemManager.create(Item(items[0]), player)
                }
            }
        }

        private fun constructPack(): Array<Item> {
            val build = if (this == SEED_PACK || this == HERB_PACK)
                RandomFunction.random(MIN_BUILD, MAX_BUILD)
            else
                RandomFunction.random(38, 43)

            var left = build
            val pack = ArrayList<Item>(20)

            for (i in items!!.indices) {
                val itemId = items[i]
                var amt = if (this == SEED_PACK || this == HERB_PACK)
                    RandomFunction.random(1, 5)
                else
                    RandomFunction.random(16, 25)

                if (amt > left) amt = left
                if (amt < 1) continue

                val itemToAdd =
                    if (this != SEED_PACK)
                        Item(ItemDefinition.forId(itemId).noteId, amt)
                    else
                        Item(itemId, amt)

                pack.add(itemToAdd)
                left -= amt
                if (left <= 0) break
            }

            return pack.toTypedArray()
        }

        fun hasVoidSkills(player: Player): Boolean {
            for (skillId in SKILL_ARRAY) {
                val required = if (skillId != Skills.PRAYER) 42 else 22
                if (getStatLevel(player, skillId) < required) {
                    sendMessage(
                        player,
                        "You need level 42 in hitpoints, attack, defence, strength, ranged, magic and 22 prayer to purchase this item."
                    )
                    return false
                }
            }
            return true
        }
    }

    companion object {
        const val GREEN = "<col=04B404>"
        private val SKILL_HEADER = intArrayOf(10, 12, 11, 15, 13, 16, 14)
        private val SKILL_ARRAY =
            intArrayOf(
                Skills.ATTACK,
                Skills.STRENGTH,
                Skills.DEFENCE,
                Skills.RANGE,
                Skills.MAGIC,
                Skills.HITPOINTS,
                Skills.PRAYER
            )
        private val SKILL_POINTS = intArrayOf(1, 10, 100)
        private val CHARM_POINTS = intArrayOf(2, 28, 56)
        private val CHARM_AMOUNTS = intArrayOf(1, 14, 28)

        fun open(player: Player) {
            removeAttribute(player, "pc-reward")
            removeAttribute(player, "pc-reward:option")

            sendString(player, "Points: ${player.getSavedData().activityData.pestPoints}", 105)
            clear(player)
            openInterface(player, Components.PEST_REWARDS_267)
        }

        private fun sendSkills(player: Player) {
            for ((idx, skill) in SKILL_ARRAY.withIndex()) {
                val header = SKILL_HEADER.getOrNull(idx) ?: SKILL_HEADER.getOrNull(0)!!
                val text =
                    if (getStatLevel(player, skill) < 25)
                        core.tools.RED + "Must reach level 25 first."
                    else
                        GREEN +
                                Skills.SKILL_NAME[skill] +
                                " - " +
                                calculateExperience(player, skill, 1) +
                                " xp"
                sendString(player, text, header)
            }
        }

        private fun sendString(player: Player, string: String, child: Int) {
            if (player.isActive && player.interfaceManager.opened?.id == Components.PEST_REWARDS_267)
            player.packetDispatch.sendString(string, Components.PEST_REWARDS_267, child)
        }

        fun clear(player: Player) {
            sendSkills(player)
            for (reward in Reward.values()) {
                if (reward.isSkillReward) continue
                if (reward.isCharm) {
                    sendString(
                        player,
                        if (player.getSavedData().activityData.pestPoints < 2)
                            core.tools.RED + "You need 2 points."
                        else GREEN + reward.text,
                        reward.header
                    )
                } else {
                    val pts = reward.basePoints
                    val text =
                        when {
                            player.getSavedData().activityData.pestPoints < pts && pts > 1 ->
                                core.tools.RED + "You need $pts points."
                            player.getSavedData().activityData.pestPoints < 1 && pts <= 1 ->
                                core.tools.RED + "You need at least 1 point."
                            else -> GREEN + reward.text
                        }
                    sendString(player, text, reward.header)
                }
            }
        }

        fun calculateExperience(player: Player, skillId: Int, points: Int): Int {
            val level = getStatLevel(player, skillId)
            val N =
                when (skillId) {
                    Skills.PRAYER -> 18
                    Skills.MAGIC,
                    Skills.RANGE -> 32
                    Skills.ATTACK,
                    Skills.STRENGTH,
                    Skills.DEFENCE,
                    Skills.HITPOINTS -> 35
                    else -> 35
                }
            val xpPerPoint = ((level.toLong() * level.toLong()) / 600).toInt() * N
            val bonus =
                when {
                    points >= 100 -> 1.1
                    points >= 10 -> 1.01
                    else -> 1.0
                }
            return (points * xpPerPoint * bonus).toInt()
        }

        fun getReward(player: Player): Reward? {
            return player.getAttribute("pc-reward", null)
        }

        fun getCachedOption(player: Player): Int {
            return player.getAttribute("pc-reward:option", -1)
        }
    }
}
