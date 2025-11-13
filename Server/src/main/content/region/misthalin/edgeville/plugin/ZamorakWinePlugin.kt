package content.region.misthalin.edgeville.plugin

import core.api.drainStatLevel
import core.api.impact
import core.api.sendChat
import core.api.sendGraphics
import core.game.global.action.PickupHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.world.map.RegionManager
import shared.consts.Graphics
import shared.consts.Items
import shared.consts.NPCs

/**
 * Handles the interaction for taking the
 * wine of zamorak from the asgarnia chaos temple.
 */
class ZamorakWinePlugin : InteractionListener {

    companion object {
        private val COMBAT_STATS = intArrayOf(0, 1, 2, 3, 4, 5, 6)
    }

    override fun defineListeners() {
        on(Items.WINE_OF_ZAMORAK_245, IntType.GROUND_ITEM, "take") { player, wine ->
            if (player.location.getRegionId() != 11574) {
                PickupHandler.take(player, wine as GroundItem)
                return@on true
            }

            RegionManager.getLocalNpcs(player).forEach { npc ->
                if (npc.id == NPCs.MONK_OF_ZAMORAK_188) {
                    sendChat(npc, "STOP STEALING MY WINE! GAH!")
                    npc.attack(player)
                }
            }

            drainCombatStats(player)
            sendGraphics(Graphics.FLAMES_OF_ZAMORAK_78, player.location)
            impact(player, calculateImpact(player), ImpactHandler.HitsplatType.NORMAL)
            PickupHandler.take(player, wine as GroundItem)
            return@on true
        }
    }

    private fun calculateImpact(player: Player): Int {
        return when (player.skills.lifepoints) {
            in 1..19 -> 1
            in 20..39 -> 2
            in 40..59 -> 3
            in 60..79 -> 4
            else -> 5
        }
    }

    private fun drainCombatStats(player: Player) {
        COMBAT_STATS.forEach { stat ->
            val dynamicLevel = player.skills.dynamicLevels[stat]
            val staticLevel = player.skills.getStaticLevel(stat)

            if (dynamicLevel <= 0 || staticLevel <= 0) return@forEach

            val drainAmount = if (staticLevel > 40) 3 else 1
            val percentage = drainAmount.toDouble() / dynamicLevel

            drainStatLevel(player, stat, percentage, 0.075)
        }
    }
}