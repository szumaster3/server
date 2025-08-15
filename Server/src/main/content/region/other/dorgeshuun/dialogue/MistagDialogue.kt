package content.region.other.dorgeshuun.dialogue

import content.region.misthalin.lumbridge.quest.lost_tribe.dialogue.MistagLostTribeDialogue
import content.region.misthalin.lumbridge.quest.lost_tribe.plugin.GoblinFollower
import content.region.other.dorgeshuun.quest.dttd.dialogue.MistagDialogueFile
import core.api.getAttribute
import core.api.getQuestStage
import core.api.inInventory
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Mistag dialogue.
 */
@Initializable
class MistagDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val ltStage = getQuestStage(player, Quests.THE_LOST_TRIBE)

        if (args.size > 1 && args[1] == "greeting") {
            npc(FaceAnim.OLD_NORMAL, "A human knows ancient greeting?")
            loadFile(MistagLostTribeDialogue(true, ltStage))
            return true
        }
        if (!getAttribute(player, "mistag-greeted", false)) {
            npc(FaceAnim.OLD_NORMAL, "Who...who are you? How did you get in here?")
            stage = -100
            return true
        }

        if (ltStage == 45) {
            npc(FaceAnim.OLD_NORMAL, "Greetings, friend. I am sorry I panicked when I saw you.")
            loadFile(MistagLostTribeDialogue(false, ltStage))
            return true
        } else if (ltStage == 50) {
            npc(FaceAnim.OLD_NORMAL, "Hello, friend?")
            loadFile(MistagLostTribeDialogue(false, ltStage))
            return true
        }
        npc(FaceAnim.OLD_NORMAL, "Hello friend!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            -100 -> npc(FaceAnim.OLD_NORMAL, "Help! A surface dweller this deep in our mines? We will", "all be destroyed!").also { stage++ }
            -99 -> end()
            START_DIALOGUE -> {
                if (getQuestStage(player, Quests.THE_LOST_TRIBE) == 100 && getQuestStage(player, Quests.DEATH_TO_THE_DORGESHUUN) == 0) {
                    npc(FaceAnim.OLD_NORMAL, "It is good to see you again! The Dorgeshuun Council would like to ask a favour of you, if you are interested.").also { stage++ }
                } else {
                    options("Can I sell you some ore?", "Why do the dorgeshuun live underground?", "What happened to your arm?", "Can you show me the way out of the mines?").also { stage = 2 }
                }
            }
            1 -> options("What is this favour?", "Can I sell you some ore?", "Why do the dorgeshuun live underground?", "What happened to your arm?", "Can you show me the way out of the mines?").also { stage = 3 }
            2 -> when (buttonId) {
                1 -> player("Can I sell you some ore?").also { stage = 4 }
                2 -> player("Why do the dorgeshuun live underground?").also { stage = 8 }
                3 -> player("What happened to your arm?").also { stage = 5 }
                4 -> player("Can you show me the way out of the mines?").also { stage = 7 }
            }
            3 -> when (buttonId) {
                1 -> player("What is this favour?").also {
                    end()
                    openDialogue(player, MistagDialogueFile(), npc)
                }

                2 -> player("Can I sell you some ore?").also { stage = 4 }
                3 -> player("Why do the dorgeshuun live underground?").also { stage = 8 }
                4 -> player("What happened to your arm?").also { stage = 5 }
                5 -> player("Can you show me the way out of the mines?").also { stage = 7 }
            }
            4 -> {
                if (inInventory(player, Items.SILVER_ORE_442) || inInventory(player, Items.IRON_ORE_440)) {
                    npc(FaceAnim.OLD_NORMAL, "Certainly. I will buy any iron or silver ore you mine.", "Speak to me again when you have some.").also { stage = END_DIALOGUE }
                } else {
                    npc(FaceAnim.OLD_NORMAL, "Certainly. I will buy any iron or silver ore you mine.", "Speak to me again when you have some.").also { stage = END_DIALOGUE }
                }
            }
            5 -> npcl(FaceAnim.OLD_NORMAL, "I lost it in a mining accident a few years ago. This area is very unstable. That's why we put markers on the wall showing the safest path.").also { stage = END_DIALOGUE }
            6 -> npc(FaceAnim.OLD_NORMAL, "Certainly, friend!").also { stage = END_DIALOGUE }
            7 -> npc(FaceAnim.OLD_NORMAL, "Certainly. Come back soon!").also {
                GoblinFollower.sendToLumbridge(player)
                stage = END_DIALOGUE
            }
            8 -> npcl(FaceAnim.OLD_NORMAL, "Our ancient legends say that goblins were created by an evil god in order to fight in a huge way. This god sent the Dorgeshuun tribe to fight a battle where all would die.").also { stage++ }
            9 -> npcl(FaceAnim.OLD_NORMAL, "But our ancestors escaped by hiding in a deep hole in the ground where our god could not find us. Eventually an earthquake sealed the hole and they were forever safe.").also { stage++ }
            10 -> options("The war is over now, so you can return to the surface.", "What was the name of your god?").also { stage++ }
            11 -> when (buttonId) {
                1 -> playerl(FaceAnim.NEUTRAL, "The war is over now, so you can return to the surface.").also { stage = 12 }
                2 -> playerl(FaceAnim.HALF_ASKING, "What was the name of your god?").also { stage = 14 }
            }
            12 -> npcl(FaceAnim.OLD_NORMAL, "Even if the gods are no longer at war, I find it hard to believe that a tribe of peaceful goblins would be safe above. I have heard of humans coming down into the daves to slaughter our people!").also { stage++ }
            13 -> npcl(FaceAnim.OLD_NORMAL, "But even if we could return, we would not want to. We are adapted to living underground, and we have built a home for ourselves that we do not want to leave.").also { stage = END_DIALOGUE }
            14 -> npcl(FaceAnim.OLD_NORMAL, "Our ancestors did not speak his name, thinking that to do so would attract his attention. His name has been unspoken for so long that it is now entirely forgotten.").also { stage++ }
            15 -> npcl(FaceAnim.OLD_NORMAL, "That is for the best. We have survived perfectly well without the gods, and we do not want a return to the old ways.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = MistagDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.MISTAG_2084)
}
