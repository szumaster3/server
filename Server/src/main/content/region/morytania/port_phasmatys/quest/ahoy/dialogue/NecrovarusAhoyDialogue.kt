package content.region.morytania.port_phasmatys.quest.ahoy.dialogue

import content.region.morytania.port_phasmatys.quest.ahoy.plugin.GhostsAhoyUtils
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

class NecrovarusAhoyDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        val questStage = getQuestStage(player!!, Quests.GHOSTS_AHOY)
        npc = NPC(NPCs.NECROVARUS_1684)
        when (questStage) {
            1 -> when (stage) {
                0 -> player("I must speak with you on behalf of Velorina.").also { stage++ }
                1 -> npc(FaceAnim.ANNOYED, "You dare to speak that name in this place?????").also { stage++ }
                2 -> player("She wants to pass-").also { stage++ }
                3 -> npc(FaceAnim.ANNOYED, "Silence!!").also { stage++ }
                4 -> npc(FaceAnim.ANNOYED, "Or I will incinerate the flesh from your bones!!!").also { stage++ }
                5 -> player("But she-").also { stage++ }
                6 -> npc(FaceAnim.ANGRY, "Get out of my sight!! Or I promise you that you will", "regret your insolence for the rest of eternity!!!").also { stage++ }
                7 -> {
                    end()
                    setQuestStage(player!!, Quests.GHOSTS_AHOY, 2)
                }
            }

            2 -> when (stage) {
                0 -> player("Please, listen to me-").also { stage++ }
                1 -> npc("No - listen to me. Go from this place and do not return,", "or I will remove your head.").also { stage = END_DIALOGUE }
            }

            4 -> when (stage) {
                0 -> player("Wheels have been set in motion, Necrovarus,", "wheels that will set the citizens of Port Phasmatys", "free.").also { stage++ }
                1 -> npc("Oh goody goody. I just can't wait.").also { stage = END_DIALOGUE }
            }

            in 5..10 -> when (stage) {
                0 -> if (getAttribute(player!!, GhostsAhoyUtils.petitioncomplete, false)) {
                    player("Necrovarus, I am presenting you with a petition form", "that has been signed by 10 citizens of Port Phasmatys.").also { stage++ }
                } else {
                    npc(FaceAnim.ANGRY, "Get out of my sight!! Or I promise you that you will", "regret your insolence for the rest of eternity!!!").also { stage = END_DIALOGUE }
                }
                1 -> npc("A Petition you say?", "Continue, mortal.").also { stage++ }
                2 -> player("It says that the citizens of Port Phasmatys should have", "the right to choose whether they pass over into the next", "world or not, and not have this decided by the powers", "that be on their behalf.").also { stage++ }
                3 -> npc("I see.").also { stage++ }
                4 -> player("So you will let them pass over if they wish?").also { stage++ }
                5 -> npc("Oh yes.").also { stage++ }
                6 -> player("Really?").also { stage++ }
                7 -> npc("NO!!!!! Get out of my sight before I burn every ounce", "of flesh from your bones!!!!!").also { stage++ }
                8 -> sendDialogue(player!!, "The petition form turns to ashes in your hand.").also { stage++ }
                9 -> {
                    end()
                    removeItem(player!!, Items.PETITION_FORM_4283)
                    addItemOrDrop(player!!, Items.ASHES_592)
                    sendDialogue(player!!, "In his rage Necrovarus drops a key on the floor.")
                    produceGroundItem(player!!, Items.BONE_KEY_4272, 1, player!!.location)
                }
            }

            80 -> when (stage) {
                0 -> if (inEquipment(player!!, Items.GHOSTSPEAK_AMULET_4250)) {
                    npc("You dare to face me again - you must be truly", "insane!!!!").also { stage++ }
                } else {
                    npc(FaceAnim.ANGRY, "Get out of my sight!! Or I promise you that you will", "regret your insolence for the rest of eternity!!!").also { stage = END_DIALOGUE }
                }
                1 -> player("No, Necrovarus, I am not insane. With this enchanted", "amulet of ghostspeak I have the power to command you", "to do my will!").also { stage++ }
                2 -> {
                    setTitle(player!!, 3)
                    sendOptions(player!!, "What do you want to command Necrovarus to do?", "Let any ghost who so wishes pass on into the next world.", "Tell me a joke.", "Do a chicken impression.").also { stage++ }
                }
                3 -> when (buttonID) {
                    1 -> player("Let any ghost who so wishes pass on into the next world.").also { stage = 14 }
                    2 -> player("Tell me a joke.").also { stage++ }
                    3 -> player("Do a chicken impression.").also { stage = 11 }
                }
                4 -> sendItemDialogue(player!!, Items.GHOSTSPEAK_AMULET_4250, "A beam of intense light radiates out from the amulet of ghostspeak.").also { stage++ }
                5 -> npc("Knock knock").also { stage++ }
                6 -> player("Who's there?").also { stage++ }
                7 -> npc("Egbert.").also { stage++ }
                8 -> player("Egbert who?").also { stage++ }
                9 -> npc("Egbert no bacon.").also { stage++ }
                10 -> sendItemDialogue(player!!, Items.GHOSTSPEAK_AMULET_4250, "Luckily the amulet of ghostspeak does not seem to have fully discharged.").also { stage = 2 }
                11 -> sendItemDialogue(player!!, Items.GHOSTSPEAK_AMULET_4250, "A beam of intense light radiates out from the amulet of ghostspeak.").also { stage++ }
                12 -> npc("Cluck cluck squuuaaaakkkk cluck cluck.", "I think I've laid an egg...").also { stage++ }
                13 -> sendItemDialogue(player!!, Items.GHOSTSPEAK_AMULET_4250, "Luckily the amulet of ghostspeak does not seem to have fully discharged.").also { stage = 2 }
                14 -> sendItemDialogue(player!!, Items.GHOSTSPEAK_AMULET_4250, "A beam of intense light radiates out from the amulet of ghostspeak.").also { stage++ }
                15 -> npc("I - will - let ...").also { stage++ }
                16 -> player("Carry on...").also { stage++ }
                17 -> npc("...any...").also { stage++ }
                18 -> player("Yes?").also { stage++ }
                19 -> npc("... ghost who so wishes ...").also { stage++ }
                20 -> player("I think we're almost getting there...").also { stage++ }
                21 -> npc("... pass into the next world.").also { stage++ }
                22 -> {
                    end()
                    setQuestStage(player!!, Quests.GHOSTS_AHOY, 99)
                }
            }
        }
    }
}
