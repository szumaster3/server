package content.region.misthalin.dialogue.dorgeshuun

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class NardokDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_SNEAKY, "Psst... wanna buy some weapons?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("What have you got?", "No thanks.", "Why are you whispering?").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player("What have you got?").also { stage++ }
                    2 -> player("No thanks.").also { stage = 9 }
                    3 -> player("Why are you whispering?").also { stage = 10 }
                }
            2 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Well, first up there's the normal bone club and bone spear. Good, solid frog-bone, and the spear has an iron tip. Can withstand a lot of punishment, if you know what I mean!",
                ).also {
                    stage++
                }
            3 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "But if you're after something a bit fancier, a bit more sophisticated, there's the bone crossbow and dagger.",
                ).also {
                    stage++
                }
            4 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "These are specially good for getting your prey unawares, but you're not going to get the best out of them unless you've been taught the special technique.",
                ).also {
                    stage++
                }
            5 -> npcl(FaceAnim.OLD_NORMAL, "So, you interested?").also { stage++ }
            6 -> options("What have you got?", "No thanks.", "Why are you whispering?").also { stage++ }
            7 ->
                when (buttonId) {
                    1 -> player("I might be.").also { stage++ }
                    2 -> player("No thanks.").also { stage = 9 }
                }
            8 -> {
                end()
                openNpcShop(player, NPCs.NARDOK_4312)
            }
            9 -> npcl(FaceAnim.OLD_NORMAL, "Yeah, you stay out of trouble.").also { stage = END_DIALOGUE }
            10 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Well, y'know, it's not the most reputable profession, is it? Selling weapons?",
                ).also {
                    stage++
                }
            11 -> player("Why not?").also { stage++ }
            12 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Well, they're used to hurt people, aren't they? That's not considered very, y'know, respectable. Even though the guards use them, and the hunters, they're still considered a bit, y'know, dirty",
                ).also {
                    stage++
                }
            13 -> npcl(FaceAnim.OLD_NORMAL, "Isn't it like that where you come from?").also { stage++ }
            14 -> player("No, on the surface weapon-making is considered", "a noble profession.").also { stage++ }
            15 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "I don't know, you surface people are strange!",
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return NardokDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.NARDOK_4312)
    }
}
