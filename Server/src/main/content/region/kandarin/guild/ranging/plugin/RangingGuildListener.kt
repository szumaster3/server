package content.region.kandarin.guild.ranging.plugin

import core.api.*
import core.game.container.impl.EquipmentContainer
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.RegionManager.getLocalNpcs
import core.game.world.map.zone.ZoneBorders
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Regions
import shared.consts.Scenery

class RangingGuildListener : InteractionListener, MapArea {

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders.forRegion(Regions.RANGING_GUILD_10549))
    }

    override fun areaEnter(entity: Entity) {
        if (entity !is Player) return

        val player = entity
        val players = RegionManager.getLocalPlayers(player)

        val inAreaPlayers = players.filter { p ->
            defineAreaBorders().any { zone -> inBorders(p, zone) }
        }

        if (inAreaPlayers.size > 1) {
            return
        }

        getLocalNpcs(Location.create(2668, 3427, 2)).forEach { n ->
            towerDirections[n.id]?.let { dir ->
                sendChat(n, "The $dir tower is occupied, get them!")
            }
        }
    }

    companion object {
        private val towerDirections = mapOf(
            NPCs.TOWER_ADVISOR_684 to "north",
            NPCs.TOWER_ADVISOR_685 to "east",
            NPCs.TOWER_ADVISOR_686 to "south",
            NPCs.TOWER_ADVISOR_687 to "west"
        )
    }

    override fun defineListeners() {

        /*
         * Handles conversation with the Ranging Guild Doorman.
         */

        on(NPCs.RANGING_GUILD_DOORMAN_679, IntType.NPC, "talk-to") { player, node ->
            sendPlayerDialogue(player, "Hello there.")
            addDialogueAction(player) { _, _ ->
                sendNPCDialogueLines(
                    player,
                    node.id,
                    FaceAnim.HALF_GUILTY,
                    false,
                    "Greetings. If you are an experienced archer, you may",
                    "want to visit the guild here...",
                )

            }
            return@on true
        }

        /*
         * Handles dialogue with a Ranging Guild Guard.
         */

        on(NPCs.GUARD_678, IntType.NPC, "talk-to") { player, node ->
            sendPlayerDialogue(player, "Hello there.")
            addDialogueAction(player) { _, _ ->
                sendNPCDialogueLines(
                    player,
                    node.id,
                    FaceAnim.HALF_GUILTY,
                    false,
                    "Greetings, traveller. Enjoy the time at the Ranging", "Guild.",
                )

            }
            return@on true
        }

        /*
         * Handles "fire-at" interaction with the archery target.
         */
        on(Scenery.TARGET_2513, IntType.SCENERY, "fire-at") { player, node ->
            if (player.archeryTargets <= 0) {
                sendNPCDialogueLines(
                    player,
                    NPCs.COMPETITION_JUDGE_693,
                    FaceAnim.HALF_GUILTY,
                    false,
                    "Sorry, you may only use the targets for the",
                    "competition, not for practicing.",
                )
                return@on true
            }
            if (!inInventory(player, Items.BRONZE_ARROW_882) ||
                player.equipment[EquipmentContainer.SLOT_WEAPON] == null || (
                        !player.equipment[EquipmentContainer.SLOT_WEAPON].definition.name.lowercase()
                            .contains("shortbow", true) &&
                                !player.equipment[EquipmentContainer.SLOT_WEAPON].definition.name.lowercase()
                                    .contains("longbow", true))
            ) {
                sendMessage(player, "You must have bronze arrows and a bow equipped.")
                return@on true
            }
            submitIndividualPulse(player, ArcheryCompetitionPulse(player, (node.asScenery())))
            return@on true
        }
    }
}
