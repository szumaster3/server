package content.region.morytania.dialogue.canifis

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class FidelioDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hello there.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "H-hello. You l-look like a s-stranger to these p-parts.",
                    "Would you l-ike to buy something? I h-have some s-",
                    "special offers at the m-minute...some s-sample bottles for",
                    "s-storing s-snail slime.",
                ).also {
                    stage++
                }
            1 -> options("Yes, please.", "No, thanks.", "Why are your prices so high?").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Yes, please.").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "No thanks.").also { stage = 4 }
                    3 -> player(FaceAnim.HALF_GUILTY, "Why are your prices so high?").also { stage = 5 }
                }
            3 -> {
                end()
                openNpcShop(player, NPCs.FIDELIO_1040)
            }
            4 ->
                npcl(
                    FaceAnim.SAD,
                    "(sigh) Th-that's okay. Nobody ever w-wants to buy my wares. Oh, s-sure, if it was food or c-clothes they would though!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            5 ->
                npcl(
                    FaceAnim.SAD,
                    "As you p-probably know, the h-humans hate our kind and k-keep us trapped here. To get my s-stocks I have to sneak into the human lands in s-secret!",
                ).also {
                    stage++
                }
            6 ->
                npcl(
                    FaceAnim.SAD,
                    "All the s-secrecy and d-danger has played h-havoc with my nerves!",
                ).also { stage++ }
            7 -> player("But all your stuff is overpriced junk!").also { stage++ }
            8 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "N-not at all! They are v-valuable human artefacts",
                    "smuggled here at g-great personal risk!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return FidelioDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FIDELIO_1040)
    }
}
