package content.region.kandarin.quest.luc2.handlers

import content.region.kandarin.quest.luc2.general.KhazardLaundererDialogue
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs.consts.*

class WhileGuthixSleepsListeners : InteractionListener {
    override fun defineListeners() {
        on(NPCs.KHAZARD_LAUNDERER_8428, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, KhazardLaundererDialogue())
            return@on true
        }

        onUseWith(IntType.ITEM, Items.ENRICHED_SNAPDRAGON_14487, Items.TRUTH_SERUM_6952) { player, used, with ->
            sendMessages(
                player,
                "You mix the enriched snapdragon into the truth serum.",
                "It dissolves immediately and gives off a delicious, delicate aroma.",
            )
            animate(player, 3283)
            runTask(player, 1) {
                if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                    addItem(player, Items.SUPER_TRUTH_SERUM_14488)
                }
            }
            return@onUseWith true
        }

        on(Scenery.TRAPDOOR_TABLE_40897, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "You open the trapdoor.")
            setVarbit(player, 5511, 3)
            return@on true
        }

        on(Scenery.TRAPDOOR_40898, IntType.SCENERY, "climb-into", "close") { player, _ ->
            when (getUsedOption(player)) {
                "close" -> {
                    sendMessage(player, "You close the trapdoor.")
                    setVarbit(player, 5511, 2)
                    return@on true
                }

                else -> {
                    lock(player, 6)
                    submitIndividualPulse(
                        player,
                        object : Pulse(1) {
                            var counter = 0

                            override fun pulse(): Boolean {
                                when (counter++) {
                                    0 -> openInterface(player, Components.FADE_TO_BLACK_115)
                                    3 -> teleport(player, Location(2035, 4379, 0))
                                    4 -> openInterface(player, Components.FADE_FROM_BLACK_170)
                                    6 -> {
                                        closeOverlay(player)
                                        player.musicPlayer.unlock(Music.DANGEROUS_LOGIC_579)
                                        unlock(player)
                                        return true
                                    }
                                }
                                return false
                            }
                        },
                    )
                }
            }
            return@on true
        }
    }
}
