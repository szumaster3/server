package content.region.misthalin.dialogue.draynor

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class OliviaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        options(
            "Would you like to trade in seeds?",
            "Where do I get higher-level seeds?",
            "About the recent bank robbery...",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                when (buttonId) {
                    1 -> npc(FaceAnim.HAPPY, "Certainly.").also { stage++ }
                    2 -> player(FaceAnim.ASKING, "Where do I get higher-level seeds?").also { stage = 2 }
                    3 ->
                        player(
                            FaceAnim.ASKING,
                            "About the recent bank robbery...",
                            "The robber attacked you, didn't he?",
                        ).also { stage = 3 }
                }

            1 -> end().also { openNpcShop(player, npc.id) }
            2 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "The Master Farmers usually carry a few rare seeds",
                    "around with them, although I don't know if they'd want",
                    "to part with them for any price to be honest.",
                ).also { stage = END_DIALOGUE }

            3 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Something like that, yes. I was just minding my own",
                    "business, trying to keep these pesky thieves off my stalls,",
                    "when I heard a strange noise and a scream.",
                ).also { stage++ }

            4 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I went across to see what was going on, but there was",
                    "a bright purple flash and everything went black. Before I knew",
                    "what had happened, I was standing in Falador!",
                ).also { stage++ }

            5 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "It took me ages to get back, and when I arrived I saw",
                    "there was a huge hole in the bank wall and some of the bankers",
                    "had been killed!",
                ).also { stage++ }

            6 -> playerl(FaceAnim.FRIENDLY, "Did you see who teleported you to Falador?").also { stage++ }
            7 -> npcl(FaceAnim.HALF_GUILTY, "No, they were standing behind that old man's door.").also { stage++ }
            8 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "He really shouldn't leave his door open all day,",
                    "especially in this neighbourhood - there are thousands of thieves around,",
                    "and anyone could wander in!",
                ).also { stage++ }

            9 -> playerl(FaceAnim.FRIENDLY, "Okay, thanks for your time.").also { stage++ }
            10 ->
                npc(
                    FaceAnim.HALF_ASKING,
                    "You're welcome. Would you like to see my fine",
                    "selection of seeds?",
                ).also { stage++ }

            11 -> options("Yes please.", "No thanks.").also { stage++ }
            12 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Yes please.").also { stage = 1 }
                    2 -> playerl(FaceAnim.FRIENDLY, "No thanks.").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return OliviaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.OLIVIA_2233, NPCs.OLIVIA_2572)
    }
}
