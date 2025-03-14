package content.region.desert.dialogue.sophanem

import core.api.inEquipment
import core.api.sendChat
import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class KlenterDialogue(
    player: Player? = null,
) : Dialogue(player) {
    val forceChat = arrayOf("Agghhh!", "Aieee!", "Waghhhh!", "Wooo!")
    val forceDialogue =
        arrayOf(
            "You worm! May the Devourer take your soul.",
            "Give me my guts back, thief.",
            "You pathetic excuse for a thief.",
            "Return what is mine.",
            "Grave defiler.",
            "Thief.",
            "Evil doer.",
            "Spawn of evil.",
            "Graverobber.",
            "Organ snatcher.",
        )

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!inEquipment(player, Items.GHOSTSPEAK_AMULET_552)) {
            sendDialogue(
                player,
                "The spirit tries to converse with you, but it makes no sense. All you can understand from him is his deepest anger which is directed towards you.",
            ).also {
                stage =
                    END_DIALOGUE
            }
        } else {
            npc(FaceAnim.ANGRY, "You foul thief, return what is mine.")
        }
        sendChat(npc, forceChat.random())
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.ASKING, "What?").also { stage++ }
            1 -> npc(FaceAnim.ANNOYED, "You heard me, restore what you have stolen from", "my tomb!").also { stage++ }
            2 -> player(FaceAnim.STRUGGLE, "But...").also { stage++ }
            3 -> npc(forceDialogue.random()).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KLENTER_2014)
    }
}
