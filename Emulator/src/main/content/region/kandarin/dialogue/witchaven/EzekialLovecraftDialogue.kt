package content.region.kandarin.dialogue.witchaven

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class EzekialLovecraftDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.FRIENDLY, "Well hello there; welcome to our little village. Pray, stay awhile.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "What do you do here?",
                    "Can I buy some fishing supplies please?",
                    "You are a bit too strange for me. Bye.",
                ).also {
                    stage++
                }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.ASKING, "What do you do here?").also { stage = 10 }
                    2 -> player(FaceAnim.HALF_ASKING, "Can I buy some fishing supplies please?").also { stage = 25 }
                    3 -> player(FaceAnim.ANNOYED, "You are a bit too strange for me. Bye.").also { stage = 30 }
                }
            10 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I supply the local fishermen with the tools and bait they need to do their job.",
                ).also {
                    stage++
                }
            11 -> player(FaceAnim.FRIENDLY, "Interesting. Have you been doing it long?").also { stage++ }
            12 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Why yes, all of my life. I took over from my father, who inherited the business from his father and so on. In fact, there have been Lovecraft's selling bait for over ten generations.",
                ).also {
                    stage++
                }
            13 -> player(FaceAnim.FRIENDLY, "Wow, that's some lineage.").also { stage++ }
            14 ->
                npcl(
                    FaceAnim.LAUGH,
                    "Oh yes, we have a long and interesting family history. For one reason or another the Lovecraft's have always been bait sellers or writers.",
                ).also {
                    stage++
                }
            15 -> playerl(FaceAnim.FRIENDLY, "Hmm, yes well. I'm sure it's all fascinating, but I...").also { stage++ }
            16 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Oh very, very fascinating, for instance my great grandfather Howard...",
                ).also { stage++ }
            17 ->
                playerl(
                    FaceAnim.HALF_ROLLING_EYES,
                    "Yawn. Oh, I'm sorry but I really must be getting on. I think my giraffe needs feeding.",
                ).also {
                    stage++
                }
            18 -> npc(FaceAnim.HALF_ASKING, "Your?").also { stage++ }
            19 ->
                playerl(
                    FaceAnim.ANNOYED,
                    "Giraffe. Sorry, but he gets cranky without enough sugar. Bye now!",
                ).also { stage++ }
            20 -> npc(FaceAnim.FRIENDLY, "Oh right, goodbye.").also { stage = END_DIALOGUE }
            25 -> {
                end()
                openNpcShop(player, NPCs.EZEKIAL_LOVECRAFT_4856)
            }

            30 -> npc(FaceAnim.FRIENDLY, "Sniff. Yes, everyone says that.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return EzekialLovecraftDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.EZEKIAL_LOVECRAFT_4856)
    }
}
