package content.region.misc.dialogue.mosle

import core.api.inInventory
import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class JoeDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player("Hello!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                if (!inInventory(player, Items.BOOK_O_PIRACY_7144)) {
                    npcl(
                        FaceAnim.FRIENDLY,
                        "Arr? Be ye wantin' te go on account with our gang o' fillibusters?",
                    ).also { stage++ }
                } else {
                    npc("Whadda ya want?").also { stage = 4 }
                }
            1 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "The powder monkey be takin' a caulk after gettin' rowdy on bumboo, so there be plenty of room for ye.",
                ).also {
                    stage++
                }
            2 -> player(FaceAnim.STRUGGLE, "Riiiiight...").also { stage++ }
            3 -> playerl(FaceAnim.STRUGGLE, "I'll just be over here if you need me.").also { stage = END_DIALOGUE }
            4 ->
                options(
                    "Whadda ya got?",
                    "Nothing! Honest!",
                    "I wanted to know where you got that harpoon hand.",
                ).also { stage++ }
            5 ->
                when (buttonId) {
                    1 -> npc("We gots grog and we gots food. Here, take a look.").also { stage = 16 }
                    2 -> player("Nothing! Honest!").also { stage = 17 }
                    3 -> playerl(FaceAnim.HAPPY, "I wanted to know where you got that harpoon hand.").also { stage = 6 }
                }
            6 -> npcl(FaceAnim.FRIENDLY, "Yer new in town aintcha?").also { stage++ }
            7 -> playerl(FaceAnim.STRUGGLE, "Newish certainly.").also { stage++ }
            8 -> npcl(FaceAnim.FRIENDLY, "Well, if ye wants te hear the story, here goes.").also { stage++ }
            9 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "See, I was walkin' along the docks one day, and this fierce storm blew up.",
                ).also {
                    stage++
                }
            10 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "All of a sudden I turn an there, bearin' down on me is this albatross...",
                ).also {
                    stage++
                }
            11 ->
                playerl(
                    FaceAnim.NEUTRAL,
                    "Sorry to interrupt, but I would just like to clear something up.",
                ).also { stage++ }
            12 -> npcl(FaceAnim.FRIENDLY, "What? I was just gettin' to the good bit.").also { stage++ }
            13 ->
                playerl(
                    FaceAnim.NEUTRAL,
                    "When I said 'I wanted to know where you got that harpoon hand', I meant where did you get it made, or buy it from?",
                ).also {
                    stage++
                }
            14 ->
                npcl(FaceAnim.FRIENDLY, "If ye don't wants to hear the story then I ain't gonna tell ye!").also {
                    stage =
                        END_DIALOGUE
                }
            16 -> {
                end()
                openNpcShop(player, NPCs.JOE_3163)
            }
            17 -> npc("Then stop takin' up me floor space!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return JoeDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.JOE_3163)
    }
}
