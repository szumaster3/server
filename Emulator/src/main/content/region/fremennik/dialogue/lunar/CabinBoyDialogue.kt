package content.region.fremennik.dialogue.lunar

import core.api.addItemOrDrop
import core.api.inInventory
import core.api.sendChat
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class CabinBoyDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (inInventory(player, Items.EMERALD_LENS_9066, 1) || player.bank.contains(Items.EMERALD_LENS_9066, 1)) {
            playerl(FaceAnim.FRIENDLY, "So you've plucked up the courage to come and confront that girl!").also {
                stage =
                    0
            }
        } else {
            player(FaceAnim.FRIENDLY, "Hi.").also { stage = 20 }
        }

        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.CHILD_FRIENDLY, "That I did, that I did!").also { stage++ }
            1 -> player(FaceAnim.ASKING, "And?").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.CHILD_FRIENDLY,
                    "She turned out to be really nice! She's joining us to become a pirate!",
                ).also {
                    stage++
                }
            3 ->
                playerl(
                    FaceAnim.HALF_WORRIED,
                    "Really? And you're not sceptical about this? You know, after what she did last time?",
                ).also {
                    stage++
                }
            4 -> npc(FaceAnim.CHILD_SUSPICIOUS, "She---has---no---spell---on---me.").also { stage++ }
            5 -> player(FaceAnim.HALF_THINKING, "Huh? Why are you talking like that?").also { stage++ }
            6 -> npc(FaceAnim.CHILD_THINKING, "Like what?").also { stage++ }
            7 ->
                playerl(
                    FaceAnim.HALF_ASKING,
                    "Like someone else was talking for you. Are you sure she hasn't put a spell on you again? Did she wave a watch in front of your face? You know, tell you to look deep into her eyes?",
                ).also {
                    stage++
                }
            8 -> npc(FaceAnim.CHILD_SUSPICIOUS, "She---has---no---spell---on---me.").also { stage++ }
            9 ->
                playerl(
                    FaceAnim.HALF_THINKING,
                    "I think you've been hypnotised. I wonder what happens if I click my fingers?",
                ).also {
                    sendChat(player, "*click*").also { stage++ }
                }
            10 -> npc(FaceAnim.CHILD_SAD, "*Cluck* *cluck* *bwaarrk*").also { stage++ }
            11 -> npc(FaceAnim.CHILD_SAD, "").also { stage++ }
            12 ->
                player(FaceAnim.WORRIED, "Oh dear. Oh well, I'm sure you'll learn one day.").also {
                    stage =
                        END_DIALOGUE
                }
            20 -> npc(FaceAnim.CHILD_FRIENDLY, "I bet you're after another lens!").also { stage++ }
            21 -> player(FaceAnim.HALF_THINKING, "How could you possibly know that?").also { stage++ }
            22 ->
                npcl(
                    FaceAnim.CHILD_FRIENDLY,
                    "Hey, I think I'm learning a thing or two from these Moon Clan ladies.",
                ).also {
                    stage++
                }
            23 -> options("Please", "No thanks").also { stage++ }
            24 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_ASKING, "Oi! I need another lens, boy.").also { stage = 25 }
                    2 -> player(FaceAnim.SUSPICIOUS, "I think I'll pass. But thanks..").also { stage = END_DIALOGUE }
                }
            25 ->
                npc(FaceAnim.CHILD_FRIENDLY, "Huh, oh ok, I suppose I owe you one.").also {
                    end()
                    addItemOrDrop(player, Items.EMERALD_LENS_9066, 1)
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return CabinBoyDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CABIN_BOY_4539)
    }
}
