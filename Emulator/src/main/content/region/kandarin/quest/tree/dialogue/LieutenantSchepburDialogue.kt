package content.region.kandarin.quest.tree.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class LieutenantSchepburDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.OLD_DEFAULT, "Move into position lads! eh? Who are you and what do you want?").also { stage++ }
        return true
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Move into position lads! eh? Who are you and what do you want?",
                ).also { stage++ }
            1 -> playerl(FaceAnim.ASKING, "Who are you then?").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Lieutenant Schepbur, commanding officer of the new Armoured Tortoise Regiment.",
                ).also {
                    stage++
                }
            3 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "There's only two tortoises here, that's hardly a regiment.",
                ).also { stage++ }
            4 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "This is just the beginning! Gnome breeders and trainers are already working to expand the number of units. Soon we'll have hundreds of these beauties, nay thousands! And they will not only carry mages and",
                ).also {
                    stage++
                }
            5 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "archers but other fiendish weapons of destruction of gnome devising. An army of giant tortoises will march upon this battlefield and rain the fire of our wrath upon all our enemies! Nothing will be able to stop us!",
                ).also {
                    stage++
                }
            6 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Oooookayy...... I'll leave you to it then....",
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LIEUTENANT_SCHEPBUR_3817)
    }
}
