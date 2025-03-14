package content.region.misthalin.dialogue.lumbridge

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class HamMemberDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player("What are all you people doing here?").also { stage = 1 }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 ->
                when ((1..3).random()) {
                    1 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Many of us disagree with the king about what freedoms the local monster population should have. We're taking a stand and mobilising our forces against the monstrous hordes.",
                        ).also {
                            stage++
                        }
                    2 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I'm totally in awe of Johanhus, he really knows what's what. I know he keeps going on about monsters and it's clear there are too many of them, so hey, I agree with whatever Johanhus says.",
                        ).also {
                            stage++
                        }
                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "We're against the monsters..like Johanhus says...we don't like them...you know.",
                        ).also {
                            stage++
                        }
                }

            2 ->
                options(
                    "Who are you and what do you do here?",
                    "What do you think you're going to achieve?",
                    "Where did all you people come from?",
                    "Ok, thanks.",
                ).also { stage++ }

            3 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_ASKING, "Who are you and what do you do here?").also { stage = 4 }
                    2 -> player(FaceAnim.HALF_ASKING, "What do you think you're going to achieve?").also { stage = 5 }
                    3 -> player(FaceAnim.HALF_ASKING, "Where did all you people come from?").also { stage = 8 }
                    4 -> playerl(FaceAnim.FRIENDLY, "Ok, thanks.").also { stage = END_DIALOGUE }
                }
            4 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I'm a strong believer in the non-monsters policy...we should really get rid of them...and if that means I have to live in a cave like a monster, so be it!",
                ).also {
                    stage =
                        2
                }
            5 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "We want a world without monsters, to live in safety and without fear of being attacked by these ferocious beasts.",
                ).also {
                    stage++
                }
            6 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "But there aren't that many ferocious beasts in the towns and cities.",
                ).also {
                    stage++
                }
            7 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "That's not enough, we want to get rid of them totally, we want to enjoy the surrounding lands and not worry about our children playing in caves and so on.",
                ).also {
                    stage =
                        2
                }
            8 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Most of us came from small towns that had been attacked by monsters. We all got fed up with it and so decided to join this movement.",
                ).also {
                    stage++
                }
            9 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "We're hoping to return to the towns and cities when we've cleaned up the areas that these monsters live in.",
                ).also {
                    stage =
                        2
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.HAM_GUARD_1710,
            NPCs.HAM_GUARD_1711,
            NPCs.HAM_GUARD_1712,
            NPCs.HAM_DEACON_1713,
            NPCs.HAM_MEMBER_1714,
            NPCs.HAM_MEMBER_1715,
            NPCs.HAM_MEMBER_1716,
            NPCs.HAM_MEMBER_1717,
        )
    }
}
