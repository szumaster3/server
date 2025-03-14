package content.region.misthalin.dialogue.wizardstower

import core.api.*
import core.game.dialogue.Dialogue
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import org.rs.consts.NPCs
import org.rs.consts.Sounds

@Initializable
class IsidorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("Oh, hello there! Can I do anything for you?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                player(
                    "I'm a friend of Explorer Jack and need teleporting",
                    "somewhere. The phrase is 'Ectosum glissendo'. Could",
                    "you help me, please?",
                )
                stage = 1
            }

            1 ->
                if (!player.familiarManager.hasFamiliar()) {
                    npc("Oh, okay then! There you go.")
                    stage = 2
                } else {
                    npc("I can teleport you, but not any pets or followers you", "may have.")
                    stage = 100
                }

            2 -> {
                end()
                npc.animate(Animation(437))
                npc.faceTemporary(player, 1)
                npc.graphics(Graphics(108))
                lock(player, 4)
                playAudio(player, Sounds.CURSE_ALL_125, 0, 1)
                Projectile.create(npc, player, 109).send()
                npc.sendChat("Ectosum glissendo!")
                GameWorld.Pulser.submit(
                    object : Pulse(1) {
                        var counter = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                0 -> {
                                    lock(player, 2)
                                    player.graphics(
                                        Graphics(
                                            110,
                                            150,
                                        ),
                                    )
                                }

                                1 -> {
                                    teleport(player, location(2070, 5802, 0))
                                    player.graphics(
                                        Graphics(
                                            110,
                                            150,
                                        ),
                                    )
                                    unlock(player)
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            }

            100 -> end()
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return IsidorDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ISIDOR_8544)
    }
}
