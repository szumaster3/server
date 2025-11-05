package content.region.kandarin.ardougne.dialogue

import content.region.kandarin.ardougne.quest.cog.dialogue.BrotherKojoDialogueFile
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Brother Kojo dialogue.
 */
@Initializable
class BrotherKojoDialogue(player: Player? = null) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val hasCompletedClockTowerQuest = isQuestComplete(player, Quests.CLOCK_TOWER)

        if(!hasCompletedClockTowerQuest) {
            openDialogue(player!!, BrotherKojoDialogueFile(), npc)
            return true
        }

        if(!inInventory(player, Items.WATCH_2575)) {
            playerl(FaceAnim.NEUTRAL, "Hello.")
        } else {
            npcl(FaceAnim.HAPPY, "Oh hello there traveller. You've done a grand job with the clock. It's just like new.")
            stage = 9
        }
        return true
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            0 -> npcl(FaceAnim.FRIENDLY, "Hello, traveller, how can I help?").also { stage++ }
            1 -> playerl(FaceAnim.NEUTRAL, "I'm trying to learn how to be a navigator.").also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY, "I don't know if I can help you there.").also { stage++ }
            3 -> playerl(FaceAnim.NEUTRAL, "The professor from the Observatory says that I need a watch.").also { stage++ }
            4 -> npcl(FaceAnim.FRIENDLY, "Ah, that I can help you with. I've been tinkering with this new idea of a watch and made a few.").also { stage++ }
            5 -> npcl(FaceAnim.FRIENDLY, "The problem is the villagers don't see the point as they have the Clock Tower!").also { stage++ }
            6 -> playerl(FaceAnim.NEUTRAL, "Can I have one?").also { stage++ }
            7 -> npcl(FaceAnim.FRIENDLY, "You can have this one! It's the display model.").also { stage++ }
            8 -> {
                if (freeSlots(player) == 0) {
                    npcl(FaceAnim.NEUTRAL, "You don't have enough space for the watch. Come back to me when you do.")
                    return true
                }
                sendItemDialogue(player, Items.WATCH_2575, "Brother Kojo has given you a watch.")
                addItem(player, Items.WATCH_2575)
                stage = 9
            }
            9 -> end()
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BROTHER_KOJO_223)
}
