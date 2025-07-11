package content.global.skill.thieving

import core.api.*
import core.game.event.ResourceProducedEvent
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.GameWorld
import core.game.world.map.RegionManager.getLocalNpcs
import core.tools.RandomFunction
import core.tools.StringUtils
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * A pulse that handles the interaction logic for stealing from a stall.
 */
class StallThiefPulse(
    player: Player?,
    node: Scenery?,
    private val stall: Stall?
) : SkillPulse<Scenery?>(player, node) {

    private var ticks = 0

    override fun start() {
        player.setAttribute("thieveDelay", GameWorld.ticks)
        super.start()
    }

    override fun checkRequirements(): Boolean {
        if (stall == null) {
            return false
        }
        if (player.inCombat()) {
            sendMessage(player, "You can't steal from the market stall during combat!")
            return false
        }
        if (getStatLevel(player, Skills.THIEVING) < stall.level) {
            sendDialogue(player, "You need to be level " + stall.level + " to steal from the " + node!!.name.lowercase() + ".")
            return false
        }
        if (freeSlots(player) == 0) {
            sendMessage(player, "You don't have enough inventory space.")
            return false
        }
        if (inBorders(player, getRegionBorders(10553)) && !isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
            sendDialogue(player, "The ${if (stall.fullIDs.contains(4278)) "fur trader" else "fishmonger"} is staring at you suspiciously. You cannot steal from his stall while he distrusts you.")
            return false
        }
        return true
    }

    override fun hasInactiveNode(): Boolean = if (player.getAttribute("thieveDelay", 0) <= GameWorld.ticks) {
        false
    } else {
        super.hasInactiveNode()
    }

    override fun animate() {}

    override fun reward(): Boolean {
        val goods = stall?.message ?: "goods"

        if (ticks == 0) {
            animate(player, ANIMATION)
            lockInteractions(player, 2)
            if (node?.id != 2793) {
                sendMessage(player, "You attempt to steal some $goods from the stall.")
            }
        }

        if (++ticks % 3 != 0) {
            return false
        }

        val success = success()
        if (success) {
            if (stall == Stall.SILK_STALL) {
                player.getSavedData().globalData.setSilkSteal(System.currentTimeMillis() + 1200000)
            }
            if (stall == Stall.TEA_STALL) {
                player.getSavedData().globalData.setTeaSteal(System.currentTimeMillis() + 1200000)
            }
            if (node!!.isActive) {
                SceneryBuilder.replace(node, node!!.transform(stall!!.getEmpty(node!!.id)), stall.delay)
            }
            val item = stall!!.randomLoot
            player.inventory.add(item)
            rewardXP(player, Skills.THIEVING, stall.experience)
            if (item.id == Items.GRAPES_1987) {
                sendMessage(player, "You steal grapes from the grape stall.")
                return true
            }
            if (item.id == Items.ROCK_CAKE_2379) {
                sendMessage(player, "You cautiously grab a cake from the stall.")
                return true
            }
            if (stall == Stall.CANDLES) {
                return true
            }
            sendMessage(
                player, "You steal " + (if (StringUtils.isPlusN(item.name)) {
                    "an "
                } else {
                    "a "
                }) + getItemName(item.id).lowercase() + "."
            )
            player.dispatch(ResourceProducedEvent(item.id, item.amount, node!!, 0))
        }
        return true
    }

    override fun message(type: Int) {
        if (stall == Stall.CANDLES) {
            return
        }
    }

    private fun success(): Boolean {
        val mod = 0
        if (RandomFunction.random(15 + mod) < 4) {
            if (stall == Stall.CANDLES) {
                stun(player, 15, false)
                impact(player, 3, ImpactHandler.HitsplatType.NORMAL)
                sendMessage(player, "A higher power smites you.")
                return false
            }
            for (npc in getLocalNpcs(player.location, 8)) {
                if (!npc.properties.combatPulse.isAttacking && (npc.id == NPCs.GUARD_32 || npc.id == NPCs.MARKET_GUARD_2236)) {
                    npc.sendChat("Hey! Get your hands off there!")
                    npc.properties.combatPulse.attack(player)
                    return false
                }
            }
        }
        return true
    }

    companion object {
        private const val ANIMATION = Animations.MULTI_TAKE_832
    }
}
