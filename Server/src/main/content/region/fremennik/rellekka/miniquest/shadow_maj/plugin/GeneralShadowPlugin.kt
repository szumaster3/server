package content.region.fremennik.rellekka.miniquest.shadow_maj.plugin

import content.region.fremennik.rellekka.miniquest.shadow_maj.cutscene.CavernCutscene
import content.region.fremennik.rellekka.miniquest.shadow_maj.GeneralShadow
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import shared.consts.Items
import shared.consts.Scenery

class GeneralShadowPlugin : InteractionListener {

    override fun defineListeners() {/*
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

        /*
         * Handles enter to the cave with bouncer (General Shadows mini-quest).
         */

        on(Scenery.CRACK_21800, IntType.SCENERY, "Enter") { player, node ->
            when {
                node.location.x == 1759 && node.location.y == 4712 -> {
                    teleport(player, Location.create(2617, 9828, 0), TeleportManager.TeleportType.INSTANT, 1)
                    return@on true
                }
                GeneralShadow.isComplete(player) && node.location.x == 2617 && node.location.y == 9827 -> {
                    teleport(player, Location.create(1759, 4711, 0), TeleportManager.TeleportType.INSTANT, 1)
                    return@on true
                }
                getAttribute(player, GeneralShadow.GS_RECEIVED_SEVERED_LEG, false)
                        && GeneralShadow.getShadowProgress(player) == 4
                        && inInventory(player, Items.SEVERED_LEG_10857) -> {
                    openDialogue(
                        player,
                        object : DialogueFile() {
                            override fun handle(componentID: Int, buttonID: Int) {
                                when (stage) {
                                    0 -> {
                                        sendDialogue(player, "You have a bad feeling about crawling into the next cavern.")
                                        stage++
                                    }
                                    1 -> {
                                        setTitle(player, 2)
                                        options("Yes", "No", title = "Do you want to enter the cavern?")
                                        stage++
                                    }
                                    2 -> when (buttonID) {
                                        1 -> {
                                            end()
                                            CavernCutscene(player).start()
                                        }
                                        2 -> end()
                                    }
                                }
                            }
                        },
                        node
                    )
                    return@on true
                }

                else -> {
                    sendMessage(player, "Nothing interesting happens.")
                    return@on false
                }
            }
        }
    }
}
