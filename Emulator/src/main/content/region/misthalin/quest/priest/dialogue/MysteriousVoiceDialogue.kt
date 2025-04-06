package content.region.misthalin.quest.priest.dialogue

import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.sendDialogue
import core.api.sendDialogueLines
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.node.scenery.Scenery
import core.tools.BLUE
import core.tools.END_DIALOGUE
import core.tools.RED
import org.rs.consts.Quests

class MysteriousVoiceDialogue(private val doors: Scenery) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        val questStage = getQuestStage(player!!, Quests.PRIEST_IN_PERIL)
        when (stage) {
            0 -> {
                if (questStage == 10) {
                    sendDialogueLines(
                        player!!,
                        "You knock at the door...You hear a voice from inside.",
                        "${BLUE}Who are you, and what do you want?"
                    ).also { stage = 1 }
                }
                if (questStage == 12) {
                    sendDialogueLines(
                        player!!,
                        "You knock at the door...You hear a voice from inside.",
                        "${BLUE}You again?",
                        "${BLUE}What do you want now?"
                    ).also { stage = 11 }
                }
                if (questStage >= 13) {
                    sendDialogue(player!!, "You knock at the door...The door swings open as you knock.").also { stage = 15 }
                }
            }
            1 -> player(FaceAnim.HALF_GUILTY, "Ummmm.....").also { stage++ }
            2 -> player(FaceAnim.HALF_GUILTY, "Roald sent me to check on Drezel.").also { stage++ }
            3 -> sendDialogueLines(
                player!!,
                "${BLUE}(Psst... Hey... Who's Roald? Who's Drezel?)$RED (Uh... isn't Drezel that",
                "${RED}dude upstairs? Oh, wait, Roald's the King of Varrock right?)${BLUE} (He is???",
                "${BLUE}Aw man... Hey, you deal with this okay?) He's just coming! Wait a",
                "${BLUE}second!$RED Hello, my name is Drevil.${BLUE} (Drezel!)$RED I mean Drezel.",
            ).also { stage++ }
            4 -> player(
                FaceAnim.HALF_GUILTY, "Well, as I say, the King sent me to make sure", "everything's okay with you."
            ).also { stage++ }
            5 -> sendDialogue(
                player!!, "${RED}And, uh, what would you do if everything wasn't okay with me?"
            ).also { stage++ }
            6 -> player(FaceAnim.HALF_GUILTY, "I'm not sure. Ask you what help you need I suppose.").also { stage++ }
            7 -> sendDialogueLines(
                player!!,
                "${RED}Ah, good, well, I don't think...${BLUE} (Psst... hey... the dog!)$RED OH! Yes, of",
                "course! Will you do me a favour adventurer?"
            ).also { stage++ }
            8 -> player(FaceAnim.HALF_GUILTY, "Sure. I'm a helpful person!").also { stage++ }
            9 -> sendDialogueLines(
                player!!,
                "${RED}HAHAHAHA! Really? Thanks buddy! You see that mausoleum out",
                "${RED}there? There's a horrible big dog underneath it that I'd like you to",
                "${RED}kill for me! It's been really bugging me! Barking all the time and",
                "${RED}stuff! Please kill it for me buddy!",
            ).also { stage++ }
            10 -> player(FaceAnim.HALF_GUILTY, "Okey-dokey, one dead dog coming up.").also {
                setQuestStage(player!!, Quests.PRIEST_IN_PERIL, 11)
                stage = END_DIALOGUE
            }
            11 -> player(FaceAnim.HALF_GUILTY, "I killed that dog for you.").also { stage++ }
            12 -> sendDialogueLines(
                player!!,
                "${BLUE}HAHAHAHAHA! Really? Hey, that's great!$RED Yeah thanks a lot buddy!",
                "${RED}HAHAHAHAHAHA"
            ).also { stage++ }
            13 -> player(FaceAnim.HALF_ASKING, "What's so funny?").also { stage++ }
            14 -> sendDialogueLines(
                player!!,
                "${BLUE}HAHAHAHA nothing buddy! We're just so grateful to you!",
                "${BLUE}HAHAHA$RED Yeah, maybe you should go tell the King what a great job",
                "${RED}you did buddy! HAHAHA",
            ).also { stage = END_DIALOGUE }
            15 -> {
                end()
                DoorActionHandler.handleDoor(player!!, doors)
            }
        }
    }
}