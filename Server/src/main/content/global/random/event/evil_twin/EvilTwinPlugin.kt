package content.global.random.event.evil_twin

import content.data.GameAttributes
import core.api.*
import core.game.component.Component
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.net.packet.PacketRepository
import core.net.packet.context.CameraContext
import core.net.packet.out.CameraViewPacket
import shared.consts.NPCs

/**
 * Handles the evil twin random event.
 * @author Emperor, szu
 */
class EvilTwinPlugin :
    InteractionListener,
    MapArea {
    private val mollyId = (NPCs.MOLLY_3892..NPCs.MOLLY_3911).toIntArray()
    private val doorsId = shared.consts.Scenery.DOOR_14982
    private val controlPanel = shared.consts.Scenery.CONTROL_PANEL_14978

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(getRegionBorders(EvilTwinUtils.region.id))
    override fun getRestrictions(): Array<ZoneRestriction> = arrayOf(ZoneRestriction.CANNON, ZoneRestriction.FOLLOWERS)

    override fun defineListeners() {

        /*
         * Handles talk to molly at Random event.
         */

        on(mollyId, IntType.NPC, "talk-to") { player, node ->
            if ((EvilTwinUtils.tries < 1 || EvilTwinUtils.success) &&
                node.id >= NPCs.MOLLY_3892 &&
                node.id <= NPCs.MOLLY_3911
            ) {
                openDialogue(player, MollyDialogue(if (EvilTwinUtils.success) 2 else 1), node.id)
            }
            return@on true
        }

        /*
         * Handles operating the crane.
         */

        on(controlPanel, IntType.SCENERY, "use") { player, _ ->
            if (EvilTwinUtils.success) {
                sendMessage(player, "You already caught the evil twin.")
                return@on true
            } else {
                player.interfaceManager.openSingleTab(
                    Component(240).setUncloseEvent { player, c ->
                        SceneryBuilder.remove(EvilTwinUtils.currentCrane)
                        SceneryBuilder.add(Scenery(66, EvilTwinUtils.currentCrane?.location, 22, 0))
                        EvilTwinUtils.currentCrane = EvilTwinUtils.currentCrane!!.transform(EvilTwinUtils.currentCrane!!.id, EvilTwinUtils.currentCrane!!.rotation, EvilTwinUtils.region.baseLocation.transform(14, 12, 0))
                        SceneryBuilder.add(Scenery(14977, EvilTwinUtils.currentCrane?.location, 22, 0))
                        SceneryBuilder.add(EvilTwinUtils.currentCrane)
                        PacketRepository.send(CameraViewPacket::class.java, CameraContext(player, CameraContext.CameraType.RESET, 0, 0, 0, 0, 0))
                        true
                    }
                )
                player.packetDispatch.sendString("Tries: ${EvilTwinUtils.tries}", 240, 27)
                EvilTwinUtils.updateCraneCam(player, 14, 12)
            }
            return@on false
        }

        /*
         * Handles doors between molly and twins if player wants to rush.
         */

        on(doorsId, IntType.SCENERY, "open") { player, node ->
            val end = DoorActionHandler.getEndLocation(player, node.asScenery())
            if (player.location.getLocalX() < 9 && !player.getAttribute(GameAttributes.RE_TWIN_DIAL, false)) {
                openDialogue(player, MollyDialogue(3), EvilTwinUtils.mollyNPC!!)
                return@on true
            }
            DoorActionHandler.open(node.asScenery(), node.asScenery(), node.id, node.id + 1, true, 3, false)
            forceWalk(player, end, "")
            return@on true
        }
    }
}
