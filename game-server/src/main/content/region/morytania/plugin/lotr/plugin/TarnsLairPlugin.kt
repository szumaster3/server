package content.region.morytania.plugin.lotr.plugin

import core.api.*
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents Tarn's Lair.
 */
class TarnsLairPlugin : MapArea, InteractionListener {

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(ZoneBorders(3121, 4537, 3204, 4673))

    override fun getRestrictions(): Array<ZoneRestriction> = arrayOf(ZoneRestriction.CANNON)

    //override fun entityStep(entity: Entity, location: Location, lastLocation: Location) {
    //    val player = entity as? Player ?: return
    //    val trap = FloorTrap.getFromCoords(location.x, location.y) ?: return
    //    if (location != trap.location) return
    //    stopWalk(player)
    //    animateScenery(Scenery(trap.trap.id, trap.trap.location), 5631)
    //    queueScript(player, 1, QueueStrength.SOFT) {
    //        animate(player, 1441)
    //        sendChat(player, "Ouch!")
    //        val direction = Direction.getLogicalDirection(location, trap.location)
    //        val targetLocation = location.transform(direction, 1)
    //        forceMove(player, player.location, targetLocation, 0, 60, trap.direction, 1441)
    //        impact(player, Random.nextInt(0, 5))
    //        playAudio(player, Sounds.LOTR_LOGTRAP_3318)
    //        return@queueScript stopExecuting(player)
    //    }
    //}

    override fun defineListeners() {
        on(NPCs.ODOVACAR_5383, IntType.NPC, "bank", "collect") { player, node ->
            val option = getUsedOption(player)
            when (option) {
                "bank" -> {
                    if (!removeItem(player, Item(Items.COINS_995, 100))) {
                        sendNPCDialogue(
                            player,
                            node.id,
                            "I'm afraid you don't have the necessary funds with you at this time so I can't allow you to access your account.",
                            FaceAnim.HALF_GUILTY,
                        )
                    } else {
                        sendMessage(player, "You pay 100 coins to use the travelling bank.")
                        openBankAccount(player)
                    }
                }

                else -> openGrandExchangeCollectionBox(player)
            }
            return@on true
        }
    }
}
