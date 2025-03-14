package content.region.asgarnia.dialogue.dwarvenmines

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class HuraDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_DEFAULT, "'Ello " + player.username + ".")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.HALF_GUILTY, "Hello, what's that you've got there?").also { stage++ }
            1 -> npc(FaceAnim.OLD_DEFAULT, "A crossbow, are you interested?").also { stage++ }
            2 -> player(FaceAnim.HALF_GUILTY, "Maybe, are they any good?").also { stage++ }
            3 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Are they any good?! They're dwarven engineering at",
                    "its best!",
                ).also { stage++ }
            4 ->
                options(
                    "How do I make one for myself?",
                    "What about ammo?",
                    "Thanks for telling me. Bye!",
                ).also { stage++ }
            5 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "How do I make one for myself?").also { stage = 10 }
                    2 -> player(FaceAnim.HALF_GUILTY, "What about ammo?").also { stage = 20 }
                    3 -> player(FaceAnim.HALF_GUILTY, "Thanks for telling me. Bye!").also { stage = END_DIALOGUE }
                }
            10 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Well, firstly you'll need to chop yourself some wood,",
                    "then use a knife on the wood to whittle out a nice",
                    "crossbow stock like these here.",
                ).also {
                    stage++
                }
            11 -> player(FaceAnim.HALF_GUILTY, "Wood fletched into stock... check.").also { stage++ }
            12 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Then get yourself some metal and a hammer and smith",
                    "yourself some limbs for the bow, mind that you use the",
                    "right metals and wood though as some wood is too light",
                    "to use with some metal and vice versa.",
                ).also {
                    stage++
                }
            13 -> player(FaceAnim.HALF_GUILTY, "Which goes with which?").also { stage++ }
            14 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Wood and Bronze as they're basic materials, Oak and",
                    "Blurite, Willow and Iron, Steel and Teak, Mithril and",
                    "Maple, Adamantite and Mahogany and finally Runite",
                    "and Yew.",
                ).also {
                    stage++
                }
            15 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "Ok, so I have my stock and a pair of limbs... what now?",
                ).also { stage++ }
            16 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Simply take a hammer and smack the limbs firmly onto",
                    "the stock. You'll need a string, only they're not",
                    "the same as normal bows. You'll need to dry some large",
                    "animal's meat to get sinew, then spin that on a spinning",
                ).also {
                    stage++
                }
            17 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "wheel, it's the only thing we've found to be strong",
                    "enough for a crossbow.",
                ).also {
                    stage++
                }
            18 -> player(FaceAnim.HALF_GUILTY, "Thanks for telling me. Bye!").also { stage = END_DIALOGUE }
            20 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "You can smith yourself lots of different bolts, don't",
                    "forget to flight them with feathers like you do arrows",
                    "though. You can poison any untipped bolt but there's",
                    "also the option of tipping them with gems then",
                ).also {
                    stage++
                }
            21 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "enchanting them with runes. This can have some pretty",
                    "powerful effects.",
                ).also {
                    stage++
                }
            22 -> player(FaceAnim.HALF_GUILTY, "Oh my poor bank, how will I store all those?!").also { stage++ }
            23 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Find Hirko in Keldagrim, he also sells crossbow parts",
                    "and I'm sure he has something you can use to store",
                    "bolts in.",
                ).also {
                    stage++
                }
            24 -> player(FaceAnim.HALF_GUILTY, "Thanks for the info.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return HuraDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HURA_4563)
    }
}
