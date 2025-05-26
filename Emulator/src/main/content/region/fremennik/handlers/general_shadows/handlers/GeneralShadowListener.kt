package content.region.fremennik.handlers.general_shadows.handlers

import content.region.fremennik.handlers.general_shadows.GeneralShadow
import content.region.fremennik.handlers.general_shadows.cutscene.CavernCutscene
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs.consts.Items
import org.rs.consts.Scenery

class GeneralShadowListener : InteractionListener {
    override fun defineListeners() {
        /*
         * Handles enter to the cave with bouncer (General Shadows mini-quest).
         */

        on(Scenery.CRACK_21800, IntType.SCENERY, "Enter") { player, node ->
            if (GeneralShadow.isComplete(player)) {
                teleport(player, Location(1759, 4711, 0))
                return@on true
            }
            if (getAttribute(player, GeneralShadow.GS_RECEIVED_SEVERED_LEG, false) && GeneralShadow.getShadowProgress(player) == 4 && inInventory(player, Items.SEVERED_LEG_10857)) {
                openDialogue(
                    player,
                    object : DialogueFile() {
                        override fun handle(
                            componentID: Int,
                            buttonID: Int,
                        ) {
                            when (stage) {
                                0 ->
                                    sendDialogue(
                                        player,
                                        "You have a bad feeling about crawling into the next cavern.",
                                    ).also { stage++ }
                                1 -> {
                                    setTitle(player, 2)
                                    sendDialogueOptions(player, "Do you want to enter the cavern?", "Yes", "No")
                                    stage++
                                }
                                2 ->
                                    when (buttonID) {
                                        1 -> {
                                            end()
                                            lock(player, 1000)
                                            lockInteractions(player, 1000)
                                            CavernCutscene(player).start()
                                        }
                                        2 -> end()
                                    }
                            }
                        }
                    },
                    node,
                )
            } else if (player.location == location(1759, 4711, 0)) {
                teleport(player, location(2617, 9828, 0))
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@on true
        }

        /*
         * Handles enter to the Goblin temple.
         */

        on(Scenery.STAIRS_27543, IntType.SCENERY, "climb-down") { player, _ ->
            teleport(player, Location.create(2784, 4241, 0))
            return@on true
        }

        /*
         * Handles exit from Goblin temple.
         */

        on(Scenery.DOOR_27332, IntType.SCENERY, "open") { player, _ ->
            teleport(player, Location.create(2581, 9851, 0))
            return@on true
        }
    }
}
