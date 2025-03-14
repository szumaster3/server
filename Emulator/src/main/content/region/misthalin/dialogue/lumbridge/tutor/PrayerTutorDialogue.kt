package content.region.misthalin.dialogue.lumbridge.tutor

import core.api.sendItemDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class PrayerTutorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        options("I already know about the basic prayers, got any tips?", "Tell me about different bones.", "Goodbye.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                when (buttonId) {
                    1 ->
                        player(
                            FaceAnim.ASKING,
                            "I already know about the basic prayers, got any tips?",
                        ).also { stage = 10 }
                    2 -> player(FaceAnim.HALF_ASKING, "Tell me about different bones.").also { stage = 20 }
                    3 -> player(FaceAnim.FRIENDLY, "Goodbye.").also { stage = END_DIALOGUE }
                }
            10 ->
                npc(
                    FaceAnim.HAPPY,
                    "For you " + player.username + "? Always. There are many",
                    "advantages to using the protection prayers when",
                    "fighting the more dangerous foes. You can protect",
                    "yourself from magic, melee or ranged attacks with these",
                ).also {
                    stage++
                }
            11 -> npc(FaceAnim.HAPPY, "useful prayers.").also { stage++ }
            12 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "A good prayer to have when venturing into the",
                    "wilderness is protect item. This will protect one of your",
                    "items if you should die there.",
                ).also {
                    stage++
                }
            13 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "Remember though that venturing into the wilderness is",
                    "a risky business, store your items in a bank before you",
                    "go there so that you don't lose them.",
                ).also {
                    stage++
                }
            14 ->
                options(
                    "I already know about the basic prayers, got any tips?",
                    "Tell me about different bones.",
                    "Goodbye.",
                ).also {
                    stage =
                        0
                }
            20 -> options("Basic Bones", "Big Bones", "Babydragon Bones", "Goodbye.").also { stage++ }
            21 ->
                when (buttonId) {
                    1 ->
                        sendItemDialogue(
                            player,
                            Items.BONES_526,
                            "Basic bones are left by many creatures such as goblins, monkeys and that sort of thing. They won't get you much when you bury them, but if you do it every time you come across them, it all adds up!",
                        ).also {
                            stage++
                        }
                    2 ->
                        sendItemDialogue(
                            player,
                            Items.BIG_BONES_532,
                            "Big bones you can get by killing things like ogres and giants, them being big things and all. They're quite a good boost for your prayer if you are up to fighting the big boys.",
                        ).also {
                            stage++
                        }
                    3 ->
                        sendItemDialogue(
                            player,
                            Items.DRAGON_BONES_536,
                            "If you're feeling adventurous and up to tackling baby dragons, you can get these Baby Dragon bones which are even better than big bones.",
                        ).also {
                            stage =
                                20
                        }
                    4 -> player(FaceAnim.FRIENDLY, "Goodbye.").also { stage = END_DIALOGUE }
                }
            22 ->
                sendItemDialogue(
                    player,
                    Items.BIG_BONES_532,
                    "You can probably find them in caves and such dark dank places.",
                ).also {
                    stage =
                        20
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PRAYER_TUTOR_4903)
    }
}
