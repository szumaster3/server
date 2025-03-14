package content.minigame.mta.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class CharmedWarriorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when (npc.id) {
            NPCs.CHARMED_WARRIOR_3105, NPCs.CHARMED_WARRIOR_3104 -> player("Is there anybody there?")
            else -> player("Hello?")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (npc.id) {
            NPCs.CHARMED_WARRIOR_3107 ->
                when (stage) {
                    0 -> npc(FaceAnim.OLD_NORMAL, "Can't you see I'm busy?").also { stage++ }
                    1 -> player("Well, I can't really see YOU.").also { stage = END_DIALOGUE }
                }

            NPCs.CHARMED_WARRIOR_3106 ->
                when (stage) {
                    0 ->
                        npc(
                            FaceAnim.OLD_NORMAL,
                            "Hey! You haven't paid for your Magic Training Arena",
                            "Membership money!",
                        ).also {
                            stage++
                        }
                    1 -> player("You're lying. I can see right through you!").also { stage++ }
                    2 -> npc(FaceAnim.OLD_NORMAL, "Oh, HA HA, very funny.").also { stage = END_DIALOGUE }
                }

            NPCs.CHARMED_WARRIOR_3105 ->
                when (stage) {
                    0 -> npc(FaceAnim.OLD_NORMAL, "Wooo wooo! Be afraid for I'm a scary ghost. Wooo!").also { stage++ }
                    1 -> player("Er, whatever.").also { stage = END_DIALOGUE }
                }

            NPCs.CHARMED_WARRIOR_3104 ->
                when (stage) {
                    0 -> npc(FaceAnim.OLD_NORMAL, "What do you think?").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.CHARMED_WARRIOR_3104,
            NPCs.CHARMED_WARRIOR_3105,
            NPCs.CHARMED_WARRIOR_3106,
            NPCs.CHARMED_WARRIOR_3107,
        )
    }
}
