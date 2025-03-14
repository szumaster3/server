package core.game.system.command.rottenpotato

import core.api.removeAttribute
import core.api.sendInputDialogue
import core.api.sendMessage
import core.game.bots.AIRepository
import core.game.dialogue.Dialogue
import core.game.dialogue.InputType
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.login.PlayerParser
import core.game.world.ImmerseWorld
import core.game.world.repository.Repository
import core.plugin.Initializable
import core.tools.colorize

@Initializable
class RottenPotatoRSHDDialogue(
    player: Player? = null,
) : Dialogue(player) {
    val ID = 38575793

    override fun newInstance(player: Player?): Dialogue {
        return RottenPotatoRSHDDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        options("Wipe bots", "Spawn bots", "Force log players", "View bank", "Copy inventory")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                when (buttonId) {
                    1 -> {
                        AIRepository.clearAllBots()
                        player.sendMessage(colorize("%RBots wiped."))
                        end()
                    }

                    2 -> {
                        ImmerseWorld.spawnBots()
                        player.sendMessage(colorize("%RBots Respawning."))
                        end()
                    }

                    3 -> {
                        Repository.disconnectionQueue.clear().also { end() }
                        Repository.players.toArray().forEach {
                            val p = it.asPlayer()
                            if (p != null && !p.isArtificial) { // Should never be null.
                                removeAttribute(p, "combat-time")
                                p.clear()
                                PlayerParser.save(p)
                                p.details.save()
                            }
                        }
                    }

                    4 -> {
                        end()
                        sendInputDialogue(player, InputType.STRING_SHORT, "Enter player name:") { value ->
                            val other =
                                Repository.getPlayerByName(
                                    value.toString().lowercase().replace(" ", "_"),
                                )
                            if (other == null) {
                                sendMessage(player, colorize("%RInvalid player name."))
                                return@sendInputDialogue
                            }
                            other.bank.open(player)
                        }
                    }

                    5 -> {
                        end()
                        sendInputDialogue(player, InputType.STRING_SHORT, "Enter player name:") { value ->
                            val other =
                                Repository.getPlayerByName(
                                    value.toString().lowercase().replace(" ", "_"),
                                )
                            if (other == null) {
                                sendMessage(player, colorize("%RInvalid player name."))
                                return@sendInputDialogue
                            }

                            player.inventory.clear()
                            player.inventory.addAll(other.inventory)
                        }
                    }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(ID)
    }
}
