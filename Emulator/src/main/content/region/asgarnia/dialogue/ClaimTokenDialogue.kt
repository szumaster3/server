package content.region.asgarnia.dialogue

import core.api.hasSpaceFor
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.NPCs

class ClaimTokenDialogue(private val npcId: NPC) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = npcId

        val amount = player!!.getSavedData().activityData.warriorGuildTokens
        val faceAnimation = if(npc!!.id == NPCs.GAMFRED_4287) FaceAnim.CHILD_NORMAL else FaceAnim.HALF_GUILTY
        when (stage) {
            0 -> player("May I claim my tokens please?").also { stage++ }
            1 -> if (amount < 1) {

                npc(faceAnimation,
                    "I'm afraid you have not earned any tokens yet. Try",
                    "some of the activities around the guild to earn some.",
                )
                stage = 3
            } else {
                npc(faceAnimation,"Of course! Here you go, you've earned $amount tokens!")
                stage++
            }

            2 -> {
                val item = Item(Items.WARRIOR_GUILD_TOKEN_8851, amount)
                if (!hasSpaceFor(player!!, item)) {
                    player("Sorry, I don't seem to have enough inventory space.")
                    stage++
                }
                player!!.getSavedData().activityData.warriorGuildTokens = 0
                player!!.inventory.add(item)
                player("Thanks!")
                stage++
            }

            3 -> end()
            4 -> {
                player("Ok, I'll go see what I can find.")
                stage--
            }
        }
    }
}