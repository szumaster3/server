package content.region.kandarin.quest.grail.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

class FishermanDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.FISHERMAN_219)

        when (stage) {
            0 -> npcl(FaceAnim.NEUTRAL, "Hi! I don't get many visitors here!").also { stage++ }
            1 ->
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "How's the fishing?", 2),
                    Topic(FaceAnim.NEUTRAL, "Any idea how to get into the castle?", 5),
                    Topic(FaceAnim.NEUTRAL, "Yes, well, this place is a dump.", 10),
                )
            2 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Not amazing. Not many fish can live in this gungey stuff. I remember when this was a pleasant river teeming with every sort of fish...",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            5 -> npcl(FaceAnim.NEUTRAL, "Why, that's easy!").also { stage++ }
            6 -> npcl(FaceAnim.NEUTRAL, "Just ring one of the bells outside.").also { stage++ }
            7 -> playerl(FaceAnim.NEUTRAL, "...I didn't see any bells.").also { stage++ }
            8 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "You must be blind then. There's ALWAYS bells there when I go to the castle.",
                ).also {
                    GroundItemManager.create(Item(Items.GRAIL_BELL_17, 1), Location.create(2762, 4694, 0), player!!)
                    stage = END_DIALOGUE
                }
            10 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "This place used to be very beautiful, however, as our king grows old and weak, the land seems to be dying too.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
    }
}
