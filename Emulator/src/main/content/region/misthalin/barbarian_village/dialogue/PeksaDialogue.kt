package content.region.misthalin.barbarian_village.dialogue

import core.api.interaction.openNpcShop
import core.api.quest.isQuestInProgress
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Peksa dialogue.
 *
 * Relations:
 * - [Scorpion Catcher quest][content.region.kandarin.quest.scorpcatcher.ScorpionCatcher]
 */
@Initializable
class PeksaDialogue(player: Player? = null) : Dialogue(player) {
    

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Are you interested in buying or selling a helmet?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> if (isQuestInProgress(player, Quests.SCORPION_CATCHER, 10, 99)) {
                options("I could be, yes.", "No, I'll pass on that.", "I've heard you have a small scorpion in your possession.").also { stage++ }
            } else {
                options("I could be, yes.", "No, I'll pass on that.").also { stage++ }
            }

            1 -> when (buttonId) {
                1 -> end().also { openNpcShop(player, NPCs.PEKSA_538) }
                2 -> player(FaceAnim.HALF_GUILTY, "No, I'll pass on that.").also { stage++ }
                3 -> npcl(FaceAnim.THINKING, "Now how could you know about that, I wonder? Mind you, I don't have it anymore.").also { stage = 3 }
            }
            2 -> npc(FaceAnim.HALF_GUILTY, "Well, come back if you change your mind.").also { stage = END_DIALOGUE }
            3 -> npcl(FaceAnim.NEUTRAL,"I gave it as a present to my brother Ivor when I visited our outpost in the west.").also { stage++ }
            4 -> npcl(FaceAnim.NEUTRAL,"Well, actually I hid it in his bed so it would nip him. It was a bit of a surprise gift.").also { stage++ }
            5 -> options("So where's this outpost?", "Thanks for the information.").also { stage++ }
            6 -> when (buttonId) {
                1 -> playerl(FaceAnim.HALF_ASKING, "So where's this outpost?").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "Thanks for the information.").also { stage = 8 }
            }
            7 -> npcl(FaceAnim.NEUTRAL,"Its a fair old trek to the west, across the White Wolf Mountains. Then head west, north-west until you see the axes and horned helmets.").also { stage++ }
            8 -> npcl(FaceAnim.NEUTRAL,"No problems! Tell Ivor I said hi!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = PeksaDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.PEKSA_538)
}
