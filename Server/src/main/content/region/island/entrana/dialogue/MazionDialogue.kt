package content.region.island.entrana.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

class MazionDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val handProgress = getQuestStage(player, Quests.THE_HAND_IN_THE_SAND)

        if (handProgress == 12) {
            player("Hello there!")
            return true
        }

        if (inInventory(player, Items.WIZARDS_HEAD_6957)) {
            npcl(FaceAnim.ANNOYED, "I see you still have that head! Take it back to the Wizards in Yanille!")
            stage = END_DIALOGUE
            return true
        }

        if (handProgress == 13 && !inInventory(player, Items.WIZARDS_HEAD_6957)) {
            npcl(FaceAnim.ANNOYED, "Hello again ${player.name}!")
            stage = 8
            return true
        }

        when ((1..3).random()) {
            1 -> npcl(FaceAnim.FRIENDLY, "Nice weather we're having today!").also { stage = END_DIALOGUE }
            2 -> npcl(FaceAnim.FRIENDLY, "Hello ${player.name}, fine day today!").also { stage = END_DIALOGUE }
            3 -> npcl(FaceAnim.FRIENDLY, "Please leave me alone, a parrot stole my banana.").also { stage = END_DIALOGUE }
        }

        stage = END_DIALOGUE
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.HALF_ASKING, "Uh...greetings ${player.name}!").also { stage++ }
            1 -> player(FaceAnim.HALF_ASKING, "Uhh... How do you know my name?").also { stage++ }
            2 -> npcl(FaceAnim.HALF_ASKING, "Oh, I like to keep ahead of things.").also { stage++ }
            3 -> player(FaceAnim.HALF_ASKING, "Err.. ok. Well, I've been sent from the Wizards' Guild in Yanille. There's been an... incident... Do you have any body parts?").also { stage++ }
            4 -> npcl(FaceAnim.HALF_ASKING, "How did you know! I found the most awful thing in my sandpit - a head!").also { stage++ }
            5 -> player(FaceAnim.HALF_ASKING, "Ahhh good! I need to take it back to be buried!").also { stage++ }
            6 -> npcl(FaceAnim.HALF_ASKING, "You're very strange, but if it means I get rid of the horrid thing...").also { stage++ }
            7 -> if(freeSlots(player) == 0) {
                npcl(FaceAnim.NEUTRAL, "Or I would if you had any room in your bag!").also { stage = END_DIALOGUE }
            } else {
                sendItemDialogue(player, Items.WIZARDS_HEAD_6957, "Mazion gives you the head.")
                addItem(player, Items.WIZARDS_HEAD_6957, 1)
                setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, 13)
                stage = END_DIALOGUE
            }

            8 -> showTopics(
                Topic("What should I do with the head?", 8),
                Topic("I've lost my head!", 10)
            )
            9 -> npcl(FaceAnim.ANNOYED, "It was you that came demanding the head to give to the wizard in the first place! Go back to Yanille with it!").also { stage = 8 }
            10 -> if(freeSlots(player) == 0) {
                npcl(FaceAnim.NEUTRAL, "You dropped it and you don't have room to carry it now, come back when you do.").also { stage = END_DIALOGUE }
            } else {
                npcl(FaceAnim.FRIENDLY, "Keep your hair on! You dropped it! Make sure you take it straight back to the wizards else you won't have a leg to stand on.").also { stage++ }
            }
            11 -> {
                sendItemDialogue(player, Items.WIZARDS_HEAD_6957, "Mazion gives you the head.")
                addItem(player, Items.WIZARDS_HEAD_6957, 1)
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = MazionDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.MAZION_3114)

}