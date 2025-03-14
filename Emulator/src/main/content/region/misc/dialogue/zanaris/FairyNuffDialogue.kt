package content.region.misc.dialogue.zanaris

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class FairyNuffDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.OLD_DEFAULT, "Hello, can I help you at all?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("What do you do around here?", "No thanks. Just looking around.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HALF_ASKING, "What do you do around here?").also { stage++ }
                    2 -> playerl(FaceAnim.HALF_GUILTY, "No thanks. Just looking around.").also { stage = 12 }
                }
            2 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Oh, I'm the resident healer. My name is Nuff, Fairy Nuff. Who are you?",
                ).also {
                    stage++
                }
            3 -> playerl(FaceAnim.HALF_GUILTY, "My name is " + player.username + ".").also { stage++ }
            4 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Pleased to meet you, Player. As I was saying, I'm the healer in Zanaris. I graduated over 200 human years ago. Look, there's my Advanced Healing Certificate on the wall. I never go anywhere without it. It's my job to look after the sick, wounded or dying fairies around here.",
                ).also {
                    stage++
                }
            5 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Of course, fairies being magical by nature, not many of them get hurt, so I don't normally have much to do.",
                ).also {
                    stage++
                }
            6 ->
                playerl(
                    FaceAnim.HALF_ASKING,
                    "What about healing humans? Lots of adventurers must get hurt fighting the Otherworldly beings.",
                ).also {
                    stage++
                }
            7 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Yes, they do. Silly isn't it? I do use my healing magic on humans sometimes, but I don't like them attacking our other guests, so I charge them for the service.",
                ).also {
                    stage++
                }
            8 -> playerl(FaceAnim.HALF_ASKING, "How much do you charge to heal someone, then?").also { stage++ }
            9 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Well, the gold that you humans are so fond of has no real value in Zanaris, so I ask my patients to pay me in gems. I will heal 4 life points for an opal, 5 life points for either a sapphire or a piece of jade, 7 life points for an emerald or red topaz, 9 for a ruby and 11 for a diamond.",
                ).also {
                    stage++
                }
            10 ->
                playerl(
                    FaceAnim.HALF_ASKING,
                    "Only 11 life points for a diamond? Doesn't that seem a bit steep?",
                ).also {
                    stage++
                }
            11 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Maybe to you, but you'd be surprised how many adventurers are willing to pay those prices.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            12 ->
                npcl(FaceAnim.OLD_DEFAULT, "Okay. If you want to know anything, just ask.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return FairyNuffDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FAIRY_NUFF_3303, NPCs.FAIRY_NUFF_4434, NPCs.FAIRY_NUFF_4435, NPCs.FAIRY_NUFF_4436)
    }
}
