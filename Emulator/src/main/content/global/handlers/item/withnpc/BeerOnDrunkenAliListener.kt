package content.global.handlers.item.withnpc

import core.api.removeItem
import core.api.runTask
import core.api.sendChat
import core.api.sendNPCDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs

class BeerOnDrunkenAliListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.NPC, Items.BEER_1917, NPCs.DRUNKEN_ALI_1863) { player, used, with ->
            if (used.id != Items.BEER_1917) {
                sendNPCDialogue(player, NPCs.DRUNKEN_ALI_1863, "Eh? What's this? I don't want that, get me a beer.")
            } else {
                if (removeItem(player, used.asItem())) {
                    var random = RandomFunction.random(0, 7)
                    when (random) {
                        0 -> sendChat(with.asNpc(), "I've got a lovely bunch of coconuts, doobidy doo.")
                        1 ->
                            sendNPCDialogue(
                                player,
                                NPCs.DRUNKEN_ALI_1863,
                                "Thank you my friend - now if you don't mind I'm having a conversation with my imaginary friend Bob here.",
                            )

                        2 -> {
                            sendChat(with.asNpc(), "Did you hear the one about the man who walked into a bar?").also {
                                runTask(player, 2) { sendChat(with.asNpc(), "He hurt his foot! Hah!") }
                            }
                        }

                        3 -> {
                            sendChat(with.asNpc(), "Wha'? Are you lookin' at me? Are you lookin' at me?").also {
                                runTask(player, 2) {
                                    sendChat(with.asNpc(), "You know something? You're drunk, you are.")
                                }
                            }
                        }

                        4 -> sendChat(with.asNpc(), "Why does nobody like me?")
                        5 -> sendChat(with.asNpc(), "Cheers for the beers!")
                        6 -> sendChat(with.asNpc(), "What you looking at.")
                        7 -> {
                            sendChat(
                                with.asNpc(),
                                "Did you hear the one where the camel walks into the bar and orders a pint?.",
                            ).also {
                                runTask(player, 2) { sendChat(with.asNpc(), "The barman asks 'Why the long face!'") }
                            }
                        }
                    }
                }
            }
            return@onUseWith true
        }
    }
}
