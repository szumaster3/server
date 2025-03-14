package content.region.fremennik.handlers.general_shadows

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs.consts.Scenery

class GeneralShadowListener : InteractionListener {
    override fun defineListeners() {
        on(Scenery.CRACK_21800, IntType.SCENERY, "Enter") { player, node ->
            if (getAttribute(player, GeneralShadowUtils.GS_SEVERED_LEG, false)) {
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
            } else if (getAttribute(player, GeneralShadowUtils.GS_COMPLETE, false)) {
                teleport(player, Location(1759, 4711, 0))
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@on true
        }
    }
}
