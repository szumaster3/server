package content.minigame.stealingcreation.handlers

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import java.util.*

object StealingCreation {
    private val redTeam = ArrayList<Player>()
    private val blueTeam = ArrayList<Player>()
    private var lobbyTask: Timer? = null
    private var gameTask: Timer? = null
    val sacredClayItem = intArrayOf(14182, 14184, 14186, 14188, 14190)
    val classItems =
        intArrayOf(
            14132,
            14122,
            14142,
            14152,
            14172,
            14162,
            14367,
            14357,
            14347,
            14411,
            14391,
            14401,
            14337,
            14317,
            14327,
            14297,
            14287,
            14307,
            14192,
            14202,
            12850,
            12851,
            14422,
            14377,
            14421,
            -1,
            -1,
            14215,
            14225,
            14235,
            14245,
            14255,
            14265,
            14275,
            14285,
        )
    val lobbyLocation = Location(2968, 9701, 0)
    val waitingArea = Location.create(1922, 5698, 0)
    val gatherClay = Animation.create(10602)
    private val SkillIds =
        intArrayOf(
            Skills.WOODCUTTING,
            Skills.MINING,
            Skills.FISHING,
            Skills.HUNTER,
            Skills.COOKING,
            Skills.HERBLORE,
            Skills.CRAFTING,
            Skills.SMITHING,
            Skills.FLETCHING,
            Skills.RUNECRAFTING,
            Skills.CONSTRUCTION,
        )
    private val CombatSkillIds =
        intArrayOf(
            Skills.ATTACK,
            Skills.STRENGTH,
            Skills.DEFENCE,
            Skills.HITPOINTS,
            Skills.RANGE,
            Skills.MAGIC,
            Skills.PRAYER,
            Skills.SUMMONING,
        )
    private val basicAnimation = intArrayOf(10603, 10608, 10613, 10618)

    private class LobbyTimer(
        private var minutes: Int = 0,
    ) : TimerTask() {
        override fun run() {
            if (!hasRequiredPlayers()) {
                cancel()
                return
            } else if (minutes++ == 2) {
                passToGame()
                cancel()
                return
            }
        }
    }

    fun enterTeamLobby(
        player: Player,
        inRedTeam: Boolean,
    ) {
        if (!canEnter(player, inRedTeam)) {
            return
        } else if (!hasRequiredPlayers()) {
        }
    }

    fun passToGame() {
    }

    fun handleKiln(
        player: Player,
        componentId: Int,
        index: Int,
        itemId: Int,
        amount: Int,
    ): Boolean {
        val clayId: Int = sacredClayItem[index]
        if (inInventory(player, clayId, 1)) {
            if (addItem(
                    player,
                    classItems[componentId - 37] +
                        (
                            if ((componentId == 57 || componentId == 58 || componentId == 61)) {
                                0
                            } else if (componentId ==
                                56
                            ) {
                                index
                            } else if (componentId >= 64) {
                                (-index * 2)
                            } else {
                                (index * 2)
                            }
                        ),
                    (
                        if (componentId in 56..58) {
                            15 * (index + 1)
                        } else if (componentId == 61) {
                            index + 1
                        } else {
                            1
                        }
                    ) * amount,
                )
            ) {
                removeItem(player, Item(itemId, amount))
                return true
            }
        }
        sendMessage(player, "You have no clay to process.")
        return false
    }

    fun checkRequirements(
        player: Player,
        requestedSkill: Int,
        index: Int,
    ): Boolean {
        val level = getLevelForIndex(index)
        if (getStatLevel(player, requestedSkill) <= level) {
            sendMessage(
                player,
                "You dont have the required ${Skills.SKILL_NAME[requestedSkill]} level for that quality of clay.",
            )
            return false
        }
        return true
    }

    fun startDynamicSkill(
        player: Player,
        scenery: Scenery,
        animation: Animation,
        baseId: Int,
        objectIndex: Int,
    ) {
        if (!checkRequirements(player, getScenery(), objectIndex)) return
        val item = getBestItem(player, baseId)
        if (item.id == -1) {
            Animation(10602)
        } else if (inInventory(player, item.id, item.amount)) {
            animation.id
        }
        submitIndividualPulse(player, CreationSkillPulse(player, scenery, animation, item, baseId, objectIndex))
    }

    private fun getBestItem(
        player: Player,
        baseId: Int,
    ): Item {
        for (index in 4 downTo 0) {
            val item = Item(baseId + (index * 2), 1)
            if (inInventory(player, item.id)) {
                return item
            }
        }
        return Item(-1, 1)
    }

    fun getScenery(): Int = 0

    fun getRequestedKilnSkill(index: Int): Int =
        when {
            index in 0..1 || index in 6..8 || index in 15..17 -> Skills.SMITHING
            index in 2..3 || index in 9..14 || index in 18..19 || index == 23 -> Skills.CRAFTING
            index == 4 -> Skills.CONSTRUCTION
            index == 5 -> Skills.COOKING
            index in 20..21 -> Skills.RUNECRAFTING
            index in 22..24 -> Skills.SUMMONING
            index in 25..32 -> Skills.HERBLORE
            else -> 1
        }

    private fun getLevelForIndex(index: Int): Int {
        var level = 0
        for (i in 0 until index) {
            if (i == index) return level
            level += 20
        }
        return level
    }

    private fun hasRequiredPlayers(): Boolean = redTeam.size >= 5 && blueTeam.size >= 5

    fun updateInterfaces() {
        val hidden = hasRequiredPlayers()
        for (player in redTeam) {
            setComponentVisibility(player, 804, 2, hidden)
            updateTeamComponents(player, true, hidden)
        }
        for (player in blueTeam) {
            setComponentVisibility(player, 804, 2, hidden)
            updateTeamComponents(player, false, hidden)
        }
    }

    fun updateTeamComponents(
        player: Player,
        inRedTeam: Boolean,
        hidden: Boolean,
    ) {
        val skillTotal = getTotalLevel(SkillIds, inRedTeam)
        val combatTotal = getTotalLevel(CombatSkillIds, inRedTeam)
        val otherSkillTotal = getTotalLevel(SkillIds, !inRedTeam)
        val otherCombatTotal = getTotalLevel(CombatSkillIds, !inRedTeam)
        sendString(player, "$combatTotal", 804, 4)
        sendString(player, "$skillTotal", 804, 5)
        sendString(player, "$otherSkillTotal", 804, 6)
        sendString(player, "$otherCombatTotal", 804, 7)
        for (i in 33..33) {
            sendString(
                player,
                "${if (hidden) {
                    5 - (if (inRedTeam) redTeam.size else blueTeam.size)
                } else if (inRedTeam) {
                    redTeam.size
                } else {
                    blueTeam.size
                }}",
                804,
                i,
            )
        }
    }

    private fun canEnter(
        player: Player,
        inRedTeam: Boolean,
    ): Boolean {
        val skillTotal = getTotalLevel(SkillIds, inRedTeam)
        val combatTotal = getTotalLevel(CombatSkillIds, inRedTeam)
        val otherSkillTotal = getTotalLevel(SkillIds, !inRedTeam)
        val otherCombatTotal = getTotalLevel(CombatSkillIds, !inRedTeam)
        if (skillTotal + combatTotal > otherSkillTotal + otherCombatTotal) {
            sendMessage(player, "This team is too strong for you to join at present.")
            return false
        } else if (!anyInEquipment(player) || freeSlots(player) == 0 || player.familiarManager.hasFamiliar()) {
            sendMessage(
                player,
                "You may not take any items into Stealing Creation. You can use the nearby bank deposit bank to empty your inventory and store wore items.",
            )
            return false
        }
        return true
    }

    private fun getTotalLevel(
        ids: IntArray,
        inRedTeam: Boolean,
    ): Int {
        var skillTotal = 0
        for (player in if (inRedTeam) redTeam else blueTeam) {
            if (player == null) continue
            for (skillRequested in ids) {
                skillTotal += player.skills.getLevel(skillRequested)
            }
        }
        return skillTotal
    }

    fun getAnimationForBase(
        baseId: Int,
        index: Int,
    ): Animation = Animation(basicAnimation[index] + baseId)
}
