package content.global.random.event.freaky_forester

import content.data.GameAttributes
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery

/**
 * Handles interaction for Freaky forester random event.
 * @author szu
 */
class FreakyForesterPlugin : InteractionListener, MapArea {

    companion object {
        private const val EXIT_ID = Scenery.PORTAL_8972
        private const val NPC_ID = NPCs.FREAKY_FORESTER_2458
        private val PHEASANT_NPC_IDS = intArrayOf(NPCs.PHEASANT_2459, NPCs.PHEASANT_2460, NPCs.PHEASANT_2461, NPCs.PHEASANT_2462)
    }
    override fun defineListeners() {
        on(NPC_ID, IntType.NPC, "talk-to") { player, node ->
            if (!inBorders(player, FreakyForesterUtils.freakArea)) {
                sendDialogue(player, "Freaky Forester is not interested in talking.")
                return@on true
            }

            if (getAttribute(player, GameAttributes.RE_FREAK_TASK, -1) == -1) {
                FreakyForesterUtils.giveFreakTask(player)
            }

            openDialogue(player, FreakyForesterDialogue(), node.asNpc())
            return@on true
        }

        on(PHEASANT_NPC_IDS, IntType.NPC, "attack") { player, node ->
            if (hasComplete(player) || hasRawPheasant(player)) {
                sendDialogue(player, "You don't need to attack any more pheasants.")
            } else {
                player.attack(node.asNpc())
            }
            return@on true
        }

        on(EXIT_ID, IntType.SCENERY, "enter") { player, _ ->
            if (getAttribute(player, GameAttributes.RE_FREAK_COMPLETE, false)) {
                FreakyForesterUtils.cleanup(player)
                queueScript(player, 6, QueueStrength.SOFT) {
                    FreakyForesterUtils.reward(player)
                    return@queueScript stopExecuting(player)
                }
            } else {
                sendMessage(player, "A supernatural force prevents you from leaving.")
            }
            return@on true
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders(2587, 4758, 2616, 4788))

    override fun getRestrictions(): Array<ZoneRestriction> =
        arrayOf(
            ZoneRestriction.RANDOM_EVENTS,
            ZoneRestriction.CANNON,
            ZoneRestriction.FOLLOWERS
        )

    override fun areaEnter(entity: Entity) {
        entity.locks.lockTeleport(1000000)
    }

    /**
     * Checks if the player has already completed the event.
     */
    private fun hasComplete(player: Player): Boolean =
        getAttribute(player, GameAttributes.RE_FREAK_COMPLETE, false)

    /**
     * Checks if the player has any raw item or has already killed npc.
     */
    private fun hasRawPheasant(player: Player): Boolean =
        inInventory(player, Items.RAW_PHEASANT_6178) ||
                inInventory(player, Items.RAW_PHEASANT_6179) ||
                getAttribute(player, GameAttributes.RE_FREAK_KILLS, false)
}
