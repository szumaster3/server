package content.region.karamja.quest.mm.dialogue

import content.region.kandarin.quest.grandtree.dialogue.ShipyardWorkerDialogue
import core.api.quest.getQuestStage
import core.api.sendItemDialogue
import core.game.dialogue.DialogueFile
import core.game.global.action.DoorActionHandler
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.Quests

class ShipyardWorkerMMDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (getQuestStage(player!!, Quests.MONKEY_MADNESS)) {
            0 -> ShipyardWorkerDialogue()
            10 ->
                when (stage) {
                    0 -> npcl("Hey you! What are you up to?").also { stage++ }
                    1 -> playerl("I'm trying to open the gate!").also { stage++ }
                    2 -> npcl("I can see that! Why?").also { stage++ }
                    3 -> playerl("I am on a special mission for King Narnode of the Gnomes.").also { stage++ }
                    4 -> npcl("Narnode? I don't believe you. He wouldn't send a human!").also { stage++ }
                    5 -> playerl("Well, he did...").also { stage++ }
                    6 -> npcl("Tough.").also { stage++ }
                    7 -> playerl("Look, I have the Gnome Royal Seal.").also { stage++ }
                    8 -> {
                        end()
                        if (player!!.inventory.containsItem(Item(Items.GNOME_ROYAL_SEAL_4004))) {
                            sendItemDialogue(
                                player!!,
                                Items.GNOME_ROYAL_SEAL_4004,
                                "You show the shipyard worker the Royal seal.",
                            ).also { stage = 9 }
                        } else {
                            sendItemDialogue(
                                player!!,
                                Items.GNOME_ROYAL_SEAL_4004,
                                "You don't actually have the Royal Seal.",
                            ).also { stage = END_DIALOGUE }
                        }
                    }

                    9 -> npcl("Wow. I haven't seen one of these since...").also { stage++ }
                    10 -> playerl("Since when?").also { stage++ }
                    11 -> npcl("... since Glough used to visit.").also { stage++ }
                    12 ->
                        npcl(
                            "Anyway. Please step inside, ${if (player!!.isMale) "mister" else "miss"}",
                        ).also { stage++ }
                    13 -> {
                        stage = END_DIALOGUE
                        DoorActionHandler.autowalkFence(
                            player!!,
                            Scenery(2438, Location(2945, 3041, 0)),
                            3727,
                            3728,
                        )
                        end()
                    }
                }
        }
    }
}
