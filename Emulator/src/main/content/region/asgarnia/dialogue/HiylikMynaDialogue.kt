package content.region.asgarnia.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class HiylikMynaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("Hello friend. What can I do for you?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Can you tell me why you're here?",
                    "Why is there a mini-game sign here?",
                    "Ok, thanks.",
                ).also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Can you tell me why you're here?").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "Why is there a mini-game sign here?").also { stage = 9 }
                    3 -> player(FaceAnim.HALF_GUILTY, "Ok, thanks.").also { stage = END_DIALOGUE }
                }
            2 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Of course. I plan to assist the forces of Saradomin who",
                    "will come out to aid, and fight against the legion of",
                    "vamypyres that infest this foul place. These brave",
                    "mercenaries shall need a guide.",
                ).also {
                    stage++
                }
            3 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "When a guide arrives, I will aid the mercenary by",
                    "holding their wealth for their eventual return. The",
                    "mercenary will repay the guide by giving him a token",
                    "for items which I am holding.",
                ).also {
                    stage++
                }
            4 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I shall give such items to the guide as a payment for their",
                    "services once I'm handed the token.",
                ).also {
                    stage++
                }
            5 -> player(FaceAnim.HALF_GUILTY, "What items will you hold?").also { stage++ }
            6 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Oh, the range of items is very varied. Mercenaries tend",
                    "to hold raw materials in high esteem and so will deposit",
                    "those with me. They also seem to be good battering",
                    "items.",
                ).also {
                    stage++
                }
            7 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Generally, things like uncooked lobsters, coal, silver bars,",
                    "bow strings. Things which are useful! Plus the odd",
                    "tome of experience, which are considered incredibly",
                    "useful.",
                ).also {
                    stage++
                }
            8 -> player(FaceAnim.HALF_GUILTY, "Okay, thanks.").also { stage = END_DIALOGUE }
            9 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Mini-game sign? Surely sir it is confused and not at all",
                    "in their right mind.",
                ).also {
                    stage++
                }
            10 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "This is the collection area for the forces of Saradomin.",
                    "If you know of the plight of the Myreque, you should",
                    "understand what is at stake! Aid the Myreque if you",
                    "can and attempt to draw back the darkness of",
                ).also {
                    stage++
                }
            11 -> npc(FaceAnim.HALF_GUILTY, "Hallowvale.").also { stage++ }
            12 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Then perhaps you can then be a guiding light for the",
                    "forces of Saradomin, as they enter the lair of the beast.",
                ).also {
                    stage++
                }
            13 -> player(FaceAnim.HALF_GUILTY, "Ok, thanks.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return HiylikMynaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HIYLIK_MYNA_1514)
    }
}
