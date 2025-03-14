package content.region.kandarin.dialogue.jiggig

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Sounds
import org.rs.consts.Vars

@Initializable
class OgreGuardDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) in 1..12) {
            npc(
                FaceAnim.OLD_DEFAULT,
                "You needs to stay away from dis place...yous get da",
                "sickies and mebe yous goes to dead if yous da unlucky",
                "fing.",
            )
            stage = 0
        } else if (getVarbit(player, Vars.VARBIT_QUEST_ZOGRE_GATE_PASSAGE_496) == 1 ||
            getVarbit(player, Vars.VARBIT_QUEST_ZORGE_FLESH_EATERS_PROGRESS_487) == 13
        ) {
            npc(FaceAnim.OLD_DEFAULT, "Yeah, whats yous wants creatures?")
            stage = END_DIALOGUE
        } else {
            npc(
                FaceAnim.OLD_DEFAULT,
                "You needs to stay away from dis place...yous get da",
                "sickies and mebe yous goes to dead if yous da unlucky",
                "fing.",
            )
            stage = 5
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                player(
                    "But Grish has asked me to look into this place and find",
                    "out why all the undead ogres are here.",
                ).also {
                    stage++
                }
            1 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Ok, dat is da big, big scary, danger fing!",
                    "You's sure you's wants to go in?",
                ).also {
                    stage++
                }
            2 -> player("Yes, I'm sure.").also { stage++ }
            3 -> npc(FaceAnim.OLD_DEFAULT, "Ok, I opens da stoppa's for yous creature.").also { stage++ }
            4 -> {
                end()
                submitWorldPulse(
                    object : Pulse() {
                        var counter = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                0 -> {
                                    npc.asNpc().faceLocation(Location.create(2457, 3048, 0))
                                }

                                2 -> {
                                    animate(npc.asNpc(), 2102)
                                    playAudio(player, Sounds.OGRE_DESTROY_BARRICADE_1954, 1)
                                }

                                4 -> {
                                    setVarbit(player, Vars.VARBIT_QUEST_ZOGRE_GATE_PASSAGE_496, 1, true)
                                }

                                5 -> {
                                    end()
                                    face(npc.asNpc(), player, 2)
                                    sendNPCDialogue(
                                        player,
                                        NPCs.OGRE_GUARD_2042,
                                        "Ok der' yous goes!",
                                        FaceAnim.OLD_DEFAULT,
                                    ).also {
                                        stage =
                                            END_DIALOGUE
                                    }
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            }

            5 -> player("Don't worry, I know how to take care of myself.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return OgreGuardDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.OGRE_GUARD_2042)
    }
}
