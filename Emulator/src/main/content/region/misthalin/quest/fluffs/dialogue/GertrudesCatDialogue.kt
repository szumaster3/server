package content.region.misthalin.quest.fluffs.dialogue

import core.api.animate
import core.api.quest.getQuestStage
import core.api.sendChat
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Animations
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class GertrudesCatDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private val BEND_DOWN = Animation.create(Animations.MULTI_BEND_OVER_827)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        options("Talk-with", "Pick-up", "Stroke")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val quest = getQuestStage(player, Quests.GERTRUDES_CAT)
        when (stage) {
            545 -> end()
            0 ->
                when (buttonId) {
                    1 ->
                        if (quest < 60) {
                            end()
                            sendChat(npc, "Miaoww")
                        } else {
                            player("Oh, it looks like Fluffs has returned.", "I'd better leave her alone.").also {
                                stage =
                                    END_DIALOGUE
                            }
                        }

                    2 -> {
                        close()
                        player.pulseManager.run(
                            object : Pulse(1, player) {
                                var count = 0

                                override fun pulse(): Boolean {
                                    when (count++) {
                                        0 -> animate(player, BEND_DOWN)
                                        2 -> sendChat(npc, "Hisss!")
                                        3 -> sendChat(player, "Ouch!")
                                        4 -> {
                                            if (quest == 40) {
                                                core.api.sendDialogue(
                                                    player,
                                                    "Fluffs laps up the milk greedly. Then she mews at you again.",
                                                )
                                                stage = 545
                                                return true
                                            }
                                            if (quest == 30) {
                                                core.api.sendDialogue(player, "Maybe the cat is hungry?")
                                                stage = 545
                                                return true
                                            }
                                            if (quest == 50) {
                                                end()
                                                return true
                                            }
                                            end()
                                            core.api.sendDialogue(player, "Maybe the cat is thirsty?")
                                            stage = 545
                                        }
                                    }
                                    return count == 5
                                }
                            },
                        )
                    }

                    545 -> end()
                    3 -> {
                        close()
                        player.pulseManager.run(
                            object : Pulse(1, player) {
                                var count = 0

                                override fun pulse(): Boolean {
                                    when (count++) {
                                        0 -> player.animate(BEND_DOWN)
                                        2 -> npc.sendChat("Hisss!")
                                        3 -> player.sendChat("Ouch!")
                                        4 -> {
                                            if (quest == 40) {
                                                return true
                                            }
                                            interpreter.sendDialogue("Perhaps the cat want's something?")
                                            stage = 545
                                        }
                                    }
                                    return count == 5
                                }
                            },
                        )
                        if (quest == 40) {
                            return true
                        }
                        GameWorld.Pulser.submit(
                            object : Pulse(7, player) {
                                override fun pulse(): Boolean {
                                    end()
                                    return true
                                }
                            },
                        )
                    }
                }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return GertrudesCatDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GERTRUDES_CAT_2997)
    }
}
